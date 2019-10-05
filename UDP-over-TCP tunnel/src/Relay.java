import Threads.TcpRelayAgentClientThread;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Relay {
    private Relay(){
        try {
            int tcpPort = 999;
            ServerSocket serverSocket = new ServerSocket(tcpPort);
            System.out.println("Socket is created");
            Socket client=null;
            while (true) {
                try {
                    client = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InetAddress address = client.getInetAddress();
                int clientPort = client.getPort();
                System.out.println("IP:" + address.toString() + ":" + clientPort + " is connected");
                new Thread(new TcpRelayAgentClientThread(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Relay();
    }



}
