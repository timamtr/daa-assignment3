### DAA Assignment3 - Report and Defense (50 points)

##### Problem 1 and 2 - Number of Provinces (10 points)

**Problem Overview:**
The task is to find the number of connected components (provinces) in an undirected graph given as an $n \times n$ adjacency matrix `isConnected`.

* **Problem 1 (DFS Approach):**
    * **Algorithm:** We maintain a `visited` boolean array of size $n$. We iterate through each vertex from $0$ to $n-1$. If a vertex is not visited, we increment the province count and initiate a Depth-First Search (DFS) from that vertex to mark all reachable vertices in the same connected component as visited.
    * **Time Complexity:** $O(n^2)$, because the graph is represented as an adjacency matrix and we inspect every cell of the $n \times n$ matrix once across the entire traversal.
    * **Space Complexity:** $O(n)$ for the `visited` array and the recursion call stack.

* **Problem 2 (Disjoint Set Union Approach):**
    * **Algorithm:** We initialize a Disjoint Set Union (DSU) data structure with $n$ isolated components (`provinces = n`). We iterate through the upper triangle of the matrix (`isConnected[i][j] == 1`). For every edge, we perform the `union(i, j)` operation using path compression and union by rank. Whenever two previously separate components are merged, we decrement the `provinces` counter.
    * **Time Complexity:** $O(n^2 \cdot \alpha(n)) \approx O(n^2)$, where $\alpha$ is the inverse Ackermann function (practically constant). We iterate over $\frac{n(n-1)}{2}$ pairs and perform near-constant time DSU operations.
    * **Space Complexity:** $O(n)$ to store the `parent` and `rank` arrays.

---

##### Problem 3 and 4 - Min Cost Spanning Tree (10 points)

**Problem Overview:**
Given $n$ points on a 2D plane, we need to find the Minimum Spanning Tree (MST) cost where the edge weight between any two points is their Manhattan distance: $|x_1 - x_2| + |y_1 - y_2|$. This forms a complete undirected graph with $V = n$ vertices and $E = \frac{n(n-1)}{2}$ edges.

