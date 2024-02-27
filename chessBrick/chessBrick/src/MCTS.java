import java.util.ArrayList;
import java.util.Random;

public class MCTS {
    public static int TIMEOUT = 2000000000;

    public static DeltaMovement monteCarlo(Board b, boolean isWhite) {
        long startTime = System.currentTimeMillis();
        Node root = new Node(b);
        while (System.currentTimeMillis() - startTime < TIMEOUT) {
            Node promisingNode = selectPromisingNode(root);
            if (promisingNode == null) {
                System.out.println("No promising node found!");
                break; // Exit the loop if no promising node is found
            }
            if (promisingNode.board.gameEnd(promisingNode.isWhiteMove) == 0) {
                expandNode(promisingNode);
            }
            Node nodeToExplore = promisingNode;
            if (promisingNode.childArray.size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            if (nodeToExplore == null) {
                System.out.println("No node to explore!");
                break; // Exit the loop if no node to explore is found
            }
            int playoutResult = simulateRandomPlayout(nodeToExplore);
            backPropagation(nodeToExplore, playoutResult);
        }
        Node winnerNode = root.getChildWithMaxScore();
        if (winnerNode == null) {
            System.out.println("No winner node found!");
            return null; // Return null if no winner node is found
        }
        return winnerNode.move;
    }
    
    

    private static Node selectPromisingNode(Node rootNode) {
        Node node = rootNode;
        while (!node.isLeafNode()) {
            node = node.selectChild();
        }
        return node;
    }

    private static void expandNode(Node node) {
        ArrayList<ArrayList<DeltaMovement>> possibleMoves = node.board.allMoves(node.isWhiteMove);
        for (ArrayList<DeltaMovement> moves : possibleMoves) {
            for (DeltaMovement move : moves) {
                Board tempBoard = node.board.boardWithMove(move.p, move);
                Node newNode = new Node(tempBoard, node, !node.isWhiteMove, move); // Pass the move to the constructor
                node.childArray.add(newNode);
            }
        }
    }
    

    private static int simulateRandomPlayout(Node node) {
        Board tempBoard = new Board(node.board);
        boolean isWhiteMove = node.isWhiteMove;
        int gameResult = tempBoard.gameEnd(isWhiteMove);
        Random rand = new Random();
        while (gameResult == 0) {
            ArrayList<ArrayList<DeltaMovement>> allMoves = tempBoard.allMoves(isWhiteMove);
            int randomIndex = rand.nextInt(allMoves.size());
            ArrayList<DeltaMovement> randomPieceMoves = allMoves.get(randomIndex);
            randomIndex = rand.nextInt(randomPieceMoves.size());
            DeltaMovement randomMove = randomPieceMoves.get(randomIndex);
            tempBoard = tempBoard.boardWithMove(randomMove.p, randomMove);
            isWhiteMove = !isWhiteMove;
            gameResult = tempBoard.gameEnd(isWhiteMove);
        }
        return gameResult;
    }

    private static void backPropagation(Node nodeToExplore, int playoutResult) {
        Node tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.visits++;
            tempNode.score += playoutResult;
            tempNode = tempNode.parent;
        }
    }
}
