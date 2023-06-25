#include <iostream>
#include <string>
#include <vector>
#include <fstream>
#include "HexGame.h"

int main()
{
	int choice, compChoice1, compChoice2;

    /*FIVE GAMES AUTOMATICALLY LOADED, YOU CAN ACCESS THEM FROM '2. Choose Active Game', YOU CAN TYPE 'MENU' WHILE PLAYING A GAME TO PLAY OTHERS*/
	ifstream file;
	file.open("save1.txt");
	file >> Hex::chooseGame(1);
	file.close();
	file.open("save2.txt");
	file >> Hex::chooseGame(2);
	file.close();
	file.open("save3.txt");
	file >> Hex::chooseGame(3);
	file.close();
	file.open("save4.txt");
	file >> Hex::chooseGame(4);
	file.close();
	file.open("save5.txt");
	file >> Hex::chooseGame(5);
	file.close();

	cout << "IN-GAME COMMANDS (WHILE PLAYING GAME): \nMENU : to return main menu\nSAVE filename.txt : to save game\nLOAD filename.txt : to load game (you can access it from 'Choose active games' after loaded the game)\nUNDO : to remove last move (against computer it will remove two moves because otherwise it doesn't make sense)\nSCORE : To print the scores of both players.\n";

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

			if(Hex::chooseGame(compChoice1) == Hex::chooseGame(compChoice2))
				cout << "First game has more marked cells for the USER(s).\n";
			else
				cout << "Second game has more marked cells for the USER(s).\n";
		}
        else if(choice==4){
            cout << "Total marked cells on all active game boards " << Hex::gettotalmarkedCount() << endl;
        }
    } while (choice != 0);

    return 0;
}