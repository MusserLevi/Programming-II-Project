import java.io.IOException;
import java.nio.file.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
public class points {
    private int points = 0;
    private String word;
    public points(String word) {
        this.word = word;
        calculatePoints();
    }
    private void calculatePoints() {
        int length = word.length();
        if (length == 4) {
            points += 5;
        } else if (length == 5) {
            points += 10;
        } else if (length == 6) {
            points += 15;
        }
    }
    public int getPoints() {
        return points;
    }
    public void printPoints() {
        System.out.println("You have " + points + " points currently.");
    }
}
public class back {
    public String[] fours;
    public String[] fives;
    public String[] sixes;
    public ArrayList<String> Guesses=new ArrayList<>();
    Random rand=new Random();
    public void setup(){
        try{
            List<String> temp=Files.readAllLines(Paths.get("words/fours.txt"));
            this.fours=temp.toArray(new String[0]);
            temp=Files.readAllLines(Paths.get("words/fives.txt"));
            this.fives=temp.toArray(new String[0]);
            temp=Files.readAllLines(Paths.get("words/sixes.txt"));
            this.sixes=temp.toArray(new String[0]);;
        }
        catch(IOException excep){System.out.println("IO Error");}
    }
    
    public String getWord(int len){
        String out=new String();
        if(len==1){out=this.fours[rand.nextInt(0,fours.length)];}
        else if(len==2){out=this.fives[rand.nextInt(0,fives.length)];}
        else if(len==3){out=this.sixes[rand.nextInt(0,sixes.length)];}
        return out;
    }
    public String ScrambleWord(String word){
        String out=new String();String temp=word;
        while(temp.length()>0){
            int ranInd=rand.nextInt(0,temp.length());
            out+=temp.charAt(ranInd);
            temp=temp.substring(0,ranInd)+temp.substring(ranInd+1);
        }
        return out;
    }
    public int guessWord(String guess,String word){
        int out=0;
        if(guess!=word){Guesses.add(guess);}
        else if(guess==word){Guesses.clear();out=1;}
        return out;
    }
    public static void main(String[] args){
        back b = new back()
        b.setup();
    }
}
