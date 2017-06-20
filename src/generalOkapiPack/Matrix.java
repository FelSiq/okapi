package generalOkapiPack;
// ------------------------------------------
import java.io.File;

/**
* Class responsible of performing basic matrix operation. 
*/
public abstract class Matrix /*extends GeneralOkapi*/ {
	/**
	* Add a constant real value to all matrix elements.
	*/
	static public OkapiTable<Double> addc(OkapiTable<Double> m, int num) {
		OkapiTable<Double> mr = TableManager.create(m.getRowNum(), m.getColNum());
		for(int i = 0;i < m.getRowNum(); i++){
			for(int j = 0;j < m.getColNum(); j++){
				mr.setElement(i, j, m.getElement(i, j) + num);
			}
		}
		return mr;
	}

	/**
	* Multiply a matrix to a constant.
	*/
	static public OkapiTable<Double> multc(OkapiTable<Double> m, int num){
		OkapiTable<Double> mr = TableManager.create(m.getRowNum(), m.getColNum());
		for(int i=0;i < m.getRowNum();i++){
			for(int j=0; j < m.getColNum();j++){
				mr.setElement(i, j, m.getElement(i, j)*num);
			}
		}
		return mr;
	}
	
	/**
	* Add two matrices mr = (ma + mb).
	*/
	static public OkapiTable<Double> add(OkapiTable<Double> ma, OkapiTable<Double> mb) {
		OkapiTable<Double> mr = null;

		if (ma.getRowNum() == mb.getRowNum() && ma.getColNum() == mb.getColNum()) {
			mr = TableManager.create(ma.getRowNum(), ma.getColNum());
			for(int i = 0;i < ma.getRowNum(); i++){
				for(int j = 0;j < ma.getColNum(); j++){
					mr.setElement(i, j, ma.getElement(i, j) + mb.getElement(i, j));
				}
			}
		} else {
			System.out.println("E: To add matrices, they need to have the same dimensions.");
		}
		return mr;
	}

	/**
	* Substract two matrices mr = (ma - mb).
	*/
	static public OkapiTable<Double> sub(OkapiTable<Double> ma, OkapiTable<Double> mb) {
		OkapiTable<Double> mr = null;
		if (ma.getRowNum() == mb.getRowNum() && ma.getColNum() == mb.getColNum()) {
			mr = TableManager.create(ma.getRowNum(), ma.getColNum());
			for(int i=0;i<ma.getRowNum();i++){
				for(int j=0;j<ma.getColNum();j++){
					mr.setElement(i, j, ma.getElement(i, j) - mb.getElement(i, j));
				}
			}
		} else {
			System.out.println("E: To subtract matrices, they need to have the same dimensions.");
		}
		return mr;
	}
	
	/**
	* Multiply two matrices mr = (ma * mb).
	*/
	static public OkapiTable<Double> mult(OkapiTable<Double> ma, OkapiTable<Double> mb){
		OkapiTable<Double> mr = null;
		if (ma.getColNum() == mb.getRowNum()) {
			mr = TableManager.create(ma.getRowNum(), mb.getColNum());
			for(int i = 0; i < ma.getRowNum(); i++){
				for(int j = 0; j < mb.getColNum(); j++){
					for(int k = 0; k < mb.getRowNum(); k++){
						mr.setElement(i, j, mr.getElement(i, j) + (ma.getElement(i, k) * mb.getElement(k, j)));
					}
				}
			}
		} else {
			System.out.println("E: To multiply matrices, Ncol(A) = Nrow(B).");
		}
			
		return mr;
	}
}