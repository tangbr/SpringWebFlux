package com.atguigu.webfluxdemo002;

import com.atguigu.webfluxdemo002.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


// 必须先启动server中的 main 方法得到 port number 后, 再运行此处的 main 方法
public class Client {

    public static void main(String[] args) {
        //调用服务器地址
        WebClient webClient = WebClient.create("http://127.0.0.1:57792");
        //根据id查询
        String id = "1";
        User userresult = webClient.get().uri("/users/{id}",id)
                .accept(MediaType.APPLICATION_JSON).retrieve()
                .bodyToMono(User.class)   //此处要用无参构造
                .block();   //相当于订阅
        System.out.println(userresult.getName());

        //查询所有
        Flux<User> results = webClient.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(User.class);//此处为何没有.block()?

        results.map(stu -> stu.getName()).buffer().doOnNext(System.out::println)
                .blockFirst();
    }
}
