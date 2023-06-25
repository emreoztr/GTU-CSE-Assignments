#include <iostream>
#include "GameBoard.h"

using namespace std;

int gboard_prep(char board[12][12]){ //board preparation function
	int size=0;

	cout << "Please choose table size (it can be from 6 to 12, for example if you write 6 a 6x6 game board will be created): " << endl;
	cin >> size; //Takes board size

	if((size>=6) && (size<=12))
		for(int i=0; i<size; ++i)		//Filling the game board with '.'
			for(int j=0; j<size; ++j)
				board[i][j]='.';

	else{
		size=0;
		cerr << "Please choose a valid table size!!!" << endl; //error for entering non-valid size
	}

	return size;  //returning the board size to caller function, it will be used later
}

void gboard_print(char board[12][12], int size){ //this function is for printing the current state of board
	char letter_row = 'a';

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



