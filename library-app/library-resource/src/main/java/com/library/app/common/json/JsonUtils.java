package com.library.app.common.json;



import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.app.common.model.PaginatedData;

public class JsonUtils {

	private JsonUtils() {
	}
	
	public static JsonElement getJsonelementWithId(final Long id) {
		final JsonObject idJson = new JsonObject();
		idJson.addProperty("id", id);
		
		return idJson;

	}

	public static <T> JsonElement getJsonElementWithPagingAndEntries(PaginatedData<T> paginatedData, final EntityJsonConverter<T> entityJsonConverter) {
		
	
		JsonObject jsonWithEntriesAndPaging = new JsonObject();
		
		JsonObject jsoPaging  = new JsonObject();
		jsoPaging.addProperty("totalRecords", paginatedData.getNumberOfRows());
		
		jsonWithEntriesAndPaging.add("paging", jsoPaging);
		jsonWithEntriesAndPaging.add("entries", entityJsonConverter.convertToJsonElement(paginatedData.getRows()));
		
		return jsonWithEntriesAndPaging;
	}

}

