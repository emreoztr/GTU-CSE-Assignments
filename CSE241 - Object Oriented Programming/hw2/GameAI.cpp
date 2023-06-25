#include <iostream>
#include "GameBoard.h"
#include "GameSys.h"
#include "GameAI.h"
#include "GameFileManage.h"

using namespace std;

int comp_choose_side(const int size, const int col, const int row){	//checks the side of a place
	int returnVal;
	if(col>=(size/2.0))
		returnVal=0;
	else
		returnVal=1;

	return returnVal;
}

void comp_incr_values_default(int direction, int value[2]){	//default incrementation values

	switch(direction){
		case 0:
			value[0]=0;
			value[1]=-1;
			break;
		case 1:
			value[0]=-1;
			value[1]=0;
			break;
		case 2:
			value[0]=1;
			value[1]=-1;
			break;
		case 3:
			value[0]=1;
			value[1]=0;
			break;
		case 4:
			value[0]=0;
			value[1]=1;
			break;
		case 5:
			value[0]=-1;
			value[1]=1;
			break;
		default:
			value[0]=0;
			value[1]=0;
			break;
	}
}

void comp_incr_values_upleft(int direction, int value[2]) {	//incrementation values for up leftt direction

	switch (direction) {
	case 0:
		value[0] = 0;
		value[1] = -1;
		break;
	case 1:
		value[0] = -1;
		value[1] = 0;
		break;
	case 2:
		value[0] = 1;
		value[1] = -1;
		break;
	case 3:
		value[0] = 1;
		value[1] = 0;
		break;
	default:
		value[0] = 0;
		value[1] = 0;
		break;
	}
}

void comp_incr_values_dnleft(int direction, int value[2]) {	//incrementation values for down leftt direction

	switch (direction) {
	case 0:
		value[0] = -1;
		value[1] = 1;
		break;
	case 1:
		value[0] = 0;
		value[1] = 1;
		break;
	case 2:
		value[0] = -1;
		value[1] = 0;
		break;
	case 3:
		value[0] = 1;
		value[1] = 0;
		break;
	default:
		value[0] = 0;
		value[1] = 0;
		break;
	}
}

void comp_incr_values_upright(int direction, int value[2]) {		//incrementation values for up right direction

	switch (direction) {
	case 0:
		value[0] = 1;
		value[1] = -1;
		break;
	case 1:
		value[0] = 0;
		value[1] = -1;
		break;
	case 2:
		value[0] = 1;
		value[1] = 0;
		break;
	case 3:
		value[0] = -1;
		value[1] = 0;
		break;
	default:
		value[0] = 0;
		value[1] = 0;
		break;
	}
}

void comp_incr_values_dnright(int direction, int value[2]) {	//incrementation values for down right direction

	switch (direction) {
	case 0:
		value[0] = 0;
		value[1] = 1;
		break;
	case 1:
		value[0] = 1;
		value[1] = 0;
		break;
	case 2:
		value[0] = -1;
		value[1] = 1;
		break;
	case 3:
		value[0] = -1;
		value[1] = 0;
		break;
	default:
		value[0] = 0;
		value[1] = 0;
		break;
	}
}

bool comp_is_empty(const char a_board[12][12], const int size, const int loc_col, const int loc_row){	//checks the place is safe or not
	bool returnVal=false;

	if(((loc_col>=0 && loc_col<size) && (loc_row>=0 && loc_row<size)))
		if(a_board[loc_row][loc_col]!='E' && a_board[loc_row][loc_col]!='Q' && a_board[loc_row][loc_col]!='W')
			returnVal=true;

	return returnVal;
}

int comp_check_row(const char a_board[12][12], const int size, const int col, const int row){	//checks the enemy in the row
	int returnVal=-1;
	bool end_look=false;


	for(int i=0; i<col; ++i){
		if(a_board[row][i]=='E'){
			returnVal=col-i;
			end_look=true;
		}
		if(returnVal!=-1 && (a_board[row][i]=='C' || a_board[row][i]=='W')){
			returnVal=-1;
			end_look=false;
		}
	}

	for(int i=size-1; i>col && !end_look; --i){
		if(a_board[row][i]=='E')
			returnVal=i-col;
		if(returnVal!=-1 && (a_board[row][i]=='C' || a_board[row][i]=='W')){
			returnVal=-1;
		}
	}

	return returnVal;
}

