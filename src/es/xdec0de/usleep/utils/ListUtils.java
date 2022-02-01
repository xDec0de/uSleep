package es.xdec0de.usleep.utils;

public class ListUtils {

	public static String join(Object[] array, String separator) {
		return join(array, separator, 0, array.length);
	}

	public static String join(Object[] array, String separator, int startIndex, int endIndex) {
		if (separator == null)
			separator = "";

		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0 || array == null)
			return "";

		bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length())
				+ separator.length());

		StringBuffer buf = new StringBuffer(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}
}
