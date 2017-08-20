package com.home.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * Created by Administrator on 2017/8/21.
 */
@RestController
public class IndexController {
    @GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
    public Resource index(){
        return new ClassPathResource("static/index.html");
    }
}
