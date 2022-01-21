package HW7;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static java.lang.System.*;

public class AccuweatherModel implements WeatherModel {
    //http://dataservice.accuweather.com/forecasts/v1/daily/1day
    //http://dataservice.accuweather.com/forecasts/v1/daily/5day

    public static final String PROTOCOL = "http";
    public static final String BASE_HOST = "dataservice.accuweather.com";
    public static final String FORECASTS = "forecasts";
    private static final String VERSION = "V1";
    public static final String DAILY = "daily";
    public static final String ONE_DAY = "1day";
    public static final String FIVE_DAYS = "5days";
    public static final String API_KEY = "RZn4AO9JYqiARlMgGgxqYiMn3B0A8TaQ";
    private static final String API_KEY2 = "mujc4PdPNhPYJK3pveSNxDAWUm9kJwsb";
    public static final String API_KEY_QUERY_PARAM = "apikey";
    private static final String LOCATIONS = "locations";
    private static final String CITIES = "cities";
    private static final String AUTOCOMPLETE = "autocomplete";

    public static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getWeather(String selectedCity, Period period) throws IOException {
        String weatherResponse = null;
        switch(period){
            case ONE_DAY:
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme(PROTOCOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(ONE_DAY)
                        .addPathSegment(detectCityKey(selectedCity, Period.ONE_DAY))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build();

                Request request = new Request.Builder()
                        .url(httpUrl)
                        .build();

                Response oneDayForecastsResponse = okHttpClient.newCall(request).execute();
                weatherResponse = oneDayForecastsResponse.body().string();
                return weatherResponse;

            case FIVE_DAYS:
                httpUrl = new HttpUrl.Builder()
                        .scheme(PROTOCOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(FIVE_DAYS)
                        .addPathSegment(detectCityKey(selectedCity, Period.ONE_DAY))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build();
                request = new Request.Builder()
                        .url(httpUrl)
                        .build();

                Response fiveDaysForecastsResponse = okHttpClient.newCall(request).execute();
                weatherResponse = fiveDaysForecastsResponse.body().string();

                System.out.println(weatherResponse);
                String token = String.valueOf(weatherResponse.split("[|\"}]", Integer.parseInt(" ")));
                System.out.println(token);

                String getWeatherONE_DAY = "{\n" +
                        "    \"Version\" : \"1\",\n" +
                        "    \"Key\" : \"294021\",\n" +
                        "    \"Type\" : \"City\",\n"  +
                        "    \"Rank\" : \"10\",\n" +
                        "    \"LocalizedName\" : \"Moscow\",\n" +
                        "    \"Country\" : \"Russia\",\n" +
                        "    \"ID\" : \"RU\",\n" +
                        "}";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + period);
        }
        return weatherResponse;
    }

    public String detectCityKey(String selectedCity, Period oneDay) throws IOException {
        //http://dataservice.accuweather.com/locations/v1/cities/autocomplete
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PROTOCOL)
                .host(BASE_HOST)
                .addPathSegment(LOCATIONS)
                .addPathSegment(VERSION)
                .addPathSegment(CITIES)
                .addPathSegment(AUTOCOMPLETE)
                .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                .addQueryParameter("q", selectedCity)
                        .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("accept", "application/json")
                 .build();

        Response response  = okHttpClient.newCall(request).execute();
        String responseCity = response.body().string();

        ObjectMapper objectMapper = new ObjectMapper();
        String cityKey = objectMapper.readTree(responseCity).get(0).at("/Key").asText();
        return responseCity;
    }

}
