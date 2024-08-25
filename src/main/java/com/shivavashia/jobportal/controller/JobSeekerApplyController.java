package com.shivavashia.jobportal.controller;


import com.shivavashia.jobportal.entity.*;
import com.shivavashia.jobportal.services.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UserService userService;

    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;

    public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UserService userService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService, RecruiterProfileService recruiterProfileService, JobSeekerProfileService jobSeekerProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.userService = userService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("/job-details-apply/{id}")
    public String displayDetails(@PathVariable("id") int id, Model model){
        JobPostActivity jobDetails=jobPostActivityService.getOne(id);
        List<JobSeekerApply> jobSeekerApplyList=jobSeekerApplyService.getJobCandidates(jobDetails);
        List<JobSeekerSave> jobSeekerSaveList=jobSeekerSaveService.getJobCandidates(jobDetails);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                RecruiterProfile user=recruiterProfileService.getCurrentRecruiterProfile();
                if(user!=null){
                    model.addAttribute("applyList",jobSeekerApplyList);
                }
                else{
                    JobSeekerProfile jobSeekerProfile=jobSeekerProfileService.getCurrentSeekerProfile();
                    if(user!=null){
                        boolean exists=false;
                        boolean saved=false;
                        for(JobSeekerApply jobSeekerApply:jobSeekerApplyList){
                            if(jobSeekerApply.getUserId().getUser_account_id()==user.getUser_account_id()){
                                exists=true;
                                break;
                            }
                        }
                        for(JobSeekerSave jobSeekerSave:jobSeekerSaveList){
                            if(jobSeekerSave.getUserId().getUser_account_id()==user.getUser_account_id()){
                                saved=true;
                                break;
                            }
                        }
//                        sds
                        model.addAttribute("alreadyApplied",exists);
                        model.addAttribute("alreadySaved", saved);
                    }
                }
            }
//            String username
        }

        JobSeekerApply jobSeekerApply=new JobSeekerApply();
        model.addAttribute("applyJob",jobSeekerApply);
        model.addAttribute("jobDetails",jobDetails);
        model.addAttribute("user",userService.getCurrentUserProfile());
        return "job-details";
    }


    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id,JobSeekerApply jobSeekerApply){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            Users user=userService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> jobSeekerProfile=jobSeekerProfileService.getOne(user.getUser_id());
            JobPostActivity jobPostActivity=jobPostActivityService.getOne(id);
            if(jobSeekerProfile.isPresent() && jobPostActivity!=null){
                jobSeekerApply.setUserId(jobSeekerProfile.get());
                jobSeekerApply.setJob(jobPostActivity);
                jobSeekerApply.setApplyDate(new Date());
            }
            else{
                throw new RuntimeException("User Not Found");
            }
            jobSeekerApplyService.addNew(jobSeekerApply);
        }
        return "redirect:/dashboard_JobSeeker/";
    }

//    public String
}
