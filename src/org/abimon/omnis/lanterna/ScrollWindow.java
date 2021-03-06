package org.abimon.omnis.lanterna;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Container;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class ScrollWindow extends BasicWindow {
	@Override
	/**
	 * Note: Component must be of type IScrolling
	 */
	public void setComponent(Component component) {
		if(component instanceof IScrolling)
			super.setComponent(component);
		else
			return;
	}

	@Override
	public boolean handleInput(KeyStroke key) {
		Interactable next = null;
		if(key.getKeyType() == KeyType.ArrowDown && getPos() < getMax() - 1)
		{
			((IScrolling) getComponent()).setScrollPos(getPos() + 1);
			if(getChildren().get(getPos()) instanceof Interactable)
				next = (Interactable) getChildren().get(getPos());
		}
		else if(key.getKeyType() == KeyType.ArrowUp && getPos() > 0)
		{
			((IScrolling) getComponent()).setScrollPos(getPos() - 1);
			if(getChildren().get(getPos()) instanceof Interactable)
				next = (Interactable) getChildren().get(getPos());
		}
		else if(key.getKeyType() == KeyType.ArrowLeft){
			int pos = getPos();
			
			while(!(getChildren().get(pos--) instanceof Interactable) && pos > 0);
			
			if(pos < 0)
				pos = 0;
			
			if(getChildren().get(pos) instanceof Interactable)
				next = (Interactable) getChildren().get(pos);
		}
		else if(key.getKeyType() == KeyType.ArrowRight){
			int pos = getPos();
			
			while(!(getChildren().get(pos++) instanceof Interactable) && pos < getMax() - 1);
			
			if(pos >= getMax())
				pos = getMax() - 1;
			
			if(getChildren().get(pos) instanceof Interactable)
				next = (Interactable) getChildren().get(pos);
		}
		else
			super.handleInput(key);
		if(next != null) {
			setFocusedInteractable(next);
			return true;
		}
		return false;
	}

	public List<Component> getChildren(){
		List<Component> children = new ArrayList<Component>();
		children.addAll(((Container) getComponent()).getChildren());
		return children;
	}

	public int getPos(){
		return ((IScrolling) getComponent()).getScrollPos();
	}

	public int getMax(){
		return ((IScrolling) getComponent()).getScrollMax();
	}
}
