package com.shivavashia.jobportal.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String experience_level;

    private String name;

    private String years_of_experience;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_seeker_profile")
    private JobSeekerProfile job_seeker_profile;

    public Skills() {
    }

    public Skills(Integer id, String experience_level, String name, String years_of_experience, JobSeekerProfile job_seeker_profile) {
        this.id = id;
        this.experience_level = experience_level;
        this.name = name;
        this.years_of_experience = years_of_experience;
        this.job_seeker_profile = job_seeker_profile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExperience_level() {
        return experience_level;
    }

    public void setExperience_level(String experience_level) {
        this.experience_level = experience_level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYears_of_experience() {
        return years_of_experience;
    }

    public void setYears_of_experience(String years_of_experience) {
        this.years_of_experience = years_of_experience;
    }

    public JobSeekerProfile getJob_seeker_profile() {
        return job_seeker_profile;
    }

    public void setJob_seeker_profile(JobSeekerProfile job_seeker_profile) {
        this.job_seeker_profile = job_seeker_profile;
    }

    @Override
    public String toString() {
        return "Skills{" +
                "id=" + id +
                ", experience_level='" + experience_level + '\'' +
                ", name='" + name + '\'' +
                ", years_of_experience='" + years_of_experience + '\'' +
                ", job_seeker_profile=" + job_seeker_profile +
                '}';
    }
}
