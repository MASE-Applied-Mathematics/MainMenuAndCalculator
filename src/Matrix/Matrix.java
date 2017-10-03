package Matrix;


public class Matrix {

	public static void main(String[] args) {
		int[][] matrix = { {1,2,3}, {4,5,6},{7,8,9} };
		for(int r=0; r<matrix.length; r++){
			for(int c=0; c<matrix[r].length; c++){
				System.out.print(matrix[r][c]+ " ");
				//System.out.println("__\n");
			}
			System.out.print("..\n");
		}
		//System.out.println("\n");

	}
	
	/*
	public static void multiplyMat(Matrix matrixA, Matrix matrixB){
		final int N =3;
		int[][] result = new int[N][N];
		int sum=0;
		for(int r=0; r<N; r++){
			for(int c=0; c<N; c++){
				sum=0;
				for(int k=0; k<N; k++){
					sum += matrixA[r][k]*matrixB[k][c];
				}
			result[r][c] = sum;
			}
			
		}
		
	}*/

}
