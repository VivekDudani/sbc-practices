package com.soulpeace.sbc.data;

import com.github.javafaker.Faker;
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

    @Override
    public void run(String... args) {
        log.info("Loading sample data...");
        Faker faker = new Faker();

        addDailyPracticeAndUserDetailsData(faker);
    }

    private void addDailyPracticeAndUserDetailsData(Faker faker) {
        List<DailyPractices> dailyPracticesList = IntStream.rangeClosed(1,100)
                .mapToObj(i -> {
                    UserDetails userDetails = new UserDetails(faker.name().username(), faker.name().fullName(), faker.bool().bool(), null);
                    LocalDate practiceDate = LocalDate.ofInstant(faker.date().past(2021, TimeUnit.DAYS).toInstant(), ZoneId.of("UTC")); /*LocalDate.now()*/ /*new Timestamp(Instant.now().toEpochMilli())*/
                    weekInfoService.addWeekInformation(practiceDate);
                    return new DailyPractices(i, userDetails, practiceDate,
                            faker.bool().bool(), faker.bool().bool(),
                            faker.random().nextInt(100), faker.random().nextInt(10), faker.random().nextInt(5),
                            faker.random().nextInt(5), "SP-"+faker.random().nextInt(550),
                            "BG-"+faker.random().nextInt(55), "Others");
                })
                .collect(Collectors.toList());
        dailyPracticesRepository.saveAll(dailyPracticesList);
    }
}
