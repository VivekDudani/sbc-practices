package com.soulpeace.sbc.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class WeeklyTotals {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "week_number")
    private WeekInfo weekId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id")
    private UserDetails userDetails;

    @Column(name = "SSIP")
    private Boolean ssip;

    @Column(name = "SPP")
    private Boolean spp;

    @Column(name="total_chanting")
    private Integer chanting;
    private Boolean hkm;
    private Boolean scs;

    @Column(name = "SP_Posts")
    private Boolean sp;

    @Column(name = "Others")
    private Boolean ot;
}