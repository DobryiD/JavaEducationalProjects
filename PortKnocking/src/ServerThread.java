import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {
    private ServerSocket server;
    private Socket client = null;

    ServerThread(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                client = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            InetAddress address = client.getInetAddress();
            int clientPort = client.getPort();
            System.out.println("IP:" + address.toString() + ":" + clientPort + " is connected");
            (new Thread(new ClientThread(client))).start();
        }
    }
}
