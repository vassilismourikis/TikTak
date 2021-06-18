package com.example.tiktak;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static android.os.Environment.getExternalStorageDirectory;

public class Publisher extends AsyncTask<String, String, String> implements Node{
    private ChannelName channelName;
    private int choice=-1;
    private String channelNameHash;
    Random rand = new Random();
    public ReentrantLock lock = new ReentrantLock();
    private Map<String,Value> videos = new HashMap<String,Value>();
    //υποδοχή για επικοινωνία με διακομιστή
    private Socket requestSocket = null;
    //ροή για να στέλνεις δεδομένα στον διακομιστή
    private ObjectOutputStream out = null;
    //ροή για να παίρνεις δεδομένα από τον διακομιστή
    private ObjectInputStream in = null;
    private ServerSocket server;
    private ArrayList<String> networks;
    private ArrayList<String> networks_hashes;
    private ArrayList<BigInteger>  big;
    private boolean areActionsDone = true;




    public Publisher(String s){
        this.channelName=new ChannelName(s);
        networks=new ArrayList<String>();
        networks_hashes=new ArrayList<String>();

        try {
            File myObj = new File(getExternalStorageDirectory().getAbsolutePath()+"/Android/media/"+"Client"+channelName.toString() + "/broker.txt");
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




        String hashh="";
        String hash =MD5.getMd5(channelName.toString());
        try {
            byte[] md5hex = MessageDigest.getInstance("MD5").digest(hash.getBytes());
            channelNameHash=(new BigInteger(MD5.bytesToHex(md5hex),16).mod(big.get(2))).toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            this.server=new ServerSocket();
            //server.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(),0));
            final String[] ip = {""};
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        try(final DatagramSocket socket = new DatagramSocket()){
                            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                            ip[0] = socket.getLocalAddress().getHostAddress();
                            System.out.println("PUBLISHER   "+ ip[0]);
                            server.bind(new InetSocketAddress(ip[0],0));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

            thread.join(); //waits till ip is available
            PublisherThread t=new PublisherThread(this);
            t.execute();
            System.out.println("PUBLISHERS THREAD STARTED");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getActionsDone(){
        return areActionsDone;
    }

    public void setActionsDone(Boolean b){
        areActionsDone = b;
    }

    public ServerSocket getServer(){
        return server;
    }

    @Override
    protected String doInBackground(String... strings) {


        try {
            Log.e("YOLO","ASIFG");
            connect();
            //addHashtag("#FUNNY,#WOW,#yolo,#phososhooting,#interesting");
                while(true) {
                if(!lock.isLocked()){


                    if (choice == 1) {
                        addHashtag("#FUNNY,#WOW,#yolo,#swag");
                        //addHashtag("#FUNNY,#WOW,#yolo");
                        //addHashtag("#FUNNY,#WOW,#yolo,#phososhooting,#interesting");
                        choice=-1;

                    } else if (choice == 2) {

                        areActionsDone = false;
                        deleteVideo();

                        choice=-1;
                    }else if (choice == 0){

                        disconnect();
                        break;
                    }
                }
                }

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }
    public synchronized void setChoice(int c){
        this.choice=c;

    }

    public void deleteVideo(){
        System.out.println("Select a video to delete from 0 to n");
        System.out.println(videos);
    }

    public void deleteActualVideo(String videoInt){
        System.out.println("Publisher video to delete = " + Integer.parseInt(videoInt));
        String key = (String)videos.keySet().toArray()[Integer.parseInt(videoInt)];
        Value value = videos.remove(key);
        System.out.println(value);

        try {//connect to every broker to delete videokey from the hashtag-videokey hashmap
            connect(1);
            out.writeObject(new Message(null,null,key, null, null, null, new PublisherInfo(channelName.toString(),videos,server.getInetAddress().getHostAddress(),server.getLocalPort()),7));//update publisgerinfo
            out.flush();
            disconnect();
            connect(2);
            out.writeObject(new Message(null,null,key, null, null, null, new PublisherInfo(channelName.toString(),videos,server.getInetAddress().getHostAddress(),server.getLocalPort()),7));//update publisgerinfo
            out.flush();
            disconnect();
            connect(3);
            out.writeObject(new Message(null,null,key, null, null, null, new PublisherInfo(channelName.toString(),videos,server.getInetAddress().getHostAddress(),server.getLocalPort()),7));//update publisgerinfo
            out.flush();
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        areActionsDone = true;
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
            System.out.println(networks.get(br) + (64287+br));
            requestSocket = new Socket(networks.get(br), 64287+br);
            //Obtain Socket’s OutputStream and use it to initialize ObjectOutputStream
            out = new ObjectOutputStream(requestSocket.getOutputStream());

            //Obtain Socket’s InputStream and use it to initialize ObjectInputStream
            in = new ObjectInputStream(requestSocket.getInputStream());
        }
        catch(Exception e){
            System.out.println("error on broker connectionn" + e);
        }
    }



    public void connect(int i) {

        try {
            requestSocket = new Socket(networks.get(i-1), 64286+i);
            //Obtain Socket’s OutputStream and use it to initialize ObjectOutputStream
            out = new ObjectOutputStream(requestSocket.getOutputStream());

            //Obtain Socket’s InputStream and use it to initialize ObjectInputStream
            in = new ObjectInputStream(requestSocket.getInputStream());
        }
        catch(Exception e){
            System.out.println("error on broker connectionn" + e);
        }
    }

    @Override
    public void disconnect() {
        try{
            System.out.println("DISCONNECTED PUBLISHER");
            in.close();
            out.close();
            requestSocket.close();
        }
        catch(Exception e){
            System.out.println("ERROR AT CLOSING CONNECTION");
        }


    }

    @Override
    public void updateNodes() {

    }



    public void removeHashTag(String hashtag) {

    }


    public void getBrokerList() {

    }


    public Broker hashTopic(String hash) {
        return null;
    }


    public synchronized void addHashtag(String s) throws IOException {
//FINDS THE INDIVIDUAL HASHTAGS FROM THE USER'S INPUT-------------------------------------------------------------------
        ArrayList<String> hashtags = new ArrayList<String>();
        //"#FUNNY,#WOW,...
        String hashtag="";
        int i=0;
        while(i<s.length()){
            if(s.charAt(i)=='#'){
                while(s.charAt(i)!=',' && i<s.length()-1) {
                    if(s.charAt(i+1)!=',') hashtag += s.charAt(i+1);

                    i++;
                }
                hashtags.add(hashtag);
                hashtag="";
            }
            i++;
        }
//FINDS THE INDIVIDUAL HASHTAGS FROM THE USER'S INPUT-------------------------------------------------------------------
        //GENERATES NEW VIDEOKEY AND PUSHES THE HASHTAGS TO THE CORRECT BROKER ONE BY ONE----------------------------------------------------------------------------
        Random r= new Random();

        String hashtagss = "";
        for(int j=0;j<hashtags.size();j++){
            hashtagss+=hashtags.get(j);
        }
        System.out.println(hashtagss);
        String videokey=MD5.getMd5(hashtagss+r.nextInt());
        System.out.println("Your HashCode Generated by MD5 is: " +videokey); //IDNEPENDENT VIDEO KEY THHROUGH ALL THE HASHTAGS

        connect(); //ADDING TO PUBLISHER'S VIDEOS
        out.writeObject(new Message(null,null,channelNameHash,videokey,channelName.toString(),hashtagss,new PublisherInfo(channelName.toString(),videos,server.getInetAddress().getHostAddress(),server.getLocalPort()),2));
        out.flush();
        try {
            push(videokey,hashtags);
        }
        catch(Exception e){
            System.out.println("ERRR ON PUSH  "+ e);
        }
        disconnect();
        for(int k=0;k<hashtags.size();k++){

            connect();

            String hashh="";
            String hash =MD5.getMd5(hashtags.get(k));
            try {
                byte[] md5hex = MessageDigest.getInstance("MD5").digest(hash.getBytes());
                hashh=(new BigInteger(MD5.bytesToHex(md5hex),16).mod(big.get(2))).toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            System.out.println("videokey:"+videokey + "hashtags:" + hashh );



            //SAVES HASHTAG-VIDEOKEY TUPLE----------------------------------------------------------------------------------------
            try {
                //Χρήση της μεθόδου writeObject του αντικειμένου ObjectOutputStream για να στείλει ένα Message Object στον διακομιστή
                out.writeObject(new Message(null,null,hashh,videokey,hashtags.get(k),hashtagss,new PublisherInfo(channelName.toString(),videos,server.getInetAddress().getHostAddress(),server.getLocalPort()),1));

                //flush output buffer to send data
                out.flush();

            }
            catch (Exception e){
                System.out.println("ERROR "+ e );
            }

            //SAVES HASHTAG-VIDEOKEY TUPLE----------------------------------------------------------------------------------------



        }
        //GENERATES NEW VIDEOKEY AND PUSHES THE HASHTAGS TO THE CORRECT BROKER ONE BY ONE-------------------------------------------------------------------------------


        disconnect();



    }


    public MediaMetadataRetriever readMetaData() {
        File dir = getExternalStorageDirectory();
        String path = dir.getAbsolutePath();
        File  file = new File(path,"/Android/media/"+"Client"+"Vasilis"+"/Publisher/minivideo.mp4");

        if (file.exists()) {
            Log.d("TAG", ".mp4 file Exist");

            //Added in API level 10
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(file.getAbsolutePath());
                return retriever;


            } catch (Exception e) {
                Log.d("TAG", "Exception : " + e.getMessage());
                return null;
            }
        } else {
            Log.d("TAG", ".mp4 file doesn´t exist.");
            return null;
        }
    }


    public synchronized void push(String s,ArrayList<String> hashtags) throws   IOException { //it updates the videos list with a new chunked video (value object)
        MediaMetadataRetriever retriever =readMetaData();

        ///* Width Test */Log.d("TAG","Metadataaaaa"+retriever.extractMetadata(18));
        for (int i = 0; i < 1000; i++){
            //only Metadata != null is printed!
            if(retriever.extractMetadata(i)!=null) {
                Log.d("TAG", "Metadata :: " + retriever.extractMetadata(i) +" VALUE INTEGER "+ i);
            }
        }

        VideoFile v= new VideoFile(s,"minivideo",channelName.toString(),retriever.extractMetadata(5),
                retriever.extractMetadata(9),String.valueOf(Integer.parseInt(retriever.extractMetadata(32))/Integer.parseInt(retriever.extractMetadata(9))),
                retriever.extractMetadata(18),retriever.extractMetadata(19),hashtags);
        File file = new File("Client"+channelName.toString()+"/Publisher/" + "minivideo" + ".mp4");
        BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream.read(bytes);
        v.addChunks(bytes);
        videos.put(s,new Value(v));

        System.out.println("video chunked and added to videos list");
        System.out.println(s);
        System.out.println(videos);



    }


    public byte[] getChunk(String videokey,int i){
        if(i<=videos.get(videokey).getVideo().getChunksSize())
            return videos.get(videokey).getVideo().getChunk(i);
        else
            return null;
    }

    public int getChunkSize(String videokey){
        return videos.get(videokey).getVideo().getChunksSize();
    }



    public void notifyFailure(Broker b) {

    }


    public ArrayList<Value> generateChunks(String s) {
        return null;
    }

}
