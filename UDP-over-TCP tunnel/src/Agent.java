import Threads.AgentUdpPortThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Agent {
    private String ipOfRelay;
    private Integer[] udpPorts;

    private Agent(String ipOfRelay,String[]args){
        this.ipOfRelay=ipOfRelay;
        udpPorts=new Integer[args.length-1];
        String[] temp=Arrays.copyOfRange(args,1,args.length);
        Arrays.stream(temp).map(Integer::parseInt).collect(Collectors.toList())
                .toArray(udpPorts);
        connectToRelay();
    }
    public static void main(String[] args) {
        new Agent(args[0],args);
    }

    private void configuration(PrintWriter output){
        Scanner scan=new Scanner(System.in);
        System.out.println("Write ip of receiver");
        String ipOfreceiver = (scan.nextLine());
        output.println("Receiver::"+ ipOfreceiver);
    }
    private void connectToRelay(){
        PrintWriter out ;
        BufferedReader in;
        try {
            int portOfRelay = 999;
            Socket mySocket = new Socket(ipOfRelay, portOfRelay);
            System.out.println("Connected to " + mySocket.getPort());
            out = new PrintWriter(mySocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            configuration(out);
            List<DatagramSocket> sockets = new ArrayList<>();
            for (Integer port : udpPorts) {
                DatagramSocket temp = new DatagramSocket(port);
                temp.setBroadcast(true);
                sockets.add(temp);
                (new Thread(new AgentUdpPortThread(temp,mySocket))).start();
            }
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while (true){
                if(userInput.ready()){
                    line=userInput.readLine();
                    if(line.toLowerCase().equals("quit")){
                        out.println("quit");
                        System.exit(1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
