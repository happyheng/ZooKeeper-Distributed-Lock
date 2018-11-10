package com.happyheng.support;

import java.util.Comparator;

/**
 * zooKeeper中子节点的比较类，比如 0000000000 与 0000000001 ,那么 0000000000 会排在前面
 * Created by happyheng on 2018/11/10.
 */
public class ZooChildNodeComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {

        int o1Length = o1.length();
        int o2Length = o2.length();

        int node1Path = Integer.parseInt(o1.substring(o1Length -10, o1Length));
        int node2Path = Integer.parseInt(o2.substring(o2Length -10, o2Length));

        return node1Path - node2Path;
    }
}
