package com.soulpeace.sbc.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table (uniqueConstraints ={
        @UniqueConstraint(columnNames = {"week_info_id", "user_details_id"})
})
public class WeeklyTotals {

    @Id
    @SequenceGenerator(name = "sequence_weekly_totals")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToOne(cascade = {CascadeType.ALL/*, CascadeType.MERGE, CascadeType.REFRESH*/})
    @JoinColumn(name = "week_info_id")
    private WeekInfo weekInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id")
    private UserDetails userDetails;

    @Column(name = "SSIP")
    private Boolean ssip = false;

    @Column(name = "SPP")
    private Boolean spp = false;

    @Column(name="total_chanting")
    private Integer chanting = 0;

    private Boolean hkm = false;
    private Boolean scs = false;
    private Boolean pf = false;
    private Boolean rr = false;

    @Column(name = "SP_Posts")
    private Boolean sp = false;

    @Column(name = "bg_sb_cc")
    private Boolean bg = false;

    @Column(name = "Others")
    private Boolean ot = false;

    public void incrementChantingCountBy(int count) {
        chanting += count;
    }

    public void resetPractices() {
        ssip = false;
        spp = false;
        chanting = 0;
        hkm = false;
        scs = false;
        pf = false;
        rr = false;
        sp = false;
        bg = false;
        ot = false;
    }
}