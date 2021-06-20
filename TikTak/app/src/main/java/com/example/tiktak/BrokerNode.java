package com.example.tiktak;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;



public class BrokerNode implements Broker{
    private List<ConsumerInfo> registeredUsers=new LinkedList<ConsumerInfo>();
    private List<PublisherInfo> registeredPublishers=new LinkedList<PublisherInfo>();
    private TreeSet<String> hashtags=new TreeSet<String>(); //treeset for no duplicates
    private TreeSet<String> channelnames=new TreeSet<String>();
    private HashMap<String,ArrayList<String>> hashtag_videokey= new HashMap<String,ArrayList<String>>();
    private HashMap<String,ArrayList<String>> channelName_videokey= new HashMap<String,ArrayList<String>>();
    private ServerSocket requestSocket,privateSocket = null;
    private BigInteger brokerLimit;
    private Socket s;
    private String brokerToConnect;
    public String ip;
    public int port;
    private Container[] brokersData;
    //ροή για να στέλνεις δεδομένα στον διακομιστή
    ObjectOutputStream out = null;
    //ροή για να παίρνεις δεδομένα από τον διακομιστή
    ObjectInputStream in = null;
    private static  BrokerNode broker;
    private ArrayList<String> networks;
    private ArrayList<String> networks_hashes;
    private ArrayList<BigInteger>  big;


    public BrokerNode(int number,int port){
networks=new ArrayList<String>();
networks_hashes=new ArrayList<String>();
        try {
            File myObj = new File("broker.txt");
            Scanner myReader = new Scanner(myObj);
            int counter=0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(counter==0 || counter==3 || counter==6){
                    networks.add(data);
                }
                if(counter==1|| counter==4 || counter==7){
                    networks_hashes.add(data);
                }
                System.out.println(networks);
                System.out.println(networks_hashes);
                counter++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            if(number==1) {
                this.ip = networks.get(0);
                this.port = port;
                this.brokerLimit=new BigInteger(networks_hashes.get(0));
            }
            else if(number==2){
                this.ip = networks.get(1);
                this.port = port;
                this.brokerLimit=new BigInteger(networks_hashes.get(1));
            }
            else{
                this.ip = networks.get(2);
                this.port = port;
                this.brokerLimit=new BigInteger(networks_hashes.get(2));
            }
            requestSocket = new ServerSocket(port,50,InetAddress.getByName(ip));//SERVERSOCKET FOR COMMUNICATION WITH CLIENTS


            privateSocket = new ServerSocket(64284,10,InetAddress.getByName(ip));//SERVERSOCKET FOR COMMUNICATION WITH OTHER BROKERS
            //privateSocket = new ServerSocket(64285,10,InetAddress.getByName(ip));
            //privateSocket = new ServerSocket(64286,10,InetAddress.getByName(ip));


            System.out.println(port);
            brokersData=new Container[3];

        }
        catch (Exception e){
            System.out.println(e+ " "+port);
        }

        big = new ArrayList<BigInteger>();
        big.add(new BigInteger(networks_hashes.get(0)));
        big.add(new BigInteger(networks_hashes.get(1)));
        big.add(new BigInteger(networks_hashes.get(2)));
        Collections.sort(big);
        System.out.println(big);

    }

    public boolean isChannelName(String s){
        return channelnames.contains(s);
    }

