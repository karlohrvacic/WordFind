package resources;

import java.util.Map;

public class UserInput {

    private String userString;
    private Integer userStringLenght;
    private Map<Character, Integer> userInputMap;
    private Integer language;
    public UserInput() {

    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public void setUserString(String userString) {
        this.userString = userString.replaceAll("[^a-ž]", "").toLowerCase();
        userStringLenght = userString.length();
    }

    public String getUserString() {
        return userString;
    }

    public Integer getUserStringLenght() {
        return userStringLenght;
    }

    public Map<Character, Integer> getUserInputMap() {
        return userInputMap;
    }

    public void setUserInputMap(Map<Character, Integer> userInputMap) {
        this.userInputMap = userInputMap;
    }
}
