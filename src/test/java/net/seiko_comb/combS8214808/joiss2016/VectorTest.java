package net.seiko_comb.combS8214808.joiss2016;

import static net.seiko_comb.combS8214808.joiss2016.Vector.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class VectorTest {

	public static final double EPS = 1E-8;

	@Test
	public void testZero() {
		Vector vector;
		vector = zero(3);
		assertThat(vector, is($(0, 0, 0)));
		vector = zero(5);
		assertThat(vector, is($(0, 0, 0, 0, 0)));
	}

	@Test
	public void testOf() {
		Vector vector;
		vector = $();
		assertThat(vector.length, is(0));
		assertThat(vector.value.length, is(0));
		vector = $(7, 11, 13);
		assertThat(vector.length, is(3));
		assertThat(vector.value[0], closeTo(7, 0));
		assertThat(vector.value[1], closeTo(11, 0));
		assertThat(vector.value[2], closeTo(13, 0));
	}

	@Test
	public void testPlus() {
		assertThat($(3, 2).plus($(1, 5)), is($(4, 7)));
		assertThat($(1, 1, 2, 3, 5, 8, 13, 21, 34).plus($(1, 2, 3, 5, 8, 13, 21, 34, 55)),
				is($(2, 3, 5, 8, 13, 21, 34, 55, 89)));
	}

	@Test(expected = NotSameVectorLengthException.class)
	public void testFailingPlus() {
		$(1, 2).plus($(5, 3, 1));
	}

	@Test
	public void testProduct() {
		assertThat($(2, 3).product($(1, 5)), closeTo(17, EPS));
		assertThat($(1, 2, 3, 4, 5).product($(6, 7, 8, 9, 10)), closeTo(1 * 6 + 2 * 7 + 3 * 8 + 4 * 9 + 5 * 10, EPS));
	}

	@Test(expected = NotSameVectorLengthException.class)
	public void testFailingProduct() {
		$(2, 3).product($(4, 5, 6));
	}

	@Test
	public void testAddZero() {
		assertThat($().addOne(), is($(1)));
		assertThat($(3, 4).addOne(), is($(3, 4, 1)));
	}

}
