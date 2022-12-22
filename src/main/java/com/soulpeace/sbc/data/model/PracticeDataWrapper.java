package com.soulpeace.sbc.data.model;

import com.soulpeace.sbc.data.entity.DailyPractices;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class PracticeDataWrapper {

    private String userName;
    private List<DailyPractices> practices;
}
