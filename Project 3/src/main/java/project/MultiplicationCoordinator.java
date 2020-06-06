package project;

import java.util.*;

public class MultiplicationCoordinator {

    public static void main(String args[]) {
        if (args.length != 5) {
            System.out.println("Please input 5 port numbers of servers");
            System.exit(1);
        }

        List<Integer> others = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            try {
                others.add(Integer.parseInt(args[i]));
            } catch (Exception e) {
                System.out.println("Please input 5 integers");
            }
        }

        for (int i = 0; i < 5; i++) {
            int port = Integer.parseInt(args[i]);
            MultiplicationServer server = new MultiplicationServer(port);
            server.setDistributedPorts(others);
            server.start();
//            // Test for Scenario 2
//            if (i == 4) {
//                try {
//                    Thread.sleep(20000);
//                } catch (Exception e) {}
//                server.stop();
//            }
        }
    }
}