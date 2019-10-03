# Kotlin HTTP Server




[![](https://jitpack.io/v/ExplodingDragon/MiniHttpServer.svg)](https://jitpack.io/#ExplodingDragon/MiniHttpServer)
![Java Version 1.8](https://img.shields.io/badge/JAVA-1.8+-red.svg)    ![HTTP 1.1](https://img.shields.io/badge/HTTP-1.1-pink.svg)     ![MIT](https://img.shields.io/badge/license-MIT-pink.svg)       ![Android API 15+](https://img.shields.io/badge/Android-15+-PINK.svg)       ![Build Fail](https://img.shields.io/badge/Build-Success-pink.svg)

> ~~一个轻量级 Java Http Server。~~

> 一个轻量级 Kotlin Http Server。


一个轻量级 HTTP 1.1 服务端，使用 Kotlin ，兼容Android。

## 选择理由

1. 代码量少，高内聚，低耦合
2. 结构清晰，预置大量扩展方法


## 依赖方法

### Maven

#### 1. 添加 JitPack 仓库到 ``` pom.xml ```

```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

#### 2. 添加依赖

> 注意：请将版本号换成实际所需的版本
 
```xml
<dependency>
    <groupId>com.github.ExplodingDragon.MiniHttpServer</groupId>
    <artifactId>core</artifactId>
    <version>v0.1-beta2</version>
</dependency>
```

### Gradle

#### 1. 添加 JitPack 仓库到 ```build.gradle```

```groovy
allprojects {
	repositories {
	
	
		maven { url 'https://jitpack.io' }
	}
}
```

#### 2. 添加依赖

> 注意：请将版本号换成实际所需的版本 

```groovy
dependencies {
        implementation 'com.github.ExplodingDragon.MiniHttpServer:core:v0.1-beta2'
}
```



EOF
----

```
       ____  __________________     _____
      / / / / /_  __/_  __/ __ \   / ___/___  ______   _____  _____
 __  / / /_/ / / /   / / / /_/ /   \__ \/ _ \/ ___/ | / / _ \/ ___/
/ /_/ / __  / / /   / / / ____/   ___/ /  __/ /   | |/ /  __/ /
\____/_/ /_/ /_/   /_/ /_/       /____/\___/_/    |___/\___/_/

     [Star] https://github.com/ExplodingDragon/MiniHttpServer [Star]

[Issues] https://github.com/ExplodingDragon/MiniHttpServer/issues [Issues]

```




