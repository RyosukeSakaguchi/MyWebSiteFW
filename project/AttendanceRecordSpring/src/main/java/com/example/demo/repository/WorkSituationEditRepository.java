package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.WorkSituationEdit;

public interface WorkSituationEditRepository extends CrudRepository< WorkSituationEdit, Long> {

	List<WorkSituationEdit> findByLoginIdIs(String loginId);

}
