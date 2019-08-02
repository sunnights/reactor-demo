package com.weibo.api.reactor.service;

import com.weibo.api.motan.transport.extension.MotanExtension;

/**
 * @author sunnights
 */
@MotanExtension
public interface MotanDemoService {
    boolean sleep(long millis);
}
