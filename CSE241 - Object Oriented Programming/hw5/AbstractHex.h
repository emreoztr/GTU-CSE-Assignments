#ifndef ABSTRACTHEX_H_
#define ABSTRACTHEX_H_

#include <iostream>
#include <vector>
#include <string>
#include "HexCell.h"
#include "HexExceptions.h"

using namespace std;

namespace OzturkHexGame{

enum GameMode{pvp = 1, pve};

class AbstractHex{
public:
    AbstractHex();
    AbstractHex(int bSize, GameMode mod, CellState firstPlayer);

    virtual ~AbstractHex(){/*Intentionally Empty*/}
    
    void print() const;
    friend ostream &operator<<(ostream &out, const AbstractHex &game);

    void readFromFile(const string &filename);
    void writeToFile(const string &filename) const;

    virtual void reset() = 0;

    void setSize(int boardSize);
    int getSize() const;

    void play(int col, int row);
    void play();
    virtual void playGame();

    bool isEnd() const;

    virtual const Cell &operator()(int col, int row) const = 0;
    bool operator==(const AbstractHex &other) const;
    const Cell &lastMove() const;
    int numberOfMoves() const;

    const CellState &getTurnown() const;
    const CellState getWinner() const;

protected:
    void p1SetLeftCond(bool val);
    void p1SetRightCond(bool val);
    void p2SetUpCond(bool val);
    void p2SetDnCond(bool val);
    void changeTurnown();

    void setGamemode(GameMode mod);
    void setWinner(CellState winner);

    virtual Cell &getCell(int col, int row) = 0;
    const GameMode getGamemode() const;

    void resetComp();

private:
    void setTurnown(const CellState turnOwn);
    void setComp();

    const CellState checkWin();
    bool checkWcond(const int col, const int row, const CellState player);

    const Cell &lookcompCell(int col, int row) const;
    Cell &getcompCell(int col, int row);

    void incrValues(int value[2], const int direction) const;
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
	void setcompboardmoveCount();
	void addcompMove(const Cell &newMove);

    vector<vector<Cell> > _compBoard;    //this is for computer play, it shouldnt be changed by derive
    vector<Cell> _compMoves;

    GameMode _mod;
    int _boardSize;
    int _moveCount;
    int _moveCountComp;
    Cell _lastMove;
    bool _p1Left, _p1Right;
    bool _p2Up, _p2Dn;
    CellState _winner;
    CellState _turnOwner;
};
}

#endif