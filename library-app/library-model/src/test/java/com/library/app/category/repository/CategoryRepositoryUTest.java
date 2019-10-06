package com.library.app.category.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import static com.library.app.category.commontests.category.CategoryForTestsRepository.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.resource.spi.IllegalStateException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
import com.library.app.commontests.utils.DBCommandTransactionalExecutor;
import com.library.app.commontests.utils.TestBaseRepository;

public class CategoryRepositoryUTest extends TestBaseRepository {
	private CategoryRepository categoryRepository;

	@Before
	public void initTestCase() {
		initializeTestDB();
		
		categoryRepository = new CategoryRepository();
		categoryRepository.em = em;
	}
	
	@After
	public void setdownTestCase() {
		closeEntityManager();
	}
	
	@Test
	public void addCategoryAndFindIt() throws IllegalStateException {
		final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});

		assertThat(categoryAddedId, is(notNullValue()));

		final Category category = categoryRepository.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}

	@Test
	public void findcategoryByIdNotFound() {
		final Category category = categoryRepository.findById(999L);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void findCategoryByIdWithNullId() {
		final Category category = categoryRepository.findById(null);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void updateCategory() throws IllegalStateException {
		final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});
		
		Category categoryAfterAdd  = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterAdd.getName(), is(equalTo(java().getName())));
		
		categoryAfterAdd.setName(cleanCode().getName());
		dbCommandExecutor.executeCommand(() -> {
			categoryRepository.update(categoryAfterAdd);
			return null;
		});
		
		Category categoryAfterUpdate = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterUpdate.getName(), is(equalTo(cleanCode().getName())));
	}
	
	@Test
	public void findAllCategories() throws IllegalStateException {
		dbCommandExecutor.executeCommand(() -> {
			allCategories().forEach(categoryRepository::add);
			return null;
		});
		
		final List<Category> categories = categoryRepository.findAll("name");
		assertThat(categories.size(), is(equalTo(4)));
		assertThat(categories.get(0).getName(),  is(equalTo(architecture().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(cleanCode().getName())));
		assertThat(categories.get(2).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(3).getName(), is(equalTo(networks().getName())));
	}
	
	@Test
	public void alredyExistsForAdd() throws IllegalStateException {
		dbCommandExecutor.executeCommand(() -> {
			categoryRepository.add(java());
			return null;
		});
		
		assertThat(categoryRepository.alreadyExists(java()), is(equalTo(true)));
		assertThat(categoryRepository.alreadyExists(cleanCode()), is(equalTo(false)));

	}
	
	@Test
	public void alreadyExistsCategoryWithId() throws IllegalStateException {
		final Category java = dbCommandExecutor.executeCommand(() -> {
			categoryRepository.add(cleanCode());
			return categoryRepository.add(java());
		});

		assertThat(categoryRepository.alreadyExists(java), is(equalTo(false)));

		java.setName(cleanCode().getName());
		assertThat(categoryRepository.alreadyExists(java), is(equalTo(true)));

		java.setName(networks().getName());
		assertThat(categoryRepository.alreadyExists(java), is(equalTo(false)));
	}
	
	@Test
	public void existsByd() throws IllegalStateException {
		final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});
		
		assertThat(categoryRepository.existsById(categoryAddedId), is(equalTo(true)));
		assertThat(categoryRepository.existsById(999L), is(equalTo(false)));

	}
}
