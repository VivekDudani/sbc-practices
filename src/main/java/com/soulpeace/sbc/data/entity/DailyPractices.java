package com.soulpeace.sbc.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table (uniqueConstraints ={
        @UniqueConstraint(columnNames = {"user_details_id", "practiceDate"})
})
public class DailyPractices {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id"/*, referencedColumnName = "id"*/)
    private UserDetails userDetails;

//    private Timestamp practiceDate;
    //TODO Change it to LocalDateTime
    private LocalDate practiceDate;

    @Column(name = "SSIP")
    private Boolean ssip;

    @Column(name = "SPP")
    private Boolean spp;

    private Integer chanting;
    private Integer hkm;
    private Integer scs;
    private Integer pf;

    @Column(name = "SP_Posts")
    private String sp;

    @Column(name = "BG_SB_CC")
    private String bg;

    @Column(name = "Others")
    private String ot;
}
