import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private int size; // The number of stones
    private boolean[] stones; // Game state: true for available stones, false for taken ones
    private int lastMove; // The last move
    private double childMin;
    private double childMax;
    private int move;

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size) {

        this.size = size;

        // For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[this.size + 1];
        this.stones[0] = false;

        // Set default state of stones to available
        for (int i = 1; i <= this.size; ++i) {
            this.stones[i] = true;
        }

        // Set the last move be -1
        this.lastMove = -1;

        // Set the initial values of childMax and childMax
        this.childMin = Double.POSITIVE_INFINITY;
        this.childMax = Double.NEGATIVE_INFINITY;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;

        // Set the initial values of childMax and childMax
        this.childMin = Double.POSITIVE_INFINITY;
        this.childMax = Double.NEGATIVE_INFINITY;

    }


    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
        // TODO Add your code here
        List<Integer> listOfMoves = new ArrayList<Integer>();
        if(this.lastMove == -1){
            for (int i = 1; i < Double.valueOf(this.size)/2.0; i+=2)
            {
                listOfMoves.add(i);
            }
            return listOfMoves;
        }
        for (int i = 1; i < this.lastMove; i++) {
            if(this.lastMove % i == 0 && this.stones[i]) listOfMoves.add(i);
        }
        int n = this.lastMove*2;
        int multiplier = 2;
        while(n <= size) {
            if( this.stones[n]) listOfMoves.add(n);
            n = ++multiplier*this.lastMove;
        }        
        return listOfMoves;
    }


    /**
     * This method is used to generate a list of successors
     * using the getMoves() method
     *
     * @return This is the list of state's successors
     */
    public List<GameState> getSuccessors() {
        return this.getMoves().stream().map(move -> {
            var state = new GameState(this);
            state.removeStone(move);
            return state;
        }).collect(Collectors.toList());
    }


    /**
     * This method is used to evaluate a game state based on
     * the given heuristic function
     *
     * @return int This is the static score of given state
     */
    public double evaluate(boolean maxPlayer) {
        // TODO Add your code here
        double multiplier;
        if ( maxPlayer) multiplier = -1.0;
        else multiplier = 1.0;

        List<Integer> moves = this.getMoves();
        if (moves.isEmpty()) return -1*multiplier;
        maxPlayer = !maxPlayer;
        if (this.stones[1]) return 0.0;
        if (this.lastMove == 1) {
            if(moves.size() % 2 == 0) return multiplier*(-0.5);
            else return multiplier*(0.5);
        }
        if (Helper.isPrime(this.lastMove)) {
            int count = 0;
            for (int i = 0; i < moves.size(); i++){
                if (moves.get(i) % this.lastMove == 0) count++;
            }
            if(count % 2 == 0) return multiplier*(-0.7);
            else return multiplier*(0.7);
        } else {
            int largestPrimeFactor = Helper.getLargestPrimeFactor(this.lastMove);
            int count = 0;
            for (int i = 0; i < moves.size(); i++) {
                if(moves.get(i) % largestPrimeFactor == 0) count++;
            }
            if(count % 2 == 0) return multiplier*(-0.6);
            else return multiplier*(0.6);
        }

    }

    /**
     * This method is used to take a stone out
     *
     * @param idx Index of the taken stone
     */
    public void removeStone(int idx) {
        this.stones[idx] = false;
        this.lastMove = idx;
    }

    /**
     * These are get/set methods for a stone
     *
     * @param idx Index of the taken stone
     */
    public void setStone(int idx) {
        this.stones[idx] = true;
    }

    public boolean getStone(int idx) {
        return this.stones[idx];
    }

    /**
     * These are get/set methods for lastMove variable
     *
     * @param move Index of the taken stone
     */
    public void setLastMove(int move) {
        this.lastMove = move;
    }

    public int getLastMove() {
        return this.lastMove;
    }

    /**
     * This is get method for game size
     *
     * @return int the number of stones
     */
    public int getSize() {
        return this.size;
    }

    public double getChildMin() {
        return childMin;
    }

    public void setChildMin(double childMin) {
        this.childMin = childMin;
    }

    public double getChildMax() {
        return childMax;
    }

    public void setChildMax(double childMax) {
        this.childMax = childMax;
    }

}	
