import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static java.lang.System.exit;
import java.util.Comparator;
import java.util.Collections;


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
    
    static Map<Character, Integer> makeMap(String letters){
    	Map<Character, Integer> mapica = new HashMap<Character, Integer>();        
        boolean repeats = false;
        StringBuilder letter = new StringBuilder(letters);
        for (int i = 0; i < letter.length(); i++) {
            repeats = false;
            //make it lowercase
            if (letter.charAt(i) < 97 && letter.charAt(i) < 91 && letter.charAt(i)>64) {
            	letter.append(Character.toLowerCase(letter.charAt(i)));
            	}
            //questionable code, go trough whole map if there is already that letter add a number to it
            for (Map.Entry<Character,Integer> entry : mapica.entrySet()) {
                if (letter.charAt(i) == entry.getKey()) {
                    repeats = true;
                    entry.setValue(entry.getValue()+1);
                    break;
                }
            }
            if (!repeats) {
                mapica.put(letter.charAt(i), 1);
            }
        }
        return mapica;
    }
    
    static Map<Character, Integer> loadLetters(boolean language){
    	String letters;
        if(language) {
        	System.out.println("Input available characters (without spaces): ");
        }
        else {
        	System.out.println("Upisite slova koja su ponudena (bez razmaka): ");
        }
        Scanner input = new Scanner(System.in);
        letters = input.nextLine();
        StringBuilder letter = new StringBuilder(letters);
        input.close();
        for (int i = 0; i < letter.length(); i++) {
            //lowercase
            if (letter.charAt(i) < 97 && letter.charAt(i)>64) {
            	letter.append(Character.toLowerCase(letter.charAt(i)));
            }
                //if character is not letter, delete and inform user
            if (letter.charAt(i) < 97 || letter.charAt(i)>122) {
               char tmp = letter.charAt(i);
               letter.deleteCharAt(i);
               if(language) {
                  System.out.printf("\n",  tmp, " is invalid character, it has been removed\n");
               }
               else {
                  System.out.printf("\nUpisali ste nepodrzani znak, ", tmp, " je uklonjen iz unosa\n");
                }
           }
        }
        return makeMap(letter.toString());
    }

    static boolean languagePick(){
        System.out.println("Pick language\nOdaberite jezik\nEnglish (e)\nHrvatski (h)\n");
        Scanner sc = new Scanner(System.in);
        char language = sc.next().charAt(0);
        sc.close();
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
        Collections.sort(wordDatabase,Comparator.comparing(String::length));
        return language;
    }

    public static void main(String[] args){
        ArrayList<String> wordDatabase = new ArrayList<String>();
        boolean language = start(wordDatabase);
        boolean cont = true;
        char sig = 'y';
        while (cont) {
        	Map<Character, Integer> letterDatabase = new HashMap<Character, Integer>();        
        	letterDatabase = loadLetters(language);
        }
    }

}
