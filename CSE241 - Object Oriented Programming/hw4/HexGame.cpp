#include "HexGame.h"
#include <iostream>
#include <string>
#include <vector>
#include <fstream>

using namespace std;

Hex* Hex::games = new Hex[5];
int Hex::maxgameCount = 5;
int Hex::gameCount = 5;
int Hex::totalnumMoves = 0;

/*BIG THREE*/
const Hex & Hex::operator=(const Hex &rValue){

	if (this != &rValue)
	{
		if(allMoves!=nullptr)
			delete[] allMoves;
		allMoves = new Cell *[rValue.maxcountLastmove];
		mod = rValue.getGamemode();
		if(compMoves!=nullptr)
			delete[] compMoves;
		compMoves = new Cell[rValue.maxcountcompMove];
		lastMove = rValue.lastMove;
		winner = rValue.winner;
		turnOwner = rValue.turnOwner;
		p1wcondL = rValue.p1wcondL;
		p1wcondR = rValue.p1wcondR;
		p2wcondD = rValue.p2wcondD;
		p2wcondU = rValue.p2wcondU;
		setBoardsize(rValue.getBoardsize());
		numberMoves = rValue.numberMoves;
		compmoveCount = rValue.compmoveCount;
		activeGame = rValue.activeGame;
		compboardmoveCount = rValue.compboardmoveCount;
		maxcountLastmove = rValue.maxcountLastmove;
		maxcountcompMove = rValue.maxcountcompMove;

		if (rValue.hexCells != nullptr)
		{
			if(hexCells!=nullptr)
				deleteBoard();

			createBoard();

			for (int i = 0; i < getBoardsize(); ++i){
				for (int j = 0; j < getBoardsize(); ++j){
					getCell(j, i).setter(rValue.lookCell(j, i).takeValue());
				}
			}

			for (int i = 0; i < rValue.getnumberMoves(); ++i){
				allMoves[i] = &getCell(rValue.allMoves[i]->takeCol(), rValue.allMoves[i]->takeRow());
			}
		}

		if(rValue.compBoard!=nullptr){
			if(compBoard!=nullptr)
				deletecompBoard();
			createcompBoard();
			for (int i = 0; i < getBoardsize(); ++i){
				for (int j = 0; j < getBoardsize(); ++j){
					getcompCell(j, i).setter(rValue.lookcompCell(j, i).takeValue());
				}
			}

			for (int i = 0; i < compmoveCount; ++i){
				compMoves[i] = rValue.compMoves[i];
			}
		}


	}

	return *this;
}

Hex::Hex(const Hex &mainObj) : hexCells(nullptr), compBoard(nullptr), compMoves(nullptr), allMoves(nullptr){

	(*this) = mainObj; //calling assignment operator because copy constructor and assignment is likely same.
}

Hex::~Hex(){
	deleteBoard();
	deletecompBoard();
	if(allMoves!=nullptr){
		delete[] allMoves;
		allMoves = nullptr;
	}

	if(compMoves!=nullptr){
		delete[] compMoves;
		compMoves = nullptr;
	}
}

/*Operator Overloads*/

Hex &Hex::operator--(){
	if(numberMoves>0){	//looks if there is a move can be delete or not
		allMoves[numberMoves - 1]->setter(emp); //make the last move's cellstate empty
		if(getGamemode()==pve && getTurnowner()==player1){ //if it is a player vs computer game this part deletes the last move from AI board too and deletes the last user move too.
			compBoard[allMoves[numberMoves - 1]->takeRow()][allMoves[numberMoves - 1]->takeCol()].setter(emp);
			compmoveCount--;
			if(numberMoves>1){
				allMoves[numberMoves - 2]->setter(emp);
				totalnumMoves--;
				numberMoves--;
			}
		}
		totalnumMoves--;	//decreasing number of moves in all games
		numberMoves--;		//decreasing number of moves in this game

		if(getGamemode()==pvp)	//if game mode is plaver vs. player it will change the turn, else it doesnt change the turn because move of the player will be deleted too.
			changeTurnowner();	
	}
	else{
		cout << "No move to delete!\n";
	}

	return *this;	//returns caller object
}

Hex Hex::operator--(int placeHolder){ //only difference from the pre decrement is; it isreturning a copy of old game object
	Hex temp = *this;
	if (numberMoves > 0)
	{
		allMoves[numberMoves - 1]->setter(emp);
		if(getGamemode()==pve && getTurnowner()==player1){
			compBoard[allMoves[numberMoves - 1]->takeRow()][allMoves[numberMoves - 1]->takeCol()].setter(emp);
			compmoveCount--;
			if(numberMoves>1){
				allMoves[numberMoves - 2]->setter(emp);
				totalnumMoves--;
				numberMoves--;
			}
		}
		totalnumMoves--;
		numberMoves--;

		if(getGamemode()==pvp)
			changeTurnowner();
	}
	else{
		cout << "No move to delete!\n";
	}

	return temp;
}

bool Hex::operator==(const Hex &rValue) const{  //this function compares two games
	bool returnVal;
	int firstCount, secondCount;

	if(getGamemode()==pvp) //if it is pvp it takes the number of moves from first game
		firstCount = getnumberMoves();
	else	//if it is pve it takes number of moves made by USER
		firstCount = getnumberMoves() - getcompnumberMoves();
	
	if(rValue.getGamemode()==pvp)	//same thing above, but this part takes the move count of second game
		secondCount = rValue.getnumberMoves();
	else
		secondCount = rValue.getnumberMoves() - rValue.getcompnumberMoves();

	
	if(firstCount > secondCount)
		returnVal = true;
	
	else
		returnVal = false;

	return returnVal;
}

