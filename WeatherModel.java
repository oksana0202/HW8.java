package HW7;

import java.io.IOException;

public interface WeatherModel {
    String getWeather(String selectedCity, Period period) throws IOException;

    List<Weather> getSavedToDBWeather();
}
