import java.util.ArrayList;
import java.util.List;

public class Problem07 {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] pre : prerequisites) {
            adj.get(pre[1]).add(pre[0]);
        }

        int[] state = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0) {
                if (hasCycle(adj, state, i)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasCycle(List<List<Integer>> adj, int[] state, int u) {
        state[u] = 1;
        for (int v : adj.get(u)) {
            if (state[v] == 1) {
                return true;
            }
            if (state[v] == 0 && hasCycle(adj, state, v)) {
                return true;
            }
        }
        state[u] = 2;
        return false;
    }
}