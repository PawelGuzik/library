package com.library.app.category.resource;
import static org.hamcrest.CoreMatchers.*;
import static com.library.app.category.commontests.category.CategoryForTestsRepository.*;
import static com.library.app.commontests.utils.FileTestNameUtils.*;
import static com.library.app.commontests.utils.JsonTestUtils.*;
import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.resource.CategoryJsonConverter;
import com.library.app.category.resource.CategoryResource;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.HttpCode;
import com.library.app.commontests.utils.ResourceDefinitions;

public class CategoryResourceUTest {
	private CategoryResource categoryResource;
	
	private static final String PATH_RESOURCE = ResourceDefinitions.CATEGORY.getResourceName();
	
	@Mock
	private CategoryServices categoryServices;
	
	@Before
	public void initTestCase() {
		MockitoAnnotations.initMocks(this);
		categoryResource = new CategoryResource();
		
		categoryResource.categoryServices = categoryServices;
		categoryResource.categoryJsonConverter = new CategoryJsonConverter();
	}
	
	@Test
	public void addValidCategory() {
		when(categoryServices.add(java())).thenReturn(categoryWithId(java(), 1L));
		
		Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE	, "newCategory.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
		assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
	}
	
	@Test 
	public void addExistentCategory() {
		when(categoryServices.add(java())).thenThrow(new CategoryExistentException());
		
		final Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "newCategory.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
	}
	
	@Test 
	public void addCategoryWithNullName() {
		when(categoryServices.add(new Category())).thenThrow(new FieldNotValidException("name", "may not be null"));
		
		final Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "categoryWithNullName.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "categoryErrorNullName.json");
	}
	
	private void assertJsonResponseWithFile(Response response, String fileName) {
		assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE	, fileName));
	}
	
	@Test
	public void updateValidCategory() {
		Response response = categoryResource.update(1L,
				readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
		assertThat(response.getEntity().toString(), is(equalTo("")));
	}
	
	@Test
	public void updateCategoryWithNameBelongingToOtherCategory() {
		doThrow(new CategoryExistentException()).when(categoryServices).update(categoryWithId(java(), 1L));
		Response response = categoryResource.update(1L,
				readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));
		assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
	}
	
	@Test
	public void updateCategoryWithNullName() {
		doThrow(new FieldNotValidException("name", "may not be null")).when(categoryServices).update(
				categoryWithId(new Category(), 1L));
	}
	
	@Test
	public void updateCategoryNotFound() {
		doThrow(new CategoryNotFoundException()).when(categoryServices).update(categoryWithId(java(),2L));
		
		Response response = categoryResource.update(2L,
				readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
		assertJsonResponseWithFile(response, "categoryNotFound.json");
		
	}
	
	@Test
	public void findCategory() {
		when(categoryServices.findById(1L)).thenReturn(categoryWithId(java(), 1L));
		
		Response response = categoryResource.findById(1L);
		assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
		assertJsonResponseWithFile(response, "categoryFound.json");
	}
	
	@Test
	public void findCategoryNotFound() {
		when(categoryServices.findById(1L)).thenThrow(new CategoryNotFoundException());
		
		final Response response = categoryResource.findById(1L);
		assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
		
	}
	
	@Test
	public void findAllNoCategory() {
		when(categoryServices.findAll()).thenReturn(new ArrayList<>());
		
		final Response response = categoryResource.findAll();
		assertThat(response.getStatus(),  is(equalTo(HttpCode.OK.getCode())));
		assertJsonResponseWithFile(response, "emptyListOfCategories.json");
	}
	
	@Test
	public void findAllTwoCategories() {
		when(categoryServices.findAll()).thenReturn(
				Arrays.asList(categoryWithId(java(), 1L), categoryWithId(networks(), 2L)));
		final Response response = categoryResource.findAll();
		assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
		assertJsonResponseWithFile(response, "twoCategories.json");
	}
	

}
