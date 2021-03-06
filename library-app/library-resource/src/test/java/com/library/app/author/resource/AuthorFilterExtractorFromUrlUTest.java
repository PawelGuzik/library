package com.library.app.author.resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.common.model.filter.PaginationData;
import com.library.app.common.model.filter.PaginationData.OrderMode;

public class AuthorFilterExtractorFromUrlUTest {
	
	@Mock
	private UriInfo uriInfo;
	
	@Before
	public void inittestCase() {
		MockitoAnnotations.initMocks(this);
	
	}
	
	@Test
	public void onlyDefaultValues() {
		setUpUriInfo(null, null, null, null);
		
		AuthorFilterExtractorFromUrl extractor = new AuthorFilterExtractorFromUrl(uriInfo);
		AuthorFilter authorFilter = extractor.getFilter();
		
		assertActualPaginationDataWithExpected(authorFilter.getPaginationData(), new PaginationData(0, 10, "name", OrderMode.ASCENDING));
		assertThat(authorFilter.getName(), is(nullValue()));
		
	}
	
	@Test
	public void withPaginationAndNameAndSortAscending() {
		setUpUriInfo("2", "5", "Robert", "id");

		final AuthorFilterExtractorFromUrl extractor = new AuthorFilterExtractorFromUrl(uriInfo);
		final AuthorFilter authorFilter = extractor.getFilter();

		assertActualPaginationDataWithExpected(authorFilter.getPaginationData(), new PaginationData(10, 5, "id",
				OrderMode.ASCENDING));
		assertThat(authorFilter.getName(), is(equalTo("Robert")));
	}

	@Test
	public void withPaginationAndNameAndSortAscendingWithPrefix() {
		setUpUriInfo("2", "5", "Robert", "+id");

		final AuthorFilterExtractorFromUrl extractor = new AuthorFilterExtractorFromUrl(uriInfo);
		final AuthorFilter authorFilter = extractor.getFilter();

		assertActualPaginationDataWithExpected(authorFilter.getPaginationData(), new PaginationData(10, 5, "id",
				OrderMode.ASCENDING));
		assertThat(authorFilter.getName(), is(equalTo("Robert")));
	}

	@Test
	public void withPaginationAndNameAndSortDescending() {
		setUpUriInfo("2", "5", "Robert", "-id");

		final AuthorFilterExtractorFromUrl extractor = new AuthorFilterExtractorFromUrl(uriInfo);
		final AuthorFilter authorFilter = extractor.getFilter();

		assertActualPaginationDataWithExpected(authorFilter.getPaginationData(), new PaginationData(10, 5, "id",
				OrderMode.DESCENDING));
		assertThat(authorFilter.getName(), is(equalTo("Robert")));
	}
	
	@SuppressWarnings("unchecked")
	private void setUpUriInfo(String page, String perPage, String name, String sort) {
		Map<String, String> parameters = new LinkedHashMap<>();
		parameters.put("page", page);
		parameters.put("per_page", perPage);
		parameters.put("name", name);
		parameters.put("sort", sort);
	
		MultivaluedMap<String, String> multiMap = mock(MultivaluedMap.class);
		
		for(Entry<String, String> keyValue : parameters.entrySet()) {
			when(multiMap.getFirst(keyValue.getKey())).thenReturn(keyValue.getValue());
		}
		
		when(uriInfo.getQueryParameters()).thenReturn(multiMap);
	}
	
	private static void assertActualPaginationDataWithExpected(PaginationData actual, PaginationData expected) {
		assertThat(actual.getFirstResult(), is(equalTo(expected.getFirstResult())));
		assertThat(actual.getMaxResults(), is(equalTo(expected.getMaxResults())));
		assertThat(actual.getOrderField(), is(equalTo(expected.getOrderField())));
		assertThat(actual.getOrderMode(), is(equalTo(expected.getOrderMode())));
	}

}
