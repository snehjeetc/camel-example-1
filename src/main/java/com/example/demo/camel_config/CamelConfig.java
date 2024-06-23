package com.example.demo.camel_config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;

@Configuration
public class CamelConfig extends RouteBuilder {

    
    @Autowired
    Environment env; 

  

    // @Value("${camel.component.servlet.mapping.contextPath}")
    // private String contextPath; 


    // public CamelConfig(@Autowired Environment env) {
    //     this.env = env;
    // }

    @Override
    public void configure() throws Exception {

        from("direct:start")
        .log("Here : ${header.name}")
        .to("bean:myBean?method=someMethod(${body})"); 
        restConfiguration()
                .component("servlet")
        // //         // // .contextPath(env.getProperty("") + "/rest/*")
                .contextPath(env.getProperty("camel.component.servlet.mapping.contextPath"))
                .apiProperty("api.title", "Spring Boot Camel Postgres Rest API.")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .port(env.getProperty("server.port", "8080"))
                .bindingMode(RestBindingMode.json);



        rest()
        .consumes(MediaType.APPLICATION_JSON_VALUE)
        // .produces(MediaType.APPLICATION_JSON_VALUE)
        // .get(env.getProperty("hello.url"))
        // http://localhost:8080/demo/camel/hello?name=john
        .get("hello")
            .produces(MediaType.TEXT_PLAIN_VALUE)
            .outType(String.class)
            .param()
            .name("name")
            .type(RestParamType.query)
            .endParam()      
            .to("direct:dummy")
            // http://localhost:8080/demo/camel/students
        .get("students")
            .produces(MediaType.APPLICATION_JSON_VALUE)
            .to("direct:fetchAllStudent");


        // .responseMessage(200 , "Accepted"); 
        from("direct:dummy").transform().constant("Accepted"); 
       
        // from("file:{{src.fileIN-path}}?noop=true&delete=true")
        // .filter(header(Exchange.FILE_NAME).startsWith("new"))
        // .convertBodyTo(String.class)
        // .log("${body}")
        // .to("file:{{src.fileOUT-path}}"); 
        // from("rest:get:student:/fetchAll")
        
    }

}
