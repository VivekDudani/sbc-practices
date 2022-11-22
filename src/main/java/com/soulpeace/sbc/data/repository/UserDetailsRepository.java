package com.soulpeace.sbc.data.repository;

import com.soulpeace.sbc.data.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

    @Query("From UserDetails as ud where ud.userName = ?1")
    public UserDetails findByUserName(String userName);
}
