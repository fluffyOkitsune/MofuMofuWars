package mmw.scene.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import mmw.Game;
import mmw.database.MapChip;
import mmw.database.StageData;
import mmw.database.Terrain;
import mmw.database.WeaponType;
import mmw.help_window.HelpWindow;
import mmw.help_window.HelpWindowUnitOnBoard;
import mmw.obj.UnitOnBoard;
import mmw.scene.Scene;
import mmw.scene.unit_info.SceneUnitInfo;
import mmw.unit.Unit;

/** 対戦画面 */
public class SceneBoard extends Scene {
	private StageData stageData;
	private UnitOnBoard[] player;
	private UnitOnBoard selectedUnit;
	private Point[] route;
	private UnitOnBoard[] enemy;

	public HelpWindow helpWindow;

	// 画面
	public static final int CHIP_MAG = 1;
	public static final int CHIP_COL = Game.WINDOW_WIDTH / Game.CHIP_SIZE / CHIP_MAG;
	public static final int CHIP_RAW = Game.WINDOW_HEIGHT / Game.CHIP_SIZE / CHIP_MAG;
	public static final int SCROLL_BORDER = 2;

	BufferedImage imgField;
	BufferedImage imgUnit;

	BufferedImage frameTerrain;

	// カーソルの座標
	public static final int CURSOL_NONE = 0;
	public static final int CURSOL_UP = 0;
	public static final int CURSOL_DOWN = 1;
	public static final int CURSOL_LEFT = 2;
	public static final int CURSOL_RIGHT = 3;
	public static final int[] CURSOL_X = { 0, 0, -1, 1 };
	public static final int[] CURSOL_Y = { -1, 1, 0, 0 };
	public static final int CURSOL_MOVE_TIME = 5;

	Point cursolPos;
	int cursolTime;
	int cursolDir;

	// ユニットの移動アニメ
	public boolean isUnitMoving;

	// 画面に表示するエリア
	Point offset;
	Point drawRange;
	boolean doScroll;

	// プレイ中のステージ番号
	int stageID;

	private SceneUnitInfo winUnitInfo;
	private boolean updateNeeded;

	public SceneBoard(Game g, int ID) {
		super(g);
		stageID = ID;

		createStageData(stageID);

		cursolPos = new Point(0, 0);
		cursolTime = 0;
		cursolDir = 0;

		drawRange = new Point(0, 0);
		offset = new Point(0, 0);

		imgField = createFieldImg(stageID);
		imgUnit = createUnitImg(stageID);
		createAttackRange();

		update();
	}

	@Override
	public boolean run() {
		if (cursolTime > 0) {
			updateNeeded = true;
			cursolTime--;
			if (cursolTime == 0)
				doScroll = false;
		}

		updateNeeded = unitRun() || updateNeeded;

		if (updateNeeded) {
			update();
			updateNeeded = false;
		}
		return false;

	}

	private boolean unitRun() {
		boolean updateNeeded = false;
		for (UnitOnBoard u : player)
			updateNeeded = u.run() || updateNeeded;
		for (UnitOnBoard u : enemy)
			updateNeeded = u.run() || updateNeeded;
		return updateNeeded;
	}

	/** ステージデータ作成 */
	void createStageData(int ID) {
		stageData = StageData.loadStageDataFile(ID);
		// 自軍
		ArrayList<UnitOnBoard> temp = new ArrayList<UnitOnBoard>();
		for (Unit u : stageData.player) {
			temp.add(new UnitOnBoard(stageData, this, u));
		}
		player = temp.toArray(new UnitOnBoard[0]);

		// 敵軍
		temp = new ArrayList<UnitOnBoard>();
		for (Unit u : stageData.enemy) {
			temp.add(new UnitOnBoard(stageData, this, u));
		}
		enemy = temp.toArray(new UnitOnBoard[0]);
	}

	public void createAttackRange() {
		for (UnitOnBoard u : player)
			u.makeAttackRange(stageData.map);
		for (UnitOnBoard u : enemy)
			u.makeAttackRange(stageData.map);
	}