int comp_check_row_risk(const char a_board[12][12], const int size, const int col, const int row){	//checks the risk value in a row
	int returnVal=-1;
	bool end_look=false;


	for(int i=0; i<col; ++i){
		if(a_board[row][i]=='R'){
			returnVal=col-i;
			end_look=true;
		}
		if(returnVal!=-1 && (a_board[row][i]=='C' || a_board[row][i]=='W')){
			returnVal=-1;
			end_look=false;
		}
	}

	for(int i=size-1; i>col && !end_look; --i){
		if(a_board[row][i]=='R')
			returnVal=i-col;
		if(returnVal!=-1 && (a_board[row][i]=='C' || a_board[row][i]=='W')){
			returnVal=-1;
		}
	}

	return returnVal;
}

int comp_check_risk(const char a_board[12][12], const int size, const int col, const int row){	//checks the risk value for edges of place
	int r_count=0, incrVal[2];
	for(int i=0; i<6; i++){
		comp_incr_values_default(i, incrVal);
		if(a_board[row+incrVal[1]][col+incrVal[0]]=='R')
			r_count+=1;
	}
	if((col==size-1 || col==0) && r_count>=1)
		r_count=5;

	return r_count;
}

int comp_check_neighbor(const char a_board[12][12], const int size, const int col, const int row){	//returns the count of already moved places at the edges of place
	int c_count=0, incrVal[2];

	for(int i=0; i<6; i++){
		comp_incr_values_default(i, incrVal);
		if(a_board[row+incrVal[1]][col+incrVal[0]]=='C')
			c_count+=1;
	}

	return c_count;
}

void comp_reset_path(char a_board[12][12], const int size){	//resets L(Low priority values), H(High priority values) and K(Highest priority values)
	for(int i=0; i<size; ++i){
		for(int j=0; j<size; ++j){
			if(a_board[i][j]=='L' || a_board[i][j]=='H' || a_board[i][j]=='K')
				a_board[i][j]='.';
		}
	}
}

bool comp_update_pathfinding_upright(char a_board[12][12], const int size, const int col, const int row, int& count){
	int incrVal[2];
	bool returnVal=false;
	char holderBf, holderAf;

	if(a_board[row][col]!='C')
		count+=1;

	if(count<4*size){
		holderBf=a_board[row][col];	//saving for in case turning it back
		holderAf=a_board[row][col];

		if(holderBf!='C'){	//in this if section program choose the priority for the place
			if((comp_check_row(a_board, size, col, row)<=1 && comp_check_row(a_board, size, col, row)>=0) || comp_check_risk(a_board, size, col, row)>=4 || holderBf=='R')
				holderAf='K';
			else if(comp_check_risk(a_board, size, col, row)>=1)
				holderAf='H';
			else
				holderAf='L';
		}

		a_board[row][col]='Q';	//changing the name of the place to make sure the program wont go to this place again
		if(holderBf=='C')
			a_board[row][col]='W';	//if it is move of the computer program will change it to the W instead of Q

		if(row==0)	//if it is at the top, path condition satisfies and return true to end recursive
			returnVal=true;

		for(int i=0; i<4 && returnVal==false; ++i){	//taking the increment values and checks the new place is safe or not
			comp_incr_values_upright(i, incrVal);

			returnVal=comp_is_empty(a_board, size, col+incrVal[0], row+incrVal[1]);

			if(returnVal==true)
				returnVal=comp_update_pathfinding_upright(a_board, size, col+incrVal[0], row+incrVal[1], count);
		}

		if(returnVal==false){	//if it has not succeed it takes the value of place back
			a_board[row][col]=holderBf;
			if(count<4*size)	//if counter is more than the border, it doesn't decrease counter
				count-=1;
		}
		else{
			a_board[row][col]=holderAf;	//if it has succeed place takes the new value
		}
	}
	return returnVal;
}

