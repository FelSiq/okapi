
public class MatrixShort {
	private int col;
	private int row;
	private int matrix[][];
	
	public int GetPoint(int i,int j){
		return matrix[i][j];
	}
	
	public void FillMatrix(int i,int j,int num){
		matrix[i][j] = num;
	}
	
	public int GetRow(){
		return row;
	}
	
	public int GetCol(){
		return col;
	}
	
	public MatrixShort(int row,int col){
		this.col = col;
		this.row = row;
		this.matrix = new int[row][col];
	}
}
