#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
#include <string>
#include <map>

using namespace std;
void ucitajDatoteku(vector<string>& bazaRijeci) {
	// ne radi s čćžšđ
	cout << "Ucitavam rjecnik, molimo pricekajte...\n";
	ifstream datoteka ("rjecnik.dic");
	try {
		if (datoteka.is_open()) {
			string str;
			while (getline(datoteka, str)) {
				if (str.size() > 0)
					bazaRijeci.push_back(str);
			}
			datoteka.close();
		}
		else {
			cout << "Ne mogu otvoriti datoteku\nProvjerite nalazi li se rjecnik.dic datoteka uz .exe datoteku\n";
			throw(1);
		}
	}
	catch (int broj) {
		if (broj == 1) {
			system("pause");
			exit(1);
		}
	}
}

map<char, int> dajMapu(string slova) {
	map<char, int> mapica;
	bool pojavlja = false;
	for (int i = 0; i < slova.size(); i++) {
		pojavlja = false;
		if (slova[i] < 97 && slova[i] < 91 && slova[i]>64) {
			slova[i] += 32;
		}
		for (auto itr = mapica.begin(); itr != mapica.end(); ++itr) {
			if (slova[i] == itr->first) {
				pojavlja = true;
				itr->second++;
				break;
			}
		}
		if (pojavlja == false) {
			mapica.insert(pair<char, int>(slova[i], 1));
		}
	}
	return mapica;
}

template <class T>
void ispisiVektor(vector<T>& vektor) {
	for (int i = 0; i < vektor.size(); i++) {
		cout << vektor[i] << endl;
	}
	if (vektor.size() == 0) {
		cout << "\nNema rjesenja\n";
	}
}

map<char, int> ucitajSlova(){
	map<char, int> bazaSlova;
	string slova;
	bool pojavlja = false;
	cout << "Upisite slova koja su ponudena (bez razmaka): ";
	cin >> slova;
		for (int i = 0; i < slova.size(); i++) {
			if (slova[i] < 97 && slova[i] <91 && slova[i]>64) {
				slova[i] += 32;
			}
			try {
				if (slova[i] < 97 || slova[i]>122) {
					char tmp = slova[i];
					slova.erase(slova.begin() + i);
					throw(tmp);
				}
			}
			catch (char znak) {
				if (znak) {
					cout << "\nUpisali ste nepodrzani znak, "<< znak <<" je uklonjen iz unosa\n";
					i--;
				}
			}
		}
	return dajMapu(slova);
}

vector<string> smanjiBazu(vector<string>& bazaRijeci, map<char, int> bazaSlova) {
	vector<string> rjesenja;
	int i = 0, velicina = 0;
	for (auto it = bazaSlova.begin(); it != bazaSlova.end(); ++it) {
		velicina += it->second;
	}
	while (bazaRijeci[i].size() <= velicina) {
		rjesenja.push_back(bazaRijeci[i]);
		i++;
	}
	return rjesenja;
}


vector<string> filtrirajBazu(vector<string>& bazaRijeci, map<char, int> bazaSlova) {
	vector<string> noviVektor;
	for (int i = 0; i < bazaRijeci.size(); i++) {
		bool nijeRjesenje = false;
		map<char, int> mapa = dajMapu(bazaRijeci[i]);
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
			noviVektor.push_back(bazaRijeci[i]);
		}
	}
	return noviVektor;
}
template <class T>
void obrisiVektor(vector<T>vektor) {
	vektor.erase(vektor.begin(), vektor.end());
}

void obrisiMapu(map<char, int>mapa) {
	mapa.erase(mapa.begin(), mapa.end());
}

int main() {
	cout << "Program trenutno ne podrzava dijakriticke znakove! " << endl;
	char nastavi = 'y';
	vector<string> bazaRijeci;
	ucitajDatoteku(bazaRijeci);

	sort(bazaRijeci.begin(), bazaRijeci.end(), [](string& first, const string& second) {
		return first.size() < second.size(); });


	while (nastavi == 'y' || nastavi == 'Y') {
		map<char, int> bazaSlova = ucitajSlova();

		vector<string> bazaRjesenja = smanjiBazu(bazaRijeci, bazaSlova);
		vector<string> rjesenja = filtrirajBazu(bazaRjesenja, bazaSlova);

		ispisiVektor(rjesenja);

		obrisiVektor(bazaRjesenja);
		obrisiVektor(rjesenja);
		obrisiMapu(bazaSlova);
		cout << "\nNastaviti? (y/n): ";
		cin >> nastavi;
	}
	return 0;
}
