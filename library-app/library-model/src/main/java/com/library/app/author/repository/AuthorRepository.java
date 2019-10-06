package com.library.app.author.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.library.app.author.model.Author;
import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.repository.GenericRepository;

@Stateless
public class AuthorRepository extends GenericRepository<Author> {
	
	@PersistenceContext
	EntityManager em;
	
	@Override
	protected Class<Author> getPersistentClass() {
	return Author.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	
	


	
	@SuppressWarnings("unchecked")
	public PaginatedData<Author> findByFilter(final AuthorFilter filter){
		final StringBuilder clause = new StringBuilder("WHERE e.id is not null");
		final Map<String, Object> queryParameters =new HashMap<>();
		if(filter.getName() !=null) {
			clause.append(" AND UPPER(e.name) Like UPPER(:name)");
			queryParameters.put("name", "%" + filter.getName() + "%");
		}
		
		return findByParameters(clause.toString(), filter.getPaginationData(), queryParameters, "name ASC");
	}
	


	
}
