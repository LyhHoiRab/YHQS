package com.yhqs.core.organization.service;

import com.yhqs.core.organization.entity.Company;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;

import java.util.List;

public interface CompanyService{

    void save(Company company);

    void update(Company company);

    Company getById(String id);

    List<Company> find(String id, String name, String address, String phone, String accountId);

    Page<Company> page(PageRequest pageRequest, String id, String name, String address, String phone, String accountId);
}
