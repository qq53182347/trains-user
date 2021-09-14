package com.tw.dao;

import com.tw.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author liuguanghui
 * @version V1.0
 * @Description: 用户查询数据库层
 * @Date: 2021-08-31
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

}

