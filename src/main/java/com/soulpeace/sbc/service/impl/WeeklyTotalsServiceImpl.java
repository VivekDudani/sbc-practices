package com.soulpeace.sbc.service.impl;

import com.soulpeace.sbc.data.entity.UserDetails;
import com.soulpeace.sbc.data.entity.WeekInfo;
import com.soulpeace.sbc.data.entity.WeeklyTotals;
import com.soulpeace.sbc.data.repository.WeeklyTotalsRepository;
import com.soulpeace.sbc.service.WeekInfoService;
import com.soulpeace.sbc.service.WeeklyTotalsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class WeeklyTotalsServiceImpl implements WeeklyTotalsService {

    private final WeeklyTotalsRepository weeklyTotalsRepository;

    private final WeekInfoService weekInfoService;

    @Override
    public WeeklyTotals addWeeklyTotalEntry(WeeklyTotals weeklyTotals) {
        return weeklyTotalsRepository.save(weeklyTotals);
    }

    @Override
    public List<WeeklyTotals> getWeeklyTotalsForTheWeekSortedByUserDetails(LocalDate dateOfTheWeek) {
        WeekInfo weekInfo = weekInfoService.getWeekInformationByGivenDate(dateOfTheWeek);
        return weeklyTotalsRepository.findAllByWeekInfoOrderByUserDetails(weekInfo);
    }

    @Override
    public WeeklyTotals getWeeklyTotalForTheGivenWeekAndUser(UserDetails userDetails, WeekInfo weekInfo) {
        return weeklyTotalsRepository.findWeeklyTotalsByUserDetailsAndWeekInfo(userDetails, weekInfo);
    }

    @Override
    public Map<String, WeeklyTotals> getWeeklyTotalPerUserForTheGivenWeek(LocalDate dateOfTheWeek) {
        WeekInfo weekInfo = weekInfoService.getWeekInformationByGivenDate(dateOfTheWeek);
        List<WeeklyTotals> weeklyTotals = weeklyTotalsRepository.findAllByWeekInfoOrderByUserDetails(weekInfo);
        Map<String, WeeklyTotals> result = new HashMap<>(10);
        weeklyTotals.forEach(wt -> {
            result.put(wt.getUserDetails().getUserName(), wt);
        });

        return result;
    }

}