* **Problem 3 (Kruskal's Algorithm):**
    * **Algorithm:** We generate all $\frac{n(n-1)}{2}$ edges and sort them in non-decreasing order of their Manhattan distances. Then, we iterate through the sorted edges and use a Disjoint Set Union (DSU) to add an edge to our MST if it does not form a cycle. We stop once we have added $n - 1$ edges.
    * **Time Complexity:** $O(n^2 \log n)$. Generating all edges takes $O(n^2)$, and sorting $E = O(n^2)$ edges takes $O(E \log E) = O(n^2 \log n^2) = O(n^2 \log n)$. DSU operations take $O(n^2 \alpha(n))$.
    * **Space Complexity:** $O(n^2)$ to store all edges, plus $O(n)$ for the DSU arrays.

* **Problem 4 (Prim's Algorithm):**
    * **Algorithm:** Since the graph is dense (complete graph), we use the dense version of Prim's algorithm without a priority queue. We maintain a `minDist` array to track the minimum weight edge connecting each unvisited vertex to the growing MST, and an `inMST` boolean array. In each of the $n$ iterations, we find the unvisited vertex $u$ with the smallest `minDist[u]` in $O(n)$ time, add it to the MST, and update `minDist` for all remaining unvisited vertices in $O(n)$ time.
    * **Time Complexity:** $O(n^2)$, as we perform $n$ iterations and each iteration does two linear scans of size $n$. This is optimal for dense graphs and avoids the logarithmic overhead of a binary heap.
    * **Space Complexity:** $O(n)$ for the `inMST` and `minDist` arrays (edges are computed on the fly).

---

##### Problem 5 and 6 - Software Dependency Cycles (10 points)

**Problem Overview:**
Given a directed graph of $n$ modules and $m$ dependencies, a "circular dependency group" corresponds to a Strongly Connected Component (SCC). We need to count the total number of SCCs. Because $n \le 10^5$ and $m \le 2 \cdot 10^5$, an iterative DFS with flat array adjacency lists (`head`, `to`, `next`) is used to prevent `StackOverflowError` and optimize memory.

* **Problem 5 (Kosaraju's Algorithm):**
    * **Algorithm:** Kosaraju's algorithm runs in two passes:
        1. Perform a DFS on the original graph to compute the finishing order (post-order) of vertices.
        2. Reverse all edges in the graph and perform a second DFS processing vertices in decreasing order of their finishing times. Each full traversal in the second pass discovers exactly one SCC.
    * **Time Complexity:** $O(V + E)$ (or $O(n + m)$), as we traverse the original graph once and the transposed graph once.
    * **Space Complexity:** $O(V + E)$ to store the graph, the reversed graph, visited flags, and explicit traversal stacks.

* **Problem 6 (Tarjan's Algorithm):**
    * **Algorithm:** Tarjan's algorithm finds all SCCs in a single DFS pass. It maintains two arrays: `ids` (discovery time of each node) and `low` (the lowest discovery time reachable from the node's subtree), alongside an explicit stack of currently active nodes (`onStack`). When a node $u$ finishes exploring all its neighbors and `ids[u] == low[u]`, $u$ is the root of an SCC; we pop nodes from the Tarjan stack until $u$ is popped and increment the SCC count.
    * **Time Complexity:** $O(V + E)$ (or $O(n + m)$), visiting each vertex and edge exactly once.
    * **Space Complexity:** $O(V + E)$ for the adjacency list representation, tracking arrays (`ids`, `low`, `onStack`), and explicit stacks.

---

##### Problem 7 and 8 - Course Schedule (10 points)

**Problem Overview:**
Given $V = \text{numCourses}$ and directed prerequisite edges $E$, we need to determine if it is possible to finish all courses. This is equivalent to **cycle detection** in a directed graph (checking if the graph is a Directed Acyclic Graph, or DAG).

* **Problem 7 (DFS Approach):**
    * **Algorithm:** We build an adjacency list and use 3-color cycle detection via a `state` array: `0` = unvisited, `1` = visiting (currently on the recursion stack), `2` = visited (completely processed). During DFS from node $u$, if we encounter a neighbor $v$ with `state[v] == 1`, we have found a back-edge (and thus a cycle), so we return `false`. If all nodes are processed without finding a cycle, we return `true`.
    * **Time Complexity:** $O(V + E)$, as every vertex and edge is explored at most once.
    * **Space Complexity:** $O(V + E)$ for the adjacency list, plus $O(V)$ for the `state` array and recursion stack.

* **Problem 8 (Kahn's Algorithm - BFS Approach):**
    * **Algorithm:** We compute the in-degree for every vertex and push all vertices with `inDegree == 0` (courses with no prerequisites) into a queue. While the queue is not empty, we pop a vertex $u$, increment our `completed` counter, and decrement the in-degree of all its neighbors. If a neighbor's in-degree drops to `0`, we push it into the queue. If `completed == numCourses`, the graph has no cycles (`true`); otherwise, a cycle prevented some nodes from reaching in-degree `0` (`false`).
    * **Time Complexity:** $O(V + E)$ to build the graph, calculate in-degrees, and process each vertex and edge in the BFS queue.
    * **Space Complexity:** $O(V + E)$ for the adjacency list and $O(V)$ for the `inDegree` array and BFS queue.

---

##### Problem 9 and 10 - Course Schedule II (10 points)

**Problem Overview:**
Building upon Problems 7 and 8, we must return a valid **Topological Sort** (an ordering of courses to take). If a cycle exists, we must return an empty array `[]`.

* **Problem 9 (DFS Approach):**
    * **Algorithm:** We use the same 3-state DFS cycle detection as in Problem 7. When a vertex $u$ finishes exploring all its outgoing edges without finding a cycle (`state[u]` becomes `2`), we place $u$ at the back of our `order` array (starting from `index = numCourses - 1` down to `0`). This naturally records the reverse post-order, which is a valid topological sorting. If a cycle is detected at any point, we immediately return `new int[0]`.
    * **Time Complexity:** $O(V + E)$ to traverse the entire graph and populate the topological order array.
    * **Space Complexity:** $O(V + E)$ for the adjacency list, and $O(V)$ for the `state` array, `order` array, and recursion call stack.

* **Problem 10 (Kahn's Algorithm - BFS Approach):**
    * **Algorithm:** Similar to Problem 8, we push all vertices with `inDegree == 0` into a queue. Whenever we pop a vertex $u$ from the queue, we append it directly to our `order` array at `order[index++]` and reduce the in-degrees of its neighbors, pushing any neighbor that reaches `inDegree == 0` into the queue. At the end, if `index == numCourses`, we return `order`; otherwise, a cycle exists and we return `new int[0]`.
    * **Time Complexity:** $O(V + E)$, as each vertex is enqueued/dequeued at most once and each directed edge is relaxed once.
    * **Space Complexity:** $O(V + E)$ for the adjacency list and $O(V)$ for the `inDegree` array, `order` array, and queue.