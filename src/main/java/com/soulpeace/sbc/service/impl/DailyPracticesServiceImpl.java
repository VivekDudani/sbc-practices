package com.soulpeace.sbc.service.impl;

import com.soulpeace.sbc.data.entity.DailyPractices;
import com.soulpeace.sbc.data.entity.UserDetails;
import com.soulpeace.sbc.data.entity.WeeklyTotals;
import com.soulpeace.sbc.data.model.PracticeDataWrapper;
import com.soulpeace.sbc.data.repository.DailyPracticesRepository;
import com.soulpeace.sbc.service.DailyPracticesService;
import com.soulpeace.sbc.service.WeeklyTotalsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class DailyPracticesServiceImpl implements DailyPracticesService {

    private final DailyPracticesRepository dailyPracticesRepository;

    private final WeeklyTotalsService weeklyTotalsService;

    @Override
    public DailyPractices addDailyPractices(UserDetails userDetail, LocalDate practiceDate, Boolean ssip, Boolean spp, Integer chanting,
                                            Integer hkm, Integer scs, Integer pf, Integer rr, Integer spPostCount, String spPost,
                                            Integer bgCount, String bg, Integer otCount, String others) {
        DailyPractices dailyPractice = new DailyPractices();

        String userName = userDetail.getUserName();
        List<DailyPractices> dailyPracticesExistForUser = getPracticesByUserNameAndDateSortedByDate(userName,
                practiceDate, practiceDate);
        //Check if entry exist for this user and date
        if (!dailyPracticesExistForUser.isEmpty()) {
            if (dailyPracticesExistForUser.size() > 1) {
                log.error("More than 1 practice exist for user {} and Date {}", userName, practiceDate);
                throw new IllegalStateException(String.format("More than 1 practice exist for user %s and Date %s", userName,
                        practiceDate));
            }
            log.info("Practice already exist for user: {} and Date: {}. Updating it.", userName, practiceDate);
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
        dailyPractice.setRr(rr);
        dailyPractice.setSpPostCount(spPostCount);
        dailyPractice.setSp(spPost);
        dailyPractice.setBgCount(bgCount);
        dailyPractice.setBg(bg);
        dailyPractice.setOtCount(otCount);
        dailyPractice.setOt(others);

        return dailyPracticesRepository.save(dailyPractice);
    }

    @Override
    public List<DailyPractices> getAllDailyPractices() {
        return dailyPracticesRepository.findAll();
    }

    @Override
    public List<DailyPractices> getAllPracticesByUserName(String userName) {
        return dailyPracticesRepository.findAllByUserName(userName);
    }

    @Override
    public List<DailyPractices> getPracticesByDateRangeSortedByUser(LocalDate practiceStartDate, LocalDate practiceEndDate) {
        return dailyPracticesRepository.findByDateBetweenOOrderByUserDetails(practiceStartDate, practiceEndDate);
    }

    @Override
    public List<PracticeDataWrapper> getPracticesByDateRangeSortedByUserAndDate(LocalDate practiceStartDate, LocalDate practiceEndDate) {
        List<DailyPractices> dailyPractices = dailyPracticesRepository.
                findByDateBetweenOrderByUserDetailsAndDate(practiceStartDate, practiceEndDate);

        // get the totals
        Map<String, WeeklyTotals> weeklyTotalPerUserForTheGivenWeek = weeklyTotalsService.getWeeklyTotalPerUserForTheGivenWeek(practiceStartDate);

        //        System.out.println(wrapper);
        return getSbcPracticesWrapper(dailyPractices, weeklyTotalPerUserForTheGivenWeek);
    }

    private List<PracticeDataWrapper> getSbcPracticesWrapper(List<DailyPractices> dailyPractices, Map<String, WeeklyTotals> weeklyTotalsForAllUsers) {
        List<PracticeDataWrapper> wrapper = new ArrayList<>(10);
        String prevUser = "";
        List<DailyPractices> individualPractices = new ArrayList<>(7);
        //club practice per user
        for (DailyPractices dp : dailyPractices) {
            String currUser = dp.getUserDetails().getUserName();
            if (!currUser.equals(prevUser) && !prevUser.equals("")) {
                wrapper.add(new PracticeDataWrapper(prevUser, individualPractices, weeklyTotalsForAllUsers.get(prevUser)));
                individualPractices = new ArrayList<>(7);
            }
            individualPractices.add(dp);
            prevUser = currUser;
        }
        //last entry
        wrapper.add(new PracticeDataWrapper(prevUser, individualPractices, weeklyTotalsForAllUsers.get(prevUser)));

        return wrapper;
    }

    @Override
    public List<DailyPractices> getPracticesByUserNameAndDateSortedByDate(String userName, LocalDate practiceStartDate, LocalDate practiceEndDate) {
        return dailyPracticesRepository.findByUserNameAndDateBetweenOrderByDate(userName, practiceStartDate, practiceEndDate);
    }
}
