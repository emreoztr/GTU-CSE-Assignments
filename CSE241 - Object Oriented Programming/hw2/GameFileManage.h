#ifndef GAMEFILEMANAGE_H_
#define GAMEFILEMANAGE_H_
#include <string>
using namespace std;

enum cell {empty = '.', pl1 = 'x', pl2 = 'o', pl1_w = 'X', pl2_w = 'O' };
enum gamemode { pvp=0, pve };
char fman_save(const string& filename, const char board[12][12], const int size, const char player, const int moveCount, const gamemode& mode);
char fman_load(string filename, char board[12][12]);

#endif
