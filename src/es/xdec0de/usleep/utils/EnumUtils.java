package es.xdec0de.usleep.utils;

public class EnumUtils {

	public static <E extends Enum<E>> Enum<E> getEnum(Class<E> enumClass, String str) {
		if(str == null)
			return null;
		try {
			return Enum.valueOf(enumClass, str);
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	public static <E extends Enum<E>> Enum<?> ofOther(Class<E> enumTo, Enum<?> enumFrom) {
		return getEnum(enumTo, enumFrom.name());
	}
}
