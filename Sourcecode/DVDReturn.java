package sample.net.sqlitetutorial;

import java.util.ArrayList;

public class DVDReturn {

    int id;
    String titel;
    String director;
    int year;

    DVDReturn(int id, String titel, String director, int year){
        this.id = id;
        this.titel = titel;
        this.director = director;
        this.year = year;
    }

    public int getId(){return this.id;}

    public String getTitel(){return this.titel;}

    public String getDirector(){return this.director;}

    public int getYear(){return this.year;}


    public void setId(int id){this.id = id;}

    public void setTitel(String titel){this.titel = titel;}

    public void setDirector(String director){this.director = director;}

    public void setYear(int year){this.year = year;}
}