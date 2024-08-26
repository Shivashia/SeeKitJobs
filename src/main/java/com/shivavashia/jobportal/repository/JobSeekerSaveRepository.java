package com.shivavashia.jobportal.repository;

import com.shivavashia.jobportal.entity.JobPostActivity;
import com.shivavashia.jobportal.entity.JobSeekerProfile;
import com.shivavashia.jobportal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave,Integer> {

    public List<JobSeekerSave> findByUserId(JobSeekerProfile userAccountId);


    List<JobSeekerSave> findByJob(JobPostActivity job);
}