    public static  void main(String args[]){

        Socket connection;


        broker=new BrokerNode(1,64287);//Broker 1
        //broker=new BrokerNode(2,64288);//Broker 2
        //broker=new BrokerNode(3,64289);//Broker 3


        BrokerThread t = new BrokerThread(broker);


        t.execute();

        while(true){
            try {
                //waiting for client
                connection = broker.getSocket().accept();
                //Πολυνημάτωση: Για κάθε πελάτη, ξεκίνα νέο νήμα, και συνέχισε να δέχεσαι αιτήσεις
                ActionsForClients t1 = new ActionsForClients(connection,broker);
                //με την start ξεκινάει το νέο νήμα
                // και εκτελείται η .run μέθοδος
                // Το νέο νήμα παίρνει σαν είσοδο στον constructor του
                //Το αντικείμενο connection που πρόκειται για την υποδοχή της συγκεκριμένης σύνδεσης με τον πελάτη
                t1.execute();
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void updateVideos(PublisherInfo p, String key){
        System.out.println(registeredPublishers.size()+  "FIRST");
        if(registeredPublishers.size()==0){
            System.out.println(registeredPublishers.size() + "IN SIZE ==0");
            registeredPublishers.add(p);
            return;
        }
        boolean found=true;
        for(int i=0;i<registeredPublishers.size();i++){
            System.out.println(registeredPublishers.size() + "INTO FOR" );
            if(registeredPublishers.get(i).channelName.equals(p.channelName)){
                System.out.println("updated publishers info");
                found=false;
                registeredPublishers.set(i,p);
                break;
            }
        }
        if(found)registeredPublishers.add(p);

        for(int i=0;i<hashtag_videokey.size();i++) {
            String firstKey = (String)hashtag_videokey.keySet().toArray()[i];
            ArrayList<String> ar=hashtag_videokey.get(firstKey);
            for(int j=0;j<ar.size();j++){
                if(ar.get(j).equals(key))ar.remove(j);
            }
            if(firstKey.equals(key)) {
                hashtag_videokey.put(firstKey,ar);
            }
        }

        for(int i=0;i<channelName_videokey.size();i++) {
            String firstKey = (String)channelName_videokey.keySet().toArray()[i];
            ArrayList<String> ar=channelName_videokey.get(firstKey);
            for(int j=0;j<ar.size();j++){
                if(ar.get(j).equals(key))ar.remove(j);
            }
            if(firstKey.equals(key)) {
                 channelName_videokey.put(firstKey,ar);
            }
        }

    }

    public TreeSet<String> getHashtags(){
        return this.hashtags;
    }

    public TreeSet<String> getChannelNames(){
        return this.channelnames;
    }

    public void addChannelName(String channelname){
        channelnames.add(channelname);
    }

    public void registerPublisher(PublisherInfo p){
        this.registeredPublishers.add(p);
    }

    public Info registerConsumer(ConsumerInfo s){
        boolean t=false;
        for(int i=0;i<registeredUsers.size();i++){
            if(s.channelName.equals(registeredUsers.get(i).channelName)){
                registeredUsers.set(i,s);
                t=true;
            }
        }
        if(!t){
            this.registeredUsers.add(s);
        }
        return new Info(brokersData);

    }

    public void updateBrokersData(Container c, String port){
        //System.out.println("port:            "+ port);
        if(port.equals("64287")){
            //System.out.println("saved to 0 ");
            brokersData[0]=c;
        }
        else if(port.equals("64288")){
            //System.out.println("saved to 1 ");
            brokersData[1]=c;
        }
        else if(port.equals("64289")){
            //System.out.println("saved to 2 ");
            brokersData[2]=c;
        }

    }

    public ServerSocket getBrokerSocket(){
        return this.privateSocket;
    }
    public ServerSocket getSocket(){
        return this.requestSocket;
    }


    public synchronized void saveHashtag(Message m) throws IOException {


        if(new BigInteger(m.getA()).compareTo(big.get(0))<=0){
            brokerToConnect=big.get(0).toString();
            if(big.get(0).compareTo(brokerLimit)==0) {
                //System.out.println(m.getA() + "     " + "IF" + "     " + m.getB());
                if( hashtag_videokey.get(m.getA())==null){
                    ArrayList<String>updatedList= new ArrayList<String>();
                    updatedList.add(m.getB());
                    hashtag_videokey.put(m.getA(), updatedList);
                }
                else {
                    ArrayList<String> updatedList = hashtag_videokey.get(m.getA());
                    updatedList.add(m.getB());
                    hashtag_videokey.put(m.getA(), updatedList);
                }
                hashtags.add(m.getA1());
                brokersData[2]=new Container(hashtags,channelnames,port);
                Refresher t= new Refresher(brokersData[2],broker);
                t.execute();
                //System.out.println("saved at broker with port: " + "                     " + privateSocket.getLocalPort() + "!!!!!!!!!!!!!!!!!!!");
                //NOTIFY THE REGISTERED UDERS------------------------------------------------------------------------------------------------------------------------
                for(int i=0;i<registeredUsers.size();i++){
                    for(int j=0;j<registeredUsers.get(i).getSubs().size();j++){
                        //System.out.println(registeredUsers.get(i).getSubs().get(j)  + "    "+ m.getA1());
                        if(registeredUsers.get(i).getSubs().get(j).equals(m.getA1())){
                            System.out.println("FOUND USER FOR NOTIFICATION");
                            connectToConsumer(registeredUsers.get(i));
                            out.writeObject(new Message(null,null,"NEW VIDEO TO: " +m.getA1(),m.getPublisher().channelName,null,null,null,15));
                            out.flush();
                            disconnect();
                        }
                    }
                }

                //NOTIFY THE REGISTERED UDERS---------------------------------------------------------------------------------------------------------------------
            }
            else{
                connect();
                //System.out.println(m.getA()+"     "+"ELSE"+"     "+m.getB());
                out.writeObject(new Message(null,null,m.getA(),m.getB(),m.getA1(),m.getB1(),m.getPublisher(),1));
                out.flush();
                disconnect();
            }
        }
        else if(new BigInteger(m.getA()).compareTo(big.get(1))<=0){
            brokerToConnect=big.get(1).toString();
            if(big.get(1).compareTo(brokerLimit)==0) {
                //System.out.println(m.getA() + "     " + "IF" + "     " + m.getB());
                if( hashtag_videokey.get(m.getA())==null){
                    ArrayList<String>updatedList= new ArrayList<String>();
                    updatedList.add(m.getB());
                    hashtag_videokey.put(m.getA(), updatedList);
                }
                else {
                    ArrayList<String> updatedList = hashtag_videokey.get(m.getA());
                    updatedList.add(m.getB());
                    hashtag_videokey.put(m.getA(), updatedList);
                }
                hashtags.add(m.getA1());
                brokersData[1]=new Container(hashtags,channelnames,port);
                Refresher t= new Refresher(brokersData[1],broker);
                t.execute();
                //System.out.println("saved at broker with port: " + "                     " + privateSocket.getLocalPort() + "!!!!!!!!!!!!!!!!!!!");
                //NOTIFY THE REGISTERED UDERS------------------------------------------------------------------------------------------------------------------------
                for(int i=0;i<registeredUsers.size();i++){
                    for(int j=0;j<registeredUsers.get(i).getSubs().size();j++){
                        //System.out.println(registeredUsers.get(i).getSubs().get(j)  + "    "+ m.getA1());
                        if(registeredUsers.get(i).getSubs().get(j).equals(m.getA1())){
                            System.out.println("FOUND USER FOR NOTIFICATION");
                            connectToConsumer(registeredUsers.get(i));
                            out.writeObject(new Message(null,null,"NEW VIDEO TO: " +m.getA1(),m.getPublisher().channelName,null,null,null,15));
                            out.flush();
                            disconnect();
                        }
                    }
                }

                //NOTIFY THE REGISTERED UDERS---------------------------------------------------------------------------------------------------------------------
            }
            else{
                connect();
                //System.out.println(m.getA()+"     "+"ELSE"+"     "+m.getB());
                out.writeObject(new Message(null,null,m.getA(),m.getB(),m.getA1(),m.getB1(),m.getPublisher(),1));
                out.flush();
                disconnect();
            }

        }
        else{
            brokerToConnect=big.get(2).toString();
            if(big.get(2).compareTo(brokerLimit)==0) {
                //System.out.println(m.getA() + "     " + "IF" + "     " + m.getB());
                if( hashtag_videokey.get(m.getA())==null){
                    ArrayList<String>updatedList= new ArrayList<String>();
                    updatedList.add(m.getB());
                    hashtag_videokey.put(m.getA(), updatedList);
                }
                else {
                    ArrayList<String> updatedList = hashtag_videokey.get(m.getA());
                    updatedList.add(m.getB());
                    hashtag_videokey.put(m.getA(), updatedList);
                }
                hashtags.add(m.getA1());
                brokersData[0]=new Container(hashtags,channelnames,port);
                Refresher t= new Refresher(brokersData[0],broker);
                t.execute();
                //System.out.println("saved at broker with port: " + "                     " + privateSocket.getLocalPort() + "!!!!!!!!!!!!!!!!!!!");
                //NOTIFY THE REGISTERED UDERS------------------------------------------------------------------------------------------------------------------------
                for(int i=0;i<registeredUsers.size();i++){
                    for(int j=0;j<registeredUsers.get(i).getSubs().size();j++){
                        //System.out.println(registeredUsers.get(i).getSubs().get(j)  + "    "+ m.getA1());
                        if(registeredUsers.get(i).getSubs().get(j).equals(m.getA1())){
                            System.out.println("FOUND USER FOR NOTIFICATION");
                            connectToConsumer(registeredUsers.get(i));
                            out.writeObject(new Message(null,null,"NEW VIDEO TO: " +m.getA1(),m.getPublisher().channelName,null,null,null,15));
                            out.flush();
                            disconnect();
                        }
                    }
                }

                //NOTIFY THE REGISTERED UDERS---------------------------------------------------------------------------------------------------------------------
            }
            else{
                connect();
                //System.out.println(m.getA()+"     "+"ELSE"+"     "+m.getB());
                out.writeObject(new Message(null,null,m.getA(),m.getB(),m.getA1(),m.getB1(),m.getPublisher(),1));
                out.flush();
                disconnect();
            }
        }
        //UPDATE REGISTERED PUBLISHERS--------------------------------------------------------------------------------------------------------------------------------
        System.out.println(registeredPublishers.size()+  "FIRST");
        if(registeredPublishers.size()==0){
            System.out.println(registeredPublishers.size() + "IN SIZE ==0");
            registeredPublishers.add(m.getPublisher());
            return;
        }
        boolean found=true;
        for(int i=0;i<registeredPublishers.size();i++){
            System.out.println(registeredPublishers.size() + "INTO FOR" );
            if(registeredPublishers.get(i).channelName.equals(m.getPublisher().channelName)){
                System.out.println("updated publishers info");
                found=false;
                registeredPublishers.set(i,m.getPublisher());
                break;
            }
        }
        if(found)registeredPublishers.add(m.getPublisher());
        //UPDATE REGISTERED PUBLISHERS--------------------------------------------------------------------------------------------------------------------------------




    }

    public synchronized void saveChannelName(Message m) throws IOException {


        if(new BigInteger(m.getA()).compareTo(big.get(0))<=0){
            brokerToConnect=big.get(0).toString();
            if(big.get(0).compareTo(brokerLimit)==0) {
                //System.out.println(m.getA() + "     " + "IF" + "     " + m.getB());
                if( channelName_videokey.get(m.getA())==null){
                    ArrayList<String>updatedList= new ArrayList<String>();
                    updatedList.add(m.getB());
                    channelName_videokey.put(m.getA(), updatedList);
                }
                else {
                    ArrayList<String> updatedList = channelName_videokey.get(m.getA());
                    updatedList.add(m.getB());
                    channelName_videokey.put(m.getA(), updatedList);
                }
                channelnames.add(m.getA1());
                brokersData[2]=new Container(hashtags,channelnames,port);
                Refresher t= new Refresher(brokersData[2],broker);
                t.execute();
                //System.out.println("saved at broker with port: " + "                     " + privateSocket.getLocalPort() + "!!!!!!!!!!!!!!!!!!!");
                //NOTIFY THE REGISTERED UDERS------------------------------------------------------------------------------------------------------------------------
                for(int i=0;i<registeredUsers.size();i++){
                    for(int j=0;j<registeredUsers.get(i).getSubs().size();j++){
                        //System.out.println(registeredUsers.get(i).getSubs().get(j)  + "    "+ m.getA1());
                        if(registeredUsers.get(i).getSubs().get(j).equals(m.getA1())){
                            System.out.println("FOUND USER FOR NOTIFICATION");
                            connectToConsumer(registeredUsers.get(i));
                            out.writeObject(new Message(null,null,"NEW VIDEO TO: " +m.getA1(),m.getPublisher().channelName,null,null,null,15));
                            out.flush();
                            disconnect();
                        }
                    }
                }

                //NOTIFY THE REGISTERED UDERS---------------------------------------------------------------------------------------------------------------------
            }
            else{
                connect();
                //System.out.println(m.getA()+"     "+"ELSE"+"     "+m.getB());
                out.writeObject(new Message(null,null,m.getA(),m.getB(),m.getA1(),m.getB1(),m.getPublisher(),2));
                out.flush();
                disconnect();
            }
        }
        else if(new BigInteger(m.getA()).compareTo(big.get(1))<=0){
            brokerToConnect=big.get(1).toString();
            if(big.get(1).compareTo(brokerLimit)==0) {
                //System.out.println(m.getA() + "     " + "IF" + "     " + m.getB());
                if( channelName_videokey.get(m.getA())==null){
                    ArrayList<String>updatedList= new ArrayList<String>();
                    updatedList.add(m.getB());
                    channelName_videokey.put(m.getA(), updatedList);
                }
                else {
                    ArrayList<String> updatedList = channelName_videokey.get(m.getA());
                    updatedList.add(m.getB());
                    channelName_videokey.put(m.getA(), updatedList);
                }
                channelnames.add(m.getA1());
                brokersData[1]=new Container(hashtags,channelnames,port);
                Refresher t= new Refresher(brokersData[1],broker);
                t.execute();
                //System.out.println("saved at broker with port: " + "                     " + privateSocket.getLocalPort() + "!!!!!!!!!!!!!!!!!!!");
                //NOTIFY THE REGISTERED UDERS------------------------------------------------------------------------------------------------------------------------
                for(int i=0;i<registeredUsers.size();i++){
                    for(int j=0;j<registeredUsers.get(i).getSubs().size();j++){
                        //System.out.println(registeredUsers.get(i).getSubs().get(j)  + "    "+ m.getA1());
                        if(registeredUsers.get(i).getSubs().get(j).equals(m.getA1())){
                            System.out.println("FOUND USER FOR NOTIFICATION");
                            connectToConsumer(registeredUsers.get(i));
                            out.writeObject(new Message(null,null,"NEW VIDEO TO: " +m.getA1(),m.getPublisher().channelName,null,null,null,15));
                            out.flush();
                            disconnect();
                        }
                    }
                }

                //NOTIFY THE REGISTERED UDERS---------------------------------------------------------------------------------------------------------------------
            }
            else{
                connect();
                //System.out.println(m.getA()+"     "+"ELSE"+"     "+m.getB());
                out.writeObject(new Message(null,null,m.getA(),m.getB(),m.getA1(),m.getB1(),m.getPublisher(),2));
                out.flush();
                disconnect();
            }

        }
        else{
            brokerToConnect=big.get(2).toString();
            if(big.get(2).compareTo(brokerLimit)==0) {
                //System.out.println(m.getA() + "     " + "IF" + "     " + m.getB());
                if( channelName_videokey.get(m.getA())==null){
                    ArrayList<String>updatedList= new ArrayList<String>();
                    updatedList.add(m.getB());
                    channelName_videokey.put(m.getA(), updatedList);
                }
                else {
                    ArrayList<String> updatedList = channelName_videokey.get(m.getA());
                    updatedList.add(m.getB());
                    channelName_videokey.put(m.getA(), updatedList);
                }
                channelnames.add(m.getA1());
                brokersData[0]=new Container(hashtags,channelnames,port);
                Refresher t= new Refresher(brokersData[0],broker);
                t.execute();
                //System.out.println("saved at broker with port: " + "                     " + privateSocket.getLocalPort() + "!!!!!!!!!!!!!!!!!!!");
                //NOTIFY THE REGISTERED UDERS------------------------------------------------------------------------------------------------------------------------
                for(int i=0;i<registeredUsers.size();i++){
                    for(int j=0;j<registeredUsers.get(i).getSubs().size();j++){
                        //System.out.println(registeredUsers.get(i).getSubs().get(j)  + "    "+ m.getA1());
                        if(registeredUsers.get(i).getSubs().get(j).equals(m.getA1())){
                            System.out.println("FOUND USER FOR NOTIFICATION");
                            connectToConsumer(registeredUsers.get(i));
                            out.writeObject(new Message(null,null,"NEW VIDEO TO: " +m.getA1(),m.getPublisher().channelName,null,null,null,15));
                            out.flush();
                            disconnect();
                        }
                    }
                }

                //NOTIFY THE REGISTERED UDERS---------------------------------------------------------------------------------------------------------------------
            }
            else{
                connect();
                //System.out.println(m.getA()+"     "+"ELSE"+"     "+m.getB());
                out.writeObject(new Message(null,null,m.getA(),m.getB(),m.getA1(),m.getB1(),m.getPublisher(),2));
                out.flush();
                disconnect();

            }
        }

//        System.out.println(registeredPublishers.size()+  "FIRST");
//        if(registeredPublishers.size()==0){
//            System.out.println(registeredPublishers.size() + "IN SIZE ==0");
//            registeredPublishers.add(m.getPublisher());
//            return;
//        }
//        boolean found=true;
//        for(int i=0;i<registeredPublishers.size();i++){
//            System.out.println(registeredPublishers.size() + "INTO FOR" );
//            if(registeredPublishers.get(i).channelName.equals(m.getPublisher().channelName)){
//                found=false;
//                registeredPublishers.remove(i);
//                registeredPublishers.add(m.getPublisher());
//                break;
//            }
//        }
//        if(found)registeredPublishers.add(m.getPublisher());


    }

    public BigInteger getBrokerLimit(){
        return brokerLimit;
    }

    public void init(int a){}

    @Override
    public List<BrokerNode> getBrokers() {
        return null;
    }

    //public List<BrokerNode> getBrokers(){
    //    return brokers;
    //}



    public void connectToConsumer(ConsumerInfo c) {
        try{
            s = new Socket(c.getAddress(), c.getPort());

            //Obtain Socket’s OutputStream and use it to initialize ObjectOutputStream
            out = new ObjectOutputStream(s.getOutputStream());

            //Obtain Socket’s InputStream and use it to initialize ObjectInputStream
            in = new ObjectInputStream(s.getInputStream());

        }catch (IOException e){
            System.out.println(e + "On connection");
        }
    }



    public void connect(){ //connectswith the other brokers
       // System.out.println("MY PORTTTTTTTTT "+privateSocket.getLocalPort());
        if(brokerToConnect.equals(big.get(0).toString()))
        {
            try{
                for(int i=0;i<networks_hashes.size();i++){
                    if(networks_hashes.get(i).equals(big.get(0).toString())){
                        s = new Socket(InetAddress.getByName(networks.get(i)), 64286); //to find the right ip from the not sorted in paralle networks array
                        break;
                    }
                }


                //Obtain Socket’s OutputStream and use it to initialize ObjectOutputStream
                out = new ObjectOutputStream(s.getOutputStream());

                //Obtain Socket’s InputStream and use it to initialize ObjectInputStream
                in = new ObjectInputStream(s.getInputStream());

            }catch (IOException e){
                System.out.println(e + "On connection");
            }

        }
        else if(brokerToConnect.equals(big.get(1).toString())) {
            try{
                for(int i=0;i<networks_hashes.size();i++){
                    if(networks_hashes.get(i).equals(big.get(1).toString())){
                        s = new Socket(InetAddress.getByName(networks.get(i)), 64285); //to find the right ip from the not sorted in paralle networks array
                        break;
                    }
                }

                //Obtain Socket’s OutputStream and use it to initialize ObjectOutputStream
                out = new ObjectOutputStream(s.getOutputStream());

                //Obtain Socket’s InputStream and use it to initialize ObjectInputStream
                in = new ObjectInputStream(s.getInputStream());

            }catch (IOException e){
                System.out.println(e + "On connection");
            }



        }
        else {
            try{
                for(int i=0;i<networks_hashes.size();i++){
                    if(networks_hashes.get(i).equals(big.get(2).toString())){
                        s = new Socket(InetAddress.getByName(networks.get(i)), 64284); //to find the right ip from the not sorted in paralle networks array
                        break;
                    }
                }

                //Obtain Socket’s OutputStream and use it to initialize ObjectOutputStream
                out = new ObjectOutputStream(s.getOutputStream());

                //Obtain Socket’s InputStream and use it to initialize ObjectInputStream
                in = new ObjectInputStream(s.getInputStream());

            }catch (IOException e){
                System.out.println(e + "On connection");
            }



        }

    }

    public void disconnect(){
        try {
            s.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println(e);
        }

    }

    public void updateNodes(){}



    public ArrayList<String> getRelatedVideos(String s,String channelName){
        //ArrayList<String> videokeys=new ArrayList<String>();
        String hashh="";
        String hash = MD5.getMd5(s);
        try {
            byte[] md5hex = MessageDigest.getInstance("MD5").digest(hash.getBytes());
            hashh=(new BigInteger(MD5.bytesToHex(md5hex),16).mod(big.get(2))).toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(hashtag_videokey.get(hashh)!=null){
            return hashtag_videokey.get(hashh);

        }
        else {
            return channelName_videokey.get(hashh);
        }

    }

    public void calculateKeys(){}

    public Client acceptConnection(Client p){return new Client("");}

    public void notifyPublisher(String s){}

    public void notifyBrokersOnChanges(){}

    public synchronized byte[] pull(String videokey,int u) {
        byte[] b = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Socket pubConnect = null;
        try {
        for (int i = 0; i < registeredPublishers.size(); i++) {
            if (registeredPublishers.get(i).getVideos().get(videokey) != null){ //for not to search on every publisher for video thjat does not exissts !!!!
                if (registeredPublishers.get(i).getVideos().get(videokey).getVideo().getVideokey().equals(videokey)) {


                    pubConnect = new Socket(String.valueOf(registeredPublishers.get(i).getAddress()), registeredPublishers.get(i).getPort());

                    out = new ObjectOutputStream(pubConnect.getOutputStream());
                    //out: για γράψιμο στον πελάτη

                    in = new ObjectInputStream(pubConnect.getInputStream());

                    out.writeObject(new Message(null,null, videokey, this.ip, String.valueOf(this.port), String.valueOf(u), null, -1));
                    out.flush();


                    try {

                        b = (byte[]) in.readObject();
                        System.out.println(registeredPublishers.size() + "    " + i);
                        return b;

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    public void filterConsumers(){}

}
