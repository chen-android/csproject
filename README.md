# 概要点
------
### 1.初始化
主工程**application()**中，先运行**CsUtils.init()**

### 2.数据存储
CsDbUtils 中，使用的是默认**AppPreferences()**，若有需要增加更多不同数据组，需自行增加。
别忘记主工程的**build.gradle**中增加
```java
defaultConfig {
    ...
    resValue "string", "tray__authority", "${applicationId}.tray"
}
```

