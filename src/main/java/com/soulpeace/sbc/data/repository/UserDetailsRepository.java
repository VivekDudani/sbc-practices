package com.soulpeace.sbc.data.repository;

import com.soulpeace.sbc.data.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

    @Query("From UserDetails as ud where ud.userName = ?1")
    public UserDetails findByUserName(String userName);

    public UserDetails findByUserNameIgnoreCase(String userName);

    public Set<UserDetails> findAllByUserCreatedByAndIsActiveTrue(UserDetails userCreatedBy);

    @Query("Update UserDetails ud set ud.isActive = ?1 where ud.userName = ?2")
    public UserDetails activateDeactivateUser(String userName, boolean isActive);

}
