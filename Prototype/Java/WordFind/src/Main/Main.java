package main;

import resources.UserInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.exit;


public class Main {

    public static final Scanner userInput = new Scanner(System.in);

    public static void main(String[] args){
        List<String> wordDatabase = new ArrayList<>();
        int language = start(wordDatabase);
        boolean cont = true;
        while (cont) {
            List<String> result = databaseFilter(wordDatabase, loadLetters(language));

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
            System.out.println("Loading Word Database, please wait...");
        } else if (language == 1){
            fileName = "src/dictionaries/rjecnik.dic";
            System.out.println("Učitavam rječnik, molimo pričekajte...");
        } else if (language == 2){
            language = 0;
            System.out.println("Please put dictionary file in location src/dictionaries/ and input filename ");
            System.out.print("Filename: ");
            String tmpFileName = userInput.nextLine();
            fileName = "src/dictionaries/" + tmpFileName;

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

    static UserInput loadLetters(int language){
        if(language == 0) {
        	System.out.print("Input available characters: ");
        }
        else {
        	System.out.print("Upišite slova koja su ponuđena: ");
        }
        UserInput input = new UserInput(userInput.nextLine());
        input.setUserInputMap(makeMap(input.getUserString()));
        return input;
    }

    static int languagePick(){
        System.out.println("""
                Pick language:
                English (e)
                Odaberite jezik:
                Hrvatski (h)
                Custom language (c)""");
        String languagePicker;
        int language = -1;
        boolean inputingLanguage;
        do{
            System.out.print("Input: ");
            languagePicker = userInput.nextLine();
            inputingLanguage = false;
            //Custom language
            switch (languagePicker) {
                case "e", "E" -> language = 0;
                case "h", "H" -> language = 1;
                case "c", "C" -> language = 2;
                default -> {
                    System.out.println("Unknown choice\nNepoznat odabir");
                    inputingLanguage = true;
                }
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
            }	else {
            	System.out.println("\nNema rjesenja\n");
            }
    	} else{
            for(String result : results){
                System.out.println(result);
            }
        }
    }

     static List<String> databaseFilter(List<String> wordsDatabase, UserInput userString){

         wordsDatabase = wordsDatabase.stream()
                 .filter(word -> word.length() <= userString.getUserStringLenght())
                 .collect(Collectors.toList());

         List<String> results = new ArrayList<>();

         boolean possibleAnswer;
         for (String word : wordsDatabase){
             possibleAnswer = true;
             Map<Character, Integer> tmpMap = makeMap(word);
             for (Map.Entry<Character, Integer> entry : tmpMap.entrySet()){
                 if(!(userString.getUserInputMap().containsKey(entry.getKey())) ||
                         userString.getUserInputMap().get(entry.getKey()) < entry.getValue() ){
                     possibleAnswer = false;
                     break;
                 }
             }
             if(possibleAnswer){
                 results.add(word);
             }
         }
         return results;
     }
}
