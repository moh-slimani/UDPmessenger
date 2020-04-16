import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
public class UDPClient {
    private DatagramSocket udpSocket;
    private InetAddress serverAddress;
    private int port;
    private Scanner scanner;
    private UDPClient(String destinationAddr, int port) throws IOException {
        this.serverAddress = InetAddress.getByName(destinationAddr);
        this.port = port;
        udpSocket = new DatagramSocket(this.port);
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) throws NumberFormatException, IOException {

        UDPClient client = new UDPClient("172.18.0.2",9875);
        System.out.println("-- Running UDP Client at " + InetAddress.getLocalHost() + " --");
        client.start();
    }

    private void start() throws IOException {
        String in = "";
        String username;

        System.out.print("put your username: ");
        username = scanner.nextLine();

        String connect =  username + ":connect" ;

        DatagramPacket p = new DatagramPacket(connect.getBytes(), connect.getBytes().length, serverAddress, port);

        this.udpSocket.send(p);

        new Thread(() -> {

            String msg = "";

            while (!msg.equals("disconnected")) {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                try {
                    udpSocket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                msg = new String(packet.getData()).trim();
                System.out.println(msg);
            }
        }).start();


        while (!in.trim().equals(username + ":disconnect")) {
            in = scanner.nextLine();

            in = username + ":" + in;

            p = new DatagramPacket(
                    in.getBytes(), in.getBytes().length, serverAddress, port);

            this.udpSocket.send(p);
        }

    }
}