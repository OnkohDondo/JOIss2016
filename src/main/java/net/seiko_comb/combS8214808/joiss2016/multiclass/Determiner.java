package net.seiko_comb.combS8214808.joiss2016.multiclass;

public interface Determiner {
	public void learn(Data data);

	public boolean check(Data data);
}
