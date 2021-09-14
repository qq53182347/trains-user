package com.tw.impl;

import com.tw.constant.UserConstant;
import com.tw.dao.UserRepository;
import com.tw.dao.entity.UserEntity;
import com.tw.exception.DataNotFoundException;
import com.tw.exception.DataValidateException;
import com.tw.service.domain.User;
import com.tw.service.impl.UserServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author liuguanghui
 * @version V1.0
 * @Description: 用户业务层测试
 * @Date: 2021-08-30
 */
public class UserServiceImplTest {

    public static final long ID_NOT_EXIST = -1L;
    public static final int PAGE = 1;
    public static final int SIZE = 10;
    public static final int AGE = 29;
    public static final Long ACTUAL_ID = 3L;

    private UserRepository userRepository = mock(UserRepository.class);

    private UserServiceImpl userService = new UserServiceImpl(userRepository);

    @Test
    public void shouldEqualsWhenQueryUserByUserId() {
        UserEntity userEntity = getUserEntity();
        when(userRepository.findById(ACTUAL_ID)).thenReturn(Optional.of(userEntity));
        assertEquals(userService.getUserById(ACTUAL_ID).getId(), userEntity.getId());
    }

    @Test
    public void shouldThrowsExceptionWhenQueryUserByUserIdNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(DataNotFoundException.class, () ->
                userService.getUserById(ID_NOT_EXIST));
        assertEquals(UserConstant.Message.DATA_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void shouldInsertUserDataAndReturnIdNotNull() {
        User user = getUser();
        UserEntity userEntity = getUserEntity();
        when(userRepository.save(any())).thenReturn(userEntity);
        User returnUser = userService.insertUser(user);
        assertEquals(returnUser.getId(), ACTUAL_ID);
    }

    @Test
    public void shouldThrowsExceptionWhenInsertUserIsNull() {
        Exception exception = assertThrows(DataValidateException.class, () ->
                userService.insertUser(null));
        assertEquals(UserConstant.Message.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void shouldThrowsExceptionWhenInsertUserNameIsNull() {
        Exception exception = assertThrows(DataValidateException.class, () ->
                userService.insertUser(new User()));
        assertEquals(UserConstant.Message.USERNAME_NOT_FOUND, exception.getMessage());
        User user = new User();
        user.setName("");
        Exception exception2 = assertThrows(DataValidateException.class, () ->
                userService.insertUser(user));
        assertEquals(UserConstant.Message.USERNAME_NOT_FOUND, exception2.getMessage());
    }

    @Test
    public void shouldThrowsExceptionWhenDeleteUserByIdNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(DataNotFoundException.class, () ->
                userService.deleteUserById(ID_NOT_EXIST));
        assertEquals(UserConstant.Message.DATA_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void shouldDeleteUserDataAndReturnNotNull() {
        UserEntity user = getUserEntity();
        doNothing().when(userRepository).deleteById(any());
        when(userRepository.findById(ACTUAL_ID)).thenReturn(Optional.of(user));
        User retUser = userService.deleteUserById(ACTUAL_ID);
        verify(userRepository, times(1)).deleteById(any());
        assertEquals(retUser.getId(), ACTUAL_ID);
    }

    @Test
    public void shouldUpdateUserDataAndReturnNotNull() {
        UserEntity userEntity = getUserEntity();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(userRepository.saveAndFlush(any())).thenReturn(userEntity);
        User user = getUser();
        User retUser = userService.updateUserById(user);
        assertEquals(retUser.getId(), user.getId());
    }


    @Test
    public void shouldThrowsExceptionWhenUpdateUserByIdNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(DataNotFoundException.class, () ->
                userService.updateUserById(new User()));
        assertEquals(UserConstant.Message.DATA_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void shouldThrowsExceptionWhenUpdateUserByIdUserIsNull() {
        Exception exception = assertThrows(DataValidateException.class, () ->
                userService.updateUserById(null));
        assertEquals(UserConstant.Message.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void shouldExistDataWhenQueryAgeByPage() {
        List<UserEntity> userEntities = Lists.list(getUserEntity(), getUserEntity(), getUserEntity(), getUserEntity());
        Page<UserEntity> pageUsers = getUserEntities(userEntities);
        extracted(pageUsers);
        Page<UserEntity> userCriteria = userService.findUserPageByConditions(PAGE, SIZE,
                "", AGE, "", "");
        assertEquals(userCriteria.getContent().size(), 4);
    }

    private Page<UserEntity> getUserEntities(List<UserEntity> userEntities) {
        return (Page<UserEntity>) new PageImpl(userEntities);
    }

    @Test
    public void shouldNotExistDataWhenQueryAgeAsMinusByPage() {
        List<UserEntity> userEntities = Lists.emptyList();
        Page<UserEntity> pagedTasks = getUserEntities(userEntities);
        extracted(pagedTasks);
        Page<UserEntity> userCriteria = userService.findUserPageByConditions(PAGE, SIZE,
                "", -1, "", "");
        assertEquals(userCriteria.getContent().size(), 0);
    }

    @Test
    public void shouldExistDataWhenQueryNameByPage() {
        List<UserEntity> userEntities = Lists.list(getUserEntity(), getUserEntity(), getUserEntity(), getUserEntity());
        Page<UserEntity> pageUsers = getUserEntities(userEntities);
        extracted(pageUsers);
        Page<UserEntity> userCriteria = userService.findUserPageByConditions(PAGE, SIZE,
                "zhangSan", null, "", "");
        assertEquals(userCriteria.getContent().size(), 4);
    }

    @Test
    public void shouldNotExistDataWhenQueryNameAsUUIDByPage() {
        List<UserEntity> userEntities = Lists.emptyList();
        Page<UserEntity> pageUsers = getUserEntities(userEntities);
        extracted(pageUsers);
        Page<UserEntity> userCriteria = userService.findUserPageByConditions(PAGE, SIZE,
                UUID.randomUUID().toString(), null, null, null);
        assertEquals(userCriteria.getContent().size(), 0);
    }

    @Test
    public void shouldExistDataWhenQueryCreateTimeRangeByPage() {
        List<UserEntity> userEntities = Lists.list(getUserEntity(), getUserEntity(), getUserEntity(), getUserEntity());
        Page<UserEntity> pageUsers = getUserEntities(userEntities);
        extracted(pageUsers);
        Page<UserEntity> userCriteria = userService.findUserPageByConditions(PAGE, SIZE,
                "", null, "2021-08-31T00:02:00", "2021-09-31T00:02:00");
        assertEquals(userCriteria.getContent().size(), 4);
    }

    @Test
    public void shouldNotExistDataWhenQueryCreateTimeRangeByPage() {
        List<UserEntity> userEntities = Lists.list(getUserEntity(), getUserEntity(), getUserEntity(), getUserEntity());
        Page<UserEntity> pageUsers = getUserEntities(userEntities);
        extracted(pageUsers);
        Page<UserEntity> userCriteria = userService.findUserPageByConditions(PAGE, SIZE,
                "", null, "2010-08-31T00:02:00", "2010-09-31T00:02:00");
        assertEquals(userCriteria.getContent().size(), 4);
    }

    private void extracted(Page<UserEntity> pageUsers) {
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageUsers);
    }

    private User getUser() {
        return User.builder().address("heiHei").age(29).id(ACTUAL_ID)
                .createdAt(LocalDateTime.now()).email("222@xx.com")
                .phoneNumber("12828282828").name("zhangSan").build();
    }

    private UserEntity getUserEntity() {
        return UserEntity.builder().address("heiHei").age(29).id(ACTUAL_ID)
                .createdAt(LocalDateTime.now()).email("222@xx.com")
                .phoneNumber("12828282828").name("zhangSan").build();
    }

}
