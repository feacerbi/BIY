package br.com.felipeacerbi.biy.repository;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.utils.RequestCallback;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class DataManager {

    @Inject
    RestDataSource restDataSource;

    @Inject
    DataManager() {}

    public void requestRecipes(RequestCallback<List<Recipe>> recipesRequest) {
        getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createObserver(recipesRequest));
    }

    private Observable<List<Recipe>> getRecipes() {
        return Observable.create(new ObservableOnSubscribe<List<Recipe>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Recipe>> subscriber) throws Exception {
                restDataSource.getRecipes(new RequestCallback<List<Recipe>>() {
                    @Override
                    public void onSuccess(List<Recipe> recipes) {
                        Collections.sort(recipes);
                        subscriber.onNext(recipes);
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(String error) {
                        subscriber.onError(new Throwable(error));
                    }
                });
            }
        });
    }

    private <T> Observer<T> createObserver(final RequestCallback<T> request) {
        return new Observer<T>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull T object) { request.onSuccess(object); }

            @Override
            public void onError(@NonNull Throwable e) { request.onError(e.getMessage()); }

            @Override
            public void onComplete() {}
        };
    }

}
