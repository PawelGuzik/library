package com.library.app.commontests.utils;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Ignore;

import com.library.app.author.model.Author;
import com.library.app.category.model.Category;

@Ignore
@Stateless
public class TestRepositoryEJB {
	
	@PersistenceContext
	private EntityManager em;
	
	private static final List<Class<?>> ENTITIES_TO_REMOVE = Arrays.asList(Category.class, Author.class);
	
	public void deleteAll() {
		for(Class<?> entityClass : ENTITIES_TO_REMOVE) {
			deleteAllForEntity(entityClass);
		}
	}
	@SuppressWarnings("unchecked")
	private void deleteAllForEntity(Class<?> entityClass) {

		List<Object> rows = em.createQuery("SELECT o FROM " + entityClass.getSimpleName() + " o").getResultList();
		for(Object row : rows) {
			em.remove(row);
		}
	}

}
