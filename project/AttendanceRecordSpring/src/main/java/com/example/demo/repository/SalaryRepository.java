package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.SalaryMaster;

public interface SalaryRepository  extends CrudRepository< SalaryMaster, Long> {

	SalaryMaster findByPositionIs(String position);

}
