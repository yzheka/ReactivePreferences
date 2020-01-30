# ReactivePreferences

* Create preferences:
```
class SomePreferences(...):RxPreferences(...) {
    val someInt by intPreference()
    val someString by stringPreference()
    val someEnum by serializablePreference<Test?>(null,null)
    val someSerializable by serializablePreference<Data?>(null,null)
    ...
}
```
* Use preferences
```
val preferences=SomePreferences(...)
preferences.someInt.value=0 // write value
val value=preferences.someInt.value // reat value
preferences.someInt.addOnChangeListener(...) // observe changes

//Helper extensions
val liveData = preferences.someInt.toLiveData()
va lobservable = preferences.someInt.toObservable()
```
Supported data types:
* int
* long
* float
* boolean
* string
* stringSet
* serializable
