package mmw.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/** 武器 */
public class Weapon {
	private static Weapon[] weaponList;

	/** 武器の名前 */
	public final String name;
	/** 武器の耐久値 */
	public final int durablity;
	/** 売値 */
	public final int price;
	/** 武器の種類 */
	public final WeaponType type;
	/** 必要な武器レベル */
	public final char level;
	/** 威力 */
	public final int atk;
	/** 命中補正値 */
	public final int hit;
	/** 回避補正値 */
	public final int dodge;
	/** 必殺補正値 */
	public final int critical;
	/** 射程 */
	public final int reach;
	/** 隣接マスを攻撃できるか */
	public final boolean canNextAttack;
	/** 武器がもつ属性 */
	public final WeaponAttr attr;
	/** 武器の説明 */
	public final String explain;

	public Weapon(String name, String durablity, String price, String type,
			String level, String atk, String hit, String dodge,
			String critical, String reach, String canNextAttack, String attr,
			String explain) {
		this.name = name;
		this.durablity = Integer.parseInt(durablity);
		this.price = Integer.parseInt(price);
		this.type = WeaponType.valueOf(type);
		this.level = level.charAt(0);
		this.atk = Integer.parseInt(atk);
		this.hit = Integer.parseInt(hit);
		this.dodge = Integer.parseInt(dodge);
		this.critical = Integer.parseInt(critical);
		this.reach = Integer.parseInt(reach);
		this.canNextAttack = Boolean.parseBoolean(canNextAttack);
		this.attr = attr.equals("") ? WeaponAttr.なし : WeaponAttr.valueOf(attr);
		this.explain = explain;
	}

	public static final void loadWeaponFile() {
		System.out.print("Load Weapon Data File : ");
		System.out.flush();
		try {
			// csvファイルはBOMなしUTF-8
			final String FILE_NAME = "src/csv/Weapon.csv";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(FILE_NAME), "UTF-8"));

			// ヘッダーの確認
			if (!br.readLine().equals("WeaponData,,,,,,,,,,,,")) {
				br.close();
				throw new RuntimeException("ERROR : " + FILE_NAME);
			}
			br.readLine();

			// weapon_data
			String line;
			ArrayList<Weapon> weapon = new ArrayList<Weapon>();
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				weapon.add(new Weapon(tokens[0], tokens[1], tokens[2],
						tokens[3], tokens[4], tokens[5], tokens[6], tokens[7],
						tokens[8], tokens[9], tokens[10], tokens[11],
						tokens[12]));
			}
			br.close();
			System.out.println("OK");
			weaponList = weapon.toArray(new Weapon[0]);
		} catch (IOException e) {
			System.out.println("ERROR");
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public static final Weapon valueOf(String name) {
		for (Weapon w : weaponList) {
			if (name.equals(w.name))
				return w;
		}
		throw new IllegalArgumentException(name);
	}
}