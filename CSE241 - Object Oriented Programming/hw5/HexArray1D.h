#ifndef HEXARRAY1D_H_
#define HEXARRAY1D_H_

#include <iostream>
#include <cstdlib>
#include "AbstractHex.h"

using namespace std;

namespace OzturkHexGame{
class HexArray1D : public AbstractHex{
public:
    HexArray1D(int bSize, GameMode mod, CellState firstPlayer);
    HexArray1D(const string &filename);
    
    /*BIG THREE*/
    HexArray1D(const HexArray1D &other);
    const HexArray1D &operator=(const HexArray1D &other);
    virtual ~HexArray1D() override;

    /*MOVE SEMANTICS*/
    HexArray1D(HexArray1D &&temp);
    const HexArray1D &operator=(HexArray1D &&temp);

    virtual void reset() override;
    virtual const Cell &operator()(int col, int row) const override;

private:
    Cell *_hexBoard;
    virtual Cell &getCell(int col, int row) override;
};
}

#endif