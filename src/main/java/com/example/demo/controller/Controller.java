package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.camel_config.ProducerFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController("/testing")
public class Controller {
    
    //Service
    @Autowired
    ProducerFile producerFile; 

    @GetMapping("path")
    public ResponseEntity<String> getMethodName(@RequestParam("param") String param) {
        producerFile.doSomething(param);        
        return ResponseEntity.ok("Recived"); 
    }
    
}
