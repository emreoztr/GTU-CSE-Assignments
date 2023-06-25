While playing in game you can write:
RESET : to reset game
LMOVE : to look last move
MOVECOUNT : to take movecount from table
LOAD test.txt : to load game from a file (write file name instead of test.txt)
SAVE test.txt : to save game to a file (write file name instead of test.txt)
LOOK A5 : to look a position in board (write location instead of A5)
RESIZE 12 : to resize and reset board (write board size you wanted after "RESIZE", ex: RESIZE 8
MENU : to return menu

You can test exception of:
operator() by writing invalid position to "LOOK" (for example for board size 6, LOOK A7)
lastMove by writing LMOVE in an empty board
