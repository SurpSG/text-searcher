public class ThreadMat implements Runnable {
    int[][] matA;
    int[][] matB;
    Thread t;
    int[][] res;

    public ThreadMat() {
        t = new Thread(this);
    }

    public void add(int[][] mat1, int[][] mat2, int[][] resultat)
            throws ArrayIndexOutOfBoundsException {
        matA = mat1;
        matB = mat2;
        res = resultat;
        t.start();
    }

    @Override
    public void run() throws ArrayIndexOutOfBoundsException {
        int m = matA.length;
        int n = matB[0].length;
        int o = matB.length;
        res = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < o; k++) {
                    res[i][j] += matA[i][k] * matB[k][j];
                }
            }
        }
    }

}