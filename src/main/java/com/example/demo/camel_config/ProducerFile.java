package com.example.demo.camel_config;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProducerFile {

    private ProducerTemplate template; 
    
    public ProducerFile(@Autowired CamelContext ctx){ 
        this.template = ctx.createProducerTemplate(); 
    }


    public void doSomething(String req){ 
    }
}
