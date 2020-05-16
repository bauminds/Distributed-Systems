package utility;

import org.hornetq.core.config.impl.FileConfiguration;

import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.jms.server.JMSServerManager;
import org.hornetq.jms.server.impl.JMSServerManagerImpl;

public class NotificationHQServer {
    public static void startServer() {
        try
        {
            FileConfiguration configuration = new FileConfiguration();
            configuration.setConfigurationUrl("hornetq-configuration.xml");
            configuration.start();

            HornetQServer server = HornetQServers.newHornetQServer(configuration);
            JMSServerManager jmsServerManager = new JMSServerManagerImpl(server, "hornetq-jms.xml");
            //if you want to use JNDI, simple inject a context here or don't call this method and make sure the JNDI parameters are set.
            jmsServerManager.setContext(null);
            jmsServerManager.start();
            System.out.println("HornetQ Server is started.");
        }
        catch (Throwable e)
        {
            System.out.println("Server starting failed!");
            e.printStackTrace();
        }
    }
    public static void main (String[] args) {
        startServer();
    }
}
