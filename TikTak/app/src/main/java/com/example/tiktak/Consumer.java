package com.example.tiktak;
import android.os.AsyncTask;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer extends AsyncTask<String, String, String> implements Node{
    private int choice=-1;
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    Random rand=new Random();
    private String name;
    public ReentrantLock lock = new ReentrantLock();
    Info infos;
    Scanner sc = new Scanner(System.in);
    ServerSocket s;
    ArrayList<String> subs;
    private ArrayList<String> networks;
    private ArrayList<String> networks_hashes;
    private ArrayList<BigInteger>  big;
    //public void register(Broker b,String s){}

    public String getConsumerName(){
        return name;
    }

    private boolean areActionsDone = true;

    public boolean getActionsDone(){
        return areActionsDone;
    }

    public void setActionsDone(Boolean b){
        areActionsDone = b;
    }


    public void disconnect(Broker b,String s){}

    public void playData(String s,Value v){}

    @Override
    protected String doInBackground(String... strings) {

        while (true) {
            if (!lock.isLocked()) {
                if (choice == 4) {
                    System.out.println("CONSUMER");
                    choice = -1;
                } else if (choice == 5) {

                    areActionsDone = false;
                    connect();
                    System.out.println("OUT OF CONNECT");
                    if (infos != null) {
                        System.out.println(infos);
                    }
                    else System.out.println("no entries yet");

                    choice = -1;
                } else if (choice == 6) {
                    areActionsDone = false;
                    choice = -1;
                    //subscribe
                }else if (choice == 0) {
                    disconnect();
                    areActionsDone = true;
                    break;
                }
            }
        }
        return null;
    }

    public void print(String s){
        System.out.println(s);
    }

    public void SelectHashtag(String HashtagChoice) { //  AKA pull
        if(!lock.isLocked()) {
            boolean f = false;
            String ch = HashtagChoice;
            System.out.println(ch);
            Iterator<String> it;
            String current;
            if(!infos.getInfos1().isEmpty()) {
                 it= infos.getInfos1().iterator();

                 current = null;
                while (it.hasNext()) {

                    current = it.next();
                    if (current.equals(ch)) {
                        f = true;
                        System.out.println("GETTING FROM BROKER1");
                        try {
                            disconnect();
                            connect(1);
                            out.writeObject(new Message(null,null,ch, name, null, null,null,  5));
                            out.flush();
                            ArrayList<String> videokeys=(ArrayList<String>) in.readObject();
                            disconnect();
                            if(videokeys.size()!= 0){
                                System.out.println("chose between: ");
                                for(int i=0;i<videokeys.size();i++){
                                    System.out.println(i + " : " +videokeys.get(i));
                                }
                                connect(1);
                                out.writeObject(new Message(null,null,videokeys.get(sc.nextInt()), null,null,null,null, 6));
                                out.flush();
                                byte[] bytee=(byte[])in.readObject();
                                File file = new File("Client"+name+"/Consumer/" +"output.mp4");
                                BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(file));

                                while(bytee!=null){
                                    fileOutput.write(bytee);
                                    bytee=(byte[])in.readObject();
                                }
                                fileOutput.close();
                                disconnect();
                                System.out.println("video transfered");
                                areActionsDone = true;//me ayto tha paixoyme gia thn anamonh stio scannerr
                            }
                            else{
                                System.out.println("NO ENTRIES");
                            }

                        }catch (Exception e){}
                    }

                }
            }
            if(!infos.getInfos2().isEmpty()) {
            it = infos.getInfos2().iterator();

            current = null;
            while (it.hasNext()) {

                current = it.next();
                if (current.equals(ch)) {
                    f = true;
                    System.out.println("GETTING FROM BROKER2");
                    try {
                        disconnect();
                        connect(2);
                        out.writeObject(new Message(null,null,ch, name, null, null,null,  5));
                        out.flush();
                        ArrayList<String> videokeys=(ArrayList<String>) in.readObject();
                        disconnect();
                        if(videokeys.size()!= 0){
                            System.out.println("chose between: ");
                            for(int i=0;i<videokeys.size();i++){
                                System.out.println(i + " : " +videokeys.get(i));
                            }
                            connect(2);
                            out.writeObject(new Message(null,null,videokeys.get(sc.nextInt()), null,null,null,null, 6));
                            out.flush();
                            byte[] bytee=(byte[])in.readObject();
                            File file = new File("Client"+name+"/Consumer/" +"output.mp4");
                            BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(file));

                            while(bytee!=null){




                                fileOutput.write(bytee);



                                bytee=(byte[])in.readObject();
                            }
                            fileOutput.close();
                            disconnect();
                            System.out.println("video transfered");
                            areActionsDone = true;//me ayto tha paixoyme gia thn anamonh stio scannerr
                        }
                        else{
                            System.out.println("NO ENTRIES");
                        }
                    }catch (Exception e){}
                }

            }
            }
            if(!infos.getInfos3().isEmpty()) {
                it = infos.getInfos3().iterator();

                current = null;
                while (it.hasNext()) {

                    current = it.next();
                    if (current.equals(ch)) {
                        f = true;
                        System.out.println("GETTING FROM BROKER3");
                        try {
                            disconnect();
                            connect(3);
                            out.writeObject(new Message(null,null,ch, name, null, null,null, 5));
                            out.flush();
                            ArrayList<String> videokeys=(ArrayList<String>) in.readObject();
                            disconnect();
                            if(videokeys.size()!= 0){
                                System.out.println("chose between: ");
                                for(int i=0;i<videokeys.size();i++){
                                    System.out.println(i + " : " +videokeys.get(i));
                                }
                                connect(3);
                                out.writeObject(new Message(null,null,videokeys.get(sc.nextInt()), null,null,null,null, 6));
                                out.flush();
                                byte[] bytee=(byte[])in.readObject();
                                File file = new File("Client"+name+"/Consumer/" +"output.mp4");
                                BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(file));

                                while(bytee!=null){




                                    fileOutput.write(bytee);



                                    bytee=(byte[])in.readObject();
                                }
                                fileOutput.close();
                                disconnect();
                                System.out.println("video transfered");
                                areActionsDone = true;//me ayto tha paixoyme gia thn anamonh stio scannerr
                            }
                            else{
                                System.out.println("NO ENTRIES");
                            }
                        }catch (Exception e){}
                    }


                }
            }
            if (!f) {
                System.out.println("hashtag/channelname not found");
                areActionsDone = true;
                choice = -1;
            }
        }
        areActionsDone = true;
        choice = -1;
        disconnect();
    }


    public Consumer(String name){
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
        big = new ArrayList<BigInteger>();
        big.add(new BigInteger(networks_hashes.get(0)));
        big.add(new BigInteger(networks_hashes.get(1)));
        big.add(new BigInteger(networks_hashes.get(2)));
        Collections.sort(big);
        System.out.println(big);

        try {
            s=new ServerSocket();

            String ip="";
            try(final DatagramSocket socket = new DatagramSocket()){
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
            }
            System.out.println("CONSUMER   "+ ip);
            s.bind(new InetSocketAddress(ip,0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread t = new ConsumerThread(this);
        t.start();
        this.subs=new ArrayList<String>();
        this.name=name;
    }

    public void setChoice(int c){
        this.choice=c;
    }

    @Override
    public void init(int a) {

    }

    @Override
    public List<BrokerNode> getBrokers() {
        return null;
    }



    @Override
    public void connect() {
        int br= rand.nextInt(3);// 0-2
        try {
            requestSocket = new Socket(networks.get(br), 64287 + br);
            //Obtain Socket’s OutputStream and use it to initialize ObjectOutputStream
            out = new ObjectOutputStream(requestSocket.getOutputStream());

            //Obtain Socket’s InputStream and use it to initialize ObjectInputStream
            in = new ObjectInputStream(requestSocket.getInputStream());
            out.writeObject(new Message(new ConsumerInfo(name,subs,s.getInetAddress().getHostAddress(),s.getLocalPort()),null,name,null,null,null,null, 4));
            out.flush();

            infos= (Info) in.readObject();
        }
        catch(Exception e){
            System.out.println("error on broker connection" + e);
        }
    }

    public void connect(int i) {

        try {
            requestSocket = new Socket(networks.get(i-1), 64286 +i );
            //Obtain Socket’s OutputStream and use it to initialize ObjectOutputStream
            out = new ObjectOutputStream(requestSocket.getOutputStream());

            //Obtain Socket’s InputStream and use it to initialize ObjectInputStream
            in = new ObjectInputStream(requestSocket.getInputStream());

        }
        catch(Exception e){
            System.out.println("error on broker connection" + e);
        }
    }

    public void subscribe(String s){
        System.out.println(s);
        subs.add(s);

        try {
            connect(1);
            out.writeObject(new Message(new ConsumerInfo(name,subs,this.s.getInetAddress().getHostAddress(),this.s.getLocalPort()),null,name,null,null,null,null, 4));
            out.flush();

            infos= (Info) in.readObject();
            disconnect();
            connect(2);
            out.writeObject(new Message(new ConsumerInfo(name,subs,this.s.getInetAddress().getHostAddress(),this.s.getLocalPort()),null,name,null,null,null,null, 4));
            out.flush();

            infos= (Info) in.readObject();
            disconnect();
            connect(3);
            out.writeObject(new Message(new ConsumerInfo(name,subs,this.s.getInetAddress().getHostAddress(),this.s.getLocalPort()),null,name,null,null,null,null, 4));
            out.flush();

            infos= (Info) in.readObject();
            disconnect();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        areActionsDone = true;
    }

    @Override
    public void disconnect() {
        try {
            requestSocket.close();
            out.close();
            in.close();
        }
        catch (Exception e){}

    }

    @Override
    public void updateNodes() {

    }

    public ServerSocket getServer() {
        return s;
    }
}