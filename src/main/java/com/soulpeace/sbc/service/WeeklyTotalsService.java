package com.soulpeace.sbc.service;

import com.soulpeace.sbc.data.entity.UserDetails;
import com.soulpeace.sbc.data.entity.WeekInfo;
import com.soulpeace.sbc.data.entity.WeeklyTotals;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WeeklyTotalsService {

    WeeklyTotals addWeeklyTotalEntry(WeeklyTotals weeklyTotals);

    List<WeeklyTotals> getWeeklyTotalsForTheWeekSortedByUserDetails(LocalDate dateOfTheWeek);

    Map<String, WeeklyTotals> getWeeklyTotalPerUserForTheGivenWeek(LocalDate dateOfTheWeek);

    WeeklyTotals getWeeklyTotalForTheGivenWeekAndUser(UserDetails userDetails, WeekInfo weekInfo);
}
