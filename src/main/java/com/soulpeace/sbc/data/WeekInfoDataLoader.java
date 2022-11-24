package com.soulpeace.sbc.data;

import com.soulpeace.sbc.data.repository.WeekInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.Locale;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Slf4j
@AllArgsConstructor
@Component
public class WeekInfoDataLoader implements CommandLineRunner {

    public static final String TIME_ZONE = "UTC";

    private final WeekInfoRepository weekInfoRepository;

    @Override
    public void run(String... args) throws Exception {
//        log.info("Loading Week Information Data...");
//        clearAllData();
//        populateWeekInfo(2020, 2050);
    }

    /*public static void main(String[] args) {
        populateWeekInfo(2022, 2050);
    }*/

    private static void populateWeekInfo(int yearStart, int yearEnd) {

        LocalDate today = LocalDate.now();

        LocalDate monday = today.with(previousOrSame(MONDAY));
        LocalDate sunday = today.with(nextOrSame(SUNDAY));

        System.out.println("Today: " + today);
        System.out.println("Monday of the Week: " + monday);
        System.out.println("Sunday of the Week: " + sunday);

        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDate.ofYearDay(yearStart, 1),
                LocalTime.of(0, 0), ZoneId.of(TIME_ZONE)).plusWeeks(1);
        DayOfWeek dayOfWeek = zonedDateTime.getDayOfWeek();
        int weekNo = zonedDateTime.get(WeekFields.of(Locale.ENGLISH).weekOfYear());
        LocalDate localDate = zonedDateTime.toLocalDate();
        LocalDate weekStart = localDate.with(previousOrSame(MONDAY));
        LocalDate weekEnd = localDate.with(nextOrSame(SUNDAY));

        System.out.println("Day of Week: " + dayOfWeek.toString());
        System.out.println("Week Number: " + weekNo);
        System.out.println("Current Date: " + localDate);
        System.out.println("Start of the Week: " + weekStart);
        System.out.println("End of the Week: " + weekEnd);

        /*for (int i=yearStart; i<=yearEnd; i++) {

        }*/
    }

    private void clearAllData() {
        weekInfoRepository.deleteAll();
        log.info("Cleared all data from WeekInfo.");
    }
}
