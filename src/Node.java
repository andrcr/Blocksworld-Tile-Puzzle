import java.util.ArrayList;

/**
 * Created by Andr on 28-Nov-17.
 */
public class Node implements Comparable {

    Node parent;
    //blocks.get(0) is always the agent
    ArrayList<Block> blocks;
    //    y
    //   --->
    // x |
    //   v
    char lastMove;
    int size, noBlocks, cost, depth;


    //root
    public Node(int size, int noBlocks) {
        this.size = size;
        this.noBlocks = noBlocks;
        blocks = new ArrayList<Block>();
        blocks.add(new Block('1', size - 1, size - 1));
        char tmp;
        for (int i = 0; i < noBlocks; i++) {
            tmp = 'A';
            tmp += i;
            blocks.add(new Block(tmp, size - 1, i));
        }
    }

    //node
    public Node(Node parent, char move) {
        this.parent = parent;
        this.lastMove = move;
        this.size = parent.getSize();
        this.noBlocks = parent.getNoBlocks();
        blocks = new ArrayList<>();
        for (Block b : parent.getBlocks())
            blocks.add(new Block(b.name, b.x, b.y));
        this.makeMove(move);
    }

    public Node(Node parent, char move, boolean hasBlocks) {
        this.parent = parent;
        this.lastMove = move;
        this.size = parent.getSize();
        this.noBlocks = parent.getNoBlocks();
        blocks = new ArrayList<>();
        for (Block b : parent.getBlocks())
            blocks.add(new Block(b.name, b.x, b.y));
        this.makeMoveObstacle(move);
    }

    //used for test
    //creates the solution for that size
    public Node(int size, int noBlocks, boolean isSolution) {
        this.size = size;
        blocks = new ArrayList<Block>();
        blocks.add(new Block('1', size - 1, size - 1));
        this.noBlocks = noBlocks;
        //agent's pos is irrelevant
        char tmp;
        for (int i = 0; i < noBlocks; i++) {
            tmp = 'A';
            tmp += i;
            blocks.add(new Block(tmp, i + size - noBlocks, 1));
        }
    }

    @SuppressWarnings("Duplicates")
    private void makeMove(char move) {

        switch (move) {
            case 'U': //up  x--
                blocks.get(0).x--;
                //checking if the agent is over a block
                //it can be over at most a block
                for (int i = 1; i < blocks.size(); i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) {  //they have to swap
                            blocks.get(i).x++;
                            return;
                        }
                break;

            case 'D': //down x++
                blocks.get(0).x++;
                //checking if the agent is over a block
                //it can be over at most a block
                for (int i = 1; i < blocks.size(); i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) {  //they have to swap
                            blocks.get(i).x--;
                            return;
                        }
                break;

            case 'L': //left y--
                blocks.get(0).y--;
                //checking if the agent is over a block
                //it can be over at most a block
                for (int i = 1; i < blocks.size(); i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) {  //they have to swap
                            blocks.get(i).y++;
                            return;
                        }
                break;

            case 'R': //right y++
                blocks.get(0).y++;
                //checking if the agent is over a block
                //it can be over at most a block
                for (int i = 1; i < blocks.size(); i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) {  //they have to swap
                            blocks.get(i).y--;
                            return;
                        }
                break;
        }
    }

    @SuppressWarnings("Duplicates")
    private void makeMoveObstacle(char move) {

        switch (move) {
            case 'U': //up  x--
                //checking if the agent is over a block
                //it can be over at most a block

                blocks.get(0).x--;
                for (int i = 1; i <= noBlocks; i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) {//they have to swap
                            blocks.get(0).x--;
                            blocks.get(i).x++;
                            return;
                        }

                //checks if there is an immovable block
                for (int i = noBlocks + 1; i < blocks.size(); i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) {  //found block
                            blocks.get(0).x++;  //agent goes back
                            return;
                        }
                break;

            case 'D': //down x++
                blocks.get(0).x++;
                //checking if the agent is over a block
                //it can be over at most a block
                for (int i = 1; i <= noBlocks; i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) {  //they have to swap
                            blocks.get(i).x--;
                            return;
                        }

                //checks if there is an immovable block
                for (int i = noBlocks + 1; i < blocks.size(); i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) { //found block
                            blocks.get(0).x--;  //agent goes back
                            return;
                        }
                break;

            case 'L': //left y--
                blocks.get(0).y--;
                //checking if the agent is over a block
                //it can be over at most a block
                for (int i = 1; i <= noBlocks; i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) {  //they have to swap
                            blocks.get(i).y++;
                            return;
                        }

                //checks if there is an immovable block
                for (int i = noBlocks + 1; i < blocks.size(); i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) { //found block
                            blocks.get(0).y++;  //agent goes back
                            return;
                        }
                break;

            case 'R': //right y++
                blocks.get(0).y++;
                //checking if the agent is over a block
                //it can be over at most a block
                for (int i = 1; i <= noBlocks; i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) {  //they have to swap
                            blocks.get(i).y--;
                            return;
                        }

                //checks if there is an immovable block
                for (int i = noBlocks + 1; i < blocks.size(); i++)
                    if (blocks.get(i).y == blocks.get(0).y)
                        if (blocks.get(i).x == blocks.get(0).x) { //found block
                            blocks.get(0).y--;  //agent goes back
                            return;
                        }
                break;
        }
    }

