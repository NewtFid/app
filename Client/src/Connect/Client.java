package Connect;

import Connect.Phone;
import Connect.Weather;


import java.io.IOException;
public class Client {

    private Phone phone;

    public Client()
    {
        phone = new Phone("127.0.0.1", 8000);
    }

    public String[] getCities()
    {
        phone.writeLine("getCities");
        String response = phone.readLine();
        return response.split(",");
    }

    public Weather getWeather(String city)
    {
        phone.writeLine("getWeather,"+city);
        String response = phone.readLine();
        String[] data = response.split(",");
        if(data.length < 5){return null;}
        Weather weather = new Weather();
        weather.temp = Float.parseFloat(data[0]);
        weather.feels = Float.parseFloat(data[1]);
        weather.temp_min = Float.parseFloat(data[2]);
        weather.temp_max = Float.parseFloat(data[3]);
        weather.pressure = Float.parseFloat(data[4]);
        return weather;
    }


}
