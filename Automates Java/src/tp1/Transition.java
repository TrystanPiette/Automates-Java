package tp1;

public class Transition {
	private int input;
	private State toState;
	private State fromState; 

	public Transition(int input, State fromState, State toState) { // constructeur de transition
		this.input = input;
		this.fromState = fromState;
		this.toState = toState;
	}

	public int getInput() {
		return input;
	}

	public State getFromState() {
		return fromState;
	}

	public State getToState() {
		return toState;
	}
}