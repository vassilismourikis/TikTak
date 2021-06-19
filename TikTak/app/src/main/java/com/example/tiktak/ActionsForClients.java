package com.example.tiktak;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class ActionsForClients extends AsyncTask<String, String, String> {
	ObjectInputStream in;
	ObjectOutputStream out;
	BrokerNode broker;

	public ActionsForClients(Socket connection, BrokerNode broker) {
		//ο constructor αρχικοποιεί τα αντικείμενα-ροές για την επικοινωνία με τον αντίστοιχο πελάτη
		try {
			out = new ObjectOutputStream(connection.getOutputStream());
			//out: για γράψιμο στον πελάτη

			in = new ObjectInputStream(connection.getInputStream());
			//in: για διάβασμα από τον πελάτη
			this.broker=broker;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(String... strings) {


		try {


			Message abMessage = (Message)in.readObject();

			String a = abMessage.getA();
			String b = abMessage.getB();
			if(abMessage.getC()==1) {//SAVE THE HASHTAG-VIDEOKEY TUPLE
				//System.out.println("SAVING THE HASHTAG");
				broker.saveHashtag(abMessage);
			}
			else if(abMessage.getC()==2) {//SAVE THE CHANNEL-VIDEOKEY TUPLE
				//System.out.println("SAVING THE CHANNELNAME");
				broker.saveChannelName(abMessage);
			}
			else if(abMessage.getC()==4){//FIRST CONNECT OF CONSUMER
				out.writeObject(broker.registerConsumer(abMessage.getConsumer()));
				out.flush();
			}
			else if(abMessage.getC()==5){//GET RELATED VIDEOS

				ArrayList<String> videokeys=broker.getRelatedVideos(abMessage.getA(),abMessage.getB());
				out.writeObject(videokeys);
				out.flush();
			}
			else if(abMessage.getC()==6){//GET ACTUAL VIDEO
				int u=0;
				byte[] bytee=broker.pull(abMessage.getA(),u++);

				while(bytee!=null) {

					out.writeObject(bytee);
					out.flush();
					bytee=broker.pull(abMessage.getA(),u++);
				}
				out.writeObject(null);
				out.flush();
			}
			else if(abMessage.getC()==7) {
				broker.updateVideos(abMessage.getPublisher(),abMessage.getA());
			}
			else if(abMessage.getC()==20) {
				out.writeBoolean(broker.isChannelName(a));
				out.flush();
			}




		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				in.close();
				out.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		return null;
	}
}