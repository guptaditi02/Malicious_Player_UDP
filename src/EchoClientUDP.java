//Aditi Gupta - argupta@andrew.cmu.edu- Project2Task1
// Taken Code from EchoClientUDP.java from Coulouris text


import java.net.*;
import java.io.*;

public class EchoClientUDP {
    public static void main(String args[]) {
        DatagramSocket aSocket = null;
        try {
            // Announce that the client is running
            System.out.println("The UDP client is running.");

            // Change the server address to "localhost"
            InetAddress aHost = InetAddress.getByName("localhost");

            // Prompt the user for the server side port number
            System.out.print("Enter the server side port number (e.g., 6789): ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int serverPort = Integer.parseInt(reader.readLine());

            // Create a DatagramSocket for sending and receiving UDP packets
            aSocket = new DatagramSocket();

            String nextLine;
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));

            while ((nextLine = typed.readLine()) != null) {
                // Convert the input text into bytes and create a DatagramPacket
                byte[] m = nextLine.getBytes();
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                aSocket.send(request);

                // Check if the client wants to halt
                if (nextLine.trim().equalsIgnoreCase("halt!")) {
                    System.out.println("Client side quitting.");
                    break;
                }

                // Receive a reply from the server
                byte[] buffer = new byte[1000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);

                // Extract and print the reply from the server
                int replyLength = reply.getLength();
                byte[] replyData = new byte[replyLength];

                // Code taken from this site:
                // https://stackoverflow.com/questions/5690954/java-how-to-read-an-unknown-number-of-bytes-from-an-inputstream-socket-socke

                System.arraycopy(reply.getData(), 0, replyData, 0, replyLength);
                String replyString = new String(replyData);
                System.out.println("Reply from server: " + replyString);
            }
        } catch (SocketException e) {
            System.out.println("Socket Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }
}
