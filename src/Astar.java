import java.util.ArrayList;

/**
 * Created by MA649
 * MOHAMMED ALTHEGAH
 */

public class Astar {
    ArrayList<Node> openList;
    ArrayList<Node> closedList;
    ArrayList<Node> possibleNodes;
    Node path;

    /** puzzles to insert in 2d array

     *d_**acd*bbdd*dba2dc**abd*ba_d*ddb
     cd**aad*b_bd*dbd2ca**a_d*dbbd*bdd
     da**d_d*cadb*bdb2da**dd_*cbdd*abb
     db**_ad*bdda*bcd2ba**ddd*bbdd*ac_
     dc**ba_*ddbd*abd2_c**dba*dddd*bab
     ad**_bb*ddcd*dba2ad**b_c*bdbd*dda
     db**_ad*adbd*dcb2ba**abd*ddbc*d_d
     dd**bca*ab_b*ddd2dd**bab*acdd*_bd
     ba**bd_*cddd*abd2ba**cd_*ddbd*abd
     bd**aab*dcdd*db_2ab**dda*d_bb*dcd
     cd**dbd*da_b*bad2cd**ddb*abdd*a_b
     dd**aba*cbb_*ddd2db**dca*ab_d*bdd
     da**abd*bcdd*db_2ad**acb*b_dd*dbd
     dd**bbd*addc*ab_2bd**_dd*badc*dab
     ac**_ba*dbdd*dbd2ac**bda*d_dd*bdb
     db**bad*dbca*dd_2ba**dd_*bddc*bda

     */

    public Astar()
    {
        //initial and goal configurations 

        String[][] initial = {{"a","c","*","*"},
                             {"_","b","a","*"},
                             {"d","b","d","d"},
                             {"*","d","b","d"}};

        String[][] goal = {{"a","c","*","*"},
                           {"b","d","a","*"},
                           {"d","_","d","d"},
                           {"*","b","d","b"}};


        openList = new ArrayList<Node>();
        closedList = new ArrayList<Node>();
        openList.add(new Node(initial));

        while(!openList.isEmpty()){
            Node lowest = getFnNode(openList); //get lowest puzzle in the openlist
            openList.remove(lowest);
            closedList.add(lowest);
            possibleNodes = nextConfig(lowest, goal);
            //loop through possible nodes,
            for(int i = 0; i < possibleNodes.size(); i++)
            {
                if( isSame(possibleNodes.get(i).node,goal)){
                    path = possibleNodes.get(i);
                    System.out.println("Solution Found :)");
                    while (path != null) {
                        printSolution(path);
                        path = path.parentNode;
                    }return;
                }
                if(!insideCL(possibleNodes.get(i), closedList)){
                    openList.add(possibleNodes.get(i));
                }
            }
        }
    }

    public void printSolution(Node configuration){
        System.out.println("");
        for(int i = 0 ; i < 4;i++){
            for(int j = 0 ; j < 4;j++){
                System.out.print(configuration.node[i][j]);
            }
            System.out.println();
        }
    }
    public boolean isSame(String[][] node1,String[][] node2){ //check if the 2 nodes match
        for (int x = 0 ; x < node1.length;x++){
            for(int y = 0;y< node1.length; y++){
                if(!node1[y][x].equals(node2[y][x])){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean insideCL(Node current, ArrayList<Node> closedList) // if node exists in closed list
    {
        for(int i = 0; i < closedList.size(); i++)
        {
            if(isSame(current.node, closedList.get(i).node))
            {
                return true;
            }
        }
        return false;
    }

    public Node getFnNode(ArrayList<Node> openList)
    {
        Node lowest = openList.get(0);

        for(int i = 0; i < openList.size(); i++)
        {
            if(openList.get(i).fn < lowest.fn) {
                lowest = openList.get(i);
            }
        }
        return lowest;//return lowest state
    }

    public int getHnValue(String[][] state, String[][] goal){ //check how many tiles are misplaced
        int hn = 0;
        for(int i = 0; i < state.length;i++){
            for(int j = 0; j < state.length;j++){
                if(!state[i][j].equals(goal[i][j])){
                   hn = hn + 1;
                }
            }
        }
        return hn;
    }
    
    public void copy(String[][] current, String[][] newState){ //copy array from currentState to newState
        for(int i = 0; i < current.length;i++){
            for(int j = 0; j < current.length; j++){
                newState[i][j] = current[i][j];
            }
        }
    }

    public ArrayList<Node> nextConfig(Node currentPuzzle,String[][] goal){

        int height  =-1;
        int width = -1;
        String underscore = "_";
        String star = "*";

        //find the underscore bar
        for (int coloumn = 0 ; coloumn < currentPuzzle.node.length; coloumn++){
            for(int row = 0; row < currentPuzzle.node.length; row++)
            {
                if(currentPuzzle.node[coloumn][row].equals(underscore))
                {
                    width = coloumn;
                    height = row;
                }
            }
        }
        ArrayList<Node> nextStates = new ArrayList<Node>(); // store next state
        //UP
        if(height - 1 >= 0 && !(currentPuzzle.node[width][height-1]).equals(star))
        {
            String[][] up = new String[currentPuzzle.node.length][currentPuzzle.node.length];
            copy(currentPuzzle.node, up);
            up[width][height] = up[width][height-1];
            up[width][height-1] = underscore;

            Node upPos = new Node(up, currentPuzzle.gn+1, getHnValue(up, goal));
            upPos.setParentNode(currentPuzzle);
            nextStates.add(upPos);
        }
        //LEFT
        if(width - 1 >= 0 && !(currentPuzzle.node[width-1][height]).equals(star)){
            String[][] left = new String[currentPuzzle.node.length][currentPuzzle.node.length];
            copy(currentPuzzle.node, left);
            left[width][height] = left[width-1][height];
            left[width-1][height] = underscore;

            Node leftPos = new Node(left, currentPuzzle.gn+1, getHnValue(left, goal));
            leftPos.setParentNode(currentPuzzle);
            nextStates.add(leftPos);
        }

        //DOWN
        if(height + 1 < currentPuzzle.node.length && !(currentPuzzle.node[width][height+1].equals(star))){
            String[][] down = new String[currentPuzzle.node.length][currentPuzzle.node.length];
            copy(currentPuzzle.node, down);
            down[width][height] = down[width][height+1];
            down[width][height+1] = underscore;

            Node downPos = new Node(down, currentPuzzle.gn+1, getHnValue(down, goal));
            downPos.setParentNode(currentPuzzle);
            nextStates.add(downPos);
        }
        //RIGHT
        if(width +  1 <  currentPuzzle.node.length && !(currentPuzzle.node[width+1][height]).equals(star)){
            String[][] right = new String[currentPuzzle.node.length][currentPuzzle.node.length];
            copy(currentPuzzle.node, right);
            right[width][height] = right[width+1][height];
            right[width+1][height] = underscore;

            Node rightPos = new Node(right, currentPuzzle.gn+1, getHnValue(right, goal));
            rightPos.setParentNode(currentPuzzle);
            nextStates.add(rightPos);
        }
        return nextStates;
    }
}
