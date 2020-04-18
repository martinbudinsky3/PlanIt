import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class Main{

    public static void main(String[] args) throws IOException {
        URL url = new URL("http://localhost:8080/users");
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }
}
