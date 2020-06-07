// Java program to illustrate Server side
// Implementation using ServerSocket
import java.net.*;
import java.io.*;
import java.util.HashMap;
public class TCPServer {
	static HashMap<Integer, Integer> map = new HashMap<>();
	
	@SuppressWarnings("null")
	public static void main(String[] args) {
		ServerSocket server = null; 
		Socket clientSocket = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		try {
			int serverPort = Integer.valueOf(args[0]);
			 server = new ServerSocket(serverPort);
			 System.out.println("Server started");
			 System.out.println("Waiting for a client...");
			while (true) {
				clientSocket = server.accept();
				System.out.println("Client accepted");
				
				in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
				String receiveData = in.readUTF();
				if (receiveData.startsWith("Get")) {
					int key = Integer.valueOf(receiveData.substring(4));
					out.writeUTF(receiveData);
				} else if (receiveData.startsWith("Delete")) {
					int key = Integer.valueOf(receiveData.substring(7));
					map.remove(key);
					out.writeUTF(receiveData);
				} else if (receiveData.startsWith("Put")) {
					String[] split = receiveData.substring(4).split(",");
					int key = Integer.valueOf(split[0]);
					int val = Integer.valueOf(split[1]);
					map.put(key, val);
					out.writeUTF(receiveData);
				} else {
					System.out.println("Received unsolicited request acknowledging unknown method");
				}
			}
		} catch(IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
	}

}
