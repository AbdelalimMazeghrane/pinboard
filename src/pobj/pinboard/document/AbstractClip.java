package pobj.pinboard.document;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class AbstractClip {
	private double left;
	private double top;
	private double right;
	private double bottom;
	private Color color;
	
	public AbstractClip(double left, double top, double right, double bottom, Color color) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.color = color;
	}
	

	public double getTop() {
		return top;
	}

	public double getLeft() {
		return left;
	}

	public double getBottom() {
		return bottom;
	}

	public double getRight() {
		return right;
	}

	public void setGeometry(double left, double top, double right, double bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;

	}

	public void move(double x, double y) {
		right += x;
		left += x;
		top += y;
		bottom += y;

	}

	public boolean isSelected(double x, double y) {
		if (x < left || x > right) return false;
		if (y < top || y > bottom) return false;
		return true;
	}

	public void setColor(Color c) {
		color = c;

	}

	public Color getColor() {
		return color;
	}
}
