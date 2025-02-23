package org.tucil1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.awt.image.BufferedImage;
import javafx.scene.paint.Color;

public class App extends Application {
    private Board board;
    private GridPane boardDisplay;
    private Label statusLabel;
    private Button solveButton;
    private Button saveButton;
    private Button resetButton;
    

    private static final String[] CSS_COLORS = {
        "", // Index 0 (unused)
        "rgb(255,0,0)",        // 1
        "rgb(255,128,0)",      // 2
        "rgb(255,255,0)",      // 3
        "rgb(128,255,0)",      // 4
        "rgb(0,255,0)",        // 5
        "rgb(0,255,128)",      // 6
        "rgb(0,255,255)",      // 7
        "rgb(0,128,255)",      // 8
        "rgb(0,0,255)",        // 9
        "rgb(128,0,255)",      // 10
        "rgb(255,0,255)",      // 11
        "rgb(255,0,128)",      // 12
        "rgb(255,128,128)",    // 13
        "rgb(255,191,0)",      // 14
        "rgb(191,255,0)",      // 15
        "rgb(0,255,191)",      // 16
        "rgb(0,191,255)",      // 17
        "rgb(128,128,255)",    // 18
        "rgb(255,128,255)",    // 19
        "rgb(255,69,0)",       // 20
        "rgb(0,128,128)",       // 21
        "rgb(128,0,128)",       // 22
        "rgb(128,128,0)",       // 23
        "rgb(255,215,0)",       // 24
        "rgb(169,169,169)",     // 25
        "rgb(0,191,191)"        // 26
    };

    public static String getColorforGUI(int id) {
        if (id < 1 || id > 26) {
            return "rgb(255,255,255)";
        }
        return CSS_COLORS[id];
    }

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

        char id = getPieceID(rawPieces.get(0));
        int intID = id - 'A';
        if(x == -1 || y == -1){
            throw new IllegalArgumentException("Pieces broken!");
        }
        return new Piece(n, m, pieceShape, intID);

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

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        statusLabel = new Label("Please load a puzzle file");
        statusLabel.setStyle("-fx-font-size: 14px;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loadButton = new Button("Load Puzzle");
        solveButton = new Button("Solve!");
        saveButton = new Button("Save as PNG");

        Button resetButton = new Button("Reset");

        solveButton.setDisable(true);
        saveButton.setDisable(true);
        resetButton.setDisable(true);

        buttonBox.getChildren().addAll(resetButton,loadButton, solveButton, saveButton);

        boardDisplay = new GridPane();
        boardDisplay.setAlignment(Pos.CENTER);
        boardDisplay.setHgap(1);
        boardDisplay.setVgap(1);
        boardDisplay.setStyle("-fx-background-color: white;");

        root.getChildren().addAll(resetButton,statusLabel, buttonBox, boardDisplay);

        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            
            if (selectedFile != null) {
                try {
                    BufferedReader reader = Files.newBufferedReader(selectedFile.toPath());
                    String line = reader.readLine();

                    String[] nm = line.split(" ");
                    int n = Integer.parseInt(nm[0]);
                    int m = Integer.parseInt(nm[1]);
                    int p = Integer.parseInt(nm[2]);

                    String boardType = reader.readLine();

                    List<String> pieces = new ArrayList<>();
                    while ((line = reader.readLine()) != null) {
                        pieces.add(line);
                    }
                    reader.close();

                    Piece[] pieceList = proccesPieceFromStringList(pieces, p);
                    int pieceCount = p;

                    
                    board = new Board(n, m, pieceList, pieceCount);
                    statusLabel.setText("File loaded: " + selectedFile.getName());
                    solveButton.setDisable(false);
                    updateBoardDisplay();
                    
                } catch (IOException | NumberFormatException | IndexOutOfBoundsException ex) {
                    showAlert("Error", "Failed to parse file: " + ex.getMessage());
                }
            }
        });

        solveButton.setOnAction(e -> {
            if (board != null) {
                board.solve();
                updateBoardDisplay();
                saveButton.setDisable(false);
                resetButton.setDisable(false);
                statusLabel.setText("Solution found!");
            }
        });

        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
            );
            File file = fileChooser.showSaveDialog(primaryStage);
            
            if (file != null) {
                try {
                    WritableImage snapshot = boardDisplay.snapshot(new SnapshotParameters(), null);
                    
                    BufferedImage bufferedImage = new BufferedImage(
                        (int) snapshot.getWidth(),
                        (int) snapshot.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                    );

                    for (int x = 0; x < snapshot.getWidth(); x++) {
                        for (int y = 0; y < snapshot.getHeight(); y++) {
                            Color color = snapshot.getPixelReader().getColor(x, y);
                            int argb = ((int) (color.getOpacity() * 255) << 24) |
                                     ((int) (color.getRed() * 255) << 16) |
                                     ((int) (color.getGreen() * 255) << 8) |
                                     ((int) (color.getBlue() * 255));
                            bufferedImage.setRGB(x, y, argb);
                        }
                    }

                    javax.imageio.ImageIO.write(bufferedImage, "png", file);
                    showAlert("Success", "Board saved successfully!");
                } catch (IOException ex) {
                    showAlert("Error", "Failed to save the image: " + ex.getMessage());
                }
            }
        });

        resetButton.setOnAction(e -> {
            board.pieces = null;
            board.grid = null;
            board = null;
            updateBoardDisplay();
            solveButton.setDisable(true);
            saveButton.setDisable(true);
            resetButton.setDisable(true);
            statusLabel.setText("Please load a puzzle file");
        });

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Pentomino Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateBoardDisplay() {
        boardDisplay.getChildren().clear();
        if (board == null) return;

        for (int i = 1; i <= board.n; i++) {
            for (int j = 1; j <= board.m; j++) {
                Pane cell = new Pane();
                cell.setPrefSize(30, 30);
                
                char piece = board.grid[i][j];
                if (piece != '.') {
                    String color = getColorforGUI(piece - 'A' + 1);
                    cell.setStyle("-fx-background-color: " + color + ";");
                } else {
                    cell.setStyle("-fx-background-color: black;");
                }
                
                boardDisplay.add(cell, j-1, i-1);
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}