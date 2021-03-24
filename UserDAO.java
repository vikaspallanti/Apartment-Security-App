package com.cg.aps.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cg.aps.entity.User;
/**
 * 
 * @author Vikas
 * login module repository
 *
 */
@Repository
public interface UserDAO extends JpaRepository<User,Integer> {

	@Query("select u from User u where u.name = :name")
	public List<User> findByName(String name) throws Exception;
	
	//public User findByUserIdAndPassword(User user) throws Exception;

}
