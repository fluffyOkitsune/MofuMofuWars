package mmw.database;

/** 月齢 */
public enum Moon {
	新月("新月", "真っ暗で見えない。命中が下がる。"), //
	三日月("三日月", "不思議な夜。魔力が上がる。"), //
	上弦の月("上弦の月", ""), //
	満月("満月", "まんまるおつきさま。命中が上がる。"), //
	下弦の月("下弦の月", ""), //
	;
	final String name;
	final String explain;

	Moon(String name, String explain) {
		this.name = name;
		this.explain = explain;
	}
}
