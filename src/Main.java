import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

//-Xmx16G jvm option needed/or at least more than default for size 4

public class Main {

    static BufferedWriter fout;
    static long nodes;
    static long milisToRun = 1000 * 15 * 1, startTime;

    public static void main(String[] args) throws Exception {


        FileWriter fw = new FileWriter("out.txt");
        fout = new BufferedWriter(fw);
        Node s;

        boolean bfs = true, dfs = true, ids = true, astar = true;
        int j = 3; //default number of blocks

        //no extras
        //increases the size until all search methods fail

        //to add extra features change the name of the search methods by adding an prefix
        //an i for when there are immovable objects or f for graph search
        //so change bfs(s) to fbfs(s) or ibfs()
        //bfs=ids=astar=false;

        for (int i = 4; bfs||dfs||ids||astar ;i++){


            //s.addImmovableBlock( 0,i-1);
            s = new Node(i, j);

            fout.write('\n' + "Size is " + i + 'X' + i + '\n' + "Number of Blocks is " + j + '\n');

            //System.out.println(i);
            if (bfs)
                try {
                    bfs(s);
                } catch (Exception e) {
                    bfs = false;
                    fout.write("BFS failed because " + e.getMessage() + '\n');
                }
            if (dfs)
                try {
                    dfs(s);
                } catch (Exception e) {
                    dfs = false;
                    fout.write("DFS failed because " + e.getMessage() + '\n');
                }
            if (ids)
                try {
                    iterativeDeepening(s);
                } catch (Exception e) {
                    ids = false;
                    fout.write("IDS failed because " + e.getMessage() + '\n');
                }
            if (astar)
                try {
                    astar(s);
                } catch (Exception e) {
                    astar = false;
                    fout.write("A* failed because " + e.getMessage() + '\n');
                }
        }

        fout.close();
        fw.close();
    }


    //breadth first search
    static void bfs(Node start) throws Exception {

        Queue<Node> q = new LinkedList<>();
        ArrayList<Node> moves;
        Node n;
        startTime = System.currentTimeMillis();
        nodes = 0;
        q.offer(start);

        while (!q.isEmpty()) {

            nodes++;
            n = q.poll();

            if ((System.currentTimeMillis() - startTime) > milisToRun)
                throw new Exception(" 15 minute exceeded ");

            if (n.isSolution()) {
                long time = System.currentTimeMillis() - startTime;
                fout.write('\n' + "BFS time = " + time + " ms" + '\n' + "Nodes expanded : " + nodes + '\n');
                printSol(foundSol(n));
                return;
            }

            moves = n.getMoves();
            for (Node c : moves) q.offer(c);
        }

    }

    //depth first search
    static void dfs(Node start) throws Exception {

        ArrayList<Node> moves;
        Node n = start;
        startTime = System.currentTimeMillis();
        nodes = 0;
        while (true) {

            nodes++;

            if ((System.currentTimeMillis() - startTime) > milisToRun)
                throw new Exception(" 15 minute exceeded ");

            if (n.isSolution()) {
                long time = System.currentTimeMillis() - startTime;
                fout.write('\n' + "DFS time = " + time + " ms" + '\n' + "Nodes expanded : " + nodes + '\n');
                //commented because the output file gets too big
                //printSol(foundSol(n));
                return;
            }

            moves = n.getMoves();
            Collections.shuffle(moves);
            n = moves.get(0);

        }
    }

    //iterative deepening
    static void iterativeDeepening(Node start) throws Exception {

        Node found;
        int depth = 0;
        startTime = System.currentTimeMillis();
        nodes = 0;

        while (true) {

            found = dls(start, depth);

            if (found != null && found.isSolution()) {
                long time = System.currentTimeMillis() - startTime;
                fout.write('\n' + "IDS time = " + time + " ms" + '\n' + "Nodes expanded : " + nodes + '\n');
                printSol(foundSol(found));
                return;
            }

            depth++;

        }
    }

    static Node dls(Node n, int depth) throws Exception {

        nodes++;
        ArrayList<Node> moves;
        Node found;

        if ((System.currentTimeMillis() - startTime) > milisToRun)
            throw new Exception(" 15 minute exceeded ");

        //n is solution
        if (depth == 0 && n.isSolution())
            return n;

        if (depth > 0) {

            moves = n.getMoves();

            for (Node node : moves) {

                found = dls(node, depth - 1);

                //a solution was found, stopping the search
                if (found != null)
                    return found;
            }
        }
        return null;
    }

