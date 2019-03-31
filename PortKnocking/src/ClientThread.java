import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket clientSocket;

    ClientThread(Socket socket) {
        clientSocket = socket;

    }

    @Override
    public void run() {
        BufferedReader input;
        PrintWriter output;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            while (true) {
                if (input.ready()) {
                    System.out.println(input.readLine());
                    output.println("Welcome to the server!");
                    clientSocket.close();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
