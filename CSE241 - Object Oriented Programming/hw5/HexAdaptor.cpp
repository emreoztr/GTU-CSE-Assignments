#include <iostream>
#include <deque>
#include <array>
#include <sstream>
#include <string>
#include "HexAdaptor.h"

using namespace std;

namespace OzturkHexGame{
    template<class T>
    HexAdaptor<T>::HexAdaptor(int bSize, GameMode mod, CellState firstPlayer) : _maxcap(256), AbstractHex(bSize, mod, firstPlayer){
        setSize(bSize);
    }

    template<class T>
    HexAdaptor<T>::HexAdaptor(const string &filename) : _maxcap(256){
        setSize(_maxcap);
        readFromFile(filename);
    }


    template<class T>
    void HexAdaptor<T>::reset(){
        if(_hexBoard.empty()){
            _hexBoard.resize(getSize() * getSize());
        }
        auto iter = _hexBoard.begin();
        for (int i = 0; i < getSize(); ++i)
        {
            for (int j = 0; j < getSize(); ++j){
                iter->setter(j, i, emp);
                iter++;
            }
        }
        resetComp();
    }

    template<>
    void HexAdaptor<array<Cell, 256> >::reset(){
        if(getSize()*getSize() <= _maxcap){
            auto iter = _hexBoard.begin();
            for (int i = 0; i < getSize(); ++i)
            {
                for (int j = 0; j < getSize(); ++j){
                    iter->setter(j, i);
                    iter++;
                }
            }
        }
        else{
            throw InvalidHexSize();
        }
    }

    template<class T>
    const Cell &HexAdaptor<T>::operator()(int col, int row) const{
        if((col>=0 && col<getSize()) && (row>=0 && row<getSize()))
            return _hexBoard[(row * getSize()) + col];
        else{
            throw InvalidHexPlace();
        }
    }

    template<class T>
    Cell &HexAdaptor<T>::getCell(int col, int row){
        return _hexBoard[(row * getSize()) + col];
    }


template class HexAdaptor<vector<Cell> >;
template class HexAdaptor<deque<Cell> >;
template class HexAdaptor<array<Cell, 256> >;
}