package com.library.libraryservice.service;

import com.library.libraryservice.LibraryServiceApplication;
import com.library.libraryservice.dto.UsersDTO;
import com.library.libraryservice.enums.UserProfile;
import com.library.libraryservice.model.Users;
import com.library.libraryservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LibraryServiceApplication.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllUsers() {
        Users user1 = new Users(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, LocalDateTime.now(), null);
        Users user2 = new Users(null, "Jane Doe", "jane.doe@example.com", UserProfile.ADMIN, true, LocalDateTime.now(), null);
        userRepository.saveAll(List.of(user1, user2));

        List<UsersDTO> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("Jane Doe", users.get(1).getName());
    }

    @Test
    public void testSaveUser() {
        UsersDTO userDTO = new UsersDTO(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        UsersDTO savedUserDTO = userService.saveUser(userDTO);
        assertNotNull(savedUserDTO.getId());
        assertEquals("John Doe", savedUserDTO.getName());
    }

    @Test
    public void testGetUserById() {
        Users user = new Users(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        user = userRepository.save(user);

        UsersDTO foundUserDTO = userService.getUserById(user.getId());
        assertEquals(user.getId(), foundUserDTO.getId());
    }

    @Test
    public void testUpdateUser() {
        Users user = new Users(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        user = userRepository.save(user);

        UsersDTO updatedUserDTO = new UsersDTO(user.getId(), "John Smith", "john.smith@example.com", UserProfile.USER, true, null, null);
        UsersDTO result = userService.updateUser(user.getId(), updatedUserDTO);

        assertEquals("John Smith", result.getName());
        assertEquals("john.smith@example.com", result.getEmail());
    }

    @Test
    public void testDeactivateUser() {
        Users user = new Users(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        user = userRepository.save(user);

        userService.deactivateUser(user.getId());
        UsersDTO deactivatedUserDTO = userService.getUserById(user.getId());
        assertFalse(deactivatedUserDTO.isActive());
    }
}
