import java.awt.Color;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
import java.awt.event.KeyEvent;
import java.io.*;

//Handles the backend logic for the JWordle game, evaluating player guesses and maintaining
//the state of the game
public class JWordleLogic{
   
   
  
   //Number of words in the provided words.txt file
   private static final int WORDS_IN_FILE = 5758;
   
   //Use for generating random numbers!
   private static final Random rand = new Random();
   
   //Dimensions of the game grid in the game window
   public static final int MAX_ROWS = 6;
   public static final int MAX_COLS = 5;
   
   //Character codes for the enter and backspace key press
   public static final char ENTER_KEY = KeyEvent.VK_ENTER;
   public static final char BACKSPACE_KEY = KeyEvent.VK_BACK_SPACE;
   
   //The null character value (used to represent an "empty" value for a spot on the game grid)
   public static final char NULL_CHAR = 0;
   
   //Various Color Values
   private static final Color CORRECT_COLOR = new Color(53, 209, 42); //(Green)
   private static final Color WRONG_PLACE_COLOR = new Color(235, 216, 52); //(Yellow)
   private static final Color WRONG_COLOR = Color.DARK_GRAY; //(Dark Gray [obviously])
   private static final Color DEFAULT_KEYBOARD_COLOR = new Color(160, 163, 168); //(Light Gray)
   
   //Name of file containing all the five letter words
   private static final String WORDS_FILENAME = "words.txt";
   
   //Secret word used when the game is running in debug mode
   private static final char[] DEBUG_SECRET_WORD = {'B', 'A', 'N', 'A', 'L'};      
   

   //...Feel free to add more final variables of your own!
   private static final Color[] COLOR_MAP = {CORRECT_COLOR, WRONG_PLACE_COLOR};
            
   
   
   
   
   //******************   NON-FINAL GLOBAL VARIABLES   ******************
   //********  YOU CANNOT ADD ANY ADDITIONAL NON-FINAL GLOBALS!  ******** 
   
   
   //Array storing all words read out of the file
   private static String[] words;
   
   //The current row/col where the user left off typing
   private static int currentRow, currentCol;
      
   
   //*******************************************************************
   
   
   
   
   
   //This function gets called ONCE when the game is very first launched
   //before the user has the opportunity to do anything.
   //
   //Should return the randomly chosen "secret word" the player needs to guess
   //as a char array
   public static char[] initGame(){

      if (GameLauncher.DEBUG_USE_HARDCODED_WORD)
      {
         // JWordleGUI.setGridLetter(0, 0, 'C');
         // JWordleGUI.setGridColor(0, 0, CORRECT_COLOR);
         // JWordleGUI.setGridLetter(1, 3, 'O');
         // JWordleGUI.setGridColor(1, 3, WRONG_COLOR);
         // JWordleGUI.setGridLetter(3, 4, 'S');
         // JWordleGUI.setGridLetter(5, 4, 'C');
         // JWordleGUI.setGridColor(5, 4, WRONG_PLACE_COLOR);
         
         return DEBUG_SECRET_WORD;
      }
         
  
      return null;  //placeholder...
   }
   
   public static boolean traverseArray(char[] arr, char key) //traverse the word to see if current char is in it
   {
      for(char each: arr)
      {
         if(key == each)
            return true;
      }
      return false;
   }
   
   public static void paintKey(char key, Color color)
   {
      Color currentColor = JWordleGUI.getKeyColor(key);
      if(!currentColor.equals(CORRECT_COLOR))
      {
         JWordleGUI.setKeyColor(key, color); 
      }
      
   }

   public static void enterAction() //make sure evaluating will only work when it is the last col; It also evaluate whether the game ends or not.
   {
      if(currentCol==4 && JWordleGUI.getGridLetter(currentRow, currentCol)!=NULL_CHAR)
            {
               if(evaluating())
               {
                  JWordleGUI.endGame(true);
               }
               else if(!evaluating() && currentRow==5)
               {
                  JWordleGUI.endGame(false);
               }
               currentRow++;
               currentCol = 0;
            }
   }

   public static boolean evaluating() //evaluate each char in the word
   {
      boolean result = true;
      for(int i =0; i<5; i++)
      {
         char currentKey = JWordleGUI.getGridLetter(currentRow, i);
         if ( currentKey == DEBUG_SECRET_WORD[i])
         {
            JWordleGUI.setGridColor(currentRow, i, CORRECT_COLOR);
            paintKey(currentKey, CORRECT_COLOR);
            
         }
         else if(traverseArray(DEBUG_SECRET_WORD, currentKey)) //in the word, but wrong place
         {
            JWordleGUI.setGridColor(currentRow, i, WRONG_PLACE_COLOR);
            paintKey(currentKey, WRONG_PLACE_COLOR);
            result = false;
         }
         else
         {
            JWordleGUI.setGridColor(currentRow, i, WRONG_COLOR);
            paintKey(currentKey, WRONG_COLOR);
            result = false;
         }
      }
      return result;
   }

   public static void input(char key) //input valid character
   {
      if(currentCol<4)
      {
         JWordleGUI.setGridLetter(currentRow, currentCol, key);
         currentCol++;
      }
      else if(currentCol==4 && JWordleGUI.getGridLetter(currentRow, currentCol)==NULL_CHAR)
      {
         JWordleGUI.setGridLetter(currentRow, currentCol, key);
      }
   }

   public static void backspace(char key) //delete character 
   {
     
      if(currentCol!=0 && currentCol!=4 || (currentCol ==4 && JWordleGUI.getGridLetter(currentRow, currentCol)== NULL_CHAR) )
      {
         JWordleGUI.setGridLetter(currentRow, currentCol-1, NULL_CHAR);
         currentCol--;
      }
      else if (currentCol==4 && JWordleGUI.getGridLetter(currentRow, currentCol)!= NULL_CHAR)
      {
         currentCol++;
         JWordleGUI.setGridLetter(currentRow, currentCol-1, NULL_CHAR);
         currentCol--;
      }
      
   }
   
   //This function gets called everytime the user types a valid key on the
   //keyboard (alphabetic character, enter, or backspace) or clicks one of the
   //keys on the graphical keyboard interface.
   //
   //The key pressed is passed in as a char value.
   public static void keyPressed(char key){
      
       //implement me!
      //  int charNum = (int) key;
      //  if (charNum == 87)
      //    JWordleGUI.wiggleGrid(3);
      if(currentCol<5 && currentRow<6)
      {
         if(key == BACKSPACE_KEY)
         {
            backspace(key);
         }
         else if(key == ENTER_KEY)
         {
            enterAction();
         }
         else
         {    
            input(key);
         }
      }
      else if(currentRow==5 )
      {
         JWordleGUI.endGame(evaluating());
      }  
      
      System.out.println(currentRow+" "+currentCol);
      System.out.println("keyPressed called! key (int value) = '" + ((int)key) + "'");
   }
   
   
}
