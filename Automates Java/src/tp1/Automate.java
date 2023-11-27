// Les fonctions sont expliquées plus en détail dans le fichier Word.
package tp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Automate {
	private State initialState;
	private List<State> states = new ArrayList<State>();
	private static List<State> finalStates = new ArrayList<State>();
	private static List<Transition> transitions = new ArrayList<>();
	static List<State> loadedStates = new ArrayList<>();

	public Automate(State initialState) { // constructeur de l'automate que par son état initial
		this.initialState = initialState;
		states = new ArrayList<>();
	}

	public static boolean validateAutomate(Automate automate, String input) {
	    if (automate == null || input == null) {
	        return false;
	    }

	    State currentState = automate.getInitialState();

	    for (char c : input.toCharArray()) {
	        if (c != '0' && c != '1') {
	            return false; // retourne false si != de 0 ou 1.
	        }

	        char inputValue = (char) Character.getNumericValue(c);
	        Transition transition = automate.findTransition(currentState, inputValue);

	        if (transition == null) {
	            return false; // Si la transition n'est pas valide il retourne false.
	        }

	        currentState = transition.getToState();
	    }

	    return automate.isFinalState(currentState);
	}

	public static Automate loadAutomateFromFile(String filePath) {

		List<Transition> loadedTransitions = new ArrayList<>();
		State loadedInitialState = null;

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");

				if (parts.length < 2) {
					continue;
				}

				String action = parts[0];

				if (action.equals("state") && parts.length >= 3) {
				    String stateName = parts[1];
				    int stateValue;
				    try {
				        stateValue = Integer.parseInt(parts[2]);
				        State newState = new State(stateName, stateValue == 1);
				        loadedStates.add(newState);

				        if (stateValue == 1) {
				            finalStates.add(newState); // Ajouter à la liste des states finales
				        }

				        if (stateValue == 1 && loadedInitialState == null) {
				            loadedInitialState = newState;
				        }
				    } catch (NumberFormatException e) {
				    }
				} else if (action.equals("transition") && parts.length >= 4) {
					String fromStateName = parts[1];
					int input;
					try {
						input = Integer.parseInt(parts[2]);
						String toStateName = parts[3];
						State fromState = findStateByName(loadedStates, fromStateName);
						State toState = findStateByName(loadedStates, toStateName);

						if (fromState != null && toState != null) {
							Transition newTransition = new Transition(input, fromState, toState);
							loadedTransitions.add(newTransition);
						}
					} catch (NumberFormatException e) {
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Erreur en ouvrant le fichier: " + e.getMessage());
		}

		if (loadedInitialState != null) {
			Automate automate = new Automate(loadedInitialState);
			automate.setStates(loadedStates);
			automate.setTransitions(loadedTransitions);
			return automate;
		}

		return null;
	}

	public static Automate loadAutomateFromUserInput(int numStates, int numTransitions) { // Fonction non-utilisée dans le programme mais qui crée un automate par l'utlisateur et non un fichier txt.
		List<Transition> loadedTransitions = new ArrayList<>();
		List<State> loadedStates = new ArrayList<>();
		State loadedInitialState = null;

		Scanner scanner = new Scanner(System.in);

		for (int i = 0; i < numStates; i++) {
			System.out.println("Entrez les détails des states (Nom Valeur - 0 or 1):");
			String stateInput = scanner.nextLine();
			String[] stateParts = stateInput.split(" ");

			if (stateParts.length != 2) {
				System.err.println("Erreur!. Format invalid.");
				i--;
				continue;
			}

			String stateName = stateParts[0];
			int stateValue;

			try {
				stateValue = Integer.parseInt(stateParts[1]);
				if (stateValue != 0 && stateValue != 1) {
					System.err.println("Erreur! L'état doit être 0 ou 1.");
					i--;
					continue;
				}

				State newState = new State(stateName, stateValue == 1);
				loadedStates.add(newState);

				if (stateValue == 1 && loadedInitialState == null) {
					loadedInitialState = newState;
				}
			} catch (NumberFormatException e) {
				System.err.println("Erreur! Pour la valeur: " + e.getMessage());
			}
		}

		for (int i = 0; i < numTransitions; i++) {
			System.out.println("Entrez les détails des transitions  (from input to - 0 or 1):");
			String transitionInput = scanner.nextLine();
			String[] transitionParts = transitionInput.split(" ");

			if (transitionParts.length != 3) {
				System.err.println("Erreur! Format invalid de transition."); // doit suivre le format présenté
				i--;
				continue;
			}

			String fromStateName = transitionParts[0];
			int input;

			try {
				input = Integer.parseInt(transitionParts[1]);
				if (input != 0 && input != 1) {
					System.err.println("Erreur! Doit être 0 ou 1");
					i--;
					continue;
				}

				String toStateName = transitionParts[2];
				State fromState = findStateByName(loadedStates, fromStateName);
				State toState = findStateByName(loadedStates, toStateName);

				if (fromState != null && toState != null) {
					Transition newTransition = new Transition(input, fromState, toState);
					loadedTransitions.add(newTransition);
				}
			} catch (NumberFormatException e) {
				System.err.println("Erreur de parsing!: " + e.getMessage());
			}
		}

		scanner.close();

		System.out.println("States:");
		for (State state : loadedStates) {
			System.out.println(state.getName() + " " + (state.getValue() ? 1 : 0));
		}

		System.out.println("Transitions:");
		for (Transition transition : loadedTransitions) {
			System.out.println(transition.getFromState().getName() + " " + transition.getInput() + " "
					+ transition.getToState().getName());
		}

		if (loadedInitialState != null) {
			Automate automate = new Automate(loadedInitialState);
			automate.setStates(loadedStates);
			automate.setTransitions(loadedTransitions);
			return automate;
		}

		return null;
	}

	private static State findStateByName(List<State> states, String name) {
		for (State state : states) {
			if (state.getName().equals(name)) {
				return state;
			}
		}
		return null;
	}

	private void setStates(List<State> states2) {
		states = states2; 
	}

	private void setTransitions(List<Transition> transitions2) {
		transitions = transitions2;
	}

	public void stateFinal() { // permet de retourner les états finals de l’automate.
		for (State state : loadedStates) {
			if (state.isFinal() == 1) {
				System.out.println("État final de l'automate: " + state.getName());
			}
		}
	}

	public String toString() {	// affichage de l'automate notamment au numero2
		StringBuilder automateString = new StringBuilder();
		automateString.append("\n---Voici votre automate---");
		automateString.append("\nStates:\n");
		for (State state : states) {
			automateString.append(state.getName()).append(" (").append(state.isFinal()).append(")\n");
		}

		automateString.append("\nTransitions:\n");
		for (Transition transition : transitions) {
			automateString.append(transition.getFromState().getName()).append(" -> ").append(transition.getInput())
					.append(" -> ").append(transition.getToState().getName()).append("\n");
		}

		return automateString.toString();
	}
	
	public boolean isFinalState(State state) {
	    return finalStates.contains(state);
	}
	private Transition findTransition(State fromState, char input) { // permet de vérifier si la transition est valide, sinon retourne null
		for (Transition transition : transitions) {
			if (transition.getInput() == input) {
				return transition;
			}
		}
		return null;
	}

	public State getInitialState() {
		return initialState;
	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

}