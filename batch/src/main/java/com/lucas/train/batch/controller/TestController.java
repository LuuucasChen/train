package com.lucas.train.batch.controller;

import com.lucas.train.batch.feign.BusinessFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private BusinessFeign businessFeign;

    @GetMapping("/hello")
    public String hello() {
        String hello = businessFeign.hello();
        System.out.print(hello);
        return hello + " Hello World !! Batch";
    }
}
