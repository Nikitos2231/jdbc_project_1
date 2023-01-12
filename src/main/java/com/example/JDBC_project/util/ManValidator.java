package com.example.JDBC_project.util;

import com.example.JDBC_project.model.Man;
import com.example.JDBC_project.service.ManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ManValidator implements Validator {

    private final ManService manService;

    @Autowired
    public ManValidator(ManService manService) {
        this.manService = manService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Man.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Man man = (Man) target;
        if (manService.isExistManByName(man.getName())) {
            errors.rejectValue("name","", "Человек с таким именем уже существует!");
        }
    }
}
