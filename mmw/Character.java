package mmw;

import java.awt.Point;

public class Character {
	/** Êu */
	Point pos;
	/** ¼O */
	String name;
	/** í° */
	String species;
	/** ºíID */
	byte type;
	/** õµÄ¢éíID */
	byte weapon;
	/** x */
	byte level;
	/** Í */
	byte power;
	/** Í */
	byte magicP;
	/** çõÍ */
	byte guard;
	/** h */
	byte magicG;
	/** íp³ */
	byte dexterity;
	/** ñð */
	byte dodge;
	/** àÓx */
	byte fluffy;
	/** íx */
	byte weaponLevel[];
	/** íxo±l */
	byte weaponExp[];
	/** XL */
	byte skill[];

	Character() {

	}

	public Character(int posX, int posY, String name, String species,
			byte type, byte weapon, byte level, byte power, byte magicP,
			byte guard, byte magicG, byte dexterity, byte dodge, byte fluffy,
			byte[] weaponL, byte[] skill) {
		this.pos = new Point(posX, posY);
		this.name = name;
		this.species = species;
		this.type = type;
		this.weapon = weapon;
		this.level = level;
		this.power = power;
		this.magicP = magicP;
		this.guard = guard;
		this.magicG = magicG;
		this.dexterity = dexterity;
		this.dodge = dodge;
		this.fluffy = fluffy;
		this.weaponLevel = weaponL;
		this.skill = skill;
	}

	public void init() {

	}

	public void update() {

	}

	public void draw() {

	}

}
