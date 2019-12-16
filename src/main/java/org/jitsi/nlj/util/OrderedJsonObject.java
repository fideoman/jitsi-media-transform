package org.jitsi.nlj.util;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONAware;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

public class OrderedJsonObject extends LinkedHashMap implements Map, JSONAware, JSONStreamAware {

	private static final long serialVersionUID = -503443796854799292L;

	public OrderedJsonObject() {
		super();
	}

	public OrderedJsonObject(Map map) {
		super(map);
	}

	public static void writeJSONString(Map map, Writer out) throws IOException {
		if (map == null) {
			out.write("null");
			return;
		}

		boolean first = true;
		Iterator iter = map.entrySet().iterator();

		out.write('{');
		while (iter.hasNext()) {
			if (first)
				first = false;
			else
				out.write(',');
			Map.Entry entry = (Map.Entry) iter.next();
			out.write('\"');
			out.write(escape(String.valueOf(entry.getKey())));
			out.write('\"');
			out.write(':');
			JSONValue.writeJSONString(entry.getValue(), out);
		}
		out.write('}');
	}

	public void writeJSONString(Writer out) throws IOException {
		writeJSONString(this, out);
	}

	public static String toJSONString(Map map) {
		if (map == null)
			return "null";

		StringBuffer sb = new StringBuffer();
		boolean first = true;
		Iterator iter = map.entrySet().iterator();

		sb.append('{');
		while (iter.hasNext()) {
			if (first)
				first = false;
			else
				sb.append(',');

			Map.Entry entry = (Map.Entry) iter.next();
			toJSONString(String.valueOf(entry.getKey()), entry.getValue(), sb);
		}
		sb.append('}');
		return sb.toString();
	}

	public String toJSONString() {
		return toJSONString(this);
	}

	private static String toJSONString(String s, Object value, StringBuffer sb) {
		sb.append('\"');
		if (s == null)
			sb.append("null");
		else
			for (int i = 0; i < s.length(); i++) {
				char ch = s.charAt(i);
				switch (ch) {
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				case '/':
					sb.append("\\/");
					break;
				default:
					if ((ch >= '\000' && ch <= '\037') || (ch >= '' && ch <= '') || (ch >= ' ' && ch <= '⃿')) {
						String ss = Integer.toHexString(ch);
						sb.append("\\u");
						for (int k = 0; k < 4 - ss.length(); k++)
							sb.append('0');
						sb.append(ss.toUpperCase());
						break;
					}
					sb.append(ch);
					break;
				}
			}
		sb.append('\"').append(':');

		sb.append(JSONValue.toJSONString(value));

		return sb.toString();
	}

	public String toString() {
		return toJSONString();
	}

	public static String toString(String key, Object value) {
		StringBuffer sb = new StringBuffer();
		toJSONString(key, value, sb);
		return sb.toString();
	}

	public static String escape(String s) {
		return JSONValue.escape(s);
	}
}
