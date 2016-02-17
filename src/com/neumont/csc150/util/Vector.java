package com.neumont.csc150.util;

/**
 * An object with direction and magnitude
 * @author JoJones
 */
public class Vector {
	private double dX;
	private double dY;

	public Vector() {
		setdX(0.0);
		setdY(0.0);
	}

	public Vector(double dX, double dY) {
		setdX(dX);
		setdY(dY);
	}

	public double length() {
		return Math.sqrt(getdX()*getdX() + getdY()*getdY());
	}

	/**
	 * Normalize the vector
	 * @return Normalized Vector
	 */
	public Vector normalize() {
		Vector v2 = new Vector();

		double length = length();
		if (length != 0) {
			v2.setdX(this.getdX()/length);
			v2.setdY(this.getdY()/length);
		}

		return v2;
	}   

	public String toString() {
		return "Vector(" + getdX() + ", " + getdY() + ")";
	}

	public double getdX() {
		return dX;
	}

	public void setdX(double dX) {
		this.dX = dX;
	}

	public double getdY() {
		return dY;
	}

	public void setdY(double dY) {
		this.dY = dY;
	}
}