package com.company;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Directed, weighted graph representation
 */

/**Constructor of an empty graph*/
public class Graph {
    private int nrOfVertices;
    private int nrOfEdges;
    private final LinkedList<Vertex> verticesList;

    Graph() {
        nrOfVertices = 0;
        nrOfEdges = 0;
        verticesList = new LinkedList<>();
    }

    /**Clear the graph*/
    void clear(){
        nrOfVertices=0;
        nrOfEdges=0;
        verticesList.clear();
    }

    /**
     * Add edge by names of vertices, if vertex doesn't exist create a new one
     */
    void addEdge(String v, String u, int weight) {
        Vertex ver1, ver2;
        if (weight < 1)
            throw new IllegalArgumentException();
        if (!contains(v)) {
            ver1 = add(v);
            nrOfVertices++;
        } else
            ver1 = get(v);
        if (!contains(u)) {
            ver2 = add(u);
            nrOfVertices++;
        } else
            ver2 = get(u);
        ver1.addEdge(ver2.getNr(), weight);
        nrOfEdges++;
    }

    /**
     * Add vertex by name, nr of vertex is created automatically
     */
    @SuppressWarnings("unchecked for duplicates")
    Vertex add(String name) {
        Vertex vertex = new Vertex(nrOfVertices, name);
        verticesList.add(vertex);
        nrOfVertices++;
        return vertex;
    }

    /**
     * Check is graph contains vertex of given number
     */
    @Contract(pure = true)
    private boolean contains(int v) {
        return validateVertex(v);
    }

    /**
     * Check for containnig vertex of given name
     */
    private boolean contains(String s) {
        return validateVertex(s);
    }

    /**
     * Return vertex of given number
     */
    @Nullable
    private Vertex get(int nr) {
        if (validateVertex(nr))
            return verticesList.get(nr);
        return null;
    }

    /**
     * Return vertex of given name
     */
    @Nullable
    private Vertex get(String s) {
        for (Vertex v : verticesList)
            if (v.getData().equals(s))
                return v;
        return null;
    }

    /**
     * Check if vertex of given number is correct graph number
     */
    @Contract(pure = true)
    private boolean validateVertex(int v) {
        return (v >= 0 && v < nrOfVertices);
    }

    /**
     * Check if vertex of given name is correct graph vertex
     */
    private boolean validateVertex(String s) {
        for (Vertex v : verticesList)
            if (v.getData().equals(s))
                return true;
        return false;
    }

    /**
     * Find minimum spanning tree
     */
    void minSpanningTree(int begin) {
        if (!validateVertex(begin))
            throw new IllegalArgumentException();
        PrimsAlgorithm primsAlgorithm = new PrimsAlgorithm(this);
        primsAlgorithm.prim(verticesList.get(1));
        System.out.println("Połączenia pomiedzy węzłami dla wprowadzonych danych:");
        primsAlgorithm.getMinPath(1);
        System.out.print("Wymagana długość kabla: "+primsAlgorithm.minCableLength()+"m\n\n");
    }

    @Override
    public String toString() {
        for (Vertex v : verticesList) {
            System.out.print(v + v.getAdjList());
        }
        return "Directed weighted graph";
    }

    /**
     * Private class for representing vertex of a graph
     * Contains Vertex nr ,name, nr of edges to other vertices
     * and weight.
     * Adjacency list is given as a TreeMap - Key is
     * a nr of vertex to which we can travel and a Value
     * is a weight of a path
     */
    private class Vertex implements Comparable<Vertex> {
        private final int nr;
        private final String data;
        private int edges;
        private int weight;
        private final TreeMap<Integer, Integer> adj;

        /**
         * Create empty vertex
         */
        Vertex(int nr, String data) {
            this.nr = nr;
            this.data = data;
            edges = 0;
            adj = new TreeMap<>();
        }

        /**
         * Add edge between  this vertex and other vertex with given weight
         */
        void addEdge(int vertex, int weight) {
            if (vertex < 0 || weight < 1) throw new IllegalArgumentException();
            adj.put(vertex, weight);
            edges++;
        }


        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(this.weight, o.getWeight());
        }

