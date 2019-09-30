package com.library.app.commontests.utils;

import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONException;
import org.junit.Ignore;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.google.gson.JsonObject;
import com.library.app.common.json.JsonReader;

@Ignore
public class JsonTestsUtils {
	public static final String BASE_JSON_DIR = "json/";
	
	private JsonTestsUtils() {
	}
	
	public static String readJsonFile(String relativePath) {
		InputStream is = JsonTestsUtils.class.getClassLoader().getResourceAsStream(BASE_JSON_DIR + relativePath);
		try(Scanner s = new Scanner(is)){
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}
	
	public static void assertJsonMatchesFileContent(String actualJson, String fileNameWithExpectedJson) {
		assertJsonMatchesExpectedJson(actualJson, readJsonFile(fileNameWithExpectedJson));
	}
	
	public static void assertJsonMatchesExpectedJson(String actualJson, String expectedJson) {
		try {
			JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
		} catch(JSONException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static Long getIdFromJson(final String json) {
		final JsonObject  jsonObject = JsonReader.readAsJsonObject(json);
		return JsonReader.getLongOrNull(jsonObject, "id");
	}
	
}
