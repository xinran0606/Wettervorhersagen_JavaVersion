import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class WeatherApp {

    private static String API_URL;
    private static String API_KEY;

    public static void main(String[] args) {
        try {
            // Read the configuration file
            ConfigReader configReader = new ConfigReader("config.properties");
            API_URL = configReader.getAPIUrl();
            API_KEY = configReader.getAPIKey();

            // Print out the configuration details to check if they are correct
            System.out.println("API_URL: " + API_URL);
            System.out.println("API_KEY: " + API_KEY);

            // Read city name from the terminal input
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter city name: ");
            String city = scanner.nextLine();  // Read the city name entered by the user

            // Fetch the weather data for the entered city
            fetchWeather(city);

        } catch (IOException e) {
            e.printStackTrace(); // If there is an error reading the config file, print the error
        }
    }

    public static void fetchWeather(String city) {
        OkHttpClient client = new OkHttpClient();

        // Construct the API request URL
        String url = String.format("%s?q=%s&appid=%s&units=metric", API_URL, city, API_KEY);

        // Print the constructed URL to ensure it's correct
        System.out.println("Request URL: " + url);

        // Create the request
        Request request = new Request.Builder().url(url).build();

        // Asynchronously execute the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // If the request fails, print the error message
                System.err.println("Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    parseWeatherData(jsonResponse); // Parse and display the weather data
                } else {
                    // If the request is successful but returns an error response code
                    System.err.println("Request failed, response code: " + response.code());
                }
            }
        });
    }

    public static void parseWeatherData(String jsonResponse) {
        // Use Gson to parse the returned JSON data
        Gson gson = new Gson();
        WeatherResponse weatherResponse = gson.fromJson(jsonResponse, WeatherResponse.class);

        // Get city information
        String city = weatherResponse.city.name;
        String country = weatherResponse.city.country;

        // Print city and country
        System.out.println("City: " + city + ", Country: " + country);

        // Get the first item in the weather list (you can loop through all data if needed)
        for (WeatherResponse.ListItem item : weatherResponse.list) {
            String dateTime = item.dt_txt; // Date and time
            double temp = item.main.temp;  // Temperature

            // Print the time and temperature
            System.out.println("Time: " + dateTime + ", Temperature: " + temp + "°C");
        }
    }

    // Data model: Build it based on the OpenWeatherMap API response structure
    static class WeatherResponse {
        City city;
        List<ListItem> list;

        static class City {
            String name;
            String country;
        }

        static class ListItem {
            String dt_txt;  // Time
            Main main;      // Temperature
        }

        static class Main {
            double temp;    // Temperature
        }
    }
}
