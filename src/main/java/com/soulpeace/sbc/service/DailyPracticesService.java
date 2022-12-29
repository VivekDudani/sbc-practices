package com.soulpeace.sbc.service;

import com.soulpeace.sbc.data.entity.DailyPractices;
import com.soulpeace.sbc.data.entity.UserDetails;
import com.soulpeace.sbc.data.model.PracticeDataWrapper;

import java.time.LocalDate;
import java.util.List;

public interface DailyPracticesService {

    DailyPractices addDailyPractices(UserDetails userDetail, LocalDate practiceDate, Boolean ssip, Boolean spp, Integer chanting,
                                     Integer hkm, Integer scs, Integer pf, Integer rr, Integer spPostCount, String spPost,
                                     Integer bgCount, String bg, Integer otCount, String others);

    List<DailyPractices> getAllDailyPractices();

    List<DailyPractices> getAllPracticesByUserName(String userName);

    List<DailyPractices> getPracticesByDateRangeSortedByUser(LocalDate practiceStartDate, LocalDate practiceEndDate);

    List<PracticeDataWrapper> getPracticesByDateRangeSortedByUserAndDate(LocalDate practiceStartDate, LocalDate practiceEndDate);

    List<DailyPractices> getPracticesByUserNameAndDateSortedByDate(String userName, LocalDate practiceStartDate, LocalDate practiceEndDate);
}
