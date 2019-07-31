package com.weibo.api;

import com.weibo.api.service.MotanDemoService;
import com.weibo.api.service.MotanDemoServiceExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import reactor.core.publisher.Mono;

public class DemoRpcClient {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"classpath:motan_demo_client.xml"});
        MotanDemoService service = (MotanDemoService) ctx.getBean("motanDemoReferer");
        for (int i = 0; i < 3; i++) {
            System.out.println(service.hello("motan" + i));
            Thread.sleep(1000);
        }
        MotanDemoServiceExtension extensionService = (MotanDemoServiceExtension) ctx.getBean("motanDemoExtensionReferer");
        Mono<String> str = extensionService.helloReactor("123");
        System.out.println(str.block());
        System.out.println("motan demo is finish.");
        System.exit(0);
    }

}