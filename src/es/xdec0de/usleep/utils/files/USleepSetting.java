package es.xdec0de.usleep.utils.files;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.util.Vector;

public interface USleepSetting {

	/**
	 * Gets the corresponding <b>config.yml</b>'s getPath() of the setting.
	 * 
	 * @return The getPath() to the setting.
	 */
	public String getPath();

	/**
	 * Gets the setting as a string.
	 * 
	 * @return The setting as a string or an empty string if {@link #getPath()} is invalid or the setting is not a String.
	 */
	default public String asString() {
		return USPConfig.get().getString(getPath(), "");
	}

	/**
	 * Gets the setting as a string list.
	 * 
	 * @return The setting as a string list or an empty string list if {@link #getPath()} is invalid.
	 */
	default public List<String> asStringList() {
		return USPConfig.get().getStringList(getPath());
	}

	/**
	 * Gets the setting as a short.
	 * 
	 * @return The setting as a short or 0 if {@link #getPath()} is invalid or the setting is not a number.
	 */
	default public short asByte() {
		try {
			return Byte.parseByte(asString());
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	/**
	 * Gets the setting as a short.
	 * 
	 * @return The setting as a short or 0 if {@link #getPath()} is invalid or the setting is not a number.
	 */
	default public short asShort() {
		try {
			return Short.parseShort(asString());
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	/**
	 * Gets the setting as an integer.
	 * 
	 * @return The setting as an integer or 0 if {@link #getPath()} is invalid or the setting is not a number.
	 */
	default public int asInt() {
		return USPConfig.get().getInt(getPath(), 0);
	}

	/**
	 * Gets the setting as a long.
	 * 
	 * @return The setting as a long or 0 if {@link #getPath()} is invalid or the setting is not a number.
	 */
	default public long asLong() {
		return USPConfig.get().getLong(getPath(), 0);
	}

	/**
	 * Gets the setting as a double.
	 * 
	 * @return The setting as a double or 0 if {@link #getPath()} is invalid or the setting is not a number.
	 */
	default public double asDouble() {
		return USPConfig.get().getDouble(getPath(), 0);
	}

	/**
	 * Gets the setting as a float.
	 * 
	 * @return The setting as a float or 0 if {@link #getPath()} is invalid or the setting is not a number.
	 */
	default public float asFloat() {
		try {
			return Float.parseFloat(asString());
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	/**
	 * Gets the setting as a boolean.
	 * 
	 * @return The setting as a boolean or <b>false</b> if {@link #getPath()} is invalid or the setting is not a boolean.
	 */
	default public boolean asBoolean() {
		return USPConfig.get().getBoolean(getPath(), false);
	}

	/**
	 * Gets the setting as a {@link Color}
	 * 
	 * @return The setting as a color or <b>null</b> if {@link #getPath()} is invalid or the setting is not a color.
	 */
	default public Color asColor() {
		return USPConfig.get().getColor(getPath(), null);
	}

	/**
	 * Gets the setting as a {@link Vector}.
	 * 
	 * @return The setting as a vector or <b>null</b> if {@link #getPath()} is invalid or the setting is not a vector.
	 */
	default public Vector asVector() {
		return USPConfig.get().getVector(getPath(), null);
	}

	/**
	 * Sets {@link #getPath()} to the specified value, without saving, if value is null, the entry will be removed.
	 * 
	 * @param value New value to set the getPath() to.
	 */
	default public void set(Object value) {
		USPConfig.get().set(getPath(), value);
	}

	/**
	 * Sets {@link #getPath()} to the specified value, then, saves and reloads <b>config.yml</b>, if value is null, the entry will be removed.
	 * 
	 * @param value New value to set the getPath() to.
	 */
	default public void setReload(Object value) {
		USPConfig.get().set(getPath(), value);
		USPConfig.save();
		USPConfig.reload(false, false);
	}

}
