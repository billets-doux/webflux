package com.billetsdoux.webfluxcommon.handler;

import com.billetsdoux.webfluxcommon.bean.Student;
import com.billetsdoux.webfluxcommon.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class StudentHandler {

    private final StudentRepository repository;

    @Autowired
    public StudentHandler(StudentRepository repository) {
        this.repository = repository;
    }

    // 查询
    public Mono<ServerResponse> findAllHandle(ServerRequest request){

        return ServerResponse

                // 指定响应码(返回BodyBuilder的方法称为响应体设置中间方法)
                .ok()

                // 指定请求体中的内容类型
                .contentType(MediaType.APPLICATION_JSON_UTF8)

                // 响应体设置终止方法，构建响应体
                .body(repository.findAll(), Student.class);
    }

    // 添加

    public Mono<ServerResponse> saveHandle(ServerRequest request){
        // 从请求中获取需要添加的数据，并将其封装为指定的类型对象，存放到Nono中
        Mono<Student> studentMono = request.bodyToMono(Student.class);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)

                .body(repository.saveAll(studentMono),Student.class);


    }

    // 删除
    public Mono<ServerResponse> delHandle(ServerRequest request){

        String id = request.pathVariable("id");
        return repository.findById(id)
                .flatMap(student -> repository.delete(student).then(ServerResponse.ok().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
