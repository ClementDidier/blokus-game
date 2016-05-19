package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import entities.CellColor;
import entities.CellType;
import entities.Tile;
import utilities.Vector2;

/**
 * @author Groupe 1
 *
 */
public class TilePanel implements DrawableInterface{
	/**
	 * Etat du panel des pièce
	 */
	private boolean state;
	
	/**
	 * La couleur des pièces
	 */
	private Color tileColor;
	
	/**
	 * La liste des pièces
	 */
	private HashMap<Tile, Vector2<Integer>> tiles;
	
	/**
	 * La longueur du panel
	 */
	private int width;

	/**
	 * La largeur du panel
	 */
	private int height;
	
	/**
	 * Position du panel dans le panel joueur (PlayerPanel)
	 */
	private Vector2<Integer> pos;
	
	/**
	 * Constructeur de TilePanel
	 * @param color la couleur des pièces dans le panel
	 */
	public TilePanel(Color color, int width, int height, Vector2<Integer> pos) {
		this.state = false;
		this.tileColor = color;
		this.width = width;
		this.height = height;
		this.pos = pos;
	}

	/**
	 * Fonction qui ajoute une pièce dans le panel
	 * @param t la pièce concernée
	 */
	public void addTile(Tile t, Vector2<Integer> v)
	{
		this.tiles.put(t, v);
	}
	
	/**
	 * Fonction qui retire une pièce du panel
	 * @param t la pièce concernée
	 */
	public void removeTile(Tile t)
	{
		this.tiles.remove(t);
	}
	
	/**
	 * Fonction qui renvoie la pièce sélectionnée lors du clic grâce à la position v
	 * @param v la position du clic
	 * @return la pièce cliquée
	 */
	public Tile getTile(Vector2<Integer> v)
	{
		Tile res = null;
		
		for(Entry<Tile, Vector2<Integer>> entry : this.tiles.entrySet())
		{
			if(entry.getKey().isInBounds(v, entry.getValue()))
			{
				res = entry.getKey();
				break;
			}
		}
		return res;
	}
	
	/**
	 * Fonction qui rend la main au panel joueur
	 */
	public void enable()
	{
		this.state = true;
	}
	
	/**
	 * Fonction qui désactive le panel joueur
	 */
	public void disable()
	{
		this.state = false;
	}
	
	/**
	 * Getter de la longueur
	 * @return la longueur
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Settter de la longueur
	 * @param width la longueur
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Getter de la largeur
	 * @return la largeur
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Setter de la largeur
	 * @param height la largeur
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Getter de la position du panel
	 * @return la position du panel
	 */
	public Vector2<Integer> getPos() {
		return pos;
	}

	/**
	 * Setter de la position du panel
	 * @param pos la position du panel
	 */
	public void setPos(Vector2<Integer> pos) {
		this.pos = pos;
	}

	@Override
	public void update(float elapsedTime) {
		
	}

	@Override
	public void draw(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) g.create();
		CellType[][] matrix;
		Tile t;
		Vector2<Integer> pos1stCell;
		int x = 48;
		int y = 91;
		Vector2<Integer> posCell = new Vector2<Integer>(x, y);
		int width;
		int height;
		int nbTilesLine = 1;
		
		for(Entry<Tile, Vector2<Integer>> entry : this.tiles.entrySet())
		{
			t = entry.getKey();
			matrix = t.getMatrix();
			pos1stCell = t.getFirstCase();
			width = t.getCouleur().getImage().getWidth();
			height = t.getCouleur().getImage().getHeight();
			for(int i=pos1stCell.getX(); i<Tile.WIDTH; i++)
			{
				for(int j=0; j<Tile.HEIGHT; i++)
				{
					if(matrix[i][j] == CellType.PIECE)
					{
						g.drawImage(t.getCouleur().getImage(), posCell.getX(), posCell.getY(), width, height, null);
					}
					posCell.setY(posCell.getY()+height);
				}
				posCell.setX(posCell.getX()+width);
			}
			nbTilesLine++;
			
			if(nbTilesLine<=4)
			{
				posCell.setX((x*nbTilesLine)+(width*4));
				posCell.setY(y);
			}
			else
			{
				posCell.setX(x);
				y = y + (height*4);
				posCell.setY(y);
				nbTilesLine = 1;
			}			
		}
		
		g2d.dispose();
	}
		
}
