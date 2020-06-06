package project;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import java.util.Scanner;

public class MultiplicationClient {
    private String hostname = "localhost";
    private int port = 8081;

    public MultiplicationClient() {}

    public MultiplicationClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public static void main(String[] args) throws TException {
        MultiplicationClient client = new MultiplicationClient();
        if (args.length == 2) {
            try {
                int port = Integer.parseInt(args[1]);
                client = new MultiplicationClient(args[0], port);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Please check the format of port number");
            }
        }

        MultiplicationService.Client c = null;
        try {
            c = client.connect();
        } catch (TException e) {
            System.out.println("port already been taken");
            System.exit(1);
        }
        Scanner input = new Scanner(System.in);
        while (true) {
            // read the string from user input
            System.out.print("Enter action(PUT(Key,Value)|GET(Key)|DELETE(Key)):");
            String s = input.nextLine();

            Message message = constructMessage(s);
            if (message != null) {
                Message response = c.process(message);
                System.out.println("Response received from " + client.hostname + ":" + client.port + " | Operation status: " + response.getResult() +
                        (response.getOp() != Operation.GET ? " | Commit status: " + response.getStatus() : ""));
                if (response.getResult() == Result.Success && response.getOp() == Operation.GET) {
                    System.out.println("value: " + response.getValue());
                }
            }
        }
    }

    public MultiplicationService.Client connect() throws TException {
        TTransport transport;

        transport = new TSocket(this.hostname, this.port);

        TProtocol protocol = new  TBinaryProtocol(transport);
        MultiplicationService.Client client = new MultiplicationService.Client(protocol);
        transport.open();

        return client;
    }

//    public void connect() {
//        try {
//            TTransport transport;
//
//            transport = new TSocket(this.hostname, this.port);
//            transport.open();
//
//            TProtocol protocol = new  TBinaryProtocol(transport);
//            MultiplicationService.Client client = new MultiplicationService.Client(protocol);
//
//            perform(client);
//
//            transport.close();
//        } catch (TException x) {
//            x.printStackTrace();
//            System.exit(1);
//        }
//    }

    public static Message constructMessage(String s) {
        try {
            String action = s.substring(0, s.indexOf("(")).trim();
            String body = s.substring(s.indexOf("(") + 1, s.indexOf(")"));

            switch (action) {
                case "PUT":
                    String key = body.split(",")[0];
                    String value = body.split(",")[1];
                    return new Message(key, value, Operation.PUT, Type.Client);
                case "GET":
                    return new Message(body, "", Operation.GET, Type.Client);
                case "DELETE":
                    return new Message(body, "", Operation.DELETE, Type.Client);
                default:
                    System.out.println("Unknown action, input should be PUT(<KEY>,<VALE>)|GET(<KEY>)|DELETE(<KEY>)");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Unknown action, input should be PUT(<KEY>,<VALE>)|GET(<KEY>)|DELETE(<KEY>)");
            return null;
        };
        return null;
    }

    private void perform(MultiplicationService.Client client) throws TException {
//        // Test Example
//        System.out.println("Test examples with 5 PUTs, 5 Gets and 5 Deletes: ");
//        // PUT key-value pair
//        String resp1 = client.put("3", "6");
//        System.out.println("PUT(3, 6) response: " + resp1);
//        String resp2 = client.put("10", "20");
//        System.out.println("PUT(10, 20) response: " + resp2);
//        String resp3 = client.put("13", "26");
//        System.out.println("PUT(13, 26) response: " + resp3);
//        String resp4 = client.put("16", "32");
//        System.out.println("PUT(16, 32) response: " + resp4);
//        String resp5 = client.put("18", "36");
//        System.out.println("PUT(18, 36) response: " + resp5);
//        // GET value using key
//        String resp6 = client.get("3");
//        System.out.println("GET(3) response: " + resp6);
//        String resp7 = client.get("13");
//        System.out.println("GET(13) response: " + resp7);
//        String resp8 = client.get("18");
//        System.out.println("GET(18) response: " + resp8);
//        String resp9 = client.get("19");
//        System.out.println("GET(19) response: " + resp9);
//        String resp10 = client.get("10");
//        System.out.println("GET(10) response: " + resp10);
//        // delete key
//        String resp11 = client.remove("3");
//        System.out.println("REMOVE(3) response: " + resp11);
//        String resp12 = client.remove("4");
//        System.out.println("REMOVE(4) response: " + resp12);
//        String resp13 = client.remove("10");
//        System.out.println("REMOVE(10) response: " + resp13);
//        String resp14 = client.remove("13");
//        System.out.println("REMOVE(13) response: " + resp14);
//        String resp15 = client.remove("14");
//        System.out.println("REMOVE(3) response: " + resp15);
//        System.out.println("----------- Test Finished -----------");

        // read the string from user input
        Scanner input = new Scanner(System.in);
        System.out.print("Enter action(PUT(Key,Value)|GET(Key)|DELETE(Key)):");
        String s = input.nextLine();
        input.close();

        Message message = this.constructMessage(s);
        Message response = client.process(message);
        System.out.println("response received from " + hostname + ":" + port + " Operation status: " + response.getResult());
        if (response.getResult() == Result.Success && response.getOp() == Operation.GET) {
            System.out.println("value: " + response.getValue());
        }
    }
}