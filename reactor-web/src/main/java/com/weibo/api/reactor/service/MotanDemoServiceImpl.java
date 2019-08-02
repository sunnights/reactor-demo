package com.weibo.api.reactor.service;

import java.util.concurrent.TimeUnit;

/**
 * @author sunnights
 */
public class MotanDemoServiceImpl implements MotanDemoService {
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
