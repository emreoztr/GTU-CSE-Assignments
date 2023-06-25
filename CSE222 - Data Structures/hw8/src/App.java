import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;


public class App {
    public static void main(String[] args) throws Exception {
        generalizedDikstraDriver();
        System.out.println();
        connectedComponentDriver();
        System.out.println();
        importanceValueDriver();
         
    }

    public static void importanceValueDriver(){
        System.out.println("************Importance Value Driver************");

        Graph g = new MatrixGraph(5, false);

        g.insert(new Edge(0, 1));
        g.insert(new Edge(2, 1));
        g.insert(new Edge(0,3));
        g.insert(new Edge(3, 2));
        g.insert(new Edge(0,4));
        g.insert(new Edge(4, 2));

        System.out.println("Test with manuel inputs:");
        System.out.println("Edges: 0<->1, 1<->2, 0<->3, 3<->2, 0<->4, 4<->2");
        System.out.println("Should give 0.06 for 0, 0.013 for 1, 0.06 for 2, 0.013 for 3, 0.013 for 4");
        double[] ans = ImportanceValue.importanceValueForEach(g);

        for(int i = 0; i < g.getNumV(); ++i)
            System.out.println(ans[i] + ", ");
        
        System.out.println("Test with 200 element graph with maximum 10 conencted components: ");
        Graph g2 = createGraph(10, 200);
        ImportanceValue.importanceValueForEach(g2);
        System.out.println("Test passed");

        System.out.println("Sending null to the method:");
        try{
            ImportanceValue.importanceValueForEach(null);
        }catch(NullPointerException e){
            System.out.println("Exception has thrown, test passed!");
        }

        Graph g3 = new ListGraph(5, true);

        System.out.println("Trying to send directed graph to method: ");
        try{
            ImportanceValue.importanceValueForEach(g3);
        }catch(InvalidParameterException e){
            System.out.println("Exception has thrown, test passed!");
        }
    }

