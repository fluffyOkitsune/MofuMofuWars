package mmw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

/** �X�e�[�W�����Ǘ� */
public class StageMng {
	MapChipProperty[][] mcp;
	Stage[] stage;

	StageMng() {
		// �摜���ƂɃ}�b�v�`�b�v��`
		mcp = new MapChipProperty[1][10];
		// ����
		mcp[0][0] = new MapChipProperty(0, Terrain.PLAINS, 13, 0, 0, 1, 1);
		// ��
		mcp[0][1] = new MapChipProperty(0, Terrain.GRASS, 1, 10, 1, 0, 0);

		// �����߂�
		stage = new Stage[1];
		stage[0] = new Stage(0);
		stage[0].readFile();
	}

	int getMapData(final int stageID, final int X, final int Y) {
		return stage[stageID].map.get(Y).get(X);
	}

	void update() {

	}

	/** �X�e�[�W�`�� */
	void draw(int stageID) {
		final int ID = stageID;
		final int MAP_COL = stage[ID].mapCol;
		final int MAP_ROW = stage[ID].mapRow;
		final Image FIELD = Game.game.createImage(MAP_COL * Game.CHIP_SIZE,
				MAP_ROW * Game.CHIP_SIZE);
		final Graphics G = FIELD.getGraphics();

		for (int i = 0; i < stage[stageID].numEnemy; i++) {
			final int X = stage[stageID].enemy[i].pos.x;
			final int Y = stage[stageID].enemy[i].pos.y;
			Game.game.g.setColor(new Color(0, 0, 240));
			Game.game.g.fillOval(X, Y, Game.CHIP_SIZE, Game.CHIP_SIZE);
		}

		drawField(Game.game.stageNo, G);
		drawChar(Game.game.stageNo, G);

		// �]��
		Game.game.g.drawImage(FIELD, 20, 20, 2 * MAP_COL * Game.CHIP_SIZE, 2
				* MAP_ROW * Game.CHIP_SIZE, 0, 0, MAP_COL * Game.CHIP_SIZE,
				MAP_ROW * Game.CHIP_SIZE, Game.game);

		drawTerrainInfo(Game.game.stageNo, Game.game.cursol.x,
				Game.game.cursol.y);
	}

	/** �퓬�}�b�v�`�� */
	void drawField(final int ID, final Graphics G) {
		final int CHIP_IMG_ID = stage[ID].chipImgID;
		final int MAP_COL = stage[ID].mapCol;
		final int MAP_ROW = stage[ID].mapRow;

		for (int i = 0; i < MAP_COL; i++) {
			for (int j = 0; j < MAP_ROW; j++) {
				int imgX = mcp[CHIP_IMG_ID][getMapData(ID, i, j)].imgX;
				int imgY = mcp[CHIP_IMG_ID][getMapData(ID, i, j)].imgY;
				// �����z�u���邾���̒n�`
				if (mcp[CHIP_IMG_ID][getMapData(ID, i, j)].type == 0) {
					int sizeX = mcp[CHIP_IMG_ID][getMapData(ID, i, j)].sizeX;
					int sizeY = mcp[CHIP_IMG_ID][getMapData(ID, i, j)].sizeY;
					G.drawImage(Game.imgField[CHIP_IMG_ID], i * Game.CHIP_SIZE,
							j * Game.CHIP_SIZE, (i + sizeX) * Game.CHIP_SIZE,
							(j + sizeY) * Game.CHIP_SIZE,
							imgX * Game.CHIP_SIZE, imgY * Game.CHIP_SIZE,
							(imgX + sizeX) * Game.CHIP_SIZE, (imgY + sizeY)
									* Game.CHIP_SIZE, Game.game);

					// ���͂ɍ��킹�ό`����n�`
				} else if (mcp[CHIP_IMG_ID][getMapData(ID, i, j)].type == 1) {
					if (i == 0
							|| getMapData(ID, i - 1, j) == getMapData(ID, i, j))
						imgX += 1;
					if (i == MAP_COL - 1
							|| getMapData(ID, i + 1, j) == getMapData(ID, i, j))
						imgX -= 1;
					if (j == 0
							|| getMapData(ID, i, j - 1) == getMapData(ID, i, j))
						imgY += 1;
					if (j == MAP_ROW
							|| getMapData(ID, i, j + 1) == getMapData(ID, i, j))
						imgY -= 1;
					G.drawImage(Game.imgField[CHIP_IMG_ID], i * Game.CHIP_SIZE,
							j * Game.CHIP_SIZE, (i + 1) * Game.CHIP_SIZE,
							(j + 1) * Game.CHIP_SIZE, imgX * Game.CHIP_SIZE,
							imgY * Game.CHIP_SIZE, (imgX + 1) * Game.CHIP_SIZE,
							(imgY + 1) * Game.CHIP_SIZE, Game.game);
				}
			}
		}
		// �J�[�\��
		G.setColor(Color.BLUE);
		G.drawRect(Game.game.cursol.x * Game.CHIP_SIZE, Game.game.cursol.y
				* Game.CHIP_SIZE, Game.CHIP_SIZE, Game.CHIP_SIZE);

	}

	/** ���j�b�g�̕\�� */
	void drawChar(int stageID, final Graphics G) {
		G.setColor(new Color(240, 0, 0));
		for (int i = 0; i < stage[stageID].numEnemy; i++) {
			int X = stage[stageID].enemy[i].pos.x * Game.CHIP_SIZE;
			int Y = stage[stageID].enemy[i].pos.y * Game.CHIP_SIZE;
			G.fillOval(X, Y, Game.CHIP_SIZE, Game.CHIP_SIZE);
		}
	}

