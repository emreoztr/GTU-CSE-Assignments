#include <iostream>
#include <stack>
#include <deque>
#include <array>
#include <vector>
#include "AbstractHex.h"
#include "HexVector.h"
#include "HexArray1D.h"
#include "HexAdaptor.h"

using namespace OzturkHexGame;

namespace{
    const int MAXCAP = 256; //hex adapter max board size, 16*16
}

bool checkGameValidity(const AbstractHex *game){
    int p1Count = 0, p2Count = 0;
    if (game->getSize() >= 6)
    {
        for (int i = 0; i < game->getSize(); ++i){
            for (int j = 0; j < game->getSize(); ++j){
                if((*game)(j, i).takeValue() == player1 || (*game)(j, i).takeValue() == p1_win){
                    p1Count++;
                }
                else if((*game)(j, i).takeValue() == player2 || (*game)(j, i).takeValue() == p2_win){
                    p2Count++;
                }
                else if((*game)(j, i).takeValue() != emp){
                    return false;
                }
            }
        }
        if(p1Count > p2Count + 1 || p2Count > p1Count +1)
            return false;
        else
            return true;
    }
    else{
        return false;
    }
}

void checkArrayValidity(AbstractHex* arr[], int size){
    for (int i = 0; i < size; ++i){
        if(checkGameValidity(arr[i]) == true)
            cout << i+1 << ". Game is valid.\n";
        else
            cout << i+1 << ". Game is not valid!\n";
    }
}

int main()
{
    int size = 0, mod = -1, player = 0;
    int menuChoice = -1;
    int firstGame, secGame;
    int gameCount = 0;
    AbstractHex *activeGame;
    string filename;
    AbstractHex **games = new AbstractHex*[15];
    int gamesSize = 15;

    do
    {
        cout << "--------------HEX GAME----------------\n";
        cout << "1. New Game\n2. Load Game\n3. Compare Two Games\n4. Check Validity of Created Games\n0. Exit\n";
        cout << "Please make your choice: ";
        cin >> menuChoice;

        if(menuChoice == 1){
            cout << "Write size, mod and player will play first: (for example: '7 1 1', size can be 6 and more / mod can be (1)pvp or (2)pve / (1) player1 or (2) player2)";
            cin >> size >> mod >> player;
            cout << "Create a game with\n1. Vector\n2. 1D Array\n3. Adaptor\n";
            cout << "Make your choice: ";
            cin >> menuChoice;

            if(menuChoice == 1){
                activeGame = new HexVector(size, static_cast<GameMode>(mod), static_cast<CellState>(player));
            }
            else if(menuChoice == 2){
                activeGame = new HexArray1D(size, static_cast<GameMode>(mod), static_cast<CellState>(player));
            }
            else if(menuChoice == 3){
                cout << "1. Vector\n2. Deque\n3. Array (Boardsize can be 16 at most)\n";
                cout << "Make your choice: ";
                cin >> menuChoice;
                if(menuChoice == 1){
                    activeGame = new HexAdaptor<vector<Cell>>(size, static_cast<GameMode>(mod), static_cast<CellState>(player));
                }
                else if(menuChoice == 2){
                    activeGame = new HexAdaptor<deque<Cell>>(size, static_cast<GameMode>(mod), static_cast<CellState>(player));
                }
                else if(menuChoice == 3){
                    activeGame = new HexAdaptor<array<Cell, MAXCAP>>(size, static_cast<GameMode>(mod), static_cast<CellState>(player));
                }
            }
            games[gameCount++] = activeGame;
            if(gameCount >= gamesSize){
                AbstractHex **temp = new AbstractHex*[gamesSize + 15];
                for (int i = 0; i < gamesSize; ++i)
                {
                    temp[i] = games[i];
                }
                delete[] games;
                games = temp;
            }
            gamesSize += 15;
            activeGame->playGame();
        }

        else if(menuChoice == 2){
            cout << "Please write text file will be loaded (for example: 'test.txt')\n";
            cin >> filename;
            //program can load to other types too, remove the comment line of the type you want, then add comment to old one
            activeGame = new HexVector(filename); 
            //activeGame = new HexArray1D(filename);
            //activeGame = new HexAdaptor<vector<Cell> >(filename);
            //activeGame = new HexAdaptor<deque<Cell> >(filename);
            //activeGame = new HexAdaptor<array<Cell, MAXCAP> >(filename);
            games[gameCount++] = activeGame;
            activeGame->playGame();
        }

        else if(menuChoice == 3){
            for (int i = 0; i < gameCount; ++i){
                cout << i+1 << ". Game\n";
            }
            cout << "Please choose two games: (for example: '1 3' >> this will compare first and third game) ";
            cin >> firstGame >> secGame;
            if(*(games[firstGame-1]) == *(games[secGame-1]))
                cout << "Board situations are same.\n";
            else{
                cout << "Boards are not same\n";
            }
        }

        else if(menuChoice == 4){
            checkArrayValidity(games, gameCount);
        }
    } while (menuChoice != 0);

    for (int i = 0; i < gameCount; ++i){    //deleting all the games
        delete games[i];
    }
    delete[] games; //deleting the games array
}