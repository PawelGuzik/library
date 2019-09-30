package com.library.app.author.resource;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.app.author.model.Author;
import com.library.app.category.model.Category;
import com.library.app.common.json.JsonReader;

@ApplicationScoped
public class AuthorJsonConverter {
	
	public Author convertFrom(String json) {
		JsonObject jsonObject = JsonReader.readAsJsonObject(json);
		
		final Author author = new Author();
		author.setName(JsonReader.getStringOrNull(jsonObject, "name"));

		return author;
	}
	
	public JsonElement convertToJsonElement(final Author author) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", author.getId());
		jsonObject.addProperty("name", author.getName());
		return jsonObject;
	}
	public JsonElement convertToJsonElement(final List<Author> authors) {
		final JsonArray jsonArray = new JsonArray();

		for (final Author author : authors) {
			jsonArray.add(convertToJsonElement(author));
		}

		return jsonArray;
	}
}
