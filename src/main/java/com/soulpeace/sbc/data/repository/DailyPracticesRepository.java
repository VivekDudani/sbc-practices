package com.soulpeace.sbc.data.repository;

import com.soulpeace.sbc.data.entity.DailyPractices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DailyPracticesRepository extends JpaRepository<DailyPractices, Integer> {

    @Query("From DailyPractices as dp where dp.userDetails.userName = ?1")
    List<DailyPractices> findAllByUserName(String userName);

    @Query("From DailyPractices as dp where dp.practiceDate between ?1 and ?2")
    List<DailyPractices> findByDateBetween(LocalDate practiceStartDate, LocalDate practiceEndDate);

    @Query("From DailyPractices as dp where dp.userDetails.userName = ?1 and dp.practiceDate between ?2 and ?3")
    List<DailyPractices> findByUserNameAndDateBetween(String userName, LocalDate practiceStartDate, LocalDate practiceEndDate);
}
