#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include "HexCell.h"
#include "AbstractHex.h"

using namespace std;
namespace{
int findintLength(int num){	//to find empty spaces will print while the board printing
    int returnVal=0;
    while (num != 0){
        num /= 10;
        returnVal++;
    }
    return returnVal;
}
}
namespace OzturkHexGame{
AbstractHex::AbstractHex() : AbstractHex(6, pvp, player1){}

AbstractHex::AbstractHex(int bSize, GameMode mod, CellState firstPlayer) : _moveCount(0), _moveCountComp(0), _p1Left(false),
																		   _p1Right(false), _p2Dn(false), _p2Up(false),
																		   _winner(emp), _boardSize(bSize)
{
	setGamemode(mod);
	setTurnown(firstPlayer);
}

void AbstractHex::print() const{
    cout << (*this);	//i overloaded << operator
}

void AbstractHex::playGame(){
	char colChar;
	int col, row;
	int oldSize;
	string input = "HR";
	bool invalid = false, pause = false;
	while (!isEnd() && pause == false)
	{
		print();
		do{
			invalid = false;
			if(!(getGamemode() == pve && getTurnown() == player2)){
			cout << "Please make your move (ex: A3): ";
			cin >> input;
			}
			stringstream sinput(input);

			if(input.length() == 2){
				if(!(getGamemode() == pve && getTurnown() == player2)){
					sinput >> colChar >> row;
					row--;
					col = static_cast<int> (colChar) -'A';
				}
				try{
					if(!(getGamemode() == pve && getTurnown() == player2))
						play(col, row);
				}
				catch(InvalidHexPlace a){
					cout << a.what() << endl;
					invalid = true;
				}

				if(getGamemode()==pve && invalid == false)
					play();
			}
			else{
				if(input == "RESET")
					this->reset();
				else if(input == "LMOVE"){
					try{
						lastMove();
						char col = 'A' + lastMove().takeCol();
						cout << "Last move at: " << col << lastMove().takeRow() + 1 << " Player moved that position: " << lastMove().takeValue() << endl;
					}
					catch(HexNoLastMove err){
						cerr << err.what() << endl;
					}
				}
				else if(input == "MOVECOUNT"){
					cout << "Move count is: " << numberOfMoves() << endl;
				}
				else if(input == "LOAD"){
					cin >> input;
					readFromFile(input);
				}
				else if(input == "SAVE"){
					cin >> input;
					writeToFile(input);
				}
				else if(input == "LOOK"){
					CellState a;
					cin >> colChar >> row;
					row--;
					col = static_cast<int> (colChar) -'A';
					try{
						a = (*this)(col, row).takeValue();
						if(a == emp)
							cout << "Place is empty!\n";
						else if(a == player1 || a == p1_win)
							cout << "Place is filled with x\n";
						else if(a == player2 || a == p2_win)
							cout << "Place is filled with o\n";
					}
					catch(InvalidHexPlace err){
						cout << err.what() << endl;
					}
				}
				else if(input == "RESIZE"){
					oldSize = getSize();
					cin >> col;
					try{
						setSize(col);
					}
					catch(InvalidHexSize e){
						cout << e.what() << endl;
						cout << "Game restarted with old size!\n";
						setSize(oldSize);
					}
				}
				else if(input == "MENU"){
					pause = true;
					break;
				}
			}
		} while (invalid == true);
	}
	print();
}

ostream &operator<<(ostream &out, const AbstractHex &game){
    Cell temp;
	char startChar = 'a';

    out << "\n ";
	for (int i = 0; i <= game.getSize() - 1; ++i){
		out << startChar;
		startChar += 1;
		out << " ";
	}
	out << endl;
	for (int i = 0; i < game.getSize(); ++i){
		out << i + 1;
		for (int k = 0; k < i; ++k){
            if(!(k < findintLength(i+1)-1))     //for spacing problem
				out << " ";
		}
		for (int j = 0; j < game.getSize(); ++j)
		{
			if (game(j, i).takeValue() == player1)
				out << 'x';
			else if (game(j, i).takeValue() == player2)
				out << 'o';
			else if (game(j, i).takeValue() == emp)
				out << '.';
			else if (game(j, i).takeValue() == p1_win)
				out << 'X';
			else if (game(j, i).takeValue() == p2_win)
				out << 'O';

			out << " ";
		}
		out << endl;
	}
    return out;
}

void AbstractHex::play(int col, int row){
	if(!isEnd()){
		if((col < getSize() && col >= 0) && (row < getSize() && row >= 0)){
			if((*this)(col,row).takeValue()==emp){
				getCell(col, row).setter(getTurnown());
				_lastMove = (*this)(col, row);
				_moveCount++;
				if(getTurnown()==player1){
					if(col==0 && _p1Left == false)
						_p1Left = true;
					else if(col == getSize()-1 && _p1Right == false)
						_p1Right = true;
				}
				else if(getTurnown() == player2){
					if(row == 0 && _p2Up == false)
						_p2Up = true;
					else if(row == getSize()-1 && _p2Dn == false)
						_p2Dn = true;
				}

				checkWin();

				changeTurnown();
			}
			else{
				throw InvalidHexPlace();
			}
		}
		else{
			throw InvalidHexPlace();
		}
	}
	else{
		throw CantPlayEndedGame();
	}
}

void AbstractHex::play(){
	if(!isEnd()){
		Cell *move;
		comp_reset_path();

		_moveCountComp++;
		if (numberOfMoves() == 0)
			move=comp_first_move();

		else if (numberOfMoves() <= 1){
			comp_save_enemy_move();
			move = comp_first_move(lastMove().takeRow());
		}
		
		else{
			comp_save_enemy_move();
			comp_find_path();
			move=comp_put_move();
		}

		if(move->takeRow() == 0 && _p2Up == false)
			_p2Up = true;
		else if(move->takeRow() == getSize()-1 && _p2Dn == false)
			_p2Dn = true;
		_moveCount++;

		checkWin();

		changeTurnown();
	}
	else{
		throw CantPlayEndedGame();
	}
}



void AbstractHex::readFromFile(const string &filename){
    int holder, col, row, value, numMoves, numcompMoves;
	string charHolder;
	Cell new_cell;

	ifstream file;
	file.open(filename);

	if(file.is_open()){
		getline(file, charHolder);
		if(charHolder=="!"){
			/*LOADS ALL THE NUMERIC DATA TO FILE*/
			file >> holder;
			file.ignore(1, '\n');
			setGamemode(static_cast<GameMode>(holder));
			file >> holder;
			file.ignore(1, '\n');
			setSize(holder);
			file >> numMoves;
			file.ignore(1, '\n');
            file >> numcompMoves;
            file.ignore(1, '\n');
			file >> holder;
			file.ignore(1, '\n');
			p1SetLeftCond(static_cast<bool>(holder));
			file >> holder;
			file.ignore(1, '\n');
			p1SetRightCond(static_cast<bool>(holder));
			file >> holder;
			file.ignore(1, '\n');
			p2SetDnCond(static_cast<bool>(holder));
			file >> holder;
			file.ignore(1, '\n');
			p2SetUpCond(static_cast<bool>(holder));
			file >> holder;
			file.ignore(1, '\n');
			setTurnown(static_cast<CellState>(holder));
			file >> holder;
			file.ignore(1, '\n');
			setWinner(static_cast<CellState>(holder));
			file >> col >> row >> value;
			file.ignore(1, '\n');
			_lastMove = Cell(col, row, static_cast<CellState> (value));
			getline(file, charHolder);

			/* LOADS MOVES OF THE COMPUTERS IF IT IS A PVE GAME */
			if(charHolder=="-" && getGamemode()==pve ){
				_compMoves.clear();
				for (int i = 0; i < numcompMoves; ++i)
				{
					file >> col >> row >> value;
					file.ignore(1, '\n');
					_compMoves.push_back(Cell(col, row, static_cast<CellState> (value)));
					_moveCountComp++;
				}
			}
			getline(file, charHolder);
			getline(file, charHolder);

			/* LOADS THE AI BOARD TO FILE BY SENDS COORDINATES AND VALUE OF THE POINTS */
			if(charHolder=="=" && getGamemode()==pve){	
				while (file.peek()!='='){
					file >> col >> row >> value;
					file.ignore(1, '\n');
					getcompCell(col, row).setter(static_cast<CellState> (value));
				}
			}
			getline(file, charHolder);
			getline(file, charHolder);
			/* LOADS THE MAIN BOARD TO FILE BY SENDS COORDINATES AND VALUE OF THE POINTS */
			if(charHolder=="{"){
				for (int j = 0; j < numMoves; ++j){
					file >> col >> row >> value;
					file.ignore(1, '\n');
					this->getCell(col, row).setter(static_cast<CellState>(value));
					_moveCount++;
				}
			}
			getline(file, charHolder);
			getline(file, charHolder);
			if(charHolder=="!")
				cout << "GAME LOADED SUCCESSFULLY"<< endl;
			else
				throw HexLoadError();
		}
		else{
			throw HexLoadError();
		}
	}
	else{
		throw HexLoadError();
	}
	file.close();
}

void AbstractHex::writeToFile(const string &filename) const{
	ofstream file;
	file.open(filename);

	if (file.is_open())
	{
		file << "!\n";
		/*SAVES ALL THE NUMERIC DATA TO FILE*/
		file << static_cast<int>(getGamemode()) << endl;
		file << getSize() << endl;
		file << numberOfMoves() << endl;
		file << _moveCountComp << endl;
		file << _p1Left << endl;
		file << _p1Right << endl;
		file << _p2Dn << endl;
		file << _p2Up << endl;
		file << getTurnown() << endl;
		file << _winner << endl;
		file << lastMove().takeCol() << " " << lastMove().takeRow() << " " << lastMove().takeValue() << endl;

		/* SAVES MOVES OF THE COMPUTERS IF IT IS A PVE GAME */
		file << "-\n";
		for (int i = 0; i < _moveCountComp; ++i)
			file << _compMoves[i].takeCol() << " " << _compMoves[i].takeRow() << " " << _compMoves[i].takeValue() << endl;
		file << "-\n";

		/* SAVES THE AI BOARD TO FILE BY SENDS COORDINATES AND VALUE OF THE POINTS */
		file << "=\n";
		for (int i = 0; i < getSize() && getGamemode() == pve; ++i)
			for (int j = 0; j < getSize(); ++j)
				if(_compBoard[i][j].takeValue()!=emp)
					file << _compBoard[i][j].takeCol() << " " << _compBoard[i][j].takeRow() << " " << _compBoard[i][j].takeValue() << endl;
		file << "=\n";

		/* SAVES THE MAIN BOARD TO FILE BY SENDS COORDINATES AND VALUE OF THE POINTS */
		file << "{\n";
		for (int i = 0; i < getSize(); ++i)
			for (int j = 0; j < getSize(); ++j)
				if((*this)(j, i).takeValue()!=emp)
					file << (*this)(j, i).takeCol() << " " << (*this)(j, i).takeRow() << " " << (*this)(j, i).takeValue() << endl;
		file << "}\n";

		file << "!";
	}
	else
	{
		throw HexSaveError();
	}
	file.close();
}

void AbstractHex::setSize(int boardSize){
    if(boardSize > 5){
        _boardSize = boardSize;
        this->reset();  //late-binding
		_moveCount = 0;
		if (getGamemode() == pve)
			resetComp();
	}
    else{
		throw InvalidHexSize();
	}
}

void AbstractHex::setComp(){
	resetComp();
}

void AbstractHex::resetComp(){
	vector<Cell> temp;
	if (getSize() > 5)
	{
		_compBoard.clear();
		for (int i = 0; i < getSize(); ++i){
			for (int j = 0; j < getSize(); ++j){
				temp.push_back(Cell(j, i, emp));
			}
			_compBoard.push_back(temp);
			temp.clear();
		}
		_moveCountComp = 0;
		_compMoves.clear();
	}
}

int AbstractHex::getSize() const{
    return (_boardSize);
}

bool AbstractHex::isEnd() const{
    if(_winner==emp)
        return false;
    else
        return true;
}

int AbstractHex::numberOfMoves() const{
    return _moveCount;
}

const Cell &AbstractHex::lastMove() const{
    if(numberOfMoves() <= 0){
		throw HexNoLastMove();
	}
	return _lastMove;
}

bool AbstractHex::operator==(const AbstractHex &other) const{
    bool returnVal=true;

    if (this->getSize() == other.getSize())
    {
        for (int i = 0; i < other.numberOfMoves() && returnVal == true; ++i)
            for (int j = 0; j < other.numberOfMoves() && returnVal == true; ++j)
                if((*this)(j, i).takeValue() != other(j, i).takeValue())
                    returnVal = false;
    }
    else
        returnVal = false;

    return returnVal;
}

void AbstractHex::p1SetLeftCond(bool val){
    _p1Left = val;
}

void AbstractHex::p1SetRightCond(bool val){
    _p1Right = val;
}

void AbstractHex::p2SetUpCond(bool val){
    _p2Up = val;
}

void AbstractHex::p2SetDnCond(bool val){
    _p2Dn = val;
}

const CellState AbstractHex::getWinner() const{
    return _winner;
}

void AbstractHex::setGamemode(GameMode mod){
	if(mod <= pve || mod >= pvp){
		_mod = mod;
		if(getGamemode() == pve)
			setComp();
	}
	else{
		throw InvalidGameMode();
	}
}

const GameMode AbstractHex::getGamemode() const{
	return _mod;
}

void AbstractHex::setWinner(CellState winner){
	if(winner==emp || winner==player1 || winner==player2){
		_winner = winner;
	}
}

void AbstractHex::changeTurnown(){
	if(getTurnown() == player1)
		_turnOwner = player2;
	else if (getTurnown() == player2)
		_turnOwner = player1;
}

const CellState &AbstractHex::getTurnown() const{
	return _turnOwner;
}

void AbstractHex::setTurnown(const CellState turnOwn){
	if(turnOwn==player1 || turnOwn==player2)
		_turnOwner = turnOwn;
	else{
		//exception throw
	}
}


const CellState AbstractHex::checkWin(){
	Cell temp;
	if(_p1Left == true && _p1Right == true){
		for (int i = 0; i < getSize(); ++i){
			temp = getCell(0, i);
			if(temp.takeValue()==player1)
				if(checkWcond(temp.takeCol(), temp.takeRow(), player1))
					setWinner(player1);
		}
	}
	if(_p2Up == true && _p2Dn == true){
		for (int i = 0; i < getSize(); ++i){
			temp = getCell(i, 0);
			if(temp.takeValue()==player2)
				if(checkWcond(temp.takeCol(), temp.takeRow(), player2))
					setWinner(player2);
		}
	}
	if(getWinner() == player1){
		cout << "\n\n****************\nWINNER IS PLAYER 1!\n";
	}
	else if(getWinner() == player2){
		cout << "\n\n****************\nWINNER IS PLAYER 2!\n";
	}
	return getWinner();
}

bool AbstractHex::checkWcond(const int col, const int row, const CellState player){
	bool win = false;
	int incrVal[2];

	if(player==player1 && (col>=0 && col< getSize()) && (row>=0 && row<getSize())){
		if((*this)(col, row).takeValue()==player1){
			getCell(col, row).setter(p1_win);

			if(col==getSize()-1){
				win = true;
			}

			for (int i = 0; i < 6 && win==false; ++i){
				incrValues(incrVal, i);
				win = checkWcond(col + incrVal[0], row + incrVal[1], player);
			}

			if(win==false)
				getCell(col, row).setter(player1);
		}
	}

	else if(player==player2 && (col>=0 && col<getSize()) && (row>=0 && row<getSize())){
		if((*this)(col, row).takeValue()==player2){
			getCell(col, row).setter(p2_win);

			if(row==getSize()-1){
				win = true;
			}

			for (int i = 0; i < 6 && win==false; ++i)
			{
				incrValues(incrVal, i);
				win = checkWcond(col + incrVal[0], row + incrVal[1], player);
			}

			if(win==false)
				getCell(col, row).setter(player2);
		}
	}

	return win;
}



void AbstractHex::comp_incr_values_upleft(const int direction, int value[2]) const{	//incrementation values for up leftt direction

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

void AbstractHex::comp_incr_values_dnleft(const int direction, int value[2]) const{	//incrementation values for down leftt direction

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

void AbstractHex::comp_incr_values_upright(const int direction, int value[2]) const{		//incrementation values for up right direction

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

void AbstractHex::comp_incr_values_dnright(const int direction, int value[2]) const{	//incrementation values for down right direction

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

bool AbstractHex::isAccessible(const int col, const int row) const{
	bool returnVal = false;
	if (col < getSize() && col >= 0 && row < getSize() && row >= 0)
		returnVal = true;

	return returnVal;
}

bool AbstractHex::comp_is_empty(const int loc_col, const int loc_row) const {	//checks the place is safe or not
	bool returnVal=false;
	if (isAccessible(loc_col, loc_row))
		if(lookcompCell(loc_col, loc_row).takeValue()!=enemy && lookcompCell(loc_col, loc_row).takeValue()!=closed && lookcompCell(loc_col, loc_row).takeValue()!=comp_closed)
			returnVal=true;

	return returnVal;
}

int AbstractHex::comp_check_row(const int col, const int row) const{	//checks the enemy in the row
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

	for(int i=getSize()-1; i>col && !end_look; --i){
		if(lookcompCell(i, row).takeValue()==enemy)
			returnVal=i-col;
		if(returnVal!=-1 && (lookcompCell(i, row).takeValue()==comp || lookcompCell(i, row).takeValue()==comp_closed)){
			returnVal=-1;
		}
	}

	return returnVal;
}

int AbstractHex::comp_check_row_risk(const int col, const int row) const{	//checks the risk value in a row
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

	for(int i=getSize()-1; i>col && !end_look; --i){
		if(lookcompCell(i, row).takeValue()==risk)
			returnVal=i-col;
		if(returnVal!=-1 && (lookcompCell(i, row).takeValue()==comp || lookcompCell(i, row).takeValue()==comp_closed)){
			returnVal=-1;
		}
	}

	return returnVal;
}

int AbstractHex::comp_check_risk(const int col, const int row) const{	//checks the risk value for edges of place
	int r_count=0;
	int incrVal[2];
	for (int i = 0; i < 6; i++)
	{
		incrValues(incrVal, i);
		if(isAccessible(col+incrVal[0], row+incrVal[1]))
			if(lookcompCell(col+incrVal[0], row+incrVal[1]).takeValue()==risk)
				r_count+=1;
	}
	if((col==getSize()-1 || col==0) && r_count>=1)
		r_count=5;
	return r_count;
}

int AbstractHex::comp_check_neighbor(const int col, const int row) const{	//returns the count of already moved places at the edges of place
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

void AbstractHex::comp_reset_path(){	//resets L(Low priority values), H(High priority values) and K(Highest priority values)
	for(int i=0; i<getSize(); ++i){
		for(int j=0; j<getSize(); ++j){
			if(lookcompCell(j, i).takeValue()==l_priority || lookcompCell(j, i).takeValue()==h_priority || lookcompCell(j, i).takeValue()==k_priority)
				getcompCell(j, i).setter(emp);
		}
	}
}

bool AbstractHex::comp_update_pathfinding_upright(const int col, const int row, int& count){
	int incrVal[2];
	bool returnVal=false;
	CellState holderBf, holderAf;
	if(isAccessible(col, row)){
		if (lookcompCell(col, row).takeValue() != comp)
			count += 1;

		if (count < 4 * getSize())
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
			getcompCell(col, row).setter(closed); //changing the name of the place to make sure the program wont go to this place again
			if (holderBf == comp)
				getcompCell(col, row).setter(comp_closed);; //if it is move of the computer program will change it to the W instead of Q

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
				getcompCell(col, row).setter(holderBf);
				if (count < 4 * getSize()) //if counter is more than the border, it doesn't decrease counter
					count -= 1;
			}
			else
			{
				getcompCell(col, row).setter(holderAf); //if it has succeed place takes the new value
			}
		}
	}
	return returnVal;
}

bool AbstractHex::comp_update_pathfinding_dnright(const int col, const int row, int& count){
	int incrVal[2];
	bool returnVal=false;
	CellState holderBf, holderAf;

	if(isAccessible(col, row)){
		if(lookcompCell(col, row).takeValue()!=comp)
			count+=1;

		if(count<4*getSize()){
			
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

			getcompCell(col, row).setter(closed);	//changing the name of the place to make sure the program wont go to this place again
			if(holderBf==comp)
				getcompCell(col, row).setter(comp_closed);	//if it is move of the computer program will change it to the W instead of Q

			if(row==getSize()-1)	//if it is at the top, path condition satisfies and return true to end recursive
				returnVal=true;

			for(int i=0; i<4 && returnVal==false; ++i){	//taking the increment values and checks the new place is safe or not
				comp_incr_values_dnright(i, incrVal);

				returnVal=comp_is_empty(col+incrVal[0], row+incrVal[1]);

				if(returnVal==true)
					returnVal=comp_update_pathfinding_dnright(col+incrVal[0], row+incrVal[1], count);
			}

			if(returnVal==false){	//if it has not succeed it takes the value of place back
				getcompCell(col, row).setter(holderBf);
				if(count<4*getSize())	//if counter is more than the border, it doesn't decrease counter
					count-=1;
			}
			else{
				getcompCell(col, row).setter(holderAf);	//if it has succeed place takes the new value
			}
		}
	}
	return returnVal;
}

bool AbstractHex::comp_update_pathfinding_upleft(const int col, const int row, int& count){
	int incrVal[2];
	bool returnVal=false;
	CellState holderBf, holderAf;

	if(isAccessible(col, row)){
		if(lookcompCell(col, row).takeValue()!=comp)
			count+=1;

		if(count<4*getSize()){
			
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

			getcompCell(col, row).setter(closed);	//changing the name of the place to make sure the program wont go to this place again
			if(holderBf==comp)
				getcompCell(col, row).setter(comp_closed);	//if it is move of the computer program will change it to the W instead of Q

			if(row==0)	//if it is at the top, path condition satisfies and return true to end recursive
				returnVal=true;

			for(int i=0; i<4 && returnVal==false; ++i){	//taking the increment values and checks the new place is safe or not
				comp_incr_values_upleft(i, incrVal);

				returnVal=comp_is_empty(col+incrVal[0], row+incrVal[1]);

				if(returnVal==true)
					returnVal=comp_update_pathfinding_upleft(col+incrVal[0], row+incrVal[1], count);
			}

			if(returnVal==false){	//if it has not succeed it takes the value of place back
				getcompCell(col, row).setter(holderBf);
				if(count<4*getSize())	//if counter is more than the border, it doesn't decrease counter
					count-=1;
			}
			else{
				getcompCell(col, row).setter(holderAf);	//if it has succeed place takes the new value
			}
		}
	}
	return returnVal;
}

bool AbstractHex::comp_update_pathfinding_dnleft(const int col, const int row, int& count){
	int incrVal[2];
	bool returnVal=false;
	CellState holderBf, holderAf;

	if(isAccessible(col, row)){
		if(lookcompCell(col, row).takeValue()!=comp)
			count+=1;

		if(count<4*getSize()){
			
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

			getcompCell(col, row).setter(closed);	//changing the name of the place to make sure the program wont go to this place again
			if(holderBf==comp)
				getcompCell(col, row).setter(comp_closed);	//if it is move of the computer program will change it to the W instead of Q

			if(row==getSize()-1)	//if it is at the top, path condition satisfies and return true to end recursive
				returnVal=true;

			for(int i=0; i<4 && returnVal==false; ++i){	//taking the increment values and checks the new place is safe or not
				comp_incr_values_dnleft(i, incrVal);

				returnVal=comp_is_empty(col+incrVal[0], row+incrVal[1]);

				if(returnVal==true)
					returnVal=comp_update_pathfinding_dnleft(col+incrVal[0], row+incrVal[1], count);
			}

			if(returnVal==false){	//if it has not succeed it takes the value of place back
				getcompCell(col, row).setter(holderBf);
				if(count<4*getSize())	//if counter is more than the border, it doesn't decrease counter
					count-=1;
			}
			else{
				getcompCell(col, row).setter(holderAf);	//if it has succeed place takes the new value
			}
		}
	}
	return returnVal;
}

int AbstractHex::comp_calc_row_risk(const int col, const int row) const{	//calculates the risk at the row
	int risk_count_l=0, risk_count_r=0;

	for(int i=0; i<col; ++i){
		if(lookcompCell(i, row).takeValue()==risk || lookcompCell(i, row).takeValue()==enemy)
			risk_count_l+=1;
		else if(lookcompCell(i, row).takeValue()==comp)
			risk_count_l=0;
	}

	for(int i=getSize()-1; i>col; --i){
		if(lookcompCell(i, row).takeValue()==risk || lookcompCell(i, row).takeValue()==enemy)
			risk_count_r+=1;
		else if(lookcompCell(i, row).takeValue()==comp)
			risk_count_r=0;
	}
	return (risk_count_l+risk_count_r);
}

Cell* AbstractHex::comp_put_move(){
	bool end_loop=false, risk_check=false;
	int col, row, biggest_risk=-1, risk;
	Cell returnVal;

	int holder[2];
	int c_move[2];

	for(row=0; row<getSize(); ++row){
		for(col=0; col<getSize(); ++col){
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
	for(row=0; row<getSize(); ++row){
		for(col=0; col<getSize(); ++col){
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
	for(row=0; row<getSize(); ++row){
		for(col=0; col<getSize(); ++col){
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
		getcompCell(holder[0], holder[1]).setter(comp);		//makes the move
		getCell(holder[0], holder[1]).setter(player2);
		c_move[0]=holder[0];
		c_move[1]=holder[1];
		risk_check=true;
	}
	returnVal.setter(c_move[0], c_move[1], comp);
	addcompMove(returnVal);

	return &getCell(c_move[0], c_move[1]);
}

void AbstractHex::comp_save_enemy_move(){	//saves the enemy move to the ai board
	int row, col;
	int incrVal[2];

	row=lastMove().takeRow();
	col=lastMove().takeCol();
	getcompCell(col, row).setter(enemy);
	for(int i=0; i<6; i++){
		incrValues(incrVal, i);
		if(isAccessible(col+incrVal[0], row+incrVal[1]))
			if((lookcompCell(col+incrVal[0], row+incrVal[1]).takeValue()!=enemy && lookcompCell(col+incrVal[0], row+incrVal[1]).takeValue()!=comp))
				getcompCell(col+incrVal[0], row+incrVal[1]).setter(risk);	//it writes 'R' to the neighbors of new move, 'R' means that place has risk
	}
}


void AbstractHex::comp_no_logical_move(){	//fills the ai board with logically rational priorities
	for(int i=0; i<getSize(); ++i){
		for(int j=0; j<getSize(); ++j){
			if(lookcompCell(j, i).takeValue()!=comp && lookcompCell(j, i).takeValue()!=enemy){
				if((comp_check_row(j, i)<=1 && comp_check_row(j, i)>=0) || comp_check_risk(j, i)>=4)
					getcompCell(j, i).setter(k_priority);
				else if(lookcompCell(j, i).takeValue()==risk || comp_check_risk(j, i)>=1)
					getcompCell(j, i).setter(h_priority);
				else
					getcompCell(j, i).setter(l_priority);
			}
		}
	}
}

int AbstractHex::comp_horizontal_side_risk_check() const{
	int returnVal=1, left_risk=0, right_risk=0;

	for (int row = 0; row < getSize(); ++row) {
		for (int col = 0; col < getSize(); ++col) {
			if (lookcompCell(col, row).takeValue() == enemy) {
				if (col <= (getSize() / 2))
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

int AbstractHex::comp_vertical_side_risk_check() const{
	int returnVal = 1, up_risk = 0, dn_risk = 0;

	for (int row = 0; row < getSize(); ++row) {
		for (int col = 0; col < getSize(); ++col) {
			if (lookcompCell(col, row).takeValue() == enemy) {
				if (row <= (getSize() / 2))
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

void AbstractHex::comp_find_path() {
	int path_length = 0, length_up = 0, length_dn = 0, side_h, side_v, short_path=getSize()*getSize(), path_pointer=-1;
	bool updateValup=false, updateValdn=false;
	int col, row;

	side_h = comp_horizontal_side_risk_check();
	side_v = comp_vertical_side_risk_check();
	comp_reset_path();

	for (int i = 0; i < _moveCountComp; ++i) {   //checks the path for upright and downright directions
		col = _compMoves[i].takeCol();
		row = _compMoves[i].takeRow();
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
		col = _compMoves[path_pointer].takeCol();
		row = _compMoves[path_pointer].takeRow();
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

Cell *AbstractHex::comp_first_move(const int prowMove/*=default value=-1*/) { //calculating the first move of the computer
	int horizon, moveRow, moveCol;
	Cell *returnVal;
	auto actualSize = getSize() - 1;

	horizon = comp_horizontal_side_risk_check();

	moveCol = (horizon == 0) ? (actualSize - (3 * actualSize / 4)) : (actualSize - (actualSize / 4));	//making a risk calculation, and decides where to move in column

	moveRow = (prowMove == -1) ? (actualSize / 2) : (prowMove);	//if first turn in the hands of the computer making its row move to the middle

	getcompCell(moveCol, moveRow).setter(k_priority);
	returnVal = comp_put_move();

	return returnVal;
}



void AbstractHex::addcompMove(const Cell &newMove){
	_compMoves.push_back(newMove);
}

void AbstractHex::incrValues(int value[2], const int direction) const{
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

const Cell &AbstractHex::lookcompCell(int col, int row) const{
	return (_compBoard[row][col]);
}

Cell &AbstractHex::getcompCell(int col, int row){
	return (_compBoard[row][col]);
}
}