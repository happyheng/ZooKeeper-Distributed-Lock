package com.happyheng.lock;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 *
 * Created by happyheng on 2018/11/6.
 */
@Service
public class LockBaseTest {
    @Autowired
    private ZooKeeper zooKeeper;

    @PostConstruct
    public void init() {

        try {
            List<String> childList = zooKeeper.getChildren("/", false);
            for (String child : childList) {
                System.out.println(child);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
