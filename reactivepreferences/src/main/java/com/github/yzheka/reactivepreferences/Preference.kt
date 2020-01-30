package com.github.yzheka.reactivepreferences

interface Preference<T>{
    var value:T
    val exists:Boolean
    val key:String
    fun delete()

    fun addOnChangeListener(listener: OnPreferenceChangeListener<T>)
    fun removeOnChangeListener(listener: OnPreferenceChangeListener<T>)
}