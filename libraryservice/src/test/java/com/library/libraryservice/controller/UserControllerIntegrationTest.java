package com.library.libraryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryservice.LibraryServiceApplication;
import com.library.libraryservice.dto.UsersDTO;
import com.library.libraryservice.enums.UserProfile;
import com.library.libraryservice.repository.UserRepository;
import com.library.libraryservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LibraryServiceApplication.class)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        UsersDTO user1 = new UsersDTO(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        UsersDTO user2 = new UsersDTO(2L, "Jane Doe", "jane.doe@example.com", UserProfile.ADMIN, true, null, null);
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    public void testCreateUser() throws Exception {
        UsersDTO userDTO = new UsersDTO(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);

        Mockito.when(userService.saveUser(Mockito.any(UsersDTO.class))).thenAnswer(invocation -> {
            UsersDTO newUser = invocation.getArgument(0);
            return new UsersDTO(1L, newUser.getName(), newUser.getEmail(), newUser.getProfile(),
                    newUser.isActive(), newUser.getCreatedDate(), null);
        });

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UsersDTO userDTO = new UsersDTO(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        userDTO.setId(1L);
        Mockito.when(userService.updateUser(Mockito.anyLong(), Mockito.any(UsersDTO.class))).thenAnswer(invocation -> {
            UsersDTO updatedUser = invocation.getArgument(1);
            return new UsersDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(),
                    updatedUser.getProfile(), updatedUser.isActive(), userDTO.getCreatedDate(), null);
        });

        mockMvc.perform(put("/users/{id}", userDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    public void testDeactivateUser() throws Exception {
        UsersDTO userDTO = new UsersDTO(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        userDTO.setId(1L);

        Mockito.doAnswer(invocation -> {
            Long userId = invocation.getArgument(0);
            UsersDTO deactivatedUser = new UsersDTO(userDTO.getId(), userDTO.getName(), userDTO.getEmail(),
                    userDTO.getProfile(), false, userDTO.getCreatedDate(), null);
            Mockito.when(userService.getUserById(userId)).thenReturn(deactivatedUser);
            return null;
        }).when(userService).deactivateUser(Mockito.anyLong());

        mockMvc.perform(put("/users/deactivate/{id}", userDTO.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/{id}", userDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }
}
