package com.github.yzheka.reactivepreferences.extensions

import androidx.lifecycle.LiveData
import com.github.yzheka.reactivepreferences.OnPreferenceChangeListener
import com.github.yzheka.reactivepreferences.Preference
import com.github.yzheka.reactivepreferences.RxPreferences
import io.reactivex.Observable

fun <T> Preference<T>.toLiveData():LiveData<Preference<T>> = object : LiveData<Preference<T>>(),OnPreferenceChangeListener<T> {
    override fun onActive() {
        value=this@toLiveData
        addOnChangeListener(this)
    }

    override fun onInactive() {
        removeOnChangeListener(this)
    }

    override fun onPreferenceChange(preference: Preference<T>) {
        postValue(preference)
    }
}

fun <T> Preference<T>.toObservable():Observable<Preference<T>> = Observable.create { emitter ->
    emitter.onNext(this)
    val listener=object : OnPreferenceChangeListener<T>{
        override fun onPreferenceChange(preference: Preference<T>) = emitter.onNext(preference)
    }
    addOnChangeListener(listener)
    emitter.setCancellable { removeOnChangeListener(listener) }
}