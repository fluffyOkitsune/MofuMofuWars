package mmw.database;

/** ユニットの種族 */
public enum Species {
	おきつね("おきつね", "いたずら好きで自由奔放な種族。"), //
	からす("からす", "賢く抜け目のない種族。空を飛ぶことができる。"), //
	おおかみ("おおかみ", "物怖じしない堅実な種族"), //
	おおわし("おおわし", "落ち着いた佇まいの精悍な種族。空を飛ぶことができる"), //
	ひと("ひと", "手先が器用だが非力"), //
	;
	final String name;
	final String explain;

	Species(String name, String explain) {
		this.name = name;
		this.explain = explain;
	}
}
