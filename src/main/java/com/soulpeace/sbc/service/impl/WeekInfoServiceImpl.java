package com.soulpeace.sbc.service.impl;

import com.soulpeace.sbc.data.entity.WeekInfo;
import com.soulpeace.sbc.data.repository.WeekInfoRepository;
import com.soulpeace.sbc.service.WeekInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Component
@AllArgsConstructor
@Slf4j
public class WeekInfoServiceImpl implements WeekInfoService {

    private final WeekInfoRepository weekInfoRepository;

    @Override
    public WeekInfo addWeekInformation(LocalDate currentDate) {
        LocalDate firstMondayOfWeek = currentDate.with(previousOrSame(MONDAY));
        LocalDate firstSundayOfWeek = currentDate.with(nextOrSame(SUNDAY));
        int weekNumber = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());

        WeekInfo weekInfo = weekInfoRepository.findByWeekStartDate(firstMondayOfWeek);
        if (weekInfo == null) {
            weekInfo = new WeekInfo(weekNumber, firstMondayOfWeek, firstSundayOfWeek);
            weekInfoRepository.save(weekInfo);
            log.info("WeekInfo created for WeekNumber:{}, WeekStart: {}, WeekEnd: {}", weekNumber, firstMondayOfWeek, firstSundayOfWeek);
        }
        return weekInfo;
    }

    @Override
    public WeekInfo getWeekInformationByGivenDate(LocalDate currentDate) {
        LocalDate firstMondayOfWeek = currentDate.with(previousOrSame(MONDAY));
        return weekInfoRepository.findByWeekStartDate(firstMondayOfWeek);
    }
}
