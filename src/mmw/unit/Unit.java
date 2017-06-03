package mmw.unit;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import mmw.Game;
import mmw.database.Skill;
import mmw.database.Species;
import mmw.database.UnitType;

public class Unit {
	public static final int PLAYER = 0;
	public static final int ENEMY = 1;
	public static final int ALLIANCE = 2;
	
	/** 所属する軍 */
	public int millitary;
	/** 位置 */
	public Point pos;
	/** 名前 */
	public String name;
	/** 説明 */
	public String explain;
	/** 種族 */
	public Species species;
	/** 兵種 */
	public UnitType type;
	/** 装備している武器 */
	public ItemWeapon weapon;
	/** レベル */
	public byte level;
	/** 移動力 */
	public byte move;
	/** パラメーター */
	public byte[] params;
	/** 武器レベル */
	public char[] weaponLevel;
	/** スキル */
	public Skill[] skill;

	public Unit(String posX, String posY, String name, String explain, String species, String type, String weapon,
			String level, String move, String[] params, String[] weaponL, String[] skill) {
		this.pos = new Point(Integer.parseInt(posX), Integer.parseInt(posY));
		this.name = name;
		this.explain = explain;
		this.species = Species.valueOf(species);
		this.type = UnitType.valueOf(type);
		this.weapon = new ItemWeapon(weapon);
		this.level = Byte.parseByte(level);
		this.move = Byte.parseByte(move);

		byte[] temp1 = new byte[Game.NUM_PARAM];
		for (int i = 0; i < Game.NUM_PARAM; i++) {
			temp1[i] = Byte.parseByte(params[i]);
		}
		this.params = temp1;

		char[] temp2 = new char[Game.NUM_WEAPON_TYPE];
		for (int i = 0; i < Game.NUM_WEAPON_TYPE; i++) {
			temp2[i] = weaponL[i].charAt(0);
		}
		this.weaponLevel = temp2;

		Skill[] temp3 = new Skill[Game.NUM_SKILL];
		for (int i = 0; i < Game.NUM_SKILL; i++) {
			temp3[i] = skill[i].equals("") ? Skill.なし : Skill.valueOf(skill[i]);
		}
		this.skill = temp3;
	}

	public static Unit[] loadPlayerDataFile(int ID) {
		System.out.print("Load Player Data File : ");
		System.out.flush();
		try {
			// csvファイルはBOMなしUTF-8
			final String FILE_NAME = "src/csv/PlayerData" + String.valueOf(ID) + ".csv";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), "UTF-8"));

			// ヘッダーの確認
			if (!br.readLine().equals("UnitData,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,")) {
				br.close();
				throw new RuntimeException("ERROR : " + FILE_NAME);
			}
			br.readLine();

			String line;
			ArrayList<PlayerUnit> player = new ArrayList<PlayerUnit>();
			while ((line = br.readLine()) != null) {
				final String[] tokens = line.split(",");

				String[] params = new String[Game.NUM_PARAM];
				for (int i = 0; i < Game.NUM_PARAM; i++) {
					params[i] = tokens[9 + i];
				}

				String[] paramsGrowth = new String[Game.NUM_PARAM];
				for (int i = 0; i < Game.NUM_PARAM; i++) {
					paramsGrowth[i] = tokens[9 + Game.NUM_PARAM + i];
				}

				String[] weaponLevel = new String[Game.NUM_WEAPON_TYPE];
				for (int i = 0; i < Game.NUM_WEAPON_TYPE; i++) {
					weaponLevel[i] = tokens[9 + Game.NUM_PARAM + Game.NUM_PARAM + i];
				}

				String[] skill = new String[Game.NUM_SKILL];
				for (int i = 0; i < Game.NUM_SKILL; i++) {
					skill[i] = tokens[9 + Game.NUM_PARAM + Game.NUM_PARAM + Game.NUM_WEAPON_TYPE + i];
				}

				player.add(new PlayerUnit(//
						tokens[0], // マップのX座標
						tokens[1], // マップのY座標
						tokens[2], // 名前
						tokens[3], // 説明
						tokens[4], // 種族
						tokens[5], // 兵種
						tokens[6], // 武器
						tokens[7], // レベル
						tokens[8], // 移動
						params, // パラメーター
						paramsGrowth, // パラメーター成長率
						weaponLevel, // 武器レベル
						skill// スキル
				));
			}
			br.close();
			System.out.println("OK");
			return player.toArray(new Unit[0]);
		} catch (IOException e) {
			System.out.println("ERROR");
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public static Unit[] loadEnemyDataFile(int ID) {
		System.out.print("Load Enemy Data File : ");
		System.out.flush();
		try {
			// csvファイルはBOMなしUTF-8
			final String FILE_NAME = "src/csv/EnemyData" + String.valueOf(ID) + ".csv";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), "UTF-8"));

			// ヘッダーの確認
			if (!br.readLine().equals("EnemyData,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,")) {
				br.close();
				throw new RuntimeException("ERROR : " + FILE_NAME);
			}
			br.readLine();

			String line;
			ArrayList<Unit> enemy = new ArrayList<Unit>();
			while ((line = br.readLine()) != null) {
				final String[] tokens = line.split(",");

				String[] params = new String[Game.NUM_PARAM];
				for (int i = 0; i < Game.NUM_PARAM; i++) {
					params[i] = tokens[9 + i];
				}

				String[] weaponLevel = new String[Game.NUM_WEAPON_TYPE];
				for (int i = 0; i < Game.NUM_WEAPON_TYPE; i++) {
					weaponLevel[i] = tokens[9 + Game.NUM_PARAM + i];
				}

				String[] skill = new String[Game.NUM_SKILL];
				for (int i = 0; i < Game.NUM_SKILL; i++) {
					skill[i] = tokens[9 + Game.NUM_PARAM + Game.NUM_WEAPON_TYPE + i];
				}

				enemy.add(new Unit(tokens[0], // マップのX座標
						tokens[1], // マップのY座標
						tokens[2], // 名前
						tokens[3], // 説明
						tokens[4], // 種族
						tokens[5], // 兵種
						tokens[6], // 武器
						tokens[7], // レベル
						tokens[8], // 移動
						params, // パラメータ
						weaponLevel, // 武器レベル
						skill// スキル
				));
			}
			br.close();
			System.out.println("OK");
			return enemy.toArray(new Unit[0]);
		} catch (IOException e) {
			System.out.println("ERROR");
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
