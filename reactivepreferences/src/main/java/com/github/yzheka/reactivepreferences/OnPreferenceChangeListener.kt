package com.github.yzheka.reactivepreferences

interface OnPreferenceChangeListener<T>{
    fun onPreferenceChange(preference: Preference<T>)
}