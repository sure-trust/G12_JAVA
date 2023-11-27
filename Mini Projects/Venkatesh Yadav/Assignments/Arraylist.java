import java.util.*;

public class Arraylist {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(2);
        list.add(4);
        list.add(6);
        list.add(8);
        list.add(11);
        list.add(14);
        list.add(16);
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the element : ");
        Integer abc = scan.nextInt();
        scan.close();
        
        if (list.contains(abc)) {
            System.out.println("Exist");
        } else {
            System.out.println("not NotExist");
        }
    }
}
