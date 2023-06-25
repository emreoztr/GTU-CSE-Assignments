#ifndef HEXVECTOR_H_
#define HEXVECTOR_H_

#include <iostream>
#include <vector>
#include <string>
#include "AbstractHex.h"
#include "HexCell.h"

using namespace std;

namespace OzturkHexGame{
class HexVector : public AbstractHex{
public:
    HexVector(int bSize, GameMode mod, CellState firstPlayer);
    HexVector(const string &filename);
    virtual ~HexVector(){/*Intentionally Empty*/}
    
void print() const;
    virtual const Cell &operator()(int col, int row) const override;

private:
    virtual void reset() override;
    virtual Cell &getCell(int col, int row) override;
    vector<vector<Cell> > _hexBoard;


};
}
#endif