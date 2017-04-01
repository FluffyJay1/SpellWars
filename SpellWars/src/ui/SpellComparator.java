package ui;

import java.util.Comparator;

import spell.Spell;

public class SpellComparator implements Comparator<Spell> {
	@Override
	public int compare(Spell x, Spell y) {
		if(x.sortWeight < y.sortWeight) {
			return -1;
		}
		if(x.sortWeight > y.sortWeight) {
			return 1;
		}
		return x.getName().compareTo(y.getName());
	}
}
