package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.TimeMaster;

public interface TimeRepository extends CrudRepository< TimeMaster, Long> {

	TimeMaster findByIdIs(int id);


}
