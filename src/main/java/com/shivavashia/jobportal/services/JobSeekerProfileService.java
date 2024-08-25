package com.shivavashia.jobportal.services;

import com.shivavashia.jobportal.entity.JobSeekerProfile;
import com.shivavashia.jobportal.entity.RecruiterProfile;
import com.shivavashia.jobportal.entity.Users;
import com.shivavashia.jobportal.repository.JobSeekerProfileRepository;
import com.shivavashia.jobportal.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UserRepository userRepository;

    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UserRepository userRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.userRepository = userRepository;
    }


    public Optional<JobSeekerProfile> getOne(Integer id){
        return jobSeekerProfileRepository.findById(id);

    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            Optional<JobSeekerProfile> seekerProfile = getOne(user.getUser_id());
            return seekerProfile.orElse(null);
        }else{
            return null;
        }

    }
}
