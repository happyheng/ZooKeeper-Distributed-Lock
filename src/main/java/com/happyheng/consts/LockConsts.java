package com.happyheng.consts;

/**
 *
 * Created by happyheng on 2018/11/7.
 */
public class LockConsts {

    /**
     * 主节点对应path
     */
    public static String LOCK_MASTER_PATH = "/zookeeper_lock_get_good";

    /**
     * 子节点对应path
     */
    public static String LOCK_CHILD_PATH = "/child_lock";

}