    //A*
    static void astar(Node start) throws Exception {

        ArrayList<Node> moves;
        Queue<Node> q = new PriorityQueue<>();
        Node tmp;
        nodes = 0;
        startTime = System.currentTimeMillis();
        start.setDepth(0);
        start.calculateCost();
        q.offer(start);
        while (!q.isEmpty()) {

            tmp = q.poll();
            nodes++;

            if ((System.currentTimeMillis() - startTime) > milisToRun)
                throw new Exception(" 15 minute exceeded ");

            if (tmp.isSolution()) {
                long time = System.currentTimeMillis() - startTime;
                fout.write('\n' + "A* time = " + time + " ms" + '\n' + "Nodes expanded : " + nodes + '\n');
                printSol(foundSol(tmp));
                return;
            }

            moves = tmp.getMoves();

            for (Node node : moves) {

                node.setDepth(tmp.getDepth() + 1);
                node.calculateCost();
                q.offer(node);

            }
        }
    }

    //improved getMoves and graph search bfs
    static void fbfs(Node start) throws Exception {


        //needed for graph search
        HashSet<Node> uniqueNodes = new HashSet<>();
        uniqueNodes.add(start);
        Queue<Node> q = new LinkedList<>();
        ArrayList<Node> moves;
        Node n;
        startTime = System.currentTimeMillis();
        nodes = 0;
        q.offer(start);

        while (!q.isEmpty()) {

            nodes++;
            n = q.poll();

            if ((System.currentTimeMillis() - startTime) > milisToRun)
                throw new Exception(" 15 minute exceeded ");

            if (n.isSolution()) {
                long time = System.currentTimeMillis() - startTime;
                fout.write('\n' + "BFS time = " + time + " ms" + '\n' + "Nodes expanded : " + nodes + '\n');
                printSol(foundSol(n));
                return;
            }

            moves = n.getFastMoves();

            for (Node c : moves)
                if (!uniqueNodes.contains(c) && !q.contains(c)) {     //graph search

                    q.offer(c);
                    uniqueNodes.add(c);
                }

        }

    }

    //improved getMoves and graph search dfs
    static void fdfs(Node start) throws Exception {

        //for graph search
        HashSet<Node> uniqueNodes = new HashSet<>();
        uniqueNodes.add(start);
        Stack<Node> stack = new Stack<>();
        ArrayList<Node> moves;
        Node n;
        startTime = System.currentTimeMillis();
        nodes = 0;
        stack.push(start);

        while (!stack.isEmpty()) {

            nodes++;
            n = stack.pop();

            if ((System.currentTimeMillis() - startTime) > milisToRun)
                throw new Exception(" 15 minute exceeded ");

            if (n.isSolution()) {
                long time = System.currentTimeMillis() - startTime;
                fout.write('\n' + "DFS time = " + time + " ms" + '\n' + "Nodes expanded : " + nodes + '\n');
                //commented because the output file gets too big
                //printSol(foundSol(n));
                return;
            }

            moves = n.getFastMoves();
            Collections.shuffle(moves);
            for (Node c : moves)
                if (!uniqueNodes.contains(c)) {   //graph search
                    stack.push(c);
                    uniqueNodes.add(c);
                }
        }
    }

    //improved getMoves and graph search A*
    static void fastar(Node start) throws Exception {

        ArrayList<Node> moves;
        Queue<Node> q = new PriorityQueue<>();
        //for graph search
        //upon further tests i found out it slowed the search
        //HashSet<Node> uniqueNodes = new HashSet<>();
        //uniqueNodes.add(start);
        Node tmp;
        nodes = 0;
        startTime = System.currentTimeMillis();
        start.setDepth(0);
        start.calculateCost();
        q.offer(start);

        while (!q.isEmpty()) {

            tmp = q.poll();
            nodes++;

            if ((System.currentTimeMillis() - startTime) > milisToRun)
                throw new Exception(" 15 minute exceeded ");

            if (tmp.isSolution()) {
                long time = System.currentTimeMillis() - startTime;
                fout.write('\n' + "A* time = " + time + " ms" + '\n' + "Nodes expanded : " + nodes + '\n');
                printSol(foundSol(tmp));
                return;
            }

            moves = tmp.getFastMoves();

            for (Node node : moves)
                if (!q.contains(node))   //for graph search
                {
                    node.setDepth(tmp.getDepth() + 1);
                    node.calculateCost();
                    q.offer(node);
                    //uniqueNodes.add(node);
                }

        }
    }

