package com.yhqs.core.organization.dao.mapper;

import com.yhqs.core.organization.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.wah.doraemon.utils.mybatis.Criteria;

import java.util.List;

@Mapper
@Repository
public interface CompanyMapper{

    void save(Company company);

    void update(Company company);

    Company get(@Param("params") Criteria criteria);

    List<Company> find(@Param("params") Criteria criteria);

    Long count(@Param("params") Criteria criteria);
}
