package com.chenjw.spider.dt.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.DecoderException;

public class EncodeTester {
	private int radix;
	private Map<Long, String> checks = new HashMap<Long, String>();
	public final static char[] digits = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };

	// , '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '=',
	// '<', '>', ';', ':', ',', '.', '\'', '\"', '[', ']', '_', '+', '|',
	// '\\', '?', '/', '`', '~' };

	public EncodeTester(int radix) {
		this.radix = radix;
	}

	public static int index(char c) {
		for (int i = 0; i < digits.length; i++) {
			if (c == digits[i]) {
				return i;
			}
		}
		throw new RuntimeException();
	}

	public void train(long l, String s) {

		String a = toString(l);
		if (s.length() != a.length()) {
			throw new RuntimeException("length not equal " + radix);
		}
		char[] cs = a.toCharArray();
		char[] cs1 = s.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			char c = cs[i];
			char c1 = cs1[i];
			int index = index(c);
			int index1 = index(c1);
			digits[index] = c1;
			digits[index1] = c;

		}
		check();
		checks.put(l, s);
		System.out.println(l + " trained! " + toString(l));
	}

	public void check() {
		for (Entry<Long, String> entry : checks.entrySet()) {
			String s = toString(entry.getKey());
			if (!s.equals(entry.getValue())) {
				throw new RuntimeException("want " + entry.getKey() + " "
						+ entry.getValue() + " but was " + s);
			}
		}
	}

	public String toString(long i) {
		return toString(i, radix);
	}

	public static String toString(long i, int radix) {
		char[] buf = new char[165];
		int charPos = 164;
		boolean negative = (i < 0);

		if (!negative) {
			i = -i;
		}

		while (i <= -radix) {
			buf[charPos--] = digits[(int) (-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = digits[(int) (-i)];

		if (negative) {
			buf[--charPos] = '-';
		}

		return new String(buf, charPos, (165 - charPos));
	}

	public static long s(String s) {
		char[] cs = s.toCharArray();
		int l = EncodeTester.digits.length;
		long r = 0;
		for (int i = 0; i < cs.length; i++) {
			char c = cs[i];
			int idx = EncodeTester.index(c);
			r += idx * Math.round(Math.pow(l, (cs.length - i - 1)));
		}
		return r;
	}

	public static void main(String[] args) throws DecoderException {

		for (int i = 0; i < digits.length; i++) {
			try {
				EncodeTester t = new EncodeTester(62);

				// t.train(3529449054118693L, "zcm6thhsx");
				// t.train(3529041673874812L, "zcbvpgg0Y");
				// t.train(3527929143007098L, "zbIz0cChA");
				// t.train(3530829567353879L, "zcW16uR4X");
				// t.train(3530687992576071L, "zcSkLaO9x");
				// t.train(3530485105448989L, "zcN3wmRwV");

				System.out.println("success " + i);
				return;
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}

		}

	}
}
