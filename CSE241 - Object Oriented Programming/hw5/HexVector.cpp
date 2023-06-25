#include <iostream>
#include <vector>
#include <string>
#include "AbstractHex.h"
#include "HexVector.h"
#include "HexCell.h"

using namespace std;

namespace OzturkHexGame{
HexVector::HexVector(int bSize, GameMode mod, CellState firstPlayer) : AbstractHex(bSize, mod, firstPlayer){
    setSize(bSize);
}


HexVector::HexVector(const string &filename) { readFromFile(filename); }

void HexVector::reset(){
    vector<Cell> temp;
    if(_hexBoard.size()!=0)
        _hexBoard.clear();
    for (int i = 0; i < getSize(); ++i){
        for (int j = 0; j < getSize(); ++j){
            temp.push_back(Cell(j, i, emp));
        }
        _hexBoard.push_back(temp);
        temp.clear();
    }
    resetComp();
}

const Cell &HexVector::operator()(int col, int row) const{
    if((col>=0 && col<getSize()) && (row>=0 && row<getSize()))
        return (_hexBoard[row][col]);
    else
        throw InvalidHexPlace();
}

Cell &HexVector::getCell(int col, int row){
    return (_hexBoard[row][col]);
}


}