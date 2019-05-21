package com.weibo.api.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Consumer;

public class LambdaSubscriber<T> implements Subscriber<T> {
    Consumer<? super T> dataConsumer;
    Consumer<? super Throwable> errorConsumer;
    Runnable completeConsumer;
    Consumer<? super Subscription> subscriptionConsumer;

    public LambdaSubscriber(Consumer<? super T> dataConsumer, Consumer<? super Throwable> errorConsumer, Runnable completeConsumer, Consumer<? super Subscription> subscriptionConsumer) {
        this.dataConsumer = dataConsumer;
        this.errorConsumer = errorConsumer;
        this.completeConsumer = completeConsumer;
        this.subscriptionConsumer = subscriptionConsumer;
    }

    @Override
    public void onSubscribe(Subscription s) {
        if (subscriptionConsumer != null) {
            subscriptionConsumer.accept(s);
        } else {
            s.request(Long.MAX_VALUE);
        }
    }

    @Override
    public void onNext(T t) {
        if (dataConsumer != null) {
            dataConsumer.accept(t);
        }
    }

    @Override
    public void onError(Throwable t) {
        if (errorConsumer != null) {
            errorConsumer.accept(t);
        }
    }

    @Override
    public void onComplete() {
        if (completeConsumer != null) {
            completeConsumer.run();
        }
    }
}
