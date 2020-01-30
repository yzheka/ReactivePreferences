package com.github.yzheka.reactivepreferences.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.github.yzheka.reactivepreferences.extensions.toLiveData
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences=AppPreferences(this)
        setContentView(R.layout.activity_main)
        preferences.someInt.toLiveData().observe(this, Observer { intValue.text="Int: ${it.value}" })
        preferences.someString.toLiveData().observe(this, Observer { stringValue.text="String: ${it.value}" })
        preferences.someEnum.toLiveData().observe(this, Observer { enumValue.text="Enum: ${it.value}" })
        preferences.someSerializable.toLiveData().observe(this, Observer { objectValue.text="Object: ${it.value}" })
        changeInt.setOnClickListener { preferences.someInt.value=Random.nextInt() }
        changeString.setOnClickListener { preferences.someString.value=UUID.randomUUID().toString() }
        changeEnum.setOnClickListener { preferences.someEnum.value= AppPreferences.Test.values().random() }
        changeOnject.setOnClickListener { preferences.someSerializable.value=AppPreferences.Data(Random.nextInt(),UUID.randomUUID().toString()) }
    }
}
