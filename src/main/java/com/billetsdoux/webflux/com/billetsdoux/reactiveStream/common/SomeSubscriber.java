package com.billetsdoux.webflux.com.billetsdoux.reactiveStream.common;


import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

public class SomeSubscriber implements Flow.Subscriber<String>{

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

    // 订阅者每接受一次消息数据，就会自动调用一次该方法
    @Override
    public void onNext(String item) {

        System.out.println("当前订阅者正在消费的消息为：" + item);

        try {
//            TimeUnit.SECONDS.sleep(1);
            TimeUnit.MICROSECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 设置订阅者向发布者再次订阅消息的数量，即每消费一条消息，则再向发布者订阅消息

        this.subscription.request(8);

        // 当满足某种条件取消订阅
        /*if (xxx){
            this.subscription.cancel();
        }*/


    }

    // 当订阅过程中出现异常时会自动调用该方法
    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        // 取消订阅关系
        this.subscription.cancel();

    }

    // 当令牌中的所有消息全部消费完毕会自动调用该方法
    @Override
    public void onComplete() {

        System.out.println("所有消息消费完毕");
    }
}
