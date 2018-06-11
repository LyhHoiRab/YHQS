package com.yhqs.core.organization.entity;

import lombok.Getter;
import lombok.Setter;
import org.wah.doraemon.domain.Createable;
import org.wah.doraemon.domain.Entity;
import org.wah.doraemon.domain.Updateable;

import java.util.Date;

@Getter
@Setter
public class Department extends Entity implements Createable, Updateable{

    private String companyId;
    private String name;
    private Date   createTime;
    private Date   updateTime;
}
