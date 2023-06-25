#ifndef GAMESYS_H_
#define GAMESYS_H_

const char p1 = 'x';
const char p2 = 'o';

void game_incr_values(int direction, int value[2]);
void game_movement(char board[12][12], const int size, const char player, int position[2]);
bool game_wcond_check(char board[12][12], const int size, const char player, int x, int y, const char w_state);
char game_gameplay_pvp(char board[12][12], const int size, const char player1, const char player2);

#endif
