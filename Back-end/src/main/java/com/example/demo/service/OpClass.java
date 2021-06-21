package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class OpClass {
    long start;
    long finish;
    double[] solution;
    /**
     * Rounding method
     * @param value ,value to be rounded
     * @param places the precision
     * @return the rounded number
     */
    private double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    /**
     * This method checks if the coefficient matrix has a unique solution
     * @param a the coefficient array
     * @param b	vector b in aX = b
     * @param p	the precision
     * @return true if has a unique solution , false otherwise
     */
    private boolean hasUniqueSolution(double[][] a, double[] b, int p, boolean pivot, double[] sn) {
        NodeStack s = eliminate(a, b, p, pivot, sn);
        if(s.size() == 0) {
            return false;
        }
        a = (double[][])s.pop();
        int rank = 0;
        int n = b.length;
        for(int i = 0; i < n; i++) {
            for(int j = i; j < n; j++) {
                if(a[i][j] != 0d) {
                    rank++;
                    break;
                }
            }
        }if(rank == n) {
            return true;
        }return false;
    }
    /**
     * Gauss elimination without pivoting
     * @param a coefficients array
     * @param b	vector b in aX = b
     * @param p precision
     * @return	solution array X
     */
    public double[] gaussElimination(double[][] a, double[] b, int p) {
        start = System.nanoTime();
        boolean check = hasUniqueSolution(a, b, p, false, null);
        if(check == false) {
            return null;
        }
        solution =  substitute(a, b, p);
        finish = System.nanoTime();
        System.out.println((finish - start) / 1000);
        return solution;
    }
    /**
     * Gauss elimination with pivoting
     * @param a coefficients array
     * @param b	vector b in aX = b
     * @param p precision
     * @return  solution array X
     */
    public double[] gaussEliminationPivot(double[][] a, double[] b, int p) {
        start = System.nanoTime();
        double [] sn = scalingFactors(a, b.length);
        boolean check = hasUniqueSolution(a, b, p, true, sn);
        if(check == true) {
            solution = substitute(a, b, p);
            finish = System.nanoTime();
            System.out.println((finish - start) / 1000);
            return solution;
        }return null;
    }
    /**
     * Gauss-Jordan elimination
     * @param a coefficients array
     * @param b	vector b in aX = b
     * @param p precision
     * @return  solution array X
     */
    public double[] gaussJordan(double[][] a, double[] b, int p) {
        start = System.nanoTime();
        double [] sn = scalingFactors(a, b.length);
        boolean check = hasUniqueSolution(a, b, p, true, sn);
        if(check == false) {
            return null;
        }// now array "a" is in row echelon form
        for(int i = 0; i<b.length; i++) {
            double factor = a[i][i];
            for(int j = i; j<b.length; j++) {
                a[i][j] = round(a[i][j] / factor,p);
            }b[i] = round(b[i] / factor, p);
        }// now array "a" is in reduced row echelon form
        boolean check2 = backwardEliminate(a, b, p);
        if(check2) {
            finish = System.nanoTime();
            System.out.println((finish - start) / 1000);
            return b;
        }return null;
    }
    /**
     * Forward elimination method with pivoting option
     * @param a coefficients array
     * @param b	vector b in aX = b
     * @param p precision
     * @return coefficient array in row echelon form
     */
    private boolean backwardEliminate(double[][] a, double[] b, int p) {
        double factor = 0d;
        for(int k = b.length - 1; k > 0; k--) {
            for(int i = k-1; i >= 0; i--) {
                try {
                    factor = round(a[i][k]/a[k][k], p);
                }catch(NumberFormatException e) {
                    return false;
                }
                b[i] = round(b[i] - round(factor* b[k], p), p);
            }
        }return true;
    }
    /**
     * Forward elimination method with pivoting option
     * @param a coefficients array
     * @param b	vector b in aX = b
     * @param p precision
     * @return coefficient array in row echelon form
     */
    private NodeStack eliminate(double[][] a, double[] b, int p, boolean pivot, double[] sn){
        double factor = 0d;
        for(int k = 0; k < b.length-1; k++) {
            if(pivot == true) {
                pivot(a, b, sn, k);
            }
            for(int i = k+1; i < b.length; i++) {
                try {
                    factor = round(a[i][k]/a[k][k], p);
                }catch(NumberFormatException e) {
                    return new NodeStack();
                }
                for(int j = k+1; j < b.length; j++) {
                    a[i][j] = round(a[i][j] - round(factor * a[k][j], p), p);
                }b[i] = round(b[i] - round(factor* b[k], p), p);
            }
        }NodeStack s = new NodeStack();
        s.push(b); s.push(a);
        return s;
    }
    /**
     * Backward substitution method
     * @param a coefficients array
     * @param b	vector b in aX = b
     * @param p precision
     * @return answer vector x
     */
    private double[] substitute(double[][] a, double[] b, int p) {
        int temp = b.length;
        double sum = 0d;
        double[] x = new double[temp];
        x[temp-1] = round(b[temp-1] / round(a[temp-1][temp-1], p), p);
        for(int i = temp-2; i >= 0; i--) {
            sum = 0;
            for(int j = i+1; j < temp; j++) {
                sum = round(sum + round(a[i][j] * x[j], p), p);
            }x[i] = round(round((b[i] - sum), p) / a[i][i], p);
        }
        return x;
    }

    /**
     * Forward Substitution
     * @param a coefficients array
     * @param b vector b in aX = b
     * @param p precision
     * @return answer vector x
     */
    private double[] forwardSubstitution(double[][] a, double[] b, int p){
      int n =   b.length;
      double sum;
      double[] x = new double[n];
      for(int i=0;i<n;i++){
          sum = 0.0D ;
          for(int j=0;j<i;j++)
              sum = round(sum + round(a[i][j] * x[j],p),p);

          x[i] = round(round(b[i]-sum,p)/a[i][i],p);
      }

     return  x ;
    }
    /**
     * LU decomposition
     * @param a  coefficients array
     * @param b  vector b in aX = b
     * @param p   precision
     * @param type type of method to be used
     * @return answer vector x
     */
    public  double[] lu(double[][] a, double[] b, int p,int type){

        if(type == 0)
          return  downLittle(a, b, p);
        else if(type == 1)
           return  crout(a,b,p);
        else
         return  chelosky(a,b,p);
    }

    /**
     * LU decomposition using doolittle Form.
     * @param a  coefficients array
     * @param b  vector b in aX = b
     * @param p   precision
     * @return answer vector x
     */
    private   double[] downLittle(double[][] a, double[] b, int p){
        start = System.nanoTime();
        int n = b.length;
        double[] x = new double[n] ;
        double[][] l = new double[n][n] ;
        double[][] u = new double[n][n] ;
        // set Values for l,u.
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
               if(i==0)
                   u[i][j]=a[i][j];
                if(j<i)
                   u[i][j] = 0.0D ;
               else if(j>i)
                   l[i][j]=0.0D   ;
               else
                   l[i][j]=1.0D  ;
            }
        }
        // decompose
        for(int k=0;k<n-1;k++){

            for(int i=k+1;i<n;i++){
                if(a[k][k]==0) // divide by zero
                    return  null ;
               l[i][k] = round(a[i][k]/a[k][k],p);
               for(int j=k+1;j<n;j++){
                   a[i][j] = round(a[i][j]-round(a[k][j]*l[i][k],p),p);
                   if(j>=i)
                      u[i][j] = a[i][j] ;
               }
            }
        }
        solution = substitute(u,forwardSubstitution(l,b,p),p) ;
        finish = System.nanoTime();
        System.out.println((finish - start) / 1000);
        return solution;
    }
    /**
     * LU decomposition using crout Form.
     * @param a  coefficients array
     * @param b  vector b in aX = b
     * @param p   precision
     * @return answer vector x
     */
    private  double[] crout(double[][] a, double[] b, int p){
        start = System.nanoTime();
        int n = b.length;
        double[] x = new double[n] ;
        double sum ;
        double[][] l = new double[n][n] ;
        double[][] u = new double[n][n] ;
        for (int i = 0; i < n; i++) {
            u[i][i] = 1.0D; // set elements in the diagonal  to 1
        }

        for (int j = 0; j < n; j++) {
            // set lower triangle values.
            for (int i = j; i < n; i++) {
                sum = 0.0D;
                for (int k = 0; k < j; k++) {
                    sum = round(sum + round( l[i][k] * u[k][j],p),p);
                }
                l[i][j] = round(a[i][j] - sum,p);
            }

            // set upper triangle values.
            for (int i = j; i < n; i++) {
                sum = 0;
                for(int k = 0; k < j; k++) {
                    sum = round(sum + round( l[j][k] * u[k][i],p),p);
                }
                if (l[j][j] == 0) {
                   return  null ; // divide by zero
                }
                u[j][i] = round(round( (a[j][i] - sum),p) / l[j][j],p);
            }
        }


     solution =  substitute(u,forwardSubstitution(l,b,p),p) ;
     finish = System.nanoTime();
     System.out.println((finish - start) / 1000);
     return solution;
    }
    /**
     * LU decomposition using chelosky Form.
     * @param a  coefficients array.(should be symmetric)
     * @param b  vector b in aX = b
     * @param p   precision
     * @return answer vector x
     */
    private  double[] chelosky(double[][] a, double[] b, int p){
        start = System.nanoTime();
        int n = b.length;
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++){
                if(a[i][j]!=a[j][i])
                    return null ;

            }
        double[] x = new double[n] ;
        double sum ;
        double[][] l = new double[n][n] ;
        double[][] u = new double[n][n] ; // transpose of l.
        for(int i=0;i<n;i++){
            for (int j=0;j<=i;j++){
                sum = 0.0D ;
                if(i==j) {// diagonal elements
                    for(int k=0;k<j;k++)
                        sum = round(sum + round(l[i][k]*l[i][k],p),p);
                    l[i][j] = round(Math.sqrt(round(a[i][j]-sum,p)),p);
                    u[j][i] = l[i][j];
                }
                else { // non diagonal
                    for(int k=0;k<j;k++)
                        sum = round(sum + round(l[i][k] * l[j][k],p),p);
                    if(l[j][j]==0) {// divide by zero
                        System.out.println(l[j][j]);
                        return  null ;
                    }
                    l[i][j] = round(round(a[i][j]-sum,p)/l[j][j],p);
                    u[j][i] = l[i][j];
                }
            }
        }
        solution =  substitute(u,forwardSubstitution(l,b,p),p) ;
        finish = System.nanoTime();
        System.out.println((finish - start) / 1000);
        return solution;
    }
    /**
     * Pivoting method
     * @param a coefficients array
     * @param b	vector b in aX = b
     * @param s Scaling factors of each row
     * @param k	the current row for the forward elimination
     */

    private void pivot(double[][] a, double[] b, double[] s, int k) {
        int pivot = k;
        double big = Math.abs(a[k][k] / s[k]);
        double temp = 0d;
        for(int i = k+1; i < b.length; i++) {
            temp = Math.abs(a[i][k] / s[i]);
            if(temp > big) {
                big = temp;
                pivot = i;
            }
        }if(pivot != k) {
            for(int j = k; j < b.length; j++) {
                temp = a[pivot][j];
                a[pivot][j] = a[k][j];
                a[k][j] = temp;
            }
            temp = b[pivot];
            b[pivot] = b[k];
            b[k] = temp;

            temp = s[pivot];
            s[pivot] = s[k];
            s[k] = temp;
        }
    }//869.5   793.75   873.6
    /**
     * This method forms the scaling factors array
     * @param a coefficients array
     * @param n size of b matrix (number of rows of a) in aX = b
     * @return the scaling factors matrix sn
     */

    private double[] scalingFactors(double[][] a, int n) {
        double [] sn = new double[n];
        for(int i = 0; i < n; i++) {
            sn[i] = Math.abs(a[i][0]);
            for(int j = 1; j < n; j++) {
                if(Math.abs(a[i][j]) > sn[i] ) {
                    sn[i] = Math.abs(a[i][j]);
                }
            }
        }
        return sn;
    }

    /**
	 * Gauss_Seidil method
	 * @param a coefficients array
	 * @param b	vector b in aX = b
	 * @param initial initial guess
	 * @param noOfIterations no of iterations limit
	 * @param requiredRelativeError absolute relative error required
	 * @return all the results of all the iterations x
	 * @param p precision
	 */
	public ArrayList<ArrayList<Double>> gaussSeidil (double[][] a, double[] b, double[] initial, int noOfIterations,double requiredRelativeError ,int p)
	{   start = System.nanoTime();
        if(checkZeros(a)) // can't divide by zero
            return  null ;
        int m ,k = 1 , n = a.length;
		ArrayList<ArrayList<Double>> x = new ArrayList<ArrayList<Double>>();		// the results array
		double[] relErrors = new double[n];
		for (int i = 0 ; i < n ; i++) {												// put the initial guess in x
		    ArrayList<Double> var = new ArrayList<Double>();
		    var.add(initial[i]);
		    x.add(var);
		}		
		
		do {		
			for (int i = 0 ; i < n ; i++) {	
				Double element = round(b[i]/a[i][i] , p) ;
				for (int j = 0 ; j < n ; j++) {
					if (i != j) {
						m = k;
						if ( i < j )
							m--;
						element = round(element -round( round(a[i][j]*x.get(j).get(m) ,p) /a[i][i] ,p) ,p);
					}
				}
				x.get(i).add(element);
			}
			relErrors = relativeErrors(x,k);
			k++;
		} while ( !relativeErrorTest(relErrors ,requiredRelativeError) && k <= noOfIterations);
        finish = System.nanoTime();
        System.out.println((finish - start) / 1000);
		return x;
	}
	
	/**
	 * Jacobi method
	 * @param a coefficients array
	 * @param b	vector b in aX = b
	 * @param initial initial guess
	 * @param noOfIterations no of iterations limit
	 * @param requiredRelativeError absolute relative error required
	 * @return all the results of all the iterations x
	 * @param p precision
	 */
	public ArrayList<ArrayList<Double>> jacobi (double[][] a, double[] b, double[] initial, int noOfIterations,double requiredRelativeError,int p)
	{   start = System.nanoTime();
	    if(checkZeros(a)) // can't divide by zero
	        return  null ;
		int k = 1 , n = a.length;
		ArrayList<ArrayList<Double>> x = new ArrayList<ArrayList<Double>>();		// the results array
		double[] relErrors = new double[n];
		for (int i = 0 ; i < n ; i++) {												// put the initial guess in x
		    ArrayList<Double> var = new ArrayList<Double>();
		    var.add(initial[i]);
		    x.add(var);
		}	
		
		do {		
			for (int i = 0 ; i < n ; i++) {	
				Double element = round(b[i]/a[i][i] , p) ;
				for (int j = 0 ; j < n ; j++) {
					if (i != j) {
						element = round(element -round( round(a[i][j]*x.get(j).get(k-1) ,p) /a[i][i] ,p) ,p);
					}
				}
				x.get(i).add(element);
			}
			relErrors = relativeErrors(x,k);
			k++;
		} while ( !relativeErrorTest(relErrors ,requiredRelativeError) && k <= noOfIterations );
        finish = System.nanoTime();
        System.out.println((finish - start) / 1000);
		return x;
	}
	//533.16  271
	/**
	 * 
	 * @param x all the results of all the iterations x
	 * @param k	the column that i want to calculate the relative errors
	 * @return array contains all the absolute relative errors for last step
	 */
	public double[] relativeErrors (ArrayList<ArrayList<Double>> x,int k) 
	{
		double[] relErrors = new double[x.size()];
		for ( int i =0 ; i < x.size() ; i++) {
			relErrors[i] = ( (x.get(i).get(k)- x.get(i).get(k-1)) / x.get(i).get(k) ) * 100;
		}
		return relErrors;
	}
	
	/**
	 * 
	 * @param relErrors array contains all the absolute relative errors for last step
	 * @param requiredRelativeError the required relative error
	 * @return boolean
	 */
	private boolean relativeErrorTest (double[] relErrors ,double requiredRelativeError)
	{
		for ( int i = 0 ; i < relErrors.length ; i++) {
			if (relErrors[i] >= requiredRelativeError)
				return false;
		}
		return true;
	}
	
	
	
	public double[] lastSolution (ArrayList<ArrayList<Double>> x) {
		double[] res = new double[x.size()];
		int k = x.get(0).size() - 1;
		for (int i = 0 ; i < x.size() ; i++) {
			res[i] = x.get(i).get(k);
		}
		return res;
	}
	private boolean checkZeros(double [][] a){
	    int size = a.length ;
	    for(int i=0;i<size;i++)
	        for(int j=0;j<size;j++){
	            if(i==j && a[i][j]==0)
	                return  true ;
            }
	    return false ;
    }


}

