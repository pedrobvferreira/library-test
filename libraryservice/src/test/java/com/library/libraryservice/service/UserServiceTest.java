package com.library.libraryservice.service;

import com.library.libraryservice.dto.UsersDTO;
import com.library.libraryservice.enums.UserProfile;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.model.Users;
import com.library.libraryservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllUsers() {
        Users user1 = new Users(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        Users user2 = new Users(2L, "Jane Doe", "jane.doe@example.com", UserProfile.ADMIN, true, null, null);
        List<Users> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<UsersDTO> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    public void testGetUserById() {
        Users user = new Users(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UsersDTO result = userService.getUserById(1L);
        assertEquals(UsersDTO.fromEntity(user), result);
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testSaveUser() {
        Users user = new Users(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        UsersDTO userDTO = UsersDTO.fromEntity(user);
        when(userRepository.save(any(Users.class))).thenReturn(user);

        UsersDTO result = userService.saveUser(userDTO);
        assertEquals(userDTO, result);
    }

    @Test
    public void testUpdateUser() {
        Users existingUser = new Users(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        Users updatedUser = new Users(1L, "John Smith", "john.smith@example.com", UserProfile.USER, true, null, null);
        UsersDTO updatedUserDTO = UsersDTO.fromEntity(updatedUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(Users.class))).thenReturn(updatedUser);

        UsersDTO result = userService.updateUser(1L, updatedUserDTO);
        assertEquals(updatedUserDTO, result);
    }

    @Test
    public void testDeactivateUser() {
        Users user = new Users(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deactivateUser(1L);

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(userRepository).save(userCaptor.capture());
        Users savedUser = userCaptor.getValue();

        assertEquals(1L, savedUser.getId());
        assertFalse(savedUser.isActive());
    }
}