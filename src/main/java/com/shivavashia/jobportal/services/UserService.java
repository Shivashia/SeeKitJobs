package com.shivavashia.jobportal.services;


import com.shivavashia.jobportal.entity.JobSeekerProfile;
import com.shivavashia.jobportal.entity.RecruiterProfile;
import com.shivavashia.jobportal.entity.Users;
import com.shivavashia.jobportal.repository.JobSeekerProfileRepository;
import com.shivavashia.jobportal.repository.RecruiterProfileRepository;
import com.shivavashia.jobportal.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, JobSeekerProfileRepository jobSeekerProfileRepository, RecruiterProfileRepository recruiterProfileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;

        this.passwordEncoder = passwordEncoder;
    }

    public Users addNew(Users users){
        users.setIs_active("Y");
        users.setRegistration_date(new Date(System.currentTimeMillis()));
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users savedusers=userRepository.save(users);
        int userTypeId = users.getUser_type_id().getUser_type_id();
        if(userTypeId==1){
            recruiterProfileRepository.save(new RecruiterProfile(users));
        }
        else{
            jobSeekerProfileRepository.save(new JobSeekerProfile(users));
        }
        return savedusers;

//        userRepository.save(users);
    }

    public Optional<Users> getUserByEmailId(String email){
        return userRepository.findByEmail(email);
    }

    public Object getCurrentUserProfile() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username=authentication.getName();
            Users user=userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Could Not find user"));
            int userid=user.getUser_id();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                RecruiterProfile recruiterProfile= recruiterProfileRepository.findById(userid).orElse(new RecruiterProfile());
                return recruiterProfile;
            }else{
                JobSeekerProfile jobSeekerProfile= jobSeekerProfileRepository.findById(userid).orElse(new JobSeekerProfile());
                return  jobSeekerProfile;
            }
        }
        return null;
    }

    public Users getCurrentUser() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username=authentication.getName();
            Users user=userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Could Not find user"));
            return user;
        }
        return null;
    }

    public Users findByEmail(String currentUsername) {
        return userRepository.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("Could Not find user"));
    }
}
