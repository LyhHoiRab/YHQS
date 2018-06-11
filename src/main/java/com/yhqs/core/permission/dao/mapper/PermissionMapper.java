package com.yhqs.core.permission.dao.mapper;

import com.yhqs.core.permission.consts.ResourceType;
import com.yhqs.core.permission.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.wah.doraemon.utils.mybatis.Criteria;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface PermissionMapper{

    void save(Permission permission);

    void saveList(@Param("permissions") List<Permission> permissions);

    void saveRelationsToRole(@Param("permissionIds") List<String> permissionIds, @Param("roleId") String roleId);

    void deleteRelationsToRole(@Param("roleId") String roleId, @Param("type") ResourceType type);

    void saveRelationsToAccount(@Param("permissionIds") List<String> permissionIds, @Param("accountId") String accountId);

    void saveResourceToRoles(@Param("roleIds") List<String> roleIds, @Param("permissionId") String permissionId);

    void deleteRelationsToAccount(@Param("accountId") String accountId, @Param("type") ResourceType type);

    void deleteResourcesToAccounts(@Param("permissionIds") List<String> permissionIds);

    void deleteResourceToRoles(@Param("roleIds") List<String> roleIds, @Param("permissionId") String permissionId);

    Permission get(@Param("params") Criteria criteria);

    List<Permission> find(@Param("params") Criteria criteria);

    Set<Permission> findByRoleIds(@Param("params") Criteria criteria);

    Set<Permission> findByAccountId(@Param("params") Criteria criteria);

    Set<String> findAccountIdsByResourceId(String resourceId);

    Set<String> findAccountIdsByRoleIds(@Param("roleIds") List<String> roleIds);

    Set<String> findRoleIdsByResourceId(String resourceId);
}
