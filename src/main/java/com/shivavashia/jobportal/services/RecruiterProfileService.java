package com.shivavashia.jobportal.services;


import com.shivavashia.jobportal.entity.RecruiterProfile;
import com.shivavashia.jobportal.entity.Users;
import com.shivavashia.jobportal.repository.RecruiterProfileRepository;
import com.shivavashia.jobportal.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UserRepository userRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    public Optional<RecruiterProfile> getOne(Integer id){
        return recruiterProfileRepository.findById(id);
    }

    public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
        return recruiterProfileRepository.save(recruiterProfile);
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            Optional<RecruiterProfile> recruiterProfile = getOne(user.getUser_id());
            return recruiterProfile.orElse(null);
        }else{
            return null;
        }
    }
}
