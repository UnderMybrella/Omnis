package org.abimon.omnis.swing;

import java.awt.*;

/**
 * Convenience class
 * @author {@link www.stackoverflow.com/users/1438660/splungebob}
 *
 */
public class GridBagConstraintsHelper extends GridBagConstraints {

	private static final long serialVersionUID = 1L;

	public GridBagConstraintsHelper(int gridX, int gridY){
		super();

		this.gridx = gridX;
		this.gridy = gridY;

		this.gridwidth = 1;
		this.gridheight = 1;
		this.weightx = 0;
		this.weighty = 0;
		this.anchor = NORTHWEST;
		this.fill = NONE;
		this.insets = new Insets(1,2,1,2);
		this.ipadx = 1;
		this.ipady = 1;
	}

	public GridBagConstraintsHelper anchor(GridBagAnchor anchor){
		this.anchor = anchor.getConstraint();
		return this;
	}

	public GridBagConstraintsHelper fill(GridBagFill fill){
		this.fill = fill.getConstraint();

		switch(fill){
		case HORIZONTAL:
			this.weightx = 1;
			this.weighty = 0;
			break;
		case VERTICAL:
			this.weightx = 0;
			this.weighty = 1;
			break;
		case BOTH:
			this.weightx = 1;
			this.weighty = 1;
			break;
		case NONE:
			this.weightx = 0;
			this.weighty = 0;
			break;
		}
		return this;
	}
	
	public GridBagConstraintsHelper insets(int length){
		return insets(length, length, length, length);
	}
	
	public GridBagConstraintsHelper insets(int top, int left, int bottom, int right){
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}
	
	public GridBagConstraintsHelper ipad(int ipadX, int ipadY){
		this.ipadx = ipadX;
		this.ipady = ipadY;
		return this;
	}
	
	public GridBagConstraintsHelper span(int gridWidth, int gridHeight){
		this.gridwidth = gridWidth;
		this.gridheight = gridHeight;
		return this;
	}
	
	public GridBagConstraintsHelper spanX(int gridWidth){
		return span(gridWidth, this.gridheight);
	}
	
	public GridBagConstraintsHelper spanY(int gridHeight){
		return span(this.gridwidth, gridHeight);
	}
	
	public GridBagConstraintsHelper weight(double weightX, double weightY){
		this.weightx = weightX;
		this.weighty = weightY;
		return this;
	}
	
	public GridBagConstraintsHelper weightX(double weightX){
		return weight(weightX, this.weighty);
	}
	
	public GridBagConstraintsHelper weightY(double weightY){
		return weight(this.weightx, weightY);
	}
	

}
