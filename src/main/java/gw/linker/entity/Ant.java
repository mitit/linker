package gw.linker.entity;

public class Ant {

    public int trailSize;
    public int trail[];
    public boolean visited[];

    public Ant(int tourSize) {
        this.trailSize = tourSize;
        this.trail = new int[tourSize];
        this.visited = new boolean[tourSize];
    }

    public void visitCity(int currentIndex, int city) {
        trail[currentIndex + 1] = city;
        visited[city] = true;
    }

    public boolean visited(int i) {
        return visited[i];
    }

    public double trailLength(double graph[][]) {
//        double length = graph[trail[trailSize - 1]][trail[0]];
        double length = 0.0;
        for (int i = 0; i < trailSize - 1; i++) {
            length += graph[trail[i]][trail[i + 1]];
        }
        return length;
    }

    public void clear() {
        for (int i = 0; i < trailSize; i++)
            visited[i] = false;
    }

}