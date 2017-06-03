package mmw.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import mmw.unit.Unit;

public class StageData {
	public final int ID;
	public final String name;
	public final int mapSizeWidth;
	public final int mapSizeHeight;
	public final int[][] map;
	public final Unit[] player;
	public final Unit[] enemy;

	int numEnemy;

	public StageData(int ID, String name, int[][] map) {
		this.ID = ID;
		this.name = name;
		this.mapSizeWidth = map.length;
		this.mapSizeHeight = map[0].length;
		this.map = map;
		this.player = Unit.loadPlayerDataFile(ID);
		this.enemy = Unit.loadEnemyDataFile(ID);
	}

	public static final StageData loadStageDataFile(int ID) {
		System.out.print("Load Stage Data File : ");
		System.out.flush();
		try {
			// csvファイルはBOMなしUTF-8
			final String FILE_NAME = "src/csv/MapData" + String.valueOf(ID)
					+ ".csv";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(FILE_NAME), "UTF-8"));

			String line;
			String[] tokens;

			// ヘッダーの確認
			if (!br.readLine().split(",")[0].equals("MapData")) {
				br.close();
				throw new RuntimeException("ERROR : " + FILE_NAME);
			}

			// name
			tokens = br.readLine().split(",");
			final String name = tokens[0];

			// map_data
			ArrayList<String[]> temp = new ArrayList<String[]>();
			while ((line = br.readLine()) != null) {
				temp.add(line.split(","));
			}
			final int width = temp.get(0).length;
			final int height = temp.size();
			final int[][] map = new int[width][height];
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					map[i][j] = Integer.parseInt(temp.get(j)[i]);
				}
			}

			br.close();
			System.out.println("OK");
			return new StageData(ID, name, map);
		} catch (IOException e) {
			System.out.println("ERROR");
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
