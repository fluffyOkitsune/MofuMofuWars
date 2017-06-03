package mmw.obj;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import mmw.Game;
import mmw.database.MapChip;
import mmw.database.StageData;
import mmw.database.WeaponType;
import mmw.scene.board.SceneBoard;
import mmw.unit.Unit;

/** ボード上で扱うユニット */
public class UnitOnBoard extends GameObj {
	private static final int MOVING_VEL = 10;

	public final Unit UNIT;

	final SceneBoard SB;
	final StageData STAGE;

	/** ゲーム進行に必要 */
	public boolean alive;
	public Point pos;
	public Point offset;
	public boolean drawAttackRange;

	/** パラメータ補正値 */

	/** 攻撃範囲 */
	private byte[][] attackRange;

	// 移動アニメーション
	Point[] route;
	int moveCnt;
	int moveTime;

	public UnitOnBoard(final StageData STAGE, final SceneBoard SB, final Unit UNIT) {
		this.STAGE = STAGE;
		this.SB = SB;
		this.UNIT = UNIT;
		alive = true;
		pos = new Point(UNIT.pos.x, UNIT.pos.y);
		offset = new Point(0, 0);
		drawAttackRange = false;

		route = null;
		moveCnt = -1;
		moveTime = 0;
	}

	@Override
	public boolean run() {
		if (moveCnt >= 0) {
			moving();
			return true;
		}
		return false;
	}

	public void moveTo(Point[] route) {
		this.route = route;
		moveCnt = 0;
		moveTime = MOVING_VEL;

	}

	// 移動アニメーションの描画
	private void moving() {
		if (moveTime > 0 && moveCnt < route.length - 1) {
			int dx = route[moveCnt + 1].x - route[moveCnt].x;
			int dy = route[moveCnt + 1].y - route[moveCnt].y;
			offset.x = dx * Game.CHIP_SIZE * (MOVING_VEL - moveTime) / MOVING_VEL;
			offset.y = dy * Game.CHIP_SIZE * (MOVING_VEL - moveTime) / MOVING_VEL;
			moveTime--;

		} else {
			if (moveCnt < route.length - 1) {
				moveCnt++;
				pos.x = route[moveCnt].x;
				pos.y = route[moveCnt].y;
				offset.x = 0;
				offset.y = 0;
				moveTime = MOVING_VEL;

			} else {
				// 移動終了
				SB.createAttackRange();
				SB.isUnitMoving = false;
				moveCnt = -1;
			}
		}
	}

	/** 攻撃範囲を表すマップを作製 */
	public void makeAttackRange(final int[][] MAP) {
		int numCols = MAP.length;
		int numRows = MAP[0].length;

		attackRange = new byte[numCols][numRows];
		for (int i = 0; i < numCols; i++) {
			for (int j = 0; j < numRows; j++) {
				attackRange[i][j] = 0;
			}
		}
		searchMoveRange(MAP, pos.x, pos.y, UNIT.move, false);
	}

	public byte[][] getAttackRange() {
		return attackRange;
	}

	/** ユニットの移動範囲を探索 */
	private void searchMoveRange(int[][] MAP, final int X, final int Y, int cost, boolean moved) {
		if (moved)
			cost -= MapChip.getMapChip(STAGE.map[X][Y]).terrain.costH;
		if (cost >= 0) {
			searchAttackRange(X, Y, 0);
			attackRange[X][Y] = 1; // 移動できる

			if (cost > 0) {
				// ↑
				if (Y > 0 && SB.getEnemyUnitAt(X, Y - 1) == null) {
					searchMoveRange(MAP, X, Y - 1, cost, true);
				}
				// ↓
				if (Y < STAGE.mapSizeHeight - 1 && SB.getEnemyUnitAt(X, Y + 1) == null) {
					searchMoveRange(MAP, X, Y + 1, cost, true);
				}
				// ←
				if (X > 0 && SB.getEnemyUnitAt(X - 1, Y) == null) {
					searchMoveRange(MAP, X - 1, Y, cost, true);
				}
				// →
				if (X < STAGE.mapSizeWidth - 1 && SB.getEnemyUnitAt(X + 1, Y) == null) {
					searchMoveRange(MAP, X + 1, Y, cost, true);
				}
			}
		}
	}

	/** ユニットの攻撃範囲を探索 */
	private void searchAttackRange(final int X, final int Y, int reach) {
		int reachTo;
		if (UNIT.weapon.WEAPON_DATA.reach > 0)
			reachTo = UNIT.weapon.WEAPON_DATA.reach;
		else
			// 射程-1は魔力/2
			reachTo = UNIT.params[1] / 2;

		int reachFrom;
		if (!UNIT.weapon.WEAPON_DATA.canNextAttack) {
			if (reachTo > 3)
				// 3マスを超える射程を持つ隣接攻撃できない武器
				reachFrom = 3;
			else
				// 隣接攻撃できない武器
				reachFrom = 2;
		} else
			reachFrom = 1;

		if (reach <= reachTo) {
			if (reach >= reachFrom && attackRange[X][Y] != 1)
				attackRange[X][Y] = 2; // 攻撃できる
			// ↑
			if (Y > 0) {
				searchAttackRange(X, Y - 1, reach + 1);
			}
			// ↓
			if (Y < STAGE.mapSizeHeight - 1) {
				searchAttackRange(X, Y + 1, reach + 1);
			}
			// ←
			if (X > 0) {
				searchAttackRange(X - 1, Y, reach + 1);
			}
			// →
			if (X < STAGE.mapSizeWidth - 1) {
				searchAttackRange(X + 1, Y, reach + 1);
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		// 仮に円
		if (UNIT.millitary == Unit.PLAYER)
			g.setColor(new Color(0, 0, 240));
		else if (UNIT.millitary == Unit.ENEMY)
			g.setColor(new Color(240, 0, 0));
		else if (UNIT.millitary == Unit.ALLIANCE)
			g.setColor(new Color(0, 240, 0));
		g.fillOval(Game.CHIP_SIZE * pos.x + offset.x, Game.CHIP_SIZE * pos.y + offset.y, Game.CHIP_SIZE,
				Game.CHIP_SIZE);

	}
}
