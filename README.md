# Android Editor
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#ICE_CREAM_SANDWICH"><img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat" alt="API" /></a>

## Overview
Editor library for android.

## Installation

### Gradle
1. Add repository url to build.gradle(Project)
```groovy
allprojects {
    repositories {
        ...
        maven {
            ...
            url 'https://www.myget.org/F/tkpphr-android-feed/maven/'
        }
    }
}
```

2. Add dependency to build.gradle(Module)
```groovy
dependencies {
    ...
    implementation 'com.tkpphr.android:editor:1.0.0'
    implementation 'com.android.support:design:27.1.1'
}
```

or

### Maven
```xml
...
<repositories>
  ...
  <repository>
    <id>tkpphr-android-feed</id>
    <url>https://www.myget.org/F/tkpphr-android-feed/maven/</url>
  </repository>
</repositories>
...
<dependencies>
  ...
  <dependency>
    <groupId>com.tkpphr.android</groupId>
    <artifactId>editor</artifactId>
    <version>1.0.0</version>
    <type>aar</type>
  </dependency>
  <dependency>
    <groupId>com.android.support</groupId>
    <artifactId>design</artifactId>
    <version>27.1.1</version>
    <type>aar</type>
  </dependency>
</dependencies>
...
```

## Usage
1. Create the class that inherited "com.tkpphr.android.editor.util.FileData" class.
2. Create the class that inherited "com.tkpphr.android.editor.util.Editor" class.
3. Use it.

See details at a demo project.

## License
Released under the Apache 2.0 License.
See LICENSE File.