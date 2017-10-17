package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.User;

public interface UserRepository extends CrudRepository< User, Long>  {

	// SELECT * FROM User WHERE login_id = ?1, password = ?2
	User findByLoginIdIsAndPasswordIs(String loginId, String encPass);

	// SELECT * FROM User WHERE id = ?1
	User findByIdIs(int id);

	// SELECT * FROM User
	List<User> findAll();

	// SELECT * FROM User WHERE login_id = ?1, password = ?2
	List<User> findByLoginIdIsAndNameContainingAndPositionIsAndBirthDateBetween(String loginId, String name, String Position, Date birthDateFrom, Date birthDateTo);
}
