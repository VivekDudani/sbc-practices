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

    @EmbeddedId
    private WeeklyId id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id", referencedColumnName = "id")
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