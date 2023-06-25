#include <iostream>
#include "GameBoard.h"
#include "GameSys.h"
#include "GameFileManage.h"

using namespace std;

int gboard_prep(char board[12][12]){ //board preparation function
	int size=0;

	cout << "Please choose table size (it can be from 6 to 12, for example if you write 6 a 6x6 game board will be created): " << endl;
	cin >> size; //Takes board size
	cin.ignore(1, '\n');
	if((size>=6) && (size<=12))
		for(int i=0; i<size; ++i)		//Filling the game board with '.'
			for(int j=0; j<size; ++j)
				board[i][j]='.';

	else{
		size=0;
		cout << "Please choose a valid table size!!!" << endl; //error for entering non-valid size
	}

	return size;  //returning the board size to caller function, it will be used later
}

void gboard_prep(char board[12][12], const int size) {
	if ((size >= 6) && (size <= 12))
		for (int i = 0; i < size; ++i)		//Filling the game board with '.'
			for (int j = 0; j < size; ++j)
				board[i][j] = '.';

	else {
		cerr << "!!!NON-VALID TABLE SIZE!!!" << endl; //error for entering non-valid size
	}
}

int gboard_find_new_size(const char board[12][12], int size) {
	int i;
	bool stop = false;
	for (i = 0; i < 12 && stop == false; ++i)
		if (board[0][i] != '.' && board[0][i]!=pl1 && board[0][i] != pl2 && board[0][i] != pl1_w && board[0][i] != pl2_w)
			stop = true;
	if (stop == false)
		i = size+1;
	return(i-1);
}

void gboard_print(char board[12][12], int size){ //this function is for printing the current state of board
	char letter_row = 'a';
	size = gboard_find_new_size(board, size);

	cout << " ";

	for(int i=-1; i<size; ++i){ //this is for column
		if(i>=0)
			cout << (i+1);   //prints the number of column

		for(int k=0; k<i; ++k){ //this loop slides the board to make a proper shape
			if(!(i>=9 && 0==k)) //this 'if' for prevent adding extra space if column number is bigger than 9
				cout << " ";
		}

		for(int j=0; j<size; ++j){	//this is for row
			cout << " ";
			if(-1 == i)
				cout << (letter_row++);	//prints the letter of row at start
			else
				cout << board[i][j];		//prints the board
		}
		cout << endl;	//jumping line to print a proper board
	}
}

void gboard_fill_empty(char board[12][12]) {
	for (int i = 0; i < 12; ++i) {
		for (int j = 0; j < 12; ++j)
			board[i][j] = '\0';
	}
}