    public static void generalizedDikstraDriver(){
        System.out.println("************Generalized Dijkstra Driver************");
        System.out.println("MATRIX GRAPH");
        System.out.println("Test with manuel inputs:");
        System.out.println("3 weight for each edge and 5 vertex.");
        System.out.println("Edges: 0<->1 (5.1, 5.0, 5.2), 2<->1(6.2, 6.1, 6.0), 3<->1(7.0, 7.2, 7.1), 4<->1(8.2, 8.0, 8.1)");
        GeneralizedGraph gg = new GeneralizedMatrixGraph(5, false, 3);
        double[] d = new double[3];
        d[0] = 51.1;
        d[1] = 50.0;
        d[2] = 52.2;
        gg.insert(new GeneralizedEdge(0, 1, d));

        d = new double[3];
        d[0] = 62.2;
        d[1] = 61.1;
        d[2] = 60.0;
        gg.insert(new GeneralizedEdge(2, 1, d));

        d = new double[3];
        d[0] = 70.0;
        d[1] = 72.2;
        d[2] = 71.1;
        gg.insert(new GeneralizedEdge(3, 1, d));

        d = new double[3];
        d[0] = 82.2;
        d[1] = 80.0;
        d[2] = 81.1;
        gg.insert(new GeneralizedEdge(4, 1, d));

        Iterator<GeneralizedEdge> e = gg.edgeIterator(1);
        System.out.println("Printing edges with iterator, takes vertex 1 as starting point:");
        while(e.hasNext()){
            System.out.println(e.next());
        }

        System.out.println("Testing this graph with Generalized Dijkstra:");
        d = new double[gg.getNumV()];
        int[] p;
        for(int i = 0; i < 3; ++i){
            p = new int[gg.getNumV()];
            GeneralizedDijkstra.dijkstra(gg, 0, i, new OperatorCreator(){
                @Override
                public double operation(double num1, double num2) {
                    return num1 + num2;
                }
            }, d, p);
            
            System.out.println("Length of 0th element to remaining elements with add operator (with weight" + i + "): ");
            for(int j = 0; j < gg.getNumV(); ++j)
                System.out.print(d[j] + ", ");
            System.out.println("\n");

            System.out.println("Length of 0th element to remaining elements with multiplication operator (with weight" + i + "): ");
            p = new int[gg.getNumV()];
            GeneralizedDijkstra.dijkstra(gg, 0, i, new OperatorCreator(){
                @Override
                public double operation(double num1, double num2) {
                    return num1 * num2;
                }
            }, d, p);
            for(int j = 0; j < gg.getNumV(); ++j)
                System.out.print(d[j] + ", ");
            System.out.println("\n");

            System.out.println("Length of 0th element to remaining elements with star operator (with weight" + i + "): ");
            p = new int[gg.getNumV()];
            GeneralizedDijkstra.dijkstra(gg, 0, i, new OperatorCreator(){
                @Override
                public double operation(double num1, double num2) {
                    return (num1 + num2) - num1*num2;
                }
            }, d, p);
            for(int j = 0; j < gg.getNumV(); ++j)
                System.out.print(d[j] + ", ");
            System.out.println("\n");
        }
        System.out.println("LIST GRAPH");
        System.out.println("Test with manuel inputs:");
        System.out.println("3 weight for each edge and 5 vertex.");
        System.out.println("Edges: 0<->1 (5.1, 5.0, 5.2), 2<->1(6.2, 6.1, 6.0), 3<->1(7.0, 7.2, 7.1), 4<->1(8.2, 8.0, 8.1)");
        gg = new GeneralizedListGraph(5, false, 3);
        d = new double[3];
        d[0] = 51.1;
        d[1] = 50.0;
        d[2] = 52.2;
        gg.insert(new GeneralizedEdge(0, 1, d));

        d = new double[3];
        d[0] = 62.2;
        d[1] = 61.1;
        d[2] = 60.0;
        gg.insert(new GeneralizedEdge(2, 1, d));

        d = new double[3];
        d[0] = 70.0;
        d[1] = 72.2;
        d[2] = 71.1;
        gg.insert(new GeneralizedEdge(3, 1, d));

        d = new double[3];
        d[0] = 82.2;
        d[1] = 80.0;
        d[2] = 81.1;
        gg.insert(new GeneralizedEdge(4, 1, d));

        e = gg.edgeIterator(1);
        System.out.println("Printing edges with iterator, takes vertex 1 as starting point:");
        while(e.hasNext()){
            System.out.println(e.next());
        }

        System.out.println("Testing this graph with Generalized Dijkstra:");
        d = new double[gg.getNumV()];
        p = new int[gg.getNumV()];
        for(int i = 0; i < 3; ++i){
            GeneralizedDijkstra.dijkstra(gg, 0, i, new OperatorCreator(){
                @Override
                public double operation(double num1, double num2) {
                    return num1 + num2;
                }
            }, d, p);
            
            System.out.println("Length of 0th element to remaining elements with add operator (with weight" + i + "): ");
            for(int j = 0; j < gg.getNumV(); ++j)
                System.out.print(d[j] + ", ");
            System.out.println("\n");

            System.out.println("Length of 0th element to remaining elements with multiplication operator (with weight" + i + "): ");
            p = new int[gg.getNumV()];
            GeneralizedDijkstra.dijkstra(gg, 0, i, new OperatorCreator(){
                @Override
                public double operation(double num1, double num2) {
                    return num1 * num2;
                }
            }, d, p);
            for(int j = 0; j < gg.getNumV(); ++j)
                System.out.print(d[j] + ", ");
            System.out.println("\n");

            System.out.println("Length of 0th element to remaining elements with star operator (with weight" + i + "): ");
            p = new int[gg.getNumV()];
            GeneralizedDijkstra.dijkstra(gg, 0, i, new OperatorCreator(){
                @Override
                public double operation(double num1, double num2) {
                    return (num1 + num2) - num1*num2;
                }
            }, d, p);
            for(int j = 0; j < gg.getNumV(); ++j)
                System.out.print(d[j] + ", ");
            System.out.println("\n");
        }

        System.out.println("Trying to send null to the dijkstra: ");
        try{
            GeneralizedDijkstra.dijkstra(null, 0, 0, null, null, null);
        }catch(NullPointerException exc){
            System.out.println("Exception has thrown, test passed!");
        }
    }

