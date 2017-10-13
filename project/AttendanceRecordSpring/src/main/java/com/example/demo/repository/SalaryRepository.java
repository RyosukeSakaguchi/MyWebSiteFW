package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.Salary;

public interface SalaryRepository  extends CrudRepository< Salary, Long> {

}
