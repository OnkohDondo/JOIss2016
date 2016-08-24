package net.seiko_comb.combS8214808.joiss2016.multiclass;

import net.seiko_comb.combS8214808.joiss2016.Vector;

public class Data {
	private DataFactory dataSet;

	private Vector w;
	private int classIndex;

	public Data(DataFactory dataSet, Vector w, int classIndex) {
		if (classIndex < 0 || dataSet.getClassCount() <= classIndex)
			throw new InvalidClassIndexException();
		this.dataSet = dataSet;
		this.w = w;
		this.classIndex = classIndex;
	}

	@Override
	public String toString() {
		return String.format("%s->%s", w, dataSet.getCaption(classIndex));
	}
	
	public DataFactory getDataSet() {
		return dataSet;
	}
}

