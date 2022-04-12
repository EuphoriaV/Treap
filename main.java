import java.util.Scanner;
import java.util.Random;

public class Main {

    static class Node {
        Node left;
        Node right;
        int x;
        double y;
        int w;

        public Node() {
            this.left = null;
            this.right = null;
            this.y = new Random().nextInt();
            this.w = 1;
        }

        public Node(Node left, Node right, int x) {
            this.left = left;
            this.right = right;
            this.x = x;
            this.y = new Random().nextInt();
            this.w = 1;
        }
    }

    static class Pair {
        Node first, second;

        public Pair(Node first, Node second) {
            this.first = first;
            this.second = second;
        }
    }

    static Node tree = null;

    public static int getW(Node t) {
        if (t == null) {
            return 0;
        } else {
            return t.w;
        }
    }

    public static void update(Node t) {
        if (t != null) {
            t.w = getW(t.left) + getW(t.right) + 1;
        }
    }

    public static Pair split(Node t, int k) {
        if (t == null) {
            return new Pair(null, null);
        } else if (k > t.x) {
            Pair ans = split(t.right, k);
            t.right = ans.first;
            update(t.right);
            return new Pair(t, ans.second);
        } else {
            Pair ans = split(t.left, k);
            t.left = ans.second;
            update(t.left);
            return new Pair(ans.first, t);
        }
    }

    public static Node merge(Node t1, Node t2) {
        if (t2 == null) {
            return t1;
        } else if (t1 == null) {
            return t2;
        } else if (t1.y > t2.y) {
            t1.right = merge(t1.right, t2);
            update(t1.right);
            return t1;
        } else {
            t2.left = merge(t1, t2.left);
            update(t2.left);
            return t2;
        }
    }

    public static Node insert(Node t, int k) {
        Pair ans = split(t, k);
        t = merge(merge(ans.first, new Node(null, null, k)), ans.second);
        update(t);
        return t;
    }

    public static Node remove(Node t, int k) {
        if (t.x == k) {
            t = merge(t.left, t.right);
        } else if (k < t.x) {
            t.left = remove(t.left, k);
        } else if (k > t.x) {
            t.right = remove(t.right, k);
        }
        update(t);
        return t;
    }

    public static void print(Node t) {
        if (t != null) {
            print(t.left);
            System.out.print(t.x + " ");
            print(t.right);
        }
    }

    public static int getK(Node t, int k) {
        int bebra = getW(t.right);
        if (bebra + 1 == k) {
            return t.x;
        } else if (bebra + 1 > k) {
            return getK(t.right, k);
        } else {
            return getK(t.left, k - bebra - 1);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        boolean first = true;
        for (int i = 0; i < n; i++) {
            int a = sc.nextInt(), b = sc.nextInt();
            if (a == 1) {
                if (first) {
                    tree = new Node(null, null, b);
                    first = false;
                } else {
                    tree = insert(tree, b);
                }
            } else if (a == -1) {
                tree = remove(tree, b);
            } else {
                System.out.println(getK(tree, b));
            }
        }
    }
}
