package com.library.libraryservice.service;

import com.library.libraryservice.dto.UsersDTO;
import com.library.libraryservice.dto.RentalsDTO;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.model.Users;
import com.library.libraryservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UsersDTO> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream().map(UsersDTO::fromEntity).collect(Collectors.toList());
    }

    public UsersDTO getUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return UsersDTO.fromEntity(user);
    }

    public UsersDTO saveUser(UsersDTO userDTO) {
        Users user = userDTO.toEntity();
        user = userRepository.save(user);
        return UsersDTO.fromEntity(user);
    }

    public UsersDTO updateUser(Long id, UsersDTO updatedUserDTO) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUserDTO.getName());
            existingUser.setEmail(updatedUserDTO.getEmail());
            existingUser.setProfile(updatedUserDTO.getProfile());
            existingUser.setActive(updatedUserDTO.isActive());
            existingUser.setRentals(updatedUserDTO.getRentals().stream().map(RentalsDTO::toEntity).collect(Collectors.toList()));
            Users updatedUser = userRepository.save(existingUser);
            return UsersDTO.fromEntity(updatedUser);
        }).orElseThrow(NotFoundException::new);
    }

    public void deactivateUser(Long id) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            user.setActive(false);
            userRepository.save(user);
        }, () -> {
            throw new NotFoundException();
        });
    }
}
