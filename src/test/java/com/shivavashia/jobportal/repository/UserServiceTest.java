package com.shivavashia.jobportal.repository;

import com.shivavashia.jobportal.entity.Users;
import com.shivavashia.jobportal.entity.UsersType;
import com.shivavashia.jobportal.repository.UserRepository;
import com.shivavashia.jobportal.services.UserService;
import com.shivavashia.jobportal.services.UserTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private UserTypeService userTypeService;

    @Mock
    private RecruiterProfileRepository recruiterProfileRepository;

    @Test
    public void recruiter_Profile_Save_Return_Users() {
        // Arrange
        UsersType usersType = new UsersType();
        usersType.setUser_type_id(1);
        usersType.setUser_type_name("Recruiter");

        Users test_user = new Users();
        test_user.setEmail("test_user");
        test_user.setPassword("123");
        test_user.setUser_type_id(usersType);

        // Mock the behavior of passwordEncoder and userRepository
        when(passwordEncoder.encode("123")).thenReturn("encoded_password");
        Users saved_user = new Users();
        saved_user.setUser_id(1);  // Mock the ID generation
        saved_user.setEmail("test_user");
        saved_user.setPassword("encoded_password");
        saved_user.setUser_type_id(usersType);

        when(userRepository.save(any(Users.class))).thenReturn(saved_user);

        // Act
        Users result = userService.addNew(test_user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUser_id()).isGreaterThan(0);
        assertThat(result.getPassword()).isEqualTo("encoded_password");
        assertThat(result.getEmail()).isEqualTo("test_user");
        assertThat(result.getUser_type_id()).isEqualTo(usersType);
    }
}
