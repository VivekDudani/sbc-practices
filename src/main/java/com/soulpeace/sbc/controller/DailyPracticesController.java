package com.soulpeace.sbc.controller;

import com.soulpeace.sbc.data.entity.DailyPractices;
import com.soulpeace.sbc.data.entity.UserDetails;
import com.soulpeace.sbc.data.entity.WeeklyTotals;
import com.soulpeace.sbc.data.model.PracticeDataWrapper;
import com.soulpeace.sbc.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
@Slf4j
public class DailyPracticesController {

    private final DailyPracticesService dailyPracticesService;

    private final UserDetailsService userDetailsService;

    private final WeekInfoService weekInfoService;

    private final WeeklyPracticeAggregator weeklyPracticeAggregator;

    private final WeeklyTotalsService weeklyTotalsService;

    @QueryMapping
    public List<DailyPractices> getAllDailyPractices() {
        return dailyPracticesService.getAllDailyPractices();
    }

    @QueryMapping
    public List<DailyPractices> getAllPracticesByUserName(@Argument String userName) {
        log.info("getAllPracticesByUserName invoked for user {}", userName);
        return dailyPracticesService.getAllPracticesByUserName(userName);
    }

    @QueryMapping
    public List<PracticeDataWrapper> getPracticesByDateRange(@Argument LocalDate practiceStartDate, @Argument LocalDate practiceEndDate) {
        log.info("getPracticesByDateRange invoked for date range {} and {}", practiceStartDate, practiceEndDate);
        return dailyPracticesService.getPracticesByDateRangeSortedByUserAndDate(practiceStartDate, practiceEndDate);
    }

    @QueryMapping
    public List<DailyPractices> getPracticesByUserNameAndDate(@Argument String userName, @Argument LocalDate practiceStartDate,
                                                              @Argument LocalDate practiceEndDate) {
        log.info("getPracticesByUserNameAndDate invoked for {}" + userName + " and " + practiceStartDate + " and "
                + practiceEndDate);
        return dailyPracticesService.getPracticesByUserNameAndDate(userName, practiceStartDate, practiceEndDate);
    }

    @QueryMapping
    public Set<UserDetails> getAllUsersByUserCreatedByField(@Argument String userName) {
        return userDetailsService.findAllByUserCreatedBy(userName);
    }

    @QueryMapping
    public List<WeeklyTotals> aggregateAllPracticesForGivenWeek(@Argument LocalDate practiceDate) {
        return weeklyPracticeAggregator.aggregateAllPracticesForGivenWeek(practiceDate);
    }

    @QueryMapping
    public UserDetails activateDeactivateUser(String userName, boolean isActive) {
        return userDetailsService.activateDeactivateUser(userName, isActive);
    }

    @QueryMapping
    public List<WeeklyTotals> getWeeklyTotalsForTheWeek(LocalDate dateOfTheWeek) {
        return weeklyTotalsService.getWeeklyTotalsForTheWeek(dateOfTheWeek);
    }

    @MutationMapping
    public DailyPractices createOrUpdateDailyPractice(@Argument String userName, @Argument String fullName, @Argument LocalDate practiceDate,
                                                      @Argument Boolean ssip, @Argument Boolean spp, @Argument Integer chanting,
                                                      @Argument Integer hkm, @Argument Integer scs, @Argument Integer pf,
                                                      @Argument Integer spPostCount, @Argument String spPost, @Argument Integer bgCount,
                                                      @Argument String bg, @Argument Integer otCount, @Argument String others,
                                                      @Argument boolean isUserAuthenticated, @Argument String userCreatedBy) {

        log.info("createOrUpdateDailyPractice invoked for Date: {}, User: {}, UserCreatedBy: {}", practiceDate, userName, userCreatedBy);
        //TODO Exception handling
        UserDetails userDetail = userDetailsService.getOrCreateUserDetails(userName, fullName, isUserAuthenticated, userCreatedBy);
        weekInfoService.addWeekInformation(practiceDate);
        return dailyPracticesService.addDailyPractices(userDetail, practiceDate, ssip, spp, chanting, hkm, scs, pf, spPostCount,
                spPost, bgCount, bg, otCount, others);
    }
}