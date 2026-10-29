import java.util.Arrays;

public class Problem03 {
    public int minCostConnectPoints(int[][] points) {
        int n = points.length;
        int totalEdges = n * (n - 1) / 2;
        int[][] edges = new int[totalEdges][3];
        int idx = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dist = Math.abs(points[i][0] - points[j][0]) + Math.abs(points[i][1] - points[j][1]);
                edges[idx][0] = i;
                edges[idx][1] = j;
                edges[idx][2] = dist;
                idx++;
            }
        }

        Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));

        int[] parent = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        int mstCost = 0;
        int edgesUsed = 0;

        for (int i = 0; i < totalEdges && edgesUsed < n - 1; i++) {
            if (union(parent, rank, edges[i][0], edges[i][1])) {
                mstCost += edges[i][2];
                edgesUsed++;
            }
        }

        return mstCost;
    }

    private int find(int[] parent, int i) {
        if (parent[i] != i) {
            parent[i] = find(parent, parent[i]);
        }
        return parent[i];
    }

    private boolean union(int[] parent, int[] rank, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        if (rootX == rootY) {
            return false;
        }
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        return true;
    }
}