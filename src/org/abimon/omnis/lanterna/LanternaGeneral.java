package org.abimon.omnis.lanterna;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.LayoutManager;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;

public class LanternaGeneral {
	public static Window createWindow(Component... components){
		return createWindow(new BasicWindow(), new LinearLayout(), new Panel(), components);
	}
	
	public static Window createWindow(Window window, LayoutManager layout, Panel panel, Component... components){
		panel.setLayoutManager(layout);
		for(Component comp : components)
			panel.addComponent(comp);
		window.setComponent(panel);
		return window;
	}
}