ofstream& operator<<(ofstream &file, const Hex& rValue){ //this part is for saving the game
	if(file.is_open()){
		file << "!\n";
		/*SAVES ALL THE NUMERIC DATA TO FILE*/
		file << static_cast<int>(rValue.mod) << endl;
		file << rValue.boardSize << endl;
		file << rValue.numberMoves << endl;
		file << rValue.compmoveCount << endl;
		file << rValue.p1wcondL << endl;
		file << rValue.p1wcondR << endl;
		file << rValue.p2wcondD << endl;
		file << rValue.p2wcondU << endl;
		file << rValue.turnOwner << endl;
		file << rValue.winner << endl;
		file << rValue.activeGame << endl;
		file << rValue.compboardmoveCount << endl;
		file << rValue.lastMove.takeCol() << " " << rValue.lastMove.takeRow() << " " << rValue.lastMove.takeValue() << endl;

		/* SAVES MOVES OF THE COMPUTERS IF IT IS A PVE GAME */
		file << "-\n";
		for (int i = 0; i < rValue.compmoveCount; ++i)
			file << rValue.compMoves[i].takeCol() << " " << rValue.compMoves[i].takeRow() << " " << rValue.compMoves[i].takeValue() << endl;
		file << "-\n";

		/* SAVES THE AI BOARD TO FILE BY SENDS COORDINATES AND VALUE OF THE POINTS */
		file << "=\n";
		for (int i = 0; i < rValue.boardSize && rValue.mod == pve; ++i)
			for (int j = 0; j < rValue.boardSize; ++j)
				if(rValue.lookcompCell(j, i).takeValue()!=emp)
					file << rValue.lookcompCell(j, i).takeCol() << " " << rValue.lookcompCell(j, i).takeRow() << " " << rValue.lookcompCell(j, i).takeValue() << endl;
		file << "=\n";

		/* SAVES THE MAIN BOARD TO FILE BY SENDS COORDINATES AND VALUE OF THE POINTS */
		file << "{\n";
		for (int i = 0; i < rValue.boardSize; ++i)
			for (int j = 0; j < rValue.boardSize; ++j)
				if(rValue.lookCell(i, j).takeValue()!=emp)
					file << rValue.lookCell(i, j).takeCol() << " " << rValue.lookCell(i, j).takeRow() << " " << rValue.lookCell(i, j).takeValue() << endl;
		file << "}\n";

		/* SAVES ALL THE MOVES HAPPENED IN GAME BY ORDER FOR UNDO SITUATIONS */
		file << "*\n";
		for (int i = 0; i < rValue.getnumberMoves(); ++i)
		{
			file << rValue.allMoves[i]->takeCol() << " " << rValue.allMoves[i]->takeRow() << endl;
		}
		file << "*\n";
		file << "!";
	}
	else
	{
		cerr << "FILE COULDN'T OPEN!!!" << endl;
	}

	return file;
}

ifstream& operator>>(ifstream &file, Hex &load){	//this part is for loading the game
	int holder, col, row, value, numMoves, numcompMoves;
	string charHolder;
	Hex::Cell new_cell;

    if(file.is_open()){
		getline(file, charHolder);
		if(charHolder=="!"){
			/*LOADS ALL THE NUMERIC DATA TO FILE*/
			file >> holder;
			file.ignore(1, '\n');
			load.setGamemode(static_cast<GameMode>(holder));
			file >> holder;
			file.ignore(1, '\n');
			load.setBoardsize(holder);
			file >> numMoves;
			file.ignore(1, '\n');
            file >> numcompMoves;
            file.ignore(1, '\n');
			file >> holder;
			file.ignore(1, '\n');
			load.p1wcondL = static_cast<bool>(holder);
			file >> holder;
			file.ignore(1, '\n');
			load.p1wcondR = static_cast<bool>(holder);
			file >> holder;
			file.ignore(1, '\n');
			load.p2wcondD = static_cast<bool>(holder);
			file >> holder;
			file.ignore(1, '\n');
			load.p2wcondU = static_cast<bool>(holder);
			file >> holder;
			file.ignore(1, '\n');
			load.turnOwner = static_cast<CellState>(holder);
			file >> holder;
			file.ignore(1, '\n');
			load.winner = static_cast<CellState>(holder);
			file >> holder;
			file.ignore(1, '\n');
			load.createBoard();
			if(load.getGamemode()==pve)
				load.createcompBoard();
            load.activeGame = 1;
            file >> holder;
            file.ignore(1, '\n');
			if(load.getGamemode()==pve)
				load.compboardmoveCount = holder;
			file >> col >> row >> value;
			file.ignore(1, '\n');
			load.lastMove.setter(col, row, static_cast<CellState>(value));
			getline(file, charHolder);

			/* LOADS MOVES OF THE COMPUTERS IF IT IS A PVE GAME */
			if(charHolder=="-" && load.getGamemode()==pve ){
				for (int i = 0; i < numcompMoves; ++i){
					file >> col >> row >> value;
					file.ignore(1, '\n');
					new_cell.setter(col, row, static_cast<CellState>(value));
					load.compmoveCount++;
					load.addcompMove(new_cell);
				}
			}
			getline(file, charHolder);
			getline(file, charHolder);

			/* LOADS THE AI BOARD TO FILE BY SENDS COORDINATES AND VALUE OF THE POINTS */
			if(charHolder=="=" && load.getGamemode()==pve){	
				for (int j = 0; j < load.compboardmoveCount; ++j){
					file >> col >> row >> value;
					file.ignore(1, '\n');
					load.getcompCell(col, row).setter(static_cast<CellState>(value));
				}
			}
			getline(file, charHolder);
			getline(file, charHolder);
			/* LOADS THE MAIN BOARD TO FILE BY SENDS COORDINATES AND VALUE OF THE POINTS */
			if(charHolder=="{"){
				for (int j = 0; j < numMoves; ++j){
					file >> col >> row >> value;
					file.ignore(1, '\n');
					load.getCell(col, row).setter(static_cast<CellState>(value));
				}
			}
			getline(file, charHolder);
			getline(file, charHolder);
			/* LOADS ALL THE MOVES HAPPENED IN GAME BY ORDER FOR UNDO SITUATIONS */
			if(charHolder=="*"){
				for (int i = 0; i < numMoves; ++i){
					file >> col >> row;
					file.ignore(1, '\n');
					(load.numberMoves)++;
					load.addlastMove(&load.getCell(col, row));
				}
			}
			getline(file, charHolder);
			getline(file, charHolder);
			if(charHolder=="!")
				cout << "GAME LOADED SUCCESSFULLY"<< endl;
			else
				cerr << "A PROBLEM HAPPENED WHILE LOADING!!!" << endl;
            Hex::totalnumMoves += load.getnumberMoves(); //adding number of moves from this save file to static data
		}
		else{
			cerr << "A PROBLEM HAPPENED WHILE LOADING!!!" << endl;
		}
	}
	else{
			cerr << "A PROBLEM HAPPENED WHILE LOADING!!!" << endl;
	}

	return file;
}

