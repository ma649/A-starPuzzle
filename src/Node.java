
/**
 * Created by MA649
 * MOHAMMED ALTHEGAH
 */

public class Node {
    public int fn,gn,hn; //f(n) = g(n)  + h(h)
    public String[][] node;
    public Node parentNode;


    public Node(String[][] node){
        this.node = node;
        fn = -1;
        gn = -1;
        hn = -1;
    }

    public Node(String[][] node, int gCost, int hCost){
        this.node = node;
        gn = gCost;
        hn = hCost;
        fn = gCost + hCost;
    }


    public void setParentNode(Node parentNode){
        this.parentNode = parentNode;
    }


}
