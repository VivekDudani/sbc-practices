package com.soulpeace.sbc.service;

import com.soulpeace.sbc.data.entity.WeekInfo;

import java.time.LocalDate;

public interface WeekInfoService {

    WeekInfo addWeekInformation(LocalDate currentDate);

    WeekInfo getWeekInformationByGivenDate(LocalDate currentDate);
}
