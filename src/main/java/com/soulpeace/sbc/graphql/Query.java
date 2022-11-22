package com.soulpeace.sbc.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.soulpeace.sbc.data.entity.DailyPractices;
import com.soulpeace.sbc.data.repository.DailyPracticesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
@AllArgsConstructor
public class Query implements GraphQLQueryResolver {

    private final DailyPracticesRepository dailyPracticesRepository;

    public String firstQuery () {
        return "First Query";
    }

    public List<DailyPractices> getAllDailyPractices() {
        System.out.println("getAllDailyPractices invoked");
        return dailyPracticesRepository.findAll();
    }
}
