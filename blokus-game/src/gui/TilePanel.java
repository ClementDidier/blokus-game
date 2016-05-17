package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import entities.Tile;
import utilities.Vector2;

/**
 * @author Groupe 1
 *
 */
public class TilePanel implements DrawableInterface{
	private boolean state;
	private Color tileColor;
	
	public TilePanel(Color color) {
		this.state = false;
		this.tileColor = color;
	}

	/**
	 * Fonction qui ajout une pi�ce dans le panel
	 * @param t la pi�ce concern�e
	 */
	public void addTile(Tile t)
	{
		afaire;
	}
	
	/**
	 * Fonction qui retire une pi�ce du panel
	 * @param t la pi�ce concern�e
	 */
	public void removeTile(Tile t)
	{
		afaire;
	}
	
	/**
	 * Fonction qui renvoie la pi�ce s�lectionn�e lors du clic gr�ce � la position v
	 * @param v la position du clic
	 * @return la pi�ce cliqu�e
	 */
	public Tile getTile(Vector2<Integer> v)
	{
		afaire;
		return null;
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
	
	public boolean getState()
	{
		return this.state;
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
