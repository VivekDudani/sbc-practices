package com.soulpeace.sbc.service.impl;

import com.soulpeace.sbc.data.entity.DailyPractices;
import com.soulpeace.sbc.data.entity.UserDetails;
import com.soulpeace.sbc.data.entity.WeekInfo;
import com.soulpeace.sbc.data.entity.WeeklyTotals;
import com.soulpeace.sbc.service.DailyPracticesService;
import com.soulpeace.sbc.service.WeekInfoService;
import com.soulpeace.sbc.service.WeeklyPracticeAggregator;
import com.soulpeace.sbc.service.WeeklyTotalsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class WeeklyPracticeAggregatorImpl implements WeeklyPracticeAggregator {

    private final DailyPracticesService dailyPracticesService;

    private final WeekInfoService weekInfoService;

    private final WeeklyTotalsService weeklyTotalsService;

    private WeekInfo weekInfo;
    private WeeklyTotals weeklyTotals;
    private AtomicInteger ssipCountPerUser;
    private AtomicInteger sppCountPerUser;

    public WeeklyPracticeAggregatorImpl(DailyPracticesService dailyPracticesService, WeekInfoService weekInfoService,
                                        WeeklyTotalsService weeklyTotalsService) {
        this.dailyPracticesService = dailyPracticesService;
        this.weekInfoService = weekInfoService;
        this.weeklyTotalsService = weeklyTotalsService;
    }

    @Override
    public List<WeeklyTotals> aggregateAllPracticesForGivenWeek(LocalDate practiceDate) {
        weekInfo = weekInfoService.getWeekInformationByGivenDate(practiceDate);
        LocalDate weekStartDate = weekInfo.getWeekStartDate();
        LocalDate weekEndDate = weekInfo.getWeekEndDate();

        List<DailyPractices> allPracticesForTheWeek = dailyPracticesService.getPracticesByDateRangeSortedByUser(
                weekStartDate, weekEndDate);
        if (allPracticesForTheWeek.isEmpty()) {
            log.info("No practices found for the given week- WeekStartDate: {}, WeekEndDate: {}", weekStartDate, weekEndDate);
            return null;
        }
        return aggregate(allPracticesForTheWeek);
    }

    private List<WeeklyTotals> aggregate(List<DailyPractices> allPracticesForTheWeek) {

        List<WeeklyTotals> weeklyTotalsAggregated = new ArrayList<>(10);
        initializeVars();
        UserDetails previousUser = null;

        for (DailyPractices dailyPractice : allPracticesForTheWeek) {
            UserDetails currUser = dailyPractice.getUserDetails();
            if (previousUser != null && currUser.getId().intValue() != previousUser.getId().intValue()) {

                //aggregate previous user and save
                aggregatePreviousUserPractices(weeklyTotalsAggregated, previousUser);
            }
            //accumulate practice data
            Optional.of(dailyPractice.getSsip()).ifPresent(s -> ssipCountPerUser.getAndIncrement());
            Optional.of(dailyPractice.getSpp()).ifPresent(s -> sppCountPerUser.getAndIncrement());
            weeklyTotals.incrementChantingCountBy(dailyPractice.getChanting());
            Optional.of(dailyPractice.getHkm()).filter(hCount -> hCount > 0).ifPresent(p -> weeklyTotals.setHkm(true));
            Optional.of(dailyPractice.getScs()).filter(scsCount -> scsCount > 0).ifPresent(p -> weeklyTotals.setScs(true));
            Optional.of(dailyPractice.getPf()).filter(pfCount -> pfCount > 0).ifPresent(p -> weeklyTotals.setPf(true));
            Optional.of(dailyPractice.getBgCount()).filter(bgCount -> bgCount > 0).ifPresent(p -> weeklyTotals.setBg(true));
            Optional.of(dailyPractice.getSpPostCount()).filter(spCount -> spCount > 0).ifPresent(p -> weeklyTotals.setSp(true));
            Optional.of(dailyPractice.getOtCount()).filter(otCount -> otCount > 0).ifPresent(p -> weeklyTotals.setOt(true));

            previousUser = currUser;
        }

        //for the last one
        aggregatePreviousUserPractices(weeklyTotalsAggregated, previousUser);

        return weeklyTotalsAggregated;
    }

    private void aggregatePreviousUserPractices(List<WeeklyTotals> weeklyTotalsAggregated, UserDetails previousUser) {
        weeklyTotals.setUserDetails(previousUser);
        weeklyTotals.setWeekInfo(weekInfo);
        Optional.of(ssipCountPerUser).filter(cnt -> cnt.get() > 6).ifPresent(p -> weeklyTotals.setSsip(true));
        Optional.of(sppCountPerUser).filter(cnt -> cnt.get() > 6).ifPresent(p -> weeklyTotals.setSpp(true));

        weeklyTotalsService.addWeeklyTotalEntry(weeklyTotals);
        weeklyTotalsAggregated.add(weeklyTotals);

        initializeVars();
    }

    private void initializeVars() {
        weeklyTotals = new WeeklyTotals();
        ssipCountPerUser = new AtomicInteger();
        sppCountPerUser = new AtomicInteger();
    }
}