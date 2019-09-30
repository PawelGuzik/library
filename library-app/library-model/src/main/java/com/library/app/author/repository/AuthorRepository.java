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
		
		final StringBuilder clauseSort = new StringBuilder();
		if(filter.hasOrderField()) {
			clauseSort.append("ORDER BY e." + filter.getPaginationData().getOrderField());
			clauseSort.append(filter.getPaginationData().isAscending() ? " ASC" : " DESC");
		}else {
			clauseSort.append("ORDER BY e.name ASC");
		}
		final Query queryAuthors = em.createQuery("SELECT e FROM Author e " + clause.toString() + " "
				+ clauseSort.toString());
		applyQueryParameters(queryParameters, queryAuthors);
		if(filter.hasPaginationData()) {
			queryAuthors.setFirstResult(filter.getPaginationData().getFirstResult());
			queryAuthors.setMaxResults(filter.getPaginationData().getMaxResults());
		}
		
		final List<Author> authors = queryAuthors.getResultList();
		
		final Query queryCount = em.createQuery("SELECT Count (e) From Author e " + clause.toString());
		applyQueryParameters(queryParameters, queryCount);
		final Integer count = ((Long) queryCount.getSingleResult()).intValue();
		
		return new PaginatedData<>(count, authors);
	}
	
	private void applyQueryParameters(final Map<String, Object> queryParameters, final Query query) {
		for(final Entry<String, Object> entryMap : queryParameters.entrySet()) {
			query.setParameter(entryMap.getKey(), entryMap.getValue());
		}
	}

	
}
