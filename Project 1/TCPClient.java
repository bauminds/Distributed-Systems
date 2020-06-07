// Java program to illustrate client side
// Implementation using Socket
import java.net.*;
import java.io.*;

public class TCPClient {
	
	public static void main(String[] args) {
		Socket socket = null;
		try {
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
			socket = new Socket(serverIP, serverPort);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			// pre-populate the key-value store with data and keys:
			for (int i = 5; i < 10; i++) {
				try {
					String message = "Put " + String.valueOf(i) + "," + String.valueOf(i + 4);
					out.writeUTF(message);
					String reply = in.readUTF();
					if (!reply.startsWith("Put")) {
						System.out.println("Received unsolicited response without 'Put'");
					} else if (Integer.valueOf(reply.substring(4)) != i) {
						System.out.println("Received unsolicited response acknowledging unknown PUT with an invalid key");
					} else System.out.println("Put value of key " + String.valueOf(i) + ": " + String.valueOf(i + 4));
				} catch (UnknownHostException e) {
					System.out.println("Sock: " + e.getMessage());
				} catch (EOFException e) {
					System.out.println("EOF: " + e.getMessage());
				} catch (IOException e) {
					System.out.println("IO: " + e.getMessage());
				} finally {
					if (socket != null) 
						try {
							socket.close();
						} catch (IOException e) {
							System.out.println("Close failed");
						}
				}
			}
			
			// 5 puts
			for (int i = 0; i < 5; i++) {
				try {
					String message = "Put " + String.valueOf(i) + "," + String.valueOf(i + 4);
					out.writeUTF(message);
					String reply = in.readUTF();
					if (!reply.startsWith("Put")) {
						System.out.println("Received unsolicited response without 'Put'");
					} else if (Integer.valueOf(reply.substring(4)) != i) {
						System.out.println("Received unsolicited response acknowledging unknown PUT with an invalid key");
					} else System.out.println("Put value of key " + String.valueOf(i) + ": " + String.valueOf(i + 4));
				} catch (UnknownHostException e) {
					System.out.println("Sock: " + e.getMessage());
				} catch (EOFException e) {
					System.out.println("EOF: " + e.getMessage());
				} catch (IOException e) {
					System.out.println("IO: " + e.getMessage());
				} finally {
					if (socket != null) 
						try {
							socket.close();
						} catch (IOException e) {
							System.out.println("Close failed");
						}
				}
			}
			
			// 5 gets
			for (int i = 5; i < 10; i++) {
				try {
					String message = "Get " + String.valueOf(i) + "," + String.valueOf(i + 4);
					out.writeUTF(message);
					String reply = in.readUTF();
					if (!reply.startsWith("Get")) {
						System.out.println("Received unsolicited response without 'Get'");
					} else if (Integer.valueOf(reply.substring(4)) != i) {
						System.out.println("Received unsolicited response acknowledging unknown GET with an invalid key");
					} else {
						String[] splits = reply.substring(4).split(",");
//						int val = Integer.valueOf(splits[1]);
						System.out.println("Get value of key " + String.valueOf(i) + ": " + splits[1]);
					}
				} catch (UnknownHostException e) {
					System.out.println("Sock: " + e.getMessage());
				} catch (EOFException e) {
					System.out.println("EOF: " + e.getMessage());
				} catch (IOException e) {
					System.out.println("IO: " + e.getMessage());
				} finally {
					if (socket != null) 
						try {
							socket.close();
						} catch (IOException e) {
							System.out.println("Close failed");
						}
				}
			}
			
			// 5 deletes
			for (int i = 5; i < 10; i++) {
				try {
					String message = "Delete" + String.valueOf(i) + "," + String.valueOf(i + 4);
					out.writeUTF(message);
					String reply = in.readUTF();
					if (!reply.startsWith("Get")) {
						System.out.println("Received unsolicited response without 'Delete'");
					} else if (Integer.valueOf(reply.substring(7)) != i) {
						System.out.println("Received unsolicited response acknowledging unknown Delete with an invalid key");
					} else System.out.println("Delete value of key " + String.valueOf(i));
				} catch (UnknownHostException e) {
					System.out.println("Sock: " + e.getMessage());
				} catch (EOFException e) {
					System.out.println("EOF: " + e.getMessage());
				} catch (IOException e) {
					System.out.println("IO: " + e.getMessage());
				} finally {
					if (socket != null) 
						try {
							socket.close();
						} catch (IOException e) {
							System.out.println("Close failed");
						}
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("Sock: " + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (socket != null) 
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Close failed");
				}
		}
	}
}
