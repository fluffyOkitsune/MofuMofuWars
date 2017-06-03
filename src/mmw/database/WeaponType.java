package mmw.database;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.imageio.ImageIO;

/** 武器のタイプ */
public enum WeaponType {
	剣("剣", "命中が高く、追撃ができる。", 0), //
	槍("槍", "標準的な武器", 1), //
	斧("斧", "自分から攻撃を仕掛けると与えるダメージが増える", 2), //
	弓("弓", "少し離れた相手を攻撃できるが、隣の相手は攻撃できない", 3), //
	理魔法("理魔法", "自然の理で敵を攻撃する魔法。癖がなく、扱いやすい", 4), //
	光魔法("光魔法", "聖なる力で敵を攻撃する魔法。威力は低いが、射程が長い", 5), //
	闇魔法("闇魔法", "邪悪な力で敵を攻撃する魔法。威力が高く、癖が強い", 6), //
	呪い("呪い", "敵を妨害することができる", 7), //
	杖("杖", "味方を援護することができる", 8), //
	銃("銃", "威力と必殺が高い", 9), //
	;
	public final String name;
	public final String explain;
	public static Image img;

	WeaponType(String s1, String s2, int imgID) {
		name = s1;
		explain = s2;
	}

	protected static void loadImgFile() {
		final String FILE_NAME = "src/image/WeaponType.png";
		System.out.print("Load Image File(" + FILE_NAME + "): ");
		System.out.flush();
		try {
			img = ImageIO.read(new File(FILE_NAME));
			System.out.println("OK");
		} catch (IOException e) {
			System.out.println("ERROR");
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

}
