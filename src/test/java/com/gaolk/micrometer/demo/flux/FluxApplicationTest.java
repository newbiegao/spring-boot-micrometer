package com.gaolk.micrometer.demo.flux;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Random;

@RunWith(SpringRunner.class)

public class FluxApplicationTest {


    @Test
    public void fluxBuildTest() {

        Flux.just("Hello", "World").subscribe(System.out::println);
    }

    /**
     * filter 测试
     */
    @Test
    public void fluxFilterTest() {

        Flux.range( 1 , 20)
                .filter( i -> {

                    if ( i % 2 == 0 ) return true ;
                    return false ;

                } )
                .subscribe( System.out::println) ;

    }

    @Test
    public void fluxZipWithTest() {

        Flux.range(1,10)
                .zipWith( Flux.range( 1 , 12 ) )
                .subscribe( System.out::println  ) ;

    }


    /**
     * 使用generate 构造 Flux
     */
    @Test
    public void fluxFluxGenerate() {

        Flux.generate(
                () -> 0 ,
                ( state , sink ) -> {

                    sink.next( " 3  x  " + state + " = " + 3*state );

                    if( state == 9 ) sink.complete();

                    return state +1 ;
                }
        ).subscribe( System.out::println );

    }


    /**
     * 同步代码包装
     *
     */
    @Test
    public void fluxCallable(){

        Mono<String> infoMono = Mono.fromCallable( () ->{
            Thread.sleep(5000);
            return "info ---" +  Thread.currentThread().getName()  ;
        } ).log().subscribeOn(Schedulers.elastic());

        String info = infoMono.block( Duration.ofSeconds(10) ) ;

        System.out.println(info);

    }


}
