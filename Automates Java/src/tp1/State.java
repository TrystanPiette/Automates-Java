package tp1;

import java.util.ArrayList;
import java.util.List;

public class State {
	private boolean isFinal;
	private String name;
	private List<Transition> transitions;

	public State(String name, boolean isFinal) { // constructeur de state
		this.name = name;
		this.isFinal = isFinal;
		this.transitions = new ArrayList<>();
	}

	public int isFinal() {
		if (isFinal == true) {
			return 1;
		} else
			return 0;
	}

	public String getName() {
		return name;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void addTransition(Transition transition) {
		transitions.add(transition);
	}

	public boolean getValue() {
		return isFinal;
	}
}