package com.soulpeace.sbc.service.impl;

import com.soulpeace.sbc.data.entity.WeekInfo;
import com.soulpeace.sbc.data.entity.WeeklyTotals;
import com.soulpeace.sbc.data.repository.WeeklyTotalsRepository;
import com.soulpeace.sbc.service.WeekInfoService;
import com.soulpeace.sbc.service.WeeklyTotalsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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
    public List<WeeklyTotals> getWeeklyTotalsForTheWeek(LocalDate dateOfTheWeek) {
        WeekInfo weekInfo = weekInfoService.getWeekInformationByGivenDate(dateOfTheWeek);
        return weeklyTotalsRepository.findAllByWeekInfo(weekInfo);
    }

}
