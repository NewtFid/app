import Connect.Phone;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    Connection connection;
    Statement statement;

    private Server() throws SQLException {
        initDatabase();
    }
    private static String getUrlContent(String urlAdress){
        StringBuffer content = new StringBuffer();
        try{
            URL url = new URL(urlAdress);
            URLConnection urlConn = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;

            while((line = reader.readLine()) != null){
                content.append(line +"/n");
            }
            reader.close();
        } catch(Exception e){
            System.out.println("Такой город не найден");
        }
        return content.toString();
    }
    private void initDatabase() throws SQLException {

        String jdbcURL = "jdbc:h2:./test";
        String username = "sa";
        String password = "1234";

        connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connected to H2 embedded database.");

        statement = connection.createStatement();
        try {
            statement.execute("create Table cities(name varChar(32))");
            statement.execute("INSERT INTO cities values('London') ");
            statement.execute("INSERT INTO cities values('Saint-Petersburg') ");
            statement.execute("INSERT INTO cities values('Moscow') ");
            statement.execute("INSERT INTO cities values('Kemerovo') ");
        }
        catch (org.h2.jdbc.JdbcSQLSyntaxErrorException e)
        { }
    }

    private String[] getCities() throws SQLException {
        ResultSet resultSet = statement.executeQuery("Select * FROM cities");

        List<String> result = new ArrayList<>();

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            result.add(name);
        }

        return result.toArray(new String[]{});
    }
    private String getWeather(String name) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("select name FROM Cities where lower(name) = lower(?)");
        stmt.setString(1, name);
        ResultSet result = stmt.executeQuery();

        // ResultSet result = statement.executeQuery("select name FROM Cities where lower(name) = lower('" + name + "')");
        if (!result.next()){
            return "";
        }

        String getUserCity = name;
        if(!getUserCity.equals("")) {
            String output = getUrlContent("http://api.openweathermap.org/data/2.5/weather?q=" + getUserCity + "&appid=759e7df2549251de826a98defa2102b0&units=metric");


            if (!output.isEmpty()) {
                JSONObject obj = new JSONObject(output);
                JSONObject main = obj.getJSONObject("main");
                return "" + main.getDouble("temp")
                        + "," + main.getDouble("feels_like")
                        + "," + main.getDouble("temp_min")
                        + "," + main.getDouble("temp_max")
                        + "," + main.getDouble("pressure")
                        ;
            }
        }

        return "";
    }

    public void body(Phone phone) throws SQLException {
        String request = phone.readLine();

        String[] data = request.split(",");
        switch(data[0]) {
            case "getCities":
                phone.writeLine(String.join("," , getCities()));
                break;

            case "getWeather":
                phone.writeLine(String.join("," , getWeather(data[1])));
                break;

        }
    }

    private static Server createServer(){
        try{
            return new Server();
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = createServer();

        try (ServerSocket socket = new ServerSocket(8000)) {
            System.out.println("Server started");
            while (true) {
                Phone phone = new Phone(socket);
                new Thread(() -> {
                    try (phone) {
                        server.body(phone);

                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}