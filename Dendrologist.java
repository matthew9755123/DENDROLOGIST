package dendrologist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Function;
import java.util.ArrayList;

/**
 * A testbed for an augmented implementation of an AVL tree
 * 
 * @author William Duncan, Matthew Benfield
 * @see AVLTreeAPI, AVLTree
 * 
 *      <pre>
 * Date: 10-18-23
 * Course: csc 3102 
 * Programming Project # 2
 * Instructor: Dr. Duncan
 *      </pre>
 */
public class Dendrologist {
    public static void main(String[] args) throws FileNotFoundException, AVLTreeException {
        String usage = "Dendrologist <order-code> <command-file>\n";
        usage += "  <order-code>:\n";
        usage += "  0 ordered by increasing string length, primary key, and reverse lexicographical order, secondary key\n";
        usage += "  -1 for reverse lexicographical order\n";
        usage += "  1 for lexicographical order\n";
        usage += "  -2 ordered by decreasing string length\n";
        usage += "  2 ordered by increasing string length\n";
        usage += "  -3 ordered by decreasing string length, primary key, and reverse lexicographical order, secondary key\n";
        usage += "  3 ordered by increasing string length, primary key, and lexicographical order, secondary key\n";
        if (args.length != 2) {
            System.out.println(usage);
            throw new IllegalArgumentException("There should be 2 command line arguments.");
        }

        File stringFile = new File(args[1]);
        Scanner input = new Scanner(stringFile);
        Function<String, PrintStream> func = x -> {
            return System.out.printf("%s%n", x);
        };

        int order_code = Integer.parseInt(args[0]);
        Comparator<String> cmp = (in1, in2) -> {
            switch (order_code) {
                case 0 -> {
                    if (in1.length() != in2.length()) {
                        return in1.length() - in2.length();
                    } else {
                        return -(in1.compareTo(in2));
                    }
                }
                case (-1) -> {
                    return -(in1.compareTo(in2));
                }
                case (1) -> {
                    return in1.compareTo(in2);
                }
                case (-2) -> {
                    return -(in1.length() - in2.length());
                }
                case (2) -> {
                    return in1.length() - in2.length();
                }
                case (-3) -> {
                    if (in1.length() != in2.length()) {
                        return -(in1.length() - in2.length());
                    } else {
                        return -(in1.compareTo(in2));
                    }
                }
                case (3) -> {
                    if (in1.length() != in2.length()) {
                        return in1.length() - in2.length();
                    } else {
                        return in1.compareTo(in2);
                    }
                }
                default -> {
                    System.out.println("The order code is -3 to 3.");
                    return 0;
                }
            }

        };

        AVLTree<String> tree = new AVLTree<>(cmp);
        String print1 = "";
        String print2 = "";
        while (input.hasNextLine()) {
            if (input.hasNext()) {
                print1 = input.next();
            }
            if (print1.equals("props")) {
                System.out.println("Properties:\nsize = " + tree.size() + ", height = " + tree.height()
                        + ", diameter = " + tree.diameter() + "\nfibonacci? = " + tree.isFibonacci()
                        + ", complete? = " + tree.isComplete());
                print1 = "";
            } else if (print1.equals("insert")) {
                if (input.hasNextLine()) {
                    print2 = input.next();
                }
                tree.insert(print2);
                System.out.println("Inserted: " + print2);
            } else if (print1.equals("gen")) {
                if (input.hasNextLine()) {
                    print2 = input.next();
                }
                System.out.print("Geneology: " + print2);
                if (!tree.inTree(print2)) {
                    System.out.println(" UNDEFINED");
                } else {
                    ArrayList<String> children = tree.getChildren(print2);
                    if (children.size() > 1) {
                        System.out.println("\nparent = " + tree.getParent(print2) + ", left-child = " + children.get(0)
                                + ", right-child = " + children.get(1) + "\n#ancestors = " + tree.ancestors(print2)
                                + ", #descendants = " + tree.descendants(print2));
                    }
                    if (children.size() == 1) {
                        System.out.println("\nparent = " + tree.getParent(print2) + ", left-child = " + children.get(0)
                                + ", right-child = NONE" + "\n#ancestors = " + tree.ancestors(print2)
                                + ", #descendants = " + tree.descendants(print2));
                    } else {
                        System.out.println("\nparent = " + tree.getParent(print2) + ", left-child = NONE"
                                + ", right-child = NONE" + "\n#ancestors = " + tree.ancestors(print2)
                                + ", #descendants = " + tree.descendants(print2));
                    }
                }
            } else if (print1.equals("delete")) {
                if (input.hasNextLine()) {
                    print2 = input.next();
                }
                tree.remove(print2);
                System.out.println("Deleted: " + print2);
                print2 = "";
                print1 = "";
            } else if (print1.equals("traverse")) {
                System.out.println("Pre-Order Traversal: ");
                tree.preorderTraverse(func);
                System.out.println("In-Order Traversal: ");
                tree.traverse(func);
                System.out.println("Post-Order Traversal: ");
                tree.postorderTraverse(func);
                print1 = "";
            } else {
                throw new IllegalArgumentException(args[1]+" <- Parsing Error");
            }
        }
        input.close();
    }
}