package project;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiplicationServer {

    public static MultiplicationHandler handler;

    public static MultiplicationService.Processor processor;

    private int port = 8080;
    private List<Integer> ports = new ArrayList<>();
    private Thread t = null;

    public MultiplicationServer(int port) {
        this.port = port;
    }

    public void setDistributedPorts(List<Integer> ports) {
        this.ports = ports;
    }

    public void start() {
        try {
            handler = new MultiplicationHandler(this.ports);
            processor = new MultiplicationService.Processor(handler);

            Runnable simple = new Runnable() {
                public void run() { simple(processor); }
            };

            Thread t = new Thread(simple);
            this.t = t;
            t.start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void stop() {
        System.out.println("Shutting down server with port: " + this.port);
        if (t != null) t.stop();
    }

    public void simple(MultiplicationService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(this.port);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the thrift server on port:" + this.port);
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}