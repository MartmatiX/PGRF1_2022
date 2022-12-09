import java.util.Scanner;

public class Task1Malir {

    // github:  https://github.com/MartmatiX/PGRF1_2022

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.println("Choose mode:");
            System.out.println("'1' = Task 1 and Task 2");
            System.out.println("'2' = Task 3\n");
            System.out.print("Your input:");
            int setter = sc.nextInt();
            if (setter == 1) {
                Canvas canvas = new Canvas(1280, 720);
            } else {
                Controller3D controller = new Controller3D(800, 600);
                controller.start();
            }
        } catch (Exception e) {
            System.out.println("Unexpected input, closing application!");
            //System.out.println("Exception [" + e + "]");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
