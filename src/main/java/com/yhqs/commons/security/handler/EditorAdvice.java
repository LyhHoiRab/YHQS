package com.yhqs.commons.security.handler;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.wah.doraemon.entity.consts.Sex;
import com.yhqs.commons.utils.editor.SexEditor;

@ControllerAdvice
public class EditorAdvice{

    @InitBinder
    public void initSexBinder(WebDataBinder binder){
        binder.registerCustomEditor(Sex.class, new SexEditor());
    }
}
