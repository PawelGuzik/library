package com.library.app.author.resource;

import static com.library.app.commontests.author.AuthorForTestsRepository.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static com.library.app.commontests.utils.FileTestNameUtils.*;
import static com.library.app.commontests.utils.JsonTestUtils.*;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.library.app.author.exception.AuthorNotFoundException;
import com.library.app.author.model.Author;
import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.author.services.AuthorServices;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.HttpCode;
import com.library.app.common.model.PaginatedData;
import com.library.app.commontests.utils.ResourceDefinitions;

public class AuthorResourceUTest {
	private AuthorResource authorResource;

	private static final String PATH_RESOURCE = ResourceDefinitions.AUTHOR.getResourceName();

	@Mock
	private AuthorServices authorServices;
	
	@Mock 
	private UriInfo uriInfo;

	@Before
	public void initTest() {
		MockitoAnnotations.initMocks(this);
		authorResource = new AuthorResource();

		authorResource.authorServices = authorServices;
		authorResource.authorJsonConverter = new AuthorJsonConverter();
		authorResource.uriInfo = uriInfo;
	}

	@Test
	public void addValidAuthor() {
		when(authorServices.add(robertMartin())).thenReturn(authorWithId(robertMartin(), 1L));

		final Response response = authorResource
				.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "robertMartin.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
		assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
	}

	@Test
	public void addAuthorWithNullName() throws Exception {
		when(authorServices.add((Author) anyObject())).thenThrow(new FieldNotValidException("name", "may not be null"));

		final Response response = authorResource
				.add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "authorWithNullName.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "authorErrorNullName.json");
	}

	@Test
	public void updateValidAuthor() throws Exception {
		final Response response = authorResource.update(1L,
				readJsonFile(getPathFileRequest(PATH_RESOURCE, "robertMartin.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
		assertThat(response.getEntity().toString(), is(equalTo("")));

		verify(authorServices).update(authorWithId(robertMartin(), 1L));
	}

	@Test
	public void updateAuthorWithNullName() throws Exception {
		doThrow(new FieldNotValidException("name", "may not be null")).when(authorServices)
				.update((Author) anyObject());

		final Response response = authorResource.update(1L,
				readJsonFile(getPathFileRequest(PATH_RESOURCE, "authorWithNullName.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
		assertJsonResponseWithFile(response, "authorErrorNullName.json");
	}

	@Test
	public void updateAuthorNotFound() throws Exception {
		doThrow(new AuthorNotFoundException()).when(authorServices).update(authorWithId(robertMartin(), 2L));

		final Response response = authorResource.update(2L,
				readJsonFile(getPathFileRequest(PATH_RESOURCE, "robertMartin.json")));
		assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
	}

	@Test
	public void findAuthor() throws AuthorNotFoundException {
		when(authorServices.findById(1L)).thenReturn(authorWithId(robertMartin(), 1L));

		final Response response = authorResource.findById(1L);
		assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
		assertJsonResponseWithFile(response, "robertMartinFound.json");
	}

	@Test
	public void findAuthorNotFound() throws AuthorNotFoundException {
		when(authorServices.findById(1L)).thenThrow(new AuthorNotFoundException());

		final Response response = authorResource.findById(1L);
		assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void findByFilterNoFilter() {
		final List<Author> authors = Arrays.asList(authorWithId(erichGamma(), 2L), authorWithId(jamesGosling(), 3L),
				authorWithId(martinFowler(), 4L), authorWithId(robertMartin(), 1L));

		final MultivaluedMap<String, String> multiMap = mock(MultivaluedMap.class);
		when(uriInfo.getQueryParameters()).thenReturn(multiMap);

		when(authorServices.findByFilter((AuthorFilter) anyObject())).thenReturn(
				new PaginatedData<Author>(authors.size(), authors));

		final Response response = authorResource.findByFilter();
		assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
		assertJsonResponseWithFile(response, "authorsAllInOnePage.json");
	}
	

	private void assertJsonResponseWithFile(final Response response, final String fileName) {
		assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
	}

}
