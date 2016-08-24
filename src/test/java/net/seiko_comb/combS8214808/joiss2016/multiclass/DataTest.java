package net.seiko_comb.combS8214808.joiss2016.multiclass;

import static net.seiko_comb.combS8214808.joiss2016.Vector.$;

import org.junit.Test;

@SuppressWarnings("unused")
public class DataTest {

	@Test
	public void test() {
		DataFactory dataSet = new DataFactory(3, 3);
		Data data = new Data(dataSet, $(4, 2, 3), 0);
		data = new Data(dataSet, $(1, 3, 2), 2);
	}

	@Test(expected=InvalidClassIndexException.class)
	public void testException(){
		DataFactory dataSet = new DataFactory(3, 3);
		Data data = new Data(dataSet, $(4, 2, 3), 4);
	}
	
	@Test(expected=RuntimeException.class)
	public void invalidInputDimension(){
		DataFactory dataSet = new DataFactory(3, 3);
		Data data = new Data(dataSet, $(4, 2, 3, 1), 4);
	}
}
