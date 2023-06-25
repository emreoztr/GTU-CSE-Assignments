#include <iostream>
#include <string>
#include <vector>
#include <fstream>
#include "HexGame.h"

int main()
{
	int choice, compChoice1, compChoice2;

    /*FIVE GAMES AUTOMATICALLY LOADED, YOU CAN ACCESS THEM FROM '2. Choose Active Game', YOU CAN TYPE 'MENU' WHILE PLAYING A GAME TO PLAY OTHERS*/
    Hex::loadGame("save1.txt");
    Hex::loadGame("save2.txt");
    Hex::loadGame("save3.txt");
    Hex::loadGame("save4.txt");
    Hex::loadGame("save5.txt");

    cout << "IN-GAME COMMANDS (WHILE PLAYING GAME): \nMENU : to return main menu\nSAVE filename.txt : to save game\nLOAD filename.txt : to load game (you can access it from 'Choose active games' after loaded the game)\n";

	do{ 
		cout << "1. New Game\n2. Choose Active Game\n3. Compare Move Count of Two Active Game\n4. Get total marked cell number in all games\n0. EXIT\nPlease write your choice: ";
		cin >> choice;
		cin.ignore(1, '\n');
		if(choice==1){
			cout << sizeof(Hex::createGame()) << endl;
			Hex::createGame().playGame();
		}
		else if(choice==2){
			Hex::printGames();
			cout << "Please write your choice: ";
			cin >> choice;
			cin.ignore(1, '\n');
			Hex::chooseGame(choice).playGame();
		}
		else if(choice==3){
			Hex::printGames();
			cout << "Please choose the first game to compare: ";
			cin >> compChoice1;
			cin.ignore(1, '\n');
			cout << "Please choose the second game to compare: ";
			cin >> compChoice2;
			cin.ignore(1, '\n');

			if(Hex::chooseGame(compChoice1).compareGames(Hex::chooseGame(compChoice2)))
				cout << "First game has more marked cells.\n";

			else
				cout << "Second game has more marked cells.\n";
		}
        else if(choice==4){
            cout << "Total marked cells on all active game boards " << Hex::gettotalmarkedCount() << endl;
        }
    } while (choice != 0);

    return 0;
}