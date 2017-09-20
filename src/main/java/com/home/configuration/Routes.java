package com.home.configuration;

import com.home.handler.HourseHandler;
import com.home.handler.Userhandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

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
                        .and(route(POST("/api/{userId}/hourses/{type}").and(accept(MediaType.APPLICATION_JSON_UTF8)),hourseHandler::getHourses)
                        .and(route(GET("/api/hourse/{hourseId}").and(accept(MediaType.APPLICATION_JSON_UTF8)),hourseHandler::getHourse))
                        .and(route(PUT("/api/update").and(accept(MediaType.APPLICATION_JSON_UTF8)),hourseHandler::update))
                        .and(route(DELETE("/api/delete/{hourseId}").and(type),hourseHandler::delete)))
                        .and(route(POST("/api/hourse/create").and(type),hourseHandler::create))
                .and(route(GET("/api/index").and(accept(MediaType.TEXT_HTML)),userhandler::index))
                .and(route(POST("/api/account").and(type),userhandler::register))
                .and(route(DELETE("/api/deleteUser/{userId}").and(type),userhandler::deleteUser))
                .and(route(POST("/api/accounts").and(type),userhandler::getAllUser))
                .and(route(GET("/api/qiniu/token").and(type),userhandler::getUploadToken))
                .and(route(GET("/api/front/hourses/{type}").and(type),hourseHandler::getAllHourses));
    }

}
