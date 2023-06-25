#ifndef HEXCELL_H_
#define HEXCELL_H_

#include <iostream>

using namespace std;
namespace OzturkHexGame{

enum CellState{emp = 0, player1, player2, p1_win, p2_win, comp, comp_win, risk, h_priority, l_priority, k_priority, enemy, comp_closed, no, closed};

class Cell{
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
}

#endif
