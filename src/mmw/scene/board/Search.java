package mmw.scene.board;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

import mmw.database.MapChip;
import mmw.database.Skill;
import mmw.database.StageData;
import mmw.obj.UnitOnBoard;

// A*探索でユニットの最短移動距離を導出する
public class Search {
	public static Point[] SearchRoute(SceneBoard sceneBoard, StageData stageData, UnitOnBoard unit, final Point s,
			final Point g) {
		Node goal = new Node(g);

		Node start = new Node(s);
		start.f = Search.heuristicFunc(start, g);
		start.h = start.f;
		start.cost = unit.UNIT.move;

		PriorityQueue<Node> open = new PriorityQueue(1, start);
		ArrayList<Node> close = new ArrayList();

		open.add(start);

		while (!open.isEmpty()) {
			// 最小のfを持つノードを取得
			Node n = open.poll();

			// ゴールか確認
			if (n.equals(goal)) {
				return n.getRoute();

			} else {
				// n -> m
				close.add(n);
				ArrayList<Integer> randomDir = new ArrayList();
				randomDir.add(0);
				randomDir.add(1);
				randomDir.add(2);
				randomDir.add(3);
				Collections.shuffle(randomDir);
				for (int i = 0; i < 4; i++) {
					int[] dirX = { 0, 0, -1, 1 };
					int[] dirY = { 1, -1, 0, 0 };

					// 隣のマス
					Node m = new Node(new Point(n.POS.x + dirX[randomDir.get(i)], n.POS.y + dirY[randomDir.get(i)]));

					if (0 <= m.POS.x && m.POS.x < stageData.mapSizeWidth && 0 <= m.POS.y
							&& m.POS.y < stageData.mapSizeHeight) {
						int cost = MapChip.getMapChip(stageData.map[m.POS.x][m.POS.y]).terrain.costH;
						if (n.cost >= cost)
							if (unit.UNIT.skill[0] == Skill.すり抜け || unit.UNIT.skill[1] == Skill.すり抜け
									|| unit.UNIT.skill[2] == Skill.すり抜け || unit.UNIT.skill[3] == Skill.すり抜け
									|| unit.UNIT.skill[4] == Skill.すり抜け
									|| sceneBoard.getEnemyUnitAt(m.POS.x, m.POS.y) == null) {
								// 移動コスト
								m.cost = n.cost - cost;

								// f' = g(n) + Cost(n,m) + h(m)
								int fPrime = (n.f - n.h) + cost + Search.heuristicFunc(m, g);

								if (!open.contains(m) && !close.contains(m)) {
									m.f = fPrime;
									m.parent = n;
									open.add(m);

								} else if (open.contains(m) && fPrime < m.f) {
									open.remove(m);
									m.f = fPrime;
									m.parent = n;
									open.add(m);

								} else if (close.contains(m) && fPrime < m.f) {
									close.remove(m);
									m.f = fPrime;
									m.parent = n;
									open.add(m);
								}
							}
					}
				}
			}
		}
		return null;
	}

	// ヒューリスティック関数
	private static int heuristicFunc(Node node, Point goal) {
		return Math.abs(goal.x - node.POS.x) + Math.abs(goal.y - node.POS.y);
	}
}

class Node implements Comparator {
	final Point POS;
	Node parent;
	int f;
	int h;
	int cost;

	Node(Point p) {
		this.POS = p;
	}

	Point[] getRoute() {
		ArrayList<Point> list = new ArrayList();
		Node node = this;
		list.add(node.POS);

		while ((node = node.parent) != null)
			list.add(node.POS);

		Collections.reverse(list);

		return list.toArray(new Point[1]);
	}

	@Override
	public boolean equals(Object obj) {
		return this.POS.equals(((Node) obj).POS);
	}

	@Override
	public int compare(Object o1, Object o2) {
		return Integer.compare(((Node) o1).f, ((Node) o2).f);
	}

}