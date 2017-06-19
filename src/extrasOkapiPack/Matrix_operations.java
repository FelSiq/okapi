
public class Matrix_operations {

	
	public void MatrixAdd(int num,MatrixShort m){
		for(int i = 0;i < m.GetRow();i++){
			for(int j = 0;j < m.GetCol();j++){
				m.FillMatrix(i, j,m.GetPoint(i, j) + num);
			}
		}
	}
	
	public void MatrixAdd(MatrixShort ma,MatrixShort mb){
		for(int i=0;i<ma.GetRow();i++){
			for(int j=0;j<ma.GetCol();j++){
				ma.FillMatrix(i, j, ma.GetPoint(i, j)+ mb.GetPoint(i, j));
			}
		}
	}
	
	public void MatrixMultiplication(MatrixShort m,int num){
		for(int i=0;i<m.GetRow();i++){
			for(int j=0;j<m.GetCol();j++){
				m.FillMatrix(i, j, m.GetPoint(i, j)*num);
			}
		}
	}
	
	public MatrixShort MatrixMultiplication(MatrixShort ma,MatrixShort mb){
		MatrixShort mc = new MatrixShort(ma.GetRow(),mb.GetCol());
		
		for(int i=0;i<ma.GetRow();i++){
			for(int j=0;j<mb.GetCol();j++){
				for(int k=0;k<mb.GetRow();k++){
					mc.FillMatrix(i, j, mc.GetPoint(i, j) + (ma.GetPoint(i, k)*mb.GetPoint(k, j)));
				}
			}
		}
			
		return mc;
	}
}
