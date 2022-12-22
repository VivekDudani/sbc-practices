package com.soulpeace.sbc.service;

import com.soulpeace.sbc.data.entity.WeeklyTotals;

import java.time.LocalDate;
import java.util.List;

public interface WeeklyTotalsService {

    WeeklyTotals addWeeklyTotalEntry(WeeklyTotals weeklyTotals);

    List<WeeklyTotals> getWeeklyTotalsForTheWeek(LocalDate dateOfTheWeek);
}
