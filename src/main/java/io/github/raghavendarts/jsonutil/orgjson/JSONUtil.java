package io.github.raghavendarts.jsonutil.orgjson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility helpers around org.json {@link JSONObject} / {@link JSONArray}.
 * <p>
 * <strong>Note:</strong> Uses dot-separated paths (e.g., {@code a.b.c}) for nested lookups.
 * Methods return wrapper types and may yield {@code null} when absent or type-mismatch.
 * Overloads with {@code defaultValue} return the provided fallback when needed.
 * </p>
 * <p>Some methods (e.g., {@link #expand}) mutate the provided object to create intermediates.</p>
 * <p>Relies on a {@code Constants} helper for tokens like {@code BACKSLASH}, {@code DOT},
 * {@code SOURCE_FIELD}, and {@code TARGET_FIELD}.</p>
 */

public class JSONUtil {

	/**
	 * Add {@code object} to {@code array} if the object is non-null.
	 * @param array destination array (must not be {@code null})
	 * @param object value to add when non-null
	 */

	public static void addIfNotNull(JSONArray array, Object object) {
		if (object == null) {
			return;
		}
		array.put(object);
	}

	/**
	 * Return the provided {@code array} or a new empty {@link JSONArray} if it is {@code null}.
	 * @param array input array (nullable)
	 * @return {@code array} if non-null; otherwise a new empty {@link JSONArray}
	 */

	public static JSONArray getJSONArrayInitializeIfNull(JSONArray array) {
		if (array == null) {
			array = new JSONArray();
		}
		return array;
	}

	/**
	 * Append all values from {@code array2} into {@code array1} when {@code array2} is non-null and not empty.
	 * @param array1 destination array (must not be {@code null})
	 * @param array2 source array to merge from (nullable)
	 */

	public static void mergeIfNotNull(JSONArray array1, JSONArray array2) {
		if (array2 == null || array2.isEmpty()) {
			return;
		}
		for (int i = 0; i < array2.length(); i++) {
			array1.put(array2.get(i));
		}
	}

	/**
	 * Add {@code object} to {@code array} only if the array is currently empty and the object is non-null.
	 * @param array destination array (must not be {@code null})
	 * @param object value to add if non-null and array is empty
	 */

	public static void addIfNotNullAndArrayIsEmpty(JSONArray array, Object object) {
		if (!array.isEmpty()) {
			return;
		}
		addIfNotNull(array, object);
	}

	/**
	 * Copy values from {@code source} to {@code target} based on mapping entries in {@code fields}.
	 * Each mapping object should contain {@code SOURCE_FIELD} and {@code TARGET_FIELD}.
	 * Uses {@link #getObject(JSONObject, String)} and {@link #expand(JSONObject, String, Object)}.
	 * @param source source object (nullable)
	 * @param target target object to receive values (must not be {@code null})
	 * @param fields array of mapping objects (must not be {@code null})
	 */

	public static void copyValues(JSONObject source, JSONObject target, JSONArray fields) {
		for (Object fieldObj : fields) {
			JSONObject field = (JSONObject) fieldObj;
			String sourceField = field.getString(Constants.SOURCE_FIELD);
			String targetField = field.getString(Constants.TARGET_FIELD);
			Object value = JSONUtil.getObject(source, sourceField);

			if (targetField != null && value != null) {
				expand(target, targetField, value);
			}
		}
	}

	/**
	 * Ensure the nested path {@code dotSeparatedKey} exists in {@code source} and set the terminal key to {@code value}.
	 * Creates intermediate objects as needed. Mutates {@code source}.
	 * @param source object to modify (must not be {@code null})
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @param value value to assign at the terminal key
	 * @return the same {@code source} object for chaining
	 */

	public static JSONObject expand(JSONObject source, String dotSeparatedKey, Object value) {
		String keys[] = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		for (int i = 0; i < keys.length - 1; i++) {
			if (!source.has(keys[i]) || source.isNull(keys[i])) {
				source.put(keys[i], new JSONObject());
			}
			source = source.getJSONObject(keys[i]);
		}
		source.put(keys[keys.length - 1], value);
		return source;
	}

	/**
	 * Retrieve a {@link Boolean} at a dot-separated path, or {@code null} when absent or not a {@code Boolean}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return {@link Boolean} value or {@code null}
	 */

	public static Boolean getBoolean(JSONObject object, String dotSeparatedKey) {
		Boolean value = null;
		String[] keys = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		if (object != null) {
			if (keys.length == 1) {
				if (!object.isNull(dotSeparatedKey) && object.get(dotSeparatedKey) instanceof Boolean) {
					value = object.getBoolean(dotSeparatedKey);
				}
			} else {
				boolean fetchValue = true;
				for (int i = 0; i < keys.length - 1; i++) {
					String key = keys[i];
					if (object.isNull(key) || !(object.get(key) instanceof JSONObject)) {
						fetchValue = false;
						break;
					}
					object = object.getJSONObject(key);
				}
				if (object != null && fetchValue) {
					String key = keys[keys.length - 1];
					if (!object.isNull(key) && object.get(key) instanceof Boolean) {
						value = object.optBoolean(key);
					}
				}
			}
		}
		return value;
	}

	/**
	 * Retrieve a {@link Boolean} at a dot-separated path; if {@code null}, return {@code defaultValue}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @param defaultValue value to return when the lookup is {@code null}
	 * @return resolved value or {@code defaultValue}
	 */

	public static Boolean getBoolean(JSONObject object, String dotSeparatedKey, boolean defaultValue) {
		Boolean value = getBoolean(object, dotSeparatedKey);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Retrieve a {@link Double} at a dot-separated path, or {@code null} when absent or not a {@code Double}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return {@link Double} value or {@code null}
	 */

	public static Double getDouble(JSONObject object, String dotSeparatedKey) {
		Double value = null;
		String[] keys = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		if (object != null) {
			if (keys.length == 1) {
				if (!object.isNull(dotSeparatedKey) && object.get(dotSeparatedKey) instanceof Double) {
					value = object.getDouble(dotSeparatedKey);
				}
			} else {
				boolean fetchValue = true;
				for (int i = 0; i < keys.length - 1; i++) {
					String key = keys[i];
					if (object.isNull(key) || !(object.get(key) instanceof JSONObject)) {
						fetchValue = false;
						break;
					}
					object = object.getJSONObject(key);
				}
				if (object != null && fetchValue) {
					String key = keys[keys.length - 1];
					if (!object.isNull(key) && object.get(key) instanceof Double) {
						value = object.optDouble(key);
					}
				}
			}
		}
		return value;
	}

	/**
	 * Retrieve a {@link Float} at a dot-separated path. Accepts stored {@code Float}, {@code Integer}, or {@code Double}.
	 * Returns {@code null} if absent or incompatible.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return {@link Float} value or {@code null}
	 */

	public static Float getFloat(JSONObject object, String dotSeparatedKey) {
		Float value = null;
		String[] keys = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		if (object != null) {
			if (keys.length == 1) {
				if (!object.isNull(dotSeparatedKey)) {
					if (object.get(dotSeparatedKey) instanceof Float) {
						value = object.getFloat(dotSeparatedKey);
					} else if (object.get(dotSeparatedKey) instanceof Integer) {
						value = (float) object.getInt(dotSeparatedKey);
					} else if (object.get(dotSeparatedKey) instanceof Double) {
						value = object.getFloat(dotSeparatedKey);
					}
				}
			} else {
				boolean fetchValue = true;
				for (int i = 0; i < keys.length - 1; i++) {
					String key = keys[i];
					if (object.isNull(key) || !(object.get(key) instanceof JSONObject)) {
						fetchValue = false;
						break;
					}
					object = object.getJSONObject(key);
				}
				if (object != null && fetchValue) {
					String key = keys[keys.length - 1];
					if (!object.isNull(key)) {
						if (object.get(key) instanceof Float) {
							value = object.optFloat(key);
						} else if (object.get(key) instanceof Integer) {
							value = (float) object.getInt(key);
						} else if (object.get(key) instanceof Double) {
							value = object.getFloat(key);
						}
					}
				}
			}
		}
		return value;
	}

	/**
	 * Retrieve a {@link Float} via {@link #getFloat(JSONObject, String)}; when {@code null}, return {@code defaultValue}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @param defaultValue value to use when lookup yields {@code null}
	 * @return resolved value or {@code defaultValue}
	 */

	public static Float getFloat(JSONObject object, String dotSeparatedKey, float defaultValue) {
		Float value = getFloat(object, dotSeparatedKey);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Retrieve an {@link Integer} at a dot-separated path, or {@code null} when absent or not an {@code Integer}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return {@link Integer} value or {@code null}
	 */

	public static Integer getInteger(JSONObject object, String dotSeparatedKey) {
		Integer value = null;
		String[] keys = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		if (object != null) {
			if (keys.length == 1) {
				if (!object.isNull(dotSeparatedKey) && object.get(dotSeparatedKey) instanceof Integer) {
					value = object.getInt(dotSeparatedKey);
				}
			} else {
				boolean fetchValue = true;
				for (int i = 0; i < keys.length - 1; i++) {
					String key = keys[i];
					if (object.isNull(key) || !(object.get(key) instanceof JSONObject)) {
						fetchValue = false;
						break;
					}
					object = object.getJSONObject(key);
				}
				if (object != null && fetchValue) {
					String key = keys[keys.length - 1];
					if (!object.isNull(key) && object.get(key) instanceof Integer) {
						value = object.optInt(key);
					}
				}
			}
		}
		return value;
	}

	/**
	 * Retrieve an {@link Integer}; if the lookup is {@code null}, return {@code defaultValue}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @param defaultValue value to return when lookup is {@code null}
	 * @return resolved value or {@code defaultValue}
	 */

	public static Integer getInteger(JSONObject object, String dotSeparatedKey, Integer defaultValue) {
		Integer value = getInteger(object, dotSeparatedKey);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Retrieve a {@link JSONArray} at a dot-separated path, or {@code null} when absent or not a {@code JSONArray}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return {@link JSONArray} value or {@code null}
	 */

	public static JSONArray getJSONArray(JSONObject object, String dotSeparatedKey) {
		JSONArray value = null;
		String[] keys = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		if (object != null) {
			if (keys.length == 1) {
				if (!object.isNull(dotSeparatedKey) && object.get(dotSeparatedKey) instanceof JSONArray) {
					value = object.getJSONArray(dotSeparatedKey);
				}
			} else {
				boolean fetchValue = true;
				for (int i = 0; i < keys.length - 1; i++) {
					String key = keys[i];
					if (object.isNull(key) || !(object.get(key) instanceof JSONObject)) {
						fetchValue = false;
						break;
					}
					object = object.getJSONObject(key);
				}
				if (object != null && fetchValue) {
					String key = keys[keys.length - 1];
					if (!object.isNull(key) && object.get(key) instanceof JSONArray) {
						value = object.optJSONArray(key);
					}
				}
			}
		}
		return value;
	}

	/**
	 * Retrieve a {@link JSONObject} at a dot-separated path, or {@code null} when absent or not a {@code JSONObject}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return {@link JSONObject} value or {@code null}
	 */

	public static JSONObject getJSONObject(JSONObject object, String dotSeparatedKey) {
		JSONObject value = null;
		String[] keys = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		if (object != null) {
			if (keys.length == 1) {
				if (!object.isNull(dotSeparatedKey) && object.get(dotSeparatedKey) instanceof JSONObject) {
					value = object.getJSONObject(dotSeparatedKey);
				}
			} else {
				boolean fetchValue = true;
				for (int i = 0; i < keys.length - 1; i++) {
					String key = keys[i];
					if (object.isNull(key) || !(object.get(key) instanceof JSONObject)) {
						fetchValue = false;
						break;
					}
					object = object.getJSONObject(key);
				}
				if (object != null && fetchValue) {
					String key = keys[keys.length - 1];
					if (!object.isNull(key) && object.get(key) instanceof JSONObject) {
						value = object.optJSONObject(key);
					}
				}
			}
		}
		return value;
	}

	/**
	 * Retrieve a {@link Long} at a dot-separated path. Accepts stored {@code Long} or {@code Integer}.
	 * Returns {@code null} if absent or incompatible.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return {@link Long} value or {@code null}
	 */

	public static Long getLong(JSONObject object, String dotSeparatedKey) {
		Long value = null;
		String[] keys = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		if (object != null) {
			if (keys.length == 1) {
				if (!object.isNull(dotSeparatedKey) && object.get(dotSeparatedKey) instanceof Long) {
					value = object.optLong(dotSeparatedKey);
				} else if (!object.isNull(dotSeparatedKey) && object.get(dotSeparatedKey) instanceof Integer) {
					value = Long.valueOf(object.optInt(dotSeparatedKey));
				}
			} else {
				boolean fetchValue = true;
				for (int i = 0; i < keys.length - 1; i++) {
					String key = keys[i];
					if (object.isNull(key) || !(object.get(key) instanceof JSONObject)) {
						fetchValue = false;
						break;
					}
					object = object.getJSONObject(key);
				}
				if (object != null && fetchValue) {
					String key = keys[keys.length - 1];
					if (!object.isNull(key) && object.get(key) instanceof Integer) {
						value = object.optLong(key);
					}
				}
			}
		}
		return value;
	}

	/**
	 * Retrieve a raw {@link Object} at a dot-separated path, or {@code null} when absent.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return the resolved object or {@code null}
	 */

	public static Object getObject(JSONObject object, String dotSeparatedKey) {
		Object value = null;
		String[] keys = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		if (object != null) {
			if (keys.length == 1) {
				if (!object.isNull(dotSeparatedKey)) {
					value = object.opt(dotSeparatedKey);
				}
			} else {
				boolean fetchValue = true;
				for (int i = 0; i < keys.length - 1; i++) {
					String key = keys[i];
					if (object.isNull(key) || !(object.get(key) instanceof JSONObject)) {
						fetchValue = false;
						break;
					}
					object = object.getJSONObject(key);
				}
				if (object != null && fetchValue) {
					String key = keys[keys.length - 1];
					if (!object.isNull(key)) {
						value = object.opt(key);
					}
				}
			}
		}
		return value;
	}

	/**
	 * Convenience overload of {@link #getString(JSONObject, String, String)} returning {@code null} when absent.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return string value or {@code null}
	 */

	public static String getString(JSONObject object, String dotSeparatedKey) {
		return getString(object, dotSeparatedKey, null);
	}

	/**
	 * Retrieve a {@link String} at a dot-separated path; if {@code null}, return {@code defaultValue}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @param defaultValue value to use when lookup yields {@code null}
	 * @return resolved value or {@code defaultValue}
	 */

	public static String getString(JSONObject object, String dotSeparatedKey, String defaultValue) {
		String value = null;
		String[] keys = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		if (object != null) {
			if (keys.length == 1) {
				if (!object.isNull(dotSeparatedKey) && object.get(dotSeparatedKey) instanceof String) {
					value = object.getString(dotSeparatedKey);
				}
			} else {
				boolean fetchValue = true;
				for (int i = 0; i < keys.length - 1; i++) {
					String key = keys[i];
					if (object.isNull(key) || !(object.get(key) instanceof JSONObject)) {
						fetchValue = false;
						break;
					}
					object = object.getJSONObject(key);
				}
				if (object != null && fetchValue) {
					String key = keys[keys.length - 1];
					if (!object.isNull(key) && object.get(key) instanceof String) {
						value = object.optString(key);
					}
				}
			}
		}
		if (value == null && defaultValue != null) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Returns {@code true} if the array at {@code dotSeparatedKey} is missing, {@code null}, or empty.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return whether the located array is null or empty
	 */

	public static boolean isJSONArrayNullOrEmpty(JSONObject object, String dotSeparatedKey) {
		JSONArray value = getJSONArray(object, dotSeparatedKey);
		if (value == null) {
			return true;
		}
		return (value.equals(null) || value.isEmpty());
	}

	/**
	 * Returns logical negation of {@link #isNull(JSONObject, String)}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return {@code true} if the value exists and is not {@code null}
	 */

	public static boolean isNotNull(JSONObject object, String dotSeparatedKey) {
		return !isNull(object, dotSeparatedKey);
	}

	/**
	 * Returns {@code true} if the value at {@code dotSeparatedKey} is absent or explicitly {@code null}.
	 * @param object source object (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 * @return {@code true} when missing or explicitly null
	 */

	public static boolean isNull(JSONObject object, String dotSeparatedKey) {
		Object value = getObject(object, dotSeparatedKey);
		if (value == null) {
			return true;
		}
		return value.equals(null);
	}

	/**
	 * Remove the terminal key from the nested path if it exists.
	 * Traverses nested objects but does not create intermediate objects.
	 * @param object object to modify (nullable)
	 * @param dotSeparatedKey path like {@code a.b.c}
	 */

	public static void remove(JSONObject object, String dotSeparatedKey) {
		String[] keys = dotSeparatedKey.split(Constants.BACKSLASH + Constants.DOT);
		if (object != null) {
			if (keys.length == 1) {
				if (object.has(dotSeparatedKey)) {
					object.remove(dotSeparatedKey);
				}
			} else {
				boolean isDeleteKey = true;
				for (int i = 0; i < keys.length - 1; i++) {
					String key = keys[i];
					if (object.isNull(key) || !(object.get(key) instanceof JSONObject)) {
						isDeleteKey = false;
						break;
					}
					object = object.getJSONObject(key);
				}
				if (object != null && isDeleteKey) {
					String key = keys[keys.length - 1];
					if (object.has(key)) {
						object.remove(key);
					}
				}
			}
		}
	}

	/**
	 * Returns {@code true} if {@code jsonArray} contains an element equal to {@code value}.
	 * Linear scan using {@link JSONArray#get(int)} and {@link Object#equals(Object)}.
	 * @param jsonArray array to search (must not be {@code null})
	 * @param value value to find (nullable)
	 * @return {@code true} if found; otherwise {@code false}
	 */

	public static Boolean contains(JSONArray jsonArray, Object value) {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (jsonArray.get(i).equals(value)) {
				return true;
			}
		}
		return false;
	}
}