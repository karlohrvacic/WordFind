#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
#include <string>
#include <map>

using namespace std;
void loadWords(vector<string>& wordDatabase, bool language) {
	// ne radi s čćžšđ
	ifstream file("");
	try {
		if (language == true) {
			cout << "Loading Word Database, please wait...\n";
			ifstream file("dictionary.dic");
			if (file.is_open()) {
				string str;
				while (getline(file, str)) {
					if (str.size() > 0)
						wordDatabase.push_back(str);
				}
				file.close();
			}
			else {
				cout << "Cant open file\nPlease check if file dictionary.dic is next to .exe program\n";
				throw(1);
			}
		}
		else {
			cout << "Ucitavam rjecnik, molimo pricekajte...\n";
			ifstream file("rjecnik.dic");
			if (file.is_open()) {
				string str;
				while (getline(file, str)) {
					if (str.size() > 0)
						wordDatabase.push_back(str);
				}
				file.close();
			}
			else {
				cout << "Ne mogu otvoriti datoteku\nProvjerite nalazi li se rjecnik.dic datoteka uz .exe datoteku\n";
				throw(1);
			}
		}

		
	}
	catch (int broj) {
		if (broj == 1) {
			system("pause");
			exit(1);
		}
	}
}

map<char, int> dajMapu(string letters) {
	map<char, int> mapica;
	bool repeats = false;
	for (int i = 0; i < letters.size(); i++) {
		repeats = false;
		if (letters[i] < 97 && letters[i] < 91 && letters[i]>64) {
			letters[i] += 32;
		}
		for (auto itr = mapica.begin(); itr != mapica.end(); ++itr) {
			if (letters[i] == itr->first) {
				repeats = true;
				itr->second++;
				break;
			}
		}
		if (repeats == false) {
			mapica.insert(pair<char, int>(letters[i], 1));
		}
	}
	return mapica;
}

template <class T>
void printVector(vector<T>& vektor, bool language) {
	for (int i = 0; i < vektor.size(); i++) {
		cout << vektor[i] << endl;
	}
	if (vektor.size() == 0) {
		if (language == true) {
			cout << "\nNo results\n";

		}
		else {
			cout << "\nNema rjesenja\n";

		}
	}
}

map<char, int> loadLetters(bool language){
	map<char, int> bazaSlova;
	string letters;
	bool repeats = false;
	if (language == true) {
		cout << "Input available characters (without spaces): ";
	}
	else {
		cout << "Upisite slova koja su ponudena (bez razmaka): ";
	}
	cin >> letters;
		for (int i = 0; i < letters.size(); i++) {
			if (letters[i] < 97 && letters[i] <91 && letters[i]>64) {
				letters[i] += 32;
			}
			try {
				if (letters[i] < 97 || letters[i]>122) {
					char tmp = letters[i];
					letters.erase(letters.begin() + i);
					throw(tmp);
				}
			}
			catch (char character) {
				if (character) {
					if (language == true) {
						cout << "\n" <<character << "is invalid character, it has been removed\n";
					}
					else {
						cout << "\nUpisali ste nepodrzani znak, " << character << " je uklonjen iz unosa\n";
					}
					i--;
				}
			}
		}
	return dajMapu(letters);
}

vector<string> smanjiBazu(vector<string>& wordDatabase, map<char, int> bazaSlova) {
	vector<string> rjesenja;
	int i = 0, velicina = 0;
	for (auto it = bazaSlova.begin(); it != bazaSlova.end(); ++it) {
		velicina += it->second;
	}
	while (wordDatabase[i].size() <= velicina) {
		rjesenja.push_back(wordDatabase[i]);
		i++;
	}
	return rjesenja;
}


vector<string> databaseFilter(vector<string>& wordDatabase, map<char, int> bazaSlova) {
	vector<string> noviVektor;
	for (int i = 0; i < wordDatabase.size(); i++) {
		bool nijeRjesenje = false;
		map<char, int> mapa = dajMapu(wordDatabase[i]);
		for (auto itr = mapa.begin(); itr != mapa.end(); ++itr) {
			if (bazaSlova.find(itr->first) == bazaSlova.end() || mapa.size()==1) {
				nijeRjesenje = true;
				break;
			}
			else {
				for (auto it = bazaSlova.begin(); it != bazaSlova.end(); ++it) {
					if (itr->first == it->first) {
						if (itr->second <= it->second) {
							continue;
						}
						else {
							nijeRjesenje = true;
						}
					}
				}
			}
		}
		if (nijeRjesenje == true) {
			continue;
		}
		if (nijeRjesenje == false) {
			noviVektor.push_back(wordDatabase[i]);
		}
	}
	return noviVektor;
}
template <class T>
void deleteVector(vector<T>vektor) {
	vektor.erase(vektor.begin(), vektor.end());
}

void deleteMap(map<char, int>mapa) {
	mapa.erase(mapa.begin(), mapa.end());
}

bool languagePick() {
	char language;
	cout << "Pick language\nOdaberite jezik\nEnglish (e)\nHrvatski (h)" << endl;
	cin >> language;
	if (language == 'e' || language == 'E') {
		return true;
	}
	else if (language == 'h' || language == 'H') {
		cout << "Program trenutno ne podrzava dijakriticke znakove! " << endl;
		return false;
	}
	else {
		system("CLS");
		cout << "Unknown choice\nNepoznat odabir\n" << endl;
		languagePick();
	}
}

bool start(vector<string>&wordDatabase) {

	bool language = languagePick();

	loadWords(wordDatabase, language);

	sort(wordDatabase.begin(), wordDatabase.end(), [](string& first, const string& second) {
		return first.size() < second.size(); });
	

	return language;
}

int main() {
	vector<string> wordDatabase;
	bool language = start(wordDatabase);
	char cont = 'y';


	while (cont == 'y' || cont == 'Y' || cont =='d' ||cont =='D') {
		map<char, int> letterDatabase = loadLetters(language);

		vector<string> resultDatabase = smanjiBazu(wordDatabase, letterDatabase);
		vector<string> result = databaseFilter(resultDatabase, letterDatabase);

		printVector(result, language);

		deleteVector(resultDatabase);
		deleteVector(result);
		deleteMap(letterDatabase);
		if (language == true) {
			cout << "\Continue? (y/n): ";
		}
		else {
			cout << "\nNastaviti? (d/n): ";
		}
		cin >> cont;
	}
	return 0;
}
