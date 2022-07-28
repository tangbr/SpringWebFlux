package com.atguigu.webfluxdemo002.handler;

import com.atguigu.webfluxdemo002.entity.User;
import com.atguigu.webfluxdemo002.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public class UserHandler {

    private final UserService userService;
    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> getUserById(ServerRequest request){
        int userId = Integer.valueOf(request.pathVariable("id"));
        //空值处理
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<User> userMono = this.userService.getUserById(userId);
        //使用Reactor操作符flatMap把对象userMono进行转换变成流再返回
        return
             userMono.flatMap(person -> ServerResponse.ok()
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(fromObject(person)))
                     .switchIfEmpty(notFound); //如果为空则返回notFound
    }


    //查询所有
    public Mono<ServerResponse> getAllUsers(ServerRequest request){
        Flux<User> users = this.userService.getAllUser();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users,User.class);
    }

    //添加
    public Mono<ServerResponse> saveUser(ServerRequest request){
        //得到User 对象
        Mono<User> userMono = request.bodyToMono(User.class);
        return  ServerResponse.ok().build(this.userService.saveUserInfo(userMono));
    }
}
