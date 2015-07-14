# ![Logo](https://github.com/fenli/elf/blob/master/sample/src/main/res/mipmap-mdpi/ic_launcher.png) Elf Framework
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.fenlisproject.elf/core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.fenlisproject.elf/core) 

Efl is an Android Framework to simplify the android development process. It has many commonly used features like annotation binding, fast http connection wrappers, file utils, simple data caching, etc.

## Current Features
- Annotation Based Binding ([View](https://github.com/fenli/elf/wiki/View-Binding), [Event Listener](https://github.com/fenli/elf/wiki/Event-Listener-Binding), [Intent Extra](https://github.com/fenli/elf/wiki/Intent-Extra-Binding), etc)
- [Http Request](https://github.com/fenli/elf/wiki/Http-Request) (Simple, Lightweight, Support Multipart Request Body)
- [Preferences Storage](https://github.com/fenli/elf/wiki/Preferences-Storage)
- [Simple Session Storage](https://github.com/fenli/elf/wiki/Session-Storage) (Object Caching)
- [Secure Session Storage](https://github.com/fenli/elf/wiki/Secure-Session-Storage) (Encrypted)
- [Extended Widget](https://github.com/fenli/elf/wiki/Extended-Widget) (TextView, EditText, Button, Checkbox, RadioButton)
- [Form Validation](https://github.com/fenli/elf/wiki/Form-Validation)
- [Common Utils](https://github.com/fenli/elf/wiki/Common-Utils) (MD5, File Utils, etc)

## Upcoming Features
- Bitmap Cache
- Asychronous JSON and XML Request

## How to Use
You can import this library to your project by adding following dependency to your `build.gradle` :
```gradle
repositories {
    jcenter()
}

dependencies {
    compile 'com.fenlisproject.elf:core:0.2.5'
}
```

## Basic Setup
- Extends BaseApplication

First step to use this library is to Make your Application class extend BaseApplication.
```java
public class SampleApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
```

Don't forget change application name in your manifest
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest ...>
...
    <application
        android:name="your.package.name.SampleApplication">
    </application>
</manifest>
```

- Extends BaseActivity

In you want to utilize Binding feature, you must extends your Activity with BaseActivity.
BaseActivity itself extends from AppCompatActivity which use Android Support Fragment (appcompatv7).
Define content view by `@ContentView` annotation. It will call `setContentView` for you at Runtime.
Method `onContentViewCreated` is called right before `onCreate` finish. You you can treat this method same as `onCreate` method. Do what you usually do in `onCreate` in this method.

```java
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewId(R.id.textview1)
    private TextView textView1;

    @Override
    protected void onContentViewCreated() {
        // Yo can safely access to textView1 in this method without call findViewById(). That's the magic
        textView1.setText("Hello Elf");
    }
}
```

- Extends BaseFragment

If you use Fragment, then you can extend your fragment with BaseFragment.
And the other steps are same as when you use Activity.

```java
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    @Override
    protected void onContentViewCreated() {
        // Treat this method as onCreateView() without inflate the layout
    }
    
    @OnClick(R.id.button1)
    public void onButton1Clicked() {
        // You can even bind button click listener without store it as variable.
    }
}
```

## Documentation
For more detailed documentation, please visit [Wiki Page](https://github.com/fenli/elf/wiki).

## Release Notes
Please refer to [Release Notes](https://github.com/fenli/elf/blob/master/ReleaseNotes.md) to see what's recently changed.

## License

    Copyright 2015 Steven Lewi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
