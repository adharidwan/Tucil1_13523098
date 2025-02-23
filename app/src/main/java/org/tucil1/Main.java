package org.tucil1;

import java.io.*;
import java.util.*;


public class Main {
    public static boolean samePieceBlock(String a , String b){
        char charA = '!';
        char charB = '!';
        for(int i = 0; i < a.length(); i++){
            if(a.charAt(i) >= 'A' && a.charAt(i) <= 'Z'){
                charA = a.charAt(i);
            }
        }

        for(int i = 0 ; i < b.length(); i++){
            if(b.charAt(i) >= 'A' && b.charAt(i) <= 'Z'){
                charB = b.charAt(i);
            }
        }

        return charA == charB;
    }

    public static Piece getPieceFromString(List<String> rawPieces){
        int n = rawPieces.size();
        int m = -1;
        for(int i = 0 ; i < n;i++){
            m = Math.max(m, rawPieces.get(i).length());
        }

        int x = -1;
        int y = -1;
        boolean[][] pieceShape = new boolean[n][m];

        for(int i = 0 ; i < n;i++){
            for(int j = 0 ; j < m;j++){
                if(j >= rawPieces.get(i).length()){
                    pieceShape[i][j] = false;  
                }else{
                    if(rawPieces.get(i).charAt(j) >= 'A' && rawPieces.get(i).charAt(j) <= 'Z'){
                        pieceShape[i][j] = true;
                        x = i;
                        y = j;
                    }
                }
            }
        }

        if(x == -1 || y == -1){
            throw new IllegalArgumentException("Pieces broken!");
        }
        return new Piece(n, m, pieceShape);

    }

    public static char getPieceID(String piece){
        for(int i = 0 ; i < piece.length(); i++){
            if(piece.charAt(i) >= 'A' && piece.charAt(i) <= 'Z'){
                return piece.charAt(i);
            }
        }
        return '!';
    }

    public static Piece[] proccesPieceFromStringList(List<String> rawPiecesList, int p){
        boolean[] usedChar = new boolean[26];
        for(int i = 0 ; i < 26 ; i++){
            usedChar[i] = false;
        }

        Piece[] pieces = new Piece[p+1];
        int cnt = 1;
        int len = rawPiecesList.size();
        for(int i = 0 ; i < len;i++){
            int pieceCount = i;
            while(samePieceBlock(rawPiecesList.get(i), rawPiecesList.get(pieceCount))){
                pieceCount++;
                if(pieceCount == len){
                    break;
                }
            }
            char pieceID = getPieceID(rawPiecesList.get(i));
            if(usedChar[pieceID - 'A']){
                throw new IllegalArgumentException("Pieces Already Used!");
            }else{
                usedChar[pieceID - 'A'] = true;
            }
            pieces[cnt++] = getPieceFromString(rawPiecesList.subList(i, pieceCount));
            
            i = pieceCount-1;

        }
        return pieces;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter test case file path: ");
        String filePath = scanner.nextLine();
        int n = -1, m = -1, p = -1;
        String boardType = "";
        List<String> pieces = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = null;
            
            // parse n m p 
            line = reader.readLine();
            String[] nm = line.split(" ");
            n = Integer.parseInt(nm[0]);
            m = Integer.parseInt(nm[1]);
            p = Integer.parseInt(nm[2]);
            

            // parse board
            line = reader.readLine();
            boardType = line;
            

            // parse pieces
            while((line = reader.readLine()) != null){
                pieces.add(line);
            }
            reader.close();
        }
        catch(IOException e){
            System.out.println("Error reading file");
        }finally{
            scanner.close();
        }

        Piece[] finalPieces = proccesPieceFromStringList(pieces,p);
        
        System.out.println("n = " + n);
        System.out.println("m = " + m);
        System.out.println("p = " + p);

        System.out.println("Board type: " + boardType);

        System.out.println("Pieces: ");
        for(int i = 1; i <= p; i++){
            System.out.println("Piece ID = " + (char)('A' + finalPieces[i].id));
            System.out.println("n = " + finalPieces[i].n);
            System.out.println("m = " + finalPieces[i].m);
            System.out.println("Shape: ");
            for(int j = 0 ; j < finalPieces[i].n + 2; j++){
                for(int k = 0 ; k < finalPieces[i].m + 2; k++){
                    System.out.print(finalPieces[i].shape[j][k] ? "#" : ".");
                }
                System.out.println();
            }
        }

        Board board = new Board(n, m, finalPieces, p);
        System.out.println("Board: ");
        board.printBoard();
        System.out.println("Start placing pieces: ");
        board.solve();
    }
}