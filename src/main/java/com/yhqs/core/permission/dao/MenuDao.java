package com.yhqs.core.permission.dao;

import com.yhqs.core.permission.dao.mapper.MenuMapper;
import com.yhqs.core.permission.entity.Menu;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.wah.doraemon.security.exception.DataAccessException;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;
import org.wah.doraemon.utils.IDGenerator;
import org.wah.doraemon.utils.mybatis.Criteria;
import org.wah.doraemon.utils.mybatis.Restrictions;

import java.util.Date;
import java.util.List;

@Repository
public class MenuDao{

    private Logger logger = LoggerFactory.getLogger(MenuDao.class);

    @Autowired
    private MenuMapper mapper;

    public void saveOrUpdate(Menu menu){
        try{
            Assert.notNull(menu, "菜单信息不能为空");

            if(StringUtils.isBlank(menu.getId())){
                menu.setId(IDGenerator.uuid32());
                menu.setIsParent(StringUtils.isBlank(menu.getParentId()));
                menu.setCreateTime(new Date());
                mapper.save(menu);
            }else{
                menu.setUpdateTime(new Date());
                mapper.update(menu);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void saveList(List<Menu> menus){
        try{
            Assert.notEmpty(menus, "菜单信息列表不能为空");

            final Date now = new Date();
            for(Menu menu : menus){
                Assert.hasText(menu.getId(), "菜单ID不能为空");

                menu.setIsParent(StringUtils.isBlank(menu.getParentId()));
                menu.setCreateTime(now);
            }

            mapper.saveList(menus);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Menu getById(String id){
        try{
            Assert.hasText(id, "菜单ID不能为空");

            Criteria criteria = new Criteria();
            criteria.and(Restrictions.eq("id", id));

            return mapper.get(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Page<Menu> page(PageRequest pageRequest, String id, String name, String url, String parentId, Boolean isParent){
        try{
            Assert.notNull(pageRequest, "分页信息不能为空");

            Criteria criteria = new Criteria();
            criteria.limit(Restrictions.limit(pageRequest.getOffset(), pageRequest.getPageSize()));
            criteria.sort(Restrictions.desc("parentId"));

            if(StringUtils.isNotBlank(id)){
                criteria.and(Restrictions.eq("id", id));
            }
            if(StringUtils.isNotBlank(name)){
                criteria.and(Restrictions.like("name", name));
            }
            if(StringUtils.isNotBlank(url)){
                criteria.and(Restrictions.like("url", url));
            }
            if(StringUtils.isNotBlank(parentId)){
                criteria.and(Restrictions.eq("parentId", parentId));
            }
            if(isParent != null){
                criteria.and(Restrictions.eq("isParent", isParent));
            }

            List<Menu> list  = mapper.find(criteria);
            Long       total = mapper.count(criteria);

            return new Page<Menu>(list, total, pageRequest);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public List<Menu> find(String id, String name, String url, String parentId, Boolean isParent, List<String> ids){
        try{
            Criteria criteria = new Criteria();
            criteria.sort(Restrictions.desc("parentId"));

            if(StringUtils.isNotBlank(id)){
                criteria.and(Restrictions.eq("id", id));
            }
            if(StringUtils.isNotBlank(name)){
                criteria.and(Restrictions.like("name", name));
            }
            if(StringUtils.isNotBlank(url)){
                criteria.and(Restrictions.like("url", url));
            }
            if(StringUtils.isNotBlank(parentId)){
                criteria.and(Restrictions.eq("parentId", parentId));
            }
            if(isParent != null){
                criteria.and(Restrictions.eq("isParent", isParent));
            }
            if(ids != null && !ids.isEmpty()){
                criteria.and(Restrictions.in("id", ids));
            }

            return mapper.find(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
