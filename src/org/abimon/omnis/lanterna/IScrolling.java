package org.abimon.omnis.lanterna;

import com.googlecode.lanterna.gui2.Container;

public interface IScrolling extends Container{
	public int getScrollPos();
	public void setScrollPos(int pos);
	public int getScrollMax();
}
