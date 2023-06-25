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
private:
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

	Hex(int size) : boardSize(6), winner(emp), turnOwner(player1), p1wcondL(false), p1wcondR(false),
					p2wcondU(false), p2wcondD(false), numberMoves(0), compmoveCount(0), activeGame(false),
					compboardmoveCount(0)
	{
		testHex();
	}

	/*------------------GETTERS-------------*/
	int getBoardsize() const { return boardSize; }
	GameMode getGamemode() const { return mod; }
	Cell&getCell(const int col, const int row) { return (hexCells.at(row).at(col)); }
	const Cell&lookCell(const int col, const int row) const { return (hexCells.at(row).at(col)); }
	Cell &getcompCell(const int col, const int row) { return (compBoard.at(row).at(col)); }
	const Cell &lookcompCell(const int col, const int row) const;
	CellState getTurnowner() const { return (turnOwner); }
	int getnumberMoves() const { return numberMoves; }
	int getcompnumberMoves() const { return compmoveCount; }
	bool getgameActivity() const { return activeGame; }
	const CellState isWin() const { return winner; }

    static int gettotalmarkedCount() { return totalnumMoves; }

	/*----------------SETTERS--------------*/
	bool setBoardsize(const int size) { boardSize = size; return testHex();}
	bool setGamemode(const GameMode m) { mod = m; return testHex();}
	void setCell(Cell &place, const CellState value) { place.setter(value); }
	void gameisActive() { activeGame = true; }

	/*----------------GAME ASSOCIATED FUNCTIONS-----------*/
	void playGame();
	void printBoard() const;
	
	bool compareGames(const Hex &secondGame) const;
	void saveGame(const string &filename) const;
	void createBoard();

	static void loadGame(const string &filename);
	static Hex &createGame();
	static void printGames();
	static Hex &chooseGame(const int choice);
    static int findintLength(int num);

private:
	static vector<Hex> games;
	static int gameCount;

	GameMode mod; //
	vector< vector<Cell> > hexCells;
	vector< vector<Cell> > compBoard;
	vector<Cell> compMoves; //
	Cell lastMove; //
	CellState winner;	//
	CellState turnOwner; //
	bool p1wcondL, p1wcondR; //
	bool p2wcondU, p2wcondD; //
	int boardSize; //
	int numberMoves; //
	static int totalnumMoves;
	int compmoveCount;//
	bool activeGame;  //
	int compboardmoveCount;

	bool testHex() const;
	
	void incrValues(vector<int> &value, const int direction) const;
	bool checkWcond(const int col, const int row, const CellState player);
	void checkWin();
	bool play(Cell &position);
	int findCommand(const string &com);
	void changeTurnowner();

	/*-------------BOT HELPER FUNCTIONS-----------*/
	void play();
	bool isAccessible(const int col, const int row) const;
	void comp_incr_values_upleft(const int direction, vector<int> &value) const;
	void comp_incr_values_dnleft(const int direction, vector<int> &value) const;
	void comp_incr_values_upright(const int direction, vector<int> &value) const;
	void comp_incr_values_dnright(const int direction, vector<int> &value) const;
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
	void comp_put_move();
	void comp_save_enemy_move();
	void comp_no_logical_move();
	int comp_horizontal_side_risk_check() const;
	int comp_vertical_side_risk_check() const;
	void comp_find_path();
	void comp_first_move(const int prowMove = -1);
	void createcompBoard();
	void printcompBoard() const;
	void setcompboardmoveCount();
};


#endif