package main;

import main.resources.UserInput;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.exit;

/*
public class a {

    public static final Scanner userInput = new Scanner(System.in);

    public static void main(String[] args){
        UserInput userData = new UserInput();
        List<String> wordDatabase = start(userData);
        boolean cont = true;
        while (cont) {
            loadLetters(userData);
            List<String> result = databaseFilter(wordDatabase, userData);

            //print results
            printResults(result, userData.getLanguage());

            if(userData.getLanguage() == 0) {
                System.out.print("Continue? (y/n): ");
            } else {
                System.out.print("Nastaviti? (d/n): ");
            }
            char sig = userInput.next().charAt(0);
            userInput.nextLine();
            if (sig == 'n' || sig == 'N' || sig == '0') {
                cont = false;
            }
        }
        if(userData.getLanguage() == 0) {
            System.out.println("Program has finished!");
        } else {
            System.out.println("Program Završen!");
        }
        userInput.close();
    }


    static List<String> start(UserInput userData){
        List<String> wordDatabase = languagePick(userData);
        wordDatabase.sort(Comparator.comparing(String::length));
        return wordDatabase;
    }

    static List<String> loadWords(int language, Path filename){
        List<String> wordDatabase = null;

        try{
            wordDatabase = Files.readAllLines(filename);
        } catch (FileNotFoundException e) {
            if (language == 0) {
                System.out.println("Cant open file\nPlease check if file " + filename + " exist");
            } else {
                System.out.println("Ne mogu otvoriti datoteku\nProvjerite postoji li " + filename);
            }
            exit(2);
        } catch (IOException e) {
            System.out.println("IOException file is not correct!");
            exit(3);
        }
        return wordDatabase;
    }

    static List<String> languagePick(UserInput userData){
        Path filename = null;
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
                case "e", "E" -> {
                    language = 0;
                    filename = Path.of("src/dictionaries/dictionary.dic");
                                    }
                case "h", "H" -> {
                    language = 1;
                    filename = Path.of("src/dictionaries/rjecnik.dic");
                                    }
                case "c", "C" -> {
                    language = 0;
                    do{
                        System.out.println("Please put dictionary file in location src/dictionaries/ and input filename ");
                        System.out.print("Filename: ");
                        Path tmpFileName = Paths.get(userInput.nextLine());
                        filename = Path.of("src/dictionaries/" + tmpFileName);
                    }while(!Files.exists(filename));
                                    }
                default -> {
                    System.out.println("Unknown choice\nNepoznat odabir");
                    inputingLanguage = true;
                }
            }
        }while(inputingLanguage);

        userData.setLanguage(language);

        return loadWords(language, filename);
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

    static void loadLetters(UserInput userData){
        if(userData.getLanguage() == 0) {
        	System.out.print("Input available characters: ");
        }
        else {
        	System.out.print("Upišite slova koja su ponuđena: ");
        }
        userData.setUserString(userInput.nextLine());
        userData.setUserInputMap(makeMap(userData.getUserString()));
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

    static void printResults(List<String> results, int language) {
        if(results.size() == 0) {
            if(language == 0) {
                System.out.println("No results!");
            }	else {
                System.out.println("Nema rješenja!");
            }
        } else{
            for(String result : results){
                System.out.println(result);
            }
        }
    }
}
*/