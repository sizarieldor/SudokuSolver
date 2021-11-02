import java.util.ArrayDeque;
import java.util.Scanner;

/*
Example inputs:
702050600
000003000
100009500
800000090
043000750
090000008
009700005
000200000
007040203


078000040
000108006
000000001
000000009
805407000
000900500
024005000
500040680
006090000

 */


public class Main {

    private static final int GRID_SIZE = 9;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int[][] startGrid = new int[GRID_SIZE][GRID_SIZE];
        System.out.println("Please input your puzzle. Use zeroes for the empty squares and no spaces between numbers");
        System.out.printf("Example:%n702050600%n" +
                "000003000%n" +
                "100009500%n" +
                "800000090%n" +
                "043000750%n" +
                "090000008%n" +
                "009700005%n" +
                "000200000%n" +
                "007040203%n");

        //read sudoku from console
        for (int rows = 0; rows < GRID_SIZE; rows++) {
            String row = scan.nextLine().trim();
            startGrid[rows] = readStartingInputLine(row);
        }

        int[][] solvedGrid = new int[GRID_SIZE][GRID_SIZE];
        makeCopyOfMatrix(solvedGrid, startGrid);

        //solution algorithm
        ArrayDeque<Integer> correctNumbersStack = new ArrayDeque<>();
        int startCounter = 1;

        for (int rowPosition = 0; rowPosition < GRID_SIZE; rowPosition++) {
            for (int colPosition = 0; colPosition < GRID_SIZE; colPosition++) { //go through rows & columns

                if (startGrid[rowPosition][colPosition] == 0) { //if it is empty in the original sudoku
                    boolean isWorkingSolution = false; //tracks the state of the solved grid

                    //start checking row/col/quad
                    for (int tryNum = startCounter; tryNum <= 9; tryNum++) { //check if each number from 1 to 9 is a fit
                        if (checkRow(tryNum, solvedGrid, rowPosition) && checkColumn(tryNum, solvedGrid, colPosition) && checkSquare(tryNum, solvedGrid, rowPosition, colPosition)) {
                            solvedGrid[rowPosition][colPosition] = tryNum;
                            correctNumbersStack.push(tryNum);
                            isWorkingSolution = true;
                            break;
                        }
                    }

                    if (! isWorkingSolution) { //the above check was unsuccessful, go one step back
                        startCounter = correctNumbersStack.pop() + 1; //for the previous square we checked all digits from 1 to first successful one, now we try the digit after it

                        int[] stepBackCoords = stepBack(rowPosition, colPosition, startGrid);
                        rowPosition = stepBackCoords[0];
                        colPosition = stepBackCoords[1];

                        //delete last correct number
                        solvedGrid[rowPosition][colPosition] = 0; //again go to the previous square

                        stepBackCoords = stepBack(rowPosition, colPosition, startGrid);
                        rowPosition = stepBackCoords[0];
                        colPosition = stepBackCoords[1];

                    } else { //the last number we tried was ok
                        startCounter = 1; //prepare to iterate from 1 in the next square
                    }

                }
            }
        }
        printMatrix(solvedGrid);
    }


    private static int[] readStartingInputLine(String input) {    //game reader
        char[] inputChars = input.toCharArray();
        int[] result = new int[inputChars.length];
        for (int i = 0; i < inputChars.length; i++) {
            result[i] = Character.getNumericValue(inputChars[i]);
        }
        return result;
    }

    //matrix copier
    private static void makeCopyOfMatrix(int[][] emptyMatrix, int[][] targetMatrix) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                emptyMatrix[row][col] = targetMatrix[row][col];
            }

        }
    }

    //-----------------------------------------
    //-----------The three checkers------------
    //-----------------------------------------
    private static boolean checkRow(int digitToCheck, int[][] currGrid, int currRow) {    //row checker
        for (int col = 0; col < GRID_SIZE; col++) {
            if (currGrid[currRow][col] == digitToCheck) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkColumn(int digitToCheck, int[][] currGrid, int currCol) {//column checker
        for (int row = 0; row < GRID_SIZE; row++) {
            if (currGrid[row][currCol] == digitToCheck) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkSquare(int digitToCheck, int[][] currGrid, int currRow, int currCol) { //neighboring square checker
        int localBoxRowAnchor = currRow - currRow % 3;
        int localBoxColAnchor = currCol - currCol % 3;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (currGrid[row + localBoxRowAnchor][col + localBoxColAnchor] == digitToCheck) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int[] stepBack(int rowPosition, int colPosition, int[][] startGrid) {
        int[] coordinates = new int[2];

        do {
            if (colPosition > 0) {
                colPosition--;
            } else {
                colPosition = 8;
                rowPosition--; //maybe move up one row
            }
        } while (startGrid[rowPosition][colPosition]!=0);

        coordinates[0] = rowPosition;
        coordinates[1] = colPosition;
        return coordinates;
    }

    //matrix printer
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int i : row) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }
    
    //TODO
    //checks for valid input
}
