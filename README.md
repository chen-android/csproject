# 概要点
------
### 1.数据存储
CsDbUtils 中，使用的是默认**AppPreferences()**，若有需要增加更多不同数据组，需自行增加。
别忘记主工程的**build.gradle**中增加
```tray
defaultConfig {
	...
	resValue "string", "tray__authority", "${applicationId}.tray"
	}
```

