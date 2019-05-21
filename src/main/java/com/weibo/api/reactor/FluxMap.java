package com.weibo.api.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;

public class FluxMap<T, R> extends Flux<R> {
    private final Flux<? extends T> source;
    private final Function<? super T, ? extends R> mapper;

    public FluxMap(Flux<T> source, Function<? super T, ? extends R> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Subscriber<? super R> subscriber) {
        source.subscribe(new MapSubscriber<>(subscriber, mapper));
    }

    static final class MapSubscriber<T, R> implements Subscription, Subscriber<T> {
        final Subscriber<? super R> subscriber;
        final Function<? super T, ? extends R> mapper;
        boolean done;
        Subscription subscription;

        MapSubscriber(Subscriber<? super R> subscriber, Function<? super T, ? extends R> mapper) {
            this.subscriber = subscriber;
            this.mapper = mapper;
        }

        @Override
        public void request(long n) {
            subscription.request(n);
        }

        @Override
        public void cancel() {
            subscription.cancel();
        }

        @Override
        public void onSubscribe(Subscription s) {
            this.subscription = s;
            subscriber.onSubscribe(this);
        }

        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
            subscriber.onNext(mapper.apply(t));
        }

        @Override
        public void onError(Throwable t) {
            if (done) {
                return;
            }
            done = true;
            subscriber.onError(t);
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            done = true;
            subscriber.onComplete();
        }
    }
}
