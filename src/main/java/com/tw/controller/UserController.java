package com.tw.controller;

import com.tw.controller.dto.UserDTO;
import com.tw.dao.entity.UserEntity;
import com.tw.service.UserService;
import com.tw.service.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author liuguanghui
 * @version V1.0
 * @Description: 用户api层
 * @Date: 2021-08-31
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok().body(copyPropertiesUserToUserDTO(user));
    }

    public static final Calendar tzUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @GetMapping
    public ResponseEntity<Page<UserEntity>> findUsersByPage(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam(value = "createdAtFrom", required = false) String createdAtFrom,
            @RequestParam(value = "createdAtTo", required = false) String createdAtTo) {
        Page<UserEntity> userPageByConditions = userService.findUserPageByConditions(page, size, name, age, createdAtFrom, createdAtTo);
        return ResponseEntity.ok().body(userPageByConditions);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<UserDTO> deleteUserById(@PathVariable Long id) {
        User user = userService.deleteUserById(id);
        return ResponseEntity.ok().body(copyPropertiesUserToUserDTO(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        User user = copyPropertiesUserDTOToUser(userDTO);
        User insertReturnUser = userService.insertUser(user);
        return ResponseEntity.ok().body(copyPropertiesUserToUserDTO(insertReturnUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        User user = copyPropertiesUserDTOToUser(userDTO);
        User insertReturnUser = userService.updateUserById(user);
        return ResponseEntity.ok().body(copyPropertiesUserToUserDTO(insertReturnUser));
    }

    private UserDTO copyPropertiesUserToUserDTO(User user) {
        return UserDTO.builder().name(user.getName()).id(user.getId()).phoneNumber(user.getPhoneNumber())
                .email(user.getEmail()).address(user.getAddress()).age(user.getAge())
                .createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }

    private User copyPropertiesUserDTOToUser(UserDTO user) {
        return User.builder().name(user.getName()).id(user.getId()).email(user.getEmail())
                .address(user.getAddress()).age(user.getAge()).createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt()).phoneNumber(user.getPhoneNumber()).build();
    }

}
