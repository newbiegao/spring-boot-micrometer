package com.gaolk.micrometer.demo;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Random;

@RestController
@Timed
public class MicrometerControl {

    @Autowired
    private MeterRegistry registry ;

    @Autowired
    private TimerServiceImpl timerService ;

    @RequestMapping("/info")
    public String info(){

        registry.counter("info.count", Arrays.asList(Tag.of("info_url" , "/info" )) ).increment();
        return String.valueOf(registry.get("info.count").counter().count())  ;

    }



    @RequestMapping("/user")
    public String user(){

        Random random = new Random() ;
        return String.valueOf(random.longs())  ;

    }


    @RequestMapping("/urls")
    public String urls() {

        String st = timerService.doSomeTask() ;

        return st.substring( 0 , 200)  ;
    }

    @RequestMapping("/urls2")
    public String urls2() {

        String st = timerService.doWorkAntions() ;

        return st.substring( 0 , 200)  ;
    }

}
