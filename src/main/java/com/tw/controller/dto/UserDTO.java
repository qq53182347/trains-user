package com.tw.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author liuguanghui
 * @version V1.0
 * @Description: api层用户实体类
 * @Date: 2021-08-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private static final long ssserialVersionUID = -1L;

    private Long id;

    private String name;

    private String address;

    private String email;

    private Integer age;

    private String phoneNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
