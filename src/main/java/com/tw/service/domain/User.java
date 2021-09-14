package com.tw.service.domain;


import lombok.*;

import java.time.LocalDateTime;

/**
 * @author liuguanghui
 * @version V1.0
 * @Description: 用户业务处理实体类
 * @Date: 2021-08-31
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    private Long id;


    private String name;


    private String address;


    private String email;


    private Integer age;


    private String phoneNumber;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

}
