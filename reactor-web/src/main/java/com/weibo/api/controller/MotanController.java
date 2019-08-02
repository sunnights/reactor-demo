package com.weibo.api.controller;

import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import com.weibo.api.vlog.service.MotanDemoService;
import com.weibo.api.vlog.service.MotanDemoServiceExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author sunnights
 */
@RestController
public class MotanController {
    MotanDemoService service;
    MotanDemoServiceExtension reactorService;

    @PostConstruct
    public void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"classpath*:motan_demo_server.xml"});
        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
        System.out.println("server start...");

        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"classpath:motan_demo_client.xml"});
        service = (MotanDemoService) ctx.getBean("motanDemoReferer");
        reactorService = (MotanDemoServiceExtension) ctx.getBean("motanDemoExtensionReferer");
    }

    @RequestMapping("/motan")
    public ResponseEntity<String> hello(@RequestParam("latency") long latency) {
        try {
            TimeUnit.MILLISECONDS.sleep(latency);
            return ResponseEntity.ok("ok");
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("unavailable");
        }
    }

    @RequestMapping("/netty4")
    public Boolean hello2(@RequestParam("latency") long latency) {
        try {
            return service.sleep(latency);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @RequestMapping("/reactor")
    public Mono<Boolean> hello3(@RequestParam("latency") long latency) {
        try {
            return reactorService.sleepReactor(latency);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Mono.just(false);
    }

    @RequestMapping("/gc")
    public Boolean gc() {
        System.gc();
        return true;
    }
}
