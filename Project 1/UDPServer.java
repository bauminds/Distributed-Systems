// Java program to illustrate Server side
// Implementation using DatagramSocket
import java.util.HashMap;
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException; 

public class UDPServer {
	static HashMap<Integer, Integer> map = new HashMap<>();
	
	public static void main(String[] args) {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket();
			int serverPort = Integer.valueOf(args[0]);
			byte[] buffer = new byte[65535];
			DatagramPacket request = null;
			while (true) {
				request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String receiveData = new String(request.getData()); 
				System.out.println("Client-: " + request.getData());
				if (receiveData.startsWith("Get")) {
					int key = Integer.valueOf(receiveData.substring(4));
					int val = map.get(key);
					map.put(key, val);
					byte[] valData = String.valueOf(val).getBytes();
					DatagramPacket reply = new DatagramPacket(valData, String.valueOf(val).length(), request.getAddress(), request.getPort());
					aSocket.send(reply);
				} else if (receiveData.startsWith("Delete")) {
					int key = Integer.valueOf(receiveData.substring(7));
					map.remove(key);
					DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
					aSocket.send(reply);
				} else if (receiveData.startsWith("Put")) {
					String[] split = receiveData.substring(4).split(",");
					int key = Integer.valueOf(split[0]);
					int val = Integer.valueOf(split[1]);
					map.put(key, val);
					DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
					aSocket.send(reply);
				} else {
					System.out.println("Received unsolicited request acknowledging unknown method");
				}
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage()); 
		} finally {
			if (aSocket != null) aSocket.close();
		}
	}
}
