package com.shivavashia.jobportal.services;


import com.shivavashia.jobportal.entity.*;
import com.shivavashia.jobportal.repository.JobPostActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JobPostActivityService {
    private final JobPostActivityRepository jobPostActivityRepository;

    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity){
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobsDTO> getRecruiterJobs(int recruiter){
        List<IRecruiterJob> recruiterJobsDTO=jobPostActivityRepository.getRecruiterJobs(recruiter);

        List<RecruiterJobsDTO> recruiterJobsDTOList=new ArrayList<>();
        for(IRecruiterJob rec: recruiterJobsDTO){
            JobLocation loc=new JobLocation(rec.getLocationId(),rec.getCity(),rec.getState(),rec.getCountry());
            JobCompany company=new JobCompany(rec.getCompanyId(),rec.getName(),"");
            recruiterJobsDTOList.add(new RecruiterJobsDTO(rec.getTotalCandidates(),rec.getJob_post_id(),rec.getJob_title(),loc,company));
        }
        return recruiterJobsDTOList;
    }

    public JobPostActivity getOne(int id) {
        return jobPostActivityRepository.findById(id).orElseThrow(()-> new RuntimeException("Job Not Found"));
    }

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote, LocalDate searchDate) {
        return Objects.isNull(searchDate)?jobPostActivityRepository.searchWithoutDate(job,location,remote,type)
                :jobPostActivityRepository.search(job,location,remote,type,searchDate);

    }
}
