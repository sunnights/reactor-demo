package com.weibo.api.reactor;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Flux<T> implements Publisher<T> {
    public static <T> Flux<T> just(T... data) {
        return new FluxArray<>(data);
    }

    public void subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> errorConsumer, Runnable completeConsumer, Consumer<? super Subscription> subscriptionConsumer) {
        subscribe(new LambdaSubscriber<>(consumer, errorConsumer, completeConsumer, subscriptionConsumer));
    }

    public void subscribe(Consumer<? super T> consumer) {
        subscribe(consumer, null, null, null);
    }

    public <V> Flux<V> map(Function<? super T, ? extends V> mapper) {
        return new FluxMap<>(this, mapper);
    }
}
