import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class client {
    public static void main(String[] args) throws TTransportException {
        String hostname = args[0];
        int port = Integer.valueOf(args[1]);

        TTransport transport = new TSocket(hostname, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        KeyValueStore.Client client = new KeyValueStore.Client(protocol);

        // prepopulate values
        transport.open();
        for (int i = 5; i < 10; i++) {
            try {
                client.put(i, i + 5);
                printCurTime();
                System.out.println("Client put key: " + String.valueOf(i) + ", value: " + String.valueOf(i + 5) + ".");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        transport.close();

        // wait for input
        while (true) {
            try {
                transport.open();

                Scanner scanner = new Scanner(System.in);
                String[] input = scanner.nextLine().split(" ");
                if (!isValid(input)) {
                    System.out.println("Invalid input.");
                    transport.close();
                    continue;
                }

                if (input[0].equals("PUT")) {
                    client.put(Integer.valueOf(input[1]), Integer.valueOf(input[2]));
                    printCurTime();
                    System.out.println("Client put key: " + input[1] + ", value: " + input[2] + " succeeded.");
                } else if (input[0].equals("GET")) {
                    String value = client.get(Integer.valueOf(input[1]));
                    printCurTime();
                    if (value.equals("")) {
                        System.out.println("Client get key: " + input[1] + " failed.");
                    } else {
                        System.out.println("Client get key: " + input[1] + ", value: " + value + ".");
                    }
                } else {
                    boolean deleted = client.deleteVal(Integer.valueOf(input[1]));
                    printCurTime();
                    if (deleted) {
                        System.out.println("Client delete key: " + input[1] + " succeeded.");
                    } else {
                        System.out.println("Client delete key: " + input[1] + " failed.");
                    }
                }
                transport.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void printCurTime() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
    }

    public static boolean isValid(String[] input) {
        if (input[0].equals("PUT")) {
            if (input.length != 3) return false;
        } else if (input[0].equals("GET") || input[0].equals("DELETE")) {
            if (input.length != 2) return false;
        } else {
            return false;
        }
        return true;
    }
}
