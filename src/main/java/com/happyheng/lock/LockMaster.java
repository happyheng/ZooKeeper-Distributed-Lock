package com.happyheng.lock;

import com.happyheng.consts.LockConsts;
import com.happyheng.node.MasterNode;
import com.happyheng.node.Node;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

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

        try {
            List<String> childList = zooKeeper.getChildren("/", false);
            for (String child : childList) {
                System.out.println(child);
            }

            // 启动时即监听对应 master 节点
            boolean createSuccess = false;
            try {
                // 创建 /lock_getGood
                zooKeeper.create(LockConsts.LOCK_MASTER_PATH + "/master", "".getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                createSuccess = true;
            } catch (KeeperException.NodeExistsException e) {

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
            }

            if (createSuccess) {
                Node node = new MasterNode(zooKeeper);
                node.doJob();
            }


        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
