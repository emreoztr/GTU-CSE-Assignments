#ifndef HEXADAPTOR_H_
#define HEXADAPTOR_H_

#include <iostream>
#include "AbstractHex.h"
#include "HexExceptions.h"

using namespace std;

namespace OzturkHexGame{

template<class T>
class HexAdaptor : public AbstractHex{
public:
    HexAdaptor(int bSize, GameMode mod, CellState firstPlayer);
    HexAdaptor(const string &filename);
    virtual ~HexAdaptor(){}

    virtual void reset() override;

    virtual const Cell &operator()(int col, int row) const override;

private:
    T _hexBoard;
    const int _maxcap;

    virtual Cell &getCell(int col, int row);
};
}
#endif