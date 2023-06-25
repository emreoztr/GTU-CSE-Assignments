#include <iostream>
#include "GameBoard.h"
#include "GameSys.h"
#include "GameAI.h"

using namespace std;


int main() {
	char board[12][12];
	char winner, ai_board;
	int size, choice;
	bool aiboard=false;

	cout << "************** HEX GAME **************" << endl << endl << "1. Player vs. Player" << endl << "2. Player vs. Computer" << endl << endl;
	cout << "Choose the Game Mode: ";
	cin >> choice;

	if(choice==1 || choice==2){
		size=gboard_prep(board);

		if(choice==1){
			winner = game_gameplay_pvp(board, size, p1, p2);
		}

		else if(choice==2){
			cout << "If you want to see the AI board, please write 'y', if not please write 'n' (y/n)\n";
			cin >> ai_board;
			winner=comp_gameplay_pve(board, size, p1, p2, ai_board);
		}

		cout << "**************************************" << endl;
		gboard_print(board, size);

		cout << endl <<  "**************************************" << endl << endl;

		if(winner!='\0')
			cout << "WINNER IS PLAYER " << winner << endl;
		else
			cout << "******DRAW*******" << endl;
	}
	else
		cerr << "!!!Please write a valid number!!!" << endl;

	return 0;
}


