import java.util.Arrays;

public class Problem06 {
    public int countDependencyGroups(int n, int[][] dependencies) {
        int m = dependencies.length;
        int[] head = new int[n];
        int[] to = new int[m];
        int[] next = new int[m];
        Arrays.fill(head, -1);

        for (int i = 0; i < m; i++) {
            int u = dependencies[i][0];
            int v = dependencies[i][1];
            to[i] = v;
            next[i] = head[u];
            head[u] = i;
        }

        int[] ids = new int[n];
        int[] low = new int[n];
        boolean[] onStack = new boolean[n];
        Arrays.fill(ids, -1);

        int[] tarjanStack = new int[n];
        int tarjanTop = -1;

        int[] callStack = new int[n];
        int[] edgeStack = new int[n];

        int idCounter = 0;
        int sccCount = 0;

        for (int i = 0; i < n; i++) {
            if (ids[i] == -1) {
                int top = 0;
                callStack[top] = i;
                edgeStack[top] = head[i];
                ids[i] = low[i] = idCounter++;
                tarjanStack[++tarjanTop] = i;
                onStack[i] = true;

                while (top >= 0) {
                    int u = callStack[top];
                    int e = edgeStack[top];

                    if (e != -1) {
                        int v = to[e];
                        edgeStack[top] = next[e];

                        if (ids[v] == -1) {
                            top++;
                            callStack[top] = v;
                            edgeStack[top] = head[v];
                            ids[v] = low[v] = idCounter++;
                            tarjanStack[++tarjanTop] = v;
                            onStack[v] = true;
                        } else if (onStack[v]) {
                            if (ids[v] < low[u]) {
                                low[u] = ids[v];
                            }
                        }
                    } else {
                        if (ids[u] == low[u]) {
                            while (true) {
                                int node = tarjanStack[tarjanTop--];
                                onStack[node] = false;
                                if (node == u) {
                                    break;
                                }
                            }
                            sccCount++;
                        }
                        top--;
                        if (top >= 0) {
                            int parent = callStack[top];
                            if (low[u] < low[parent]) {
                                low[parent] = low[u];
                            }
                        }
                    }
                }
            }
        }

        return sccCount;
    }
}