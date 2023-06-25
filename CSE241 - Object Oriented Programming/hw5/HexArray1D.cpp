#include <iostream>
#include <cstdlib>
#include <string>
#include "HexArray1D.h"

using namespace std;

namespace OzturkHexGame{
    HexArray1D::HexArray1D(int bSize, GameMode mod, CellState firstPlayer) : AbstractHex(bSize, mod, firstPlayer), _hexBoard(nullptr){
        setSize(bSize);
    }
    HexArray1D::HexArray1D(const string &filename){
        readFromFile(filename);
    }

    /*BIG THREE*/
    HexArray1D::HexArray1D(const HexArray1D &other){
        (*this) = other;
    }
    const HexArray1D &HexArray1D::operator=(const HexArray1D &other){
        if(this != &other){
            (*this).AbstractHex::operator=(other);  //assigning the base class part
            setSize(other.getSize());
            for (int i = 0; i < getSize(); ++i){
                for (int j = 0; j < getSize(); ++j){
                    this->getCell(j, i).setter(other(j, i).takeValue());
                }
            }
        }
        return *this;
    }
    HexArray1D::~HexArray1D(){
        if(_hexBoard != nullptr)
            free(_hexBoard);
    }


    /*MOVE SEMANTICS*/
    HexArray1D::HexArray1D(HexArray1D &&temp){
        (*this).AbstractHex::operator=(temp);   //copying all abstract class members
        if(_hexBoard != nullptr)    //if lvalue is not nullptr this part will delete it
            free(_hexBoard);
        _hexBoard = temp._hexBoard; //takes the temp part's _hexBoard
        temp._hexBoard = nullptr;   //makes temp's part _hexBoard to nullptr because temp part will call destructor after this move semantic copy constructor
    }

    const HexArray1D &HexArray1D::operator=(HexArray1D &&temp){
        (*this).AbstractHex::operator=(temp);   //copying all abstract class members
        if(_hexBoard != nullptr)    //if lvalue is not nullptr this part will delete it
            free(_hexBoard);
        _hexBoard = temp._hexBoard; //takes the temp part's _hexBoard
        temp._hexBoard = nullptr;   //makes temp's part _hexBoard to nullptr because temp part will call destructor after this move semantic copy constructor
        return (*this);
    }



    void HexArray1D::reset(){
        if(_hexBoard != nullptr){
            free(_hexBoard);
            _hexBoard = nullptr;
        }
        _hexBoard = static_cast<Cell*> (calloc(getSize() * getSize(), sizeof(Cell)));
        for (int i = 0; i < getSize(); ++i){
            for (int j = 0; j < getSize(); ++j){
                _hexBoard[(i * getSize()) + j].setter(j, i);
            }
        }
        resetComp();
    }

    const Cell &HexArray1D::operator()(int col, int row) const{
        if((col>=0 && col<getSize()) && (row>=0 && row<getSize()))
            return _hexBoard[(row * getSize()) + col];
        else
            throw InvalidHexPlace();
    }
    Cell &HexArray1D::getCell(int col, int row){
        return _hexBoard[(row * getSize()) + col];
    }
}