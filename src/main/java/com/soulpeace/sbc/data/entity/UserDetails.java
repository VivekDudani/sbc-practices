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

    public UserDetails(String userName, String fullName) {
        this.userName = userName;
        this.fullName = fullName;
    }
}