    //i assume input is valid and block is not over a start state or final state
    public void addImmovableBlock(int x, int y) {

        //it's over a start state
        if (x == size - 1)
            return;
        //it's over an end state
        if (y == 1 && x >= (size - noBlocks))
            return;

        blocks.add(new Block('i', x, y));
    }

    //used to test
    public void printNodes() {

        char[][] arr = new char[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                arr[i][j] = '0';

        for (int i = 0; i < blocks.size(); i++)
            arr[blocks.get(i).x][blocks.get(i).y] = blocks.get(i).name;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                System.out.print(arr[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }

    public boolean isSolution() {

        int tmp = size - noBlocks;

        for (int i = 1; i <= noBlocks; i++)
            if (!(blocks.get(i).y == 1 && (blocks.get(i).name - 'A' + tmp) == blocks.get(i).x))
                return false;

        return true;
    }

    public ArrayList<Node> getFastMoves() {

        ArrayList<Node> moves = new ArrayList<Node>();
        Block agent = blocks.get(0);

        if ((agent.x > 0) && lastMove != 'D')
            moves.add(new Node(this, 'U'));

        if ((agent.y > 0) && lastMove != 'R')
            moves.add(new Node(this, 'L'));

        if ((agent.y < size - 1) && lastMove != 'L')
            moves.add(new Node(this, 'R'));

        if ((agent.x < size - 1) && lastMove != 'U')
            moves.add(new Node(this, 'D'));

        return moves;
    }

    @SuppressWarnings("Duplicate")
    public ArrayList<Node> getMoves() {

        ArrayList<Node> moves = new ArrayList<Node>();
        Block agent = blocks.get(0);

        if (agent.x > 0)
            moves.add(new Node(this, 'U'));

        if (agent.y > 0)
            moves.add(new Node(this, 'L'));

        if (agent.y < size - 1)
            moves.add(new Node(this, 'R'));

        if (agent.x < size - 1)
            moves.add(new Node(this, 'D'));

        return moves;
    }

    public ArrayList<Node> getMovesObstacle() {

        ArrayList<Node> moves = new ArrayList<Node>();
        Block agent = blocks.get(0);

        if (agent.x > 0 && lastMove != 'D')
            moves.add(new Node(this, 'U', true));

        if (agent.y > 0 && lastMove != 'R')
            moves.add(new Node(this, 'L', true));

        if ((agent.y < size - 1) && lastMove != 'L')
            moves.add(new Node(this, 'R', true));

        if ((agent.x < size - 1) && lastMove != 'U')
            moves.add(new Node(this, 'D', true));

        return moves;
    }

    //cost to another node
    //used in case there is a different final state
    public int calculateCost(Node dest) {

        int cost = 0, costBlock;
        ArrayList<Block> a = this.getBlocks();
        ArrayList<Block> b = dest.getBlocks();
        for (int i = 1; i < a.size(); i++) {

            costBlock = Math.abs(a.get(i).x - b.get(i).x) + Math.abs(a.get(i).y - b.get(i).y);
            cost += costBlock;
        }

        cost += depth;
        this.cost = cost;
        return cost;
    }

    //cost to solution
    public int calculateCost() {

        int cost = 0, costBlock;
        ArrayList<Block> a = this.getBlocks();

        for (int i = 1; i < a.size(); i++) {

            costBlock = Math.abs(a.get(i).x - (i + size - noBlocks - 1)) + Math.abs(a.get(i).y - 1);
            cost += costBlock;
        }

        cost += depth;
        this.cost = cost;
        return cost;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public char getLastMove() {
        return lastMove;
    }

    public Node getParent() {
        return parent;
    }

    public int getSize() {
        return size;
    }

    public int getNoBlocks() {
        return noBlocks;
    }

    public int getCost() {
        return cost;
    }

    //used to test
    public void setBlock(int letter, int x, int y) {
        blocks.set(letter, new Block((char) ('A' + letter - 1), x, y));
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public int compareTo(Object n) {
        Node tmp = (Node) n;
        return this.getCost() - tmp.getCost();
    }

    public int hashCode() {
        int sum = blocks.get(0).hashCode();
        for (Block i : blocks)
            sum = 7 * sum + i.hashCode();
        return sum;
    }
}