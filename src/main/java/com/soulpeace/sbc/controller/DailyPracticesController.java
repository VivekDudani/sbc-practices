package com.soulpeace.sbc.controller;

import com.soulpeace.sbc.data.entity.DailyPractices;
import com.soulpeace.sbc.data.entity.UserDetails;
import com.soulpeace.sbc.data.repository.DailyPracticesRepository;
import com.soulpeace.sbc.service.UserDetailsService;
import com.soulpeace.sbc.service.WeekInfoService;
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

    private final DailyPracticesRepository dailyPracticesRepository;

    private final UserDetailsService userDetailsService;

    private final WeekInfoService weekInfoService;

    @QueryMapping
    public List<DailyPractices> getAllDailyPractices() {
        return dailyPracticesRepository.findAll();
    }

    @QueryMapping
    public List<DailyPractices> getAllPracticesByUserName(@Argument String userName) {
        log.info("getAllPracticesByUserName invoked for user {}", userName);
        return dailyPracticesRepository.findAllByUserName(userName);
    }

    @QueryMapping
    public List<DailyPractices> getPracticesByDateRange(@Argument LocalDate practiceStartDate, @Argument LocalDate practiceEndDate) {
        log.info("getPracticesByDateRange invoked for date range {} and {}", practiceStartDate, practiceEndDate);
        return dailyPracticesRepository.findByDateBetween(practiceStartDate, practiceEndDate);
    }

    @QueryMapping
    public List<DailyPractices> getPracticesByUserNameAndDate(@Argument String userName, @Argument LocalDate practiceStartDate,
                                                          @Argument LocalDate practiceEndDate) {
        System.out.println("getPracticesByUserNameAndDate invoked for "+ userName + " and " + practiceStartDate + " and " + practiceEndDate);
        return dailyPracticesRepository.findByUserNameAndDateBetween(userName, practiceStartDate, practiceEndDate);
    }

    @QueryMapping
    public Set<UserDetails> getAllUsersByUserCreatedByField(@Argument String userName) {
        return userDetailsService.findAllByUserCreatedBy(userName);
    }

    @MutationMapping
    public DailyPractices createOrUpdateDailyPractice(@Argument String userName, @Argument String fullName, @Argument LocalDate practiceDate,
                                              @Argument Boolean ssip, @Argument Boolean spp, @Argument Integer chanting,
                                              @Argument Integer hkm, @Argument Integer scs, @Argument Integer pf,
                                              @Argument String spPost, @Argument String bg, @Argument String others,
                                              @Argument boolean isUserAuthenticated, @Argument String userCreatedBy) {
        log.info("createOrUpdateDailyPractice invoked for Date: {}, User: {}, UserCreatedBy: {}", practiceDate, userName, userCreatedBy);

        UserDetails userDetail = getOrCreateUserDetails(userName, fullName, isUserAuthenticated, userCreatedBy);
        DailyPractices dailyPractice = new DailyPractices();

        List<DailyPractices> dailyPracticesExistForUser = dailyPracticesRepository.findByUserNameAndDateBetween(userName,
                practiceDate, practiceDate);
        //Entry exist for this user and date
        if (!dailyPracticesExistForUser.isEmpty()) {
            if (dailyPracticesExistForUser.size() > 1) {
                log.error("More than 1 practice exist for user {} and Date {}", userName, practiceDate);
                throw new IllegalStateException(String.format("More than 1 practice exist for user %s and Date %s", userName,
                        practiceDate));
            }
            log.info("Practice exist already for user: {} and Date: {}. Updating it.", userName, practiceDate);
            dailyPractice = dailyPracticesExistForUser.get(0);
        }

        dailyPractice.setUserDetails(userDetail);
        dailyPractice.setPracticeDate(practiceDate);
        dailyPractice.setSsip(ssip);
        dailyPractice.setSpp(spp);
        dailyPractice.setChanting(chanting);
        dailyPractice.setHkm(hkm);
        dailyPractice.setScs(scs);
        dailyPractice.setPf(pf);
        dailyPractice.setSp(spPost);
        dailyPractice.setBg(bg);
        dailyPractice.setOt(others);

        weekInfoService.addWeekInformation(practiceDate);

        return dailyPracticesRepository.save(dailyPractice);
    }

    private UserDetails getOrCreateUserDetails(String userName, String fullName, boolean isAuthenticated,
                                               String userCreatedBy) {
        return userDetailsService.getOrCreateUserDetails(userName, fullName, isAuthenticated, userCreatedBy);
    }

    //    @SchemaMapping(typeName = "Query", value = "firstQuery")
    @QueryMapping
    public String firstQuery () {
        return "First Query";
    }
}