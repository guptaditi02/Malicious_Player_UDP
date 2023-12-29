//Aditi Gupta - argupta@andrew.cmu.edu- Project2Task1
// Taken code from EchoServerUDP.java from Coulouris text

import java.net.*;
import java.io.*;

public class EchoServerUDP {
    public static void main(String args[]) {
        DatagramSocket aSocket = null;
        byte[] buffer = new byte[1000];
        try {
            // Announce that the server is running
            System.out.println("The UDP server is running");
            // Create a BufferedReader to read input from the user
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Prompt the user for the port number to listen on
            System.out.print("Enter the port number for the server to listen on (e.g., 6789): ");
            int serverPort = Integer.parseInt(reader.readLine());

            // Create a DatagramSocket to listen for incoming UDP packets
            aSocket = new DatagramSocket(serverPort);
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);

            while (true) {
                // Receive an incoming UDP packet (request) from a client
                aSocket.receive(request);

                // Calculate the length of actual data in the request
                int requestLength = request.getLength();
                byte[] requestData = new byte[requestLength];

                // Code taken from this site:
                // https://stackoverflow.com/questions/5690954/java-how-to-read-an-unknown-number-of-bytes-from-an-inputstream-socket-socke
                System.arraycopy(request.getData(), request.getOffset(), requestData, 0, requestLength);
                String requestString = new String(requestData, 0, requestLength);

                // Print the received request
                System.out.println("Echoing: " + requestString);
                // Check if the client wants to halt
                if (requestString.trim().equalsIgnoreCase("halt!")) {
                    System.out.println("UDP Server side quitting");
                    break;
                }

                // Prepare and send a reply to the client
                DatagramPacket reply = new DatagramPacket(requestData, requestLength, request.getAddress(), request.getPort());
                aSocket.send(reply);
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
