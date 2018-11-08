package com.happyheng.node;

import org.apache.zookeeper.ZooKeeper;

/**
 *
 * Created by happyheng on 2018/11/7.
 */
public abstract class Node {

    protected ZooKeeper zooKeeper;

    public Node(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public abstract   void doJob();
}
