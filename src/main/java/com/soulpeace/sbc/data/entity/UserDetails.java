package com.soulpeace.sbc.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table (uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userName"})
})
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userName;

    private String fullName;

    @Column(columnDefinition = "boolean default true" )
    private boolean isActive = true;

    private boolean isAuthenticated;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id")
    private UserDetails userCreatedBy;

    public UserDetails(String userName, String fullName, boolean isAuthenticated, UserDetails userCreatedBy) {
        this.userName = userName;
        this.fullName = fullName;
        this.isAuthenticated = isAuthenticated;
        this.userCreatedBy = userCreatedBy;
    }
}
