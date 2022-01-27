public class DataBaseRepository {
    private static final String DB_PATH = "jdbc:sqlite:geekbrains.db";
    private String getWeather = "select * from weather * city";
    private String insertWeather = "insert into weather (city, localdate, temperature) values (? ? ?)";
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean saveWeatherToDatabase(Weather weather) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_PATH)) {
            PreparedStatement saveWeather = connection.prepareStatement(insertWeather);
            saveWeather.setString(1,weather.getCity());
            saveWeather.setString(2,weather.getLocalDate());
            saveWeather.setString(3,weather.getTemperature());

            return saveWeather.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new SQLException("Сохранение в базу не выполнено!");

    }
    public void saveWeatherToDatabase(List<Weather> weatherList) {
        try (Connection connection = DriverManager.getConnection(DB_PATH)) {
            PreparedStatement saveWeather = connection.prepareStatement(insertWeather);
            for (Weather weather : weatherList) {
                saveWeather.setString(1, weather.getCity());
                saveWeather.setString(2, weather.getLocalDate());
                saveWeather.setString(3, weather.getTemperature());

                saveWeather.addBatch();
            }
            saveWeather.executeBatch();
        } catch (SQLException trowables) {
            trowables.printStackTrace();
        }
    }

    public List<Weather> getSavedToDBWeather() {
        List<Weather> weather = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_PATH)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getWeather);

            while (resultSet.next()){
                System.out.println(resultSet.getInt("id"));
                System.out.println(" ");
                System.out.println(resultSet.getString("city"));
                System.out.println(" ");
                System.out.println(resultSet.getString("localdate"));
                System.out.println(" ");
                System.out.println(resultSet.getDouble("temperature"));
                System.out.println(" ");
                weather.add (new Weather(resultSet.getString("city")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weather;
    }
}
