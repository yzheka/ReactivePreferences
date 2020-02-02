# ReactivePreferences

[![](https://jitpack.io/v/yzheka/ReactivePreferences.svg)](https://jitpack.io/#yzheka/ReactivePreferences)

* Create preferences:
```
class SomePreferences(...):RxPreferences(...) {
    val someInt by intPreference()
    val someString by stringPreference()
    val someEnum by serializablePreference<Test>(null,Test.TEST1)
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

To get a Git project into your build:

**Step 1.** Add the JitPack repository to your build file
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
**Step 2.** Add the dependency
```
dependencies {
	...
	implementation "com.github.yzheka.ReactivePreferences:reactivepreferences:$latest_version"
	implementation "com.github.yzheka.ReactivePreferences:reactivepreferences-extensions:$latest_version" //for Rxjava2 and livedata support
}
```