	// ----------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------
	@Override
	public void keyPressed(KeyEvent e) {
		if (cursolTime == 0 && !isUnitMoving) {
			int key = e.getKeyCode();

			if (helpWindow == null) {
				switch (key) {
				case KeyEvent.VK_UP:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
					moveCursol(e);
					break;

				case KeyEvent.VK_Z:
					if (selectedUnit != null && route != null) {
						// 選択したユニットの移動
						moveUnit(selectedUnit, route);
						selectedUnit = null;
						route = null;

					} else {
						// ユニットの選択
						selectedUnit = getPlayerUnitAt(cursolPos.x, cursolPos.y);
					}

					updateNeeded = true;
					break;

				case KeyEvent.VK_X:
					if (selectedUnit != null) {
						// ユニット選択のキャンセル
						selectedUnit = null;
						route = null;

					} else {
						// 攻撃範囲の表示
						UnitOnBoard unit;
						if ((unit = getEnemyUnitAt(cursolPos.x, cursolPos.y)) != null) {
							unit.drawAttackRange = !unit.drawAttackRange;
						}
						updateNeeded = true;
						break;
					}

				case KeyEvent.VK_H:// ヘルプウィンドウ
					UnitOnBoard unit;
					if ((unit = getEnemyUnitAt(cursolPos.x, cursolPos.y)) != null) {
						helpWindow = new HelpWindowUnitOnBoard(GAME, unit, cursolPos.x - drawRange.x,
								cursolPos.y - drawRange.y);
					}
					updateNeeded = true;
					break;

				}

				if (selectedUnit != null) {
					Point unitPos = new Point(selectedUnit.pos.x, selectedUnit.pos.y);
					route = Search.SearchRoute(this, stageData, selectedUnit, unitPos, cursolPos);

					// DEBUG
					if (true && route != null) {
						for (int i = 0; i < route.length; i++)
							System.out.println("ROUTE : " + route[i].x + "," + route[i].y);
					}
				}

			} else {
				helpWindow.keyPressed(e);
				updateNeeded = true;
			}
			System.out.println("(" + cursolPos.x + "," + cursolPos.y + ")");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	// カーソルの移動
	private void moveCursol(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			if (cursolPos.y > 0) {
				cursolPos.y--;
				// 画面のスクロール
				if (drawRange.y > 0 && cursolPos.y < drawRange.y + SCROLL_BORDER) {
					drawRange.y--;
					doScroll = true;
				}
				cursolTime = CURSOL_MOVE_TIME;
				cursolDir = CURSOL_UP;
			}
			updateNeeded = true;
			break;

		case KeyEvent.VK_DOWN:
			if (cursolPos.y + 1 < stageData.mapSizeHeight) {
				cursolPos.y++;
				// 画面のスクロール
				if (drawRange.y + CHIP_RAW < stageData.mapSizeHeight
						&& cursolPos.y >= drawRange.y + CHIP_RAW - 1 - SCROLL_BORDER) {
					drawRange.y++;
					doScroll = true;
				}
				cursolTime = CURSOL_MOVE_TIME;
				cursolDir = CURSOL_DOWN;
			}
			updateNeeded = true;
			break;

		case KeyEvent.VK_LEFT:
			if (cursolPos.x > 0) {
				cursolPos.x--;
				// 画面のスクロール
				if (drawRange.x > 0 && cursolPos.x < drawRange.x + SCROLL_BORDER) {
					drawRange.x--;
					doScroll = true;
				}
				cursolTime = CURSOL_MOVE_TIME;
				cursolDir = CURSOL_LEFT;
			}
			updateNeeded = true;
			break;

		case KeyEvent.VK_RIGHT:
			if (cursolPos.x + 1 < stageData.mapSizeWidth) {
				cursolPos.x++;
				// 画面のスクロール
				if (drawRange.x + CHIP_COL < stageData.mapSizeWidth
						&& cursolPos.x >= drawRange.x + CHIP_COL - 1 - SCROLL_BORDER) {
					drawRange.x++;
					doScroll = true;
				}
				cursolTime = CURSOL_MOVE_TIME;
				cursolDir = CURSOL_RIGHT;
			}
			updateNeeded = true;
			break;
		}
	}

	// ユニットの移動アニメーション
	private void moveUnit(UnitOnBoard unit, Point[] route) {
		unit.moveTo(route);
		isUnitMoving = true;
	}

	// ----------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------
	@Override
	public void update() {
		Graphics g = LAYER_G;
		final int MAP_COL = stageData.mapSizeWidth;
		final int MAP_ROW = stageData.mapSizeHeight;

		imgUnit = createUnitImg(stageID);

		final BufferedImage FIELD = new BufferedImage(MAP_COL * Game.CHIP_SIZE, MAP_ROW * Game.CHIP_SIZE,
				BufferedImage.TYPE_INT_ARGB);
		final Graphics FIELD_G = FIELD.getGraphics();

		drawField(FIELD_G);
		drawCursol(FIELD_G);

		// フィールドの描画
		g.drawImage(FIELD, 0, 0, CHIP_MAG * MAP_COL * Game.CHIP_SIZE, CHIP_MAG * MAP_ROW * Game.CHIP_SIZE, 0, 0,
				MAP_COL * Game.CHIP_SIZE, MAP_ROW * Game.CHIP_SIZE, GAME);

		// 地形効果の表示
		drawTerrainInfo(g, cursolPos.x, cursolPos.y);
		drawUnitInfo(g, cursolPos.x, cursolPos.y);

		// ユニット情報の描画
		if (winUnitInfo != null)
			winUnitInfo.draw(g);

		// ヘルプ画面の描画
		if (helpWindow != null)
			helpWindow.draw(g);

	}

	/** マップ画像の作成 */
	private final BufferedImage createFieldImg(final int ID) {
		final int MAP_COL = stageData.mapSizeWidth;
		final int MAP_ROW = stageData.mapSizeHeight;

		final BufferedImage FIELD_IMG = new BufferedImage(MAP_COL * Game.CHIP_SIZE, MAP_ROW * Game.CHIP_SIZE,
				BufferedImage.TYPE_INT_ARGB);
		final Graphics G = FIELD_IMG.getGraphics();

		for (int i = 0; i < MAP_COL; i++) {
			for (int j = 0; j < MAP_ROW; j++) {
				final MapChip MAP_CHIP = MapChip.getMapChip(getMapData(i, j));
				int imgX = MAP_CHIP.imgX;
				int imgY = MAP_CHIP.imgY;

				if (MAP_CHIP.type == 0) {
					// ただ配置するだけの地形
					final int sizeX = MAP_CHIP.sizeX;
					final int sizeY = MAP_CHIP.sizeY;
					G.drawImage(MAP_CHIP.getImage(), i * Game.CHIP_SIZE, j * Game.CHIP_SIZE,
							(i + sizeX) * Game.CHIP_SIZE, (j + sizeY) * Game.CHIP_SIZE, imgX * Game.CHIP_SIZE,
							imgY * Game.CHIP_SIZE, (imgX + sizeX) * Game.CHIP_SIZE, (imgY + sizeY) * Game.CHIP_SIZE,
							GAME);

				} else if (MAP_CHIP.type == 1) {
					for (int k = 0; k < 4; k++) {
						int sgnX = 0;
						int sgnY = 0;

						// 符号
						switch (k) {
						case 0:
							sgnX = 1;
							sgnY = 1;
							break;
						case 1:
							sgnX = -1;
							sgnY = 1;
							break;
						case 2:
							sgnX = 1;
							sgnY = -1;
							break;
						case 3:
							sgnX = -1;
							sgnY = -1;
							break;
						}

						// 周囲に合わせ変形する地形
						int centerX = 1;
						int centerY = 2;
						int here = getMapData(i, j);

						// 隣のマスがここと同じチップイメージか
						boolean nextX = (!(0 <= i - sgnX && i - sgnX < MAP_COL) || getMapData(i - sgnX, j) == here);
						boolean nextY = (!(0 <= j - sgnY && j - sgnY < MAP_ROW) || getMapData(i, j - sgnY) == here);

						if (!nextX && !nextY) {
							// 隅になる場合
							centerX -= sgnX;
							centerY -= sgnY;

						} else if (nextX && !nextY) {
							// 横向きの辺になる場合
							centerY -= sgnY;

						} else if (!nextX && nextY) {
							// 縦向きの辺になる場合
							centerX -= sgnX;

						} else if (nextX && nextY) {
							if ((i - sgnX < 0 || i - sgnX > MAP_COL - 1) || (j - sgnY < 0 || j - sgnY > MAP_ROW - 1)
									|| getMapData(i - sgnX, j - sgnY) == here) {
								// 中心になる場合
								centerX = 1;
								centerY = 2;

							} else {
								// 凹みになる場合
								centerX = 2;
								centerY = 0;

							}
						}

						int imgDrawX = i * Game.CHIP_SIZE;
						int imgDrawY = j * Game.CHIP_SIZE;
						int srcDrawX = (imgX + centerX) * Game.CHIP_SIZE;
						int srcDrawY = (imgY + centerY) * Game.CHIP_SIZE;
						int[] fromX = { 0, Game.CHIP_SIZE / 2, 0, Game.CHIP_SIZE / 2 };
						int[] fromY = { 0, 0, Game.CHIP_SIZE / 2, Game.CHIP_SIZE / 2 };
						int[] toX = { Game.CHIP_SIZE / 2, Game.CHIP_SIZE, Game.CHIP_SIZE / 2, Game.CHIP_SIZE };
						int[] toY = { Game.CHIP_SIZE / 2, Game.CHIP_SIZE / 2, Game.CHIP_SIZE, Game.CHIP_SIZE };

						G.drawImage(MAP_CHIP.getImage(), imgDrawX + fromX[k], imgDrawY + fromY[k], imgDrawX + toX[k],
								imgDrawY + toY[k], srcDrawX + fromX[k], srcDrawY + fromY[k], srcDrawX + toX[k],
								srcDrawY + toY[k], GAME);
					}
				}
			}
		}
		return FIELD_IMG;
	}

	/** ユニット画像の作成 */
	private final BufferedImage createUnitImg(final int ID) {
		final int MAP_COL = stageData.mapSizeWidth;
		final int MAP_ROW = stageData.mapSizeHeight;

		final BufferedImage IMG = new BufferedImage(MAP_COL * Game.CHIP_SIZE, MAP_ROW * Game.CHIP_SIZE,
				BufferedImage.TYPE_INT_ARGB);
		final Graphics G = IMG.getGraphics();

		// 自軍の描画
		drawPlayerAttackRange(G);
		for (UnitOnBoard c : player) {
			c.draw(G);
		}

		// 敵軍の描画
		drawEnemyAttackRange(G);
		for (UnitOnBoard c : enemy) {
			c.draw(G);
			final int X = c.pos.x;
			final int Y = c.pos.y;
			// 仮に円
			G.setColor(new Color(240, 0, 0));
			G.fillOval(Game.CHIP_SIZE * X, Game.CHIP_SIZE * Y, Game.CHIP_SIZE, Game.CHIP_SIZE);
		}

		drawRoute(G);

		return IMG;
	}

	/** 自軍ユニットの攻撃範囲の表示 */
	void drawPlayerAttackRange(final Graphics G) {
		if (selectedUnit != null) {
			byte[][] range = selectedUnit.getAttackRange();
			for (int i = 0; i < range.length; i++) {
				for (int j = 0; j < range[0].length; j++) {
					if (range[i][j] == 1) {
						// 移動範囲
						G.setColor(new Color(0, 0, 240, 120));
						G.fillRect(Game.CHIP_SIZE * i, Game.CHIP_SIZE * j, Game.CHIP_SIZE, Game.CHIP_SIZE);
					} else if (range[i][j] == 2) {
						// 攻撃範囲
						G.setColor(new Color(240, 0, 0, 120));
						G.fillRect(Game.CHIP_SIZE * i, Game.CHIP_SIZE * j, Game.CHIP_SIZE, Game.CHIP_SIZE);
					} else if (range[i][j] == 3) {
						// 杖の範囲
						G.setColor(new Color(0, 240, 0, 120));
						G.fillRect(Game.CHIP_SIZE * i, Game.CHIP_SIZE * j, Game.CHIP_SIZE, Game.CHIP_SIZE);
					}
				}
			}
		}
	}

	/** 敵軍ユニットの攻撃範囲の表示 */
	void drawEnemyAttackRange(final Graphics G) {
		for (UnitOnBoard c : enemy) {
			if (c.drawAttackRange) {
				byte[][] range = c.getAttackRange();
				for (int i = 0; i < range.length; i++) {
					for (int j = 0; j < range[0].length; j++) {
						if (range[i][j] == 1) {
							// 移動範囲
							G.setColor(new Color(0, 0, 240, 120));
							G.fillRect(Game.CHIP_SIZE * i, Game.CHIP_SIZE * j, Game.CHIP_SIZE, Game.CHIP_SIZE);
						} else if (range[i][j] == 2) {
							// 攻撃範囲
							G.setColor(new Color(240, 0, 0, 120));
							G.fillRect(Game.CHIP_SIZE * i, Game.CHIP_SIZE * j, Game.CHIP_SIZE, Game.CHIP_SIZE);
						} else if (range[i][j] == 3) {
							// 杖の範囲
							G.setColor(new Color(0, 240, 0, 120));
							G.fillRect(Game.CHIP_SIZE * i, Game.CHIP_SIZE * j, Game.CHIP_SIZE, Game.CHIP_SIZE);
						}
					}
				}
			}
		}
	}

	/** 自軍ユニットの移動ルートの描画 */
	void drawRoute(Graphics g) {
		if (route != null) {
			for (int i = 0; i < route.length; i++) {
				g.setColor(new Color(240, 240, 240));
				g.fillOval(Game.CHIP_SIZE * route[i].x + Game.CHIP_SIZE / 3,
						Game.CHIP_SIZE * route[i].y + Game.CHIP_SIZE / 3, Game.CHIP_SIZE / 3, Game.CHIP_SIZE / 3);
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------
	/** 戦闘マップ描画 */
	void drawField(final Graphics G) {
		int x, y;
		if (doScroll) {
			x = -Game.CHIP_SIZE * cursolTime * CURSOL_X[cursolDir] / CURSOL_MOVE_TIME;
			y = -Game.CHIP_SIZE * cursolTime * CURSOL_Y[cursolDir] / CURSOL_MOVE_TIME;
		} else {
			x = 0;
			y = 0;
		}
		int rangeX = Game.CHIP_SIZE * drawRange.x;
		int rangeY = Game.CHIP_SIZE * drawRange.y;

		x = offset.x + rangeX + x;
		y = offset.y + rangeY + y;

		G.drawImage(imgField, 0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, x, y, x + Game.WINDOW_WIDTH,
				y + Game.WINDOW_HEIGHT, GAME);
		G.drawImage(imgUnit, 0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, x, y, x + Game.WINDOW_WIDTH,
				y + Game.WINDOW_HEIGHT, GAME);
	}

	/** カーソルの描画 */
	void drawCursol(final Graphics G) {
		G.setColor(Color.BLUE);
		int x, y;
		if (doScroll) {
			x = (cursolPos.x - drawRange.x) * Game.CHIP_SIZE;
			y = (cursolPos.y - drawRange.y) * Game.CHIP_SIZE;
		} else {
			x = (cursolPos.x - drawRange.x) * Game.CHIP_SIZE
					- Game.CHIP_SIZE * cursolTime * CURSOL_X[cursolDir] / CURSOL_MOVE_TIME;
			y = (cursolPos.y - drawRange.y) * Game.CHIP_SIZE
					- Game.CHIP_SIZE * cursolTime * CURSOL_Y[cursolDir] / CURSOL_MOVE_TIME;
		}
		G.drawRect(x, y, Game.CHIP_SIZE, Game.CHIP_SIZE);
	}

	/** 地形効果情報の表示 */
	void drawTerrainInfo(Graphics g, final int X, final int Y) {
		final MapChip mcp = MapChip.getMapChip(getMapData(X, Y));
		final Terrain t = mcp.terrain;
		final String NAME = t.name;
		final String EXP = t.explain;
		final int GUARD = t.guard;
		final int DODGE = t.dodge;
		final int CURE = t.cure;

		int posX;
		if (cursolPos.x < drawRange.x + 5)
			posX = Game.WINDOW_WIDTH - 100;
		else
			posX = 0;

		g.setColor(Color.BLACK);
		g.fillRect(posX, 0, 100, 80);
		g.setColor(Color.WHITE);
		g.drawRect(posX, 0, 100, 80);
		g.setFont(new Font(null, Font.PLAIN, 16));
		g.drawString(NAME, posX + 10, 20);

		// 地形効果の防御率
		if (GUARD > 0)
			g.setColor(Color.BLUE);
		else if (GUARD < 0)
			g.setColor(Color.RED);
		else
			g.setColor(Color.WHITE);
		g.setFont(new Font(null, Font.PLAIN, 12));
		g.drawString("防御:" + GUARD + "%", posX + 10, 35);

		// 地形効果の回避率
		if (DODGE > 0)
			g.setColor(Color.BLUE);
		else if (DODGE < 0)
			g.setColor(Color.RED);
		else
			g.setColor(Color.WHITE);
		g.setFont(new Font(null, Font.PLAIN, 12));
		g.drawString("回避:" + DODGE + "%", posX + 10, 50);

		// 地形効果の回復率
		if (CURE > 0)
			g.setColor(Color.BLUE);
		else if (CURE < 0)
			g.setColor(Color.RED);
		else
			g.setColor(Color.WHITE);
		g.setFont(new Font(null, Font.PLAIN, 12));
		g.drawString("回復:" + CURE + "%", posX + 10, 65);

		// 説明文の描画
		if (!EXP.equals("")) {
			g.setColor(Color.BLACK);
			g.fillRect(0, Game.WINDOW_HEIGHT - 20, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
			g.setColor(Color.WHITE);
			g.setFont(new Font(null, Font.PLAIN, 12));
			g.drawString(EXP, 10, Game.WINDOW_HEIGHT - 5);
		}
	}

	/** ユニット情報の表示 */
	void drawUnitInfo(Graphics g, final int X, final int Y) {
		final UnitOnBoard UNIT = getUnitAt(X, Y);
		if (UNIT != null) {
			int posX = 2 * Game.CHIP_SIZE * (X - drawRange.x) + 25;
			int posY = 2 * Game.CHIP_SIZE * (Y - drawRange.y) + 25;
			if (X - drawRange.x > 10)
				posX -= 100;
			if (X - drawRange.y > 7)
				posY -= 100;

			final String NAME = UNIT.UNIT.name;
			final String TYPE = UNIT.UNIT.type.name;
			final String WEAPON = UNIT.UNIT.weapon.WEAPON_DATA.name;
			final char[] WEAPON_LEVEL = UNIT.UNIT.weaponLevel;

			g.setColor(Color.BLACK);
			g.fillRect(posX, posY, 100, 90);
			g.setColor(Color.WHITE);
			g.drawRect(posX, posY, 100, 90);

			g.setFont(new Font(null, Font.PLAIN, 15));
			g.drawString(NAME, posX + 10, posY + 20);
			g.setFont(new Font(null, Font.PLAIN, 10));
			g.drawString(TYPE, posX + 10, posY + 40);

			int n = 0;
			for (int i = 0; i < WEAPON_LEVEL.length; i++) {
				if (WEAPON_LEVEL[i] != '-') {
					g.drawImage(WeaponType.img, //
							posX + 10 + 30 * n, posY + 50, //
							posX + 10 + 30 * n + 16, posY + 50 + 16, //
							16 * i, 0, 16 * (i + 1), 16, GAME);
					g.drawString(String.valueOf(WEAPON_LEVEL[i]), posX + 30 + 30 * n, posY + 60);
					n++;
				}
			}
			g.drawString(WEAPON, posX + 10, posY + 80);
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------
	/** 指定座標のマップチップIDの取得 */
	int getMapData(final int X, final int Y) {
		return stageData.map[X][Y];
	}

	/** 指定座標のユニットの取得 */
	public UnitOnBoard getUnitAt(final int X, final int Y) {
		for (UnitOnBoard u : player) {
			if (X == u.pos.x && Y == u.pos.y)
				return u;
		}
		for (UnitOnBoard u : enemy) {
			if (X == u.pos.x && Y == u.pos.y)
				return u;
		}
		return null;
	}

	/** 指定座標の自軍ユニットの取得 */
	public UnitOnBoard getPlayerUnitAt(final int X, final int Y) {
		for (UnitOnBoard u : player) {
			if (X == u.pos.x && Y == u.pos.y)
				return u;
		}
		return null;
	}

	/** 指定座標の敵軍ユニットの取得 */
	public UnitOnBoard getEnemyUnitAt(final int X, final int Y) {
		for (UnitOnBoard u : enemy) {
			if (X == u.pos.x && Y == u.pos.y)
				return u;
		}
		return null;
	}
}
