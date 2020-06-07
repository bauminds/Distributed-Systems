// Java program to illustrate client side
// Implementation using DatagramSocket
import java.io.IOException;
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient {
	
	public static void main(String[] args) throws IOException {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket();
			aSocket.setSoTimeout(5 * 1000);
			// get IP address of the server
			// if given IP address of the server
			InetAddress serverIP = null;
			if (Character.isDigit(args[0].charAt(0))) {
				byte[] ip = args[0].getBytes();
				serverIP = InetAddress.getByAddress(ip);
			} else { // if given hostname
				serverIP = InetAddress.getByName(args[0]);
			}
			// get port number of the server 
			int serverPort = Integer.valueOf(args[1]);
		
			// pre-populate the key-value store with data and keys:
			for (int i = 5; i < 10; i++) {
				try {
					String message = "Put" + String.valueOf(i) + "," + String.valueOf(i + 4);
					byte[] m = message.getBytes();
					DatagramPacket request = new DatagramPacket(m, message.length(), serverIP, serverPort);
					aSocket.send(request);
					
					// receive
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(reply);
					String replyData = new String(reply.getData());
					
					System.out.println(System.currentTimeMillis());
					if (!replyData.startsWith("Get")) {
						System.out.println("Received unsolicited response without 'Get'");
					} else if (Integer.valueOf(replyData.substring(4)) != i) {
						System.out.println("Received unsolicited response acknowledging unknown GET with an invalid key");
					} else System.out.println("Get value of key " + String.valueOf(i) + ": " + replyData);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 5 puts
			for (int i = 0; i < 5; i++) {
				try {
					String message = "Put " + String.valueOf(i) + "," + String.valueOf(i + 4);
					byte[] m = message.getBytes();
					DatagramPacket request = new DatagramPacket(m, message.length(), serverIP, serverPort);
					aSocket.send(request);
					
					// receive
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(reply);
					
					System.out.println(System.currentTimeMillis());
					System.out.println("Get value of key " + String.valueOf(i) + ": " + new String(reply.getData()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// 5 gets
			for (int i = 0; i < 10; i += 2) {
				try {
					String message = "Get " + String.valueOf(i);
					byte[] m = message.getBytes();
					DatagramPacket request = new DatagramPacket(m, message.length(), serverIP, serverPort);
					aSocket.send(request);
					
					// receive
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(reply);
					String replyData = new String(reply.getData());
					
					System.out.println(System.currentTimeMillis());
					if (!replyData.startsWith("Get")) {
						System.out.println("Received unsolicited response without 'Get'");
					} else if (Integer.valueOf(replyData.substring(4)) != i) {
						System.out.println("Received unsolicited response acknowledging unknown GET with an invalid key");
					} else System.out.println("Get value of key " + String.valueOf(i) + ": " + replyData);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// 5 deletes
			for (int i = 0; i < 5; i++) {
				try {
					String message = "Delete " + String.valueOf(i);
					byte[] m = message.getBytes();
					DatagramPacket request = new DatagramPacket(m, message.length(), serverIP, serverPort);
					aSocket.send(request);
					
					// receive
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					String replyData = new String(reply.getData());
					
					System.out.println(System.currentTimeMillis());
					if (!replyData.startsWith("Delete")) {
						System.out.println("Received unsolicited response without 'Delete'");
					} else if (Integer.valueOf(replyData.substring(7)) != i) {
						System.out.println("Received unsolicited response acknowledging unknown DELETE with an invalid key");
					} else System.out.println("Delete value of key " + String.valueOf(i) + ": " + replyData);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
}
