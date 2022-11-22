package com.soulpeace.sbc.data.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class WeeklyId implements Serializable {
    private int id;
    private String weekNumber;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
}
