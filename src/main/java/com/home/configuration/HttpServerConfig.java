package com.home.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.ipc.netty.http.server.HttpServer;


/**
 * Created by Administrator on 2017/8/19.
 */
@Configuration
public class HttpServerConfig {
    @Autowired
    private Environment environment;

    @Bean
    public HttpServer httpServer(RouterFunction<?> routerFunction){
        HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer server = HttpServer.create("localhost",environment.getProperty("server.port",Integer.class));
        server.newHandler(adapter);
        return server;
    }
}
