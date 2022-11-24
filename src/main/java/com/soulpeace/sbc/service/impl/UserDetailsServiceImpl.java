package com.soulpeace.sbc.service.impl;

import com.soulpeace.sbc.data.entity.UserDetails;
import com.soulpeace.sbc.data.repository.UserDetailsRepository;
import com.soulpeace.sbc.service.UserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails findByUserName(String userName) {
        UserDetails userDetails = userDetailsRepository.findByUserName(userName);
        return Optional.ofNullable(userDetails).orElseThrow(() ->
                new IllegalStateException("User entry not found for given UserName: " + userName));
    }

    @Override
    public UserDetails findByUserNameIgnoreCase(String userName) {
        UserDetails userDetails = userDetailsRepository.findByUserNameIgnoreCase(userName);
        return Optional.ofNullable(userDetails).orElseThrow(() ->
                new IllegalStateException("User entry not found for given UserName: " + userName));
    }

    @Override
    public Set<UserDetails> findAllByUserCreatedBy(String userCreatedBy) {
        UserDetails userDetails = findByUserNameIgnoreCase(userCreatedBy);
        return userDetailsRepository.findAllByUserCreatedByAndIsActiveTrue(userDetails);
    }

    @Override
    public UserDetails activateDeactivateUser(String userName, boolean isActive) {
        return userDetailsRepository.activateDeactivateUser(userName, isActive);
    }

    @Override
    public UserDetails getOrCreateUserDetails(String userName, String fullName, boolean isAuthenticated,
                                               String userCreatedBy) {
        UserDetails userDetailCreatedBy = null;
        if (userCreatedBy != null && !userCreatedBy.isEmpty()) {
            userDetailCreatedBy = userDetailsRepository.findByUserNameIgnoreCase(userCreatedBy);
            Optional.ofNullable(userDetailCreatedBy).orElseThrow(() ->
                    new IllegalStateException("Main user entry is missing for UserCreatedBy: " + userCreatedBy));
        }
        UserDetails userDetail = userDetailsRepository.findByUserNameIgnoreCase(userName);
        if (userDetail == null) {
            log.info("Creating new User with UserName: {} and FullName: {}", userName, fullName);
            userDetail = new UserDetails(userName, fullName, isAuthenticated, userDetailCreatedBy);
        }
        return userDetail;
    }
}
