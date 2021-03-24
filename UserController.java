package com.cg.aps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cg.aps.entity.Guard;
import com.cg.aps.entity.Owner;
import com.cg.aps.entity.User;
import com.cg.aps.exception.ApplicationException;
import com.cg.aps.exception.DatabaseException;
import com.cg.aps.exception.DuplicateRecordException;
import com.cg.aps.exception.RecordNotFoundException;
import com.cg.aps.service.GuardService;
import com.cg.aps.service.OwnerService;
import com.cg.aps.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 
 * @author Vikas
 *
 */
@Api
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private GuardService guardService;
	 
	/**
	 * @author Vikas
	 * @return all users
	 */
	@ApiOperation(value = "Get all users",
			response = User.class,
			tags = "get-all-users",
			httpMethod = "GET")
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> userList = userService.search();
			return new ResponseEntity<>(userList, HttpStatus.OK);
		}catch(DatabaseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}
	
	/**
	 * @author Vikas
	 * @param userId - id of user
	 * @return a user with userId
	 */
	@ApiOperation(value = "Get user by Id",
			response = User.class,
			tags = "get-user",
			consumes = "userId",
			httpMethod = "GET")
	@GetMapping("/users/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId){
		try {
			User user = userService.findByPk(userId);
			return new ResponseEntity<>(user,HttpStatus.OK);
		}catch(RecordNotFoundException e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}

	/**
	 * @author Vikas
	 * @param user - user object
	 * @return id of the user
	 */
	@ApiOperation(value = "Add user",
			response = Integer.class,
			tags = "add-user",
			consumes = "User object",
			httpMethod = "POST")
	@PostMapping("/users")
	public ResponseEntity<Integer> addUser(@RequestBody User user) {
		try {
			Integer userId = userService.addUser(user);	
			if(user.getRole().toLowerCase().equals("owner")) {
				Owner owner = new Owner();
				owner.setOwnerId(user.getUserId());
				owner.setOwnerName(user.getName());
				owner.setEmailId(user.getEmailId());
				owner.setMobileNo(user.getMobileNo());
				ownerService.addOwner(owner);
			}else if(user.getRole().toLowerCase().equals("guard")) {
				Guard guard = new Guard();
				guard.setGuardId(user.getUserId());
				guard.setGuardName(user.getName());
				guard.setEmailId(user.getEmailId());
				guard.setMobileNo(user.getMobileNo());
				guardService.addGuard(guard);
			}
			return new ResponseEntity<>(userId, HttpStatus.OK);
		}catch(DuplicateRecordException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}
	
	/**
	 * @author Vikas
	 * @param user - user object
	 * @return update message
	 */
	@ApiOperation(value = "update user",
			response = String.class,
			tags = "update-user",
			consumes = "User object",
			httpMethod = "PUT")
	@PutMapping("/users")
	public ResponseEntity<String> updateUser(@RequestBody User user){
		try {
			userService.updateUser(user);
			if(user.getRole().toLowerCase().equals("owner")) {
				Owner owner = ownerService.findByPk(user.getUserId());
				owner.setOwnerId(user.getUserId());
				owner.setOwnerName(user.getName());
				owner.setEmailId(user.getEmailId());
				owner.setMobileNo(user.getMobileNo());
				ownerService.updateOwner(owner);
			}else if(user.getRole().toLowerCase().equals("guard")) {
				Guard guard = guardService.findByPk(user.getUserId());
				guard.setGuardId(user.getUserId());
				guard.setGuardName(user.getName());
				guard.setEmailId(user.getEmailId());
				guard.setMobileNo(user.getMobileNo());
				guardService.updateGuard(guard);
			}
			return new ResponseEntity<>("updated successfully",HttpStatus.OK);
		}catch(RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}
	
	/**
	 * @author Vikas
	 * @param user - user object
	 * @return delete message
	 */
	@ApiOperation(value = "delete user",
			response = String.class,
			tags = "delete-user",
			consumes = "User object",
			httpMethod = "DELETE")
	@DeleteMapping("/users")
	public ResponseEntity<String> deleteUser(@RequestBody User user){
		try {
			userService.deleteUser(user);
			if(user.getRole().toLowerCase().equals("owner")) {
				Owner owner = ownerService.findByPk(user.getUserId());
				ownerService.deleteOwner(owner);
			}else if(user.getRole().toLowerCase().equals("guard")) {
				Guard guard = guardService.findByPk(user.getUserId());
				guardService.deleteGuard(guard);
			}
			return new ResponseEntity<>("deleted successfully",HttpStatus.OK);
		}catch(RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}
	
  
	
	/**
	 * @author Vikas
	 * @param user - user object
	 * @return verification
	 */
	@ApiOperation(value = "authentication",
			response = String.class,
			tags = "authenticate-user",
			consumes = "User object",
			httpMethod = "GET")
	@GetMapping("/login")
	public ResponseEntity<String> getUserAuthentication(@RequestBody User user){
		try {
			userService.authenticate(user);
			return new ResponseEntity<>("Logged In",HttpStatus.OK);
		}catch(ApplicationException e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}
	
	
	
	/**
	 * @author Vikas
	 * @param id - user id
	 * @param oldPassword - old password
	 * @param newPassword - new password
	 * @return true or false
	 */
	@ApiOperation(value = "update password",
			response = Boolean.class,
			tags = "update-password",
			consumes = "userId, old password, new password",
			httpMethod = "PUT")
	@PutMapping("/updatePassword")
	public ResponseEntity<Boolean> updateUserPassword(@PathVariable Integer id,@PathVariable String oldPassword,@PathVariable String newPassword){
		try {
			Boolean check = userService.changePassword(id, oldPassword, newPassword);
			return new ResponseEntity<>(check,HttpStatus.OK);
		}catch(ApplicationException e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}
}
