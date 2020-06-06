package project;

import org.apache.thrift.TException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MultiplicationHandler implements MultiplicationService.Iface {
    // Use ConcurrentHashMap for multi-thread safety
    protected ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    // cache is used to prepare for commit
    protected ConcurrentHashMap<String, String> cache = null;
    // ports number for other servers
    protected List<Integer> ports = new ArrayList<>();
    protected List<MultiplicationService.Client> clients = new ArrayList<>();


    public MultiplicationHandler(List<Integer> ports) {
        this.ports = ports;
    }

    @Override
    public Message process(Message message) throws TException {
        if (message.getOp().equals(Operation.GET)) {
            return get(message);
        } else {
            return update(message);
        }
    }

    @Override
    public Message canCommit(Message message) {
        cache = new ConcurrentHashMap(map);
        if (message.getOp() == Operation.PUT) {
            cache.put(message.getKey(), message.getValue());
        } else {
            cache.remove(message.getKey());
        }
        message.setStatus(Status.Commit);
        return message;
    }

    public Message get(Message message) {
        message.setValue(map.get(message.getKey()));
        message.setResult(message.getValue() != null ? Result.Success : Result.Fail);
        return message;
    }

    public Message update(Message message) throws TException {

        if (message.getType() == Type.Client) {
            message.setType(Type.Server);
            List<Message> results = new ArrayList<>();
            System.out.println("starting a vote for " + message.getOp() + " request: ");
            int i = 0;
//            if (ports.size() == clients.size()) {
//                for (MultiplicationService.Client c : clients) {
//                    Message res = c.canCommit(message);
//                    results.add(res);
//                    System.out.println("localhost:" + this.ports.get(i) + " say: " + res.getStatus().toString());
//                    i++;
//                }
//            } else {
            clients = new ArrayList<>();
            for (int port : ports) {
                try {
                    MultiplicationService.Client c = connectServer(port);
                    Message res = c.canCommit(message);
                    clients.add(c);
                    results.add(res);
                    System.out.println("localhost:" + this.ports.get(i) + " say: " + res.getStatus().toString());
                } catch (TException e) {
                    Message res = message.deepCopy();
                    res.setStatus(Status.Abort);
                    results.add(res);
                    System.out.println("localhost:" + this.ports.get(i) + " say: " + res.getStatus().toString());
                }
                i++;
            }
//            }

            for (Message m : results) {
                if (m.getStatus() == Status.Abort) {
                    message.setStatus(Status.Abort);
                    message.setResult(Result.Fail);
                    for (MultiplicationService.Client c : clients) {
                        c.doAbort(message);
                    }
                    return message;
                }
            }

            Message res = null;
            for (MultiplicationService.Client c : clients) {
                res = c.doCommit(message);
            }
            message.setResult(res.getResult());
            message.setValue(res.getValue());
            return message;
        }
        return message;
    }

    public MultiplicationService.Client connectServer(int port) throws TException {
        MultiplicationClient client = new MultiplicationClient("localhost", port);
        MultiplicationService.Client c = client.connect();
        return c;
    }

    @Override
    public Message doCommit(Message message) {
        switch(message.getOp()) {
            case GET:
                message.setValue(map.get(message.getKey()));
                message.setResult(message.getValue() != null ? Result.Success : Result.Fail);
                break;
            case PUT:
                map.put(message.getKey(), message.getValue());
                message.setResult(Result.Success);
                break;
            case DELETE:
                message.setResult(map.remove(message.getKey()) != null ? Result.Success : Result.Fail);
                System.out.println("delete: " + message.getResult());
                break;
        }
        message.setStatus(Status.Commit);
        return message;
    }

    @Override
    public Message doAbort(Message message) {
        this.cache = null;
        return message;
    }
}