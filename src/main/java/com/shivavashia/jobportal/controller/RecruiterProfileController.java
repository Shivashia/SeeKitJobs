package com.shivavashia.jobportal.controller;


import ch.qos.logback.core.util.StringUtil;
import com.shivavashia.jobportal.entity.RecruiterProfile;
import com.shivavashia.jobportal.entity.Users;
import com.shivavashia.jobportal.repository.UserRepository;
import com.shivavashia.jobportal.services.RecruiterProfileService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
public class RecruiterProfileController {
    private final UserRepository userRepository;
    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(UserRepository userRepository, RecruiterProfileService recruiterProfileService) {
        this.userRepository = userRepository;
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping("/recruiter-profile/")
    public String recruiterProfileForm(Model model){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
           Users user= userRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not find the user."));
           Optional<RecruiterProfile> recruiterProfile=recruiterProfileService.getOne(user.getUser_id());
           if(!recruiterProfile.isEmpty()){
               model.addAttribute("profile", recruiterProfile);
           }
        }
        return "recruiter_profile";
    }

    @PostMapping("/recruiter-profile/addNew")
    public String addNew(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multiPartFile, Model model){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            Users user= userRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not find the user."));
            recruiterProfile.setUser_id(user);
            recruiterProfile.setUser_account_id(user.getUser_id());
        }
        model.addAttribute("profile",recruiterProfile);
        //setting profile pic
        String fileName="";
        if(!multiPartFile.getOriginalFilename().equals("")){
            fileName= StringUtils.cleanPath(Objects.requireNonNull(multiPartFile.getOriginalFilename()));
            recruiterProfile.setProfile_photo(fileName);
        }
        RecruiterProfile savedUser=recruiterProfileService.addNew(recruiterProfile);

        String uploadDir="photos/recruiter/"+savedUser.getUser_account_id();
        try {
            System.out.println("Here Before Save ");
            FileUploadUtil.saveFile(uploadDir,fileName,multiPartFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/dashboard_Recruiter/";
    }
}
