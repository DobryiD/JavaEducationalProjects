package Threads;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TcpRelayAgentClientThread implements Runnable {
    private Socket clientSocket;
    private static Map<Socket,String> map=new HashMap<>();
    public TcpRelayAgentClientThread(Socket client){
        clientSocket=client;
    }
    @Override
    public void run() {
        BufferedReader in ;
        PrintWriter out;
        InetAddress address=null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out=new PrintWriter(clientSocket.getOutputStream(),true);
            DatagramSocket udpSocket=new DatagramSocket();

            new Thread(new RelayUdpPortThread(udpSocket,clientSocket)).start();
            System.out.println(udpSocket.getLocalPort());
            String line;
            while (true) {
                if(in.ready()) {
                    line = in.readLine();
                    System.out.println("Agent sent: " + line);

                    if (line.toLowerCase().contains("receiver::")) {
                        String[] temp = line.split("::");
                        String ip = temp[1];

                        if(map.containsValue(ip)){
                            out.println("Someone is communicating with this IP");
                        }else{
                            map.put(clientSocket,ip);
                            address = InetAddress.getByName(ip);
                        }
                    }
                    if(line.toLowerCase().contains("quit")){
                        map.remove(clientSocket);
                        clientSocket.close();
                    }
                    if (line.toLowerCase().contains("message from")) {
                        String[] temp = line.split("::");
                        int port=Integer.parseInt(temp[1]);
                        String message = temp[2];
                        byte[] buff = String.valueOf(message).getBytes();
                        DatagramPacket packet = new DatagramPacket(buff, buff.length, address, port);
                        udpSocket.send(packet);

                    }
                }

            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
