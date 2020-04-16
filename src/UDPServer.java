import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class UDPServer {
    private DatagramSocket udpSocket;
    private int port;

    public UDPServer(int port) throws SocketException, IOException {
        this.port = port;
        this.udpSocket = new DatagramSocket(this.port);
    }

    private void listen() throws Exception {
        System.out.println("-- Running Server at " + InetAddress.getLocalHost() + "--");
        String msg;

        HashMap<String, String> clients = new HashMap<>();

        while (true) {

            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            DatagramPacket p = new DatagramPacket(buf, buf.length);

            // blocks until a packet is received
            udpSocket.receive(packet);
            msg = new String(packet.getData()).trim();

            String[] info = msg.split(":");
            if (info.length > 0)
                switch (info[1]) {
                    case "connect":
                        clients.put(info[0], packet.getAddress().getHostAddress());

                        p = new DatagramPacket("connected".getBytes(), "connected".getBytes().length, packet.getAddress(), port);
                        this.udpSocket.send(p);
                        clients.forEach((username, address) ->
                                System.out.println(username + " : " + address));
                        break;
                    case "list":
                        StringBuilder list = new StringBuilder();
                        for (Map.Entry<String, String> entry : clients.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            list.append(key).append(" : ").append(value).append("\n");
                        }

                        p = new DatagramPacket(list.toString().getBytes(), list.toString().getBytes().length, packet.getAddress(), port);
                        this.udpSocket.send(p);
                        break;
                    case "send":
                        String msgToSend = info[0]+ ":" + info[3];
                        p = new DatagramPacket(msgToSend.getBytes(), msgToSend.getBytes().length, InetAddress.getByName(clients.get(info[2])), port);
                        this.udpSocket.send(p);
                        break;
                    case "disconnect":
                        clients.remove(info[0]);
                        clients.forEach((username, address) ->
                                System.out.println(username + " : " + address));
                        p = new DatagramPacket("disconnected".getBytes(), "disconnected".getBytes().length, packet.getAddress(), port);
                        this.udpSocket.send(p);
                        break;
                    default:
                        System.out.println("problem .....");
                }


            System.out.println("Message from " +
                    packet.getAddress().getHostAddress() +
                    ": " + msg);
        }
    }

    public static void main(String[] args) throws Exception {
        UDPServer client = new UDPServer(9875);
        client.listen();
    }
}