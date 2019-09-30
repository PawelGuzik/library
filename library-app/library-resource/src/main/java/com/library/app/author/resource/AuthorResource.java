package com.library.app.author.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.app.author.model.Author;
import com.library.app.author.services.AuthorServices;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.json.JsonUtils;
import com.library.app.common.json.OperationResultJsonWriter;
import com.library.app.common.model.HttpCode;
import com.library.app.common.model.OperationResult;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class AuthorResource {
	
	private Logger logger= LoggerFactory.getLogger(getClass());
		

	@Inject
	AuthorServices authorServices;
	
	@Inject
	AuthorJsonConverter authorJsonConverter;
	
	@POST
	public Response add(String body) {
		logger.debug("Adding a new author with body {}", body);
		Author author = authorJsonConverter.convertFrom(body);
		
		HttpCode httpCode = HttpCode.CREATED;
		OperationResult result;
		try {
			author = authorServices.add(author);
			result = OperationResult.success(JsonUtils.getJsonelementWithId(author.getId()));
		}catch(FieldNotValidException e) {
			httpCode = HttpCode.VALIDATION_ERROR;
			logger.error("One of the fileds of the author is not valid", e);
			result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
		}
		
		logger.debug("Returning the operation result after adding author: {}", result);
		return Response.status(httpCode.getCode())
				.entity(OperationResultJsonWriter.tojson(result))
				.build();
	}

}
