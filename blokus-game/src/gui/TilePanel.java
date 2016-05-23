package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import entities.CellColor;
import entities.CellType;
import entities.Player;
import entities.Tile;
import navigation.Page;
import utilities.BufferedImageHelper;
import utilities.Vector2;

/*
id: colonne,ligne
1:	10,10
2:	40,10
3:	130,220
4:	50,200
5:	40,60
6: 	180,180
7: 	220,210
8: 	70,10
9: 	90,60
10:	10,40
11:	200,10
12:	160,260
13:	10,150
14:	120,10
15:	120,90
16:	130,140
17:	10,240
18:	70,150
19:	180,110
20:	190,40
21:	80,230
 */


/**
 * @author Groupe 1
 *
 */
public class TilePanel implements DrawableInterface{
	
	private final static int OFFSET_X_P1_BLUE = 48;
	private final static int OFFSET_Y_P1_BLUE = 91;
	
	private final static int OFFSET_X_P2_YELLOW = 995;
	private final static int OFFSET_Y_P2_YELLOW = 91;
	
	private final static int OFFSET_X_P1_RED = 48;
	private final static int OFFSET_Y_P1_RED = 400;
	
	private final static int OFFSET_X_P2_GREEN = 995;
	private final static int OFFSET_Y_P2_GREEN = 400;
	/**
	 * Etat du panel des pièce
	 */
	private boolean state;
	
	/**
	 * La couleur des pièces
	 */
	private CellColor tileColor;
	
	/**
	 * La liste des pièces
	 */
	private HashMap<Tile, Vector2> tiles;
	
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
	private Vector2 pos;
	
	private BufferedImage cellMaskImage;
	
	private Player player;

	private BlokusTile bt;
	
	/**
	 * Constructeur de TilePanel
	 * @param color la couleur des pièces dans le panel
	 */
	public TilePanel(CellColor color, int width, int height, Vector2 pos, Player p) {
		this.state = false;
		this.tileColor = color;
		this.width = width;
		this.height = height;
		this.pos = pos;
		this.player = p;
		this.tiles = new HashMap<Tile, Vector2>();
		
		ArrayList<Tile> listOfTiles = Tile.getListOfNeutralTile(color);
//		this.tiles.put(listOfTiles.get(0), new Vector2(0,0));
//		this.tiles.put(listOfTiles.get(1), new Vector2(30,0));
//		this.tiles.put(listOfTiles.get(2), new Vector2(120,210));
//		this.tiles.put(listOfTiles.get(3), new Vector2(40,190));
//		this.tiles.put(listOfTiles.get(4), new Vector2(30,50));
//		this.tiles.put(listOfTiles.get(5), new Vector2(170,170));
//		this.tiles.put(listOfTiles.get(6), new Vector2(210,200));
//		this.tiles.put(listOfTiles.get(7), new Vector2(60,0));
//		this.tiles.put(listOfTiles.get(8), new Vector2(80,50));
//		this.tiles.put(listOfTiles.get(9), new Vector2(0,30)); //-40,70
//		this.tiles.put(listOfTiles.get(10), new Vector2(190,0));
//		this.tiles.put(listOfTiles.get(11), new Vector2(150,250));
//		this.tiles.put(listOfTiles.get(12), new Vector2(0,140));
//		this.tiles.put(listOfTiles.get(13), new Vector2(110,0));
//		this.tiles.put(listOfTiles.get(14), new Vector2(110,80));
//		this.tiles.put(listOfTiles.get(15), new Vector2(120,130));
//		this.tiles.put(listOfTiles.get(16), new Vector2(00,230));
//		this.tiles.put(listOfTiles.get(17), new Vector2(60,140));
//		this.tiles.put(listOfTiles.get(18), new Vector2(170,100));
//		this.tiles.put(listOfTiles.get(19), new Vector2(180,30));
//		this.tiles.put(listOfTiles.get(20), new Vector2(70,220));
		
		this.tiles.put(listOfTiles.get(0), new Vector2(0,0));
		this.tiles.put(listOfTiles.get(1), new Vector2(10,20));
		this.tiles.put(listOfTiles.get(2), new Vector2(100,230));
		this.tiles.put(listOfTiles.get(3), new Vector2(20,210));
		this.tiles.put(listOfTiles.get(4), new Vector2(-10,90));
		this.tiles.put(listOfTiles.get(5), new Vector2(190,150));
		this.tiles.put(listOfTiles.get(6), new Vector2(190,220));
		this.tiles.put(listOfTiles.get(7), new Vector2(60,0));
		this.tiles.put(listOfTiles.get(8), new Vector2(60,70));
		this.tiles.put(listOfTiles.get(9), new Vector2(-40,70)); //-40,70
		this.tiles.put(listOfTiles.get(10), new Vector2(170,20));
		this.tiles.put(listOfTiles.get(11), new Vector2(130,270));
		this.tiles.put(listOfTiles.get(12), new Vector2(0,140));
		this.tiles.put(listOfTiles.get(13), new Vector2(130,-20));
		this.tiles.put(listOfTiles.get(14), new Vector2(70,120));
		this.tiles.put(listOfTiles.get(15), new Vector2(120,130));
		this.tiles.put(listOfTiles.get(16), new Vector2(00,230));
		this.tiles.put(listOfTiles.get(17), new Vector2(60,140));
		this.tiles.put(listOfTiles.get(18), new Vector2(170,100));
		this.tiles.put(listOfTiles.get(19), new Vector2(140,70));
		this.tiles.put(listOfTiles.get(20), new Vector2(50,240));
		
		this.cellMaskImage = BufferedImageHelper.generateMask(color.getImage(), Color.BLACK, 0.5f);
	}

