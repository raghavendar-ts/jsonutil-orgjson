package io.github.raghavendarts.jsonutil.orgjson;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil {

	public static void addIfNotNull(JSONArray array, Object object) {
		if (object == null) {
			return;
		}
		array.put(object);
	}

	public static JSONArray getJSONArrayInitializeIfNull(JSONArray array) {
		if (array == null) {
			array = new JSONArray();
		}
		return array;
	}

	public static void mergeIfNotNull(JSONArray array1, JSONArray array2) {
		if (array2 == null || array2.isEmpty()) {
			return;
		}
		for (int i = 0; i < array2.length(); i++) {
			array1.put(array2.get(i));
		}
	}

	public static void addIfNotNullAndArrayIsEmpty(JSONArray array, Object object) {
		if (!array.isEmpty()) {
			return;
		}
		addIfNotNull(array, object);
	}

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

	public static Boolean getBoolean(JSONObject object, String dotSeparatedKey, boolean defaultValue) {
		Boolean value = getBoolean(object, dotSeparatedKey);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

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

	public static Float getFloat(JSONObject object, String dotSeparatedKey, float defaultValue) {
		Float value = getFloat(object, dotSeparatedKey);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

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

	public static Integer getInteger(JSONObject object, String dotSeparatedKey, Integer defaultValue) {
		Integer value = getInteger(object, dotSeparatedKey);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

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

	public static String getString(JSONObject object, String dotSeparatedKey) {
		return getString(object, dotSeparatedKey, null);
	}

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

	public static boolean isJSONArrayNullOrEmpty(JSONObject object, String dotSeparatedKey) {
		JSONArray value = getJSONArray(object, dotSeparatedKey);
		if (value == null) {
			return true;
		}
		return (value.equals(null) || value.isEmpty());
	}

	public static boolean isNotNull(JSONObject object, String dotSeparatedKey) {
		return !isNull(object, dotSeparatedKey);
	}

	public static boolean isNull(JSONObject object, String dotSeparatedKey) {
		Object value = getObject(object, dotSeparatedKey);
		if (value == null) {
			return true;
		}
		return value.equals(null);
	}

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

	public static Boolean contains(JSONArray jsonArray, Object value) {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (jsonArray.get(i).equals(value)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		JSONObject object = new JSONObject();
	}

}
