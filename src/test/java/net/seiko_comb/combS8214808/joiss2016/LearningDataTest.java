package net.seiko_comb.combS8214808.joiss2016;

import static net.seiko_comb.combS8214808.joiss2016.Vector.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class LearningDataTest {

	@Test
	public void test() {
		LinearClassifier classifier;
		classifier = new LinearClassifier($(200, 200, 200, 100, 100, -400));
		assertThat(new LearningData(classifier, $(0.65, 0.55, 0.35, 0.6, 0.8)).y, closeTo(1, 0));
		assertThat(new LearningData(classifier, $(0.65, 0.55, 0.35, 0.6, 0.3)).y, closeTo(-1, 0));
	}
}
