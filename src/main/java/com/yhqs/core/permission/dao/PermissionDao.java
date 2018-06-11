package com.yhqs.core.permission.dao;

import com.yhqs.core.permission.consts.ResourceType;
import com.yhqs.core.permission.dao.mapper.PermissionMapper;
import com.yhqs.core.permission.entity.Permission;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.wah.doraemon.security.exception.DataAccessException;
import org.wah.doraemon.utils.IDGenerator;
import org.wah.doraemon.utils.mybatis.Criteria;
import org.wah.doraemon.utils.mybatis.Restrictions;

import java.util.*;

@Repository
public class PermissionDao{

    private Logger logger = LoggerFactory.getLogger(PermissionDao.class);

    @Autowired
    private PermissionMapper mapper;

    public void save(Permission permission){
        try{
            Assert.notNull(permission, "权限信息不能为空");
            Assert.hasText(permission.getResourceId(), "资源ID不能为空");
            Assert.notNull(permission.getType(), "权限类型不能为空");

            permission.setId(IDGenerator.uuid32());
            permission.setCreateTime(new Date());
            mapper.save(permission);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void saveList(List<Permission> permissions){
        try{
            Assert.notEmpty(permissions, "权限列表不能为空");

            final Date now = new Date();
            for(Permission permission : permissions){
                Assert.notNull(permission, "权限信息不能为空");
                Assert.hasText(permission.getResourceId(), "资源ID不能为空");
                Assert.notNull(permission.getType(), "权限类型不能为空");

                permission.setId(IDGenerator.uuid32());
                permission.setCreateTime(new Date());
            }

            mapper.saveList(permissions);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void updateFunctionsToRole(List<String> permissionIds, String roleId){
        try{
            Assert.hasText(roleId, "角色ID不能为空");

            //删除原有权限
            mapper.deleteRelationsToRole(roleId, ResourceType.FUNCTION);
            //添加权限
            if(permissionIds != null && !permissionIds.isEmpty()){
                mapper.saveRelationsToRole(permissionIds, roleId);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void updateFunctionsToAccount(List<String> permissionIds, String accountId){
        try{
            Assert.hasText(accountId, "账户ID不能为空");

            //删除原有权限
            mapper.deleteRelationsToAccount(accountId, ResourceType.FUNCTION);
            //添加权限
            if(permissionIds != null && !permissionIds.isEmpty()){
                mapper.saveRelationsToAccount(permissionIds, accountId);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void updateMenusToRole(List<String> permissionIds, String roleId){
        try{
            Assert.hasText(roleId, "角色ID不能为空");

            //删除原有权限
            mapper.deleteRelationsToRole(roleId, ResourceType.MENU);
            //添加权限
            if(permissionIds != null && !permissionIds.isEmpty()){
                mapper.saveRelationsToRole(permissionIds, roleId);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void updateCompanysToAccount(List<String> permissionIds, String accountId){
        try{
            Assert.hasText(accountId, "账户ID不能为空");

            //删除原有权限
            mapper.deleteRelationsToAccount(accountId, ResourceType.COMPANY);
            //添加权限
            if(permissionIds != null && !permissionIds.isEmpty()){
                mapper.saveRelationsToAccount(permissionIds, accountId);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void updateDepartmentsToAccount(List<String> permissionIds, String accountId){
        try{
            Assert.hasText(accountId, "账户ID不能为空");

            //删除原有权限
            mapper.deleteRelationsToAccount(accountId, ResourceType.DEPARTMENT);
            //添加权限
            if(permissionIds != null && !permissionIds.isEmpty()){
                mapper.saveRelationsToAccount(permissionIds, accountId);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void updatePositionsToAccount(List<String> permissionIds, String accountId){
        try{
            Assert.hasText(accountId, "账户ID不能为空");

            //删除原有权限
            mapper.deleteRelationsToAccount(accountId, ResourceType.POSITION);
            //添加权限
            if(permissionIds != null && !permissionIds.isEmpty()){
                mapper.saveRelationsToAccount(permissionIds, accountId);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void updateWechatsToAccount(List<String> permissionIds, String accountId){
        try{
            Assert.hasText(accountId, "账户ID不能为空");

            //删除原有权限
            mapper.deleteRelationsToAccount(accountId, ResourceType.WECHAT);
            //添加权限
            if(permissionIds != null && !permissionIds.isEmpty()){
                mapper.saveRelationsToAccount(permissionIds, accountId);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void updateDevicesToAccount(List<String> permissionIds, String accountId){
        try{
            Assert.hasText(accountId, "账户ID不能为空");

            //删除原有权限
            mapper.deleteRelationsToAccount(accountId, ResourceType.DEVICE);
            //添加权限
            if(permissionIds != null && !permissionIds.isEmpty()){
                mapper.saveRelationsToAccount(permissionIds, accountId);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void updateResourcesToAccount(List<String> permissionIds, String accountId){
        try{
            Assert.notEmpty(permissionIds, "权限ID列表不能为空");

            //删除原有权限
            mapper.deleteResourcesToAccounts(permissionIds);
            //添加新权限
            if(StringUtils.isNotBlank(accountId)){
                mapper.saveRelationsToAccount(permissionIds, accountId);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void saveResourceToRoles(String permissionId, List<String> roleIds){
        try{
            Assert.notEmpty(roleIds, "角色ID列表不能为空");
            Assert.hasText(permissionId, "资源权限ID不能为空");

            mapper.saveResourceToRoles(roleIds, permissionId);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void deleteResourceToRoles(String permissionId, List<String> roleIds){
        try{
            Assert.notEmpty(roleIds, "角色ID列表不能为空");
            Assert.hasText(permissionId, "资源权限ID不能为空");

            mapper.deleteResourceToRoles(roleIds, permissionId);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Permission getByResourceId(String resourceId){
        try{
            Assert.hasText(resourceId, "资源ID不能为空");

            Criteria criteria = new Criteria();
            criteria.and(Restrictions.eq("resourceId", resourceId));

            return mapper.get(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Set<Permission> findByRoleIds(List<String> roleIds, ResourceType type){
        try{
            Assert.notEmpty(roleIds, "角色ID列表不能为空");

            Criteria criteria = new Criteria();
            criteria.sort(Restrictions.desc("createTime"));
            criteria.and(Restrictions.in("rp.roleId", roleIds));

            if(type != null){
                criteria.and(Restrictions.eq("p.type", type.getId()));
            }

            return mapper.findByRoleIds(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Set<Permission> findByAccountId(String accountId, ResourceType type){
        try{
            Assert.hasText(accountId, "账户ID不能为空");

            Criteria criteria = new Criteria();
            criteria.sort(Restrictions.desc("createTime"));
            criteria.and(Restrictions.eq("ap.accountId", accountId));

            if(type != null){
                criteria.and(Restrictions.eq("p.type", type.getId()));
            }

            return mapper.findByAccountId(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Set<String> findResourceIdsByRoleIds(List<String> roleIds, ResourceType type){
        try{
            Assert.notEmpty(roleIds, "角色ID列表不能为空");

            Criteria criteria = new Criteria();
            criteria.sort(Restrictions.desc("createTime"));
            criteria.and(Restrictions.in("rp.roleId", roleIds));

            if(type != null){
                criteria.and(Restrictions.eq("p.type", type.getId()));
            }

            Set<Permission> permissions = mapper.findByRoleIds(criteria);
            Set<String> ids = new HashSet<String>();

            if(permissions != null && !permissions.isEmpty()){
                for(Permission permission : permissions){
                    if(StringUtils.isNotBlank(permission.getResourceId())){
                        ids.add(permission.getResourceId());
                    }
                }
            }

            return ids;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Set<String> findResourceIdsByAccountId(String accountId, ResourceType type){
        try{
            Assert.hasText(accountId, "账户ID不能为空");

            Criteria criteria = new Criteria();
            criteria.sort(Restrictions.desc("createTime"));
            criteria.and(Restrictions.eq("ap.accountId", accountId));

            if(type != null){
                criteria.and(Restrictions.eq("p.type", type.getId()));
            }

            Set<Permission> permissions = mapper.findByAccountId(criteria);
            Set<String> ids = new HashSet<String>();

            if(permissions != null && !permissions.isEmpty()){
                for(Permission permission : permissions){
                    if(StringUtils.isNotBlank(permission.getResourceId())){
                        ids.add(permission.getResourceId());
                    }
                }
            }

            return ids;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Set<String> findAccountIdsByResourceId(String resourceId){
        try{
            Assert.hasText(resourceId, "资源ID不能为空");

            Set<String> accountIds = new HashSet<String>();

            //用户权限
            accountIds.addAll(mapper.findAccountIdsByResourceId(resourceId));
            //角色权限
            Set<String> roleIds = mapper.findRoleIdsByResourceId(resourceId);
            //用户角色
            if(roleIds != null && !roleIds.isEmpty()){
                accountIds.addAll(mapper.findAccountIdsByRoleIds(new ArrayList<String>(roleIds)));
            }

            return accountIds;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
