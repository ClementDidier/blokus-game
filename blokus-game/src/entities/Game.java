package entities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;

import utilities.Move;
import utilities.UndoRedoManager;

public class Game
{
	private static final int EVENT_TURN_ENDED = 5;
	/**
	 * Plateau de jeu courant
	 */
	private Board board;
	/**
	 * Liste des joueurs de la partie
	 */
	private ArrayList<Player> players;
	/**
	 * Tour actuel, aussi utilisé pour determiner le joueur courant
	 */
	private int currentTurn;
	/**
	 * Prise en charge de la sauvegarde de l'état de la partie pour les
	 * annulations et répétitions de coups
	 */
	private UndoRedoManager<Board> undoRedoManager;
	/**
	 * Liste des ActionListeners de cette partie
	 */
	private ArrayList<ActionListener> listeners;
	private ArrayList<CellColor> playingColors;
	
	private Gson gson;

	public Game()
	{	
		this.gson = new Gson();
		this.currentTurn = 0;

		this.board = new Board();

		this.players = new ArrayList<Player>();
		ArrayList<CellColor> colorsP1 = new ArrayList<CellColor>();
		colorsP1.add(CellColor.BLUE);
		colorsP1.add(CellColor.RED);
		this.players.add(new PlayerRandom("J1", colorsP1));
		ArrayList<CellColor> colorsP2 = new ArrayList<CellColor>();
		colorsP2.add(CellColor.YELLOW);
		colorsP2.add(CellColor.GREEN);
		this.players.add(new PlayerRandom("J2", colorsP2));

		this.undoRedoManager = new UndoRedoManager<Board>();

		this.listeners = new ArrayList<ActionListener>();

		this.playingColors = new ArrayList<CellColor>();
		this.playingColors.add(CellColor.BLUE);
		this.playingColors.add(CellColor.YELLOW);
		this.playingColors.add(CellColor.RED);
		this.playingColors.add(CellColor.GREEN);
	}

	/**
	 * Renvoie l'état de la partie, la partie est finie quand aucun joueur ne
	 * peut jouer un coup supplémentaire.
	 * 
	 * @return Etat de la partie, true si elle est terminée.
	 */
	public boolean isTerminated()
	{
		if (this.playingColors.isEmpty())
			return true;
		return false;
	}

	/**
	 * Transmet une copie du plateau courant
	 * 
	 * @return Copie du plateau courant
	 */
	public Board getBoard()
	{
		return this.board.copy();
	}

	/**
	 * Permet de récuperer le tour courant
	 * 
	 * @return Tour courant
	 */
	public int getTurn()
	{
		return this.currentTurn;
	}

	// TODO: Implémenter le undoRedoManager différemment car besoin sauvegarder
	// état des joueurs
	// aussi(pieces restantes, etc)
	public void undoMove()
	{
		this.board = undoRedoManager.undo(this.board);
		this.currentTurn--;
	}

	public void redoMove()
	{
		this.board = undoRedoManager.redo(this.board);
		this.currentTurn++;
	}

	public boolean canUndo()
	{
		return this.undoRedoManager.canUndo();
	}

	public boolean canRedo()
	{
		return this.undoRedoManager.canRedo();
	}

	/**
	 * Récupère le joueur courant
	 * 
	 * @return Joueur courant
	 */
	public Player getCurrentPlayer()
	{
		CellColor c = getCurrentColor();

		for (Player p : this.players)
		{
			if (p.colors.contains(c))
				return p;
		}

		return null;
	}

	/**
	 * @return
	 */
	public CellColor getCurrentColor()
	{
		CellColor c;
		switch (this.currentTurn % 4)
		{
		case 0:
			c = CellColor.BLUE;
			break;
		case 1:
			c = CellColor.YELLOW;
			break;
		case 2:
			c = CellColor.RED;
			break;
		case 3:
			c = CellColor.GREEN;
			break;
		default:
			c = CellColor.BLUE;
			break;
		}

		return c;
	}

	/**
	 * Appelé à chaque itération de jeu
	 */
	public void update()
	{
		if (!this.isTerminated())
		{
			if (this.playingColors.contains(this.getCurrentColor()))
			{
				Player p = this.getCurrentPlayer();
				if (!p.isPlaying())
				{
					Move m = p.getMove();
					if (m == null)
					{
						p.play(this, this.getCurrentColor());
					}
					else
					{
						if (m.getTile() != null)
						{
							this.doMove(m);
						}
						else
						{
							this.playingColors.remove(this.getCurrentColor());
						}
					}
				}
			}
			else
			{
				this.currentTurn++;
			}
		}
	}

	/**
	 * Sauvegarde la partie dans un fichier TODO: do it
	 */
	public void save(){
		File file = new File("sauvegarde");
		FileWriter fileWriter;
		JsonObject obj = new JsonObject();
		
		
	
	}
	
	public void load(){
		System.out.println("load");
		
	}

	/**
	 * Fonction appellée quand un coup valide doit être traité/appliqué par Game
	 * 
	 * @param m
	 *            Coup joué
	 */
	public void doMove(Move m)
	{
		this.undoRedoManager.add(this.board.copy());

		try
		{
			this.board.addTile(m.getTile(), m.getTileOrigin(), m.getPosition());
			this.currentTurn++;
			this.raiseEvent(new ActionEvent(this, EVENT_TURN_ENDED, null));
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Ajoute un listener à la classe de jeu
	 * 
	 * @param al
	 *            Le listener à ajouter
	 */
	public void addListener(ActionListener al)
	{
		this.listeners.add(al);
	}

	/**
	 * Supprime un listener de la classe de jeu
	 * 
	 * @param al
	 *            Le listener à supprimer
	 */
	public void removeListener(ActionListener al)
	{
		this.listeners.remove(al);
	}

	/**
	 * Lance l'évènement sur tous les listeners de la classe
	 * 
	 * @param e
	 *            Les informtations de l'évènement lancé
	 */
	public void raiseEvent(ActionEvent e)
	{
		for (ActionListener al : this.listeners)
		{
			al.actionPerformed(e);
		}
	}

	/**
	 * Renvoi la liste de tout les coups possibles pour le joueur courant en
	 * tenant compte du plateau et de son inventaire de pièces.
	 * 
	 * @return Liste des coups possibles
	 */
	public List<Move> possibleMoves()
	{
		ArrayList<Move> res = new ArrayList<Move>();

		return res;
	}

	/**
	 * Renvoi un objet Game représentant l'objet courant auquel on a appliqué un
	 * mouvement qui ne doit pas être pris en compte dans l'historique de la
	 * partie
	 * 
	 * @param m
	 *            Coup à jouer
	 * @return Nouvel état de partie
	 */
	public Game simulateMove(Move m)
	{
		// TODO
		return null;
	}

	/**
	 * Renvoi le score du joueur fourni en paramètre dans l'état actuel du jeu
	 * 
	 * @param player
	 *            Joueur
	 * @return Score du joueur
	 */
	public int getScore(Player player)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Obtient la liste des joueurs du jeu
	 * 
	 * @return La liste des joueurs du jeu
	 */
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
}
