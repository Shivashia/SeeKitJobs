package com.shivavashia.jobportal.services;

import com.shivavashia.jobportal.entity.Users;
import com.shivavashia.jobportal.repository.UserRepository;
import com.shivavashia.jobportal.util.CustomerUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user=userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Could not Find the User"));
        return new CustomerUserDetails(user);
    }
}
