package com.library.app.user.services.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.utils.PasswordUtils;
import com.library.app.common.utils.ValidationUtils;
import com.library.app.user.excpetion.UserExistentException;
import com.library.app.user.excpetion.UserNotFoundException;
import com.library.app.user.model.User;
import com.library.app.user.model.filter.UserFilter;
import com.library.app.user.repository.UserRepository;
import com.library.app.user.services.UserServices;

@Stateless
public class UserServicesImpl implements UserServices{
	
	@Inject
	UserRepository userRepository;
	
	@Inject
	Validator validator;

	@Override
	public User add(User user){
		validateUser(user);
		user.setPassword(PasswordUtils.encryptPassword(user.getPassword()));
		// TODO Auto-generated method stub
		return userRepository.add(user);
	}
	
	private void validateUser(User user) throws FieldNotValidException, UserExistentException {
		if(userRepository.alreadyExists(user)) {
			throw new UserExistentException();
		}
		ValidationUtils.validateEntityFields(validator, user);
		
	}

	@Override
	public User findById(Long id) {
		User user = userRepository.findById(id);
		if(user == null) {
			throw new UserNotFoundException();
		}
		return user;
	}

	@Override
	public void update(User user) {
		User existentUser = findById(user.getId());
		user.setPassword(existentUser.getPassword());
		
		validateUser(user);
		userRepository.update(user);
	}

	@Override
	public void updatePassword(Long id, String password) {
		final User user = findById(id);
		user.setPassword(PasswordUtils.encryptPassword(password));
		userRepository.update(user);
		
	}

	@Override
	public User findByEmail(String email){
		User user = userRepository.findByEmail(email);
		if(user == null) {
			throw new UserNotFoundException();
		}
		return user;
	}

	@Override
	public User findByEmailAndPassword(String email, String password) {
		User user = findByEmail(email);
		if(!user.getPassword().equals(PasswordUtils.encryptPassword(password)))
			throw new UserNotFoundException();
		// TODO Auto-generated method stub
		return user;
	}

	@Override
	public PaginatedData<User> findByFilter(UserFilter userFilter) {
		return userRepository.findByFilter(userFilter);
	}

}