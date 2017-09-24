package com.home.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.model.*;
import com.home.repository.HourseRepository;
import com.home.repository.UserRepository;
import com.home.util.ServerResponseUtil;
import com.home.vo.*;
import com.home.vo.PageRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Created by Administrator on 2017/8/20.
 */
@Component
public class HourseHandler {
    @Autowired
    HourseRepository hourseRepository;

    @Autowired
    UserRepository userRepository;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HourseHandler.class);

    public static final ApiResponse noData = new ApiResponse(200,"page parameter error",Collections.EMPTY_LIST,0,0,0);

    public static final NoPagingResponse error = NoPagingResponse.error("参数不对，服务器拒绝响应");

    private List<BaseHourse> empty = Collections.EMPTY_LIST;

    public Mono<ServerResponse> getHourses(ServerRequest request){
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        String userId = request.pathVariable("userId");
        String type = request.pathVariable("type");
        Mono<PageRequest> page = request.bodyToMono(PageRequest.class).switchIfEmpty(Mono.just(new PageRequest())).cache(Duration.ofSeconds(5));
//        System.out.println(page.block().getName());//极有可能因为这个block
//        Flux<BaseHourse> hourses = hourseRepository.findByCreateByOrIsPublic(sort,request.pathVariable("userId"),"1")
//                .filter(res -> res.getType().equals(request.pathVariable("type")))
//                .filter(res -> {
//                    boolean bool;
//                    if(StringUtils.isEmpty(title)){
//                        bool = true;
//                    } else {
//                        bool = res.getTitle().contains(title);
//                    }
//                    return bool;
//                });
        User user = userRepository.findById(userId).block();
        return page.flatMap(p -> {
            Flux<BaseHourse> hour;
            Mono<Long> count;
//            BaseHourse baseHourse = new BaseHourse();
//            baseHourse.setType(type);
//            baseHourse.setIsDeleted("0");
//            ExampleMatcher matcher = ExampleMatcher.matching()
//                    .withMatcher("type",match -> match.startsWith())
//                    .withMatcher("isDeleted",match -> match.startsWith());
//            if(!StringUtils.isEmpty(p.getName())){
//                baseHourse.setTitle(p.getName());
//                matcher.withMatcher("title",match -> match.contains());
//            }
//            Example<BaseHourse> example = Example.of(baseHourse);
              Pageable pageable = org.springframework.data.domain.PageRequest.of(p.getPageNumber(),p.getPageSize(), Sort.Direction.DESC,"createDate");
//            hour = hourseRepository.findByCreateByOrIsPublic(pageable,example,userId,"1");
            if(user.getType() == 0) {
                if (StringUtils.isEmpty(p.getName())) {
                    hour = hourseRepository.findByCreateByAndTypeAndIsDeletedOrIsPublicAndTypeAndIsDeleted(
                            pageable, userId, type, "0", "1", type, "0");
                    count = hourseRepository.countByCreateByAndTypeAndIsDeletedOrIsPublicAndTypeAndIsDeleted(userId, type, "0", "1", type, "0");
                } else {
                    hour = hourseRepository.findByCreateByAndTypeAndTitleLikeAndIsDeletedOrIsPublicAndTypeAndTitleLikeAndIsDeleted(
                            pageable, userId, type, p.getName(), "0", "1", type, p.getName(), "0");
                    count = hourseRepository.countByCreateByAndTypeAndTitleLikeAndIsDeletedOrIsPublicAndTypeAndTitleLikeAndIsDeleted(
                            userId, type, p.getName(), "0", "1", type, p.getName(), "0");
                }
            } else {
                if (StringUtils.isEmpty(p.getName())) {
                    hour = hourseRepository.findByIsDeletedAndType(pageable,"0",type);
                    count = hourseRepository.countByIsDeletedAndType("0",type);
                } else {
                    hour = hourseRepository.findByIsDeletedAndTypeAndTitleLike(pageable,"0",type,p.getName());
                    count = hourseRepository.countByIsDeletedAndTypeAndTitleLike("0",type,p.getName());
                }
            }
            return ServerResponseUtil.createByMono(ApiResponse.build(count,hour.collectList()
                    ,p.getPageNumber(),p.getPageSize()),ApiResponse.class);
        });
//        System.out.println(hourses.collectList().block().size());
//        Mono<ApiResponse> build = ApiResponse.build(hourses.count(), hourses.collectList().zipWith(page, (list, pag) -> {
//            Integer start = pag.getPageNumber()*pag.getPageSize();
//            Integer end = (pag.getPageNumber()+1)*pag.getPageSize();
//            list = end > list.size() ? list.subList(start,list.size()) : list.subList(start,end);
//            return list;
//        }),page).onErrorReturn(noData);
//        return ServerResponseUtil.createByMono(build,ApiResponse.class);
//        Mono<List<BaseHourse>> hourse = page.flatMap(pageRequest -> hourseRepository.findByCreateByOrIsPublic(sort,request.pathVariable("userId"),"1")
//        .filter(res -> res.getType().equals(request.pathVariable("type")))
//                .filter(res -> {
//                    boolean bool;
//                    if(StringUtils.isEmpty(pageRequest.getName())){
//                        bool = true;
//                    } else {
//                        bool = res.getTitle().contains(pageRequest.getName());
//                    }
//                    return bool;
//                }).collectList());
//        return page.zipWith(hourse).flatMap(re -> {
//            List<BaseHourse> data = re.getT2();
//            PageRequest pageRequest = re.getT1();
//            ApiResponse response= new ApiResponse(200,"success",data,data.size(),pageRequest.getPageNumber(),pageRequest.getPageSize());
//            return ServerResponseUtil.createResponse(response);
//        });
//        Mono<Pageable> pageable = page.map(p -> org.springframework.data.domain.PageRequest
//                .of(p.getPageNumber(),p.getPageSize(), Sort.Direction.DESC,"createDate"));
//        Mono<Page<BaseHourse>> pageData = pageable.zipWith(page).flatMap(a -> {
//            Pageable able = a.getT1();
//            PageRequest pageRequest = a.getT2();
////            Page<BaseHourse> hourse = hourseRepository.findCreateByAndTitleLikeOrIsPublic(able,request.pathVariable("userId"),pageRequest.getName(),"0");
////            return Mono.just(hourse);
//            return hourseRepository.findCreateByAndTitleLikeOrIsPublic(able,request.pathVariable("userId"),pageRequest.getName(),"0");
//        });
//        return pageData.flatMap(data -> ServerResponseUtil.createResponse(new ApiResponse(200,"success",data.getContent(),(int)data.getTotalElements(),
//                data.getNumber(),data.getSize())));
    }

    public Mono<ServerResponse> getHourse(ServerRequest request){
        return hourseRepository.findById(request.pathVariable("hourseId"))
                .flatMap(data -> ServerResponseUtil.createResponse(NoPagingResponse.success(data)))
                .switchIfEmpty(ServerResponseUtil.createResponse(NoPagingResponse.noFound()));
    }

    public Mono<ServerResponse> findHourse(ServerRequest request){
        return hourseRepository.findById(request.pathVariable("hourseId"))
                .flatMap(data ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(fromObject(new NoPagingResponse(200,"success",data)))
                );
    }

    public Mono<ServerResponse> update(ServerRequest request){
        String type = request.queryParam("type").get();
        Class<? extends BaseHourse> clazz = type.equals("1") ? DealHourse.class : RentHouse.class;
        return request.bodyToMono(clazz).flatMap(hourse -> hourseRepository.save(hourse))
                .flatMap(data -> ServerResponseUtil.createResponse(NoPagingResponse.success(data)))
                .onErrorResume(throwable -> ServerResponseUtil.error()).switchIfEmpty(ServerResponseUtil.error());
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        Mono<BaseHourse> hourse = hourseRepository.findById(request.pathVariable("hourseId")).map(res -> {
            res.setIsDeleted("1");
            res.setUpdateBy(request.bodyToMono(User.class).map(User::getId));
            return res;
        });
        return ServerResponseUtil.createByMono(hourse.flatMap(data -> hourseRepository.save(data))
                .flatMap(r -> Mono.just(NoPagingResponse.success(r)))
                .onErrorReturn(error),NoPagingResponse.class);
    }

    public Mono<ServerResponse> create(ServerRequest request){
        String type = request.queryParam("type").get();
        Class<? extends BaseHourse> clazz = type.equals("1") ? DealHourse.class : RentHouse.class;
        Mono<BaseHourse> hourseMono = request.bodyToMono(clazz).map(hourse -> {
            hourse.setCreateDate(new Date());
            hourse.setType(type);
            hourse.setIsDeleted("0");
            return hourse;
        });
        return ServerResponseUtil.createByMono(hourseMono.flatMap(data -> hourseRepository.save(data))
                .flatMap(r -> Mono.just(NoPagingResponse.success(r)))
                .onErrorReturn(error),NoPagingResponse.class);
    }

    public Mono<ServerResponse> getAllHourses(ServerRequest request){
        String type = request.pathVariable("type");
        Integer pageSize = Integer.valueOf(request.queryParam("pageSize").orElse("10"));
        Integer pageNumber = Integer.valueOf(request.queryParam("pageNumber").orElse("0"));
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNumber,pageSize, Sort.Direction.DESC,"createDate");
        Mono<Long> count = hourseRepository.countByTypeAndIsDeleted(type,"0");
//        return hourseRepository.findByTypeAndIsDeleted(pageable,type,"0").collectList()
//                .zipWith(count)
//                .flatMap(data -> ServerResponseUtil.createResponse(FrontResponse.success(
//                        new FrontData(data.getT2().intValue(),pageNumber,pageSize,data.getT1()))));
////                .onErrorResume(throwable -> ServerResponseUtil.error());
//        Mono<Page<BaseHourse>> pages = hourseRepository.findByTypeAndIsDeleted(pageable,type,"0");
        Flux<BaseHourse> hourse = hourseRepository.findByTypeAndIsDeleted(pageable,type,"0");
        return ServerResponseUtil.createByMono(FrontData.build(count,hourse.collectList().onErrorResume(t -> Mono.just(empty)),pageNumber,pageSize)
                .map(data -> new FrontResponse(200,"success",data)),FrontResponse.class)
                .onErrorResume(throwable -> ServerResponseUtil.error());
    }
}