bool comp_update_pathfinding_upleft(char a_board[12][12], const int size, const int col, const int row, int& count){
	int incrVal[2];
	bool returnVal=false;
	char holderBf, holderAf;

	if(a_board[row][col]!='C')
		count+=1;

	if(count<4*size){
		holderBf=a_board[row][col];	//saving for in case turning it back
		holderAf=a_board[row][col];

		if(holderBf!='C'){	//in this if section program choose the priority for the place
			if((comp_check_row(a_board, size, col, row)<=1 && comp_check_row(a_board, size, col, row)>=0) || comp_check_risk(a_board, size, col, row)>=4 || holderBf=='R')
				holderAf='K';
			else if(holderBf=='R' || comp_check_risk(a_board, size, col, row)>=1)
				holderAf='H';
			else
				holderAf='L';
		}

		a_board[row][col]='Q';	//changing the name of the place to make sure the program wont go to this place again
		if(holderBf=='C')
			a_board[row][col]='W';	//if it is move of the computer program will change it to the W instead of Q

		if(row==0)	//if it is at the top, path condition satisfies and return true to end recursive
			returnVal=true;

		for(int i=0; i<4 && returnVal==false; ++i){	//taking the increment values and checks the new place is safe or not
			comp_incr_values_upleft(i, incrVal);

			returnVal=comp_is_empty(a_board, size, col+incrVal[0], row+incrVal[1]);

			if(returnVal==true)
				returnVal=comp_update_pathfinding_upleft(a_board, size, col+incrVal[0], row+incrVal[1], count);
		}

		if(returnVal==false){	//if it has not succeed it takes the value of place back
			a_board[row][col]=holderBf;
			if(count<4*size)	//if counter is more than the border, it doesn't decrease counter
				count-=1;
		}
		else{
			a_board[row][col]=holderAf;	//if it has succeed place takes the new value
		}
	}
	return returnVal;
}

bool comp_update_pathfinding_dnright(char a_board[12][12], const int size, const int col, const int row, int& count){	//updating the pathfind board to downright direction
	int incrVal[2];
	bool returnVal=false;
	char holderBf, holderAf;

	if(a_board[row][col]!='C')
		count+=1;

	if(count<4*size){
		holderBf=a_board[row][col];	//saving for in case turning it back
		holderAf=a_board[row][col];

		if(holderBf!='C'){	//in this if section program choose the priority for the place
			if((comp_check_row(a_board, size, col, row)<=1 && comp_check_row(a_board, size, col, row)>=0) || comp_check_risk(a_board, size, col, row)>=4 || holderBf=='R')
				holderAf='K';
			else if(holderBf=='R' || comp_check_risk(a_board, size, col, row)>=1)
				holderAf='H';
			else
				holderAf='L';
		}

		a_board[row][col]='Q';	//changing the name of the place to make sure the program wont go to this place again

		if(holderBf=='C')
			a_board[row][col]='W';	//if it is move of the computer program will change it to the W instead of Q

		if(row==size-1)	//if it is at the bottom path condition satisfies and return true to end recursive
			returnVal=true;

		for(int i=0; i<4 && returnVal==false; ++i){	//taking the increment values and checks the new place is safe or not
			comp_incr_values_dnright(i, incrVal);
			returnVal=comp_is_empty(a_board, size, col+incrVal[0], row+incrVal[1]);

			if(returnVal==true)
				returnVal=comp_update_pathfinding_dnright(a_board, size, col+incrVal[0], row+incrVal[1], count);
		}

		if(returnVal==false){	//if it has not succeed it takes the value of place back
			a_board[row][col]=holderBf;
			if(count<4*size)	//if counter is more than the border, it doesn't decrease counter
				count-=1;
		}
		else{
			a_board[row][col]=holderAf;	//if it has succeed place takes the new value
		}
	}
	return returnVal;
}

