/*
 *  Copyright 2009-2016 Weibo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
