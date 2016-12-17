CMSC 125 Machine Problem 2
==============================How to run the file===============================

How to run the program:
  Just click the Lomente_Bankers.exe file. IF THE EXECUTABLE FILE WON'T WORK
  PLEASE USE THE JAR FILE AND FOLLOW THE INSTRUCTION BELOW:
              To run the file, MUST use the TERMINAL and run the .jar file:
              Run this command inside the PageReplacementAlgo/dist directory
                          "java -jar PageReplacementPolicies.jar"

  This do not have a .exe file because the .jar file that was compiled and build
  in my Linux machine won't run properly in Windows. The .jar file will only run
  in windows using its command prompt.

================================================================================
Page Replacement Policies
    - Page Replacement Policies are the replacement algorithms that decides
        which page to be swap out to disk when a page fault occurs.
    - These algorithms can be used in an operating system that uses paging for
        virtual memory management.

First In First Out (FIFO)
    - Page replacement strategy that is like a Queueing system. The first page
        that enters the queue will be replaced.

Least Recently Used (LRU)
    - A Page Replacement strategy that replaces the frame that has the longest
        residence period among all the frames

Least Frequently Used (LFU)
    - A Page replacement algorithm that replaces the frame with the least
        frequency of usage

The program:
    User Inputs:
        1. The program will ask for the Replacement Policy to be used in the
            simulation.
        2. It will ask for the number of free frames.
        3. It will ask for the length of the reference string.
        4. Then it will as for the reference string one by one.
                Example:
                        REFERENCE STRING:
                        6
                        7
                        8
                        9
                        1
                        2
                        5
    The algorithm:
        FIFO - uses the Queue data structure because of its first in first out
                feature
            > If it is a page fault it count will increment, head of the
                will be remove, the ith element in the string array "tally" will
                be marked "F" for page fault.
                        -3-            -0-
                        [1]            [2]
                        [2]     >>     [3]
                        [3]            [0]
                        -F-            -F-
            > If it is a HIT the ith element in the string array "tally" will be
                marked "H" for HIT
                        -H-     >>     -F-

        LRU - uses the LinkedList data structure of its easy way of removing the
                head and adding to the end of the list without manually shifting
                the elements. I used LinkedList instead of Queue because the
                need to still access the elements in between the head and the
                tail of the list.
            > In case of a page fault, if the all of the frames are occupied,
                remove the head of the queue and add the ith element in the
                reference string
                        -3-     	-0-
                        [1]     	[2]
                        [2]     >>	[3]
                        [3]     	[0]
                        -F-     	-F-
            > But in case of a page fault and there still exists a free frame,
                it will just add the string to the end of the list.
                        -7-     	-0-
                        [7]     	[7]
                        [ ]     >>	[0]
                        [ ]     	[ ]
            > In case of a hit, it will remove the head of the queue and put it
                back at the tail because the tail is the most recently used of all
                the frames
                        -2-     	-0-
                        [0]     	[1]
                        [1]     >>	[2]
                        [2]     	[0]
                        -F-     	-H-

        LFU - also used of the LinkedList data structure because of the same
                reasons stated above.
            > In case of a page fault, residence period will be reset back to 0
            > In case of a page fault, if all of the free frames are occupied
                it will get index of the frame with the most number of
                frequently used. In case of a tie in the number of usage, it
                uses the FIFO as a tie breaker. Then it will frame remove and
                replace the frame
                        -4-     	-2-
                        [4]     	[4]
                        [3]     >>	[2]
                        [0]     	[0]
                        -F-     	-F-
            >If there exists an unoccupied frame, and there still exists a free
                frame, it will just add the string to the end of the list.
                        -7-     	-0-
                        [7]     	[7]
                        [ ]     >>	[0]
                        [ ]     	[ ]
            > In case of a hit, the number of usage frequency of the ith
                element will increment
