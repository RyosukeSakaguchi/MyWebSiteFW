package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.PositionMaster;

public interface PositionRepository  extends CrudRepository< PositionMaster, Long> {

	// SELECT * FROM Position
	List<PositionMaster> findAll();

}
