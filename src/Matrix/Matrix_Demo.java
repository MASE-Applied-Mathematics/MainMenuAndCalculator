package Matrix;

import java.util.Scanner;
import java.util.Random;
public class Matrix_Demo
{
    static int[][] matrixA;
    static int[][] matrixB;
    static int N;
    static void Popilate()
    {
        Random randomGenerator = new Random();
        for(int r=0; r<matrixA.length; r++)
        {
           for(int c=0; c<matrixA[0].length; c++)
           {
               matrixA[r][c] = randomGenerator.nextInt(100);
               matrixB[r][c] = randomGenerator.nextInt(100);
           }
        }
    }
    static void Print(int[][] dataM, String name)
    {	
        System.out.println(name+" = ");
        for(int r=0; r<N; r++)
        {
           for(int c=0; c<N; c++)
           {
               System.out.print(dataM[r][c]+" ");
           }
           System.out.println(" ");
        }
    }
    static void CalculateValues()
    {
        int minA=101, maxA=-1, minB=101, maxB=-1, sumA=0, sumB=0;
        double avgA = 0, avgB=0;
        for(int r=0; r<matrixA.length; r++)
        {
           for(int c=0; c<matrixA[0].length; c++)
           {
                if(matrixA[r][c]<minA)
                    minA = matrixA[r][c];
                if(matrixA[r][c]>maxA)
                    maxA = matrixA[r][c];
                if(matrixB[r][c]<minB)
                    minB = matrixB[r][c];
                if(matrixB[r][c]>maxB)
                    maxB = matrixB[r][c];
                sumA+=matrixA[r][c]; sumB+=matrixB[r][c];
           }
        }
        System.out.println("\nResults for Matrix A:\nSum: "+sumA+"\nMin Value: "+minA+"\nMax value; "+maxA+"\nAverage:"+(sumA/(N*N)));
        System.out.println("\nResults for Matrix B:\nSum: "+sumB+"\nMin Value: "+minB+"\nMax value; "+maxB+"\nAverage:"+(sumB/(N*N)));
    }
    static void AddSubMatrices(int opt)
    {

        int[][] result = new int[N][N];
        if(opt==1)
            System.out.println("\nAdding Matrices - ");
        else
            System.out.println("\nSubtracting Matrices - ");
        for(int r=0; r<matrixA.length; r++)
        {
           for(int c=0; c<matrixA[0].length; c++)
           {
                if(opt==1)
                    result[r][c] = matrixA[r][c]+matrixB[r][c];
                else
                    result[r][c] = matrixA[r][c]-matrixB[r][c];
                System.out.print(result[r][c]+" ");
           }
           System.out.println(" ");
        }
    }
    static void MultiplyMat()
    {
        int[][] result = new int[N][N];
        int sum = 0;
        for(int r=0; r<N; r++)
        {
            for(int c=0; c<N; c++)
            {
                sum=0;
                for(int k=0; k<N; k++)
                {
                    sum+=matrixA[r][k]*matrixB[k][c];
                }    
                result[r][c]=sum;
            }
        }
        Print(result, "Matrix A x Matrix B");
    }
    public static void main(String[]args)
    {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the size of N: ");
        N = in.nextInt();
        matrixA = new int[N][N];
        matrixB = new int[N][N];
        Popilate();
        Print(matrixA, "Matrix A");
        Print(matrixB, "Matrix B");
        CalculateValues();
        AddSubMatrices(1);
        System.out.println("\n");
        AddSubMatrices(0);
        MultiplyMat();
    }
}