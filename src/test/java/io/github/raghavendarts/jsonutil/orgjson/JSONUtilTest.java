package io.github.raghavendarts.jsonutil.orgjson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import io.github.raghavendarts.jsonutil.orgjson.JSONUtil;

public class JSONUtilTest {

	@Test
	public void testAddIfNotNull() {
		JSONArray arr = new JSONArray();
		JSONUtil.addIfNotNull(arr, "value");
		assertEquals(1, arr.length());

		JSONUtil.addIfNotNull(arr, null);
		assertEquals(1, arr.length()); // no change
	}

	@Test
	public void testGetJSONArrayInitializeIfNull() {
		JSONArray arr = null;
		arr = JSONUtil.getJSONArrayInitializeIfNull(arr);
		assertNotNull(arr);
		assertEquals(0, arr.length());
	}

	@Test
	public void testMergeIfNotNull() {
		JSONArray arr1 = new JSONArray().put("a");
		JSONArray arr2 = new JSONArray().put("b").put("c");

		JSONUtil.mergeIfNotNull(arr1, arr2);
		assertEquals(3, arr1.length());
		assertEquals("c", arr1.get(2));

		JSONUtil.mergeIfNotNull(arr1, null);
		assertEquals(3, arr1.length()); // no change
	}

	@Test
	public void testExpand() {
		JSONObject obj = new JSONObject();
		JSONUtil.expand(obj, "a.b.c", "value");
		assertEquals("value", obj.getJSONObject("a").getJSONObject("b").getString("c"));
	}

	@Test
	public void testGetBoolean() {
		JSONObject obj = new JSONObject().put("flag", true).put("nested", new JSONObject().put("flag", false));

		assertTrue(JSONUtil.getBoolean(obj, "flag"));
		assertFalse(JSONUtil.getBoolean(obj, "nested.flag"));
		assertNull(JSONUtil.getBoolean(obj, "missing"));
		assertTrue(JSONUtil.getBoolean(obj, "missing", true)); // default value
	}

	@Test
	public void testGetInteger() {
		JSONObject obj = new JSONObject().put("num", 42).put("nested", new JSONObject().put("num", 99));

		assertEquals(Integer.valueOf(42), JSONUtil.getInteger(obj, "num"));
		assertEquals(Integer.valueOf(99), JSONUtil.getInteger(obj, "nested.num"));
		assertNull(JSONUtil.getInteger(obj, "missing"));
		assertEquals(Integer.valueOf(7), JSONUtil.getInteger(obj, "missing", 7));
	}

	@Test
	public void testGetDoubleAndFloat() {
		JSONObject obj = new JSONObject().put("doubleVal", 3.14).put("floatVal", 2.5f).put("intVal", 10);

		assertEquals(Double.valueOf(3.14), JSONUtil.getDouble(obj, "doubleVal"));
		assertEquals(Float.valueOf(2.5f), JSONUtil.getFloat(obj, "floatVal"));
		assertEquals(Float.valueOf(10f), JSONUtil.getFloat(obj, "intVal")); // int → float
		assertEquals(Float.valueOf(1.23f), JSONUtil.getFloat(obj, "missing", 1.23f));
	}

	@Test
	public void testGetLong() {
		JSONObject obj = new JSONObject().put("longVal", 10000000000L).put("intVal", 123);

		assertEquals(Long.valueOf(10000000000L), JSONUtil.getLong(obj, "longVal"));
		assertEquals(Long.valueOf(123L), JSONUtil.getLong(obj, "intVal")); // int → long
	}

	@Test
	public void testGetJSONArrayAndJSONObject() {
		JSONObject nested = new JSONObject().put("key", "value");
		JSONObject obj = new JSONObject().put("arr", new JSONArray().put("a").put("b")).put("nested", nested);

		JSONArray arr = JSONUtil.getJSONArray(obj, "arr");
		assertEquals(2, arr.length());

		JSONObject nestedObj = JSONUtil.getJSONObject(obj, "nested");
		assertEquals("value", nestedObj.getString("key"));
	}

	@Test
	public void testGetString() {
		JSONObject obj = new JSONObject().put("name", "John");

		assertEquals("John", JSONUtil.getString(obj, "name"));
		assertNull(JSONUtil.getString(obj, "missing"));
		assertEquals("default", JSONUtil.getString(obj, "missing", "default"));
	}

	@Test
	public void testIsJSONArrayNullOrEmpty() {
		JSONObject obj = new JSONObject().put("arr", new JSONArray().put("x"));
		JSONObject emptyObj = new JSONObject().put("arr", new JSONArray());

		assertFalse(JSONUtil.isJSONArrayNullOrEmpty(obj, "arr"));
		assertTrue(JSONUtil.isJSONArrayNullOrEmpty(emptyObj, "arr"));
		assertTrue(JSONUtil.isJSONArrayNullOrEmpty(new JSONObject(), "missing"));
	}

	@Test
	public void testIsNullAndIsNotNull() {
		JSONObject obj = new JSONObject().put("name", "John");

		assertTrue(JSONUtil.isNotNull(obj, "name"));
		assertTrue(JSONUtil.isNull(obj, "missing"));
	}

	@Test
	public void testRemove() {
		JSONObject obj = new JSONObject().put("a", new JSONObject().put("b", "c"));
		JSONUtil.remove(obj, "a.b");

		assertFalse(obj.getJSONObject("a").has("b"));
	}

	@Test
	public void testContains() {
		JSONArray arr = new JSONArray().put("a").put("b").put("c");
		assertTrue(JSONUtil.contains(arr, "b"));
		assertFalse(JSONUtil.contains(arr, "z"));
	}
}
