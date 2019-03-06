package com.billetsdoux.webflux.com.billetsdoux.webflux.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class SomeController {

    @GetMapping("/common")
    public String commonHandler(){


        log.info("common -- start");
        String result = doSome("common handler");
        log.info("common -- end");
        return result;

    }

    @GetMapping("/mono")
    public Mono<String> monoHandler(){

        log.info("mono -- start");
        Mono<String> result = Mono.fromSupplier(() -> doSome("mono start"));

        log.info("mono -- end");
        return result;
    }

    @GetMapping("/fluxHandler")
    Flux<String> fluxHandler(){
        return Flux.just("beijing","shanghai");
    }

    private String doSome(String msg){

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msg;
    }

    @GetMapping("/times")
    public Flux<String> timeHandler(@RequestParam List<String> cities){
        log.info("flux -- start");
        Flux<String> result = Flux.fromStream(cities.stream().map(i -> doSome("elem-" + i)));
        log.info("flux -- end");

        return result;

    }
}
