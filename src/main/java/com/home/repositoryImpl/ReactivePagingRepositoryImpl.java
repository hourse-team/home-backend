package com.home.repositoryImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.model.BaseHourse;
import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.reactivestreams.client.*;
import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ReactivePagingRepositoryImpl<T,ID> implements ReactivePagingRepository<T,ID> {

//    private static final Logger logger = LoggerFactory.getLogger(ReactivePagingRepositoryImpl.class);
//
//    @Autowired
//    PagingHourseRepository pagingHourseRepository;
//
//    @Override
//    public <T1 extends BaseHourse> Mono<Page<T1>> findByTypeAndIsDeleted(Pageable pageable, String status, String isDeleted) {
//        Page<T1> page = pagingHourseRepository.findByTypeAndIsDeleted(pageable,status,isDeleted);
//        logger.info(""+page.getTotalElements());
//        return Mono.just(page);
//    }
//
//    @Override
//    public <T1 extends BaseHourse> Mono<Page<T1>> findCreateByAndTitleLikeOrIsPublic(Pageable pageable, String userId, String title, String isPublic) {
//        Page<T1> page;
//        if(StringUtils.isEmpty(title)){
//            page = pagingHourseRepository.findByCreateByOrIsPublic(pageable,userId,isPublic);
//        } else {
//            page = pagingHourseRepository.findByCreateByAndTitleLikeOrIsPublicAndTitleLike(pageable,userId,title,isPublic);
//        }
//        return Mono.just(page);
//    }

    public static void main(String[] args) throws InterruptedException {
        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("hourse_property");
        MongoCollection<Document> collection = database.getCollection("hourse");
        ObjectMapper mapper = new ObjectMapper();
        Flux<BaseHourse> list = Flux.from(collection.find().skip(299000).limit(10)).map(document -> {
            BaseHourse hourse = null;
            String id = document.remove("_id").toString();
            Class clazz = null;
            try {
                clazz = Class.forName(document.remove("_class").toString().replaceAll("mongo","home"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                 Object o = mapper.readValue(document.toJson(),clazz);
                BeanUtils.copyProperties(o,hourse);
                 hourse.setId(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hourse;
        });
        Subscriber subscriber = new PrintDocumentSubscriber();
        list.subscribe(subscriber);
        Thread.sleep(120000);
    }

    @Override
    public <T1 extends BaseHourse> Flux<T1> findByTypeAndIsDeleted(Pageable pageable, String status, String isDeleted) {

        return null;
    }

    private static class PrintDocumentSubscriber implements Subscriber {
        @Override
        public void onSubscribe(Subscription subscription) {
            subscription.request(3);
        }

        @Override
        public void onNext(Object o) {
            System.out.println(o.toString());
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("Failed");
        }

        @Override
        public void onComplete() {
            System.out.println("F");
        }
    }
}
