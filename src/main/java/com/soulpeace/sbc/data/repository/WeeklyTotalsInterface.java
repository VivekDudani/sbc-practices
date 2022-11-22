package com.soulpeace.sbc.data.repository;

import com.soulpeace.sbc.data.entity.WeeklyId;
import com.soulpeace.sbc.data.entity.WeeklyTotals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyTotalsInterface extends JpaRepository<WeeklyTotals, WeeklyId> {
}
