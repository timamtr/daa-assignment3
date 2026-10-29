import java.util.Arrays;

public class Problem05 {
    public int countDependencyGroups(int n, int[][] dependencies) {
        int m = dependencies.length;
        int[] head = new int[n];
        int[] to = new int[m];
        int[] next = new int[m];
        Arrays.fill(head, -1);

        int[] revHead = new int[n];
        int[] revTo = new int[m];
        int[] revNext = new int[m];
        Arrays.fill(revHead, -1);

        for (int i = 0; i < m; i++) {
            int u = dependencies[i][0];
            int v = dependencies[i][1];
            to[i] = v;
            next[i] = head[u];
            head[u] = i;

            revTo[i] = u;
            revNext[i] = revHead[v];
            revHead[v] = i;
        }

        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int orderIdx = 0;

        int[] callStack = new int[n];
        int[] edgeStack = new int[n];

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                int top = 0;
                callStack[top] = i;
                edgeStack[top] = head[i];
                visited[i] = true;

                while (top >= 0) {
                    int u = callStack[top];
                    int e = edgeStack[top];

                    if (e != -1) {
                        edgeStack[top] = next[e];
                        int v = to[e];
                        if (!visited[v]) {
                            visited[v] = true;
                            top++;
                            callStack[top] = v;
                            edgeStack[top] = head[v];
                        }
                    } else {
                        order[orderIdx++] = u;
                        top--;
                    }
                }
            }
        }

        Arrays.fill(visited, false);
        int sccCount = 0;

        for (int i = n - 1; i >= 0; i--) {
            int startNode = order[i];
            if (!visited[startNode]) {
                sccCount++;
                int top = 0;
                callStack[top] = startNode;
                visited[startNode] = true;

                while (top >= 0) {
                    int u = callStack[top--];
                    for (int e = revHead[u]; e != -1; e = revNext[e]) {
                        int v = revTo[e];
                        if (!visited[v]) {
                            visited[v] = true;
                            callStack[++top] = v;
                        }
                    }
                }
            }
        }

        return sccCount;
    }
}