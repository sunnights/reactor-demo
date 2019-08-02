package com.weibo.api.reactor.service;

import com.weibo.api.motan.rpc.ResponseFuture;
import reactor.core.publisher.Mono;

public interface MotanDemoServiceExtension extends MotanDemoService {
    ResponseFuture sleepAsync(long millis);

    Mono<Boolean> sleepReactor(long millis);
}
