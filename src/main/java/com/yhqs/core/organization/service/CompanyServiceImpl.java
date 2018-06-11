package com.yhqs.core.organization.service;

import com.yhqs.core.organization.dao.CompanyDao;
import com.yhqs.core.organization.entity.Company;
import com.yhqs.core.permission.consts.ResourceType;
import com.yhqs.core.permission.dao.PermissionDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService{

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    @Transactional(readOnly = false)
    public void save(Company company){
        Assert.notNull(company, "公司信息不能为空");

        companyDao.saveOrUpdate(company);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(Company company){
        Assert.notNull(company, "公司信息不能为空");
        Assert.hasText(company.getId(), "公司ID不能为空");

        companyDao.saveOrUpdate(company);
    }

    @Override
    public Company getById(String id){
        Assert.hasText(id, "公司ID不能为空");

        return companyDao.getById(id);
    }

    @Override
    public List<Company> find(String id, String name, String address, String phone, String accountId){

        //公司ID列表
        List<String> ids = new ArrayList<String>();

        //查询资源
        if(StringUtils.isNotBlank(accountId)){
            ids.addAll(permissionDao.findResourceIdsByAccountId(accountId, ResourceType.COMPANY));
        }

        return companyDao.find(id, name, address, phone, new ArrayList(ids));
    }

    @Override
    public Page<Company> page(PageRequest pageRequest, String id, String name, String address, String phone, String accountId){
        Assert.notNull(pageRequest, "分页信息不能为空");

        //公司ID列表
        List<String> ids = new ArrayList<String>();

        //查询资源
        if(StringUtils.isNotBlank(accountId)){
            ids.addAll(permissionDao.findResourceIdsByAccountId(accountId, ResourceType.COMPANY));
        }

        return companyDao.page(pageRequest, id, name, address, phone, ids);
    }
}
