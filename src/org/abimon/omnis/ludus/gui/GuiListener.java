package org.abimon.omnis.ludus.gui;

public interface GuiListener {
	public void onGuiOpened(Gui gui);
	public void onGuiReplaced(Gui gui, Gui replacing);
	public void onGuiClosed(Gui gui);
}
