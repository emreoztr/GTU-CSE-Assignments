
import java.util.Scanner;

public class Test {
    /*
     * public static void main(String[] args) { Scanner input = new
     * Scanner(System.in); HashSet<Integer> a = new HashSet<Integer>();
     * HashSet<Integer> b = new HashSet<Integer>(); LinkedList<Integer> c = new
     * LinkedList<Integer>();
     * 
     * Iterator<Integer> itera = a.iterator(); Iterator<Integer> iterb =
     * b.iterator(); Iterator<Integer> iterc = c.iterator();
     * 
     * int num; System.out.println("First set: ");
     * 
     * num = input.nextInt(); while (num != 0) { c.add(Integer.valueOf(num)); num =
     * input.nextInt(); }
     * 
     * System.out.println("Second set: "); num = input.nextInt(); while (num != 0) {
     * b.add(Integer.valueOf(num)); num = input.nextInt(); }
     * 
     * //System.out.println(a.containsAll(b)); //a.removeAll(b); //a.addAll(b);
     * c.removeAll(b);
     * 
     * System.out.println("First set: "); while (iterc.hasNext()) {
     * System.out.printf("%d,", iterc.next()); } System.out.println();
     * 
     * System.out.println("Second set: "); while (iterb.hasNext()) {
     * System.out.printf("%d,", iterb.next()); } System.out.println();
     * 
     * input.close();
     * 
     * }
     */