    private static Graph createGraph(int count, int size) {
        Random rand = new Random();
        Graph g = new MatrixGraph(size, false);

        ArrayList<Integer> numbers = new ArrayList<>();
        ArrayList<ArrayList<Integer>> comps = new ArrayList<>();
        int avr = size / count;

        int fsize = size;

        for (int i = 0; i < size; ++i)
            numbers.add(i);

        Collections.shuffle(numbers);
        int numInd = 0;
        for (int i = 0; i < count - 1; ++i) {
            int compSize = rand.nextInt(size / (count - i)) + 2;
            ArrayList<Integer> comp = new ArrayList<>();

            for (int j = 0; j < compSize; ++j)
                comp.add(numbers.get(numInd++));

            size -= comp.size();

            comps.add(comp);
        }
        ArrayList<Integer> comp = new ArrayList<>();
        for (int i = 0; i < size; ++i)
            comp.add(numbers.get(numInd++));

        comps.add(comp);

        for (ArrayList<Integer> c : comps) {
            for (int i = 0; i < c.size() - 1; ++i)
                g.insert(new Edge(c.get(i), c.get(i + 1)));

            int otherCon = rand.nextInt(c.size() / 2);
            for (int i = 0; i < otherCon; ++i) {
                int first = rand.nextInt(c.size());
                int sec;
                do {
                    sec = rand.nextInt(c.size());
                } while (first != sec);
                g.insert(new Edge(c.get(first), c.get(sec)));
            }
        }

        return g;
    }

    private static long[] averageTime(int size){
        long startTime;
        long endTime;
        Graph g;
        long[] returnVal = new long[2];
        for(int i = 0; i < 10; ++i){
            g = createGraph(size/5, size);
            startTime = System.nanoTime();
            ConnectedComponentFinders.findWithBFS(g);
            endTime = System.nanoTime();
            returnVal[0] += ((endTime - startTime) / 10);
        }

        for(int i = 0; i < 10; ++i){
            g = createGraph(size/5, size);
            startTime = System.nanoTime();
            ConnectedComponentFinders.findWithDFS(g);
            endTime = System.nanoTime();
            returnVal[1] += ((endTime - startTime) / 10);
        }

        return returnVal;
    }

    public static void connectedComponentDriver(){
        System.out.println("**********Connected Component Driver**********");
        System.out.println("----------1000 ELEMENTS GRAPHS----------------");
        long[] result = averageTime(1000);
        System.out.println("Connected Component Finder with BFS: " + (double) result[0] / 1000000);
        System.out.println("Connected Component Finder with DFS: " +  (double)result[1]/ 1000000);
        System.out.println("----------2000 ELEMENTS GRAPHS----------------");
        result = averageTime(2000);
        System.out.println("Connected Component Finder with BFS: " +  (double)result[0]/ 1000000);
        System.out.println("Connected Component Finder with DFS: " +  (double)result[1]/ 1000000);
        System.out.println("----------5000 ELEMENTS GRAPHS----------------");
        result = averageTime(5000);
        System.out.println("Connected Component Finder with BFS: " +  (double)result[0]/ 1000000);
        System.out.println("Connected Component Finder with DFS: " +  (double)result[1]/ 1000000);
        System.out.println("----------10000 ELEMENTS GRAPHS----------------");
        result = averageTime(10000);
        System.out.println("Connected Component Finder with BFS: " +  (double)result[0]/ 1000000);
        System.out.println("Connected Component Finder with DFS: " +  (double)result[1]/ 1000000);
    }
}
