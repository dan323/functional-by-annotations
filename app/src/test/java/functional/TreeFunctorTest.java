package functional;

import functional.annotation.iface.util.FunctorUtil;
import functional.data.tree.BinaryTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreeFunctorTest {

    @Test
    public void treeTest() {
        BinaryTree<Integer> leaf5 = BinaryTree.leaf(5);
        BinaryTree<Integer> leaf6 = BinaryTree.leaf(6);
        BinaryTree<Integer> tree = BinaryTree.node(leaf5, leaf6);

        var sol = BinaryTree.map(tree, k -> k * k);

        assertEquals(BinaryTree.node(BinaryTree.leaf(25), BinaryTree.leaf(36)), sol);
    }

    @Test
    public void treeFunctor() {
        BinaryTree<Integer> leaf5 = BinaryTree.leaf(5);
        BinaryTree<Integer> leaf6 = BinaryTree.leaf(6);
        BinaryTree<Integer> tree = BinaryTree.node(leaf5, leaf6);

        var sol = FunctorUtil.<BinaryTree, BinaryTree, BinaryTree<Integer>, BinaryTree<Integer>, Integer, Integer>map(BinaryTree.class, BinaryTree.class, tree, k -> k * k);

        assertEquals(BinaryTree.node(BinaryTree.leaf(25), BinaryTree.leaf(36)), sol);
    }
}
