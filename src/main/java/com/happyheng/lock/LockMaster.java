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


//            // 启动时即监听对应 master 节点
//            boolean createSuccess = false;
//            try {
//                // 创建 /lock_getGood
//                zooKeeper.create(LockConsts.LOCK_MASTER_PATH + "/master", "".getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//                createSuccess = true;
//            } catch (KeeperException.NodeExistsException e) {
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (KeeperException e) {
//                e.printStackTrace();
//            }
//
//            if (createSuccess) {
//
//                new Thread(()->{
//                    Node node = new MasterNode(zooKeeper);
//                    node.doJob();
//                }).start();
//
//            }



    }
}
