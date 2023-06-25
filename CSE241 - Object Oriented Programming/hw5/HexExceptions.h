#ifndef HEXEXCEPTIONS_H_
#define HEXEXCEPTIONS_H_

#include <iostream>

using std::exception;

namespace OzturkHexGame{

class InvalidHexSize : public exception{
public:
    const char* what(){
    	return("Invalid hex board size, hex board sizes should be bigger than 6. (If it is created with STL Array it cannot pass 16");
    }
};

class InvalidHexPlace : public exception{
public:
    const char* what(){
    	return("Place of the move is invalid!");
    }
};

class CantPlayEndedGame : public exception{
public:
    const char* what(){
    	return("Cant play an ended game!");
    }
};

class HexLoadError : public exception{
public:
    const char* what(){
    	return("Game not loaded successfully!");
    }
};

class HexSaveError : public exception{
public:
    const char* what(){
    	return("Game not saved successfully!");
    }
};

class HexNoLastMove : public exception{
public:
    const char* what(){
    	return("There is no last move!");
    }
};


class InvalidGameMode : public exception{
public:
    const char* what(){
    	return("Gamemode is not valid!");
    }
};
} // namespace OzturkHexGame

#endif