# Wettervorhersage_JavaVersion

# WeatherApp - 获取世界各地一周天气

**WeatherApp** 是一个用 Java 编写的应用程序，通过 OpenWeatherMap API 获取并显示全球天气预报。用户可以输入城市名，程序会返回该城市的当前天气信息，包括城市、国家、时间和温度等。

## 功能
- 用户可以输入任意城市名称。
- 程序通过 OpenWeatherMap API 获取天气数据。
- 输出包括城市、国家、时间和温度。

## 依赖
- **OkHttp**：用于发送 HTTP 请求并获取天气数据。
- **Gson**：用于解析 JSON 格式的数据。

## 如何运行

### 步骤 1: 获取 API 密钥

1. 前往 [OpenWeatherMap 网站](https://openweathermap.org/)。
2. 注册并获得一个 API 密钥。你需要这个密钥来访问天气数据。

### 步骤 2: 配置 `config.properties` 文件

在程序的根目录下创建一个名为 `config.properties` 的文件，内容如下：

```
API_URL=https://api.openweathermap.org/data/2.5/forecast
API_KEY=你的API密钥
```

请将 `API_KEY` 替换为你在 OpenWeatherMap 上获取的密钥。

### 步骤 3: 编译和运行程序

1. 确保你的机器上安装了 Java 开发环境（JDK）。
2. 下载并安装 **OkHttp** 和 **Gson** 库。可以通过 Maven 或 Gradle 来管理依赖，或者手动下载 JAR 文件。
3. 编译并运行 `WeatherApp.java` 文件。

### 示例命令（终端）：

假设你已经在项目根目录中：

```bash
javac WeatherApp.java
java WeatherApp
```

### 步骤 4: 输入城市名

运行程序后，终端会提示你输入城市名。输入一个城市名，程序将查询该城市的天气数据并显示结果。

### 示例：

```
Enter city name: London
Request URL: https://api.openweathermap.org/data/2.5/forecast?q=London&appid=your-api-key&units=metric
City: London, Country: GB
Time: 2024-12-09 15:00:00, Temperature: 8.5°C
Time: 2024-12-09 18:00:00, Temperature: 7.8°C
Time: 2024-12-09 21:00:00, Temperature: 7.1°C
...
```

## 代码解释

### `WeatherApp.java` 文件

```java
import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.Scanner;
```

- `import` 语句导入了所需的类和库。
    - `okhttp3`：用于发送 HTTP 请求并获取天气数据。
    - `com.google.gson`：用于解析 JSON 数据。
    - `java.io.IOException`：用于处理可能的输入输出错误。
    - `java.util.Scanner`：用于读取用户输入的城市名。

```java
public class WeatherApp {
```

- 这行代码定义了 `WeatherApp` 类，程序的主体部分。

```java
    private static String API_URL;
    private static String API_KEY;
```

- 定义了两个静态变量 `API_URL` 和 `API_KEY`，用于存储 OpenWeatherMap API 的地址和密钥。

```java
    public static void main(String[] args) {
```

- `main` 方法是程序的入口点，程序从这里开始执行。

```java
        try {
            ConfigReader configReader = new ConfigReader("config.properties");
            API_URL = configReader.getAPIUrl();
            API_KEY = configReader.getAPIKey();
```

- 通过 `ConfigReader` 类读取 `config.properties` 文件，获取 API 地址和密钥。

```java
            System.out.println("API_URL: " + API_URL);
            System.out.println("API_KEY: " + API_KEY);
```

- 打印 `API_URL` 和 `API_KEY`，以确认读取的配置信息是否正确。

```java
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter city name: ");
            String city = scanner.nextLine();
```

- 使用 `Scanner` 从终端读取用户输入的城市名称。

```java
            fetchWeather(city);
```

- 调用 `fetchWeather` 方法，传入用户输入的城市名称，获取该城市的天气数据。

```java
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

- 捕获并处理可能的输入输出异常，如果读取配置文件或其他操作出现错误，会打印错误信息。

```java
    public static void fetchWeather(String city) {
```

- `fetchWeather` 方法用于发送 HTTP 请求并获取天气数据。

```java
        OkHttpClient client = new OkHttpClient();
```

- 创建一个 `OkHttpClient` 对象，它用于发送 HTTP 请求。

```java
        String url = String.format("%s?q=%s&appid=%s&units=metric", API_URL, city, API_KEY);
```

- 使用 `String.format` 构建请求 URL，`%s` 会被替换为 API 地址、城市名称和 API 密钥。

```java
        System.out.println("Request URL: " + url);
```

- 打印生成的请求 URL，检查它是否正确。

```java
        Request request = new Request.Builder().url(url).build();
```

- 创建一个 `Request` 对象，设置请求的 URL。

```java
        client.newCall(request).enqueue(new Callback() {
```

- 使用 `OkHttpClient` 对象发送异步请求，`enqueue` 方法会异步执行 HTTP 请求。

```java
            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println("Request failed: " + e.getMessage());
            }
```

- 如果请求失败，打印错误信息。

```java
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    parseWeatherData(jsonResponse);
                } else {
                    System.err.println("Request failed, response code: " + response.code());
                }
            }
```

- 如果请求成功，获取响应的内容并将其传递给 `parseWeatherData` 方法解析；否则打印失败的响应码。

```java
    public static void parseWeatherData(String jsonResponse) {
```

- `parseWeatherData` 方法用于解析天气数据。

```java
        Gson gson = new Gson();
        WeatherResponse weatherResponse = gson.fromJson(jsonResponse, WeatherResponse.class);
```

- 使用 `Gson` 库将 JSON 格式的天气数据转换为 `WeatherResponse` 对象。

```java
        String city = weatherResponse.city.name;
        String country = weatherResponse.city.country;
```

- 从解析后的 `WeatherResponse` 对象中提取城市和国家信息。

```java
        System.out.println("City: " + city + ", Country: " + country);
```

- 打印城市和国家信息。

```java
        for (WeatherResponse.ListItem item : weatherResponse.list) {
            String dateTime = item.dt_txt;
            double temp = item.main.temp;
            System.out.println("Time: " + dateTime + ", Temperature: " + temp + "°C");
        }
```

- 遍历天气列表，提取每个时间点的天气信息（时间和温度）并打印。

```java
    static class WeatherResponse {
        City city;
        List<ListItem> list;
    }
```

- `WeatherResponse` 类用于存储返回的天气数据，包括城市信息和天气列表。

```java
        static class City {
            String name;
            String country;
        }
```

- `City` 类存储城市的名称和国家。

```java
        static class ListItem {
            String dt_txt;
            Main main;
        }
```

- `ListItem` 类存储每个天气数据项，包括时间和温度。

```java
        static class Main {
            double temp;
        }
    }
}
```

- `Main` 类存储温度信息。

---

## 开源许可证

此项目遵循 MIT 许可证。你可以自由地使用、修改和分发此项目，但请保留原始版权和许可证声明。
