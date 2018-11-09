package com.happyheng.Controller;

import com.happyheng.consts.LockConsts;
import com.happyheng.lock.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Created by happyheng on 2018/11/8.
 */
@RestController
@RequestMapping("/lock")
public class LockController {

    private static final Logger logger = LoggerFactory.getLogger(LockController.class);

    @Autowired
    private LockService lockService;


    @RequestMapping("/goods")
    public String lock(HttpServletRequest request) {

        final int randomNum = (int) (10000 * Math.random());

        logger.info(" randomNum " + randomNum + " 开启 ");
        lockService.lock(LockConsts.LOCK_MASTER_PATH, ()->{
            logger.info(" randomNum " + randomNum + " 执行中 ");
        });
        return "";
    }

}
