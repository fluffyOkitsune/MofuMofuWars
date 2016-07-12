package mmw;

import java.awt.Point;

public class Character {
	/** 位置 */
	Point pos;
	/** 名前 */
	String name;
	/** 種族 */
	String species;
	/** 兵種ID */
	byte type;
	/** 装備している武器ID */
	byte weapon;
	/** レベル */
	byte level;
	/** 力 */
	byte power;
	/** 魔力 */
	byte magicP;
	/** 守備力 */
	byte guard;
	/** 魔防 */
	byte magicG;
	/** 器用さ */
	byte dexterity;
	/** 回避 */
	byte dodge;
	/** もふ度 */
	byte fluffy;
	/** 武器レベル */
	byte weaponLevel[];
	/** 武器レベル経験値 */
	byte weaponExp[];
	/** スキル */
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