	/**
	 * Fonction qui ajoute une pièce dans le panel
	 * @param t la pièce concernée
	 */
	public void addTile(Tile t, Vector2 v)
	{
		this.tiles.put(t, v);
	}
	
	/**
	 * Fonction qui retire une pièce du panelbTilesLine<=4)
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
	public Tile getTile(Vector2 v)
	{
		Tile res = null;
		
		for(Entry<Tile, Vector2> entry : this.tiles.entrySet())
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
	public Vector2 getPos() {
		return pos;
	}

	/**
	 * Setter de la position du panel
	 * @param pos la position du panel
	 */
	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	@Override
	public void update(float elapsedTime) {
		
	}

	
	@Override
	public void draw(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		Tile t;
		Vector2 posTile;
		Vector2 posCell = new Vector2(OFFSET_X_P1_BLUE, OFFSET_Y_P1_BLUE);
//		int nbTilesLine = 1;
//		int nbTilesColonnes = 1;
//		ArrayList<Tile> ap = (ArrayList<Tile>) this.player.getTileInventory();
		int dX = 0;
		int diffX = 0;
		int dY = 0;
		int diffY = 0;
		int basePosCellX = 0;
		int basePosCellY = 0;
		
		if(this.tileColor == CellColor.BLUE)
		{
			basePosCellX = OFFSET_X_P1_BLUE;
			basePosCellY = OFFSET_Y_P1_BLUE;
		}
		else if(this.tileColor == CellColor.YELLOW)
		{
			basePosCellX = OFFSET_X_P2_YELLOW;
			basePosCellY = OFFSET_Y_P2_YELLOW;
		}
		else if(this.tileColor == CellColor.RED)
		{
			basePosCellX = OFFSET_X_P1_RED;
			basePosCellY = OFFSET_Y_P1_RED; 
		}
		else if(this.tileColor == CellColor.GREEN)
		{
			basePosCellX = OFFSET_X_P2_GREEN;
			basePosCellY = OFFSET_Y_P2_GREEN;
		}
		
		for(Entry<Tile, Vector2> entry : this.tiles.entrySet())
		{
			t = entry.getKey();
			posTile = entry.getValue();
			
			bt = new BlokusTile(t);
			posCell.setX(basePosCellX+posTile.getX());
			posCell.setY(basePosCellY+posTile.getY());
			bt.setPosition(posCell);
			bt.draw(g2d);
			
			posCell.setX(0);
			posCell.setY(0);
			
			
//			for(int i=0; i<Tile.WIDTH; i++)
//			{
//				for(int j=0; j< Tile.HEIGHT; j++)
//				{
//					if(t.getMatrix()[i][j] == CellType.PIECE)
//					{
//						if(dX==0&&dY==0)
//						{
//							dX = i; //2
//							dY = j; //1
//
//							posCell.setX(posCellx+posTile.getX());
//							posCell.setY(posCelly+posTile.getY());
//						}
//						else
//						{
//							diffX = dX - i; // 2 - 3 = -1
//							diffY = dY - j; // 1 - 1 = 0
//							
//							posCell.setX(posTile.getX()+posCellx+diffX*CellColor.CELL_WIDTH); //
//							posCell.setY(posTile.getY()+posCelly+diffY*CellColor.CELL_HEIGHT);
//						}
//						g2d.drawImage(t.getCouleur().getImage(), posCell.getX(), posCell.getY(), CellColor.CELL_WIDTH, CellColor.CELL_HEIGHT, null);
//					}
//				}
//			}
//			dX = 0;
//			dY = 0;
//			diffX = 0;
//			diffY = 0;
		}
		g2d.dispose();
	}
}
