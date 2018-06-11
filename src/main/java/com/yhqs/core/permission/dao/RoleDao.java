package com.yhqs.core.permission.dao;

import com.yhqs.core.permission.dao.mapper.RoleMapper;
import com.yhqs.core.permission.entity.Role;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.wah.doraemon.entity.consts.UsingState;
import org.wah.doraemon.security.exception.DataAccessException;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;
import org.wah.doraemon.utils.IDGenerator;
import org.wah.doraemon.utils.mybatis.Criteria;
import org.wah.doraemon.utils.mybatis.Restrictions;

import java.util.Date;
import java.util.List;

@Repository
public class RoleDao{

    private Logger logger = LoggerFactory.getLogger(RoleDao.class);

    @Autowired
    private RoleMapper mapper;

    public void saveOrUpdate(Role role){
        try{
            Assert.notNull(role, "权限信息不能为空");

            if(StringUtils.isBlank(role.getId())){
                Assert.notNull(role.getIsAdmin(), "超级管理员标示不能为空");

                role.setId(IDGenerator.uuid32());
                role.setState(UsingState.USABLE);
                role.setCreateTime(new Date());
                mapper.save(role);
            }else{
                role.setUpdateTime(new Date());
                mapper.update(role);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void updateRelationsToAccount(List<String> roleIds, String accountId){
        try{
            Assert.hasText(accountId, "账户ID不能为空");

            //删除原有角色
            mapper.deleteRelationsToAccount(accountId);
            //添加角色
            if(roleIds != null && !roleIds.isEmpty()){
                mapper.saveRelationsToAccount(roleIds, accountId);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Role getById(String id){
        try{
            Assert.hasText(id, "角色ID不能为空");

            Criteria criteria = new Criteria();
            criteria.and(Restrictions.eq("id", id));

            return mapper.get(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public List<Role> findAdmins(){
        try{
            Criteria criteria = new Criteria();
            criteria.and(Restrictions.eq("isAdmin", true));

            return mapper.find(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Page<Role> page(PageRequest pageRequest, String id, String name, UsingState state, Boolean isAdmin, List<String> ids){
        try{
            Assert.notNull(pageRequest, "分页信息不能为空");

            Criteria criteria = new Criteria();
            criteria.and(Restrictions.limit(pageRequest.getOffset(), pageRequest.getPageSize()));
            criteria.sort(Restrictions.desc("createTime"));

            if(StringUtils.isNotBlank(id)){
                criteria.and(Restrictions.eq("id", id));
            }
            if(StringUtils.isNotBlank(name)){
                criteria.and(Restrictions.like("name", name));
            }
            if(state != null){
                criteria.and(Restrictions.eq("state", state.getId()));
            }
            if(isAdmin != null){
                criteria.and(Restrictions.eq("isAdmin", isAdmin));
            }
            if(ids != null && !ids.isEmpty()){
                criteria.and(Restrictions.in("id", ids));
            }

            List<Role> list  = mapper.find(criteria);
            Long       total = mapper.count(criteria);

            return new Page<Role>(list, total, pageRequest);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public List<Role> findByAccountId(String accountId){
        try{
            Assert.hasText(accountId, "账户ID不能为空");

            return mapper.findByAccountId(accountId);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
