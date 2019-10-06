package com.library.app.user.repository;

import java.util.Map;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;



import com.library.app.common.model.PaginatedData;
import com.library.app.common.repository.GenericRepository;
import com.library.app.user.model.User;
import com.library.app.user.model.filter.UserFilter;

@Stateless
public class UserRepository extends GenericRepository<User> {
	
	@PersistenceContext
	EntityManager em;

	@Override
	protected Class<User> getPersistentClass() {
		return User.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	public boolean alreadyExists(User user) {
		// TODO Auto-generated method stub
		return alreadyExists("email", user.getEmail(), user.getId());
	}

	public User findByEmail(String email) {
		try {
		return (User) em.createQuery("SELECT e FROM User e WHERE e.email = :email")
				.setParameter("email", email )
				.getSingleResult();
		}catch(final NoResultException e) {
			return null;
		}
	}

	public PaginatedData<User> findByFilter(UserFilter userFilter) {
		StringBuilder clause  = new StringBuilder("WHERE e.id is not null");
		
		Map<String, Object> queryParameters = new HashMap<>();
		if(userFilter.getName() !=null) {
			clause.append(" And Upper(e.name) Like Upper(:name)");
			queryParameters.put("name", "%" + userFilter.getName() + "%");
		}
		if(userFilter.getUserType() !=null) {
			clause.append(" And e.userType = :userType");
			queryParameters.put("userType", userFilter.getUserType());
		}
		return findByParameters(clause.toString(), userFilter.getPaginationData(), queryParameters, "name ASC");
	}
	
	

}
