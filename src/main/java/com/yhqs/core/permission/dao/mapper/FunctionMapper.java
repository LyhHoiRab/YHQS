package com.yhqs.core.permission.dao.mapper;

import com.yhqs.core.permission.entity.Function;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.wah.doraemon.utils.mybatis.Criteria;

import java.util.List;

@Mapper
@Repository
public interface FunctionMapper{

    void save(Function function);

    void saveList(@Param("functions") List<Function> functions);

    void update(Function function);

    void updateList(@Param("functions") List<Function> functions);

    Function get(@Param("params") Criteria criteria);

    List<Function> find(@Param("params") Criteria criteria);

    Long count(@Param("params") Criteria criteria);
}
