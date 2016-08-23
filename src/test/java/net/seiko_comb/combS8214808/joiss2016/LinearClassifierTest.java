package net.seiko_comb.combS8214808.joiss2016;

import static net.seiko_comb.combS8214808.joiss2016.Vector.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class LinearClassifierTest {

	public static final double EPS = VectorTest.EPS;

	@Test
	public void test() {
		LinearClassifier classifier;
		classifier = new LinearClassifier($(1, 4));
		assertThat(classifier.f($(0)), closeTo(4, EPS));
		classifier = new LinearClassifier($(200, 200, 200, 100, 100, -400));
		assertThat(classifier.f($(0.65, 0.55, 0.35, 0.6, 0.8)), closeTo(50, EPS));
	}

}