ostream& operator<<(ostream& screen, const Hex& game){	//This function prints the board from Hex object
	Hex::Cell temp;
	char startChar = 'a';

    screen << "\n ";
	for (int i = 0; i <= game.getBoardsize() - 1; ++i){
		screen << startChar;
		startChar += 1;
		screen << " ";
	}
	screen << endl;
	for (int i = 0; i < game.getBoardsize(); ++i){
		screen << i + 1;
		for (int k = 0; k < i; ++k){
            if(!(k < Hex::findintLength(i+1)-1))     //for spacing problem
				screen << " ";
		}
		for (int j = 0; j < game.getBoardsize(); ++j)
		{
			if (game.lookCell(j, i).takeValue() == player1)
				screen << 'x';
			else if (game.lookCell(j, i).takeValue() == player2)
				screen << 'o';
			else if (game.lookCell(j, i).takeValue() == emp)
				screen << '.';
			else if (game.lookCell(j, i).takeValue() == p1_win)
				screen << 'X';
			else if (game.lookCell(j, i).takeValue() == p2_win)
				screen << 'O';

			screen << " ";
		}
		screen << endl;
	}

	return screen;
}

void Hex::calculateScore(){	//this function calculates score when needed
	int incrVal[2];
	CellState val;
	int colScore=0, rowScore=0, x, y;
	for (int i = 0; i < getnumberMoves(); ++i)//looks all the moves
	{
		val = allMoves[i]->takeValue();
		for (int j = 0; j < 6 && val==player1; ++j){	//looks the neigbors of the move
			incrValues(incrVal, j);
			x = allMoves[i]->takeCol() + incrVal[0];
			y = allMoves[i]->takeRow() + incrVal[1];
			if ((x >= 0 && x < boardSize) && (y >= 0 && y < boardSize)){
				if (lookCell(x, y).takeValue() == player1)
				{
					rowScore += 10;		//if neighbor of the move is at the correct way rowScore increases
				}
			}
		}
		for (int j = 0; j < 4 && val==player2; ++j){	//looks the neigbors of the move
			incrValues(incrVal, j);
			x = allMoves[i]->takeCol() + incrVal[0];
			y = allMoves[i]->takeRow() + incrVal[1];
			if ((x >= 0 && x < boardSize) && (y >= 0 && y < boardSize)){
				if (lookCell(x, y).takeValue() == player2)
				{
					colScore += 10;	//if neighbor of the move is at the correct way rowScore increases
				}
			}
		}
	}
	player1Score = rowScore;
	player2Score = colScore;

	while(player1Score>=maxScore || player2Score>=maxScore)	//if one of the initial score passed the max score this part multiplies maxScore by 10 until it pass scores of players
		maxScore *= 10;
}


void Hex::deleteBoard(){	//this function deletes the board to prevent memory leak
	if(hexCells!=nullptr){
		for (int i = 0; i < getBoardsize(); ++i){
			delete[] hexCells[i];
		}
		delete[] hexCells;
		hexCells = nullptr;
	}
}

void Hex::deletecompBoard(){	//this function deletes the AI board to prevent memory leak
	if(compBoard!=nullptr){
		for (int i = 0; i < getBoardsize(); ++i){
			delete[] compBoard[i];
		}
		delete[] compBoard;
		compBoard = nullptr;
	}
}

void Hex::addlastMove(Cell *lMove){	//adds last move to the array of last moves, increments the array size if needed
	Cell **temp;
	if (maxcountLastmove < getnumberMoves())
	{	
		maxcountLastmove *= 2;
		temp = new Cell *[maxcountLastmove];
		if(temp!=nullptr){
			for (int i = 0; i < getnumberMoves()-1; ++i){
				temp[i] = allMoves[i];
			}
			delete[] allMoves;
			allMoves = temp;
		}
		else{
			cerr << "Not enough memory!\n";
			exit(1);
		}
	}
	allMoves[getnumberMoves() - 1] = lMove;
}

void Hex::setcompboardmoveCount(){
	int count=0;
	for (int i = 0; i < getBoardsize(); ++i){
		for (int j = 0; j < getBoardsize(); ++j){
			if(lookcompCell(j, i).takeValue()!=emp)
				++count;
		}
	}
	compboardmoveCount = count;
}

Hex &Hex::createGame(){
	gameCount++;
	if(gameCount>maxgameCount){
		Hex *new_games;
		maxgameCount += 5;
		new_games = new Hex[maxgameCount];

		for (int i = 0; i < gameCount-1; ++i)
			new_games[i] = games[i];
		
		if(games!=nullptr)
			delete[] games;

		games = new_games;
	}
	return games[gameCount - 1];
}

void Hex::printGames(){
	for (int i = 0; i < gameCount; ++i){
		cout << i + 1 << ". Game" << endl;
	}
}


Hex &Hex::chooseGame(const int choice){
	if (choice > gameCount || choice <= 0){
		cerr << "Program has not the game spesified from choice!\n";
		exit(1);
	}

	return (games[choice - 1]);
}
	
int Hex::findCommand(const string &com){
	int returnVal;
	Hex debug;
	string first_part;
	string filename;
	ofstream outfile;
	ifstream infile;

	first_part = com.substr(0, com.find(' '));

	if(first_part=="MENU"){
		returnVal = 1;
	}
	else if(first_part=="SAVE"){
		if (getGamemode() == pve)
			setcompboardmoveCount();
		filename = com.substr(com.find(' ')+1, com.find('\n'));
		outfile.open(filename);
		outfile << (*this);
		cout << "GAME SAVED SUCCESSFULLY IN " << filename << endl;
		outfile.close();
		returnVal = 2;
	}
	else if(first_part=="LOAD"){
		filename = com.substr(com.find(' ')+1, com.find('\n'));
		Hex &load = createGame();
		infile.open(filename);
		infile >> load;
		infile.close();
		returnVal = 3;
	}
	else if(first_part=="UNDO"){
		(*this)--;
		cout << (*this);
		returnVal = 4;
	}
	else if(first_part=="SCORE"){
		calculateScore();
		cout << "First player's score is: " << getP1score() << endl;
		cout << "Second player's score is: " << getP2score() << endl;
		returnVal = 5;
	}
	else
	{
		returnVal = 6;
	}

	return returnVal;
}


inline const Hex::Cell &Hex::lookcompCell(const int col, const int row) const{
	return (compBoard[row][col]);
}

