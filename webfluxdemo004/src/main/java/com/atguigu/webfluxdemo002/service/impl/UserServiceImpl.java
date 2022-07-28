package com.atguigu.webfluxdemo002.service.impl;

import com.atguigu.webfluxdemo002.entity.User;
import com.atguigu.webfluxdemo002.service.UserService;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


@Repository
public class UserServiceImpl implements UserService {

    private final Map<Integer,User> users = new HashMap<>();

    public UserServiceImpl(){
        this.users.put(1,new User("lucy", "male", 20));
        this.users.put(2,new User("mary", "female", 30));
        this.users.put(3,new User("jack", "male", 50));
    }


    @Override
    public Mono<User> getUserById(int id) {
        return Mono.justOrEmpty(this.users.get(id));
    }

    @Override
    public Flux<User> getAllUser() {
        return Flux.fromIterable(this.users.values());
    }

    @Override
    public Mono<Void> saveUserInfo(Mono<User> userMono) {
        return userMono.doOnNext(person -> {
            int id = users.size() + 1;
            users.put(id,person);
        }).thenEmpty(Mono.empty());
    }
}
