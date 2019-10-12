import java.util.List;

public class AlphaBetaPruning {
    boolean maxPlayer;

    int move;
    double value;
    int nodesVisited;
    int nodesEvaluated;
    int maxDepth;
    double avgBranchingFactor;
    int notPruned;

    public AlphaBetaPruning(boolean maxPlayer) {
        this.maxPlayer = maxPlayer;
        this.nodesVisited = 0;
        this.nodesEvaluated = 0;
        this.maxDepth = Integer.MAX_VALUE;
        this.notPruned = 0;
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
        // TODO Add your code here
        System.out.println("Move: " + this.move);        
        System.out.println("Value: " + String.format("%.1f", this.value));
        System.out.println("Number of Nodes Visited: " + this.nodesVisited);
        System.out.println("Number of Nodes Evaluated: " + this.nodesEvaluated);
        System.out.println("Max Depth Reached: " + this.maxDepth);        
        System.out.println("Avg Effective Branching Factor: " + String.format("%.1f", this.avgBranchingFactor));
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
        // TODO Add your code here
        this.value = alphabeta(state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, this.maxPlayer);
        this.move = state.getMove();
        this.maxDepth = depth - this.maxDepth;
        this.avgBranchingFactor = (double)this.notPruned/(double)(this.nodesVisited-this.nodesEvaluated);
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
        // TODO Add your code here
        this.nodesVisited++;
        if(depth < this.maxDepth) this.maxDepth = depth;
        if (depth == 0) {
            this.nodesEvaluated++;
            return state.evaluate(!maxPlayer);
        }       
        List<GameState> successors = state.getSuccessors();
        if (successors.isEmpty()) {
            this.nodesEvaluated++;
            return state.evaluate(!maxPlayer);
        }
        Double value;
        for(int i = 0; i < successors.size(); i++) {
            
            if (i!=0)
            {
                if (maxPlayer){
                    if(state.getChildMax() >= beta) break;
                }
                else {
                    if(state.getChildMin() <= alpha) break;
                }
            }
            this.notPruned++;
         value = alphabeta(successors.get(i), depth - 1, alpha, beta, !maxPlayer);
            
            if (value > state.getChildMax())
            {
                state.setChildMax(value);
                if(maxPlayer) {
                    state.setMove (successors.get(i).getLastMove());
                    alpha = state.getChildMax();
                }
            } 
            if (value < state.getChildMin()) 
            {
                state.setChildMin(value);
                if(!maxPlayer) {
                    state.setMove (successors.get(i).getLastMove());
                    beta = state.getChildMin();
                }
                
            }
        }

        if (maxPlayer) return state.getChildMax();
        else return state.getChildMin();    
    }
}
