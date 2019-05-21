package com.weibo.api.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class FluxArray<T> extends Flux<T> {
    private T[] array;

    public FluxArray(T[] data) {
        this.array = data;
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onSubscribe(new ArraySubscription<>(subscriber, array));
    }

    static class ArraySubscription<T> implements Subscription {
        final Subscriber<? super T> subscriber;
        final T[] array;
        int index;
        boolean canceled;

        public ArraySubscription(Subscriber<? super T> subscriber, T[] array) {
            this.subscriber = subscriber;
            this.array = array;
        }

        @Override
        public void request(long n) {
            if (canceled) {
                return;
            }
            long length = array.length;
            for (int i = 0; i < n && index < length; i++) {
                subscriber.onNext(array[index++]);
            }
            if (index == length) {
                subscriber.onComplete();
            }
        }

        @Override
        public void cancel() {
            canceled = true;
        }
    }
}
