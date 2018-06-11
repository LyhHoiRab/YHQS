package com.yhqs.core.organization.service;

import com.yhqs.core.organization.dao.DepartmentDao;
import com.yhqs.core.organization.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    @Transactional(readOnly = false)
    public void save(Department department){
        Assert.notNull(department, "部门信息不能为空");
        Assert.hasText(department.getCompanyId(), "公司ID不能为空");

        departmentDao.saveOrUpdate(department);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(Department department){
        Assert.notNull(department, "部门信息不能为空");
        Assert.hasText(department.getId(), "部门ID不能为空");

        departmentDao.saveOrUpdate(department);
    }

    @Override
    public Department getById(String id){
        Assert.hasText(id, "部门ID不能为空");

        return departmentDao.getById(id);
    }

    @Override
    public List<Department> find(String id, String name, String companyId){
        return departmentDao.find(id, name, companyId);
    }

    @Override
    public Page<Department> page(PageRequest pageRequest, String id, String name, String companyId){
        Assert.notNull(pageRequest, "分页信息不能为空");

        return departmentDao.page(pageRequest, id, name, companyId);
    }
}
