package com.yhqs.core.organization.dao.mapper;

import com.yhqs.core.organization.entity.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.wah.doraemon.utils.mybatis.Criteria;

import java.util.List;

@Mapper
@Repository
public interface PositionMapper{

    void save(Position position);

    void update(Position position);

    Position get(@Param("params") Criteria criteria);

    List<Position> find(@Param("params") Criteria criteria);

    Long count(@Param("params") Criteria criteria);
}
