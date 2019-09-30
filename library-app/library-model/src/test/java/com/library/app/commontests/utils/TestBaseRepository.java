package com.library.app.commontests.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import com.library.app.category.repository.CategoryRepository;

@Ignore
public class TestBaseRepository {

	private EntityManagerFactory emf;
	protected EntityManager em;
	protected DBCommandTransactionalExecutor dBCommandExecutor;

	
	protected void initializeTestDB() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();


		dBCommandExecutor = new DBCommandTransactionalExecutor(em);
	}

	protected void closeEntityManager() {
		em.close();
		emf.close();
	}

}
