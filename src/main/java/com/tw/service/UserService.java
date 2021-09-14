package com.tw.service;

import com.tw.dao.entity.UserEntity;
import com.tw.service.domain.User;
import org.springframework.data.domain.Page;

/**
 * @author liuguanghui
 * @version V1.0
 * @Description: 用户接口层
 * @Date: 2021-08-31
 */
public interface UserService {

    User getUserById(long l);

    User insertUser(User user);

    User deleteUserById(long l);

    User updateUserById(User user);

    Page<UserEntity> findUserPageByConditions(Integer page, Integer size, String name, Integer age, String createdAtFrom, String createdAtTo);

}
