package com.yhqs.core.organization.dao;

import com.yhqs.core.organization.dao.mapper.PositionMapper;
import com.yhqs.core.organization.entity.Position;
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
public class PositionDao{

    private Logger logger = LoggerFactory.getLogger(PositionDao.class);

    @Autowired
    private PositionMapper mapper;

    public void saveOrUpdate(Position position){
        try{
            Assert.notNull(position, "岗位信息不能为空");

            if(StringUtils.isBlank(position.getId())){
                Assert.hasText(position.getDepartmentId(), "部门ID不能为空");

                position.setId(IDGenerator.uuid32());
                position.setCreateTime(new Date());
                mapper.save(position);
            }else{
                position.setUpdateTime(new Date());
                mapper.update(position);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Position getById(String id){
        try{
            Assert.hasText(id, "岗位ID不能为空");

            Criteria criteria = new Criteria();
            criteria.and(Restrictions.eq("id", id));

            return mapper.get(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public List<Position> find(String id, String name, String departmentId){
        try{
            Criteria criteria = new Criteria();
            criteria.sort(Restrictions.desc("createTime"));

            if(StringUtils.isNotBlank(id)){
                criteria.and(Restrictions.eq("id", id));
            }
            if(StringUtils.isNotBlank(name)){
                criteria.and(Restrictions.like("name", name));
            }
            if(StringUtils.isNotBlank(departmentId)){
                criteria.and(Restrictions.eq("departmentId", departmentId));
            }

            return mapper.find(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Page<Position> page(PageRequest pageRequest, String id, String name, String departmentId){
        try{
            Assert.notNull(pageRequest, "分页信息不能为空");

            Criteria criteria = new Criteria();
            criteria.limit(Restrictions.limit(pageRequest.getOffset(), pageRequest.getPageSize()));
            criteria.sort(Restrictions.desc("createTime"));

            if(StringUtils.isNotBlank(id)){
                criteria.and(Restrictions.eq("id", id));
            }
            if(StringUtils.isNotBlank(name)){
                criteria.and(Restrictions.like("name", name));
            }
            if(StringUtils.isNotBlank(departmentId)){
                criteria.and(Restrictions.eq("departmentId", departmentId));
            }

            List<Position> list  = mapper.find(criteria);
            Long           total = mapper.count(criteria);

            return new Page<Position>(list, total, pageRequest);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
