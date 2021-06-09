package com.example.tiktak;

import java.io.Serializable;
import java.net.Socket;
import java.net.SocketAddress;

public class Message implements Serializable {

	private static final long serialVersionUID = -2723363051271966964L;
	String a,a1;
	String b,b1;
	PublisherInfo p;
	int c;
	Container con;
	ConsumerInfo consumer;

	public Message(ConsumerInfo consumer, Container co, String a, String b, String a1, String b1, PublisherInfo p, int c) {
		super();
		this.consumer=consumer;
		this.con=co;
		this.a = a; //data
		this.b = b; //data
		this.a1 = a1; //hash of a info
		this.b1 = b1; //hash of b info
		this.c=c; //code for the action to be executed
		this.p=p; //publisher info


	}



	public PublisherInfo getPublisher(){
		return p;
	}

	public ConsumerInfo getConsumer(){
		return consumer;
	}

	public String getA() {
		return a;
	}
	public String getA1() {
		return a1;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}
	public String getB1() {
		return b1;
	}

	public int getC() {
		return c;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String toString() {
		return "a: " + a + ", b: " + b;
	}
}