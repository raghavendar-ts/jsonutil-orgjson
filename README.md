# JSONUtil for org.json

[![Maven Central](https://img.shields.io/maven-central/v/io.github.raghavendar-ts/jsonutil-orgjson.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.raghavendar-ts/jsonutil-orgjson)
[![javadoc](https://javadoc.io/badge2/io.github.raghavendar-ts/jsonutil-orgjson/javadoc.svg)](https://javadoc.io/doc/io.github.raghavendar-ts/jsonutil-orgjson)

A lightweight helper/wrapper library for [`org.json`](https://stleary.github.io/JSON-java/)  
to safely extract values from `JSONObject` and `JSONArray` without repetitive null checks.

---

## Installation

Add the dependency to your project:

**Maven**
```xml
<dependency>
  <groupId>io.github.raghavendar-ts</groupId>
  <artifactId>jsonutil-orgjson</artifactId>
  <version>1.0.0</version>
</dependency>
```
## Gradle
```
implementation 'io.github.raghavendar-ts:jsonutil-orgjson:1.0.0'
```

## Gradle (Kotlin DSL)
```
implementation("io.github.raghavendar-ts:jsonutil-orgjson:1.0.0")
```

## Usage
```java
import org.json.JSONObject;
import io.github.raghavendarts.jsonutil.JSONUtil;

public class Example {
    public static void main(String[] args) {
        JSONObject obj = new JSONObject();
        obj.put("id", 101);
        obj.put("name", "Raghavendar");
        obj.put("active", true);

        // Safe extraction
        Integer id = JSONUtil.getInteger(obj, "id");        // 101
        String name = JSONUtil.getString(obj, "name");      // "Raghavendar"
        Boolean active = JSONUtil.getBoolean(obj, "active");// true
        String email = JSONUtil.getString(obj, "email");    // "" (default safe value)

        System.out.println(id + ", " + name + ", " + active + ", " + email);
    }
}
```

## Features
- Avoids repetitive null and JSONException checks.
- Provides safe defaults ("", 0, false, empty JSONObject/JSONArray).
- Works as a wrapper on top of the popular org.json library.

## License

This project is licensed under the MIT License — see the LICENSE
 file for details.

## Author
Raghavendar T S

GitHub: [`@raghavendar-ts`](https://stleary.github.io/JSON-java/)

LinkedIn: [`Raghavendar T S`](https://www.linkedin.com/in/raghavendar-ts/)