        @Override
        public boolean equals(Object obj) {
            Vertex ver = (Vertex) obj;
            return getData().equals(ver.getData());
        }

        @Override
        public String toString() {
            String s = " Miasto: " + data;
            return s;
        }


        /**
         * Getters
         */
        int getWeight() {
            return weight;
        }

        String getAdjList() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n Możemy się dostać bezpośrednio do: \n");
            adj.forEach((key, value) -> {
                stringBuilder.append(verticesList.get(key));
                stringBuilder.append(" ");
                stringBuilder.append(value);
                stringBuilder.append(" km\n");
            });
            stringBuilder.append("\n");
            return stringBuilder.toString();
        }

        int getNr() {
            return nr;
        }

        String getData() {
            return data;
        }

        public int getEdges() {
            return edges;
        }

        TreeMap<Integer, Integer> getAdj() {
            return adj;
        }

        /**
         * Setter
         */
        void setWeight(int weight) {
            this.weight = weight;
        }
    }

    /**Private class used for finding a minimum spanning tree
     * Contains graph, Priority Queue of all vertices to compute
     * shortest path and an array of predecessors of given vertices
     * on the shortest path. By travelling from the destination city to its
     * predecessors we finally reach the beginning city - if the road exists
     * in the graph
     * */
    private class PrimsAlgorithm{
        private final Graph graph;
        private final PriorityQueue<Vertex> queue;
        private final int [] pred;

        /**Creates an empty Prims's algorithm class instance*/
        PrimsAlgorithm(Graph g){
            graph=g;
            queue=new PriorityQueue<>();
            pred=new int[graph.nrOfVertices];
        }

        /**Set the weight od all vertices to infinite,
         * @param s root
         * set the root weight to 0 - arrival city
         */
        private  void initializeSingleSource(int s){
            for (Vertex v : verticesList){
                v.setWeight(Integer.MAX_VALUE);
            }
            verticesList.get(s).setWeight(0);
        }

        /**Check whether it is possible to reach given city
         * on shortest path than given in its weight
         * @param u root
         * @param v city on root adjacency's list
         * @param weight weight of a path between root and adjacency's list city
         */
        private void relax(int u, int v, int weight){
            if (queue.contains(verticesList.get(v)) &&
            weight<verticesList.get(v).getWeight()) {
                    pred[v] = u;
                    verticesList.get(v).setWeight(weight);
                    queue.remove(verticesList.get(v));
                    queue.add(verticesList.get(v));
                }
            }


        /** Prim's algorithm. Add all vertices to the priority queue
         * search for the the smallest based on Prims's weight
         * poll it from the queue and RELAX
         * @param v
         */
        void prim(Vertex v){
            initializeSingleSource(v.getNr());
            queue.addAll(verticesList);
            while (!queue.isEmpty()){
                Vertex vertex=queue.poll();
                for (Map.Entry<Integer,Integer> entry : vertex.getAdj().entrySet()){
                    Integer other=entry.getKey();
                    Integer weight=entry.getValue();
                    relax(vertex.getNr(),other,weight);
                }
            }

        }

        /**Sum weights of all vertices*/
        String minCableLength() {
            int sum=0;
            for (Vertex ver : verticesList){
            if (ver.getWeight()>0 && ver.getWeight()<Integer.MAX_VALUE)
            sum+=ver.getWeight();
            }
            return Integer.toString(sum);
        }


        /**Print the predecessor of each vertex*/
        void getMinPath(int begin){
            for (int i=2;i<verticesList.size();i++) {
                System.out.println(pred[i]+" "+verticesList.get(i).getData()
                +" "+verticesList.get(i).getWeight()+"m");
            }
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder=new StringBuilder();
            for (Vertex ver : verticesList){
                stringBuilder.append(ver);
                stringBuilder.append(" Waga: ");
                stringBuilder.append(ver.weight);
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
    }

}
