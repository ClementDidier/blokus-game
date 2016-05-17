package gui;

import java.awt.Graphics2D;

import entities.Tile;
import utilities.Vector2;

/**
 * @author Groupe1
 *
 */
public class PlayerPanel implements DrawableInterface{
	private boolean state;
	private TilePanel tp1;
	private TilePanel tp2;
	
	public PlayerPanel(TilePanel t1, TilePanel t2) {
		this.state = false;
		this.tp1 = t1;
		this.tp2 = t2;
	}

	/**
	 * Fonction qui rend la main au panel joueur
	 */
	public void enable()
	{
		this.state = true;
	}
	
	/**
	 * Fonction qui d�sactive le panel joueur
	 */
	public void disable()
	{
		this.state = false;
	}
	
	/**
	 * Fonction qui retourne le statut du panel du joueur
	 * @return le statut du panel joueur
	 */
	public boolean getState()
	{
		return this.state;
	}
	
	/**
	 * Fonction qui renvoie la pi�ce s�lectionn�e lors du clic gr�ce � la position v
	 * @param v la position du clic
	 * @return la pi�ce cliqu�e
	 */
	public Tile getTile(Vector2<Integer> v)
	{
		return null;
		afaire;
	}

	@Override
	public void update(float elaspedTime) {
		afaire;
	}

	@Override
	public void draw(Graphics2D g) {
		afaire;
	}
}
