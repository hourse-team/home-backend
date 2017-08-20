package com.home.configuration;

import com.home.handler.HourseHandler;
import com.home.handler.Userhandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Created by Administrator on 2017/8/20.
 */
@Configuration
public class Routes {
    @Autowired
    Userhandler userhandler;

    @Autowired
    HourseHandler hourseHandler;

    private RequestPredicate type = accept(MediaType.APPLICATION_JSON_UTF8);

    @Bean
    public RouterFunction<?> userRoutes(){
        return route(POST("/api/login").and(accept(MediaType.APPLICATION_JSON)),userhandler::getUser)
                .and(route(GET("/api/hourses/{userId}").and(accept(MediaType.APPLICATION_JSON_UTF8)),hourseHandler::getHourses)
                        .and(route(GET("/api/hourse/{hourseId}").and(accept(MediaType.APPLICATION_JSON_UTF8)),hourseHandler::getHourse))
                        .and(route(PUT("/api/update").and(accept(MediaType.APPLICATION_JSON_UTF8)),hourseHandler::update))
                        .and(route(DELETE("/api/delete/{hourseId}").and(type),hourseHandler::delete)))
                .and(route(POST("/api/hourse/create").and(type),hourseHandler::create));
    }

//    @Bean
//    public RouterFunction<?> hourseRoutes(){
//        return route(GET("/api/hourses/{userId}").and(accept(MediaType.APPLICATION_JSON_UTF8)),hourseHandler::getHourses)
//                .and(route(GET("/api/hourse/{hourseId}").and(accept(MediaType.APPLICATION_JSON_UTF8)),hourseHandler::getHourse))
//                .and(route(PUT("/api/update").and(accept(MediaType.APPLICATION_JSON_UTF8)),hourseHandler::update))
//                .and(route(DELETE("/api/delete/{hourseId}").and(type),hourseHandler::delete));
//    }
//
}
