package net.seiko_comb.combS8214808.joiss2016.multiclass;

import net.seiko_comb.combS8214808.joiss2016.Vector;

public class Data {
	private DataFactory dataSet;

	private Vector x;
	private int classIndex;

	public Data(DataFactory dataSet, Vector x, int classIndex) {
		if (classIndex < 0 || dataSet.getClassCount() <= classIndex)
			throw new InvalidClassIndexException();
		if (x.length != dataSet.getInputDimension())
			throw new RuntimeException("Invalid input vector.");
		this.dataSet = dataSet;
		this.x = x;
		this.classIndex = classIndex;
	}

	@Override
	public String toString() {
		return String.format("%s->%s", x, dataSet.getCaption(classIndex));
	}

	public DataFactory getDataSet() {
		return dataSet;
	}

	public Vector getX() {
		return x;
	}

	public int getClassIndex() {
		return classIndex;
	}
}
