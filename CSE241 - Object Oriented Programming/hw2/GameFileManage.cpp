#include <iostream>
#include <string>
#include <fstream>
#include "GameBoard.h"
#include "GameFileManage.h"
#include "GameSys.h"
#include "GameAI.h"

using namespace std;

char fman_save(const string& filename, const char board[12][12], const int size, const char player, const int moveCount, const gamemode& mode) {
	char returnVal='-';
	ofstream file;

	file.open(filename);

	if (file.is_open()) {
		file << "!\n" << mode << endl << player << endl << size << endl << moveCount << "\n{\n";
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				if (board[i][j] == pl1 || board[i][j] == pl2) 
					file << board[i][j] << j << " " << i << endl;
			}
		}
		file << "}\n" << moveCount << "\n!";
		file.close();
		cout << "Game Successfully Saved in " << filename << endl;
	}
	else {
		cerr << "!!!FILE COULDN'T OPEN!!!\n";
		exit(1);
	}
	return returnVal;
}

char fman_load(string filename, char board[12][12]) {
	//char board[12][12];
	char returnVal='-';
	int size, moveCount, cmoveCount=0;
	cell firstPlayer;
	ifstream file;
	int mod;
	decltype(filename) holder;

	file.open(filename);

	if (file.is_open()) {
		getline(file, holder);		/*takes informations with getlines*/
		if (holder == "!") {
			getline(file, holder);
			mod = holder[0] - '0';
			getline(file, holder);
			firstPlayer = static_cast<cell> (holder[0]);
			getline(file, holder);
			size = stoi(holder);
			gboard_fill_empty(board);
			gboard_prep(board, size);
			getline(file, holder);
			moveCount = stoi(holder);
			getline(file, holder);
			getline(file, holder);
			for (int i = 0; holder[0]!='}'; ++i) {
				board[stoi(&holder[3])][stoi(&holder[1])] = holder[0];
				if (holder[0] == 'o')
					++cmoveCount;
				getline(file, holder);
			}
			file.close();
			cout << "Game Successfully Loaded From " << filename << endl;
			if (mod == pve)
				returnVal = comp_gameplay_pve(board, size, pl1, pl2, 'n', true, true, cmoveCount);

			else if (mod == pvp)
				returnVal = game_gameplay_pvp(board, size, pl1, pl2, true, firstPlayer);

		}
		else {
			cerr << "!!!NON-VALID SAVE FILE!!!\n";
		}
	}
	else
		cerr << "!!!FILE COULDN'T OPEN!!!\n";
	return returnVal;
}
