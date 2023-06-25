#ifndef HEXGAME_H_
#define HEXGAME_H_

#include <iostream>
#include <string>
#include <vector>
#include <fstream>

using namespace std;

enum CellState{emp = 0, player1, player2, p1_win, p2_win, comp, comp_win, risk, h_priority, l_priority, k_priority, enemy, comp_closed, no, closed};

enum GameMode{pvp = 1, pve};

class Hex
{
public:
	class Cell
		{
		public:
			Cell() : Cell(0, 0, emp) { /*Intentionally empty*/}
			Cell(int col, int row) : Cell(col, row, emp)  { /*Intentionally empty*/}
			Cell(int col, int row, CellState value);

			bool setter(const int col, const int row);
			bool setter(const CellState value);
			bool setter(const int col, const int row, const CellState value);
			int takeCol() const;
			int takeRow() const;
			CellState takeValue() const;

		private:
			CellState cell_value;
			int pos_col;
			int pos_row;
			bool testCell() const;
		};

public:
	/*----------------CONSTRUCTORS-------------*/
	Hex() : Hex(6)	{ /*Intentionally empty*/}

	explicit Hex(int size) : boardSize(size), winner(emp), turnOwner(player1), p1wcondL(false), p1wcondR(false),
					p2wcondU(false), p2wcondD(false), numberMoves(0), compmoveCount(0), activeGame(false),
					compboardmoveCount(0), hexCells(nullptr), compBoard(nullptr), maxcountLastmove(size*size),
					maxcountcompMove(size*size), maxScore(10000)
	{
		compMoves = new Cell[maxcountcompMove];
		allMoves = new Cell *[boardSize * boardSize];
		testHex();
	}

	/*---------------BIG THREE---------------*/
	const Hex &operator=(const Hex &rValue);	//Assignment operator overload
	Hex(const Hex &mainObj);					//Copy constructor
	~Hex();									//Destructor

	/*--------------OPERATOR OVERLOADS-------*/
	Hex &operator--();	//it is for undo
	Hex operator--(int placeHolder);  //it is for undo
	bool operator==(const Hex &rValue) const;		//compare games
	friend ofstream &operator<<(ofstream &file, const Hex &rValue); //save game
	friend ifstream &operator>>(ifstream &file, Hex &load);	//load game
	friend ostream &operator<<(ostream &screen, const Hex &game);  //print board

	/*------------------GETTERS-------------*/
	int getBoardsize() const { return boardSize; }
	GameMode getGamemode() const { return mod; }
	CellState getTurnowner() const { return (turnOwner); }
	int getnumberMoves() const { return numberMoves; }
	int getcompnumberMoves() const { return compmoveCount; }
	bool getgameActivity() const { return activeGame; }
	const CellState isWin() const { return winner; }
    static int gettotalmarkedCount() { return totalnumMoves; }

	int getP1score() const { return player1Score; };
	int getP2score() const { return player2Score; }

	/*----------------GAME ASSOCIATED FUNCTIONS-----------*/
	void playGame();

	bool play(Cell &position);
	Cell &play();

	bool compareGames(const Hex &secondGame) const;
	void createBoard();

	static Hex &createGame();
	static void printGames();
	static Hex &chooseGame(const int choice);
    static int findintLength(int num);

private:
	static Hex *games;
	static int gameCount;
	static int maxgameCount;

	GameMode mod;
	Cell **hexCells;
	Cell **compBoard;
	Cell **allMoves;
	Cell *compMoves;
	Cell lastMove;
	CellState winner;
	CellState turnOwner;
	bool p1wcondL, p1wcondR;
	bool p2wcondU, p2wcondD;
	int boardSize;
	int numberMoves;
	static int totalnumMoves;
	int compmoveCount;
	bool activeGame;
	int compboardmoveCount;
	int maxcountLastmove;
	int maxcountcompMove;
	int player1Score;
	int player2Score;
	int maxScore;

	bool testHex() const;
	
	void incrValues(int value[2], const int direction) const;
	bool checkWcond(const int col, const int row, const CellState player);
	void checkWin();

	int findCommand(const string &com);
	void changeTurnowner();
	void addlastMove(Cell *lMove);
	void deleteBoard();
	void deletecompBoard();
	void calculateScore();

	void setCell(Cell &place, const CellState value) { place.setter(value); }

	Cell&getCell(const int col, const int row) { return (hexCells[row][col]); }
	const Cell&lookCell(const int col, const int row) const { return (hexCells[row][col]); }
	Cell &getcompCell(const int col, const int row) { return (compBoard[row][col]); }
	const Cell &lookcompCell(const int col, const int row) const;

	/*----------------SETTERS--------------*/
	bool setBoardsize(const int size) { boardSize = size; return testHex();}
	bool setGamemode(const GameMode m) { mod = m; return testHex();}
	void gameisActive() { activeGame = true; }

	/*-------------BOT HELPER FUNCTIONS-----------*/
	bool isAccessible(const int col, const int row) const;
	void comp_incr_values_upleft(const int direction, int value[2]) const;
	void comp_incr_values_dnleft(const int direction, int value[2]) const;
	void comp_incr_values_upright(const int direction, int value[2]) const;
	void comp_incr_values_dnright(const int direction, int value[2]) const;
	bool comp_is_empty(const int loc_col, const int loc_row) const;
	int comp_check_row(const int col, const int row) const;
	int comp_check_row_risk(const int col, const int row) const;
	int comp_check_risk(const int col, const int row) const;
	int comp_check_neighbor(const int col, const int row) const;
	void comp_reset_path();
	bool comp_update_pathfinding_upright(const int col, const int row, int &count);
	bool comp_update_pathfinding_upleft(const int col, const int row, int &count);
	bool comp_update_pathfinding_dnright(const int col, const int row, int &count);
	bool comp_update_pathfinding_dnleft(const int col, const int row, int &count);
	int comp_calc_row_risk(const int col, const int row) const;
	Cell* comp_put_move();
	void comp_save_enemy_move();
	void comp_no_logical_move();
	int comp_horizontal_side_risk_check() const;
	int comp_vertical_side_risk_check() const;
	void comp_find_path();
	Cell *comp_first_move(const int prowMove = -1);
	void createcompBoard();
	void printcompBoard() const;
	void setcompboardmoveCount();
	void addcompMove(const Cell &newMove);
};

#endif