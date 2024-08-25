package com.shivavashia.jobportal.services;

import com.shivavashia.jobportal.entity.UsersType;
import com.shivavashia.jobportal.repository.UsersTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeService {
    private final UsersTypeRepository usersTypeRepository;

    public UserTypeService(UsersTypeRepository usersTypeRepository) {
        this.usersTypeRepository = usersTypeRepository;
    }

    public List<UsersType> getAll(){
        return usersTypeRepository.findAll();
    }
}