	/** �n�`���ʏ��\�� */
	void drawTerrainInfo(int ID, final int X, final int Y) {
		final String NAME = mcp[stage[ID].chipImgID][getMapData(ID, X, Y)].terrain.name;
		final int GUARD = mcp[stage[ID].chipImgID][getMapData(ID, X, Y)].terrain.guard;
		final int DODGE = mcp[stage[ID].chipImgID][getMapData(ID, X, Y)].terrain.dodge;
		final int CURE = mcp[stage[ID].chipImgID][getMapData(ID, X, Y)].terrain.cure;
		Game.game.g.setColor(Color.WHITE);
		Game.game.g.drawString(NAME, 10, 40);
		Game.game.g.drawString("�h��:" + GUARD + "%", 10, 50);
		Game.game.g.drawString("���:" + DODGE + "%", 10, 60);
		Game.game.g.drawString("��:" + CURE + "%", 10, 70);
	}

}

/** �X�e�[�W��� */
class Stage {
	int ID;
	String name;
	int chipImgID;

	int mapCol, mapRow;
	ArrayList<ArrayList<Integer>> map;

	int numEnemy;
	Character[] enemy;

	Stage(int ID) {
		this.ID = ID;

	}

	void readFile() {
		// �t�@�C����ǂݍ���
		FileReader fr;

		// �}�b�v�f�[�^
		try {
			fr = new FileReader("src/MapData" + String.valueOf(ID) + ".csv");
			BufferedReader br = new BufferedReader(fr);

			try {
				String line;
				StringTokenizer token;
				line = br.readLine();
				token = new StringTokenizer(line, ",");
				System.out.println(token.nextToken());

				line = br.readLine();
				token = new StringTokenizer(line, ",");
				name = token.nextToken();
				chipImgID = Integer.parseInt(token.nextToken());
				mapCol = Integer.parseInt(token.nextToken());
				mapRow = Integer.parseInt(token.nextToken());

				map = new ArrayList<ArrayList<Integer>>();
				while ((line = br.readLine()) != null) {
					token = new StringTokenizer(line, ",");
					ArrayList<Integer> array = new ArrayList<Integer>();
					while (token.hasMoreTokens()) {
						array.add(Integer.parseInt(token.nextToken()));
					}
					map.add(array);
				}
				br.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// �G�R�f�[�^
		try {
			fr = new FileReader("src/EnemyData" + String.valueOf(ID) + ".csv");
			BufferedReader br = new BufferedReader(fr);

			try {
				String line;
				StringTokenizer token;
				line = br.readLine();
				token = new StringTokenizer(line, ",");
				System.out.println(token.nextToken());

				// #StageInfo
				line = br.readLine();
				token = new StringTokenizer(line, ",");
				numEnemy = Integer.parseInt(token.nextToken());

				enemy = new Character[numEnemy];
				for (int i = 0; i < numEnemy; i++) {
					line = br.readLine();
					token = new StringTokenizer(line, ",");
					final int POS_X = Integer.parseInt(token.nextToken());
					final int POS_Y = Integer.parseInt(token.nextToken());
					final String NAME = token.nextToken();
					final String SPECIES = token.nextToken();
					final byte TYPE = Byte.parseByte(token.nextToken());
					final byte WEAPON = Byte.parseByte(token.nextToken());
					final byte LEVEL = Byte.parseByte(token.nextToken());
					final byte POWER = Byte.parseByte(token.nextToken());
					final byte MAGICP = Byte.parseByte(token.nextToken());
					final byte GUARD = Byte.parseByte(token.nextToken());
					final byte MAGICG = Byte.parseByte(token.nextToken());
					final byte DEXTERITY = Byte.parseByte(token.nextToken());
					final byte DODGE = Byte.parseByte(token.nextToken());
					final byte FLUFFY = Byte.parseByte(token.nextToken());
					final byte[] WEAPONL = new byte[9];
					for (int j = 0; j < 9; j++) {
						WEAPONL[j] = Byte.parseByte(token.nextToken());
					}
					final byte[] SKILL = new byte[5];
					for (byte j = 0; j < 5; j++) {
						SKILL[j] = Byte.parseByte(token.nextToken());
					}
					enemy[i] = new Character(POS_X, POS_Y, NAME, SPECIES, TYPE,
							WEAPON, LEVEL, POWER, MAGICP, GUARD, MAGICG,
							DEXTERITY, DODGE, FLUFFY, WEAPONL, SKILL);
				}
				br.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}

/** �}�b�v�̃`�b�v���Ƃ̑��� */
class MapChipProperty {
	final int ID;
	Terrain terrain;
	byte imgX;
	byte imgY;
	byte type;
	byte sizeX;
	byte sizeY;

	/**
	 * �}�b�v�`�b�v�̑���
	 * 
	 * @param ID
	 *            �L�[
	 * @param terrain
	 *            �n�`����
	 * @param imgX
	 *            �摜�̍��WX
	 * @param imgY
	 *            �摜�̍��WY
	 * @param type
	 *            �摜�̕\���^�C�v 0 ���ʂɕ\�� 1 ����ɍ��킹�ĕ\��
	 * @param sizeX
	 *            �@type=0�ŕ\�����镝
	 * @param sizeY
	 *            �@type=0�ŕ\�����鍂��
	 */
	MapChipProperty(int ID, Terrain terrain, int imgX, int imgY, int type,
			int sizeX, int sizeY) {
		this.ID = ID;
		this.terrain = terrain;
		this.imgX = (byte) imgX;
		this.imgY = (byte) imgY;
		this.type = (byte) type;
		this.sizeX = (byte) sizeX;
		this.sizeY = (byte) sizeY;
	}
}