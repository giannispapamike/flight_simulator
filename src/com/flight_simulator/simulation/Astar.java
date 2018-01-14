package com.flight_simulator.simulation;

import com.flight_simulator.data.Flight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Astar {

    private Node start = new Node(), goal = new Node(), current = null, neighbor = null, first = new Node(), last = new Node();
    private Node[][] map = new Node[30][60];
    private int[][] in_map;
    private ArrayList<Node> path = new ArrayList<>(), closedList = new ArrayList<>();
    private Comparator<Node> comparator = new nodeComparator();
    private PriorityQueue<Node> openList = new PriorityQueue<>(10, comparator);
    private Flight flight;
    private int N = 30, M = 60;

    public Astar(int startX, int startY, int endX, int endY, int[][] map, Flight flight, int takeOffOrientation, int landOrientation) {

        this.first.x = startX;
        this.first.y = startY;
        this.first.height = map[startY][startX];
        this.last.x = endX;
        this.last.y = endY;
        this.last.height = map[endY][endX];

        this.start.x = startX;
        this.start.y = startY;
        this.goal.x = endX;
        this.goal.y = endY;

        this.in_map = map;
        this.flight = flight;

        updateStart(takeOffOrientation);
        updateGoal(landOrientation);
        initMap();
        run();
    }

    private void updateStart(int orientation) {
        switch (orientation) {
            case 1: if (this.start.y > 0) this.start.y--;
                break;
            case 2: if (this.start.x < 59) this.start.x++;
                break;
            case 3: if (this.start.y < 29) this.start.y++;
                break;
            case 4: if (this.start.x > 0) this.start.x--;
                break;
            default: break;
        }
    }

    private void updateGoal(int orientation) {
        switch (orientation) {
            case 1: if (this.start.y > 0) this.goal.y--;
                break;
            case 2: if (this.start.x < 59) this.goal.x++;
                break;
            case 3: if (this.start.y < 29) this.goal.y++;
                break;
            case 4: if (this.start.x > 0) this.goal.x--;
                break;
            default: break;
        }
    }

    private void run() {

        openList.add(map[start.y][start.x]);
        map[start.y][start.x].open = true;

        while(!openList.isEmpty()) {
            current = openList.peek();
            if (current.x == goal.x && current.y == goal.y)	{
                buildPath();
                return ;
            }

            openList.remove();
            current.open = false;
            closedList.add(current);
            current.closed = true;
            compute();

        }
        path = null;
    }

    private void compute() {
        int g, h;
        for (int i = 0; i < 4; i++) {

            if (i != selectNeighbor(i)) continue;
            if (neighbor.closed) continue;

            if (!neighbor.obstacle) {
                g = setG();
                h = setHeuristic();
                if (!neighbor.open) {
                    neighbor.open = true;
                    setNeighborComponents(g, h);
                    openList.add(neighbor);
                }
                else if ((neighbor.g + neighbor.h) > (g + h)) setNeighborComponents(g, h);
            }
        }
    }

    private void setNeighborComponents(int g, int h) {
        neighbor.g = g;
        neighbor.h = h;
        neighbor.parent = current;
    }

    private int setHeuristic() {
        return (Math.abs(goal.x - neighbor.x) + Math.abs(goal.y - neighbor.y));
//        else return (int) (Math.pow((goal.x - neighbor.x), 2) + Math.pow((goal.y - neighbor.y), 2));
    }

    private int setG() {
        return current.g + 1;
    }

    private int selectNeighbor(int i) {
        switch (i) {
            case 0: if (current.x+1 <= N) {
                neighbor = map[current.y+1][current.x]; break;
            } else i++;
            case 1: if (current.y+1 <= M) {
                neighbor = map[current.y][current.x+1]; break;
            } else i++;
            case 2: if (current.x-1 > 0) {
                neighbor = map[current.y-1][current.x]; break;
            } else i++;
            case 3: if (current.y-1 > 0) {
                neighbor = map[current.y][current.x-1]; break;
            } else i++;
        }
        return i;
    }

    private void buildPath() {
        path.add(current);
        while (current.parent != null){
            current = current.parent;
            path.add(current);
        }
        path.add(first);
        path = reverse(path);
        path.add(last);

        printPath();
    }

    private void printPath() {
        System.out.println("Im Astar...");
        for (Node node : path) {
            System.out.println("(" + node.x + "," + node.y + ") " + node.height);
        }
    }

    private class nodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node node1, Node node2)
        {
            return (node1.g + node1.h) - (node2.g + node2.h);
        }
    }

    private ArrayList<Node> reverse(ArrayList<Node> list) {
        ArrayList<Node> temp = new ArrayList<>();
        for(int i = list.size()-1; i >= 0; i--) {
            temp.add(list.get(i));
            list.remove(i);
        }
        return temp;
    }

    private void initMap() {
        for (int i = 0; i < 30; i++)
            for (int j = 0; j < 60; j++) {
                map[i][j] = new Node();
                map[i][j].y = i;
                map[i][j].x = j;
                map[i][j].closed = false;
                map[i][j].open = false;
                map[i][j].parent = null;
                map[i][j].height = in_map[i][j];
                if (map[i][j].height >= flight.getFlightHeight())
                    map[i][j].obstacle = true;
            }
    }

    public ArrayList<Node> getPath() {
        return this.path;
    }

    public class Node {
        public int x, y, g, h, height;
        Node parent;
        public boolean obstacle, closed, open;

    }
}