bool comp_update_pathfinding_dnleft(char a_board[12][12], const int size, const int col, const int row, int& count){	//updating the pathfind board to downleft direction
	int incrVal[2];
	bool returnVal=false;
	char holderBf, holderAf;

	if(a_board[row][col]!='C')
		count+=1;

	if(count<4*size){	//checking the try limit for recursive to prevent long wait times
		holderBf=a_board[row][col];	//saving for in case turning it back
		holderAf=a_board[row][col];

		if(holderBf!='C'){			//in this if section program choose the priority for the place
			if((comp_check_row(a_board, size, col, row)<=1 && comp_check_row(a_board, size, col, row)>=0) || comp_check_risk(a_board, size, col, row)>=4 || holderBf=='R')
				holderAf='K';
			else if(holderBf=='R' || comp_check_risk(a_board, size, col, row)>=1)
				holderAf='H';
			else
				holderAf='L';
		}

		a_board[row][col]='Q';	//changing the name of the place to make sure the program wont go to this place again

		if(holderBf=='C')
			a_board[row][col]='W';	//if it is move of the computer program will change it to the W instead of Q

		if(row==size-1)	//if it is at the bottom path condition satisfies and return true to end recursive
			returnVal=true;

		for(int i=0; i<4 && returnVal==false; ++i){	//taking the increment values and checks the new place is safe or not
			comp_incr_values_dnleft(i, incrVal);
			returnVal=comp_is_empty(a_board, size, col+incrVal[0], row+incrVal[1]);

			if(returnVal==true)
				returnVal=comp_update_pathfinding_dnleft(a_board, size, col+incrVal[0], row+incrVal[1], count);
		}

		if(returnVal==false){
			a_board[row][col]=holderBf;
			if(count<4*size)
				count-=1;
		}
		else{
			a_board[row][col]=holderAf;
		}
	}
	return returnVal;
}

int comp_calc_row_risk(const char a_board[12][12], const int size, const int col, const int row){	//calculates the risk at the row
	int risk_count_l=0, risk_count_r=0;

	for(int i=0; i<col; ++i){
		if(a_board[row][i]=='R' || a_board[row][i]=='E')
			risk_count_l+=1;
		else if(a_board[row][i]=='C')
			risk_count_l=0;
	}

	for(int i=size-1; i>col; --i){
		if(a_board[row][i]=='R' || a_board[row][i]=='E')
			risk_count_r+=1;
		else if(a_board[row][i]=='C')
			risk_count_r=0;
	}
	return (risk_count_l+risk_count_r);
}

void comp_put_move(char board[12][12], char a_board[12][12], const int size, const char comp_sym, int c_move[2]){
	bool end_loop=false, risk_check=false;
	int col, row, biggest_risk=-1, holder[2], risk;

	for(row=0; row<size; ++row){
		for(col=0; col<size; ++col){
			if(a_board[row][col]=='K'){					//checks the priority for highest priority level
				risk=comp_calc_row_risk(a_board, size, col, row);
				if(risk>biggest_risk){
					holder[0]=col;
					holder[1]=row;
					biggest_risk=risk;
				}
				end_loop=true;
			}
		}
	}

	if(risk_check==false && end_loop==true){
		a_board[holder[1]][holder[0]]='C';		//makes the move
		board[holder[1]][holder[0]]=comp_sym;
		c_move[0]=holder[0];
		c_move[1]=holder[1];
		risk_check=true;
	}
	if(end_loop==false)
	for(row=0; row<size; ++row){
		for(col=0; col<size; ++col){
			if(a_board[row][col]=='H'){					//checks the priority for high priority level
				risk=comp_calc_row_risk(a_board, size, col, row);
				if(risk>biggest_risk){
					holder[0]=col;
					holder[1]=row;
					biggest_risk=risk;
				}
				end_loop=true;
			}
		}
	}

	if(risk_check==false && end_loop==true){	//makes the move
		a_board[holder[1]][holder[0]]='C';
		board[holder[1]][holder[0]]=comp_sym;
		c_move[0]=holder[0];
		c_move[1]=holder[1];
		risk_check=true;
	}
	if(end_loop==false)
	for(row=0; row<size; ++row){
		for(col=0; col<size; ++col){
			if(a_board[row][col]=='L'){			//checks the priority for low priority level
				risk=comp_calc_row_risk(a_board, size, col, row);
				if(risk>biggest_risk){
					holder[0]=col;
					holder[1]=row;
					biggest_risk=risk;
				}
				end_loop=true;
			}
		}
	}

	if(risk_check==false && end_loop==true){	//makes the move
		a_board[holder[1]][holder[0]]='C';
		board[holder[1]][holder[0]]=comp_sym;
		c_move[0]=holder[0];
		c_move[1]=holder[1];
		risk_check=true;
	}
}

