package HW7;


import java.io.IOException;

import static HW7.Period.FIVE_DAYS;
import static HW7.Period.ONE_DAY;

public class Main {
    public static void main(String[] args) throws IOException {
        UserInterfaceView interFaceView = new UserInterfaceView();
        interFaceView.runInterface();
        AccuweatherModel accuweatherModel = new AccuweatherModel();

        System. out.println(accuweatherModel.detectCityKey ("Moscow", ONE_DAY));
        System. out.println(accuweatherModel.detectCityKey ("Moscow", FIVE_DAYS));
    }
}
