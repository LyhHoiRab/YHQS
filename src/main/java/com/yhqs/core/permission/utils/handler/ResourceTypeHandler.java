package com.yhqs.core.permission.utils.handler;

import com.yhqs.core.permission.consts.ResourceType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourceTypeHandler extends BaseTypeHandler<ResourceType> implements TypeHandler<ResourceType>{

    public void setNonNullParameter(PreparedStatement ps, int i, ResourceType parameter, JdbcType jdbcType) throws SQLException{
        ps.setInt(i, parameter.getId());
    }

    public ResourceType getNullableResult(ResultSet rs, String columnName) throws SQLException{
        return ResourceType.getById(rs.getInt(columnName));
    }

    public ResourceType getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
        return ResourceType.getById(rs.getInt(columnIndex));
    }

    public ResourceType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
        return ResourceType.getById(cs.getInt(columnIndex));
    }
}
