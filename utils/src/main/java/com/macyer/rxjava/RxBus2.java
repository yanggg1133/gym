package com.macyer.rxjava;

import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by liuxiu on 16/5/17.
 */
public class RxBus2 {
    private static HashMap<String, CompositeDisposable> mSubscriptionMap;
    private static volatile RxBus2 mRxBus;
    private static Subject<Object> mSubject;

    private RxBus2() {
        mSubject = PublishSubject.create().toSerialized();
    }

    //单列模式
    public static RxBus2 getIntanceBus() {
        if (mRxBus == null) {
            synchronized (RxBus2.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus2();
                }
            }
        }
        return mRxBus;
    }

    /**
     * 提供了一个新的事件,根据code进行分发
     *
     * @param code 事件code
     * @param o
     */
    public void post(int code, Object o) {
        mSubject.onNext(new RxBusBaseMessage(code, o));

    }

    /**
     * 返回指定类型的带背压的Flowable实例
     *
     * @param <T>
     * @param type
     * @return
     */
    private <T> Flowable<T> getObservable(Class<T> type) {
        return mSubject.toFlowable(BackpressureStrategy.BUFFER)
                .ofType(type);
    }

    /**
     * 一个默认的订阅方法
     *
     * @param dClass   所在的界面的class,需要通过dClass注销消息
     * @param code     消息码，分辨是那一条消息
     * @param type     发送的消息对象
     * @param consumer 接收消息的
     * @param <T>      发送的消息对象的泛型
     * @return
     */
    public <T> void doSubscribe(Object dClass, int code, Class<T> type, Consumer<T> consumer) {
        addSubscription(dClass, getObservable(RxBusBaseMessage.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(o -> {
                    //过滤code和eventType都相同的事件
                    return o.getCode() == code && type.isInstance(o.getObject());
                })
                .map(RxBusBaseMessage::getObject)
                .cast(type)
                .subscribe(consumer));
    }

    /**
     * 是否已有观察者订阅
     *
     * @return
     */
    public boolean hasObservers() {
        return mSubject.hasObservers();
    }

    /**
     * 保存订阅后的disposable
     *
     * @param o
     * @param disposable
     */
    private void addSubscription(Object o, Disposable disposable) {
        if (mSubscriptionMap == null) {
            mSubscriptionMap = new HashMap<>();
        }
        String key = o.getClass().getName();
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).add(disposable);
        } else {
            //一次性容器,可以持有多个并提供 添加和移除。
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
            mSubscriptionMap.put(key, disposables);
        }
    }

    /**
     * 取消订阅
     *
     * @param o
     */
    public static void unSubscribe(Object o) {
        if (mSubscriptionMap == null) {
            return;
        }
        String key = o.getClass().getName();
        if (!mSubscriptionMap.containsKey(key)) {
            return;
        }
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).dispose();
        }
        mSubscriptionMap.remove(key);
    }
}
