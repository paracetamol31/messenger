import com.client.Client;

import java.io.UnsupportedEncodingException;

public class MainClient {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Client client = new Client();
        client.join();
    }
}
