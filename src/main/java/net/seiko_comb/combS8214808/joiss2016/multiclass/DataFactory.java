package net.seiko_comb.combS8214808.joiss2016.multiclass;

import java.util.ArrayList;
import java.util.List;

public class DataFactory {
	private int classCount;
	private String[] caption;
	private List<Data> datas = new ArrayList<>();

	public DataFactory(int classCount) {
		this.classCount = classCount;
	}

	public int getClassCount() {
		return classCount;
	}

	public void setCaption(String... caption) {
		if (caption.length != classCount)
			throw new RuntimeException("Caption string length doesn't match.");
		this.caption = caption;
	}

	public String getCaption(int index) {
		return caption[index];
	}

	public int getIndexFromCaption(String str) {
		for (int i = 0; i < classCount; i++)
			if (caption[i].equals(str))
				return i;
		throw new RuntimeException("No caption found matches to : " + str);
	}

	public void addData(Data data) {
		if (data.getDataSet() != this)
			throw new RuntimeException("Added data of different dataset.");
		datas.add(data);
	}
	
	public List<Data> getDatas() {
		return datas;
	}
}
