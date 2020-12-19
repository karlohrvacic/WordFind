package main.gui;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.resources.UserInput;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class LanguageController implements Initializable {

    UserInput userData = new UserInput();

    Path filename;

    List<String> wordDatabase;

    @FXML
    private ToggleButton language;

    @FXML
    private ListView<String> listView;

    @FXML
    private Label userInformation;

    @FXML
    private TextField inputedWords;

    @FXML
    private Button button;

    public void changeText(){
        inputedWords.clear();
        if (language.isSelected()){
            language.setText("English");
            userInformation.setText("Upišite slova koja su ponuđena");
            Main.getMainStage().setTitle("Hrvatski");
            button.setText("Traži");
            //columnOfWords.setText("Odgovarajuće riječi");
            filename = Path.of("src/dictionaries/rjecnik.dic");

        } else{
            language.setText("Hrvatski");
            userInformation.setText("Input available characters");
            Main.getMainStage().setTitle("English");
            button.setText("Search");
            //columnOfWords.setText("Matching Words");
            filename = Path.of("src/dictionaries/dictionary.dic");

        }
        try{
            wordDatabase = Files.readAllLines(filename);
        } catch (FileNotFoundException e) {
            if (!language.isSelected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("File is not found!");
                alert.setContentText("Check if file is in right directory!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText("Datoteka nije pronađena!");
                alert.setContentText("Provjerite postoji li datoteka!");
                alert.showAndWait();               }
        } catch (IOException e) {
            if(!language.isSelected()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("IOException file is not correct!");
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText("IOException datoteka nije ispravna!");
            }
        }
        wordDatabase.sort(Comparator.comparing(String::length));
        listView.itemsProperty().setValue(FXCollections.observableArrayList(wordDatabase));
    }

    private void matches(){

        List<String> shortWordDatabase = wordDatabase.stream()
                .filter(word -> word.length() <= userData.getUserStringLenght())
                .collect(Collectors.toList());

        List<String> results = new ArrayList<>();

        boolean possibleAnswer;
        for (String word : shortWordDatabase){
            possibleAnswer = true;
            Map<Character, Integer> tmpMap = makeMap(word);
            for (Map.Entry<Character, Integer> entry : tmpMap.entrySet()){
                if(!(userData.getUserInputMap().containsKey(entry.getKey())) ||
                        userData.getUserInputMap().get(entry.getKey()) < entry.getValue() ){
                    possibleAnswer = false;
                    break;
                }
            }
            if(possibleAnswer){
                results.add(word);
            }
        }
        if(results.size() == 0){

        } else{

        }
        listView.itemsProperty().setValue(FXCollections.observableArrayList(results));
    }

    public void search(){
        userData.setUserString(inputedWords.getText().toLowerCase());
        userData.setUserInputMap(makeMap(userData.getUserString()));
        matches();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeText();
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
}
