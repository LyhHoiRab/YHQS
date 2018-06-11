package com.yhqs.core.permission.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wah.doraemon.domain.Createable;
import org.wah.doraemon.domain.Entity;
import org.wah.doraemon.domain.Updateable;

import java.util.Date;

@Getter
@Setter
public class Function extends Entity implements Createable, Updateable{

    private String        url;
    private String        description;
    private Boolean       needAllot;
    private RequestMethod method;
    private Date          createTime;
    private Date          updateTime;

    @Override
    public boolean equals(Object object){
        if(this == object){
            return true;
        }

        if(object != null && this.getClass() == object.getClass()){
            Function entity = (Function) object;

            if(StringUtils.isNotBlank(this.url) && this.method != null &&
                StringUtils.isNotBlank(entity.url) && entity.method != null){

                return (this.url.equals(entity.url) && this.method.equals(entity.method));
            }
        }

        return false;
    }
}
