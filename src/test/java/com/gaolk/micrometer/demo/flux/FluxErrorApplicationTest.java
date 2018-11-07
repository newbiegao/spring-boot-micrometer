package com.gaolk.micrometer.demo.flux;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)

public class FluxErrorApplicationTest {


    /**
     * error 操作 ，终止流程
     */
    @Test
    public void suberror1() {

        Flux<Integer> flux =  Flux.range(1,10)
                .map( this::doErrorOperator)
                .onErrorReturn(-1)
                .map( this::doMapOperator ) ;

        flux.subscribe(
                item ->{
                    System.out.println("VALUE --> " + item );
                } ,
                error ->{
                    System.out.println("ERROR --> " + error );
                }
        );

    }

    /**
     *
     * error 操作恢复流程
     */
    @Test
    public void suberror2() {

        Flux<Integer> flux =  Flux.range(1,10)
                .map( this::doErrorOperator)
                .map( this::doMapOperator )
                .onErrorResume( e -> Flux.just(-1) ) ;

        flux.subscribe( item -> System.out.println("VALUE2 --> " + item ) );


    }

    private Integer doMapOperator( Integer item ){
        if( item > 0 ) return item + 1 ;
        return item  ;
    }

    private Integer doErrorOperator( Integer item ){
        if( item > 5 )
        {
            throw new RuntimeException("error when item value ==" + item ) ;
        }
        return item  ;
    }

}
