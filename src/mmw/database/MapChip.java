package mmw.database;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.imageio.ImageIO;

/** マップのチップごとの属性 */
public class MapChip {
	private static HashMap<String, Image> IMG_FIELD;
	private static HashMap<Integer, MapChip> MAP_CHIP;

	public final int ID;
	public final Terrain terrain;
	public final String imgFileName;
	public final byte imgX;
	public final byte imgY;
	public final byte type;
	public final byte sizeX;
	public final byte sizeY;

	/**
	 * マップチップの属性
	 * 
	 * @param ID
	 *            キー
	 * @param terrain
	 *            地形効果
	 * @param imgFileName
	 *            画像ファイルの名前
	 * @param imgX
	 *            画像の座標X
	 * @param imgY
	 *            画像の座標Y
	 * @param type
	 *            画像の表示タイプ 0 普通に表示 1 周りに合わせて表示
	 * @param sizeX
	 * 
	 *            type=0で表示する幅
	 * @param sizeY
	 *            type=0で表示する高さ
	 */

	public MapChip(String ID, String terrain, String imgFileName, String imgX, String imgY, String type, String sizeX,
			String sizeY) {
		this.ID = Integer.parseInt(ID);
		this.terrain = Terrain.valueOf(terrain);
		this.imgFileName = imgFileName;
		this.imgX = Byte.parseByte(imgX);
		this.imgY = Byte.parseByte(imgY);
		this.type = Byte.parseByte(type);
		this.sizeX = Byte.parseByte(sizeX);
		this.sizeY = Byte.parseByte(sizeY);
	}

	public static final void loadMapChipFile() {
		System.out.print("Load Map Chip Data File : ");
		System.out.flush();
		try {
			// csvファイルはBOMなしUTF-8
			final String FILE_NAME = "src/csv/MapChip.csv";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), "UTF-8"));

			// ヘッダーの確認
			if (!br.readLine()
					.equals("MAPCHIP,,,,,,,")) {
				br.close();
				throw new RuntimeException("ERROR : " + FILE_NAME);
			}
			br.readLine();

			// map_chip_data
			String line;
			MAP_CHIP = new HashMap<Integer, MapChip>();
			IMG_FIELD = new HashMap<String, Image>();

			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				MAP_CHIP.put(Integer.valueOf(tokens[0]), (new MapChip(tokens[0], tokens[1], tokens[2], tokens[3],
						tokens[4], tokens[5], tokens[6], tokens[7])));

				if (!IMG_FIELD.containsKey(tokens[2])) {
					IMG_FIELD.put(tokens[2], loadImgFile(tokens[2]));
				}
			}
			br.close();
			System.out.println("OK");
		} catch (IOException e) {
			System.out.println("ERROR");
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	protected static Image loadImgFile(final String FILE_NAME) {
		System.out.print("Load Image File(" + FILE_NAME + "): ");
		System.out.flush();
		try {
			Image img = ImageIO.read(new File(FILE_NAME));
			System.out.println("OK");
			return img;
		} catch (IOException e) {
			System.out.println("ERROR");
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public static final MapChip getMapChip(final int ID) {
		return MAP_CHIP.get(Integer.valueOf(ID));
	}

	public final Image getImage() {
		return IMG_FIELD.get(this.imgFileName);
	}

}