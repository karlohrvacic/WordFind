#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
#include <string>
#include <map>
using namespace std;
//Loads Words from file
void loadWords(vector<string>& wordDatabase, bool language) {
    //check language for dictionary
    string fileName;
    language ? fileName = "dictionary.dic" : fileName = "rjecnik.dic";
    try {
        language ? cout << "Loading Word Database, please wait...\n" : cout << "Ucitavam rjecnik, molimo pricekajte...\n";
        ifstream file(fileName);
        if (file.is_open()) {
            string str;
            while (getline(file, str)) {
                if (str.size() > 1)
                    wordDatabase.push_back(str);
            }
            file.close();
        }
        else {
            throw(1);
        }
    }
    //if the file cant be opened
    catch (int broj) {
        if (broj == 1) {
            language ? cout << "Cant open file\nPlease check if file dictionary.dic is next to .exe program\n" : cout << "Ne mogu otvoriti datoteku\nProvjerite nalazi li se rjecnik.dic datoteka uz .exe datoteku\n";
            system("pause");
            exit(1);
        }
    }
}
//makes map from word
map<char, int> makeMap(string letters) {
    map<char, int> mapica;
    bool repeats = false;
    //whole word
    for (int i = 0; i < letters.size(); i++) {
        repeats = false;
        //make it lowercase
        if (letters[i] < 97 && letters[i] < 91 && letters[i]>64) {
            letters[i] += 32;
        }
        //questionable code, go trough whole map if there is already that letter add a number to it
        for (auto itr = mapica.begin(); itr != mapica.end(); ++itr) {
            if (letters[i] == itr->first) {
                repeats = true;
                itr->second++;
                break;
            }
        }
        if (!repeats) {
            mapica.insert(pair<char, int>(letters[i], 1));
        }
    }
    return mapica;
}
//print vector
template <class T>
void printVector(vector<T>& vektor, bool language) {
    //vector empty
    if (!vektor.size()) {
        language ? cout << "\nNo results\n" : cout << "\nNema rjesenja\n";
    }
    else {
        for (int i = 0; i < vektor.size(); i++) {
            cout << vektor[i] << endl;
        }
    }
}
//parses user input
map<char, int> loadLetters(bool language) {
    map<char, int> letterDatabase;
    string letters;
    bool repeats = false;
    language ? cout << "Input available characters (without spaces): " : cout << "Upisite slova koja su ponudena (bez razmaka): ";
    cin >> letters;
    for (int i = 0; i < letters.size(); i++) {
        //lowercase
        if (letters[i] < 97 && letters[i]>64) {
            letters[i] += 32;
        }
        try {
            //if character is not letter, delete and inform user
            if (letters[i] < 97 || letters[i]>122) {
                char tmp = letters[i];
                letters.erase(letters.begin() + i);
                throw(tmp);
            }
        }
        catch (char character) {
            if (character) {
                language ? cout << "\n" << character << " is invalid character, it has been removed\n" : cout << "\nUpisali ste nepodrzani znak, " << character << " je uklonjen iz unosa\n";
                i--;
            }
        }
    }
    return makeMap(letters);
}
//removes words longer than user input
vector<string> shrinkDatabase(vector<string>& wordDatabase, map<char, int> letterDatabase) {
    vector<string> result;
    int i = 0, lenght = 0;
    //count number of letters in user input
    for (auto it = letterDatabase.begin(); it != letterDatabase.end(); ++it) {
        lenght += it->second;
    }
    //remove words longer than number of letters
    while (wordDatabase[i].size() <= lenght) {
        result.push_back(wordDatabase[i++]);
    }
    return result;
}
//remove words that doesn't match
//inefficient code
vector<string> databaseFilter(vector<string>& wordDatabase, map<char, int> letterDatabase) {
    vector<string> newVector;
    for (int i = 0; i < wordDatabase.size(); i++) {
        bool notResult = false;
        map<char, int> mapa = makeMap(wordDatabase[i]);	//make map from word in DB
        //for whole map made from word in database
        for (auto itr = mapa.begin(); itr != mapa.end(); ++itr) {
            //if letter can't be found in input letters
            if (letterDatabase.find(itr->first) == letterDatabase.end()) {
                notResult = true;
                break;
            }
            else {
                //for whole map from user input
                for (auto it = letterDatabase.begin(); it != letterDatabase.end(); ++it) {
                    //check if it is same letter
                    if (itr->first == it->first) {
                        //if word have less or equal number of same letter
                        if (itr->second <= it->second) {
                            continue;
                        }
                        //if word have more same letters than user input
                        else {
                            notResult = true;
                        }
                    }
                }
            }
        }
        if (notResult) {
            continue;
        }
        //append word in vector
        if (!notResult) {
            newVector.push_back(wordDatabase[i]);
        }
    }
    return newVector;
}
//delete unused vector
template <class T>
void deleteVector(vector<T>vektor) {
    vektor.erase(vektor.begin(), vektor.end());
}
//delete unused map
void deleteMap(map<char, int>mapa) {
    mapa.erase(mapa.begin(), mapa.end());
}
//pick a language
bool languagePick() {
    char language;
    cout << "Pick language\nOdaberite jezik\nEnglish (e)\nHrvatski (h)" << endl;
    cin >> language;
    //English
    if (language == 'e' || language == 'E') {
        return true;
    }
    //Croatian
    else if (language == 'h' || language == 'H') {
        cout << "Program trenutno ne podrzava dijakriticke znakove! " << endl;
        return false;
    }
    //Unknown input
    else {
        system("CLS");
        cout << "Unknown choice\nNepoznat odabir\n" << endl;
        languagePick();
    }
}
//pick a language, load database and sort it
bool start(vector<string>& wordDatabase) {
    bool language = languagePick();
    loadWords(wordDatabase, language);
    //sort word database ascending
    sort(wordDatabase.begin(), wordDatabase.end(), [](string& first, const string& second) {
        return first.size() < second.size();
        });
    return language;
}
//run the show
int main() {
    vector<string> wordDatabase;
    bool language = start(wordDatabase);
    bool cont = true;
    char sig = 'y';
    while (cont) {
        map<char, int> letterDatabase = loadLetters(language);
        vector<string> resultDatabase = shrinkDatabase(wordDatabase, letterDatabase);
        vector<string> result = databaseFilter(resultDatabase, letterDatabase);
        //print results
        printVector(result, language);
        //delete unused elements
        deleteVector(resultDatabase);
        deleteVector(result);
        deleteMap(letterDatabase);
        language ? cout << "\Continue? (y/n): " : cout << "\nNastaviti? (d/n): ";
        cin >> sig;
        //transform char to bool
        if (sig == 'n' || sig == 'N' || sig == '0') {
            cont = false;
        }
    }
    language ? cout << "\Program has finished!\nThanks for testing!\n" : cout << "\Program izvrsen!\nHvala na testiranju!\n";
    system("pause");
    return 0;
}
