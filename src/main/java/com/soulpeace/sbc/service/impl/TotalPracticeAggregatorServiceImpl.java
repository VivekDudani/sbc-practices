package com.soulpeace.sbc.service.impl;

import com.soulpeace.sbc.data.entity.WeeklyTotals;
import com.soulpeace.sbc.service.TotalPracticeAggregatorService;
import com.soulpeace.sbc.service.WeeklyPracticeAggregator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class TotalPracticeAggregatorServiceImpl implements TotalPracticeAggregatorService {

    private final WeeklyPracticeAggregator weeklyPracticeAggregator;

    @Override
    public List<WeeklyTotals> aggregateAllPracticesForGivenWeek(LocalDate practiceDate) {
        return weeklyPracticeAggregator.aggregateAllPracticesForGivenWeek(practiceDate);
    }

    @Override
    public WeeklyTotals aggregatePracticesForGivenUserAndWeek(String userName, LocalDate practiceDate) {
        return weeklyPracticeAggregator.aggregatePracticesForUser(userName, practiceDate);
    }
}
