import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node {
    public Board board;
    public Node parent;
    public List<Node> childArray;
    public int visits;
    public float score;
    public boolean isWhiteMove;
    public DeltaMovement move; // New field to store the move associated with the node

    public Node(Board board) {
        this.board = board;
        this.parent = null;
        this.childArray = new ArrayList<>();
        this.visits = 0;
        this.score = 0;
        this.isWhiteMove = true; // Default to white move
        this.move = null; // Initialize move to null
    }

    public Node(Board board, Node parent, boolean isWhiteMove, DeltaMovement move) {
        this.board = board;
        this.parent = parent;
        this.childArray = new ArrayList<>();
        this.visits = 0;
        this.score = 0;
        this.isWhiteMove = isWhiteMove;
        this.move = move; // Initialize move with the provided move
    }

    public boolean isLeafNode() {
        return childArray.isEmpty();
    }

    public Node getRandomChildNode() {
        return childArray.get(new Random().nextInt(childArray.size()));
    }

    public boolean isFullyExpanded() {
        return childArray.size() == board.allMoves(isWhiteMove).size();
    }

    public Node selectChild() {
        // Implement selection strategy here
        // For example, UCT selection
        return UCT.findBestNodeWithUCT(this);
    }

    public Node addChild(DeltaMovement move) {
        
        Board newBoard = board.boardWithMove(board.getPiece(move.dx, move.dy), move);
        Node childNode = new Node(newBoard, this, !isWhiteMove, move); // Pass move to the child node
        childArray.add(childNode);
        return childNode;
    }

    public Node getRandomUntriedChild() {
        List<ArrayList<DeltaMovement>> untriedMoves = board.allMoves(isWhiteMove);
        for (Node child : childArray) {
            for (ArrayList<DeltaMovement> moves : child.board.allMoves(isWhiteMove)) {
                untriedMoves.remove(moves); // Remove tried moves
            }
        }
        if (!untriedMoves.isEmpty()) {
            // Randomly select an untried move and add it as a child node
            ArrayList<DeltaMovement> randomMoves = untriedMoves.get(new Random().nextInt(untriedMoves.size()));
            DeltaMovement randomMove = randomMoves.get(new Random().nextInt(randomMoves.size()));
            return addChild(randomMove);
        }
        return null; // No untried moves
    }
    
    public Node getChildWithMaxScore() {
        Node maxChild = null;
        float maxScore = Float.MIN_VALUE;
        for (Node child : childArray) {
            if (child.score > maxScore) {
                maxChild = child;
                maxScore = child.score;
            }
        }
        return maxChild;
    }
}
