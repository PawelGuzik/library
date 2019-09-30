package com.library.app.commontests.utils;

import static com.library.app.commontests.utils.FileTestNameUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.hamcrest.core.Is;
import org.junit.Ignore;

import com.library.app.common.model.HttpCode;
import com.library.app.commontests.utils.JsonTestsUtils;

@Ignore
public class IntTestUtils {
	
	public static Long addElementWithFileAndGetId(final ResourceClient resourceClient, final String pathResource, final String mainFolder, final String fileName) {
		final Response response = resourceClient.resourcePath(pathResource).postWithFile(
				getPathFilerequest(mainFolder, fileName));
		return assertResponseIsCreatedAndGetId(response);
	}
	
	public static String findById(final ResourceClient resourceClient, final String pathresource, final Long id) {
		final Response response = resourceClient.resourcePath(pathresource + "/" + id).get();
		
		assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
		return response.readEntity(String.class);
	}
	
	private static Long assertResponseIsCreatedAndGetId(final Response response) {
		assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
		final Long id = JsonTestsUtils.getIdFromJson(response.readEntity(String.class));
		assertThat(id, is(notNullValue()));
		return id;
	}

}
