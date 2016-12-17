/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagereplacementpolicies;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 *
 * @author faielomente
 */
public class PageReplacementPolicies {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        String[] refString = {"7", "0", "1", "2", "0", "3", "0", "4", "2", "3", "0", "3", "2", "1", "2"};
//        int size = 15;
//        int frames = 3;
//        int choice = 0;
        String[] refString;
        int size;
        int frames;
        int choice = 0;

        Scanner scanner = new Scanner(System.in);

        while (choice != 4) {
            
            System.out.println("Choose a PAGE REPLACEMENT POLICY:\n1. First In "
                    + "First Out\n2. Least Recently Used\n3. Least Frequently "
                    + "Used\n4. Exit");
            System.out.print("\nChoice: ");
            choice = scanner.nextInt();
            
            if (choice == 4) {
                
                break;
                
            } 
            else {
                
                System.out.println("Number of FREE FRAMES: ");
                frames = scanner.nextInt();
                System.out.println("Length of the REFERENCE STRING: ");
                size = scanner.nextInt();
                refString = new String[size];
                System.out.println("REFERENCE STRING: ");

                //asks user input for reference string
                for (int i = 0; i < size; i++) {
                    refString[i] = scanner.next();
                }

                if (choice == 1) {
                    fifo(refString, frames);
                }
                else if (choice == 2) {
                    lru(refString, frames);
                }
                else if (choice == 3) {
                    lfu(refString, frames);
                }
                
            }
        }
//        fifo(refString, frames);
//        lru(refString, frames);
//        lfu(refString, frames);
    }
    
    /**
     * Simulates the first in first out page replacement algorithm.
     * @param refString - reference string
     * @param frames - number of free frames
     */
    static void fifo(String[] refString, int frames) {
        Queue queue = new LinkedList();
        String[] tally = new String[refString.length];
        String[][] toPrint = new String[frames][refString.length];
        int count = 0;

        for (int i = 0; i < refString.length; i++) {

            //If it is a page fault it count will increment, 
            //head of the will be remove,
            //the ith element in the string array "tally" will be marked "F" for
            //for page fault
            if (queue.contains(refString[i]) == false) {
//                System.out.println("Page fault: memory reference " + refString[i] + ": not found.");
                count++;

                if (queue.size() == frames) {
                    queue.remove();
                }

                queue.add(refString[i]);
                tally[i] = "-F-";
            }
            //If it is a HIT the ith element in the string array "tally" will be
            //marked "H" for HIT
            else {
                tally[i] = "-H-";
            }
            
//            System.out.println(queue.toString());
            toPrint = populate(toPrint, queue.toArray(), i);

        }
        
        //for printing out the result
        toString(toPrint, refString, tally, frames, refString.length);
        System.out.println("Page Fault count: " + count);
    }
    
    /**
     * Simulates the least recently used page replacement algorithm. It will
     * replace the frame with the least number of usage.
     * @param refString - reference string
     * @param frames - number of free frames
     */
    static void lru(String[] refString, int frames) {
        LinkedList queue = new LinkedList();
        String[] tally = new String[refString.length];
        String[][] toPrint = new String[frames][refString.length];
        int count = 0;

        for (int i = 0; i < refString.length; i++) {
            
            //If it is a page fault
            if (queue.contains(refString[i]) == false) {
//                System.out.println("Page fault: Memory Reference " + refString[i] + ": not found.");
                count++;
                
                //If the all of the frames are occupied, remove the head of the 
                //queue
                if (queue.size() == frames) {
                    queue.remove();
                }
                
                //add the the ith element in the reference string 
                queue.add(refString[i]);
                tally[i] = "-F-";
            } 
            //In case of a hit, it will remove the head of the queue and put it
            //back at the tail becase the tail is the most recently used of all
            //the frames
            else {
                queue.add(queue.remove(queue.indexOf(refString[i])));
                tally[i] = "-H-";
            }
            
            
            toPrint = populate(toPrint, queue.toArray(), i);

        }

        toString(toPrint, refString, tally, frames, refString.length);
        System.out.println("Page Fault count: " + count);
    }
    
    /**
     * Simulates the least frequently used page replacement algorithm. In case of
     * a tie in the number of usage, it uses the FIFO as a tie breaker.
     * @param refString - the reference string
     * @param noFrames -  number of free frames
     */
    static void lfu(String[] refString, int noFrames) {
        LinkedList frames = new LinkedList();
        String[] tally = new String[refString.length];
        String[][] toPrint = new String[noFrames][refString.length];
        int count = 0;
        int indexMax;
        int[] usage = new int[noFrames];
        int[] residence = new int[noFrames];
        
        
        for (int i = 0; i < refString.length; i++) {
            
            //In case of a page fault
            if (frames.contains(refString[i]) == false) {
                count++;
                
                //if all of the free frames are occupied
                if (frames.size() == noFrames) {
                    
                    //it will get index of the frame with the most number of 
                    //frequently used frame
                    indexMax = getMax(usage, residence);
                    
                    //remove and replace the frame
                    frames.remove(indexMax);
                    frames.add(indexMax, refString[i]);
                    usage[frames.indexOf(refString[i])] = 1;
                }
                //If there exists an unoccupied frame
                else if (frames.size() < noFrames) {
                    frames.add(refString[i]);
                    usage[frames.indexOf(refString[i])] += 1;
                }
                
                //reset the residence period to 0
                residence[frames.indexOf(refString[i])] = 0;
                tally[i] = "-F-";

            }
            //In case of a hit
            else {
                //the number of usage frequency of the ith element will 
                //increment
                usage[frames.indexOf(refString[i])] += 1;
                tally[i] = "-H-";
            }
            
            //keeps track of the usage and duration of stay of each item in
            //the free frames
            for (int j = 0; j < residence.length; j++) {
                residence[j] += 1;
            }
                
//            System.out.println(frames.toString());
            toPrint = populate(toPrint, frames.toArray(), i);
        }

        toString(toPrint, refString, tally, noFrames, refString.length);
        System.out.println("Page Fault count: " + count);
    }
    
    /**
     * Get the maximum number of usage of a reference name. In case of a tie,
     * in the number of usage, it checks the residence period of the reference
     * name in the frame and return the index of the frame with the maximum 
     * number of residence period.
     * @param usage - frequency a reference string is used
     * @param residence - length of stay of a reference string in a frame
     * @return maximum number of usage and/or longest period of stay
     */
    private static int getMax(int[] usage, int[] residence) {
        int max = 0;

        for (int i = 1; i < usage.length; i++) {
            if (usage[max] < usage[i]) {
                max = i;
            } else if (usage[max] == usage[i]) {
                int rmax = 0;

                for (int j = 1; j < residence.length; j++) {
                    if (residence[rmax] <= residence[j]) {
                        rmax = j;
                    }
                }

                max = rmax;
            }
        }

        return max;
    }
    
    /**
     * Populates the table that represents the states of the queues.
     * @param table - the table to be populated
     * @param state - the states of the queues
     * @param index - index of the reference string
     * @return the table of queue states that will be printed
     */
    static String[][] populate(String[][] table, Object[] state, int index) {

        for (int i = 0; i < state.length; i++) {

            table[i][index] = String.valueOf(state[i]);

        }

        return table;
    }
    
    /**
     * Prints out the table of queue states.
     * @param tables - the table of queue states
     * @param refString - all the reference strings
     * @param tally - string array that keeps the record of page faults and hits
     * @param row - index of the row
     * @param column - index of the column
     */
    static void toString(String[][] tables, String[] refString, String[] tally, int row, int column) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < column; i++) {

            String str = "-" + refString[i] + "-";

            sb.append(str).append("\t");
        }

        sb.append("\n");

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < column; c++) {
                String s;

                if (tables[r][c] != null) {
                    s = "[" + tables[r][c] + "]";
                } else {
                    s = "[ ]";
                }

                sb.append(s).append("\t");
            }

            sb.append("\n");
        }
        for (String item : tally) {
            sb.append(item).append("\t");
        }

        System.out.println(sb.toString());
    }

}
