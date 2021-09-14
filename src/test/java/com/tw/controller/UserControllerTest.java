package com.tw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.controller.dto.UserDTO;
import com.tw.dao.entity.UserEntity;
import com.tw.service.domain.User;
import com.tw.service.impl.UserServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author liuguanghui
 * @version V1.0
 * @Description: 用户controller层测试
 * @Date: 2021-08-31
 */
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    public static final long ID_NOT_EXIST = -1L;
    public static final int AGE = 29;
    public static final long ACTUAL_ID = 3L;
    public static final int ACTUAL = 4;

    @Autowired
    private MockMvc mockMvc;


    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach()
    public void setup() {
        //初始化
        MockitoAnnotations.initMocks(this);
        //构建mvc环境
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldNotBeNullWhenRequestByUserId() throws Exception {
        User user = getUser();
        when(userService.getUserById(ACTUAL_ID)).thenReturn(user);
        MvcResult mvcResult = mockMvc.perform(
                        get("/users/{id}", ACTUAL_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        UserDTO userDTO = getUserResponseModel(mvcResult);
        assertEquals(userDTO.getId(), ACTUAL_ID);
    }


    @Test
    public void shouldBeNullWhenRequestUserByUserIdNotExist() throws Exception {
        when(userService.getUserById(ID_NOT_EXIST)).thenReturn(new User());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
                "/users/{id}", ID_NOT_EXIST).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        UserDTO userDTO = getUserResponseModel(mvcResult);
        assertNull(userDTO.getId());
    }

    @Test
    public void shouldNotBeNullWhenRequestDeleteByUserId() throws Exception {
        User user = getUser();
        when(userService.deleteUserById(ACTUAL_ID)).thenReturn(user);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(
                "/users/{id}", ACTUAL_ID).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        UserDTO userDTO = getUserResponseModel(mvcResult);
        assertEquals(userDTO.getId(), ACTUAL_ID);
    }

    @Test
    public void shouldBeNullWhenRequestDeleteByUserIdNotExist() throws Exception {
        when(userService.deleteUserById(ID_NOT_EXIST)).thenReturn(new User());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(
                "/users/{id}", ID_NOT_EXIST).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        UserDTO userDTO = getUserResponseModel(mvcResult);
        assertNull(userDTO.getId());
    }

    @Test
    public void shouldBeNotNullWhenRequestQueryUserByPageUseAge() throws Exception {
        List<UserDTO> userEntities = Lists.list(getUserDTO(), getUserDTO(), getUserDTO(), getUserDTO());
        Page<UserEntity> pageUsers = new PageImpl(userEntities);
        when(userService.findUserPageByConditions(any(), any(), any(), any(), any(), any())).thenReturn(pageUsers);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
                "/users?age=29").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        LinkedHashMap page = getUserPageResponseModel(mvcResult);
        List content = (List)page.get("content");
        assertEquals(content.size(), ACTUAL);
    }

    @Test
    public void shouldBeNotNullWhenRequestQueryUserByPageUseName() throws Exception {
        List<UserDTO> userEntities = Lists.list(getUserDTO(), getUserDTO(), getUserDTO(), getUserDTO());
        Page<UserEntity> pageUsers = new PageImpl(userEntities);
        when(userService.findUserPageByConditions(any(), any(), any(), any(), any(), any())).thenReturn(pageUsers);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
                "/users?name=zhangsan").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        LinkedHashMap page = getUserPageResponseModel(mvcResult);
        List content = (List)page.get("content");
        assertEquals(content.size(), 4);
    }

    @Test
    public void shouldBeNotNullWhenRequestQueryUserByPageAndSize() throws Exception {
        List<UserDTO> userEntities = Lists.list(getUserDTO(), getUserDTO(), getUserDTO(), getUserDTO());
        Page<UserEntity> pageUsers = new PageImpl(userEntities);
        when(userService.findUserPageByConditions(any(), any(), any(), any(), any(), any())).thenReturn(pageUsers);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
                "/users?page=1&size=10").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        LinkedHashMap page = getUserPageResponseModel(mvcResult);
        List content = (List)page.get("content");
        assertEquals(content.size(), 4);
    }

    private LinkedHashMap getUserPageResponseModel(MvcResult mvcResult) throws Exception {
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content, LinkedHashMap.class);
    }

    @Test
    public void shouldBeNullWhenRequestQueryUserByPageAndSizeNotExist() throws Exception {
        List<UserDTO> userEntities = Lists.list();
        Page<UserEntity> pageUsers = new PageImpl(userEntities);
        when(userService.findUserPageByConditions(any(), any(), any(), any(), any(), any())).thenReturn(pageUsers);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
                "/users?page=-1&size=-1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        LinkedHashMap page = getUserPageResponseModel(mvcResult);
        List content = (List)page.get("content");
        assertEquals(content.size(), 0);
    }

    @Test
    public void shouldBeNotNullWhenRequestInsertUser() throws Exception {
        User user = getUser();
        when(userService.insertUser(user)).thenReturn(user);
        String json = new ObjectMapper().writeValueAsString(user);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(
                "/users").content(json).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        UserDTO userDTO = getUserResponseModel(mvcResult);
        assertEquals(userDTO.getId(), ACTUAL_ID);
        ;
    }

    @Test
    public void shouldBeNotNullWhenRequestUpdateUser() throws Exception {
        User user = getUser();
        when(userService.updateUserById(user)).thenReturn(user);
        String json = new ObjectMapper().writeValueAsString(user);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(
                "/users/3").content(json).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        UserDTO userDTO = getUserResponseModel(mvcResult);
        assertEquals(userDTO.getId(), ACTUAL_ID);
    }

    private UserDTO getUserResponseModel(MvcResult mvcResult) throws UnsupportedEncodingException, com.fasterxml.jackson.core.JsonProcessingException {
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content, UserDTO.class);
    }

    private User getUser() {
        return User.builder().address("heiHei").age(AGE).id(ACTUAL_ID)
                .email("222@xx.com")
                .phoneNumber("12828282828").name("zhangSan").build();
    }

    private UserDTO getUserDTO() {
        return UserDTO.builder().address("heiHei").age(AGE).id(ACTUAL_ID)
                .email("222@xx.com")
                .phoneNumber("12828282828").name("zhangSan").build();
    }
}
