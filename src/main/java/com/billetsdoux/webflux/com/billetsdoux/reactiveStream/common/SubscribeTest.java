package com.billetsdoux.webflux.com.billetsdoux.reactiveStream.common;

import java.util.Random;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class SubscribeTest {
    public static void main(String[] args) {
        // 创建发布者

        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        // 创建订阅者

        SomeSubscriber subscriber = new SomeSubscriber();
        // 创建处理器
        SomeProcessor processor = new SomeProcessor();
        publisher.subscribe(processor);
        processor.subscribe(subscriber);
        // 建立订阅关系

        // 发布者生产并发送消息
        for (int i = 0; i < 300; i++) {
            int item = new Random().nextInt(100);

            // 发布消息，发布者缓存满时 submit()方法阻塞，因为发布者不具有无限缓冲区

            System.out.println("生产出第" + i + "条消息");
            publisher.submit(item);
        }
        // 关闭发布者

        publisher.close();

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
