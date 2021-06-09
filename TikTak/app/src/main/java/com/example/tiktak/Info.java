package com.example.tiktak;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

public class Info implements Serializable {

    private TreeSet<String> infos1;
    private TreeSet<String> infos2;
    private TreeSet<String> infos3;

    public TreeSet<String> getInfos1() {
        return infos1;
    }

    public void setInfos1(TreeSet<String> infos1) {
        this.infos1 = infos1;
    }

    public TreeSet<String> getInfos2() {
        return infos2;
    }

    public void setInfos2(TreeSet<String> infos2) {
        this.infos2 = infos2;
    }

    public TreeSet<String> getInfos3() {
        return infos3;
    }

    public void setInfos3(TreeSet<String> infos3) {
        this.infos3 = infos3;
    }



    public Info(Container[] b){

        infos1=new TreeSet<String>();
        infos2=new TreeSet<String>();
        infos3=new TreeSet<String>();

        Iterator<String> it;

        String current = null;
        if(b[0]!=null) {
            it = b[0].getHashtags().iterator();


            while (it.hasNext()) {

                current = it.next();
                infos1.add(current);

            }
            it = b[0].getChannelNames().iterator();

            current = null;
            while (it.hasNext()) {

                current = it.next();
                infos1.add(current);

            }
        }

        if(b[1]!=null) {
            it = b[1].getHashtags().iterator();

            current = null;
            while (it.hasNext()) {

                current = it.next();
                infos2.add(current);

            }
            it = b[1].getChannelNames().iterator();

            current = null;
            while (it.hasNext()) {

                current = it.next();
                infos2.add(current);

            }


        }

        if(b[2]!=null) {
            it = b[2].getHashtags().iterator();

            current = null;
            while (it.hasNext()) {

                current = it.next();
                infos3.add(current);

            }
            it = b[2].getChannelNames().iterator();

            current = null;
            while (it.hasNext()) {

                current = it.next();
                infos3.add(current);

            }


        }






    }

    public String toString(){
        String s="";
        Iterator<String> it = infos1.iterator();

        String current = null;
        while(it.hasNext() ) {

            current = it.next();
            s+=current;
            s+= "         ";
        }
        s+= "b1\n";
        it = infos2.iterator();

        current = null;
        while(it.hasNext() ) {

            current = it.next();
            s+=current;
            s+= "         ";

        }
        s+= "b2\n";
        it =infos3.iterator();

        current = null;
        while(it.hasNext() ) {

            current = it.next();
            s+=current;
            s+= "         ";

        }
        s+= "b3\n";

        return s;
    }

}
