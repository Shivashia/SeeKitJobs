package com.shivavashia.jobportal.controller;


import ch.qos.logback.core.util.StringUtil;
import com.shivavashia.jobportal.entity.JobSeekerProfile;
import com.shivavashia.jobportal.entity.Skills;
import com.shivavashia.jobportal.entity.Users;
import com.shivavashia.jobportal.repository.UserRepository;
import com.shivavashia.jobportal.services.JobSeekerProfileService;
import com.shivavashia.jobportal.util.FileUploadUtil;
import org.springframework.boot.Banner;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
//@RequestMapping("/")
public class JobSeekerProfileController {
    private final JobSeekerProfileService jobSeekerProfileService;
    private final UserRepository userRepository;

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UserRepository userRepository) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.userRepository = userRepository;
    }

    @GetMapping("/job-seeker-profile/")
    public String jobSeekerProfile(Model model){
        JobSeekerProfile jobSeekerProfile=new JobSeekerProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skillsList=new ArrayList<>();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users user=userRepository.findByEmail(authentication.getName()).orElseThrow(()-> new UsernameNotFoundException("Could Not find user"));
            Optional<JobSeekerProfile> seekerProfile=jobSeekerProfileService.getOne(user.getUser_id());
            if(seekerProfile.isPresent()){
                jobSeekerProfile=seekerProfile.get();
                if(jobSeekerProfile.getSkills().isEmpty()){
                    skillsList.add(new Skills());
                    jobSeekerProfile.setSkills(skillsList);
                }
            }
            model.addAttribute("skills",skillsList);
            model.addAttribute("profile",jobSeekerProfile);
        }
        return "job-seeker-profile";

    }

    @PostMapping("/job-seeker-profile/addNew")
    public String addNew(JobSeekerProfile jobSeekerProfile, @RequestParam("image") MultipartFile image, @RequestParam("pdf") MultipartFile pdf,Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users user=userRepository.findByEmail(authentication.getName()).orElseThrow(()-> new UsernameNotFoundException("Could Not find user"));
            jobSeekerProfile.setUser_id(user);
            jobSeekerProfile.setUser_account_id(user.getUser_id());
        }
        List<Skills> skillsList=new ArrayList<>();
        model.addAttribute("profile",jobSeekerProfile);
        model.addAttribute("skills",skillsList);

        for(Skills skills:jobSeekerProfile.getSkills()){
            skills.setJob_seeker_profile(jobSeekerProfile);
        }

        //file upload for image and the resume
        String imageName="";
        String resumeName="";

        if(!Objects.equals(image.getOriginalFilename(),"")){
            imageName= StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            jobSeekerProfile.setProfile_photo(imageName);
        }
        if(!Objects.equals(pdf.getOriginalFilename(),"")){
            resumeName= StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            jobSeekerProfile.setResume(resumeName);
        }
        JobSeekerProfile seekerProfile=jobSeekerProfileService.addNew(jobSeekerProfile);

        try {
            String uploadDir="photos/candidates/"+ jobSeekerProfile.getUser_account_id();
            if(!Objects.equals(image.getOriginalFilename(),"")){
                FileUploadUtil.saveFile(uploadDir,imageName,image);
            }

            if(!Objects.equals(pdf.getOriginalFilename(),"")){
                FileUploadUtil.saveFile(uploadDir,resumeName,pdf);
            }
        }catch (IOException eo){
            throw  new RuntimeException(eo);
        }
        return "redirect:/dashboard_JobSeeker/";
    }
}
