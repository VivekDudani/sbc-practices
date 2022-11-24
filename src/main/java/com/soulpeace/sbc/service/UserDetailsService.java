package com.soulpeace.sbc.service;

import com.soulpeace.sbc.data.entity.UserDetails;

import java.util.Set;

public interface UserDetailsService {

    public UserDetails findByUserName(String userName);

    public UserDetails findByUserNameIgnoreCase(String userName);

    public Set<UserDetails> findAllByUserCreatedBy(String userCreatedBy);

    public UserDetails activateDeactivateUser(String userName, boolean isActive);

    public UserDetails getOrCreateUserDetails(String userName, String fullName, boolean isAuthenticated, String userCreatedBy);
}
