package org.tucil1;

import java.util.Arrays;

public class Board {
    char[][] grid;
    int n;
    int m;
    Piece[] pieces;
    int pieceCount;
    int iterationCount = 0;
    boolean foundSolution = false;
    int emptyCellCount;

    
    public static final String RESET = "\u001B[0m";
    private static final String[] ANSI_COLORS = {
        "", //empty index-0
        "\u001B[38;2;255;0;0m",   
        "\u001B[38;2;255;128;0m",  
        "\u001B[38;2;255;255;0m",  
        "\u001B[38;2;128;255;0m", 
        "\u001B[38;2;0;255;0m",    
        "\u001B[38;2;0;255;128m", 
        "\u001B[38;2;0;255;255m",  
        "\u001B[38;2;0;128;255m", 
        "\u001B[38;2;0;0;255m",    
        "\u001B[38;2;128;0;255m",  
        "\u001B[38;2;255;0;255m",   
        "\u001B[38;2;255;0;128m",   
        "\u001B[38;2;255;128;128m",  
        "\u001B[38;2;255;191;0m",    
        "\u001B[38;2;191;255;0m",    
        "\u001B[38;2;0;255;191m",    
        "\u001B[38;2;0;191;255m",    
        "\u001B[38;2;128;128;255m",  
        "\u001B[38;2;255;128;255m", 
        "\u001B[38;2;255;69;0m",     
        "\u001B[38;2;0;128;128m",    
        "\u001B[38;2;128;0;128m",     
        "\u001B[38;2;128;128;0m",    
        "\u001B[38;2;255;215;0m",    
        "\u001B[38;2;169;169;169m", 
        "\u001B[38;2;0;191;191m"
    };

    public static String getColor(int id) {
        if (id < 1 || id > 26) {
            return RESET;
        }
        return ANSI_COLORS[id];
    }

    public Board(int n, int m, Piece[] pieces, int pieceCount) {
        this.n = n;
        this.m = m;
        this.pieces = pieces;
        this.pieceCount = pieceCount;
        this.emptyCellCount = m * n;
        this.grid = new char[n + 2][m + 2];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 1; i <= n; i++) {
            Arrays.fill(grid[i], 1, m + 1, '.');
        }
    }

    public void printBoard() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                System.out.print(getColor(grid[i][j] - 'A' + 1) + grid[i][j] + RESET);
            }
            System.out.println();
        }
    }

    private boolean placePiece(int x, int y, Piece piece) {
        if (x + piece.n - 1 > n || y + piece.m - 1 > m) return false;

        for (int i = 1; i <= piece.n; i++) {
            for (int j = 1; j <= piece.m; j++) {
                if (piece.shape[i][j] && grid[x + i - 1][y + j - 1] != '.') {
                    return false;
                }
            }
        }

        for (int i = 1; i <= piece.n; i++) {
            for (int j = 1; j <= piece.m; j++) {
                if (piece.shape[i][j]) {
                    grid[x + i - 1][y + j - 1] = (char) ('A' + piece.id);
                    emptyCellCount--;
                }
            }
        }
        return true;
    }

    private void removePiece(int x, int y, Piece piece) {
        for (int i = 1; i <= piece.n; i++) {
            for (int j = 1; j <= piece.m; j++) {
                if (piece.shape[i][j]) {
                    grid[x + i - 1][y + j - 1] = '.';
                    emptyCellCount++;
                }
            }
        }
    }

    public void findCombination(int x, int y, boolean[] usedPieces) {
        if (foundSolution) return;
        
        iterationCount++;
        
        if (emptyCellCount == 0) {
            foundSolution = true;
            return;
        }
    
        int nextX = x, nextY = y;
        boolean found = false;
        
        for (int i = 1; i <= n && !found; i++) {
            for (int j = 1; j <= m && !found; j++) {
                if (grid[i][j] == '.') {
                    nextX = i;
                    nextY = j;
                    found = true;
                }
            }
        }
        
        if (!found) return;
        
        for (int i = 1; i <= pieceCount && !foundSolution; i++) {
            if (usedPieces[i]) continue;
            
            Piece curPiece = pieces[i];
            usedPieces[i] = true;
            
            for (int rot = 0; rot < 4 && !foundSolution; rot++) {
                for (int flip = 0; flip < 2 && !foundSolution; flip++) {
                    if (placePiece(nextX, nextY, curPiece)) {
                        findCombination(nextX, nextY, usedPieces);
                        if (!foundSolution) {
                            removePiece(nextX, nextY, curPiece);
                        }
                    }
                    curPiece = curPiece.flip();
                }
                curPiece = curPiece.rotate();
            }
            
            if (!foundSolution) {
                usedPieces[i] = false;
            }
        }
    }

    public void solve() {
        boolean[] usedPieces = new boolean[pieceCount + 1];
        long startTime = System.currentTimeMillis();
        findCombination(1,1,usedPieces);
        long endTime = System.currentTimeMillis();

        System.out.println("Time Elapsed: " + (endTime - startTime) + " ms");
        System.out.println("Iteration Count: " + iterationCount);
        
        if (foundSolution) {
            System.out.println("Solution Found!");
            printBoard();
        } else {
            System.out.println("No Solution Found!");
        }
    }

}
