package br.com.felipeacerbi.biy.utils;

public interface RequestCallback<T> {
    void onSuccess(T object);
    void onError(String error);
}
