package com.yhqs.core.permission.entity;

import lombok.Getter;
import lombok.Setter;
import org.wah.doraemon.domain.Createable;
import org.wah.doraemon.domain.Entity;
import org.wah.doraemon.domain.Updateable;
import org.wah.doraemon.entity.consts.UsingState;

import java.util.Date;

@Getter
@Setter
public class Role extends Entity implements Createable, Updateable{

    private String     name;
    private UsingState state;
    private Boolean    isAdmin;
    private Date       createTime;
    private Date       updateTime;
}
