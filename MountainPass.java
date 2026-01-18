package project3.mountain;  

/** Outputs various info about a mountain range
 *  using the given data, as well as a visual representation.
 *
 * @author selen
 */
import java.util.*;
import java.io.*;
import java.awt.*;

public class MountainPass {
    
    // Asks for user input of the mountain range data file to be used before
    // calling necessary methods.
    public static void main(String[] args) throws FileNotFoundException {
        int[][] data = readFile();
            
        System.out.println("Mountain Peak: " + getHighest(data));
        System.out.println("Lowest Point: " + getLowest(data));
        Location[] path = createPath(data);
        System.out.print("Lowest Elevation Change Route: ");
        for(int i = 0; i < path.length; i++) {
            if (i < path.length - 1) {
                System.out.print(path[i].toString() + ", ");
            } else {
                System.out.println(path[i].toString());
            }
            
        }
        System.out.println("Total Elevation Change: " + totalChange(path));
        System.out.println("Steepest Elevation Change: " + steepest(path));
        
        createRange(data, path);
    }
    
    // Prompts user to input a data file before creating a 2D-array from it
    // to be used as the data set in other methods. Throws IOException if 
    // inputted file is not found.
    public static int[][] readFile() {
        System.out.print("Input file: ");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();
        sc.close();
        
       try (Scanner scanner = new Scanner(new FileReader(fileName))) {
           scanner.useDelimiter("[,\\s]+");
           int width = scanner.nextInt();
           int height = scanner.nextInt();
           int[][] data = new int[height][width];
           while (scanner.hasNextInt()) {
               for (int i = 0; i < height; i++) {
                   for (int k = 0; k < width; k++) {
                       data[i][k] = scanner.nextInt();
                   }
               }
           }
           scanner.close();
           return data;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }
    
    // Scans the data array for the highest value before
    // creating a location object for said value to store
    // its parameters. Prioritizes value closest to the top left of the array.
    public static Location getHighest(int[][] data) {
        int high = 0;
        int index1 = data.length;
        int index2 = data[0].length;
        
        for (int i = 0; i < data.length; i++) {
            for (int k = 0; k < data[0].length; k++) {
                if (data[i][k] > high) {
                    high = data[i][k];
                    index1 = i;
                    index2 = k;
                } else if ((data[i][k] == high) && (i < index1)) {
                    high = data[i][k];
                    index1 = i;
                    index2 = k;
                } else if ((data[i][k] == high) && (i == index1)) {
                    if (k < index2) {
                        high = data[i][k];
                        index1 = i;
                        index2 = k;
                    }
                }
            }
        }
        
        Location peak = new Location(index2, index1, high);
        return peak;
    }
    
    // Scans the data array for the lowest value before
    // creating a location object for said value to store
    // its parameters. Prioritizes value closest to top left of the array.
    public static Location getLowest(int[][] data) {
        int low = data[0][0];
        int index1 = data.length;
        int index2 = data[0].length;
        
        for (int i = 0; i < data.length; i++) {
            for (int k = 0; k < data[0].length; k++) {
                if (data[i][k] < low) {
                    low = data[i][k];
                    index1 = i;
                    index2 = k;
                } else if ((data[i][k] == low) && (i < index1)) {
                    low = data[i][k];
                    index1 = i;
                    index2 = k;
                } else if ((data[i][k] == low) && (i == index1)) {
                    if (k < index2) {
                        low = data[i][k];
                        index1 = i;
                        index2 = k;
                    }
                }
            }
        }
        
        Location lowest = new Location(index2, index1, low);
        return lowest;
    }
    
    // Using a random y-cord to start, finds a path through
    // the mountain with the lowest elevation change. Creates an array of 
    // Location objects to store the info of each step in this path to be
    // easily tracked and accessed. If two possible paths have the same
    // elevation, defaults to directly forward for simplicity, otherwise
    // a path is randomly selected.
    public static Location[] createPath(int[][] data) {
        Location[] path = new Location[data[0].length];
        Random rand = new Random();
        int row = rand.nextInt(data.length);
        int last = 0;
        int check1;
        int check2;
        int check3;
        int bottom = data.length - 1;
        path[0] = new Location(0, row, data[row][0]);
        
        for (int i = 1; i < data[0].length; i++) {
            if (row == 0) {
                check2 = Math.abs(data[0][last] - data[0][i]);
                check3 = Math.abs(data[0][last] - data[1][i]);
                if (check2 > check3) {
                    path[i] = new Location(i, 1, data[1][i]);
                    row = 1;
                } else if (check2 == check3) {
                    path[i] = new Location(i, 0, data[0][i]);
                } else {
                    path[i] = new Location(i, 0, data[0][i]);
                }
                
            } else if (row == bottom) {
                check1 = Math.abs(data[bottom][last] - data[bottom][i]);
                check2 = Math.abs(data[bottom][last] - data[bottom - 1][i]);
                if (check1 > check2) {
                    path[i] = new Location(i, bottom - 1, data[bottom - 1][i]);
                    row = bottom - 1;
                } else if (check1 == check2) {
                    path[i] = new Location(i, bottom, data[bottom][i]);
                } else {
                    path[i] = new Location(i, bottom, data[bottom][i]);
                }
            } else {
                check1 = Math.abs(data[row][last] - data[row - 1][i]);
                check2 = Math.abs(data[row][last] - data[row][i]);
                check3 = Math.abs(data[row][last] - data[row + 1][i]);
                if ((check1 < check2) && (check1 < check3)) {
                    path[i] = new Location(i, row - 1, data[row - 1][i]);
                    row -= 1;
                } else if ((check2 < check1) && (check2 < check3)) {
                    path[i] = new Location(i, row, data[row][i]);
                } else if ((check3 < check1) && (check3 < check2)) {
                    path[i] = new Location(i, row + 1, data[row + 1][i]);
                    row += 1;
                } else if ((check2 == check1) || (check2 == check3)) {
                    path[i] = new Location(i, row, data[row][i]);
                } else if (check1 == check3) {
                    if (rand.nextInt(2) == 1) {
                        path[i] = new Location(i, row + 1, data[row + 1][i]);
                        row += 1;
                    } else {
                        path[i] = new Location(i, row - 1, data[row - 1][i]);
                        row -= 1;
                    }
                }
            }
            last = i;
        }
        return path;
    }
    
    // Finds the steepest elevation change in the array of path locations.
    public static int steepest(Location[] list) {
        int diff = 0;
        for (int i = 0; i < list.length - 1; i++) {
            if (Math.abs(list[i].getElev() - list[i+1].getElev()) > diff) {
                diff = Math.abs(list[i].getElev() - list[i+1].getElev());
            }
        }
        return diff;
    }
    
    // Finds the total elevation change in the array of path locations.
    public static int totalChange(Location[] list) {
        int total = 0;
        for (int i = 0; i < list.length - 1; i++) {
            total += Math.abs(list[i].getElev() - list[i+1].getElev());
        }
        return total;
    }
    
    // Creates an image of 1 by 1 rectangles, each representing a value
    // from the original data array. The mountain peak is marked red and the 
    // lowest elevation change path is marked with green. All other rectangles
    // are grayscaled to represent their respective elevation in relation to
    // the total elevation range of the mountain.
    public static DrawingPanel createRange(int[][] data, Location[] list) {
        Color gray;
        DrawingPanel panel = new DrawingPanel(data[0].length, data.length);
        Graphics g = panel.getGraphics();
        for (int i = 0; i < data[0].length; i++ ) {
            for (int k = data.length - 1; k >= 0; k--) {
                gray = grayscale(data[k][i], data);
                if (list[i].getY() == k) {
                    g.setColor(Color.GREEN);
                    g.fillRect(i, k, 1, 1);
                } else if (data[k][i] == getHighest(data).getElev()) {
                    g.setColor(Color.RED);
                    g.fillRect(i, k, 1, 1);
                } else {
                    g.setColor(gray);
                    g.fillRect(i, k, 1, 1);
                }
            }
        }
        return panel;
    }
    
    // Finds the grayscale value of a given elevation to be used as 
    // a color for a cell in the visual representation of the mountain range.
    public static Color grayscale(int elev, int[][] data) {
        int gray;
        int low = getLowest(data).getElev();
        int high = getHighest(data).getElev();
        gray = 0 + (255 * (elev - low) / (high - low));
        
        Color c = new Color(gray, gray, gray);
        return c;
    }
}

