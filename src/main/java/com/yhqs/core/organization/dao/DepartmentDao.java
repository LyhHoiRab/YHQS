package com.yhqs.core.organization.dao;

import com.yhqs.core.organization.dao.mapper.DepartmentMapper;
import com.yhqs.core.organization.entity.Department;
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
public class DepartmentDao{

    private Logger logger = LoggerFactory.getLogger(DepartmentDao.class);

    @Autowired
    private DepartmentMapper mapper;

    public void saveOrUpdate(Department department){
        try{
            Assert.notNull(department, "部门信息不能为空");

            if(StringUtils.isBlank(department.getId())){
                Assert.hasText(department.getCompanyId(), "公司ID不能为空");

                department.setId(IDGenerator.uuid32());
                department.setCreateTime(new Date());
                mapper.save(department);
            }else{
                department.setUpdateTime(new Date());
                mapper.update(department);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Department getById(String id){
        try{
            Assert.hasText(id, "公司ID不能为空");

            Criteria criteria = new Criteria();
            criteria.and(Restrictions.eq("id", id));

            return mapper.get(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public List<Department> find(String id, String name, String companyId){
        try{
            Criteria criteria = new Criteria();
            criteria.sort(Restrictions.desc("createTime"));

            if(StringUtils.isNotBlank(id)){
                criteria.and(Restrictions.eq("id", id));
            }
            if(StringUtils.isNotBlank(name)){
                criteria.and(Restrictions.like("name", name));
            }
            if(StringUtils.isNotBlank(companyId)){
                criteria.and(Restrictions.like("companyId", companyId));
            }

            return mapper.find(criteria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Page<Department> page(PageRequest pageRequest, String id, String name, String companyId){
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
            if(StringUtils.isNotBlank(companyId)){
                criteria.and(Restrictions.like("companyId", companyId));
            }

            List<Department> list  = mapper.find(criteria);
            Long             total = mapper.count(criteria);

            return new Page<Department>(list, total, pageRequest);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
