/**
* The ConnectFour class.
*
* This class represents a Connect Four (TM)
* game, which allows two players to drop
* checkers into a grid until one achieves
* four checkers in a straight line.
*/
   public class ConnectFour {
   
      final int EMPTY = -1;
      final int NUMPLAYER;    // number  of players
      final int NUMROW;       // number of rows on the game board
      final int NUMCOL;       // number of columns on the game board
      final int MAXGAME;      // number of games needed to win to win a match
   	
      final int MAXMOVES = 42;
      final int NEEDEDCONSEC = 3;
   
      ConnectFourGUI gui;     // the gui that provides the front end of the game
      int numMove;            // num of move that has been made in this game
      int curPlayer;          // the id number of the current player
      int grid[][];           // represents the grid of the game board
      int score[];            // represents the scores of the players
      int lowPlaceCol[];
      int currentRow;
      int consecPairs = 0;
      int immLCol, immRCol;
   	
      boolean winGot;
      boolean stillCheckLeft;
      boolean stillCheckRight;
   	
   /**
   * Constructor:  ConnectFour
   */
      public ConnectFour(ConnectFourGUI gui) {
         this.gui = gui;
         NUMPLAYER = gui.NUMPLAYER;
         NUMROW = gui.NUMROW;
         NUMCOL = gui.NUMCOL;
         MAXGAME = gui.MAXGAME;
      	
         grid = new int [NUMROW][NUMCOL];
         score = new int [NUMPLAYER];
         lowPlaceCol = new int [NUMCOL];
      	
         resetValues();
      //          this.gui = gui;
      //          NUMPLAYER = gui.NUMPLAYER;
      //          NUMROW = gui.NUMROW;
      //          NUMCOL = gui.NUMCOL;
      //          MAXGAME = gui.MAXGAME;
      //       
      //       // TO DO:  creation of arrays, and initialization of variables should be added here
      //       
      //          grid = new int [NUMROW][NUMCOL];
      //          for (int i = 0; i < NUMROW; i++) {
      //             for (int j = 0; j < NUMCOL; j++) {
      //                grid[i][j] = EMPTY;
      //             }
      //          }
      //       
      //          score = new int [NUMPLAYER];
      //          for (int i = 0; i < NUMPLAYER; i++) {
      //             gui.setPlayerScore(i,score[i]);
      //          }
      //       	
      //          lowPlaceCol = new int [NUMCOL];
      //       	
      //          for (int i = 0; i < NUMCOL; i++) {
      //             lowPlaceCol[i] = NUMROW - 1;
      //          }
      //       	
      //          numMove = 0;
      //       	
      }
   
   /**
   * play
   * This method will be called when a column is clicked.  Parameter "column" is 
   * the number of the column that is clicked by the user
   */
      public void play (int column) {
      // TO DO:  implement the logic of the game
      
         if (lowPlaceCol[column] >= 0) {
            currentRow = lowPlaceCol[column];
            placePiece(column);
         	
         	//methods for confirming whether the win is true or false
            checkVertiWin(column);
            checkHoriWin(column);
            checkPosDiagWin(column);
            checkNegDiagWin(column);
            confirmWinOrTie(column);
         	
            switchPlayer();
         }    
      	
      }  
   	
      public void placePiece(int column) {
                   
         grid[currentRow][column] = curPlayer;
         gui.setPiece(currentRow,column,curPlayer);
         numMove++;
         lowPlaceCol[column] -= 1;
      	
         immLCol = column - 1;
         immRCol = column + 1;
      
      }
   	
      public void switchPlayer() {
         if (curPlayer == 1){
            curPlayer = 0;
         }
         else {
            curPlayer = 1;
         }
         gui.setNextPlayer(curPlayer);
      }
   	
      public void checkVertiWin(int column) {
         
         consecPairs = 0;
      	
      	//to check for a vertical win, the following must be determined
      	//whether or not the stack is tall enough in the first place to be a vertical win
      	//if the piece below is of the same type
      	//when the loop is run for the first time, it already checks whether the piece below the placed one is the current player's also
      	//meaning that checking to see if that is the case is unnecessary
         if (currentRow <= 2) {
            for (int i = 0; i < NEEDEDCONSEC; i++) {
               if (grid[currentRow + i][column] == grid[currentRow + 1 + i][column]) {
                  consecPairs++;
               }
            }
         }
      	
         if (consecPairs == NEEDEDCONSEC) {
            winGot = true;
         }
      
      }
       
      public void checkHoriWin(int column) {
         
         consecPairs = 0;
			stillCheckLeft = true;
			stillCheckRight = true;
      	
      	//
      	//use boolean to confirm whether or not to keep checking in that direction
      	//
      	
         for (int i = 0; i < NEEDEDCONSEC; i++) {
         	
         	//checks for consecutive pieces to the left of the last played piece
            if (immLCol - i >= 0) {
               if (stillCheckLeft) {
                  if (grid[currentRow][column - i] == curPlayer) {
                     if (grid[currentRow][column - i] == grid[currentRow][immLCol - i]) {
                        consecPairs++;
                     }
                     else {
                        stillCheckLeft = false;
                     }
                  }
               }
            }
         	
         	//checks for consecutive pieces to the right of the last played piece
            if (immRCol + i <= NUMCOL - 1) {
               if (stillCheckRight) {
                  if (grid[currentRow][column + i] == curPlayer) {
                     if (grid[currentRow][column + i] == grid[currentRow][immRCol + i]) {
                        consecPairs++;
                     }
                     else {
                        stillCheckRight = false;
                     }
                  }
               }
            }
         }
         
         if (consecPairs >= NEEDEDCONSEC) {
            winGot = true;
         }
      
      }
   	
      public void checkPosDiagWin(int column) {
       
         consecPairs = 0;
      	
      	//checking for positive sloping diagonal win
         for (int i = 0; i < NEEDEDCONSEC; i++) {
         	
         	//checks for consecutive pieces to the southwest of the placed piece
            if (immLCol - i >= 0 && currentRow + 1 + i <= 5) {
               if (grid[currentRow + i][column - i] == curPlayer) {
                  if (grid[currentRow + i][column - i] == grid[currentRow + 1 + i][immLCol - i]) {
                     consecPairs++;
                  }
               }
            }
         	
         	//checks for consecutice pieces to the northeast of the placed piece
            if (immRCol + i <= NUMCOL - 1 && currentRow - 1 - i >= 0) {
               if (grid[currentRow - i][column + i] == curPlayer) {
                  if (grid[currentRow - i][column + i] == grid[currentRow - 1 - i][immRCol + i]) {
                     consecPairs++;
                  }
               }
            }
         	
         }
      	
      	
         if (consecPairs >= NEEDEDCONSEC) {
            winGot = true;
         }
      
      }
   	
      public void checkNegDiagWin(int column) {
       
         consecPairs = 0;
      	
      	//checking for negatively sloping diagonal win
         for (int i = 0; i < NEEDEDCONSEC; i++) {
         	
         	//checks for consecutive pieces to the northwest of the placed piece
            if (immLCol - i >= 0 && currentRow - 1 - i >= 0) {
               if (grid[currentRow - i][column - i] == curPlayer) {
                  if (grid[currentRow - i][column - i] == grid[currentRow - 1 - i][immLCol - i]) {
                     consecPairs++;
                  }
               }
            }
         	
         	//checks for consecutice pieces to the southeast of the placed piece
            if (immRCol + i <= NUMCOL - 1 && currentRow + 1 + i <= 5) {
               if (grid[currentRow + i][column + i] == curPlayer) {
                  if (grid[currentRow + i][column + i] == grid[currentRow + 1 + i][immRCol + i]) {
                     consecPairs++;
                  }
               }
            }
         	
         }
         
         if (consecPairs >= NEEDEDCONSEC) {
            winGot = true;
         }
      
      }
   
      public void confirmWinOrTie(int column){
            	      
         if (winGot) {
            gui.showWinnerMessage(curPlayer);
            score[curPlayer]++;
            gui.resetGameBoard();
            resetValues();
         	
            for (int i = 0; i < NUMPLAYER; i++) {
               gui.setPlayerScore(i,score[i]);
            }
         
         }
         //checks for tie games
         else if (numMove == MAXMOVES) {
            gui.showTieGameMessage();
            gui.resetGameBoard();
            resetValues();
         
         }      
         if (score[curPlayer] == MAXGAME) {
            gui.showFinalWinnerMessage(curPlayer);
         }
         
      	
      }
      public void resetValues() {
      
      // TO DO:  creation of arrays, and initialization of variables should be added here
      
         for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {
               grid[i][j] = EMPTY;
            }
         }
      
         for (int i = 0; i < NUMPLAYER; i++) {
            gui.setPlayerScore(i,score[i]);
         }
      	
      	
         for (int i = 0; i < NUMCOL; i++) {
            lowPlaceCol[i] = NUMROW - 1;
         }
      	
         numMove = 0;
         winGot = false;
         stillCheckLeft = true;
         stillCheckRight = true;
      
      }
   }