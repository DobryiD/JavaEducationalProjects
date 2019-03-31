import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Server {
    private List<DatagramSocket> sockets;
    private Map<Integer, List<Integer>> map = new ConcurrentHashMap<>();
    private ServerSocket server;
    private Integer[] portsSequance;

    private Server(String... args) {
        portsSequance = new Integer[args.length];
                Arrays.stream(args).map(Integer::parseInt).
                collect(Collectors.toList()).toArray(portsSequance);
        sockets = new ArrayList<>();
        try {
            for (Integer port : portsSequance) {
                DatagramSocket temp = new DatagramSocket(port);
                temp.setBroadcast(true);
                sockets.add(temp);
                (new Thread(new ServerPortThread(temp))).start();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Server(args);
    }
    private void createServerThread() {
        int randPort = new Random().nextInt(9000) + 1000;
        try {
            server = new ServerSocket(randPort);
            System.out.println("Server address: "+server.getInetAddress()+":"+server.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        (new Thread(new ServerThread(server))).start();
    }

    private void portPacketsCheck(DatagramPacket receivedPacket) {
        boolean correct = false;
        if (map.containsKey(receivedPacket.getPort())) {
            Integer[] val = new Integer[map.get(receivedPacket.getPort()).size()];
            map.get(receivedPacket.getPort()).toArray(val);
            if (val.length == portsSequance.length) {
                Boolean[] checks = new Boolean[val.length];
                for (int i = 0; i < portsSequance.length; i++) {
                    checks[i] = val[i].equals(portsSequance[i]);
                }
                correct = Arrays.stream(checks).allMatch(Boolean::valueOf);
            }
            if (correct) {
                System.out.println(receivedPacket.getPort()+" sent correct sequence!");
                createServerThread();
                sendResponce(receivedPacket);
            }
        }
    }
    private void sendResponce(DatagramPacket receivedPacket) {

        int port = receivedPacket.getPort();
        InetAddress address = receivedPacket.getAddress();
        String mes = "Connect to:" + server.getInetAddress().toString() + ":" + server.getLocalPort();
        byte[] buff = mes.getBytes();
        DatagramPacket packetSend = new DatagramPacket(buff, buff.length, address, port);
        try {
            sockets.get(new Random().nextInt(sockets.size())).send(packetSend);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class ServerPortThread implements Runnable {
        byte[] buff = new byte[256];
        DatagramSocket socket;
        DatagramPacket packet;

        ServerPortThread(DatagramSocket socket) {
            this.socket = socket;
            packet = new DatagramPacket(buff, buff.length);
        }

        @Override
        public void run() {

            System.out.println("Socket:" + socket.getLocalPort() + " waits for packet");
            while (true) {
                try {
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    int port = packet.getPort();
                    System.out.println("Port: " + port + " sent--" + message + " to " + socket.getLocalPort());
                    if (!map.containsKey(port)) {
                        map.put(port, new ArrayList<>());
                        map.get(port).add(socket.getLocalPort());
                    } else
                        map.get(port).add(socket.getLocalPort());

                    portPacketsCheck(packet);


                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }
}
