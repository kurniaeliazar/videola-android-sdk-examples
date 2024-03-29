Videola.io SDK Kotlin Example
=====

[![](https://jitpack.io/v/org.bitbucket.gruveo/videola-sdk-android.svg)](https://jitpack.io/#org.bitbucket.gruveo/videola-sdk-android)

With the Videola.io SDK, you can add video and voice calling support to your Android app, quickly. The SDK provides a ready-to-use, white-label screen with a video and voice calling interface that you can use right away.

Setup
-----
Include JitPack by adding the following in your project build.gradle:

```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

Add Videola.io SDK in your module build.gradle dependencies:

```
compile 'org.bitbucket.gruveo:videola-sdk-android:{latest version}'
```

Add the following activity in your manifest file:

```
<activity
    android:name="io.videola.sdk.ui.CallActivity"
    android:configChanges="orientation|screenSize"/>
```

Usage
-----
The snippet below showcases launching a Videola.io call screen. Make sure to replace `YOUR_CLIENT_ID_HERE` and `YOUR_SIGNER_URL_HERE` with your client ID and signer URL, respectively. See [SDK Authentication](https://videola.io/docs/android-sdk/authentication/) for details.

```
private val SIGNER_URL = "YOUR_SIGNER_URL_HERE"

override fun onCreate(savedInstanceState: Bundle?) {
    ...    
    Videola.Builder(this)
        .callCode("videolaiorocks")
        .clientId("YOUR_CLIENT_ID_HERE")
        .eventsListener(eventsListener)
        .build()
}
    
private val eventsListener = object : Videola.EventsListener {
    override fun requestToSignApiAuthToken(token: String) {
        Videola.authorize(signToken(token))
    }
    ...
}

private fun signToken(token: String): String {
    val body = RequestBody.create(MediaType.parse("text/plain"), token)
    val request = Request.Builder()
            .url(SIGNER_URL)
            .post(body)
            .build()

    val response = OkHttpClient().newCall(request).execute()
    return response.body()?.string() ?: ""
}
    
```

For more advanced configuration and options, check the <a href="https://github.com/Gruveo/videola-android-sdk-examples/blob/master/app/src/main/kotlin/videola/sample/MainActivity.kt">sample file</a> and the [SDK documentation](https://videola.io/docs/android-sdk/).

To request production API credentials, <a href="https://videola.io/signup/">get in touch</a>.

Requirements
------------
At least Android 4.2.x (API 17).

The Manifest.permission.RECORD_AUDIO permission is mandatory for making calls, Manifest.permission.CAMERA is needed only for video calls. However, it is all handled by the SDK, so you do not have to bother with it.

The SDK manifest file contains the following features and permisions, so you <b>do not</b> have to add them in your project.

```
<uses-feature
    android:name="android.hardware.camera"
    android:required="false"/>
<uses-feature
    android:name="android.hardware.camera.autofocus"
    android:required="false"/>
<uses-feature
    android:glEsVersion="0x00020000"
    android:required="true"/>

<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.WAKE_LOCK"/>
<uses-permission android:name="android.permission.VIBRATE"/>
<uses-permission android:name="android.permission.BLUETOOTH"/>
```

Proguard
--------
The SDK is minified with self-contained proguard rules, so you do not have to add any.

License
-------
```
MIT License

Copyright (c) 2017 Gruveo, s.r.o.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
