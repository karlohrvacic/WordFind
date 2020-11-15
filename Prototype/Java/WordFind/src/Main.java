import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.exit;

public class Main {

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
            printVector(result, language);

            if(language == 0) {
                System.out.println("\nContinue? (y/n): ");
            }	else { //TODO language picker
                System.out.println("\nNastaviti? (d/n): ");
            }
            Scanner sc = new Scanner(System.in);
            char sig = sc.next().charAt(0);
            sc.close();
            if (sig == 'n' || sig == 'N' || sig == '0') {
                cont = false;
            }
        }
        if(language == 0) {
            System.out.println("\nProgram has finished!\nThanks for testing!\n");
        }	else {//todo language pick
            System.out.println("\nProgram izvrsen!\nHvala na testiranju!\n");
        }
    }

    static void loadWords(List<String> wordDatabase, int language){
        String fileName;
        if(language == 0){
            fileName = "dictionary.dic";
            System.out.println("Loading Word Database, please wait...\n");
        } else{
            fileName = "rjecnik.dic";
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
    
    static Map<Character, Integer> makeMap(String letters){
    	Map<Character, Integer> tmpMap = new HashMap<>();
        boolean repeats;
        StringBuilder letter = new StringBuilder(letters);
        for (int i = 0; i < letter.length(); i++) {
            repeats = false;
            //make it lowercase
            if (letter.charAt(i) < 97 && letter.charAt(i) < 91 && letter.charAt(i)>64) {
            	letter.append(Character.toLowerCase(letter.charAt(i)));
            	}
            //questionable code, go trough whole map if there is already that letter add a number to it
            for (Map.Entry<Character,Integer> entry : tmpMap.entrySet()) {
                if (letter.charAt(i) == entry.getKey()) {
                    repeats = true;
                    entry.setValue(entry.getValue()+1);
                    break;
                }
            }
            if (!repeats) {
                tmpMap.put(letter.charAt(i), 1);
            }
        }
        return tmpMap;
    }
    
    static Map<Character, Integer> loadLetters(int language){
    	String letters;
        if(language == 0) {
        	System.out.println("Input available characters (without spaces): ");
        }
        else { //todo language menu
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
               if(language == 0) {
                  System.out.println("\n" +  tmp + " is invalid character, it has been removed\n");
               }
               else { //TODO language pick
                  System.out.println("\nUpisali ste nepodrzani znak, " + tmp + " je uklonjen iz unosa\n");
                }
           }
        }
        return makeMap(letter.toString());
    }

    static int languagePick(){
        System.out.println("Pick language\nOdaberite jezik\nEnglish (e)\nHrvatski (h)\n");
        Scanner input = new Scanner(System.in);
        char languagePicker = input.next().charAt(0);
        int language = -1;
        boolean inputingLanguage;
        do{
            inputingLanguage = false;
            if (languagePicker == 'e' || languagePicker == 'E') {
                language = 0;
            }
            //Croatian
            else if (languagePicker == 'h' || languagePicker == 'H') {
                System.out.println("Program trenutno ne podrzava dijakriticke znakove! \n");
                language = 1;
            }// todo add language menu picker
            else {
                System.out.println("Unknown choice\nNepoznat odabir\n");
                inputingLanguage = true;
            }
        }while(inputingLanguage);

        input.close();
        return language;
    }

     static int start(List<String> wordDatabase){
        int language = languagePick();
        loadWords(wordDatabase, language);
        wordDatabase.sort(Comparator.comparing(String::length));
        return language;
    }
     
     static void printVector(List<String> result, int language) {
    	 System.out.println(Arrays.toString(result.toArray()));
    	if(result.size()==0) {
    		if(language == 0) {
            	System.out.println("\nNo results\n");
            }	else { //todo language pick
            	System.out.println("\nNema rjesenja\n");
            }
    	}
    }
    
     static List<String> shrinkDatabase(List<String> wordDatabase, Map<Character, Integer> letterDatabase){
         List<String> resultDatabase = new ArrayList<>();
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
     
     static List<String> databaseFilter(List<String> resultDatabase, Map<Character, Integer> letterDatabase){
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
