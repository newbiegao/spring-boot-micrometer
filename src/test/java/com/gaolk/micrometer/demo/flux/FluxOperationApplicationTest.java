package com.gaolk.micrometer.demo.flux;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)

public class FluxOperationApplicationTest {


    /**
     * cast 操作
     */
    @Test
    public void castTest() {

        Flux.range(1,10)
                .cast( Integer.class )
                .subscribe(System.out::println);

    }

    /**
     * 合并流
     */
    @Test
    public void mergeTest(){

        Flux<String> fl2 = Flux.just("D","E","F") ;
        Flux<String> fl1 = Flux.just("A","B","C") ;


        Flux.merge(fl1 , fl2)
            .subscribe( System.out::println );

    }


    /**
     *  map
     */
    @Test
    public void mapTest() {

        AtomicInteger index = new AtomicInteger() ;

        Flux.just("A","B","C")
                .map( item ->{

                    index.addAndGet(1);
                    return item + "-" + index ;

                } )
                .subscribe(System.out::println);

    }

    /**
     * flatMap
     */
    @Test
    public void flatMapTest(){

        Collectors.toList() ;

        Flux.range(1,10)
                .filter( item -> {

                    if( item % 2 == 0) return true ;
                    return false ;
                } )
                .flatMap( item ->{

                    return Flux.just(item.toString() + "A") ;
                } )
                .subscribe(System.out::println);
    }

    @Test
    public void intervalTest(){

        Flux.range(0,100)
            .filter( (along) ->{

                if( along % 2 == 0 ) return true ;
                return false ;

            }).map( (along )  ->{

                System.out.println( along );
                return along ;
            }).flatMap( ( along ) ->{

                return Flux.just(along);

            }).take(10).subscribe() ;
    }

}
