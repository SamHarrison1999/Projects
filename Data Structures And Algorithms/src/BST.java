import java.util.Stack;

public class BST
{ private BTNode<Integer> root;

    public BST()
    { root = null;
    }

    public boolean find(Integer i)
    { BTNode<Integer> n = root;
        boolean found = false;

        while (n!=null && !found)
        { int comp = i.compareTo(n.data);
            if (comp==0)
                found = true;
            else if (comp<0)
                n = n.left;
            else
                n = n.right;
        }

        return found;
    }

    public boolean insert(Integer i)
    { BTNode<Integer> parent = root, child = root;
        boolean goneLeft = false;

        while (child!=null && i.compareTo(child.data)!=0)
        { parent = child;
            if (i.compareTo(child.data)<0)
            { child = child.left;
                goneLeft = true;
            }
            else
            { child = child.right;
                goneLeft = false;
            }
        }

        if (child!=null)
            return false;  // number already present
        else
        { BTNode<Integer> leaf = new BTNode<Integer>(i);
            if (parent==null) // tree was empty
                root = leaf;
            else if (goneLeft)
                parent.left = leaf;
            else
                parent.right = leaf;
            return true;
        }
    }

    public int nonleaves() {
        Stack<BTNode<Integer>> nonleavesStack = new Stack<>();
        BTNode<Integer> BinaryTreenode = root;
        if (root == null) {
            return 0;

        }
        int count = 0;
        while (BinaryTreenode != null) {
            nonleavesStack.push(BinaryTreenode);
            BinaryTreenode = BinaryTreenode.left;
        }
        while (nonleavesStack.size() > 0) {
            BinaryTreenode = nonleavesStack.pop();
            if (BinaryTreenode.right != null) {
                BinaryTreenode = BinaryTreenode.right;

                while (BinaryTreenode != null) {
                    nonleavesStack.push(BinaryTreenode);
                    BinaryTreenode = BinaryTreenode.left;
                }
            } else if (BinaryTreenode.left == null) {
                count++;
            }
        }
        return count;
    }

    public int depth() {
        Stack<BTNode<Integer>> depthStack = new Stack<>();
        BTNode<Integer> BinaryTreenode = root;

        if (root == null) {
            return 0;
        }
        int count = 0;
        while (BinaryTreenode != null) {
            depthStack.push(BinaryTreenode);
            BinaryTreenode = BinaryTreenode.left;
        }
        while (depthStack.size() > 0) {
            BinaryTreenode = depthStack.pop();
            if (BinaryTreenode.right != null) {
                BinaryTreenode = BinaryTreenode.right;

                while (BinaryTreenode != null) {
                    depthStack.push(BinaryTreenode);
                    BinaryTreenode = BinaryTreenode.left;
                    count++;
                }
            }
        }
        return count;
    }

    public int range(int min, int max) {
        Stack<BTNode<Integer>> rangeStack = new Stack<>();
        BTNode<Integer> BinaryTreenode = root;
        if (root == null) {
            return 0;
        }
        if (max < min) {
            throw new IllegalArgumentException();
        }
        int a = 0;
        while (BinaryTreenode != null) {
            rangeStack.push(BinaryTreenode);
            BinaryTreenode = BinaryTreenode.left;
        }
        while (rangeStack.size() > 0) {
            BinaryTreenode = rangeStack.pop();
            if (BinaryTreenode.data >= min && BinaryTreenode.data <= max) {
                a++;
            }
            if (BinaryTreenode.right != null) {
                BinaryTreenode = BinaryTreenode.right;

                while (BinaryTreenode != null) {
                    rangeStack.push(BinaryTreenode);
                    BinaryTreenode = BinaryTreenode.left;
                }
            }
        }
        return a;
    }
}



class BTNode<T>
{ T data;
    BTNode<T> left, right;

    BTNode(T o)
    { data = o; left = right = null;
    }
}
