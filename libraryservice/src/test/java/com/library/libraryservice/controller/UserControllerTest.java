package com.library.libraryservice.controller;

import com.library.libraryservice.dto.UsersDTO;
import com.library.libraryservice.enums.UserProfile;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.model.Users;
import com.library.libraryservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetAllUsers() {
        UsersDTO user1 = new UsersDTO(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        UsersDTO user2 = new UsersDTO(2L, "Jane Doe", "jane.doe@example.com", UserProfile.ADMIN, true, null, null);
        List<UsersDTO> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UsersDTO>> response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UsersDTO> result = response.getBody();
        assert result != null;
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    public void testGetUserById() {
        UsersDTO user = new UsersDTO(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        when(userService.getUserById(1L)).thenReturn(user);

        ResponseEntity<UsersDTO> response = userController.getUserById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userService.getUserById(anyLong())).thenThrow(new NotFoundException());

        ResponseEntity<UsersDTO> response = userController.getUserById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateUser() {
        UsersDTO user = new UsersDTO(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        when(userService.saveUser(any(UsersDTO.class))).thenReturn(user);

        ResponseEntity<UsersDTO> response = userController.createUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUser() {
        UsersDTO updatedUser = new UsersDTO(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        when(userService.updateUser(anyLong(), any(UsersDTO.class))).thenReturn(updatedUser);

        ResponseEntity<UsersDTO> response = userController.updateUser(1L, updatedUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    public void testDeactivateUser() {
        doNothing().when(userService).deactivateUser(anyLong());

        ResponseEntity<Void> response = userController.deactivateUser(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deactivateUser(1L);
    }
}
