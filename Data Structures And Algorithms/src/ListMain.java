public class ListMain
{ public static void main(String args[])
{ LList<Integer> myList = new LList<Integer>(); // <>
    System.out.println("Creating Queue");
    System.out.println("Queue Contents: " + myList); // <>
    if (myList.isEmpty() == 0){
        System.out.println("The queue is empty. There are " +  myList.isEmpty() + " elements in the queue.");
    }
    myList.addLeft(3);     // <3>
    System.out.println("Adding 3 to the left of the queue");
    System.out.println("Queue Contents: " + myList); // <3>
    myList.addRight(5);     // <3,5>
    System.out.println("Adding 5 to the right of the queue");
    System.out.println("Queue Contents: " + myList); // <3,5>
    myList.addRight(7);     // <3,5,7>
    System.out.println("Adding 7 to the right of the queue");
    System.out.println("Queue Contents: " + myList); // <3,5,7>
    myList.addLeft(10);     // <10,3,5,7>
    System.out.println("Adding 10 to the left of the queue");
    System.out.println("Queue Contents: " + myList); // <10,3,5,7>
    if (myList.isEmpty() > 0){
        System.out.println("The queue is not empty. There are " +  myList.isEmpty() + " elements in the queue.");
    }
    System.out.println("Calling left: " + myList.Left(0) + " returned");
    System.out.println("Calling right: " + myList.Right(myList.isEmpty() - 1) + " returned");
    myList.removeLeft(); // <3,5,7>
    System.out.println("Removing 10 from the left of the queue");
    System.out.println("Queue Contents: " + myList); // <3,5,7>
    myList.removeRight(); // <3,5>
    System.out.println("Removing 7 from the right of the queue");
    System.out.println("Queue Contents: " + myList); // <3,5>
    myList.removeLeft(); // <5>
    System.out.println("Removing 3 from the left of the queue");
    System.out.println("Queue Contents: " + myList); // <5>
    myList.removeRight(); // <>
    System.out.println("Removing 5 from the right of the queue");
    System.out.println("Queue Contents: " + myList); // <>
    try {
        System.out.println("Calling left: " + myList.Left(0) + " returned");
    } catch (ListException e) {
        System.out.println(e);
    }
    try {
        System.out.println("Calling right: " + myList.Right(myList.isEmpty() - 1) + " returned");
    } catch (ListException e) {
        System.out.println(e);
    }
    try {
        myList.removeRight();
    } catch (ListException e) {
        System.out.println(e); // removeRight called from an empty queue
    }
    try {
        myList.removeLeft();
    } catch (ListException e) {
        System.out.println(e); // removeLeft called from an empty queue
    }
}
}