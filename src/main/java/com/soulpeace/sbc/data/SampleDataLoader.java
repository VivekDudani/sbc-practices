package com.soulpeace.sbc.data;

import com.github.javafaker.Faker;
import com.soulpeace.sbc.controller.DailyPracticesController;
import com.soulpeace.sbc.data.entity.DailyPractices;
import com.soulpeace.sbc.data.entity.UserDetails;
import com.soulpeace.sbc.data.repository.DailyPracticesRepository;
import com.soulpeace.sbc.data.repository.UserDetailsRepository;
import com.soulpeace.sbc.service.WeekInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@AllArgsConstructor
@Component
public class SampleDataLoader implements CommandLineRunner {

    private final DailyPracticesRepository dailyPracticesRepository;

    private final WeekInfoService weekInfoService;

    private final DailyPracticesController dailyPracticesController;

    @Override
    public void run(String... args) {
        log.info("Loading sample data...");
        Faker faker = new Faker();

        addDailyPracticeAndUserDetailsData(faker);
    }

    private void addDailyPracticeAndUserDetailsData(Faker faker) {
        List<String> userNames = List.of("radhe.krishna", "mahaprabhu", "ram");
        List<DailyPractices> dailyPracticesList = IntStream.rangeClosed(1, 200)
                .mapToObj(i -> {
//                    UserDetails userDetails = new UserDetails(faker.name().username(), faker.name().fullName(), faker.bool().bool(), null);
                    LocalDate practiceDate = LocalDate.ofInstant(faker.date().past(10, TimeUnit.DAYS).toInstant(), ZoneId.of("UTC")); /*LocalDate.now()*/ /*new Timestamp(Instant.now().toEpochMilli())*/
                    weekInfoService.addWeekInformation(practiceDate);
                    return dailyPracticesController.createOrUpdateDailyPractice(faker.name().username(), faker.name().fullName(),
                            practiceDate,
                            faker.bool().bool(), faker.bool().bool(),
                            faker.random().nextInt(100), faker.random().nextInt(10), faker.random().nextInt(5),
                            faker.random().nextInt(5), faker.random().nextInt(10), faker.random().nextInt(10),
                            "SP-"+faker.random().nextInt(650),
                            faker.random().nextInt(3), "BG-"+faker.random().nextInt(55),
                            faker.random().nextInt(5), "Others",
                            faker.bool().bool(), null);
                })
                .collect(Collectors.toList());
        dailyPracticesRepository.saveAll(dailyPracticesList);
    }
}
