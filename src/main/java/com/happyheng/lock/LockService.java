package com.happyheng.lock;

import com.alibaba.fastjson.JSON;
import com.happyheng.consts.LockConsts;
import com.happyheng.support.ZooChildNodeComparator;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 *
 * Created by happyheng on 2018/11/8.
 */
@Service
public class LockService {

    private static final Logger logger = LoggerFactory.getLogger(LockService.class);

    @Autowired
    private ZooKeeper zooKeeper;

    public void lock(String lockBasePath,final Runnable runnable) {


        /**
         * 可以使用 CountDownLatch 来实现同步，每次调用 lock（） 方法的时候生成一下，传入到 doLock（） 中
         */

        if (StringUtils.isEmpty(lockBasePath) || runnable == null) {
            return;
        }

        // 创建一个顺序的、临时的节点
        try {
            String childCurrentPath = zooKeeper.create(lockBasePath + LockConsts.LOCK_CHILD_PATH, "".getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            String[] childPathArray = childCurrentPath.split("[/]");
            String childRelativePath = childPathArray[childPathArray.length - 1];

            // 开始lock
            doLock(lockBasePath, childRelativePath, runnable);

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doLock(String lockBasePath,final String childRelativePath, final Runnable runnable) throws KeeperException, InterruptedException {

        String childCurrentPath = lockBasePath + "/" + childRelativePath;
        // 遍历子节点，
        List<String> masterChildRelativePathList = zooKeeper.getChildren(lockBasePath, false);
        // 将子节点置为一个有序的子节点，从小到大
        masterChildRelativePathList.sort(new ZooChildNodeComparator());

        logger.info(childRelativePath + " path " + JSON.toJSONString(masterChildRelativePathList));

        // 有两种情况，一种是第一个节点，一种不是
        if (childRelativePath.equals(masterChildRelativePathList.get(0))) {

            logger.info(childRelativePath + " 抢到了分布式锁，开始执行");

            // 说明抢到了分布式锁，开始做分布式任务，完成之后，将节点删除
            runnable.run();

            logger.info(childRelativePath + " 抢到了分布式锁，执行完成");

            // 删除节点
            zooKeeper.delete(childCurrentPath, -1);

            logger.info(childRelativePath + " 执行完成后删除了节点\n\n\n");

        } else {

            // 监听上一个节点，如果上一个节点删除，再遍历子节点
            int currentPathIndex = masterChildRelativePathList.indexOf(childRelativePath);

            // 监听上一个节点
            String childPreRelativePath = masterChildRelativePathList.get(currentPathIndex - 1);
            String childPreCurrentPath = lockBasePath + "/" + childPreRelativePath;

            logger.info(childRelativePath + " 监听 " + childPreRelativePath);

            zooKeeper.getData(childPreCurrentPath, event -> {
                if (Watcher.Event.EventType.NodeDeleted != event.getType()) {
                    logger.info("节点非删除  " + event.getType().getIntValue());
                    return;
                }

                logger.info(childRelativePath + " 上一个节点被删除，开始继续lock");

                // 重复 doLock
                try {
                    doLock(lockBasePath, childRelativePath, runnable);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, null);


        }
    }

}
