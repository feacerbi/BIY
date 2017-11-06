package br.com.felipeacerbi.biy.repository;

import android.content.Context;

import java.util.List;

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

public class DataManager {

    private JsonHelper jsonHelper;

    public DataManager(Context context) {
        jsonHelper = new JsonHelper(context);
    }

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
                List<Recipe> recipes = jsonHelper.getRecipes();

                if(recipes != null) {
                    subscriber.onNext(recipes);
                    subscriber.onComplete();
                } else {
                    subscriber.onError(new Throwable("Error reading JSON file"));
                }
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
