package Main;

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
            } else { //TODO language picker
                System.out.print("\nNastaviti? (d/n): ");
            }
            char sig = userInput.next().charAt(0);
            if (sig == 'n' || sig == 'N' || sig == '0') {
                cont = false;
            }
        }
        if(language == 0) {
            System.out.println("\nProgram has finished!\nThanks for testing!\n");
        } else {//todo language pick
            System.out.println("\nProgram izvrsen!\nHvala na testiranju!\n");
        }
        userInput.close();
    }

    static void loadWords(List<String> wordDatabase, int language){
        String fileName;
        if(language == 0){
            fileName = "src/dictionaries/dictionary.dic";
            System.out.println("Loading Word Database, please wait...\n");
        } else{
            fileName = "src/dictionaries/rjecnik.dic";
            System.out.println("Ucitavam rjecnik, molimo pricekajte...\n");
        }
        //TODO add new language menu: if language not 0 or 1 null!!!!
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
            if (language == 0) { // todo language menu
                System.out.println("Cant open file\nPlease check if file dictionary.dic is next to .exe program\n");
            } else {
                System.out.println("Ne mogu otvoriti datoteku\nProvjerite nalazi li se rjecnik.dic datoteka uz .exe datoteku\n");
            }
            exit(1);
        }
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
        else { //todo language menu
        	System.out.print("Upisite slova koja su ponudena (bez razmaka): ");
        }
        String letter = userInput.nextLine();
        letter = letter.replaceAll("[^a-ž]", "").toLowerCase();
        return makeMap(letter);
    }

    static int languagePick(){
        System.out.println("Pick language\nOdaberite jezik\nEnglish (e)\nHrvatski (h)\n");
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
                System.out.println("Program trenutno ne podrzava dijakriticke znakove! \n");
                language = 1;
            }// todo add language menu picker
            else {
                System.out.println("Unknown choice\nNepoznat odabir\n");
                inputingLanguage = true;
            }
        }while(inputingLanguage);

        return language;
    }

     static int start(List<String> wordDatabase){
        int language = languagePick();
        loadWords(wordDatabase, language);
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
         System.out.println(results.size());
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

     static List<String> databaseFilter(List<String> resultDatabase, Map<Character, Integer> letterDatabase){
        //TODO review
         List<String> newList = new ArrayList<>();
         for (String s : resultDatabase) {
             boolean notResult = false;
             Map<Character, Integer> mapa = makeMap(s);    //make map from word in DB
             for (Map.Entry<Character, Integer> entry : mapa.entrySet()) {
                 if (letterDatabase.containsKey(entry.getKey())) {
                     //for whole map from user input
                     for (Map.Entry<Character, Integer> userInput : mapa.entrySet()) {
                         //check if it is same letter
                         if (entry.getKey() == userInput.getKey()) {
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
             newList.add(s);
         }
         return newList;
     }

}
