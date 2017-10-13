package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.User;

public interface UserRepository extends CrudRepository< User, Long>  {
	// SELECT e FROM User e WHERE e.login_id = ?1, e.password = ?2
	User findByLoginIdIsAndPasswordIs(String loginId, String encPass);
}
