package com.gaolk.micrometer.demo;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Service
public class TimerServiceImpl {

    @Autowired
    private MeterRegistry meterRegistry ;

    public String doSomeTask(){

        // time start

        Timer.Sample sample = Timer.start( meterRegistry ) ;

        try {

            WebClient webClient = WebClient.create() ;

            Mono<String> resp =  webClient.get()
                    .uri("http://www.ifeng.com/")
                    .retrieve()
                    .bodyToMono(String.class) ;

           return resp.block() ;

        } finally {

            sample.stop( Timer.builder("com.wehtole.app.timer")
                    .description("this is a service timer test")
                    .tags( Arrays.asList(Tag.of("class" , "TimerServiceImpl") , Tag.of("method" , "doSomeTask") ) )
                    .register(meterRegistry)
            ) ;

        }
    }

    @Timed(value = "com.wehtole.timed",extraTags = {"timed","anotions"})
    public String doWorkAntions(){

        WebClient webClient = WebClient.create() ;

        Mono<String> resp =  webClient.get()
                .uri("https://xueqiu.com/")
                .retrieve()
                .bodyToMono(String.class) ;

        return resp.block() ;

    }

}