void comp_save_enemy_move(char a_board[12][12], const int size, const int p_move[2]){	//saves the enemy move to the ai board
	int incrVal[2], row, col;

	row=p_move[1];
	col=p_move[0];
	a_board[row][col]='E';
	for(int i=0; i<6; i++){
		game_incr_values(i, incrVal);
		if((a_board[row+incrVal[1]][col+incrVal[0]]!='E' && a_board[row+incrVal[1]][col+incrVal[0]]!='C') && (row+incrVal[1]<size && row+incrVal[1]>=0) && (col+incrVal[0]<size && col+incrVal[0]>=0))
			a_board[row+incrVal[1]][col+incrVal[0]]='R';	//it writes 'R' to the neighbors of new move, 'R' means that place has risk
	}
}

void comp_aboard_setup(char a_board[12][12], const int size){	//filling an empty ai board for start
	for(int i=0; i<size; i++)
		for(int j=0; j<size; j++)
			a_board[i][j]='.';
}


void comp_no_logical_move(char a_board[12][12], const int size){	//fills the ai board with logically rational priorities
	for(int i=0; i<size; ++i){
		for(int j=0; j<size; ++j){
			if(a_board[i][j]!='C' && a_board[i][j]!='E'){
				if((comp_check_row(a_board, size, j, i)<=1 && comp_check_row(a_board, size, j, i)>=0) || comp_check_risk(a_board, size, j, i)>=4)
					a_board[i][j]='K';
				else if(a_board[i][j]=='R' || comp_check_risk(a_board, size, j, i)>=1)
					a_board[i][j]='H';
				else
					a_board[i][j]='L';
			}
		}
	}
}

int comp_horizontal_side_risk_check(const char a_board[12][12], const int size) {
	int returnVal=1, left_risk=0, right_risk=0;

	for (int row = 0; row < size; ++row) {
		for (int col = 0; col < size; ++col) {
			if (a_board[row][col] == 'E') {
				if (col <= (size / 2))
					++left_risk;
				else
					++right_risk;
			}
		}
	}

	if (left_risk >= right_risk)
		returnVal = 1;
	else
		returnVal = 0;

	return returnVal;
}

int comp_vertical_side_risk_check(const char a_board[12][12], const int size) {
	int returnVal = 1, up_risk = 0, dn_risk = 0;

	for (int row = 0; row < size; ++row) {
		for (int col = 0; col < size; ++col) {
			if (a_board[row][col] == 'E') {
				if (row <= (size / 2))
					++up_risk;
				else
					++dn_risk;
			}
		}
	}

	if (up_risk >= dn_risk)
		returnVal = 1;
	else
		returnVal = 0;

	return returnVal;
}

