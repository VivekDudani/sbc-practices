package com.soulpeace.sbc.data.repository;

import com.soulpeace.sbc.data.entity.WeekInfo;
import com.soulpeace.sbc.data.entity.WeeklyTotals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyTotalsRepository extends JpaRepository<WeeklyTotals, Integer> {

    List<WeeklyTotals> findAllByWeekInfo(WeekInfo date);
}
