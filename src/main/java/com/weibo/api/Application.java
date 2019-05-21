package com.weibo.api;

import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

import java.util.concurrent.TimeUnit;

/**
 * @author sunnights
 */
public class Application {
    public static void main(String[] args) {
        DisposableServer server = TcpServer.create()
                .host("localhost")
                .port(8080)
                .doOnConnection(connection ->
                        connection.addHandler(new ReadTimeoutHandler(10, TimeUnit.SECONDS)))
//                .handle((inbound, outbound) ->
//                        outbound.options(sendOptions -> sendOptions.flushOnEach(false))
//                                .sendString(Mono.just("hello\n")))
                .bindNow();
        server.onDispose().block();
    }
}
