package mmw.unit;

import mmw.database.Weapon;

/** ユニットがアイテムとして持つ武器のデータ */
public class ItemWeapon {
	public final Weapon WEAPON_DATA;
	public int durablity;

	public ItemWeapon(String name) {
		WEAPON_DATA = Weapon.valueOf(name);
		durablity = WEAPON_DATA.durablity;

	}
}