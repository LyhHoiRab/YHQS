package com.yhqs.core.organization.service;

import com.yhqs.core.organization.entity.Position;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;

import java.util.List;

public interface PositionService{

    void save(Position department);

    void update(Position department);

    Position getById(String id);

    List<Position> find(String id, String name, String departmentId);

    Page<Position> page(PageRequest pageRequest, String id, String name, String departmentId);
}
