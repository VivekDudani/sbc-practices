package com.soulpeace.sbc.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
//@Table
@Table (uniqueConstraints ={
        @UniqueConstraint(columnNames = {"weekNumber", "weekStartDate"})
})
//@Embeddable
public class WeekInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private Integer weekNumber;

    private LocalDate weekStartDate;
    private LocalDate weekEndDate;

    public WeekInfo(Integer weekNumber, LocalDate weekStartDate, LocalDate weekEndDate) {
        this.weekNumber = weekNumber;
        this.weekStartDate = weekStartDate;
        this.weekEndDate = weekEndDate;
    }
}
