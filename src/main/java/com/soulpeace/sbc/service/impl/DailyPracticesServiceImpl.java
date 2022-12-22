package com.soulpeace.sbc.service.impl;

import com.soulpeace.sbc.data.entity.DailyPractices;
import com.soulpeace.sbc.data.entity.UserDetails;
import com.soulpeace.sbc.data.model.PracticeDataWrapper;
import com.soulpeace.sbc.data.repository.DailyPracticesRepository;
import com.soulpeace.sbc.service.DailyPracticesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class DailyPracticesServiceImpl implements DailyPracticesService {

    private final DailyPracticesRepository dailyPracticesRepository;

    @Override
    public DailyPractices addDailyPractices(UserDetails userDetail, LocalDate practiceDate, Boolean ssip, Boolean spp, Integer chanting,
                                            Integer hkm, Integer scs, Integer pf, Integer spPostCount, String spPost, Integer bgCount, String bg, Integer otCount, String others) {
        DailyPractices dailyPractice = new DailyPractices();

        String userName = userDetail.getUserName();
        List<DailyPractices> dailyPracticesExistForUser = getPracticesByUserNameAndDate(userName,
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
    public List<DailyPractices> getPracticesByDateRange(LocalDate practiceStartDate, LocalDate practiceEndDate) {
        return dailyPracticesRepository.findByDateBetween(practiceStartDate, practiceEndDate);
    }

    @Override
    public List<DailyPractices> getPracticesByDateRangeSortedByUser(LocalDate practiceStartDate, LocalDate practiceEndDate) {
        return dailyPracticesRepository.findByDateBetweenOOrderByUserDetails(practiceStartDate, practiceEndDate);
    }

    @Override
    public List<PracticeDataWrapper> getPracticesByDateRangeSortedByUserAndDate(LocalDate practiceStartDate, LocalDate practiceEndDate) {
        List<DailyPractices> dailyPractices = dailyPracticesRepository.findByDateBetweenOrderByUserDetailsAndDate(practiceStartDate, practiceEndDate);

        List<PracticeDataWrapper> wrapper = new ArrayList<>(10);
        String prevUser = "";
        List<DailyPractices> individualPractices = new ArrayList<>(7);
        //club practice per user
        for (DailyPractices dp : dailyPractices) {
            String currUser = dp.getUserDetails().getUserName();
            if (!currUser.equals(prevUser) && !prevUser.equals("")) {
                wrapper.add(new PracticeDataWrapper(prevUser, individualPractices));
                individualPractices = new ArrayList<>(7);
            }
            individualPractices.add(dp);
            prevUser = currUser;
        }
        //last entry
        wrapper.add(new PracticeDataWrapper(prevUser, individualPractices));
        System.out.println(wrapper);

        return wrapper;
    }

    @Override
    public List<DailyPractices> getPracticesByUserNameAndDate(String userName, LocalDate practiceStartDate, LocalDate practiceEndDate) {
        return dailyPracticesRepository.findByUserNameAndDateBetween(userName, practiceStartDate, practiceEndDate);
    }
}
