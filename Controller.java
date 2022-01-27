package HW7;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static HW7.AccuweatherModel.*;
import static java.lang.System.out;
import static javax.xml.transform.OutputKeys.VERSION;

    public Controller() {
        variants.put(1, Period.ONE_DAY);
        variants.put(5, Period.FIVE_DAYS);
        variants.put(2, Period.DB);
    }

    public String getWeather(String userInput, String selectedCity) throws IOException {
        Integer userIntegerInput = Integer.parseInt(userInput);

        switch (variants.get(userIntegerInput)) {
            case ONE_DAY:
                weatherModel.getWeather(selectedCity, Period.ONE_DAY);
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme(PROTOCOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(ONE_DAY)
                        .addPathSegment(detectCityKey(selectedCity, HW7.Period.ONE_DAY))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build();

                Request request = new Request.Builder()
                        .url(httpUrl)
                        .build();

                Response oneDayForecastsResponse = okHttpClient.newCall(request).execute();
                String weatherResponse = oneDayForecastsResponse.body().string();
                return weatherResponse;

            case FIVE_DAYS:
                weatherModel.getWeather(selectedCity, Period.FIVE_DAYS);
                httpUrl = new HttpUrl.Builder()
                        .scheme(PROTOCOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(FIVE_DAYS)
                        .addPathSegment(detectCityKey(selectedCity, HW7.Period.ONE_DAY))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build();
                request = new Request.Builder()
                        .url(httpUrl)
                        .build();

                Response fiveDaysForecastsResponse = okHttpClient.newCall(request).execute();
                weatherResponse = fiveDaysForecastsResponse.body().string();

                out.println(weatherResponse);
                break;

            case DB:
                weatherModel.getSavedToDBWeather();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + variants.get(userIntegerInput));
        }
        return userInput;
    }

    private String detectCityKey(String selectedCity, Period oneDay) {
        return selectedCity;
    }
}
