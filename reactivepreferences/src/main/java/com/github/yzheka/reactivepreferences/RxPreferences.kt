package com.github.yzheka.reactivepreferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.annotation.StringRes
import androidx.preference.PreferenceManager
import java.io.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class RxPreferences(private val context: Context,private val preferences: SharedPreferences) {
    constructor(context: Context,name:String,mode:Int=Context.MODE_PRIVATE):this(context,context.getSharedPreferences(name, mode))
    constructor(context: Context):this(context,PreferenceManager.getDefaultSharedPreferences(context))


    fun clear() = preferences.edit().clear().apply()

    protected fun intPreference(key: String?=null,defValue:Int=0) = preferenceProperty(key) {
        object : BasePreference<Int>(it){
            override var value: Int
                get() = preferences.getInt(this.key, defValue)
                set(value) = preferences.edit().putInt(this.key,value).apply()
        }
    }

    protected fun longPreference(key: String?=null,defValue: Long=0) = preferenceProperty(key) {
        object : BasePreference<Long>(it){
            override var value: Long
                get() = preferences.getLong(this.key,defValue)
                set(value) = preferences.edit().putLong(this.key,value).apply()
        }
    }

    protected fun floatPreference(key: String?=null,defValue: Float=1f) = preferenceProperty(key){
        object : BasePreference<Float>(it){
            override var value: Float
                get() = preferences.getFloat(this.key,defValue)
                set(value) = preferences.edit().putFloat(this.key,value).apply()
        }
    }

    protected fun booleanPreference(key: String?=null,defValue: Boolean=false) = preferenceProperty(key){
        object : BasePreference<Boolean>(it){
            override var value: Boolean
                get() = preferences.getBoolean(this.key,defValue)
                set(value) = preferences.edit().putBoolean(this.key,value).apply()
        }
    }

    protected fun stringPreference(key: String?=null,defValue:String="") = preferenceProperty(key){
        object : BasePreference<String>(it){
            override var value: String
                get() = preferences.getString(this.key,defValue)?:defValue
                set(value) = preferences.edit().putString(this.key,value).apply()
        }
    }

    protected fun stringSetPreference(key: String?=null,defValue:Set<String> = emptySet()) = preferenceProperty(key){
        object : BasePreference<Set<String>>(it){
            override var value: Set<String>
                get() = preferences.getStringSet(this.key,defValue)?:defValue
                set(value) = preferences.edit().putStringSet(this.key,value).apply()
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T:Serializable?> serializablePreference(key: String?=null, defValue: T) = preferenceProperty(key){
        object : BasePreference<T>(it){
            override var value: T
                get(){
                    val str=preferences.getString(this.key,null)?:return defValue
                    val bytes=Base64.decode(str,Base64.DEFAULT)
                    return ObjectInputStream(ByteArrayInputStream(bytes)).use { it.readObject() as T }
                }
                set(value) {
                    val editor=preferences.edit()
                    if(value==null){
                        editor.remove(this.key).apply()
                        return
                    }
                    val bytes=ByteArrayOutputStream().use {
                        val output=ObjectOutputStream(it)
                        output.writeObject(value)
                        output.flush()
                        it.toByteArray()
                    }
                    val str=Base64.encodeToString(bytes,Base64.DEFAULT)
                    editor.putString(this.key,str).apply()
                }
        }
    }

    protected fun intPreference(@StringRes key: Int,defValue:Int=0) = intPreference(context.getString(key),defValue)
    protected fun longPreference(@StringRes key: Int,defValue:Long=0) = longPreference(context.getString(key),defValue)
    protected fun floatPreference(@StringRes key: Int,defValue:Float=0f) = floatPreference(context.getString(key),defValue)
    protected fun booleanPreference(@StringRes key: Int,defValue:Boolean=false) = booleanPreference(context.getString(key),defValue)
    protected fun stringPreference(@StringRes key: Int,defValue:String="") = stringPreference(context.getString(key),defValue)
    protected fun stringSetPreference(@StringRes key: Int,defValue:Set<String> = emptySet()) = stringSetPreference(context.getString(key),defValue)
    protected fun <T:Serializable?> serializablePreference(@StringRes key: Int,defValue:T) = serializablePreference(context.getString(key),defValue)

    private object LOCK

    protected fun <T> preferenceProperty(key:String?,initializer:(key:String)->Preference<T>):ReadOnlyProperty<RxPreferences,Preference<T>> = object : ReadOnlyProperty<RxPreferences,Preference<T>>{
        @Volatile private var mInstance:Preference<T>?=null
        override fun getValue(thisRef: RxPreferences, property: KProperty<*>): Preference<T> = mInstance?: synchronized(LOCK){
            mInstance?:initializer(key?:property.name).also { mInstance=it }
        }
    }

    protected abstract inner class BasePreference<T>(override val key:String):Preference<T>,SharedPreferences.OnSharedPreferenceChangeListener{
        override val exists: Boolean get() = preferences.contains(key)
        override fun delete() = preferences.edit().remove(key).apply()
        private val listeners=LinkedHashSet<OnPreferenceChangeListener<T>>()

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?,key: String?) {
            if(this.key==key)listeners.forEach { it.onPreferenceChange(this) }
        }

        override fun addOnChangeListener(listener: OnPreferenceChangeListener<T>) {
            listeners.add(listener)
            if(listeners.isNotEmpty())
                preferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun removeOnChangeListener(listener: OnPreferenceChangeListener<T>) {
            listeners.remove(listener)
            if(listeners.isEmpty())
                preferences.unregisterOnSharedPreferenceChangeListener(this)
        }
    }
}