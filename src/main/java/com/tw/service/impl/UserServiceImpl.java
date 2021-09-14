package com.tw.service.impl;

import com.tw.constant.UserConstant;
import com.tw.dao.UserRepository;
import com.tw.dao.UserSpecification;
import com.tw.dao.entity.UserEntity;
import com.tw.exception.DataNotFoundException;
import com.tw.exception.DataValidateException;
import com.tw.service.UserService;
import com.tw.service.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author liuguanghui
 * @version V1.0
 * @Description: 用户业务层实现
 * @Date: 2021-08-31
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Resource
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User getUserById(long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new DataNotFoundException(UserConstant.Message.DATA_NOT_FOUND);
        }
        return copyPropertiesUserEntityToUser(user.get());
    }

    @Override
    public User insertUser(User user) {
        if (user == null) {
            throw new DataValidateException(UserConstant.Message.USER_NOT_FOUND);
        }
        if (user.getName() == null || "".equals(user.getName())) {
            throw new DataValidateException(UserConstant.Message.USERNAME_NOT_FOUND);
        }
        logger.info("insert user = {}", user);
        UserEntity userEntity = copyPropertiesUserToUserEntity(user);
        userEntity.setCreatedAt(LocalDateTime.now());
        UserEntity saveUser = userRepository.save(userEntity);
        user.setId(saveUser.getId());
        return user;
    }

    @Override
    public User deleteUserById(long id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new DataNotFoundException(UserConstant.Message.DATA_NOT_FOUND);
        }
        logger.info("delete user = {}", userOptional.get());
        userRepository.deleteById(id);
        return copyPropertiesUserEntityToUser(userOptional.get());
    }

    @Override
    public User updateUserById(User updateUser) {
        if (updateUser == null) {
            throw new DataValidateException(UserConstant.Message.USER_NOT_FOUND);
        }
        Optional<UserEntity> userOptional = userRepository.findById(updateUser.getId());
        if (!userOptional.isPresent()) {
            throw new DataNotFoundException(UserConstant.Message.DATA_NOT_FOUND);
        }
        logger.info("update user old = {}, new = {}",userOptional.get(), updateUser);
        UserEntity userEntity = copyPropertiesUserToUserEntity(updateUser);
        userEntity.setUpdatedAt(LocalDateTime.now());
        userEntity.setCreatedAt(userOptional.get().getCreatedAt());
        UserEntity userRe = userRepository.saveAndFlush(userEntity);
        return copyPropertiesUserEntityToUser(userRe);
    }

    @Override
    public Page<UserEntity> findUserPageByConditions(Integer page, Integer size, String name, Integer age,
                                                     String createdAtFrom, String createdAtTo) {
        //构造查询条件
        Specification<UserEntity> specification = new UserSpecification(name,age,createdAtFrom,createdAtTo);
        return userRepository.findAll(specification, PageRequest.of(page - 1,
                size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }


    private UserEntity copyPropertiesUserToUserEntity(User user) {
        return UserEntity.builder().name(user.getName()).id(user.getId()).phoneNumber(user.getPhoneNumber())
                .email(user.getEmail()).address(user.getAddress()).age(user.getAge())
                .createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }


    private User copyPropertiesUserEntityToUser(UserEntity userEntity) {
        return User.builder().name(userEntity.getName()).id(userEntity.getId()).email(userEntity.getEmail())
                .address(userEntity.getAddress()).age(userEntity.getAge()).createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt()).phoneNumber(userEntity.getPhoneNumber()).build();
    }
}
