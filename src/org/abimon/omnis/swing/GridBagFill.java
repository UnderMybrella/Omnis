package org.abimon.omnis.swing;

import java.awt.*;

/**
 * Convenience enum that al.iases out all possible values for the
 * GridBagConstraints fill property
 * @author {@link www.stackoverflow.com/users/1438660/splungebob}
 *
 */

public enum GridBagFill {
	NONE (GridBagConstraints.NONE),
	HORIZONTAL(GridBagConstraints.HORIZONTAL),
	VERTICAL(GridBagConstraints.VERTICAL),
	BOTH(GridBagConstraints.BOTH);
	
	private int constraint;
	
	private GridBagFill(int gbConstraint)
	{
		constraint = gbConstraint;
	}
	
	public int getConstraint()
	{
		return constraint;
	}
}
