package com.home.handler;

import com.home.model.User;
import com.home.repository.UserRepository;
import com.home.util.ServerResponseUtil;
import com.home.vo.ApiResponse;
import com.home.vo.NoPagingResponse;
import com.home.vo.PageRequest;
import com.mongodb.util.JSON;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.WebSessionManager;
import org.springframework.web.server.session.WebSessionStore;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Created by Administrator on 2017/8/19.
 */
@Component
public class Userhandler {
    @Autowired
    private UserRepository userRepository;

    @Value("${qiniu.access_key}")
    private String accessKey;

    @Value("${qiniu.secret_key}")
    private String secretKey;

    private WebSession webSession;

    private static final Logger logger = LoggerFactory.getLogger(Userhandler.class);

    private Set<HttpMethod> httpMethodSet = new HashSet<>(Arrays.asList(new HttpMethod[]{HttpMethod.GET, HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT}));


    public Mono<ServerResponse> getUser(ServerRequest request){
        return request.bodyToMono(User.class).flatMap(user -> userRepository.findByUsernameAndPassword(user.getUsername(),
                user.getPassword())).flatMap(data -> ServerResponseUtil.createResponse(NoPagingResponse.success(data)))
                .switchIfEmpty(ServerResponseUtil.createResponse(new NoPagingResponse(201,"fail","账号不存在或密码错误")))
                .onErrorResume(throwable -> ServerResponseUtil.error());
    }

    public Mono<ServerResponse> index(ServerRequest request){
        Mono<ClassPathResource> cp = Mono.just(new ClassPathResource("templates/index.html"));
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(cp,ClassPathResource.class);
    }

    public Mono<ServerResponse> register(ServerRequest request){
        Mono<User> newUser = request.bodyToMono(User.class);
        return newUser.flatMap(n -> {
            if(n.getType() == null || (n.getType() != 1 && n.getType() != 0)){
                return ServerResponseUtil.createResponse(new NoPagingResponse(202,"error","未指定用户类型"));
            }
            return userRepository.findByUsername(n.getUsername()).flatMap(user -> ServerResponseUtil.createResponse(
                    new NoPagingResponse(201,"fail","账号已存在")))
                    .switchIfEmpty(userRepository.insert(n).flatMap(user -> ServerResponseUtil.createResponse(
                            NoPagingResponse.success(user)))).onErrorResume(throwable -> ServerResponseUtil.error());
        }).switchIfEmpty(ServerResponseUtil.createResponse(new NoPagingResponse(202,"error","请输入数据！")));
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request){
        return userRepository.deleteById(request.pathVariable("userId"))
                .flatMap(data -> ServerResponseUtil.createResponse(new NoPagingResponse(200,"success",data)))
                .onErrorResume(throwable -> ServerResponseUtil.error());
    }

    public Mono<ServerResponse> getAllUser(ServerRequest request){
        Mono<PageRequest> page = request.bodyToMono(PageRequest.class).switchIfEmpty(Mono.just(new PageRequest())).cache(Duration.ofSeconds(5));
        Flux<User> users = userRepository.findAll(Sort.by(new Sort.Order(DESC,"createDate")));
        Mono<ApiResponse> apiResponse = ApiResponse.build(users.count(),users.collectList().zipWith(page, (list, pag) -> {
            Integer start = pag.getPageNumber()*pag.getPageSize();
            Integer end = (pag.getPageNumber()+1)*pag.getPageSize();
            list = end > list.size() ? list.subList(start,list.size()) : list.subList(start,end);
            return list;
        }),page).onErrorReturn(HourseHandler.noData);
        return ServerResponseUtil.createByMono(apiResponse,ApiResponse.class);
    }

    public Mono<ServerResponse> getUploadToken(ServerRequest request){
        String saveKey = request.queryParam("saveKey").orElse(null);
        String bucket = "dingshengfangchan";
        Auth auth = Auth.create(accessKey, secretKey);

        StringMap policy = new StringMap();
        policy.put("insertOnly", 1);
        policy.putNotEmpty("saveKey", saveKey);

        String token = auth.uploadToken(bucket, null, 3600, policy);
        logger.debug("bucket {}, qiniu upload token: {}", bucket, token);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(200,"success",token)));
    }
    public static void main(String[] args){
//        Mono<Integer> mono = Mono.just(1);
//        Mono<String> str= Mono.just("132");
//        String str1 = str.block();
//        Mono<String> mono1 = mono.zipWith(str,(m,s) -> s+m);
//                mono1.zipWith(str,(y,t) -> y+t).subscribe(System.out::println);
        Mono<PageRequest> page = Mono.just(new PageRequest());
        String name = page.block().getName();
        page.subscribe(System.out::println);
        Flux<Integer> hourses = Flux.just(1,2,3,4,5,6,7).filter(a -> {boolean bool;
            if(StringUtils.isEmpty(name)){
                bool = true;
            } else {
                bool = a > 8;
            }
            return bool;
        });
        Mono<ApiResponse> build = ApiResponse.build(hourses.count(), hourses.collectList().zipWith(page, (list, pag) -> {
            Integer start = pag.getPageNumber()*pag.getPageSize();
            Integer end = (pag.getPageNumber()+1)*pag.getPageSize();
            list = end > list.size() ? list.subList(start,list.size()) : list.subList(start,end);
            return list;
        }),page);
        build.subscribe(System.out::println);
    }
}
