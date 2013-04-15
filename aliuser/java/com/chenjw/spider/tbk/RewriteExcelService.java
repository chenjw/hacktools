package com.chenjw.spider.tbk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.chenjw.spider.tbk.impl.LinkTransServiceImpl;

public class RewriteExcelService {
	private LinkTransService linkTransService = new LinkTransServiceImpl();

	public void replaceUrl(String path) {
		File f = new File(path);

		InputStream input;
		try {
			input = new FileInputStream(f);
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {

				HSSFSheet st = wb.getSheetAt(i);
				Iterator<Row> rows = st.rowIterator();
				while (rows.hasNext()) {
					HSSFRow row = (HSSFRow) rows.next();
					Iterator<Cell> cells = row.cellIterator();
					while (cells.hasNext()) {
						HSSFCell cell = (HSSFCell) cells.next();

						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							String str = cell.getStringCellValue();

							if (str != null
									&& str.startsWith("http://detail.tmall.com")) {
								String uu = linkTransService.trans(str);
								if (uu != null) {
									cell.setCellValue(uu);
									System.out.println(uu);
								}

							}
						}
					}
				}
			}

			wb.write(new FileOutputStream("/home/chenjw/test/output.xls"));
			// wb.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		RewriteExcelService t = new RewriteExcelService();
		t.replaceUrl("/home/chenjw/test/tm2.xls");
	}
}
