package com.happyheng.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.IOException;

/**
 * Created by happyheng on 2018/11/6.
 */
@Configuration
@ComponentScan(basePackages = "com.happyheng")
@PropertySource("classpath:config.properties")
public class BaseConfig {

    @Autowired
    Environment env;

    @Bean
    public ZooKeeper zooKeeper(){
        String zooKeeperUrl = env.getProperty("zookeeper.url");
        try {
            return new ZooKeeper(zooKeeperUrl, 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println(event.toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
