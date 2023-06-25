
#include "HexCell.h"

using namespace std;

namespace OzturkHexGame{
Cell::Cell(int col, int row, CellState value) : pos_col(col), pos_row(row), cell_value(value)
{
	testCell();
}

bool Cell::setter(const int col, const int row)
{
	return setter(col, row, emp);
}

bool Cell::setter(const CellState value)
{
	return setter(pos_col, pos_row, value);
}

bool Cell::setter(const int col, const int row, const CellState value)
{
	pos_col = col;
	pos_row = row;
	cell_value = value;

	return testCell();
}

int Cell::takeCol() const
{
	return pos_col;
}
int Cell::takeRow() const
{
	return pos_row;
}
CellState Cell::takeValue() const
{
	return cell_value;
}

bool Cell::testCell() const
{
	bool returnVal;

	if (pos_col < 0 || pos_row < 0 || cell_value < emp || cell_value > closed)
	{
		returnVal = false;
		cerr << "Non-valid Cell Value!!!\n";
		//exit(1);
	}

	else
		returnVal = true;

	return returnVal;
}
}