package Threads;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class AgentUdpPortThread implements  Runnable {
    private DatagramSocket udpSocket;
    private DatagramPacket packet;
    private Socket tcpSocket;

    public AgentUdpPortThread(DatagramSocket udpSocket, Socket tcpSocket){
        this.udpSocket=udpSocket;
        this.tcpSocket=tcpSocket;
        byte[] buff = new byte[256];
        packet=new DatagramPacket(buff, buff.length);
    }
    @Override
    public void run() {
        try {
        System.out.println("UDP socket:" + udpSocket.getLocalPort() + " waits for packet");
        BufferedReader in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        PrintWriter out =new PrintWriter(tcpSocket.getOutputStream(), true);
        while (true) {
                udpSocket.receive(packet);
                int portOfsender=packet.getPort();
                int port = udpSocket.getLocalPort();
                InetAddress addressSender=packet.getAddress();
                String message = new String(packet.getData(), 0, packet.getLength());
                String resendMess="Message from::"+port+"::"+message;
                System.out.println(packet.getAddress()+" " + packet.getPort() + " sent--" + message + " to " + udpSocket.getLocalPort());
                out.println(resendMess);
                String line;

                if((line=in.readLine()).toLowerCase().contains("message from")){
                    System.out.println( line);
                    String[] temp = in.readLine().split("::");
                    String mess = temp[2];
                    byte[] buff = String.valueOf(mess).getBytes();
                    DatagramPacket packet = new DatagramPacket(buff, buff.length, addressSender, portOfsender);
                    udpSocket.send(packet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
