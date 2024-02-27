public class UCT {
    private static final double C = Math.sqrt(2); // Exploration parameter

    public static Node findBestNodeWithUCT(Node node) {
        double maxUCT = Double.NEGATIVE_INFINITY;
        Node bestNode = null;

        for (Node child : node.childArray) {
            double UCTValue = calculateUCTValue(child);
            if (UCTValue > maxUCT) {
                maxUCT = UCTValue;
                bestNode = child;
            }
        }

        return bestNode;
    }

    private static double calculateUCTValue(Node node) {
        if (node.visits == 0) {
            return Double.POSITIVE_INFINITY; // Return infinity for unvisited nodes
        }

        double exploitation = (double) node.score / node.visits; // Average score
        double exploration = C * Math.sqrt(Math.log(node.parent.visits) / node.visits); // Exploration term

        return exploitation + exploration;
    }
}
