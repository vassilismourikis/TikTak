package com.example.tiktak;


import java.io.File;
import java.util.*;

public class Client{

	private Publisher publisher;
	private Consumer consumer;
	static Scanner sc;
	static Scanner scanForConsumer;
	static Scanner scanForPublisher;
	Client(String name) {
		System.out.println(name);
		publisher= new Publisher(name);
		consumer= new Consumer(name);
		new File("Client"+name).mkdir();
		new File("Client"+name+"/Publisher").mkdir();
		new File("Client"+name+"/Consumer").mkdir();
		publisher.start();
		consumer.start();
	}



	
	public static void main(String args[]){

		Client c=new Client("vasilis");
		sc= new Scanner(System.in);
		scanForConsumer = new Scanner(System.in);
		scanForPublisher = new Scanner(System.in);
		int choice=-1;
		String consumerChoice = "";
		String publisherChoice = "";
		choices();
		while(true) {

			if(c.consumer.getActionsDone() && c.publisher.getActionsDone()){
				System.out.println("Give number for choice");
				choice = sc.nextInt();
			}
			if (choice == 0) {
				c.publisher.close();
				c.consumer.setChoice(0);
				c.publisher.setChoice(0);
				break;
			}

			if (choice > 3) {
				if(choice == 5){
					c.consumer.setChoice(choice);
					c.consumer.setActionsDone(false);
				}
				else if (choice == 6){
					c.consumer.setChoice(choice);
					c.consumer.setActionsDone(false);

				}



			} else if (choice < 3 && choice > 0) {
				if(choice == 2){
					c.publisher.setActionsDone(false);
				}
				c.publisher.setChoice(choice);



			}

			if(!c.consumer.getActionsDone() && choice == 5) {
				consumerChoice = scanForConsumer.nextLine();
				c.consumer.SelectHashtag(consumerChoice);
			}
			if(!c.consumer.getActionsDone() && choice == 6){
				consumerChoice = scanForConsumer.nextLine();
				c.consumer.subscribe(consumerChoice);
			}
			if(!c.publisher.getActionsDone()){
				publisherChoice = scanForPublisher.nextLine();
				c.publisher.deleteActualVideo(publisherChoice);
			}


		}

	}


	static void choices(){

		System.out.println("Choices as Publisher ");
		System.out.println("1. Add hashtags ");
		System.out.println("2. Delete Video ");
		System.out.println("Choices as Consumer ");
		System.out.println("5. Check available topics ");
		System.out.println("6. Subscribe to a topic");
		System.out.println("0. Close client ");
	}

}
