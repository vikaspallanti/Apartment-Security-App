package com.cg.aps.service;

import java.util.List;

import com.cg.aps.entity.User;
import com.cg.aps.exception.ApplicationException;
import com.cg.aps.exception.DatabaseException;
import com.cg.aps.exception.DuplicateRecordException;
import com.cg.aps.exception.RecordNotFoundException;
/**
 * 
 * @author Vikas
 * User service layer interface
 *
 */
public interface UserService {

	public Integer addUser(User user) throws DuplicateRecordException;
	public void updateUser(User user) throws RecordNotFoundException;
	public void deleteUser(User user) throws RecordNotFoundException;
	public List<User> findByName(String name) throws RecordNotFoundException;
	public User findByPk(Integer id) throws RecordNotFoundException;
	public List<User> search(Integer pageNo, Integer pageSize) throws DatabaseException;
	public List<User> search() throws DatabaseException;
	public User authenticate(User user) throws ApplicationException;
	public Boolean changePassword(Integer id,String oldPassword,String newPassword) throws ApplicationException;
	//public Boolean forgetPassword(String emailId) throws RecordNotFoundException;
}
