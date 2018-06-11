package com.yhqs.commons.utils.editor;

import org.apache.commons.lang3.StringUtils;
import org.wah.doraemon.entity.consts.Sex;

import java.beans.PropertyEditorSupport;

public class SexEditor extends PropertyEditorSupport{

    @Override
    public void setAsText(String text) throws IllegalArgumentException{
        if(!StringUtils.isBlank(text)){
            int id = Integer.parseInt(text);
            setValue(Sex.getById(id));
        }
    }
}
