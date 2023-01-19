package com.soulpeace.sbc.service;

import com.soulpeace.sbc.data.entity.WeeklyTotals;

import java.time.LocalDate;
import java.util.List;

public interface TotalPracticeAggregatorService {

    List<WeeklyTotals> aggregateAllPracticesForGivenWeek(LocalDate practiceDate);

    WeeklyTotals aggregatePracticesForGivenUserAndWeek(String userName, LocalDate practiceDate);
}
