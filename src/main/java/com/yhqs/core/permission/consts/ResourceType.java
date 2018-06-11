package com.yhqs.core.permission.consts;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wah.doraemon.domain.consts.EnumType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResourceType implements EnumType{

    @SerializedName("0")
    MENU(0, "菜单"),

    @SerializedName("1")
    FUNCTION(1, "功能"),

    @SerializedName("2")
    WECHAT(2, "微信"),

    @SerializedName("3")
    DEVICE(3, "设备"),

    @SerializedName("4")
    COMPANY(4, "公司"),

    @SerializedName("5")
    DEPARTMENT(5, "部门"),

    @SerializedName("6")
    POSITION(6, "岗位"),

    @SerializedName("999")
    UNKNOWN(999, "未知");

    private int id;
    private String description;

    public static ResourceType getById(int id){
        for(ResourceType type : ResourceType.values()){
            if(type.id == id){
                return type;
            }
        }

        return UNKNOWN;
    }
}