void comp_find_path(char a_board[12][12], const int size, const int all_c_moves[][2], const int c_move_pointer) {
	int path_length = 0, length_up = 0, length_dn = 0, side_h, side_v, short_path=size*size, path_pointer=-1;
	bool updateValup=false, updateValdn=false;

	side_h = comp_horizontal_side_risk_check(a_board, size);
	side_v = comp_vertical_side_risk_check(a_board, size);
	comp_reset_path(a_board, size);

	for (int i = 0; i < c_move_pointer; ++i) {   //checks the path for upright and downright directions

		if (comp_check_neighbor(a_board, size, all_c_moves[i][0], all_c_moves[i][1]) <= 1) {
			if (side_h == 0) {
				if (side_v == 0) {
					updateValup = comp_update_pathfinding_upright(a_board, size, all_c_moves[i][0], all_c_moves[i][1], length_up);
					updateValdn = comp_update_pathfinding_dnleft(a_board, size, all_c_moves[i][0], all_c_moves[i][1], length_dn);
				}
				else {
					updateValup = comp_update_pathfinding_upleft(a_board, size, all_c_moves[i][0], all_c_moves[i][1], length_up);
					updateValdn = comp_update_pathfinding_dnright(a_board, size, all_c_moves[i][0], all_c_moves[i][1], length_dn);
				}
			}

			else {
				if (side_v == 0) {
					updateValup = comp_update_pathfinding_upleft(a_board, size, all_c_moves[i][0], all_c_moves[i][1], length_up);
					updateValdn = comp_update_pathfinding_dnright(a_board, size, all_c_moves[i][0], all_c_moves[i][1], length_dn);
				}
				else {
					updateValup = comp_update_pathfinding_upright(a_board, size, all_c_moves[i][0], all_c_moves[i][1], length_up);
					updateValdn = comp_update_pathfinding_dnleft(a_board, size, all_c_moves[i][0], all_c_moves[i][1], length_dn);
				}
			}

			if (updateValup == true && updateValdn == true) {  //if both of them reached to the end points program gets into this if
				if (short_path > (length_up + length_dn)) {	//if it is the shortest path that have been found it saves the location
					path_pointer = i;
					short_path = length_up + length_dn;	//updating the shortest distance that have been found
				}
			}
			comp_reset_path(a_board, size);
			length_up = 0;
			length_dn = 0;
		}
	}



	if (path_pointer == -1)
		comp_no_logical_move(a_board, size);
	else {
		if (side_h == 0) {
			if (side_v == 0) {
				updateValup = comp_update_pathfinding_upright(a_board, size, all_c_moves[path_pointer][0], all_c_moves[path_pointer][1], length_up);
				updateValdn = comp_update_pathfinding_dnleft(a_board, size, all_c_moves[path_pointer][0], all_c_moves[path_pointer][1], length_dn);
			}
			else {
				updateValup = comp_update_pathfinding_upleft(a_board, size, all_c_moves[path_pointer][0], all_c_moves[path_pointer][1], length_up);
				updateValdn = comp_update_pathfinding_dnright(a_board, size, all_c_moves[path_pointer][0], all_c_moves[path_pointer][1], length_dn);
			}
		}

		else {
			if (side_v == 0) {
				updateValup = comp_update_pathfinding_upleft(a_board, size, all_c_moves[path_pointer][0], all_c_moves[path_pointer][1], length_up);
				updateValdn = comp_update_pathfinding_dnright(a_board, size, all_c_moves[path_pointer][0], all_c_moves[path_pointer][1], length_dn);
			}
			else {
				updateValup = comp_update_pathfinding_upright(a_board, size, all_c_moves[path_pointer][0], all_c_moves[path_pointer][1], length_up);
				updateValdn = comp_update_pathfinding_dnleft(a_board, size, all_c_moves[path_pointer][0], all_c_moves[path_pointer][1], length_dn);
			}
		}
	}
}

void comp_first_move(char a_board[12][12], const int size, const int prowMove = -1) { //calculating the first move of the computer
	int horizon, moveRow, moveCol;
	auto actualSize = size - 1;

	horizon = comp_horizontal_side_risk_check(a_board, size);

	moveCol = (horizon == 0) ? (actualSize - (3 * actualSize / 4)) : (actualSize - (actualSize / 4));	//making a risk calculation, and decides where to move in column

	moveRow = (prowMove == -1) ? (actualSize / 2) : (prowMove);	//if first turn in the hands of the computer making its row move to the middle

	a_board[moveRow][moveCol] = 'K';
}

