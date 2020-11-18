package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.exit;


public class Main {

    public static final Scanner userInput = new Scanner(System.in);
    //Ne radi npr kuk ništa ne izbacuje
    public static void main(String[] args){
        List<String> wordDatabase = new ArrayList<>();
        int language = start(wordDatabase);
        boolean cont = true;
        while (cont) {
            Map<Character, Integer> letterDatabase;
            List<String> resultDatabase;
            List<String> result;
            letterDatabase = loadLetters(language);
            resultDatabase = shrinkDatabase(wordDatabase, letterDatabase);
            result = databaseFilter(resultDatabase, letterDatabase);
            //print results
            printResults(result, language);

            if(language == 0) {
                System.out.print("\nContinue? (y/n): ");
            } else {
                System.out.print("\nNastaviti? (d/n): ");
            }
            char sig = userInput.next().charAt(0);
            userInput.nextLine();
            if (sig == 'n' || sig == 'N' || sig == '0') {
                cont = false;
            }
        }
        if(language == 0) {
            System.out.println("\nProgram has finished!\nThanks for testing!\n");
        } else {
            System.out.println("\nProgram izvrsen!\nHvala na testiranju!\n");
        }
        userInput.close();
    }

    static int loadWords(List<String> wordDatabase, int language){
        String fileName;

        if(language == 0){
            fileName = "src/dictionaries/dictionary.dic";
            System.out.println("Loading Word Database, please wait...\n");
        } else if (language == 1){
            fileName = "src/dictionaries/rjecnik.dic";
            System.out.println("Ucitavam rjecnik, molimo pricekajte...\n");
        } else if (language == 2){
            language = 0;
            System.out.println("Please put dictionary file in location src/dictionaries/ and input filename ");
            System.out.print("Filename: ");
            String tmpFileName = userInput.nextLine();
            fileName = new StringBuilder().append("src/dictionaries/").append(tmpFileName).toString();

        } else {
            System.out.println("Wrong language input!\nContinuing with English!");
            fileName = "src/dictionaries/dictionary.dic";
        }
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
            	String data = myReader.nextLine();
          		wordDatabase.add(data);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            if (language == 0) {
                System.out.println("Cant open file\nPlease check if file " + fileName + " exist");
            } else {
                System.out.println("Ne mogu otvoriti datoteku\nProvjerite postoji li " + fileName);
            }
            exit(1);
        }
        return language;
    }

    static Map<Character, Integer> makeMap(String word){
    	Map<Character, Integer> tmpMap = new HashMap<>();
        word = word.toLowerCase();

        for (int i = 0; i < word.length(); i++) {
            Character key = word.charAt(i);
            if(tmpMap.containsKey(key)){
                // don't want to deal with Iterators yet
                Integer tmpInt = tmpMap.get(key);
                tmpMap.remove(key);
                tmpMap.put(key, tmpInt + 1);
            }
            else{
                tmpMap.put(key, 1);
            }
        }
        return tmpMap;
    }

    static Map<Character, Integer> loadLetters(int language){
        if(language == 0) {
        	System.out.print("Input available characters (without spaces): ");
        }
        else {
        	System.out.print("Upišite slova koja su ponuđena (bez razmaka): ");
        }
        String letter = userInput.nextLine();
        letter = letter.replaceAll("[^a-ž]", "").toLowerCase();
        return makeMap(letter);
    }

    static int languagePick(){
        System.out.println("Pick language\nOdaberite jezik\nEnglish (e)\nHrvatski (h)\n" +
                "Costum language (c)");
        String languagePicker;
        int language = -1;
        boolean inputingLanguage;
        do{
            System.out.print("Input: ");
            languagePicker = userInput.nextLine();
            inputingLanguage = false;
            if (languagePicker.equals("e") || languagePicker.equals("E")) {
                language = 0;
            }
            //Croatian
            else if (languagePicker.equals("h") || languagePicker.equals("H")) {
                language = 1;
            }
            //Costum language
            else if(languagePicker.equals("c") || languagePicker.equals("C")){
                language = 2;
            }
            else {
                System.out.println("Unknown choice\nNepoznat odabir");
                inputingLanguage = true;
            }
        }while(inputingLanguage);

        return language;
    }

     static int start(List<String> wordDatabase){
        int language = loadWords(wordDatabase, languagePick());
        wordDatabase.sort(Comparator.comparing(String::length));
        return language;
    }

     static void printResults(List<String> results, int language) {
    	if(results.size() == 0) {
    		if(language == 0) {
            	System.out.println("\nNo results\n");
            }	else { //todo language pick
            	System.out.println("\nNema rjesenja\n");
            }
    	} else{
            for(String result : results){
                System.out.println(result);
            }
        }
    }

     static List<String> shrinkDatabase(List<String> wordDatabase, Map<Character, Integer> letterDatabase){
         List<String> resultDatabase;
         int lenght = 0;
       //count number of letters in user input, can become Class? from map I need only number of letters
         for (Map.Entry<Character, Integer> entry : letterDatabase.entrySet()) {
             lenght += entry.getValue();
         }

         //remove words longer than number of letters
         int finalLenght = lenght;
         resultDatabase = wordDatabase.stream()
                 .filter(word -> word.length() <= finalLenght)
                 .collect(Collectors.toList());

         return resultDatabase;
     }

     static List<String> databaseFilter(List<String> wordsDatabase, Map<Character, Integer> letterDatabase){
        //TODO review
         List<String> results = new ArrayList<>();
         for (String word : wordsDatabase) {
             boolean notResult = false;
             Map<Character, Integer> mapa = makeMap(word);    //make map from word in DB
             for (Map.Entry<Character, Integer> entry : mapa.entrySet()) {
                 if (letterDatabase.containsKey(entry.getKey())) {
                     //for whole map from user input
                     for (Map.Entry<Character, Integer> userInput : mapa.entrySet()) {
                         //check if it is same letter
                         if (entry.getKey().equals(userInput.getKey())) {
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
             results.add(word);
         }
         return results;
     }
}
