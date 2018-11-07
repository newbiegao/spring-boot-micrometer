package com.gaolk.micrometer.demo;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientCodecCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@Configuration
public class AppConfig {



    @Value("${spring.application.name}")
    public String appName ;

    @Autowired
    private MeterRegistry meterRegistry ;

    /**
     * 为监控指标设置统一标签
     * @return
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(){

       return new MeterRegistryCustomizer<MeterRegistry>(){

           @Override
           public void customize(MeterRegistry registry) {
               registry.config().commonTags(Arrays.asList(Tag.of("appname" , appName) , Tag.of("region" , "order" ) ) ) ;
           }
       };

    }

    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect(meterRegistry);
    }

    @Bean
    public WebClientCustomizer webClientCustomizer(  )
    {

        // 实现函数方法 , 构造默认webclient
        WebClientCustomizer customizer = (builder) -> {

                builder.defaultHeader("a","a") ;
                builder.clientConnector( new ReactorClientHttpConnector(clb -> {
                    clb.option(ChannelOption.CONNECT_TIMEOUT_MILLIS , 1000 )  ;
            } ) );
        }  ;

        return customizer ;

    }

}
