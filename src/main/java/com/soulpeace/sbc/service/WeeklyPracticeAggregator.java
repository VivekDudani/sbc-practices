package com.soulpeace.sbc.service;

import com.soulpeace.sbc.data.repository.DailyPracticesRepository;
import com.soulpeace.sbc.data.repository.WeeklyTotalsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WeeklyPracticeAggregator {

    private final DailyPracticesRepository dailyPracticesRepository;

    private final WeeklyTotalsRepository weeklyTotalsRepository;
}
