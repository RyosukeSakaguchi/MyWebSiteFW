package com.example.demo.repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.WorkSituation;

public interface WorkSituationRepository extends CrudRepository< WorkSituation, Long> {

	WorkSituation findByIdIs(int id);

	List<WorkSituation> findByLoginIdIs(String loginId);

	// SELECT * FROM work_situation where login_id= ?1 and create_date= ?2
	WorkSituation findByLoginIdIsAndCreateDateIs(String loginId, Date createDate);

	List<WorkSituation> findByLoginIdIsAndCreateDateBetween(String loginId, Date startDate, Date endDate);

	int countByLoginIdIsAndCreateDateIs(String loginId, Date createDate);

	@Query("SELECT w FROM WorkSituation w where w.loginId= ?1 and YEAR(w.createDate)= ?2 and MONTH(w.createDate)= ?3")
	List<WorkSituation> findByLoginIdIsAndCreateYearIsAndCreateMonthIs(String loginId, int year, int month);

	@Query("SELECT w FROM WorkSituation w where w.loginId= ?1 and YEAR(w.createDate)= ?2 and MONTH(w.createDate)= ?3 and DAY(w.createDate)= ?4")
	List<WorkSituation> findByLoginIdIsAndCreateYearIsAndCreateMonthIsAndCreateDateIs(String loginId, int year, int month, int date);

	@Modifying
	@Query("update WorkSituation w set w.workSitu = ?1, w.workEnd = ?2, w.breakTime = ?3, w.workTime = ?4, w.overtime = ?5  where w.loginId = ?6 and w.createDate = ?7")
	int setFixedWorkSituAndWorkEndAndBreakTimeAndWorkTimeAndOvertimeFor(String workSitu, Time workEnd, Time breakTime, Time workTime, Time overtime, String loginId, Date createDate);

}
