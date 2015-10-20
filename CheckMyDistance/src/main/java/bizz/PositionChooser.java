package bizz;

import bizz.Position;

/**
 * 
 * This class is used to manage the positions in a list with checkboxes
 * 
 * @author jvdur_000
 *
 */
public class PositionChooser {

	private String name;
	private boolean selected;
	private Position position;

	/**
	 * Constructor
	 * @param position the position to save
	 */
	public PositionChooser(Position position) {
		this.position = position;
		selected = false;
	}

	/*
	 * GETTERS & SETTERS
	 */
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Position getPosition() {
		return position;
	}

	public String getName() {
		return name;
	}
	
}
