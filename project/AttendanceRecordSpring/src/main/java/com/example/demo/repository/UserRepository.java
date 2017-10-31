package com.example.demo.repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

	@Modifying
	@Query("update User u set u.name = ?1, u.position = ?2, u.birthDate = ?3, u.updateDate = ?4 where u.id = ?5")
	int setFixedNameAndPositionAndBirthDateAndUpdateDateFor(String name, String position, Date birthDate, Timestamp updateDate, int id);

	@Modifying
	@Query("update User u set u.name = ?1, u.position = ?2, u.birthDate = ?3, u.password = ?4, u.updateDate = ?5 where u.id = ?6")
	int setFixedNameAndPositionAndBirthDateAndPasswordAndUpdateDateFor(String name, String position, Date birthDate, String password, Timestamp updateDate, int id);
}
