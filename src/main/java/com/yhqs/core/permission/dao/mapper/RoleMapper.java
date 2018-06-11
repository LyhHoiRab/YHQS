package com.yhqs.core.permission.dao.mapper;

import com.yhqs.core.permission.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.wah.doraemon.utils.mybatis.Criteria;

import java.util.List;

@Mapper
@Repository
public interface RoleMapper{

    void save(Role role);

    void update(Role role);

    void saveRelationsToAccount(@Param("roleIds") List<String> roleIds, @Param("accountId") String accountId);

    void deleteRelationsToAccount(String accountId);

    Role get(@Param("params") Criteria criteria);

    List<Role> find(@Param("params") Criteria criteria);

    Long count(@Param("params") Criteria criteria);

    List<Role> findByAccountId(String accountId);
}
