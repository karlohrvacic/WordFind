import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static java.lang.System.exit;
import java.util.Arrays;
import java.util.Comparator;



public class Main {


    static void loadWords(ArrayList<String> wordDatabase, boolean language){
        String fileName;
        if(language){
            fileName = "dictionary.dic";
            System.out.println("Loading Word Database, please wait...\n");
        } else {
            fileName = "rjecnik.dic";
            System.out.println("Ucitavam rjecnik, molimo pricekajte...\n");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                wordDatabase.add(sCurrentLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (language) {
                System.out.println("Cant open file\nPlease check if file dictionary.dic is next to .exe program\n");
            } else {
                System.out.println("Ne mogu otvoriti datoteku\nProvjerite nalazi li se rjecnik.dic datoteka uz .exe datoteku\n");
            }
            exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean languagePick(){
        System.out.println("Pick language\nOdaberite jezik\nEnglish (e)\nHrvatski (h)\n");
        Scanner sc = new Scanner(System.in);
        char language = sc.next().charAt(0);
        boolean Language = true;
        if (language == 'e' || language == 'E') {
            Language = true;
        }
        //Croatian
        else if (language == 'h' || language == 'H') {
            System.out.println("Program trenutno ne podrzava dijakriticke znakove! \n");
            Language = false;
        }
        //Unknown input
        else {
            System.out.println("Unknown choice\nNepoznat odabir\n");
            languagePick();
        }
        return Language;
    }

     static boolean start(ArrayList<String> wordDatabase){
        boolean language = languagePick();
        loadWords(wordDatabase, language);
        Arrays.sort(wordDatabase, Comparator.comparingInt(String::length));
        System.out.print(wordDatabase);
        return language;
    }

    public static void main(String[] args){
        ArrayList<String> wordDatabase = new ArrayList<String>();
        boolean language = start(wordDatabase);

    }

}