    public static void main(String[] args) {
        HashSet<Integer> intSet = new HashSet<Integer>();
        ArrayList<Integer> intArrList  = new ArrayList<Integer>();
        LinkedList<Integer> intLinkedList = new LinkedList<Integer>();

        HashSet<String> strSet = new HashSet<String>();
        ArrayList<String> strArrList  = new ArrayList<String>();
        LinkedList<String> strLinkedList = new LinkedList<String>();
        
        /*Iterator<String> iter_strset = strSet.iterator();
        Iterator<String> iter_strarrlist = strArrList.iterator();
        Iterator<String> iter_strlinkedlist = strLinkedList.iterator();*/
        Scanner input = new Scanner(System.in);
        int choice;
        Collection<Integer> intActive = null;
        Collection<String> strActive = null;
        boolean intmode = false, strmode = false;

        do {
            intmode = false;
            strmode = false;
            System.out.println("1) Elements in integer Set: " + intSet);
            System.out.println("2) Elements in integer Array List: " + intArrList);
            System.out.println("3) Elements in integer Linked List: " + intLinkedList);

            System.out.println("4) Elements in string Set: " + strSet);
            System.out.println("5) Elements in string Array List: " + strArrList);
            System.out.println("6) Elements in string Linked List: " + strLinkedList);

            System.out.println("0) Exit");

            System.out.print("Please choose a collection to change it: \n");
            input.reset();
            choice = input.nextInt();

            if (choice == 1) {
                intActive = (Collection<Integer>) intSet;
                intmode = true;
            }
            else if (choice == 2) {
                intActive = (Collection<Integer>) intArrList;
                intmode = true;
            }
            else if (choice == 3) {
                intActive = (Collection<Integer>) intLinkedList;
                intmode = true;
            }
            else if (choice == 4) {
                strActive = (Collection<String>) strSet;
                strmode = true;
            }
            else if (choice == 5) {
                strActive = (Collection<String>) strArrList;
                strmode = true;
            }
            else if (choice == 6) {
                strActive = (Collection<String>) strLinkedList;
                strmode = true;
            }

            if (choice != 0) {
                System.out.println("1) Add elements\n2)Remove element\n3)Check if contains\n4)Check if contains all elements from another collection\n5)Remove all elements same with another collection\n6)Retain all elements from another collection\nChoice:");
                choice = input.nextInt();
                if (choice == 1) {
                    System.out.println(
                            "Write and put space and write, if you want to stop put 0 and press enter! (For example 5 6 7 1 3 and enter)");
                    if (intmode) {
                        int num = input.nextInt();
                        do {
                            intActive.add(Integer.valueOf(num));
                            num = input.nextInt();
                        } while (num != 0);
                    } else if (strmode) {
                        String stop = new String(String.valueOf(0));
                        String str = new String(input.next());
                        do {
                            strActive.add(str);
                            str = new String(input.next());
                        } while (!str.equals(stop));
                        input.nextLine();
                    }
                }
                else if (choice == 2) {
                    System.out.println("Write the item that will be removed: ");
                    if (intmode) {
                        intActive.remove(input.nextInt());
                    } else if (strmode) {
                        strActive.remove(input.next());
                    }
                }
                else if (choice == 3) {
                    System.out.println("Write the item that will be checked if it is in the collection or not: ");
                    if (intmode) {
                        if(intActive.contains(input.nextInt()))
                            System.out.println("It contains the element");
                        else
                            System.out.println("It doesnt contain the element");
                    } else if (strmode) {
                        strActive.contains(input.next());
                    }
                }
                else if (choice == 4) {
                    if (intmode) {
                        System.out.println("1) Integer Set: " + intSet + "\n2) Integer Array List: " + intArrList
                                + "\n3) Integer Linked List: " + intLinkedList
                                + "\nPlease choose the collection that will check: ");
                        choice = input.nextInt();
                        if (choice == 1) {
                            if (intActive.containsAll(intSet))
                                System.out.println("It contains the elements");
                            else
                                System.out.println("It doesnt contain the elements");
                        } else if (choice == 2) {
                            if (intActive.containsAll(intArrList))
                                System.out.println("It contains the elements");
                            else
                                System.out.println("It doesnt contain the elements");
                        } else if (choice == 3) {
                            if (intActive.containsAll(intLinkedList))
                                System.out.println("It contains the elements");
                            else
                                System.out.println("It doesnt contain the elements");
                        }
                    }
                    else if (strmode) {
                        System.out.println("1) String Set: " + strSet + "\n2) String Array List: " + strArrList
                                + "\n3) String Linked List: " + strLinkedList
                                + "\nPlease choose the collection that will check: ");
                        choice = input.nextInt();
                        if (choice == 1) {
                            if (strActive.containsAll(strSet))
                                System.out.println("It contains the elements");
                            else
                                System.out.println("It doesnt contain the elements");
                        } else if (choice == 2) {
                            if (strActive.containsAll(strArrList))
                                System.out.println("It contains the elements");
                            else
                                System.out.println("It doesnt contain the elements");
                        } else if (choice == 3) {
                            if (strActive.containsAll(strLinkedList))
                                System.out.println("It contains the elements");
                            else
                                System.out.println("It doesnt contain the elements");
                        }
                    }
                }
                else if (choice == 5) {
                    if (intmode) {
                        System.out.println("1) Integer Set: " + intSet + "\n2) Integer Array List: " + intArrList
                                + "\n3) Integer Linked List: " + intLinkedList
                                + "\nPlease choose the collection that will remove its elements from this collection: ");
                        choice = input.nextInt();
                        if (choice == 1) {
                            intActive.removeAll(intSet);
                        } else if (choice == 2) {
                            intActive.removeAll(intArrList);
                        } else if (choice == 3) {
                            intActive.removeAll(intLinkedList);
                        }
                    } else if (strmode) {
                        System.out.println("1) String Set: " + strSet + "\n2) String Array List: " + strArrList
                                + "\n3) String Linked List: " + strLinkedList
                                + "\nPlease choose the collection that will remove its elements from this collection: ");
                        choice = input.nextInt();
                        if (choice == 1) {
                            strActive.removeAll(strSet);
                        } else if (choice == 2) {
                            strActive.removeAll(strArrList);
                        } else if (choice == 3) {
                            strActive.removeAll(strLinkedList);
                        }
                    }
                }
                else if (choice == 6) {
                    if (intmode) {
                        System.out.println("1) Integer Set: " + intSet + "\n2) Integer Array List: " + intArrList
                                + "\n3) Integer Linked List: " + intLinkedList
                                + "\nPlease choose the collection that will retain it's elements in this collection: ");
                        choice = input.nextInt();
                        if (choice == 1) {
                            intActive.retainAll(intSet);
                        } else if (choice == 2) {
                            intActive.retainAll(intArrList);
                        } else if (choice == 3) {
                            intActive.retainAll(intLinkedList);
                        }
                    }
                    else if (strmode) {
                        System.out.println("1) String Set: " + strSet + "\n2) String Array List: " + strArrList
                                + "\n3) String Linked List: " + strLinkedList
                                + "\nPlease choose the collection that will retain it's elements in this collection: ");
                        choice = input.nextInt();
                        if (choice == 1) {
                            strActive.retainAll(strSet);
                        } else if (choice == 2) {
                            strActive.retainAll(strArrList);
                        } else if (choice == 3) {
                            strActive.retainAll(strLinkedList);
                        }
                    }
                }
            }
        } while (choice != 0);

        input.close();
        //TESTING CLEAR
        intSet.clear();
        intArrList.clear();
        intLinkedList.clear();

        strArrList.clear();
        strSet.clear();
        strLinkedList.clear();

        intSet.add(5);
        intSet.add(6);
        intArrList.add(5);
        intArrList.add(6);
        intLinkedList.add(5);
        intLinkedList.add(6);

        strSet.add("Alfa");
        strSet.add("Bravo");
        strArrList.add("Alfa");
        strArrList.add("Bravo");
        strLinkedList.add("Alfa");
        strLinkedList.add("Bravo");
        System.out.println("Remove Test with Iterator:\n");
        System.out.printf("Integer Set: %s\n", intSet);
        System.out.printf("Integer ArrayList: %s\n", intArrList);
        System.out.printf("Integer LinkedList: %s\n", intLinkedList);
        System.out.printf("String Set: %s\n", strSet);
        System.out.printf("String ArrayList: %s\n", strArrList);
        System.out.printf("String LinkedList: %s\n", strLinkedList);

        Iterator<Integer> intIter = intSet.iterator();
        intIter.next();
        intIter.next();
        intIter.remove();
        intIter = intArrList.iterator();
        intIter.next();
        intIter.next();
        intIter.remove();
        intIter = intLinkedList.iterator();
        intIter.next();
        intIter.next();
        try{
            intIter.remove();
        } catch (CollectionNotRandomAccess e) {
            System.out.println("This exception is intended, it is exception test for integer linked list.");
        }

        Iterator<String> strIter = strSet.iterator();
        strIter.next();
        strIter.next();
        strIter.remove();
        strIter = strArrList.iterator();
        strIter.next();
        strIter.next();
        strIter.remove();
        strIter = strLinkedList.iterator();
        strIter.next();
        strIter.next();
        try{
            strIter.remove();
        } catch (CollectionNotRandomAccess e) {
            System.out.println("This exception is intended, it is exception test for string linked list.");
        }

        System.out.println("\nAfter removing second elements of each collection (Some of them are not removed because they are queue):");
        System.out.println("Remove Test with Iterator:\n");
        System.out.printf("Integer Set: %s\n", intSet);
        System.out.printf("Integer ArrayList: %s\n", intArrList);
        System.out.printf("Integer LinkedList: %s\n", intLinkedList);
        System.out.printf("String Set: %s\n", strSet);
        System.out.printf("String ArrayList: %s\n", strArrList);
        System.out.printf("String LinkedList: %s\n", strLinkedList);

        System.out.print("\nAfter using pool function of linked lists of string and integer:\n");
        intLinkedList.pool();
        strLinkedList.pool();
        System.out.printf("Integer LinkedList: %s\n", intLinkedList);
        System.out.printf("String LinkedList: %s\n", strLinkedList);
    }
}
