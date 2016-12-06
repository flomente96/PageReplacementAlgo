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
        int[] refString = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2};
        int size = 15;
        int frames = 3;
        int choice = 0;
        
        Scanner scanner = new Scanner(System.in);
        
//        while (choice != 4){
//            System.out.println("Choose a PAGE REPLACEMENT POLICY:\n1. First In First Out\n2. Least Recently Used\n3. Least Frequently Used\n4. Exit");
//            choice = scanner.nextInt();
//            System.out.println("Number of FREE FRAMES: ");
//            frames = scanner.nextInt();
//            System.out.println("Length of the REFERENCE STRING: ");
//            size = scanner.nextInt();
//            System.out.println("REFERENCE STRING: ");
            
            choice = 1;
            
            if(choice == 1){
                fifo(refString, frames);
            }
            else if (choice == 2){
                lru(refString, frames);
            }
            else if(choice == 3){
                lfu(refString, frames);
            }
//        }
        
        
//        fifo(refString, frames);
//        lru(refString, frames);
//        lfu(refString, frames);
    }
    
    
    static void toString(String[][] tables, int row, int column, String[] tally){
        StringBuilder sb = new StringBuilder();
        
        for(int r = 0; r < row; r++){
            for (int c = 0; c < column; c++){
                String s = "[" + tables[r][c] + "]";
                sb.append(s).append(" ");
            }
            
            sb.append("\n");
        }
        
        for(String item : tally)
            sb.append(item).append(" ");
        
        System.out.println(sb.toString());
    }
    
    
    static String[][] populate(String[][] table, Object[] state, int index){
        
        for(int i = 0; i < state.length; i++){
            
            table[i][index] = String.valueOf(state[i]);
        }
        
        return table;
    }
    
    
    static void fifo(int[] refString, int frames){
        Queue queue = new LinkedList(Arrays.asList(" ", " ", " "));
        String [] tally = new String[refString.length];
        
        String[][] toPrint = new String[frames][refString.length];
        int count = 0;
        
        for(int i = 0; i < refString.length; i++){
            
            System.out.println(queue.toString());
            toPrint = populate(toPrint, queue.toArray(), i);
            
            if(queue.contains(refString[i]) == false){
                System.out.println("Page fault: memory reference " + refString[i] + ": not found.");
                count++;
                
                if(queue.size()==frames)
                    queue.remove();
                    
                queue.add(refString[i]);
                tally[i] = "-F-";
            }
            else{
                tally[i] = "-H-";
            }
            
        }
        
        toString(toPrint, frames, refString.length, tally);
        System.out.println("Page Fault count: " + count);
    }
    
    static void lru(int[] refString, int frames){
        LinkedList queue = new LinkedList();
        int count = 0;
        
        for(int i = 0; i < refString.length; i++){
            System.out.println(queue.toString());
            
            if(queue.contains(refString[i]) == false){
                System.out.println("Page fault: Memory Reference " + refString[i] + ": not found.");
                count++;
                
                if(queue.size() == frames)
                    queue.remove();
                
                queue.add(refString[i]);
            }
            else
                queue.add(queue.remove(queue.indexOf(refString[i])));
        }
        
        System.out.println("Page Fault count: " + count);
    }
    
    static void lfu(int[] refString, int noFrames){
        LinkedList frames = new LinkedList();
        int count = 0;
        int indexMax = 0;
        int[] tally = new int[noFrames];
        int[] residence = new int[noFrames];
        
        for(int i = 0; i < refString.length; i++){
            System.out.println(frames.toString());
            
            if(frames.contains(refString[i]) == false){
                System.out.println("Page fault: Memory Reference " + refString[i] + ": not found.");
                count++;
                
                if(frames.size() == noFrames){
                    indexMax = getMax(tally, residence);
                    frames.remove(indexMax);
                }
                
                frames.add(indexMax, refString[i]);
            }
        }
        
        System.out.println("Page Fault count: " + count);
    }
    
    private static int getMax(int[] usage, int[] residence){
        int max = 0;
        
        for(int i = 0; i < usage.length; i++){
            if(usage[max] < usage[i])
                max = i;
            else if(usage[max] == usage[i]){
                if(residence[max] < residence[i])
                    max = i;
            }
        }
        
        return max;
    }
}
