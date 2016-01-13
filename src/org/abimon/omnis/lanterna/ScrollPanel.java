package org.abimon.omnis.lanterna;

import java.util.ArrayList;
import java.util.List;

import org.abimon.omnis.reflect.ReflectionHelper;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class ScrollPanel extends Panel implements IScrolling{

	int componentMax;
	int componentPosition = 0;

	public ScrollPanel(int componentMax){
		this.componentMax = componentMax;
	}

	public void addComponent(Component component) {
		super.addComponent(component);
	}

	@Override
	protected ComponentRenderer<Panel> createDefaultRenderer() {
		return new ComponentRenderer<Panel>() {

			@Override
			public TerminalSize getPreferredSize(Panel component) {
				List<Component> components = new ArrayList<Component>();
				components.addAll(getChildren());
				List<Component> childComponents = new ArrayList<Component>(componentMax);

				if((componentPosition + componentMax) > components.size()){
					int start = componentMax > components.size() ? 0 : components.size() - componentMax;
					for(int i = start; i < components.size(); i++){
						Component child = components.get(i);
						child.setPosition(new TerminalPosition(component.getPosition().getColumn(), component.getPosition().getRow() + (i - componentPosition)));
						child.setSize(new TerminalSize(child.getPreferredSize().getColumns()+10,child.getPreferredSize().getRows()));
						childComponents.add(child);
					}
				}
				else if(componentPosition > componentMax){
					for(int i = componentPosition; i < Math.min(componentMax + componentPosition, components.size()); i++){
						Component child = components.get(i);
						childComponents.add(child);
					}
				}
				else
					for(int i = 0; i < Math.min(componentMax, components.size()); i++){
						Component child = components.get(i);
						childComponents.add(child);
					}
				ReflectionHelper.setObjectFromVariable(component, "needsReLayout", true);
				return getLayoutManager().getPreferredSize(childComponents);
			}

			@Override
			public void drawComponent(TextGUIGraphics graphics, Panel component) {
				layout(graphics.getSize());
				List<Component> components = new ArrayList<Component>();
				components.addAll(getChildren());
				if((componentPosition + componentMax) < components.size())
					for(int i = componentPosition; i < Math.min(componentMax + componentPosition, components.size()); i++){
						Component child = components.get(i);
						child.setPosition(new TerminalPosition(component.getPosition().getColumn(), component.getPosition().getRow() + (i - componentPosition)));
						child.setSize(new TerminalSize(child.getPreferredSize().getColumns(),child.getPreferredSize().getRows()));
						TextGUIGraphics componentGraphics = graphics.newTextGraphics(child.getPosition(), child.getSize());
						child.draw(componentGraphics);
					}
				else{
					int start = componentMax > components.size() ? 0 : components.size() - componentMax;
					for(int i = start; i < components.size(); i++){
						Component child = components.get(i);
						child.setPosition(new TerminalPosition(component.getPosition().getColumn(), component.getPosition().getRow() + (i - start)));
						child.setSize(new TerminalSize(child.getPreferredSize().getColumns(),child.getPreferredSize().getRows()));
						TextGUIGraphics componentGraphics = graphics.newTextGraphics(child.getPosition(), child.getSize());
						child.draw(componentGraphics);
					}
				}
			}
		};
	}

	public void layout(TerminalSize size) {
		List<Component> components = new ArrayList<Component>();
		components.addAll(getChildren());
		getLayoutManager().doLayout(size, components);
		ReflectionHelper.setObjectFromVariable(this, "needsReLayout", false);
	}

	public List<Component> components(){
		List<Component> components = new ArrayList<Component>();
		components.addAll(getChildren());
		List<Component> children = new ArrayList<Component>(componentMax);
		for(int i = componentPosition; i < Math.min(componentMax + componentPosition, components.size()); i++){
			children.add(components.get(i));
		}
		return components;
	}

	public List<Component> allComponents(){
		List<Component> components = new ArrayList<Component>();
		components.addAll(getChildren());
		return components;
	}

	@Override
	public int getScrollPos() {
		return componentPosition;
	}

	@Override
	public int getScrollMax() {
		return components().size();
	}

	@Override
	public void setScrollPos(int pos) {
		componentPosition = pos;
	}
}
