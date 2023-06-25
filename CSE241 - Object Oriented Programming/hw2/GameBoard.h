#ifndef GAMEBOARD_H_
#define GAMEBOARD_H_

int gboard_prep(char board[12][12]);
void gboard_prep(char board[12][12], const int size);
void gboard_print(char board[12][12], int size);
void gboard_fill_empty(char board[12][12]);


#endif
