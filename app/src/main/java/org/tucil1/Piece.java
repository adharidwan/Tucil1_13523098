package org.tucil1;

// import java.util.List;

public class Piece {
    int id;
    int n,m;
    boolean[][] shape;
    static int counter = 0;
    
    public Piece(int n, int m, boolean[][] shape){
        this.n = n;
        this.m = m;
        this.shape = new boolean[n+2][m+2];
        this.id = counter++;

        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= m; j++){
                this.shape[i][j] = shape[i-1][j-1];
            }
        }
    }

    public Piece(int n, int m, boolean[][] shape, int id){
        this.n = n;
        this.m = m;
        this.shape = new boolean[n+2][m+2];
        this.id = id;

        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= m; j++){
                this.shape[i][j] = shape[i-1][j-1];
            }
        }
    }

    public void printPiece(){
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= m; j++){
                if(shape[i][j]){
                    System.out.print((char)('A' + id));
                }else{
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    public Piece rotate() {
        boolean[][] newShape = new boolean[this.m][this.n];
        for (int i = 1; i <= this.m; i++) {
          for (int j = 1; j <= this.n; j++) {
            newShape[i-1][j-1] = this.shape[j][this.m-i+1];
          }
        }
        return new Piece(this.m, this.n, newShape, this.id);
      }
    
    public Piece flip() {
        boolean[][] newShape = new boolean[this.n][this.m];
        for (int i = 1; i <= this.n; i++) {
          for (int j = 1; j <= this.m; j++) {
            newShape[i-1][j-1] = this.shape[i][this.m-j+1];
          }
        }
        return new Piece(this.n, this.m, newShape, this.id);
      }

    public boolean isValidPosition(int x, int y){
        return x >= 1 && x <= this.n && y >= 1 && y <= this.m;
    }


}