#include <iostream>
#include <string>
#include "GameBoard.h"
#include "GameSys.h"
#include "GameFileManage.h"

using namespace std;

char game_take_command(string input, char board[12][12], const int size, const char player, const int moveCount, const gamemode& mode) {
	char returnVal='.';
	string com, filename;

	com = input.substr(0, input.find(' '));
	if (com == "SAVE" || com == "LOAD") {
		filename = input.substr(input.find(' ')+1, input.find('\n'));
		if (com == "SAVE") {
			returnVal = fman_save(filename, board, size, player, moveCount, mode);
		}
		else if (com == "LOAD")
			returnVal=fman_load(filename, board);
	}

	return returnVal;
}

char game_movement(char board[12][12], const int size, const char player, int position[2], const gamemode& mode, const int moveCount){ //player is symbol of the player, inside of the function user choose movement location
	char col_c, returnVal='.';
	int col=-1, row=-1;
	bool endVal = false, comCheck=false;
	string command;

	do{
		cout << "Please write your move player " << player << "(Write with a lower letter, i.e. 'a1')" << endl;
		getline(cin, command);
		returnVal=game_take_command(command, board, size, player, moveCount, mode);

		if (returnVal == '.') {
			col_c = command[0];

			row = stoi(&command[1]);
			--row;
			col = static_cast<int>(col_c - 'a');

			if (!(row<0 || row>(size - 1) || col<0 || col>(size - 1)) && board[row][col] == '.') {
				board[row][col] = player;
				endVal = true;
			}
			else
				cout << "!!!Please make a valid move!!!" << endl;
		}
		else
			endVal = true;
	}while(endVal==false);

	position[0]=col;
	position[1]=row;
	return returnVal;
}

void game_incr_values(int direction, int value[2]){

	switch(direction){
		case 0:
			value[0]=0;
			value[1]=-1;
			break;
		case 1:
			value[0]=1;
			value[1]=-1;
			break;
		case 2:
			value[0]=1;
			value[1]=0;
			break;
		case 3:
			value[0]=0;
			value[1]=1;
			break;
		case 4:
			value[0]=-1;
			value[1]=1;
			break;
		case 5:
			value[0]=-1;
			value[1]=0;
			break;
		default:
			value[0]=0;
			value[1]=0;
			break;
	}
}

bool game_wcond_check(char board[12][12], const int size, const char player, int x, int y, const char w_state){
	bool returnVal=false;
	int incrVal[2];

	if(board[y][x]==player){
		board[y][x]-=32;	//makes the letter upper case
		if((w_state=='b' && y==size-1) || (w_state=='t' && y==0) || (w_state=='r' && x==size-1) || (w_state=='l' && x==0))
			returnVal=true;


		for(int i=0; i<6 && returnVal==false; ++i){
			game_incr_values(i, incrVal);

			if((((x+incrVal[0])>=0 && (x+incrVal[0])<size) && ((y+incrVal[1])>=0 && (y+incrVal[1]<size))) && (board[y+incrVal[1]][x+incrVal[0]]==player)){
				returnVal=game_wcond_check(board, size, player, x+incrVal[0], y+incrVal[1], w_state);
			}
		}

		if(returnVal==false)
			board[y][x]+=32;
	}
	return returnVal;
}

void game_load_settings(const char board[12][12], const int size, int& move_count, bool& p1_wcond_left, bool& p1_wcond_right, bool& p2_wcond_bottom, bool& p2_wcond_top) {
	for (int i = 0; i < size; ++i) {
		for (int j = 0; j < size; ++j) {
			if (board[i][j] == pl1) {
				++move_count;
				if (j == 0)
					p1_wcond_left = true;
				else if (j == (size - 1))
					p1_wcond_right = true;
			}

			else if (board[i][j] == pl2) {
				++move_count;
				if ( i == 0)
					p2_wcond_top = true;
				else if (i == (size - 1))
					p2_wcond_bottom = true;
			}
		}
	}
}

char game_gameplay_pvp(char board[12][12], const int size, const char player1, const char player2, bool load, cell firstPlayer){ //holds all the gameplay system for pvp
	bool p1_win = false, p2_win = false, draw = false, firstTour=true;
	bool p1_wcond_left=false, p1_wcond_right=false, p2_wcond_bottom=false, p2_wcond_top=false;
	int p1_move[2], p2_move[2];
	int move_count=0;
	char returnVal='.';
	gamemode mod = pvp;

	if (load == true)
		game_load_settings(board, size, move_count, p1_wcond_left, p1_wcond_right, p2_wcond_bottom, p2_wcond_top);

	gboard_print(board, size);

	while(p1_win==false && p2_win==false && draw==false){ //always checking all situations
		if (firstTour == false || firstPlayer == pl1) {
			do {
				returnVal = game_movement(board, size, player1, p1_move, mod, move_count); //player 1 makes the move
			} while (returnVal == '-');

			if (returnVal == '.') {
				if (p1_move[0] == 0 && p1_wcond_left == false) //checks if the move in the left side
					p1_wcond_left = true;
				else if (p1_move[0] == size - 1 && p1_wcond_right == false) //checks if the move in the right side
					p1_wcond_right = true;

				if (p1_wcond_right == true && p1_wcond_left == true) //if player 1 has made moves in the both sides, program starts checking win situation
					for (int i = 0; i < size && p1_win == false; i++) //finds the coordinates of left side moves and sends it to the win condition check function
						p1_win = game_wcond_check(board, size, player1, 0, i, 'r'); //if there is a win, wcond_check function returns true

				move_count += 1; //increasing move_count by 1 for draw situations
				gboard_print(board, size); //prints board

				if (move_count == (size * size)) //if all of the board filled draw situation happens
					draw = true;
			}
			firstTour = false;
		}

		if ((firstTour == false || firstPlayer == pl2) && returnVal == '.') {
			if (p1_win == false && draw == false) { //if p1 didn't win and there is no draw then function takes the second player's move
				do {
					returnVal = game_movement(board, size, player2, p2_move, mod, move_count); //player 2 makes the move
				} while (returnVal == '-');

				if (returnVal == '.') {
					if (p2_move[1] == 0 && p2_wcond_top == false) //checks if the move in the top side
						p2_wcond_top = true;
					else if (p2_move[1] == size - 1 && p2_wcond_bottom == false) //checks if the move in the right side
						p2_wcond_bottom = true;

					if (p2_wcond_bottom == true && p2_wcond_top == true)  //if player 2 has made moves in the both sides, program starts checking win situation
						for (int i = 0; i < size && p2_win == false; ++i)  //finds the coordinates of top side moves and sends it to the win condition check function
							p2_win = game_wcond_check(board, size, player2, i, 0, 'b'); //if there is a win, wcond_check function returns true
					move_count += 1;  //increasing move_count by 1 for draw situations
					gboard_print(board, size);  //prints board
				}
			}
			if (move_count == (size * size)) //if all of the board filled draw situation happens
				draw = true;

			firstTour = false;
		}

		if (returnVal == pl1)
			p1_win = true;
		else if (returnVal == pl2)
			p2_win = true;
	}

	if (p1_win == true) {  //if p1 wins the game returnVal takes the character of player1
		returnVal = player1;
	}

	else if (p2_win == true) {
		returnVal = player2; //if p2 wins the game returnVal takes the character of player2
		
	}
	else	//if a draw situation happens returnVal takes the NULL character
		returnVal='\0';

	return returnVal;
}




