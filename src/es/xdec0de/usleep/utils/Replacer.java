package es.xdec0de.usleep.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a replacer to replace parts of a string with other string, if you want to use the same replacements for multiple strings, you should 
 * create a replacer variable and apply it to as many strings as you want to <b>avoid creating multiple instances of the same replacements</b>, also, 
 * make sure that the amount of strings added to the replacer are <b>even</b>, otherwise, an exception will be thrown!<br><br>
 * 
 * The default replacer stands for a replacer with the %prefix% and %error% replaces added to it.
 * 
 * @see Replacer#Replacer(String...)
 */
public class Replacer {

	private final LinkedList<String> replaceList = new LinkedList<String>();

	/**
	 * Creates a replacer to replace parts of a string with other string, if you want to use the same replacements for multiple strings, you should 
	 * create a replacer variable and apply it to as many strings as you want to <b>avoid creating multiple instances of the same replacements</b>, also, 
	 * make sure that the amount of strings added to the replacer are <b>even</b>, otherwise, an exception will be thrown!
	 * 
	 * @param replaces The strings to be replaced, the format is "%placeholder1%", "replace1", "%placeholder2%", "replace2"...
	 * 
	 * @see #replaceAt(String)
	 * @see #replaceAt(List)
	 */
	public Replacer(String... replaces) {
		replaceList.addAll(Arrays.asList(replaces));
		if(replaceList.size() % 2 != 0)
			throw new IllegalArgumentException(replaceList.get(replaceList.size() - 1) + "does not have a replacer! Add one more element to the replacer.");
	}

	/**
	 * Adds new strings to an existing replacer, the amount of strings must also be even, note that existing replacements won't be ignored 
	 * and the new replacer won't overwrite them.
	 * 
	 * @param replaces The new strings to be replaced, the format is "%placeholder1%", "replace1", "%placeholder2%", "replace2"...
	 * 
	 * @return The old replacer with the new replacements added to it.
	 * 
	 * @throws IllegalArgumentException if the amount of strings is not even, more technically, if replaces size % 2 is not equal to 0.
	 */
	public Replacer add(String... replaces) {
		if(replaces.length % 2 != 0)
			throw new IllegalArgumentException(replaces[replaces.length -1] + "does not have a replacer! Add one more element to the replacer.");
		replaceList.addAll(Arrays.asList(replaces));
		return this;
	}

	/**
	 * Adds a replacer to an existing replacer, joining them, note that existing replacements won't be ignored and the new replacer won't overwrite them.
	 * 
	 * @param replacer The replacer to add.
	 * 
	 * @return The old replacer with the new replacements added to it.
	 */
	public Replacer add(Replacer replacer) {
		replaceList.addAll(replacer.replaceList);
		return this;
	}

	/**
	 * Applies the replacements to the specified string, it the string is null, "null" will be returned.
	 * 
	 * @param str The string to apply the replacements to.
	 * 
	 * @return A new string with the replacements applied to it.
	 */
	public String replaceAt(String str) {
		if(str == null)
			return "null";
		String res = str;
		for(int i = 0; i <= replaceList.size() - 1; i += 2)
			res = res.replace(replaceList.get(i), replaceList.get(i + 1));
		return res;
	}

	/**
	 * Applies the replacements to the specified list of strings, it the list is null, null will be returned.
	 * 
	 * @param str The string list to apply the replacements to.
	 * 
	 * @return A new string list with the replacements applied to it.
	 */
	public List<String> replaceAt(List<String> list) {
		if(list == null)
			return null;
		List<String> res = new LinkedList<String>();
		list.forEach(str -> res.add(replaceAt(str)));
		return res;
	}
}
