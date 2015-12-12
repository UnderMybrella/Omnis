package org.abimon.omnis.util;

public enum EnumANSI {
	RESET(0),
	BOLD(1),
	FAINT(2),
	ITALIC(3),
	UNDERLINE(4),
	BLINK(5),
	BLINK_FAST(6),
	NEGATIVE(7),
	CONCEAL(8),
	CROSSED_OUT(9),
	PRIMARY_FONT(10),
	/** ALTERNATE FONTS */
	FRAKTUR(20),
	BOLD_OFF(21),
	NORMAL_COLOUR(22),
	/**
	 * Will turn Fraktur off too
	 */
	ITALIC_OFF(23),
	/**
	 * Will turn Italics off too
	 */
	FRAKTUR_OFF(23),
	UNDERLINE_OFF(24),
	BLINK_OFF(25),
	RESERVED(26),
	POSITIVE(27),
	REVEAL(28),
	NOT_CROSSED_OUT(29),
	BLACK_TEXT(30),
	RED_TEXT(31),
	GREEN_TEXT(32),
	ORANGE_TEXT(33),
	BLUE_TEXT(34),
	PINK_TEXT(35),
	CYAN_TEXT(36),
	WHITE_TEXT(37),
	BLACK_BACK(40),
	RED_BACK(41),
	GREEN_BACK(42),
	ORANGE_BACK(43),
	BLUE_BACK(44),
	PINK_BACK(45),
	CYAN_BACK(46),
	WHITE_BACK(47),
	
	CURSOR_UP("A"),
	CURSOR_DOWN("B"),
	CURSOR_FORWARD("C"),
	CURSOR_BACKWARD("D"),
	CURSOR_NEXTLINE("E"),
	CURSOR_PREVLINE("F"),
	CURSOR_HORIZ_ABSOLUTE("G"),
	CURSOR_POS("H"),
	CURSOR_ERASESCREEN("J");
	;
	
	
	
	
	String controlCode = "";
	
	EnumANSI(String controlCode){
		this.controlCode = controlCode;
	}
	
	EnumANSI(int controlCode){
		this.controlCode = Integer.toString(controlCode);
	}
	
	public String toString(){
		return "\u001B[" + controlCode + "m";
	}
	
	public String getCursor(String cells){
		return toString().replace(controlCode + "m", cells + controlCode);
	}
	
	public String getCursor(int cells){
		return getCursor(Integer.toString(cells));
	}
}
