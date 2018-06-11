package com.yhqs.core.permission.entity;

import com.yhqs.core.permission.consts.ResourceType;
import lombok.Getter;
import lombok.Setter;
import org.wah.doraemon.domain.Createable;
import org.wah.doraemon.domain.Entity;
import org.wah.doraemon.domain.Updateable;

import java.util.Date;

@Getter
@Setter
public class Permission extends Entity implements Createable, Updateable{

    private String       resourceId;
    private ResourceType type;
    private Date         updateTime;
    private Date         createTime;
}
