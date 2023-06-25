#ifndef GAMEAI_H_
#define GAMEAI_H_

int comp_choose_side(const int size, const int col, const int row);
void comp_incr_values_default(int direction, int value[2]);
void comp_incr_values_upleft(int direction, int value[2]);
void comp_incr_values_dnleft(int direction, int value[2]);
void comp_incr_values_upright(int direction, int value[2]);
void comp_incr_values_dnright(int direction, int value[2]);
bool comp_is_empty(const char a_board[12][12], const int size, const int loc_col, const int loc_row);
int comp_check_row(const char a_board[12][12], const int size, const int col, const int row);
int comp_check_row_risk(const char a_board[12][12], const int size, const int col, const int row);
int comp_check_risk(const char a_board[12][12], const int size, const int col, const int row);
int comp_check_neighbor(const char a_board[12][12], const int size, const int col, const int row);
void comp_reset_path(char a_board[12][12], const int size);
bool comp_update_pathfinding_upright(char a_board[12][12], const int size, const int col, const int row, int& count);
bool comp_update_pathfinding_upleft(char a_board[12][12], const int size, const int col, const int row, int& count);
bool comp_update_pathfinding_dnright(char a_board[12][12], const int size, const int col, const int row, int& count);
bool comp_update_pathfinding_dnleft(char a_board[12][12], const int size, const int col, const int row, int& count);
int comp_calc_row_risk(const char a_board[12][12], const int size, const int col, const int row);
void comp_put_move(char board[12][12], char a_board[12][12], const int size, const char comp_sym, int c_move[2]);
void comp_save_enemy_move(char a_board[12][12], const int size, const int p_move[2]);
void comp_aboard_setup(char a_board[12][12], const int size);
void comp_no_logical_move(char a_board[12][12], const int size);
char comp_gameplay_pve(char board[12][12], const int size, const char player, const char comp_sym, const char option, bool turnFirst = false , bool load = false, int c_move_pointer = 0);


#endif
