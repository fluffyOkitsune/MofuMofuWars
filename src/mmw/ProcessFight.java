package mmw;

import mmw.database.WeaponAttr;
import mmw.unit.Unit;

public class ProcessFight {
	static int damage(Unit ch1, Unit ch2) {
		switch (ch1.weapon.WEAPON_DATA.type) {
		case 剣:
		case 槍:
		case 斧:
		case 弓:
			if (ch1.weapon.WEAPON_DATA.attr.equals(WeaponAttr.魔法武器))
				// ダメージ = 魔力 + 武器の威力 - 魔防
				return ch1.params[1] + ch1.weapon.WEAPON_DATA.atk - ch2.params[3];
			else
				// ダメージ = 力 + 武器の威力 - 防御力
				return ch1.params[0] + ch1.weapon.WEAPON_DATA.atk - ch2.params[1];
		case 理魔法:
		case 光魔法:
		case 闇魔法:
		case 呪い:
			// ダメージ = 魔力 + 武器の威力 - 魔防
			return ch1.params[3] + ch1.weapon.WEAPON_DATA.atk - ch2.params[3];
		default:
			throw new InternalError(ch1.weapon.WEAPON_DATA.type.toString());
		}
	}

	static int hit(Unit ch1, Unit ch2) {
		// 命中率 = 自分の器用さ + 武器の命中補正値 - 相手の回避 - 相手の武器の回避補正値
		return ch1.params[4] + ch1.weapon.WEAPON_DATA.hit - ch2.params[5] - ch2.weapon.WEAPON_DATA.dodge;
	}

	static int critical(Unit ch1, Unit ch2) {
		// 必殺率 = 自分の器用さ + 武器の命中補正値 - 相手の回避 - 相手の武器の回避補正値
		return ch1.params[4] + ch1.weapon.WEAPON_DATA.hit - ch2.params[5] - ch2.weapon.WEAPON_DATA.dodge;
	}
}
