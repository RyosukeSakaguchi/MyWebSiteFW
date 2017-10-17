package com.example.demo.repository;

import java.sql.Date;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.WorkSituation;

public interface WorkSituationRepository extends CrudRepository< WorkSituation, Long> {

	// SELECT * FROM work_situation where login_id= ?1 and create_date= ?2
	WorkSituation findByLoginIdIsAndCreateDateIs(String loginId, Date createDate);

}
