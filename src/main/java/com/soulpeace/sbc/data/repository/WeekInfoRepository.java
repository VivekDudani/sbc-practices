package com.soulpeace.sbc.data.repository;

import com.soulpeace.sbc.data.entity.WeekInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WeekInfoRepository extends JpaRepository<WeekInfo, Integer> {

    WeekInfo findByWeekStartDate(LocalDate weekStartDate);
}
