import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class server {
    public static KeyValueStoreHandler handler;
    public static KeyValueStore.Processor processor;
    private static int port = 9999;

    public static void main(String[] args) {
        try {
            port = Integer.valueOf(args[0]);
            handler = new KeyValueStoreHandler();
            processor = new KeyValueStore.Processor<KeyValueStore.Iface>(handler);

            Runnable simple = () -> simple(processor);
            new Thread(simple).start();
        } catch(Exception x) {
            x.printStackTrace();
        }
    }

    public static void simple(KeyValueStore.Processor processor) {
        try {
            TServerTransport serverTransport  = new TServerSocket(port);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the simple server...");
            server.serve();
            System.out.println("Done.");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
