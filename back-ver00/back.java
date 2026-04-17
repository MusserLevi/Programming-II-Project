import java.io.IOException;
import java.nio.file.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
class Points {
    private int points = 0;
    private String word;
    public Points(String word) {
        this.word = word;
        calculatePoints();
    }
    private void calculatePoints() {
        points = switch(word.length()){
            case 4 -> 5;
            case 5 -> 10;
            case 6 -> 15;
            default -> 0;
        };
    }
    public int getPoints() {
        return points;
    }
    public void printCurrentPoints() {
        System.out.println("You have " + points + " points currently.");
    }
    @Override
    public String toString() {
        return "Word: " + word + "Points: " + points;
    }
}
public class back {
    private String currentWord;
    private int TotalScore = 0;
    private Points CurrentPoints;
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
        if(len==1){
            currentWord=this.fours[rand.nextInt(0,fours.length)];
        } else if(len==2){
            currentWord=this.fives[rand.nextInt(0,fives.length)];
        } else if(len==3){
            currentWord=this.sixes[rand.nextInt(0,sixes.length)];
        }
        CurrentPoints = new Points(currentWord);
        
        return currentWord;
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
        if(!guess.equals(word))
        {Guesses.add(guess);
        } else {
            Guesses.clear();
            out=1;
            TotalScore += CurrentPoints.getPoints();
        }
        return out;
    }
    public void printTotalScore() {
        System.out.println("Total Score = " + TotalScore);
    }
    public static void main(String[] args){
        back b = new back();
        b.setup();
    }
}
