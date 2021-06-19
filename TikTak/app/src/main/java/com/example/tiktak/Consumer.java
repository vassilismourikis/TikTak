package com.example.tiktak;
import android.os.AsyncTask;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static android.os.Environment.getExternalStorageDirectory;

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
    private ArrayList<String> availableChannels;
    public boolean a=true;
    //public void register(Broker b,String s){}

    public ArrayList<String> getAvailableChannelsArray(){
        return availableChannels;
    }

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
            if(a){
                availableChannels=getAvailableChannels();
                System.out.println("UPDATED AVAILABLE CHANNELS");
            }
            if(big.size()== 100) break;
            a=false;
        }

        return null;
    }

    public ArrayList<String> getAvailableChannels() {
        connect();
        disconnect();
        Iterator<String> it;
        boolean b=false;
        String current = null;
        ArrayList<String> ret = new ArrayList<String>();

        if(infos.getInfos1()!=null) {
            it = infos.getInfos1().iterator();


            while (it.hasNext()) {

                current = it.next();
                connect(1);
                try {
                    out.writeObject(new Message(null, null, current, null, null, null, null, 20));
                    out.flush();
                    b = in.readBoolean();
                }
                catch (Exception e){
                    System.out.println("error at availablechannels on consumer");
                }
                if (b) ret.add(current);
                disconnect();
            }
        }
        if(infos.getInfos2()!=null) {
            it = infos.getInfos2().iterator();


            while (it.hasNext()) {

                current = it.next();
                connect(2);
                try {
                    out.writeObject(new Message(null, null, current, null, null, null, null, 20));
                    out.flush();
                    b = in.readBoolean();
                }
                catch (Exception e){
                    System.out.println("error at availablechannels on consumer");
                }
                if (b) ret.add(current);
                disconnect();
            }
        }
        if(infos.getInfos3()!=null) {
            it = infos.getInfos3().iterator();


            while (it.hasNext()) {

                current = it.next();
                connect(3);
                try {
                    out.writeObject(new Message(null, null, current, null, null, null, null, 20));
                    out.flush();
                    b = in.readBoolean();
                }
                catch (Exception e){
                    System.out.println("error at availablechannels on consumer");
                }
                if (b) ret.add(current);
                disconnect();
            }
        }
        return  ret;
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
            File myObj = new File(getExternalStorageDirectory().getAbsolutePath()+"/Android/media/"+"Client"+ name + "/broker.txt");
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
                //System.out.println(networks);
                //System.out.println(networks_hashes);
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
        //System.out.println(big);

        try {
            s=new ServerSocket();

            final String[] ip = {""};
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        try(final DatagramSocket socket = new DatagramSocket()){
                            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                            ip[0] = socket.getLocalAddress().getHostAddress();
                            System.out.println("CONSUMER   "+ ip[0]);
                            s.bind(new InetSocketAddress(ip[0],0));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

            thread.join(); //waits till ip is available
        } catch (IOException | InterruptedException e) {
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
