package Threads;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class RelayUdpPortThread implements Runnable {

    byte[] buff = new byte[256];
    DatagramSocket udpSocket;
    DatagramPacket packet;
    Socket tcpSocket;
    RelayUdpPortThread(DatagramSocket udpSocket, Socket tcpSocket){
        this.udpSocket=udpSocket;
        this.tcpSocket=tcpSocket;
        packet=new DatagramPacket(buff,buff.length);
    }
    @Override
    public void run() {
        try {
            System.out.println("UDP socket:" + udpSocket.getLocalPort() + " waits for packet");
            PrintWriter out =new PrintWriter(tcpSocket.getOutputStream(), true);
            while (true) {
                udpSocket.receive(packet);
                int port = packet.getPort();
                String message = new String(packet.getData(), 0, packet.getLength());
                String redoneMess="Message from::"+port+"::"+message;
                System.out.println(packet.getAddress()+" " + port + " sent--" + message + " to " + udpSocket.getLocalPort());
                out.println(redoneMess);
            }
        } catch (IOException e) {
            Thread.currentThread().interrupt();

        }
    }
}
