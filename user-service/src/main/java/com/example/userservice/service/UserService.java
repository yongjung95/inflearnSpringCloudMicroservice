package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.repository.UserEntity;
import com.example.userservice.vo.ResponseUser;

import java.util.List;

public interface UserService {

    UserDto createdUser(UserDto userDto);

    UserDto getUserByUserId(String userId);

    List<ResponseUser> getUserByAll();
}
