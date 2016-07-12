package mmw;

import java.awt.Point;

public class Character {
	/** �ʒu */
	Point pos;
	/** ���O */
	String name;
	/** �푰 */
	String species;
	/** ����ID */
	byte type;
	/** �������Ă��镐��ID */
	byte weapon;
	/** ���x�� */
	byte level;
	/** �� */
	byte power;
	/** ���� */
	byte magicP;
	/** ����� */
	byte guard;
	/** ���h */
	byte magicG;
	/** ��p�� */
	byte dexterity;
	/** ��� */
	byte dodge;
	/** ���ӓx */
	byte fluffy;
	/** ���탌�x�� */
	byte weaponLevel[];
	/** ���탌�x���o���l */
	byte weaponExp[];
	/** �X�L�� */
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
