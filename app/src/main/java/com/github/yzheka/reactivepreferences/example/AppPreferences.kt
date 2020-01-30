package com.github.yzheka.reactivepreferences.example

import android.content.Context
import com.github.yzheka.reactivepreferences.RxPreferences
import java.io.Serializable

class AppPreferences(context: Context):RxPreferences(context) {
    val someInt by intPreference()
    val someString by stringPreference()
    val someEnum by serializablePreference<Test?>(null,null)
    val someSerializable by serializablePreference<Data?>(null,null)

    enum class Test{
        TEST1,TEST2,TEST3
    }

    data class Data(
        val someInt:Int,
        val someString:String
    ):Serializable
}