package com.weibo.api.vlog.service;

import java.util.concurrent.TimeUnit;

/**
 * @author sunnights
 */
public class MotanDemoServiceImpl implements MotanDemoService {
    @Override
    public String hello(String name) {
        System.out.println(name);
        return "Hello " + name;
    }

    @Override
    public boolean sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
