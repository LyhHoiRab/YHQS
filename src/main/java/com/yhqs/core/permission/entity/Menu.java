package com.yhqs.core.permission.entity;

import lombok.Getter;
import lombok.Setter;
import org.wah.doraemon.domain.Createable;
import org.wah.doraemon.domain.Entity;
import org.wah.doraemon.domain.Updateable;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Menu extends Entity implements Createable, Updateable{

    private String  name;
    private String  routerUrl;
    private String  url;
    private String  parentId;
    private Boolean isParent;
    private Integer sorted;
    private String  iconClass;
    private Date    createTime;
    private Date    updateTime;

    private List<Menu> children;
}
