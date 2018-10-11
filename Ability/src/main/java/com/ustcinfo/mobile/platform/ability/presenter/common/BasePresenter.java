package com.ustcinfo.mobile.platform.ability.presenter.common;



import com.ustcinfo.mobile.platform.ability.retrofit.ApiStores;
import com.ustcinfo.mobile.platform.ability.retrofit.AppClient;
import com.ustcinfo.mobile.platform.ability.view.common.BaseView;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lxq on 2017/10/12
 */
public class BasePresenter<V extends BaseView> implements Presenter<V> {
    public WeakReference<V> mvpView;
    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
   public ApiStores apiStoresString = AppClient.retrofit().create(ApiStores.class);
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void attachView(V mvpView) {
        this.mvpView = new WeakReference<V>(mvpView);
    }

    public V getView() {
        return mvpView.get();
    }


    @Override
    public void detachView() {
        this.mvpView = null;
        onUnSubscribe();
    }

    //RxJava取消注册，以避免内存泄露
    public void onUnSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }


    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }


    //RxJava取消之前注册
    public void onUnSubscribePrevious() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = new CompositeSubscription();
        }
    }
}
