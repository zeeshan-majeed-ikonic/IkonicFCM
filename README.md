# IKONIC FCM

Simple and efficient library supporting Firebase Cloud Messaging Services for Android.

---

## **Get Started**

### Step 01:

> Add Maven dependencies:

#### Groovy-DSL

```kotlin 
    repositories {
	mavenCentral()
	maven { url 'https://jitpack.io' }
     }
```
#### Kotlin-DSL

```kotlin 
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
```
### Step 03:

> Add Add gradle dependencies:

#### Firebase Cloud Messaging Service

```kotlin 
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-messaging")
```

#### IKONIC FCM Helper
Version: [![](https://jitpack.io/v/zeeshan-majeed-ikonic/IkonicFCM.svg)](https://jitpack.io/#zeeshan-majeed-ikonic/IkonicFCM)

```kotlin 
    implementation ("com.github.zeeshan-majeed-ikonic:IkonicFCM:version")
```
### Step 03:

## Initialize IkHelperFCM

```kotlin 
    GlobalScope.launch {
        IkHelperFCM.startFCMService(this,topic)
    }
```
### Step 04:

## Add necessary permissions
```kotlin 
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

### Step 05:
> If you want to stop FCM service
```kotlin 
    IkHelperFCM.stopFCMService(topic)
```
#### That's it 
