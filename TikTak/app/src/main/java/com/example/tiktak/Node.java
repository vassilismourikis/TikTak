package com.example.tiktak;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public interface Node{

    public void init(int a);

    public List<BrokerNode> getBrokers();

    public void connect() throws IOException;

    public void disconnect();

    public void updateNodes();


}

