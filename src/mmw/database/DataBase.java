package mmw.database;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import mmw.unit.Unit;

public class DataBase {
	public Unit[] playerUnit;
	public Item[] item;

	public DataBase() {
		MapChip.loadMapChipFile();
		WeaponType.loadImgFile();
		Weapon.loadWeaponFile();
	}
}
