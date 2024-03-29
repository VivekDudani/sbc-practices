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
import java.util.Collections;
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
        if (weekInfo == null) {
            return Collections.emptyList();
        }
        LocalDate weekStartDate = weekInfo.getWeekStartDate();
        LocalDate weekEndDate = weekInfo.getWeekEndDate();

        List<DailyPractices> allPracticesForTheWeek = dailyPracticesService.getPracticesByDateRangeSortedByUser(
                weekStartDate, weekEndDate);
        if (allPracticesForTheWeek.isEmpty()) {
            log.info("No practices found for the given week- WeekStartDate: {}, WeekEndDate: {}", weekStartDate, weekEndDate);
            return Collections.emptyList();
        }
        return aggregate(allPracticesForTheWeek);
    }

    @Override
    public WeeklyTotals aggregatePracticesForUser(String userName, LocalDate practiceDate) {
        weekInfo = weekInfoService.getWeekInformationByGivenDate(practiceDate);
        if (weekInfo == null) {
            return null;
        }
        LocalDate weekStartDate = weekInfo.getWeekStartDate();
        LocalDate weekEndDate = weekInfo.getWeekEndDate();
        List<DailyPractices> practices = dailyPracticesService.getPracticesByUserNameAndDateSortedByDate(userName,
                weekStartDate, weekEndDate);
        if(!practices.isEmpty()) {
            List<WeeklyTotals> weeklyTotal = aggregate(practices);
            return weeklyTotal.get(0);
        }
        return null;
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

                //check if totals entry already exist for this user
                weeklyTotals = Optional.ofNullable(weeklyTotalsService.getWeeklyTotalForTheGivenWeekAndUser(currUser, weekInfo))
                        .orElse(new WeeklyTotals());
                weeklyTotals.resetPractices();
            }
            //for the very first user
            else if (previousUser == null && weeklyTotals == null) {
                //check if totals entry already exist for this user
                weeklyTotals = Optional.ofNullable(weeklyTotalsService.getWeeklyTotalForTheGivenWeekAndUser(currUser, weekInfo))
                        .orElse(new WeeklyTotals());
                weeklyTotals.resetPractices();
            }
            //accumulate practice data
            Optional.of(dailyPractice.getSsip()).ifPresent(s -> ssipCountPerUser.getAndIncrement());
            Optional.of(dailyPractice.getSpp()).ifPresent(s -> sppCountPerUser.getAndIncrement());
            weeklyTotals.incrementChantingCountBy(dailyPractice.getChanting());
            Optional.of(dailyPractice.getHkm()).filter(hCount -> hCount > 0).ifPresent(p -> weeklyTotals.setHkm(true));
            Optional.of(dailyPractice.getScs()).filter(scsCount -> scsCount > 0).ifPresent(p -> weeklyTotals.setScs(true));
            Optional.of(dailyPractice.getPf()).filter(pfCount -> pfCount > 0).ifPresent(p -> weeklyTotals.setPf(true));
            Optional.of(dailyPractice.getRr()).filter(rrCount -> rrCount > 0).ifPresent(p -> weeklyTotals.setRr(true));
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
        weeklyTotals = null;
        ssipCountPerUser = new AtomicInteger();
        sppCountPerUser = new AtomicInteger();
    }
}