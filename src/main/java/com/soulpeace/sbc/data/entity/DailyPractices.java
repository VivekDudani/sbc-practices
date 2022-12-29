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

    private Integer chanting = 0;
    private Integer hkm = 0;
    private Integer scs = 0;
    private Integer pf = 0;
    private Integer rr = 0;

    @Column(name = "sp_posts_count")
    private Integer spPostCount = 0;

    @Column(name = "sp_posts")
    private String sp;

    @Column(name = "bg_sb_cc_count")
    private Integer bgCount = 0;

    @Column(name = "BG_SB_CC")
    private String bg;

    @Column(name = "others_count")
    private Integer otCount = 0;

    @Column(name = "Others")
    private String ot;
}
