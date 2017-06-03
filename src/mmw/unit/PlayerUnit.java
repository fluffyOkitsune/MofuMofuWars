package mmw.unit;

import mmw.Game;

public class PlayerUnit extends Unit {

	private byte[] paramsGrowth;

	public PlayerUnit(String posX, String posY, String name, String explain, String species, String type, String weapon,
			String level, String move, String[] params, String[] paramsGrowth, String[] weaponLevel, String[] skill) {
		super(posX, posY, name, explain, species, type, weapon, level, move, params, weaponLevel, skill);

		byte[] temp = new byte[Game.NUM_PARAM];
		for (int i = 0; i < Game.NUM_PARAM; i++) {
			temp[i] = Byte.parseByte(paramsGrowth[i]);
		}
		this.paramsGrowth = temp;
	}

}
