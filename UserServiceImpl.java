package com.cg.aps.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.aps.dao.UserDAO;
import com.cg.aps.entity.User;
import com.cg.aps.exception.ApplicationException;
import com.cg.aps.exception.DatabaseException;
import com.cg.aps.exception.DuplicateRecordException;
import com.cg.aps.exception.RecordNotFoundException;
/**
 * 
 * @author Vikas
 * implementation of user service interface
 *
 */
@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDAO userDao;
	
	@Override
	public Integer addUser(User user) throws DuplicateRecordException {
		try {			
			userDao.save(user);
			return user.getUserId();
		}catch(DataAccessException e) {
			throw new DuplicateRecordException(e.getMessage());
		}catch(Exception e) {
			throw new DuplicateRecordException(e.getMessage());
		}
	}

	@Override
	public void updateUser(User user) throws RecordNotFoundException {
		try {			
			userDao.save(user);
		}catch(DataAccessException e) {
			throw new RecordNotFoundException(e.getMessage());
		}catch(Exception e) {
			throw new RecordNotFoundException(e.getMessage());
		}
	}

	@Override
	public void deleteUser(User user) throws RecordNotFoundException {
		try {			
			userDao.delete(user);
		}catch(DataAccessException e) {
			throw new RecordNotFoundException(e.getMessage());
		}catch(Exception e) {
			throw new RecordNotFoundException(e.getMessage());
		}
		
	}

	@Override
	public User findByPk(Integer id) throws RecordNotFoundException {
		try {			
			Optional<User> optional = userDao.findById(id);
			if(optional.isPresent()) {
				return optional.get();
			}else {
				throw new Exception("Invalid id");
			}
		}catch(DataAccessException e) {
			throw new RecordNotFoundException(e.getMessage());
		}catch(Exception e) {
			throw new RecordNotFoundException(e.getMessage());
		}
	}

	@Override
	public List<User> search(Integer pageNo, Integer pageSize) throws DatabaseException {
		try {	
			PageRequest paging = PageRequest.of(pageNo, pageSize);
			Page<User> pagedResult = userDao.findAll(paging);
			if(pagedResult.hasContent()) {
				return pagedResult.getContent();
			}else {
				throw new Exception("Invalid pageNo and pageSize");
			}
		}catch(DataAccessException e) {
			throw new DatabaseException(e.getMessage());
		}catch(Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public List<User> search() throws DatabaseException {
		try {			
			return userDao.findAll();
		}catch(DataAccessException e) {
			throw new DatabaseException(e.getMessage());
		}catch(Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public User authenticate(User user) throws ApplicationException {
		try {			
			Optional<User> optional = userDao.findById(user.getUserId());
			if(optional.isPresent()) {
				User temp = optional.get();
				if(temp.getPassword()==user.getPassword()) {
					return temp;
				}else {
					throw new Exception("Wrong password");
				}
			}else {
				throw new Exception("User doesn't exit");
			}
		}catch(DataAccessException e) {
			throw new ApplicationException(e.getMessage());
		}catch(Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	@Override
	public Boolean changePassword(Integer id, String oldPassword, String newPassword) throws ApplicationException {
		try {			
			Optional<User> optional = userDao.findById(id);
			if(optional.isPresent()) {
				User user = optional.get();
				if(user.getPassword()==oldPassword) {
					user.setPassword(newPassword);
					return true;
				}else {
					throw new Exception("Wrong password");
				}
			}else {
				throw new Exception("Invalid Id");
			}
		}catch(DataAccessException e) {
			throw new ApplicationException(e.getMessage());
		}catch(Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	/*
	 * @Override public Boolean forgetPassword(String login) throws
	 * RecordNotFoundException { // TODO Auto-generated method stub return null; }
	 */

	@Override
	public List<User> findByName(String name) throws RecordNotFoundException {
		try {			
			return userDao.findByName(name);
		}catch(DataAccessException e) {
			throw new RecordNotFoundException(e.getMessage());
		}catch(Exception e) {
			throw new RecordNotFoundException(e.getMessage());
		}
	}

}
