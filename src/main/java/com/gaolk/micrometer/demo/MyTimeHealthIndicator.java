package com.gaolk.micrometer.demo;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;


@Component
public class MyTimeHealthIndicator implements HealthIndicator {

    private Integer count = 0 ;

    @Override
    public Health health() {

        count++ ;
        System.out.println( count.toString()  + "--" +  String.valueOf(count % 2) ) ;

        if( count % 2  == 1 )
        {
            return Health.status( Status.UP).build() ;
        }

        return Health.status( Status.DOWN).build() ;
    }
}
