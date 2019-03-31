
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class Client {
    private DatagramSocket socket = null;
    private Integer[] portSequance;
    private String ipAddress;

    private Client(String ip,String... args) {
        portSequance = new Integer[args.length];
        ipAddress=ip;
        Arrays.stream(args).map(Integer::parseInt).
                collect(Collectors.toList()).toArray(portSequance);
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        sendPacket();
    }

    public static void main(String[] args) {
        new Client("localhost",args);
    }

    private void receivePacket() {
        byte[] buff = new byte[128];
        DatagramPacket packetReceived = new DatagramPacket(buff, buff.length);
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("No response from server!\nTermination!");
                System.exit(1);
            }
        }, 30000);
        try {
            socket.receive(packetReceived);
            time.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message = new String(packetReceived.getData(), 0, packetReceived.getLength());
        int port = packetReceived.getPort();
        System.out.println("Port: " + port + " sent--" + message);
        String[] temp = message.split(":");
        TCPconnection(temp);

    }

    private void TCPconnection(String[] address) {
        Socket socket;
        PrintWriter output;
        BufferedReader input;
        try {

            socket = new Socket("localhost", Integer.parseInt(address[2]));
            System.out.println("Connected to server!");
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.println("Hello");

            while (true) {
                if (input.ready()) {
                    System.out.println(input.readLine());
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

    }

    private void sendPacket() {
        int count = 0;
            InetAddress address = null;
        try {
            address = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        for (int aPortSequance : portSequance) {
            String mess = String.valueOf(count);
            byte[] buff = String.valueOf(count).getBytes();
            DatagramPacket packetSend = new DatagramPacket(buff, buff.length, address, aPortSequance);
            try {
                System.out.println("Sending " + mess + " to " + aPortSequance);
                socket.send(packetSend);
                Thread.sleep(2000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            count++;
        }
        receivePacket();

    }

}
