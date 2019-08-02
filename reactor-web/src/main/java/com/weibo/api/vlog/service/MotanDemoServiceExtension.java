package com.weibo.api.vlog.service;

import com.weibo.api.motan.rpc.ResponseFuture;
import reactor.core.publisher.Mono;

public interface MotanDemoServiceExtension extends MotanDemoService {
    ResponseFuture helloAsync(String name);

    Mono<String> helloReactor(String name);

    ResponseFuture sleepAsync(long millis);

    Mono<Boolean> sleepReactor(long millis);
}
