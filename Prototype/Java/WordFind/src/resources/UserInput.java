package resources;

import java.util.Map;

public class UserInput {

    private String userString;
    private Integer userStringLenght;
    private Map<Character, Integer> userInputMap;

    public UserInput(String userString) {
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
