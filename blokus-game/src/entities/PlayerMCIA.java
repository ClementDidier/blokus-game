package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utilities.MCNode;
import utilities.Move;

public class PlayerMCIA extends Player{
	private static final long serialVersionUID = -8387538910197440018L;

	private static final int DEFAULT_SAMPLE_SIZE = 2000;
	private Game game;
	private Random rand;
	private int piecesPlacees;

	public PlayerMCIA(String name, List<CellColor> colors) 
	{
		super(name, colors);
		this.game = null;
		this.rand = new Random();
		this.piecesPlacees = 0;
	}

	public PlayerMCIA(PlayerMCIA p) {
		super(p);
		this.game = null;
		this.rand = new Random();
		this.piecesPlacees = 0;
	}

	@Override
	public void play(Game g, CellColor c) {
		this.playing = true;
		this.game = g.copy();

		new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				chosenMove = monteCarlo(game, DEFAULT_SAMPLE_SIZE + piecesPlacees * 20);
				
				game = null;
				playing = false;
			}
	
		}).start();
		
	}

	/**
	 * Algorithme Monte Carlo (Arbre de recherche)
	 * @param game La configuration
	 * @param itermax Le nombre d'itérations
	 * @param uctk La valeur 
	 * @return
	 */
	private Move monteCarlo(Game game, long miliseconds) 
	{
		long ms = System.currentTimeMillis() + miliseconds;
		
		MCNode rootNode = new MCNode(null, null, game);

		while(ms > System.currentTimeMillis()) 
		{
			MCNode node = rootNode;
			Game state = node.getGame().copy();

			// STEP 1 : SELECTION
			MCNode selectedNode = this.selection(node, state);

			// STEP 2 : EXPAND
			MCNode expandedNode = this.expand(selectedNode, state);

			// STEP 3 : SIMULATION
			boolean gameResult  = this.simulateGameRandomlyAndRevert(expandedNode.getGame());
			
			// STEP 4 : BACKPROPAGATE
			this.backpropagate(expandedNode, gameResult);
		}
		
		return rootNode.getMostVisitedMove();
	}
	
	private MCNode selection(MCNode node, Game state)
	{
		while (!node.isLeaf() && node.getChilds().size() > 1)
		{
			node = node.selectChild();
		}
		
		return node;
	}
	
	private MCNode expand(MCNode node, Game game)
	{
		Move mv = Move.selectRandomlyPossibleMoveWithHeuristic(node, game, rand, 12);

		game.doMove(mv);
		MCNode fnode = new MCNode(node, mv, game);
		node.addChild(fnode);
		
		return node.addChild(fnode);
	}
	

	/**
	 * Simule un jeu depuis une configuration donnée et remet le plateau à la
	 * configuration donnée
	 * 
	 * @param game
	 *            La configuration
	 * @param refPlayer
	 *            Le joueur de référence
	 * @return True si le joueur de référence à gagné le jeu, False dans le cas
	 *         contraire
	 */
	private boolean simulateGameRandomlyAndRevert(Game game) {
		int count = 0;
		while (!game.isTerminated()) {
			count++;
			ArrayList<Move> moves = Move.possibleMovesWithHeurisitic(game, 12);
			Move move = Move.EMPTY;
			if(moves.size() > 1)
				move = moves.get(this.rand.nextInt(moves.size() - 1));
			else if(!moves.isEmpty())
				move = moves.get(0);
			game.doMove(move);
		}

		boolean resultGame = game.getWinner().equals(this);

		// Revert de la partie
		while (count > 0) {
			count--;
			game.undoSingleMove();
		}

		return resultGame;
	}

	/**
	 * Simule un jeu depuis une configuration donnée sans remise en état du
	 * plateau
	 * 
	 * @param game
	 *            La configuration
	 * @param refPlayer
	 *            Le joueur de référence
	 * @return True si le joueur de référence à gagné le jeu, False dans le cas
	 *         contraire
	 */
	@SuppressWarnings("unused")
	private boolean simulateGameRandomlyWhithoutRevert(Game game) {
		while (!game.isTerminated()) {
			Move mv = Move.generateRandomValidMove(game);
			game.doMove(mv);
		}

		boolean resultGame = game.getWinner().equals(this);

		return resultGame;
	}
	
	// BACKPROPAGATE
	private void backpropagate(MCNode node, boolean gameResult)
	{
		while (node != null) {
			node.update(gameResult);
			node = node.getParent();
		}
	}

	@Override
	public Player copy() {
		return new PlayerMCIA(this);
	}
}
