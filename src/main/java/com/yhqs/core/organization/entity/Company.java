package com.yhqs.core.organization.entity;

import lombok.Getter;
import lombok.Setter;
import org.wah.doraemon.domain.Createable;
import org.wah.doraemon.domain.Entity;
import org.wah.doraemon.domain.Updateable;

import java.util.Date;

@Getter
@Setter
public class Company extends Entity implements Createable, Updateable{

    private String name;
    private String address;
    private String phone;
    private Date   createTime;
    private Date   updateTime;
}
