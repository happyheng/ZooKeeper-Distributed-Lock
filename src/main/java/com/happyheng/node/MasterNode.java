package com.happyheng.node;

import com.happyheng.consts.LockConsts;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 *
 * Created by happyheng on 2018/11/7.
 */
public class MasterNode extends Node{

    private static final Logger logger = LoggerFactory.getLogger(MasterNode.class);

    public MasterNode(ZooKeeper zooKeeper) {
        super(zooKeeper);
    }

    @Override
    public void doJob() {

        // 监听下面的 /zookeeper_lock_get_good ，如果时间超过5s，将其删除
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                List<String> childNodeList = zooKeeper.getChildren(LockConsts.LOCK_MASTER_PATH, false);

                if (CollectionUtils.isEmpty(childNodeList)) {
                    // logger.info("未发现子节点");
                    continue;
                }

                String firstNodePath = childNodeList.get(0);
                String firstNodeCurrentPath = LockConsts.LOCK_MASTER_PATH + "/" + firstNodePath;
                Stat stat = new Stat();
                zooKeeper.getData(firstNodeCurrentPath, false, stat);
                /**
                 * 如果ctime比现在的时间大10秒，那么删除此节点
                 */
                if (System.currentTimeMillis() - stat.getCtime() > 10 * 1000) {
                   // logger.info("大于10秒，删除此节点，节点路径为" + firstNodeCurrentPath);
                   // zooKeeper.delete(firstNodeCurrentPath, -1);
                } else {
                   // logger.info("未大于10秒，节点路径为" + firstNodeCurrentPath);
                }

            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
