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
        String[] refString = {"7", "0", "1", "2", "0", "3", "0", "4", "2", "3", "0", "3", "2", "1", "2"};
        int size = 15;
        int frames = 3;
        int choice = 0;
//        String[] refString;
//        int size;
//        int frames;
//        int choice = 0;

        Scanner scanner = new Scanner(System.in);

//        while (choice != 4) {
//            System.out.println("Choose a PAGE REPLACEMENT POLICY:\n1. First In First Out\n2. Least Recently Used\n3. Least Frequently Used\n4. Exit");
//            choice = scanner.nextInt();
//            System.out.println("Number of FREE FRAMES: ");
//            frames = scanner.nextInt();
//            System.out.println("Length of the REFERENCE STRING: ");
//            size = scanner.nextInt();
//            refString = new String[size];
//            System.out.println("REFERENCE STRING: ");
//            
//            //asks user input for reference string2
//            for(int i = 0; i < size; i++){
//                refString[i] = scanner.next();
//            }
//            
//            if (choice == 1) {
//                fifo(refString, frames);
//            } else if (choice == 2) {
//                lru(refString, frames);
//            } else if (choice == 3) {
//                lfu(refString, frames);
//            }
//        }

//        fifo(refString, frames);
//        lru(refString, frames);
        lfu(refString, frames);
    }


    static void fifo(String[] refString, int frames) {
        Queue queue = new LinkedList(Arrays.asList(" ", " ", " "));
        String[] tally = new String[refString.length];
        String[][] toPrint = new String[frames][refString.length];
        int count = 0;

        for (int i = 0; i < refString.length; i++) {

            System.out.println(queue.toString());
            toPrint = populate(toPrint, queue.toArray(), i);

            if (queue.contains(refString[i]) == false) {
                System.out.println("Page fault: memory reference " + refString[i] + ": not found.");
                count++;

                if (queue.size() == frames) {
                    queue.remove();
                }

                queue.add(refString[i]);
                tally[i] = "-F-";
            } else {
                tally[i] = "-H-";
            }

        }

        toString(toPrint, refString, tally, frames, refString.length);
        System.out.println("Page Fault count: " + count);
    }
    

    static void lru(String[] refString, int frames) {
        LinkedList queue = new LinkedList(Arrays.asList(" ", " ", " "));
        String[] tally = new String[refString.length];
        String[][] toPrint = new String[frames][refString.length];
        int count = 0;

        for (int i = 0; i < refString.length; i++) {
            
            System.out.println(queue.toString());
            
            toPrint = populate(toPrint, queue.toArray(), i);
            
            if (queue.contains(refString[i]) == false) {
                System.out.println("Page fault: Memory Reference " + refString[i] + ": not found.");
                count++;

                if (queue.size() == frames) {
                    queue.remove();
                }

                queue.add(refString[i]);
                tally[i] = "-F-";
            } else {
                queue.add(queue.remove(queue.indexOf(refString[i])));
                tally[i] = "-H-";
            }
        }
        
        toString(toPrint, refString, tally, frames, refString.length);
        System.out.println("Page Fault count: " + count);
    }
    

    static void lfu(String[] refString, int noFrames) {
        LinkedList frames = new LinkedList(Arrays.asList(" ", " ", " "));
        String[] tally = new String[refString.length];
        String[][] toPrint = new String[noFrames][refString.length];
        int count = 0;
        int indexMax;
        int[] usage = {0, 0, 0};
        int[] residence = {0, 0, 0};

        for (int i = 0; i < refString.length; i++) {
            
            System.out.println(frames.toString());
            
            toPrint = populate(toPrint, frames.toArray(), i);

            if (frames.contains(refString[i]) == false) {
                System.out.println("Page fault: Memory Reference " + refString[i] + ": not found.");
                count++;
                
                if (frames.size() == noFrames) {
                    indexMax = getMax(usage, residence);
                    System.out.println(indexMax);
                    frames.remove(indexMax);
                    frames.add(indexMax, refString[i]);
                    usage[indexMax] += 1;
                    residence[indexMax] = 0;
                }
                else if (frames.size() < noFrames){
                    
                    frames.add(refString[i]);
                    usage[frames.indexOf(refString[i])] += 1;
//                    residence[frames.indexOf(refString[i])] = 0;
                }

                tally[i] = "-F-";
            }
            else{
                usage[frames.indexOf(refString[i])] += 1;
                tally[i] = "-H-";
            }
            
            //keeps track of the usage and duration of stay of each item in
            //the free frames
            for(int j = 0; j < frames.size();j++){
                residence[j] += 1;
            }
        }
        
        toString(toPrint, refString, tally, noFrames, refString.length);
        System.out.println("Page Fault count: " + count);
    }
    
    
    private static int getMax(int[] usage, int[] residence) {
        int max = 0;

        for (int i = 0; i < usage.length; i++) {
            if (usage[max] < usage[i]) {
                max = i;
            } else if (usage[max] == usage[i]) {
                if (residence[max] < residence[i]) {
                    max = i;
                }
            }
        }

        return max;
    }
    
    
    static String[][] populate(String[][] table, Object[] state, int index) {

        for (int i = 0; i < state.length; i++) {

            table[i][index] = String.valueOf(state[i]);
        }

        return table;
    }
    
    
    static void toString(String[][] tables, String[] refString, String[] tally, int row, int column) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < column; i++) {

            String str = "-" + refString[i] + "-";

            sb.append(str).append("\t");
        }

        sb.append("\n");

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < column; c++) {
                String s = "[" + tables[r][c] + "]";
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
