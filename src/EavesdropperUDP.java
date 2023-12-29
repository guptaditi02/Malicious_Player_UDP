//Aditi Gupta - argupta@andrew.cmu.edu - Project2Task1
// Taken reference from EchoServerUDP.java  and EchoClientUDP.java  from Coulouris textbook to make the changes
//Combined them to make EavesdropperUDP.java

import java.net.*;
import java.io.*;

public class EavesdropperUDP {
    public static void main(String args[]) {
        DatagramSocket eavesdropperSocket = null;
        DatagramSocket serverSocket = null;

        try {
            // Prompt the user for the ports
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the port for Eavesdropper to listen on (e.g., 6798): ");
            int eavesdropperPort = Integer.parseInt(reader.readLine());

            System.out.print("Enter the port number of the server (e.g., 6789): ");
            int serverPort = Integer.parseInt(reader.readLine());

            // Create a DatagramSocket for eavesdropping
            eavesdropperSocket = new DatagramSocket(eavesdropperPort);

            // Create a DatagramSocket to masquerade as the server
            serverSocket = new DatagramSocket();

            // Announce that the eavesdropper is running
            System.out.println("EavesdropperUDP is running on port " + eavesdropperPort);
            System.out.println("Masquerading as the server on port " + serverPort);

            while (true) {
                // Receive a message from the client
                byte[] clientBuffer = new byte[1000];
                DatagramPacket clientRequest = new DatagramPacket(clientBuffer, clientBuffer.length);
                eavesdropperSocket.receive(clientRequest);

                // Extract and print the client's message
                int clientRequestLength = clientRequest.getLength();
                byte[] clientRequestData = new byte[clientRequestLength];

                // Code taken from this site:
                // https://stackoverflow.com/questions/5690954/java-how-to-read-an-unknown-number-of-bytes-from-an-inputstream-socket-socke

                System.arraycopy(clientRequest.getData(), 0, clientRequestData, 0, clientRequestLength);
                String clientMessage = new String(clientRequestData);
                System.out.println("Received from client: " + clientMessage);


                //Used ChatGPT for this line
                // Replace "like" with "dislike" in the client's message
                clientMessage = clientMessage.replaceAll("(?i)\\blike\\b", "dislike");

                // Forward the modified client's message to the server
                byte[] serverRequestData = clientMessage.getBytes();
                DatagramPacket serverRequest = new DatagramPacket(serverRequestData, serverRequestData.length,
                        InetAddress.getLocalHost(), serverPort);
                serverSocket.send(serverRequest);

                // Receive the server's reply
                byte[] serverReplyBuffer = new byte[1000];
                DatagramPacket serverReply = new DatagramPacket(serverReplyBuffer, serverReplyBuffer.length);
                serverSocket.receive(serverReply);

                // Extract and print the server's message
                int serverReplyLength = serverReply.getLength();
                byte[] serverReplyData = new byte[serverReplyLength];

                // Code taken from this site:
                // https://stackoverflow.com/questions/5690954/java-how-to-read-an-unknown-number-of-bytes-from-an-inputstream-socket-socke

                System.arraycopy(serverReply.getData(), 0, serverReplyData, 0, serverReplyLength);
                String serverMessage = new String(serverReplyData);
                System.out.println("Received from server: " + serverMessage);

                // Forward the server's reply to the client
                DatagramPacket clientReply = new DatagramPacket(serverReply.getData(), serverReply.getLength(),
                        clientRequest.getAddress(), clientRequest.getPort());
                eavesdropperSocket.send(clientReply);
            }
        } catch (SocketException e) {
            System.out.println("Socket Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        } finally {
            if (eavesdropperSocket != null)
                eavesdropperSocket.close();
            if (serverSocket != null)
                serverSocket.close();
        }
    }
}
