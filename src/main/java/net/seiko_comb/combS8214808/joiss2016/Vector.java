package net.seiko_comb.combS8214808.joiss2016;

import java.util.Arrays;

public class Vector {
	public double value[];
	public int length;

	private Vector(int length) {
		this.value = new double[length];
		this.length = length;
	}

	public static Vector zero(int length) {
		return new Vector(length);
	}

	public static Vector $(double... component) {
		Vector ret = new Vector(component.length);
		System.arraycopy(component, 0, ret.value, 0, component.length);
		return ret;
	}

	public Vector plus(Vector vector) {
		checkVectorLength(this, vector);
		Vector ret = new Vector(length);
		for (int i = 0; i < length; i++)
			ret.value[i] = this.value[i] + vector.value[i];
		return ret;
	}

	public Vector addOne() {
		Vector ret = new Vector(length + 1);
		System.arraycopy(value, 0, ret.value, 0, length);
		ret.value[length] = 1;
		return ret;
	}

	public Vector product(double d) {
		Vector ret = new Vector(length);
		for (int i = 0; i < length; i++)
			ret.value[i] = d * value[i];
		return ret;
	}

	public double product(Vector vector) {
		checkVectorLength(this, vector);
		double ret = 0;
		for (int i = 0; i < length; i++)
			ret += this.value[i] * vector.value[i];
		return ret;
	}

	private void checkVectorLength(Vector v1, Vector v2) {
		if (v1.length != v2.length)
			throw new NotSameVectorLengthException();
	}

	@Override
	public String toString() {
		return Arrays.toString(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + length;
		result = prime * result + Arrays.hashCode(value);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		if (length != other.length)
			return false;
		if (!Arrays.equals(value, other.value))
			return false;
		return true;
	}
}