void comp_load_settings(const char board[12][12], const int size, char a_board[12][12], int all_c_moves[144][2], bool& p_wcond_left, bool& p_wcond_right, bool& c_wcond_up, bool& c_wcond_dn) {
	int pMove[2], cmovePointer=0;

	for (int i = 0; i < size; ++i) {
		for (int j = 0; j < size; ++j) {
			if (board[i][j] == pl2) {
				a_board[i][j] = 'C';
				all_c_moves[cmovePointer][0] = j;
				all_c_moves[cmovePointer++][1] = i;
				if (i == 0)
					c_wcond_up = true;
				else if (i == size - 1)
					c_wcond_dn = true;
			}
			else if (board[i][j] == pl1) {
				pMove[0] = j;
				pMove[1] = i;
				comp_save_enemy_move(a_board, size, pMove);
				if (j == 0)
					p_wcond_left = true;
				else if (j == size - 1)
					p_wcond_right = true;
			}
		}
	}
}

char comp_gameplay_pve(char board[12][12], const int size, const char player, const char comp_sym, const char option, bool turnFirst, bool load, int c_move_pointer) {
	char a_board[12][12], returnVal;
	int p_move[2], c_move[2], move_count=0, all_c_moves[144][2], path_pointer[2];
	int short_path=144, length_up=0, length_dn=0;
	bool comp_win=false, p_win=false, draw=false, start=false, updateValup=false, updateValdn=false, getLine=false;
	bool p1_wcond_left=false, p1_wcond_right=false, c_wcond_bottom=false, c_wcond_top=false;

	gamemode mod = pve;

	path_pointer[0]=-1;
	path_pointer[1]=0;
	comp_aboard_setup(a_board, size);
	if (load == true)
		comp_load_settings(board, size, a_board, all_c_moves, p1_wcond_left, p1_wcond_right, c_wcond_top, c_wcond_bottom);
	

	gboard_print(board, size);
	while(comp_win==false && p_win==false && draw==false){
		if (turnFirst) {
			do {
				returnVal = game_movement(board, size, player, p_move, mod, move_count);
			} while (returnVal == '-');
			if (returnVal == '.') {
				comp_reset_path(a_board, size);
				move_count += 1;

				gboard_print(board, size);

				if (p_move[0] == 0)
					p1_wcond_left = true;
				if (p_move[0] == size - 1)
					p1_wcond_right = true;

				if (p1_wcond_left == true && p1_wcond_right == true)	//checks if there is a win or not
					for (int i = 0; i < size && p_win == false; ++i)
						p_win = game_wcond_check(board, size, player, 0, i, 'r');

				if (move_count == (size * size))	//checks if there is a draw or not
					draw = true;
			}
		}
		else
			turnFirst = true;

		if (returnVal == pl1)
			p_win = true;
		else if (returnVal == pl2)
			comp_win = true;

		if(p_win==false && draw==false && comp_win==false){
			comp_save_enemy_move(a_board, size, p_move);	//saves the enemy move to the ai board

			if(start==false && load==false){			//it checks whether it is the first tour or not
				comp_first_move(a_board, size, p_move[1]);
				start=true;
			}

			else
				comp_find_path(a_board, size, all_c_moves, c_move_pointer); //creating a short path

			comp_put_move(board, a_board, size, comp_sym, c_move);  //computer choose the location of new point
			all_c_moves[c_move_pointer][0]=c_move[0];		//registering the locations of the computer's play
			all_c_moves[c_move_pointer++][1]=c_move[1];
			move_count+=1;

			if(c_move[1]==0)		//checks the location of new point for winning condition
				c_wcond_top=true;
			if(c_move[1]==size-1)
				c_wcond_bottom=true;
			

			if(option=='y')			//prints the ai board if user wants it
				gboard_print(a_board, size);

			gboard_print(board, size);

			if(c_wcond_top==true && c_wcond_bottom==true)
				for(int i=0; i<size && comp_win==false; ++i)
					comp_win=game_wcond_check(board, size, comp_sym, i, 0, 'b'); //checks the winning condition from top to bottom
		}
		if(move_count==(size*size))
			draw=true;
	}

	if (p_win == true) {
		returnVal = player;
	}

	else if (comp_win == true) {
		returnVal = comp_sym;
	}

	else
		returnVal='\0';

	return returnVal;
}