void Hex::checkWin(){
	Cell temp;
	if(p1wcondL == true && p1wcondR == true){
		for (int i = 0; i < getBoardsize(); ++i){
			temp = getCell(0, i);
			if(temp.takeValue()==player1)
				if(checkWcond(temp.takeCol(), temp.takeRow(), player1))
					winner = player1;
		}
	}
	if(p2wcondU == true && p2wcondD == true){
		for (int i = 0; i < getBoardsize(); ++i){
			temp = getCell(i, 0);
			if(temp.takeValue()==player2)
				if(checkWcond(temp.takeCol(), temp.takeRow(), player2))
					winner = player2;
		}
	}

	if(winner==player1 || winner==player2){
		calculateScore();
		if (winner == player1)
			player1Score = maxScore;
		else if(winner==player2)
			player2Score = maxScore;
		cout << "*******************************************" << endl;
		cout << "WINNER IS PLAYER" << winner << endl;
		cout << "Score of first player is: " << getP1score() << endl;
		cout << "Score of second player is: " << getP2score() << endl;
	}
}

bool Hex::checkWcond(const int col, const int row, const CellState player){
	bool win = false;
	int incrVal[2];

	if(player==player1 && (col>=0 && col<boardSize) && (row>=0 && row<boardSize)){
		if(lookCell(col, row).takeValue()==player1){
			setCell(getCell(col, row), p1_win);

			if(col==getBoardsize()-1){
				winner = player;
				win = true;
			}

			for (int i = 0; i < 6 && win==false; ++i){
				incrValues(incrVal, i);
				win = checkWcond(col + incrVal[0], row + incrVal[1], player);
			}

			if(win==false)
				setCell(getCell(col, row), player1);
		}
	}

	else if(player==player2 && (col>=0 && col<boardSize) && (row>=0 && row<boardSize)){
		if(lookCell(col, row).takeValue()==player2){
			setCell(getCell(col, row), p2_win);

			if(row==getBoardsize()-1){
				winner = player;
				win = true;
			}

			for (int i = 0; i < 6 && win==false; ++i)
			{
				incrValues(incrVal, i);
				win = checkWcond(col + incrVal[0], row + incrVal[1], player);
			}

			if(win==false)
				setCell(getCell(col, row), player2);
		}
	}

	return win;
}

