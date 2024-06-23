package com.example.demo.camel_config;

import org.springframework.stereotype.Component;

@Component
public class MyBean {
    
    public void someMethod(String body){ 
        System.out.println(body);
    } 

    public String extMethod(String body){ 
        return body + "added"; 
    }
}
