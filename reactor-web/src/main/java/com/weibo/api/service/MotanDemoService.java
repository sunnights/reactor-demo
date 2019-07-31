package com.weibo.api.service;

import com.weibo.api.motan.transport.extension.MotanExtension;

/**
 * @author sunnights
 */
@MotanExtension
public interface MotanDemoService {
    String hello(String name);

    boolean sleep(long millis);
}
