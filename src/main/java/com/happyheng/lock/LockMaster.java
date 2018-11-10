package com.happyheng.lock;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 *
 * Created by happyheng on 2018/11/6.
 */
@Service
public class LockMaster {

    @Autowired
    private ZooKeeper zooKeeper;

    @PostConstruct
    public void init() {


    }
}
