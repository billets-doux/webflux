package com.billetsdoux.webflux.com.billetsdoux.reactiveStream.common;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class SomeProcessor extends SubmissionPublisher<String> implements Flow.Processor<Integer, String> {
    // 声明订阅令牌
    private Flow.Subscription subscription;

    // 当发布者第一次发布消息时会自动调用该方法

    // 订阅者对数据的消费就发生在这里
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;

        // 设置订阅者首次向发布者（令牌）订阅消息的数量

        this.subscription.request(10);

    }

    // 将发布者发布的大于50的消息过滤掉，将小于50Integer消息转换为String
    @Override
    public void onNext(Integer item) {

        System.out.println("当前订阅者正在消费的消息为：" + item);

        if (item < 50) {
            this.submit("该消息处理完毕");
        }
        try {
            TimeUnit.MICROSECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        this.subscription.request(8);


    }

    // 当订阅过程中出现异常时会自动调用该方法
    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        // 取消订阅关系
        this.subscription.cancel();

    }

    // 当令牌中的所有消息全部处理完毕会自动调用该方法
    @Override
    public void onComplete() {

        System.out.println("所有消息处理完毕");
    }
}
