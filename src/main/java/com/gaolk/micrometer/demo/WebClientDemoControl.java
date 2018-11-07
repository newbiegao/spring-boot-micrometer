package com.gaolk.micrometer.demo;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.ipc.netty.options.ClientOptions;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

@RestController
public class WebClientDemoControl {

    @Autowired
    private WebClient.Builder webClientBuilder ;

    @RequestMapping("/webclinet/index")
    public String hello(){

        return " Hello , WebClient " ;

    }

    @RequestMapping("/web/delay/2")
    public Mono delay2s() {

        return Mono.just("hello delay 2 ms  ").delayElement(Duration.ofSeconds(2));
    }


    @RequestMapping("/web/delay/5")
    public Mono delay21s()
    {
        return Mono.just("hello delay 5 ms ").delayElement(Duration.ofSeconds(4));
    }



    @RequestMapping("/web/{path}")
    public Mono<String> web(@PathVariable String path ){


        System.out.println( "web: ---> " +  path ) ;
        return this.webClientBuilder
                .filter( (request, next) ->{

                    request.headers().keySet().forEach( key -> System.out.println(key + "==" + request.headers().get(key) )  );
                    return next.exchange(request) ;
                })
                .build()
                .get()
                .uri("http://www."+path+".com")
                .retrieve()
                .bodyToMono(String.class);
    }


    @RequestMapping("/web/zip")
    public Flux<String> zipRequestWeb(){

        Long current = System.currentTimeMillis() ;

        this.webClientBuilder.clientConnector( new ReactorClientHttpConnector(
                builder -> {
                    builder.option(ChannelOption.CONNECT_TIMEOUT_MILLIS , 2000 ) ;
                    builder.afterNettyContextInit( nettyContext -> {
                        nettyContext.addHandlerLast( new ReadTimeoutHandler(30000, TimeUnit.MILLISECONDS)) ;
                    } );
                 }
        )) ;

        WebClient clinet = this.webClientBuilder.build() ;

        Mono<String> baiduReq = clinet.get().uri("http://localhost:8080//web/delay/2")
                            .retrieve()
                            .bodyToMono(String.class);
        Mono<String> m1 = baiduReq.onErrorReturn(" time out 2 ms ") ;

        Mono<String> ifengReq =
                            clinet.get().uri("http://localhost:8080//web/delay/5")
                            .retrieve()
                            .bodyToMono(String.class) ;

        Mono<String> m2 = ifengReq.onErrorReturn(" [error] time out 5 ms ") ;

        m1 =  m1.doOnEach(this::printTick) ;

        m2=   m2.doOnEach(this::printTick) ;

        return Flux.zip( m1.timestamp() , m2.timestamp() , (baiduInfo , ifengInfo) ->{

            Long t1 = (baiduInfo.getT1() - current) / 1000 ;
            Long t2 = (ifengInfo.getT1() - current) /1000 ;
            Long allTime = ( System.currentTimeMillis() - current ) / 1000 ;
            return baiduInfo.getT2() + ifengInfo.getT2() + "time : t1 = " + t1 + " , t2 = " + t2  + " ALL Time : " + allTime ;

        } ) ;

    }

    // 显示消息内容和类型
    private void printTick( Signal<String> info ){

        System.out.println("------ tick info ----- value =  " + info.get() + "---- type = " + info.getType().toString()) ;
    }

    @RequestMapping("/web/flux")
    public String  mulitWebRequest(){

        WebClient clinet = this.webClientBuilder.build() ;

        Mono<String> baidu =
        clinet.get().uri("http://localhost:8080/web/delay/2")
        .retrieve()
        .bodyToMono(String.class);


        Mono<String> ifeng =
        clinet.get().uri("http://localhost:8080//web/delay/5")
        .retrieve()
        .bodyToMono(String.class) ;

        long t1 = System.currentTimeMillis() ;

        String rec = baidu.block() + ifeng.block() ;

        long t2 = System.currentTimeMillis() ;

        System.out.println("ifeng -->" + (t2-t1) );

        return rec ;

    }

}