    //breadth first search default with immovable objects
    @SuppressWarnings("Duplicates")
    static void ibfs(Node start) throws Exception {

        //needed for graph search
        HashSet<Node> uniqueNodes = new HashSet<>();
        uniqueNodes.add(start);
        Queue<Node> q = new LinkedList<>();
        ArrayList<Node> moves;
        Node n;
        startTime = System.currentTimeMillis();
        nodes = 0;
        q.offer(start);

        while (!q.isEmpty()) {

            nodes++;
            n = q.poll();

            if ((System.currentTimeMillis() - startTime) > milisToRun)
                throw new Exception(" 15 minute exceeded ");

            if (n.isSolution()) {
                long time = System.currentTimeMillis() - startTime;
                fout.write('\n' + "BFS time = " + time + " ms" + '\n' + "Nodes expanded : " + nodes + '\n');
                printSol(foundSol(n));
                return;
            }

            moves = n.getMovesObstacle();

            for (Node c : moves)
                if (!uniqueNodes.contains(c) && !q.contains(c)) {     //graph search

                    q.offer(c);
                    uniqueNodes.add(c);
                }

        }

    }

    //depth first search default with immovable objects
    @SuppressWarnings("Duplicates")
    static void idfs(Node start) throws Exception {

        //for graph search
        HashSet<Node> uniqueNodes = new HashSet<>();
        uniqueNodes.add(start);
        Stack<Node> stack = new Stack<>();
        ArrayList<Node> moves;
        Node n;
        startTime = System.currentTimeMillis();
        nodes = 0;
        stack.push(start);

        while (!stack.isEmpty()) {

            nodes++;
            n = stack.pop();

            if ((System.currentTimeMillis() - startTime) > milisToRun)
                throw new Exception(" 15 minute exceeded ");

            if (n.isSolution()) {
                long time = System.currentTimeMillis() - startTime;
                fout.write('\n' + "DFS time = " + time + " ms" + '\n' + "Nodes expanded : " + nodes + '\n');
                //commented because the output file gets too big
                printSol(foundSol(n));
                return;
            }

            moves = n.getMovesObstacle();

            Collections.shuffle(moves);
            for (Node c : moves)
                if (!uniqueNodes.contains(c)) {   //graph search
                    stack.push(c);
                    uniqueNodes.add(c);
                }
        }
    }


    //A* default with immovable objects
    @SuppressWarnings("Duplicates")
    static void iastar(Node start) throws Exception {

        ArrayList<Node> moves;
        Queue<Node> q = new PriorityQueue<>();
        //for graph search
        HashSet<Node> uniqueNodes = new HashSet<>();
        uniqueNodes.add(start);
        Node tmp;
        nodes = 0;
        startTime = System.currentTimeMillis();
        start.setDepth(0);
        start.calculateCost();
        q.offer(start);

        while (!q.isEmpty()) {

            tmp = q.poll();
            nodes++;

            if ((System.currentTimeMillis() - startTime) > milisToRun)
                throw new Exception(" 15 minute exceeded ");

            if (tmp.isSolution()) {
                long time = System.currentTimeMillis() - startTime;
                fout.write('\n' + "A* time = " + time + " ms" + '\n' + "Nodes expanded : " + nodes + '\n');
                printSol(foundSol(tmp));
                return;
            }

            moves = tmp.getMovesObstacle();

            for (Node node : moves)
                if (!uniqueNodes.contains(node) && !q.contains(node))   //for graph search
                {
                    node.setDepth(tmp.getDepth() + 1);
                    node.calculateCost();
                    q.offer(node);
                    uniqueNodes.add(node);
                }

        }
    }

    //auxiliary functions

    static void printSol(Stack<Character> solution) throws Exception {
        fout.write("Solution path is : ");
        while (!solution.isEmpty())
            fout.write(solution.pop() + " ");
        fout.write('\n');
    }

    //puts the solution in a stack
    //as it is written end to beginning
    //and stack is lifo
    static Stack<Character> foundSol(Node n) {

        Stack<Character> solution = new Stack<>();
        while (n.getParent() != null) {
            solution.add(n.getLastMove());
            n = n.getParent();
        }
        return solution;
    }

}