void Hex::incrValues(int value[2], const int direction) const{
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
			value[0]=0;
			value[1]=1;
			break;
		case 3:
			value[0]=-1;
			value[1]=1;
			break;
		case 4:
			value[0]=1;
			value[1]=0;
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

bool Hex::testHex() const
{
	bool returnVal = true;

	if(boardSize<6){
		cerr << "Non-Valid Board Size!!!\n";
		exit(1);
		returnVal = false;
	}

	return returnVal;
}

void Hex::createBoard(){
	Cell *new_cell;
	if(hexCells==nullptr){
		hexCells = new Cell *[getBoardsize()];
		for (int i = 0; i < getBoardsize(); ++i)
		{
			new_cell = new Cell[getBoardsize()];

			if(new_cell!=nullptr){
				hexCells[i] = new_cell;
				for (int j = 0; j < getBoardsize(); ++j)
					new_cell[j].setter(j, i, emp);
			}

			else{
				cout << "Not enough memory!\n";
				exit(1);
			}
		}
	}
}

int Hex::findintLength(int num){
    int returnVal=0;
    while (num != 0){
        num /= 10;
        returnVal++;
    }
    return returnVal;
}


void Hex::playGame(){
	int gmode, bsize, col, row, comChoice;
	char colChar;
	GameMode m;
	string command;
	if(getgameActivity()==false){		//this is start part, if game created already it wont get in this section
		cout << "****************HEX GAME****************\n\n";
		cout << "1. Player vs. Player\n2.Player vs. Computer\n\n";
		do{
			cout << "Please choose the game mode: ";
			cin >> gmode;
			m = static_cast<GameMode> (gmode);
			cin.ignore(1, '\n');
		}while(!setGamemode(m));
		
		do{
			cout << "Please write board size (ex.: Write 6 to create 6x6 board): ";
			cin >> bsize;
			cin.ignore(1, '\n');
		}while(!setBoardsize(bsize));

		createBoard();
		gameisActive();
	}
	
	if(getGamemode()==pvp){				//this is for user vs user
		while(isWin()==emp){
			cout << (*this);
			do
			{
				cout << "Player " << static_cast<int> (getTurnowner()) << " please make your move (ex.: A3): ";
				getline(cin, command);
				comChoice = findCommand(command);
			} while (comChoice == 2 || comChoice == 4 || comChoice == 5); //if user saves game it takes another command

			if(comChoice!=1 && comChoice!=3){	//if user writes MENU or LOAD it will terminate playGame() function
				col = static_cast<int>(command[0] - 'A');
				row = stoi(&command[1]) - 1;
				if(isAccessible(col, row)){		//checks if the place accessible
					if(!play(getCell(col, row)))	//play(Cell) sends correct Cell, if that place is accessible
						cout << "Please make a valid move!!!\n";
					else{		//if it is a valid move it checks is it a win state or not
						addlastMove(&getCell(col, row));
						if(row==0 && getTurnowner()==player1)
							p2wcondU = true;
						else if(row==getBoardsize()-1 && getTurnowner()==player1)
							p2wcondD = true;
						if(col==0 && getTurnowner()==player2)
							p1wcondL = true;
						else if(col==getBoardsize()-1 && getTurnowner()==player2)
							p1wcondR = true;
					}
					checkWin();
				}
				else
					cout << "Please make a valid move!!!\n";
			}
			else
				winner = no;	//if it terminates the playGame, it shouldnt be emp to quit the while loop
		}
	}
	else if(getGamemode()==pve){	//user vs computer
		createcompBoard();	//prepares computer board
		while(isWin()==emp){
			cout << (*this);

			if(getTurnowner()==player1){
				do
				{
					cout << "Player " << static_cast<int> (getTurnowner()) << " please make your move (ex.: A3): ";
					getline(cin, command);
					comChoice = findCommand(command);
				} while (comChoice == 2 || comChoice == 5);
				if(comChoice!=1 && comChoice!=3 && comChoice!=4){
					col = static_cast<int>(command[0] - 'A');
					row = stoi(&command[1]) - 1;
					if(isAccessible(col, row)){
						if(!play(getCell(col, row)))	//user makes move here
							cout << "Please make a valid move!!!\n";
						else{
							addlastMove(&getCell(col, row));
							if(col==0)
								p1wcondL = true;
							else if(col==boardSize-1)
								p1wcondR = true;
							comp_save_enemy_move();
							checkWin();
						}
					}
					else
						cout << "Please make a valid move!!!\n";
				}
				else if(comChoice!=4){
					winner = no;
				}
			}

			else if(getTurnowner()==player2 && winner==emp){
				addlastMove(&play()); //if user didnt win, computer plays his move here
				if (compMoves[compmoveCount - 1].takeRow() == 0)
					p2wcondU = true;
				else if(compMoves[compmoveCount-1].takeRow()==boardSize-1)
					p2wcondD = true;
				checkWin();
			}
		}
	}
	if(winner==no){	//turning it to emp
		winner = emp;
	}
	else
		cout << (*this);
}

bool Hex::play(Cell &position){
	bool returnVal = false;
	if(position.takeValue()==emp && isAccessible(position.takeCol(), position.takeRow())){
		setCell(position, getTurnowner());
		lastMove = position;
		changeTurnowner();
		numberMoves++;
		totalnumMoves++;
		returnVal = true;
	}

	return returnVal;
}

void Hex::changeTurnowner(){
	if(turnOwner==player1)
		turnOwner = player2;
	
	else if(turnOwner==player2)
		turnOwner = player1;
}

Hex::Cell &Hex::play(){
	Cell *returnVal;
	comp_reset_path();

	compmoveCount++;
	if(numberMoves==0)
		returnVal=comp_first_move();

	else if (numberMoves <= 1){
		returnVal=comp_first_move(lastMove.takeRow());
	}
	
	else{
		comp_find_path();
		returnVal=comp_put_move();
	}
	numberMoves++;
	totalnumMoves++;
	changeTurnowner();

	return *returnVal;
}

void Hex::comp_incr_values_upleft(const int direction, int value[2]) const{	//incrementation values for up leftt direction

	switch (direction) {
	case 0:
		value[0] = 0;
		value[1] = -1;
		break;
	case 1:
		value[0] = -1;
		value[1] = 0;
		break;
	case 2:
		value[0] = 1;
		value[1] = -1;
		break;
	case 3:
		value[0] = 1;
		value[1] = 0;
		break;
	default:
		value[0] = 0;
		value[1] = 0;
		break;
	}
}

void Hex::comp_incr_values_dnleft(const int direction, int value[2]) const{	//incrementation values for down leftt direction

	switch (direction) {
	case 0:
		value[0] = -1;
		value[1] = 1;
		break;
	case 1:
		value[0] = 0;
		value[1] = 1;
		break;
	case 2:
		value[0] = -1;
		value[1] = 0;
		break;
	case 3:
		value[0] = 1;
		value[1] = 0;
		break;
	default:
		value[0] = 0;
		value[1] = 0;
		break;
	}
}

void Hex::comp_incr_values_upright(const int direction, int value[2]) const{		//incrementation values for up right direction

	switch (direction) {
	case 0:
		value[0] = 1;
		value[1] = -1;
		break;
	case 1:
		value[0] = 0;
		value[1] = -1;
		break;
	case 2:
		value[0] = 1;
		value[1] = 0;
		break;
	case 3:
		value[0] = -1;
		value[1] = 0;
		break;
	default:
		value[0] = 0;
		value[1] = 0;
		break;
	}
}

void Hex::comp_incr_values_dnright(const int direction, int value[2]) const{	//incrementation values for down right direction

	switch (direction) {
	case 0:
		value[0] = 0;
		value[1] = 1;
		break;
	case 1:
		value[0] = 1;
		value[1] = 0;
		break;
	case 2:
		value[0] = -1;
		value[1] = 1;
		break;
	case 3:
		value[0] = -1;
		value[1] = 0;
		break;
	default:
		value[0] = 0;
		value[1] = 0;
		break;
	}
}

bool Hex::isAccessible(const int col, const int row) const{
	bool returnVal = false;

	if(col<boardSize && col>=0 && row<boardSize && row>=0)
		returnVal = true;

	return returnVal;
}

bool Hex::comp_is_empty(const int loc_col, const int loc_row) const {	//checks the place is safe or not
	bool returnVal=false;
	if(isAccessible(loc_col, loc_row))
		if(lookcompCell(loc_col, loc_row).takeValue()!=enemy && lookcompCell(loc_col, loc_row).takeValue()!=closed && lookcompCell(loc_col, loc_row).takeValue()!=comp_closed)
			returnVal=true;

	return returnVal;
}

int Hex::comp_check_row(const int col, const int row) const{	//checks the enemy in the row
	int returnVal=-1;
	bool end_look=false;

	for(int i=0; i<col; ++i){
		if(lookcompCell(i, row).takeValue()==enemy){
			returnVal=col-i;
			end_look=true;
		}
		if(returnVal!=-1 && (lookcompCell(i, row).takeValue()==comp || lookcompCell(i, row).takeValue()==comp_closed)){
			returnVal=-1;
			end_look=false;
		}
	}

	for(int i=boardSize-1; i>col && !end_look; --i){
		if(lookcompCell(i, row).takeValue()==enemy)
			returnVal=i-col;
		if(returnVal!=-1 && (lookcompCell(i, row).takeValue()==comp || lookcompCell(i, row).takeValue()==comp_closed)){
			returnVal=-1;
		}
	}

	return returnVal;
}

int Hex::comp_check_row_risk(const int col, const int row) const{	//checks the risk value in a row
	int returnVal=-1;
	bool end_look=false;


	for(int i=0; i<col; ++i){
		if(lookcompCell(i, row).takeValue()==risk){
			returnVal=col-i;
			end_look=true;
		}
		if(returnVal!=-1 && (lookcompCell(i, row).takeValue()==comp || lookcompCell(i, row).takeValue()==comp_closed)){
			returnVal=-1;
			end_look=false;
		}
	}

	for(int i=boardSize-1; i>col && !end_look; --i){
		if(lookcompCell(i, row).takeValue()==risk)
			returnVal=i-col;
		if(returnVal!=-1 && (lookcompCell(i, row).takeValue()==comp || lookcompCell(i, row).takeValue()==comp_closed)){
			returnVal=-1;
		}
	}

	return returnVal;
}

int Hex::comp_check_risk(const int col, const int row) const{	//checks the risk value for edges of place
	int r_count=0;
	int incrVal[2];
	for (int i = 0; i < 6; i++)
	{
		incrValues(incrVal, i);
		if(isAccessible(col+incrVal[0], row+incrVal[1]))
			if(lookcompCell(col+incrVal[0], row+incrVal[1]).takeValue()==risk)
				r_count+=1;
	}
	if((col==boardSize-1 || col==0) && r_count>=1)
		r_count=5;
	return r_count;
}

int Hex::comp_check_neighbor(const int col, const int row) const{	//returns the count of already moved places at the edges of place
	int c_count=0;
	int incrVal[2];

	for(int i=0; i<6; i++){
		incrValues(incrVal, i);
		if(isAccessible(col+incrVal[0], row+incrVal[1]))
			if(lookcompCell(col+incrVal[0], row+incrVal[1]).takeValue()==comp)
				c_count+=1;
	}

	return c_count;
}

void Hex::comp_reset_path(){	//resets L(Low priority values), H(High priority values) and K(Highest priority values)
	for(int i=0; i<boardSize; ++i){
		for(int j=0; j<boardSize; ++j){
			if(lookcompCell(j, i).takeValue()==l_priority || lookcompCell(j, i).takeValue()==h_priority || lookcompCell(j, i).takeValue()==k_priority)
				setCell(getcompCell(j, i), emp);
		}
	}
}

bool Hex::comp_update_pathfinding_upright(const int col, const int row, int& count){
	int incrVal[2];
	bool returnVal=false;
	CellState holderBf, holderAf;
	if (lookcompCell(col, row).takeValue() != comp)
		count += 1;

	if (count < 4 * boardSize)
	{
		holderBf = lookcompCell(col, row).takeValue(); //saving for in case turning it back
		holderAf = lookcompCell(col, row).takeValue();
		if (holderBf != comp){
			if ((comp_check_row(col, row) <= 1 && comp_check_row(col, row) >= 0) || comp_check_risk(col, row) >= 4 || holderBf == risk)
				holderAf = k_priority;
			else if (comp_check_risk(col, row) >= 1)
				holderAf = h_priority;
			
			else
				holderAf = l_priority;	
		}
	
		setCell(getcompCell(col, row), closed); //changing the name of the place to make sure the program wont go to this place again
		if (holderBf == comp)
			setCell(getcompCell(col, row), comp_closed); //if it is move of the computer program will change it to the W instead of Q

		if (row == 0) //if it is at the top, path condition satisfies and return true to end recursive
			returnVal = true;

		for (int i = 0; i < 4 && returnVal == false; ++i)
		{ //taking the increment values and checks the new place is safe or not
			comp_incr_values_upright(i, incrVal);

			returnVal = comp_is_empty(col + incrVal[0], row + incrVal[1]);

			if (returnVal == true)
				returnVal = comp_update_pathfinding_upright(col + incrVal[0], row + incrVal[1], count);
		}

		if (returnVal == false)
		{ //if it has not succeed it takes the value of place back
			setCell(getcompCell(col, row), holderBf);
			if (count < 4 * boardSize) //if counter is more than the border, it doesn't decrease counter
				count -= 1;
		}
		else
		{
			setCell(getcompCell(col, row), holderAf); //if it has succeed place takes the new value
		}
	}
	return returnVal;
}

bool Hex::comp_update_pathfinding_dnright(const int col, const int row, int& count){
	int incrVal[2];
	bool returnVal=false;
	CellState holderBf, holderAf;

	if(lookcompCell(col, row).takeValue()!=comp)
		count+=1;

	if(count<4*boardSize){
		
		holderBf = lookcompCell(col, row).takeValue(); //saving for in case turning it back
		holderAf = lookcompCell(col, row).takeValue();

		if(holderBf!=comp){	//in this if section program choose the priority for the place
			if((comp_check_row(col, row)<=1 && comp_check_row(col, row)>=0) || comp_check_risk(col, row)>=4 || holderBf==risk)
				holderAf=k_priority;
			else if(comp_check_risk(col, row)>=1)
				holderAf=h_priority;
			else
				holderAf=l_priority;
		}

		setCell(getcompCell(col, row), closed);	//changing the name of the place to make sure the program wont go to this place again
		if(holderBf==comp)
			setCell(getcompCell(col, row), comp_closed);	//if it is move of the computer program will change it to the W instead of Q

		if(row==boardSize-1)	//if it is at the top, path condition satisfies and return true to end recursive
			returnVal=true;

		for(int i=0; i<4 && returnVal==false; ++i){	//taking the increment values and checks the new place is safe or not
			comp_incr_values_dnright(i, incrVal);

			returnVal=comp_is_empty(col+incrVal[0], row+incrVal[1]);

			if(returnVal==true)
				returnVal=comp_update_pathfinding_dnright(col+incrVal[0], row+incrVal[1], count);
		}

		if(returnVal==false){	//if it has not succeed it takes the value of place back
			setCell(getcompCell(col, row), holderBf);
			if(count<4*boardSize)	//if counter is more than the border, it doesn't decrease counter
				count-=1;
		}
		else{
			setCell(getcompCell(col, row), holderAf);	//if it has succeed place takes the new value
		}
	}
	return returnVal;
}

bool Hex::comp_update_pathfinding_upleft(const int col, const int row, int& count){
	int incrVal[2];
	bool returnVal=false;
	CellState holderBf, holderAf;

	if(lookcompCell(col, row).takeValue()!=comp)
		count+=1;

	if(count<4*boardSize){
		
		holderBf = lookcompCell(col, row).takeValue(); //saving for in case turning it back
		holderAf = lookcompCell(col, row).takeValue();

		if(holderBf!=comp){	//in this if section program choose the priority for the place
			if((comp_check_row(col, row)<=1 && comp_check_row(col, row)>=0) || comp_check_risk(col, row)>=4 || holderBf==risk)
				holderAf=k_priority;
			else if(comp_check_risk(col, row)>=1)
				holderAf=h_priority;
			else
				holderAf=l_priority;
		}

		setCell(getcompCell(col, row), closed);	//changing the name of the place to make sure the program wont go to this place again
		if(holderBf==comp)
			setCell(getcompCell(col, row), comp_closed);	//if it is move of the computer program will change it to the W instead of Q

		if(row==0)	//if it is at the top, path condition satisfies and return true to end recursive
			returnVal=true;

		for(int i=0; i<4 && returnVal==false; ++i){	//taking the increment values and checks the new place is safe or not
			comp_incr_values_upleft(i, incrVal);

			returnVal=comp_is_empty(col+incrVal[0], row+incrVal[1]);

			if(returnVal==true)
				returnVal=comp_update_pathfinding_upleft(col+incrVal[0], row+incrVal[1], count);
		}

		if(returnVal==false){	//if it has not succeed it takes the value of place back
			setCell(getcompCell(col, row), holderBf);
			if(count<4*boardSize)	//if counter is more than the border, it doesn't decrease counter
				count-=1;
		}
		else{
			setCell(getcompCell(col, row), holderAf);	//if it has succeed place takes the new value
		}
	}
	return returnVal;
}

bool Hex::comp_update_pathfinding_dnleft(const int col, const int row, int& count){
	int incrVal[2];
	bool returnVal=false;
	CellState holderBf, holderAf;

	if(lookcompCell(col, row).takeValue()!=comp)
		count+=1;

	if(count<4*boardSize){
		
		holderBf = lookcompCell(col, row).takeValue(); //saving for in case turning it back
		holderAf = lookcompCell(col, row).takeValue();

		if(holderBf!=comp){	//in this if section program choose the priority for the place
			if((comp_check_row(col, row)<=1 && comp_check_row(col, row)>=0) || comp_check_risk(col, row)>=4 || holderBf==risk)
				holderAf=k_priority;
			else if(comp_check_risk(col, row)>=1)
				holderAf=h_priority;
			else
				holderAf=l_priority;
		}

		setCell(getcompCell(col, row), closed);	//changing the name of the place to make sure the program wont go to this place again
		if(holderBf==comp)
			setCell(getcompCell(col, row), comp_closed);	//if it is move of the computer program will change it to the W instead of Q

		if(row==boardSize-1)	//if it is at the top, path condition satisfies and return true to end recursive
			returnVal=true;

		for(int i=0; i<4 && returnVal==false; ++i){	//taking the increment values and checks the new place is safe or not
			comp_incr_values_dnleft(i, incrVal);

			returnVal=comp_is_empty(col+incrVal[0], row+incrVal[1]);

			if(returnVal==true)
				returnVal=comp_update_pathfinding_dnleft(col+incrVal[0], row+incrVal[1], count);
		}

		if(returnVal==false){	//if it has not succeed it takes the value of place back
			setCell(getcompCell(col, row), holderBf);
			if(count<4*boardSize)	//if counter is more than the border, it doesn't decrease counter
				count-=1;
		}
		else{
			setCell(getcompCell(col, row), holderAf);	//if it has succeed place takes the new value
		}
	}
	return returnVal;
}

int Hex::comp_calc_row_risk(const int col, const int row) const{	//calculates the risk at the row
	int risk_count_l=0, risk_count_r=0;

	for(int i=0; i<col; ++i){
		if(lookcompCell(i, row).takeValue()==risk || lookcompCell(i, row).takeValue()==enemy)
			risk_count_l+=1;
		else if(lookcompCell(i, row).takeValue()==comp)
			risk_count_l=0;
	}

	for(int i=boardSize-1; i>col; --i){
		if(lookcompCell(i, row).takeValue()==risk || lookcompCell(i, row).takeValue()==enemy)
			risk_count_r+=1;
		else if(lookcompCell(i, row).takeValue()==comp)
			risk_count_r=0;
	}
	return (risk_count_l+risk_count_r);
}

Hex::Cell* Hex::comp_put_move(){
	bool end_loop=false, risk_check=false;
	int col, row, biggest_risk=-1, risk;
	Cell returnVal;

	int holder[2];
	int c_move[2];

	for(row=0; row<boardSize; ++row){
		for(col=0; col<boardSize; ++col){
			if(lookcompCell(col, row).takeValue()==k_priority){					//checks the priority for highest priority level
				risk=comp_calc_row_risk(col, row);
				if(risk>biggest_risk){
					holder[0]=col;
					holder[1]=row;
					biggest_risk=risk;
				}
				end_loop=true;
			}
		}
	}

	if(end_loop==false)
	for(row=0; row<boardSize; ++row){
		for(col=0; col<boardSize; ++col){
			if(lookcompCell(col, row).takeValue()==h_priority){					//checks the priority for high priority level
				risk=comp_calc_row_risk(col, row);
				if(risk>biggest_risk){
					holder[0]=col;
					holder[1]=row;
					biggest_risk=risk;
				}
				end_loop=true;
			}
		}
	}

	if(end_loop==false)
	for(row=0; row<boardSize; ++row){
		for(col=0; col<boardSize; ++col){
			if(lookcompCell(col, row).takeValue()==l_priority){			//checks the priority for low priority level
				risk=comp_calc_row_risk(col, row);
				if(risk>biggest_risk){
					holder[0]=col;
					holder[1]=row;
					biggest_risk=risk;
				}
				end_loop=true;
			}
		}
	}
	if(risk_check==false && end_loop==true){
		setCell(getcompCell(holder[0], holder[1]), comp);		//makes the move
		setCell(getCell(holder[0], holder[1]), player2);
		c_move[0]=holder[0];
		c_move[1]=holder[1];
		risk_check=true;
	}
	returnVal.setter(c_move[0], c_move[1], comp);
	addcompMove(returnVal);

	return &getCell(c_move[0], c_move[1]);
}

void Hex::comp_save_enemy_move(){	//saves the enemy move to the ai board
	int row, col;
	int incrVal[2];

	row=lastMove.takeRow();
	col=lastMove.takeCol();
	setCell(getcompCell(col, row), enemy);
	for(int i=0; i<6; i++){
		incrValues(incrVal, i);
		if(isAccessible(col+incrVal[0], row+incrVal[1]))
			if((lookcompCell(col+incrVal[0], row+incrVal[1]).takeValue()!=enemy && lookcompCell(col+incrVal[0], row+incrVal[1]).takeValue()!=comp))
				setCell(getcompCell(col+incrVal[0], row+incrVal[1]), risk);	//it writes 'R' to the neighbors of new move, 'R' means that place has risk
	}
}


void Hex::comp_no_logical_move(){	//fills the ai board with logically rational priorities
	for(int i=0; i<boardSize; ++i){
		for(int j=0; j<boardSize; ++j){
			if(lookcompCell(j, i).takeValue()!=comp && lookcompCell(j, i).takeValue()!=enemy){
				if((comp_check_row(j, i)<=1 && comp_check_row(j, i)>=0) || comp_check_risk(j, i)>=4)
					setCell(getcompCell(j, i), k_priority);
				else if(lookcompCell(j, i).takeValue()==risk || comp_check_risk(j, i)>=1)
					setCell(getcompCell(j, i), h_priority);
				else
					setCell(getcompCell(j, i), l_priority);
			}
		}
	}
}

int Hex::comp_horizontal_side_risk_check() const{
	int returnVal=1, left_risk=0, right_risk=0;

	for (int row = 0; row < boardSize; ++row) {
		for (int col = 0; col < boardSize; ++col) {
			if (lookcompCell(col, row).takeValue() == enemy) {
				if (col <= (boardSize / 2))
					++left_risk;
				else
					++right_risk;
			}
		}
	}

	if (left_risk >= right_risk)
		returnVal = 1;
	else
		returnVal = 0;

	return returnVal;
}

int Hex::comp_vertical_side_risk_check() const{
	int returnVal = 1, up_risk = 0, dn_risk = 0;

	for (int row = 0; row < boardSize; ++row) {
		for (int col = 0; col < boardSize; ++col) {
			if (lookcompCell(col, row).takeValue() == enemy) {
				if (row <= (boardSize / 2))
					++up_risk;
				else
					++dn_risk;
			}
		}
	}

	if (up_risk >= dn_risk)
		returnVal = 1;
	else
		returnVal = 0;

	return returnVal;
}

void Hex::comp_find_path() {
	int path_length = 0, length_up = 0, length_dn = 0, side_h, side_v, short_path=boardSize*boardSize, path_pointer=-1;
	bool updateValup=false, updateValdn=false;
	int col, row;

	side_h = comp_horizontal_side_risk_check();
	side_v = comp_vertical_side_risk_check();
	comp_reset_path();

	for (int i = 0; i < compmoveCount; ++i) {   //checks the path for upright and downright directions
		col = compMoves[i].takeCol();
		row = compMoves[i].takeRow();
		if (comp_check_neighbor(col, row) <= 1) {
			if (side_h == 0) {
				if (side_v == 0) {
					updateValup = comp_update_pathfinding_upright(col, row, length_up);
					updateValdn = comp_update_pathfinding_dnleft(col, row, length_dn);
				}
				else {
					updateValup = comp_update_pathfinding_upleft(col, row, length_up);
					updateValdn = comp_update_pathfinding_dnright(col, row, length_dn);
				}
			}

			else {
				if (side_v == 0) {
					updateValup = comp_update_pathfinding_upleft(col, row, length_up);
					updateValdn = comp_update_pathfinding_dnright(col, row, length_dn);
				}
				else {
					updateValup = comp_update_pathfinding_upright(col, row, length_up);
					updateValdn = comp_update_pathfinding_dnleft(col, row, length_dn);
				}
			}

			if (updateValup == true && updateValdn == true) {  //if both of them reached to the end points program gets into this if
				if (short_path > (length_up + length_dn)) {	//if it is the shortest path that have been found it saves the location
					path_pointer = i;
					short_path = length_up + length_dn;	//updating the shortest distance that have been found
				}
			}

			comp_reset_path();
			length_up = 0;
			length_dn = 0;
		}
	}



	if (path_pointer == -1)
		comp_no_logical_move();
	
	else {
		col = compMoves[path_pointer].takeCol();
		row = compMoves[path_pointer].takeRow();
		if (side_h == 0) {
			if (side_v == 0) {
				updateValup = comp_update_pathfinding_upright(col, row, length_up);
				updateValdn = comp_update_pathfinding_dnleft(col, row, length_dn);
			}
			else {
				updateValup = comp_update_pathfinding_upleft(col, row, length_up);
				updateValdn = comp_update_pathfinding_dnright(col, row, length_dn);
			}
		}

		else {
			if (side_v == 0) {
				updateValup = comp_update_pathfinding_upleft(col, row, length_up);
				updateValdn = comp_update_pathfinding_dnright(col, row, length_dn);
			}
			else {
				updateValup = comp_update_pathfinding_upright(col, row, length_up);
				updateValdn = comp_update_pathfinding_dnleft(col, row, length_dn);
			}
		}
	}
}

Hex::Cell *Hex::comp_first_move(const int prowMove/*=default value=-1*/) { //calculating the first move of the computer
	int horizon, moveRow, moveCol;
	Cell *returnVal;
	auto actualSize = boardSize - 1;

	horizon = comp_horizontal_side_risk_check();

	moveCol = (horizon == 0) ? (actualSize - (3 * actualSize / 4)) : (actualSize - (actualSize / 4));	//making a risk calculation, and decides where to move in column

	moveRow = (prowMove == -1) ? (actualSize / 2) : (prowMove);	//if first turn in the hands of the computer making its row move to the middle

	setCell(getcompCell(moveCol, moveRow), k_priority);
	returnVal = comp_put_move();

	return returnVal;
}

void Hex::createcompBoard(){
	Cell *new_cell;
	if(compBoard==nullptr){
		compBoard = new Cell *[getBoardsize()];
		for (int i = 0; i < getBoardsize(); ++i)
		{
			new_cell = new Cell[getBoardsize()];

			if(new_cell!=nullptr){
				compBoard[i] = new_cell;
				for (int j = 0; j < getBoardsize(); ++j)
					new_cell[j].setter(j, i, emp);
			}

			else{
				cout << "Not enough memory!\n";
				exit(1);
			}
		}
	}
}


void Hex::addcompMove(const Hex::Cell &newMove){
	Cell *newcompMove;
	if (compmoveCount > maxcountcompMove)
	{
		maxcountcompMove *= 2;
		newcompMove = new Cell[maxcountcompMove];
		if(newcompMove!=nullptr){
			for (int i = 0; i < compmoveCount - 1; ++i){
				newcompMove[i] = compMoves[i];
			}
			delete[] compMoves;
			compMoves = newcompMove;
		}
		else{
			cerr << "Not enough memory!!!\n";
			exit(1);
		}
	}

	compMoves[compmoveCount - 1] = newMove;

}

/*------------------------------CELL PART-----------------------------------------------------*/

Hex::Cell::Cell(int col, int row, CellState value) : pos_col(col), pos_row(row), cell_value(value)
{
	testCell();
}

inline bool Hex::Cell::setter(const int col, const int row)
{
	return setter(col, row, emp);
}

inline bool Hex::Cell::setter(const CellState value)
{
	return setter(pos_col, pos_row, value);
}

bool Hex::Cell::setter(const int col, const int row, const CellState value)
{
	pos_col = col;
	pos_row = row;
	cell_value = value;

	return testCell();
}

inline int Hex::Cell::takeCol() const
{
	return pos_col;
}
inline int Hex::Cell::takeRow() const
{
	return pos_row;
}
inline CellState Hex::Cell::takeValue() const
{
	return cell_value;
}

bool Hex::Cell::testCell() const
{
	bool returnVal;

	if (pos_col < 0 || pos_row < 0 || cell_value < emp || cell_value > closed)
	{
		returnVal = false;
		cerr << "Non-valid Cell Value!!!\n";
		//exit(1);
	}

	else
		returnVal = true;

	return returnVal;
}