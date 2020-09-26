import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static java.lang.System.exit;
import java.util.Comparator;
import java.util.Collections;
import java.io.File;  // Import the File class
import java.util.Scanner;

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
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
            	String data = myReader.nextLine();
            	//System.out.println(data);
          		wordDatabase.add(data);

            }
            myReader.close();
          } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (language) {
                System.out.println("Cant open file\nPlease check if file dictionary.dic is next to .exe program\n");
            } else {
                System.out.println("Ne mogu otvoriti datoteku\nProvjerite nalazi li se rjecnik.dic datoteka uz .exe datoteku\n");
            }
            exit(1);
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
    	String letters = new String();
        if(language) {
        	System.out.println("Input available characters (without spaces): ");
        }
        else {
        	System.out.println("Upisite slova koja su ponudena (bez razmaka): ");
        }
        Scanner input = new Scanner(System.in);
        letters = input.nextLine();
        input.close();
        StringBuilder letter = new StringBuilder(letters);
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
     
     static void printVector(ArrayList<String> result, boolean language) {
    	 System.out.println(Arrays.toString(result.toArray()));
    	if(result.size()==0) {
    		if(language) {
            	System.out.println("\nNo results\n");
            }	else {
            	System.out.println("\nNema rjesenja\n");
            }
    	}
    }
    
     static ArrayList<String> shrinkDatabase(ArrayList<String> wordDatabase, Map<Character, Integer> letterDatabase){
         ArrayList<String> resultDatabase = new ArrayList<String>();
         int i = 0, lenght = 0;
       //count number of letters in user input
         for (Map.Entry<Character, Integer> entry : letterDatabase.entrySet()) {
             lenght += entry.getValue();
         }
         //remove words longer than number of letters
         while (wordDatabase.get(i).length() <= lenght) {
        	 resultDatabase.add(wordDatabase.get(i++));
         }
         return resultDatabase;
     }
     
     static ArrayList<String> databaseFilter(ArrayList<String> resultDatabase,Map<Character, Integer> letterDatabase){
         ArrayList<String> newVector = new ArrayList<String>();
         for (int i = 0; i < resultDatabase.size(); i++) {
             boolean notResult = false;
             Map<Character, Integer> mapa = makeMap(resultDatabase.get(i));	//make map from word in DB
             for (Map.Entry<Character, Integer> entry : mapa.entrySet()) {
            	if(letterDatabase.containsKey(entry.getKey())) {
            		//for whole map from user input
            		for (Map.Entry<Character, Integer> userInput : mapa.entrySet()) {
                        //check if it is same letter
                        if (entry.getKey()==userInput.getKey()) {
                            //if word have greater number of same letter
                            if (entry.getValue() > userInput.getValue()) {
                                notResult = true;
                            }
                        }
                    }
            	} else {
            		notResult = true;
                    break;
            	}
             }
             //for whole map made from word in database
        	 if (notResult) {
                 continue;
             }
             //append word in vector
             if (!notResult) {
            	 newVector.add(resultDatabase.get(i));
             }
         }
         return newVector;
     }

    public static void main(String[] args){
        ArrayList<String> wordDatabase = new ArrayList<String>();
        boolean language = start(wordDatabase);
        boolean cont = true;
        while (cont) {
        	Map<Character, Integer> letterDatabase = new HashMap<Character, Integer>();
            ArrayList<String> resultDatabase = new ArrayList<String>();
            ArrayList<String> result = new ArrayList<String>();
        	letterDatabase = loadLetters(language);
        	resultDatabase = shrinkDatabase(wordDatabase, letterDatabase);
        	result = databaseFilter(resultDatabase, letterDatabase);
        	//print results
            printVector(result, language);

            if(language) {
            	System.out.println("\nContinue? (y/n): ");
            }	else {
            	System.out.println("\nNastaviti? (d/n): ");
            }
            Scanner sc = new Scanner(System.in); 
            char sig = sc.next().charAt(0); 
            sc.close();
            if (sig == 'n' || sig == 'N' || sig == '0') {
                cont = false;
            }
        }
        if(language) {
        	System.out.println("\nProgram has finished!\nThanks for testing!\n");
        }	else {
        	System.out.println("\nProgram izvrsen!\nHvala na testiranju!\n");
        }
    }

}
