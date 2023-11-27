package tp1;

import java.util.Scanner;

public class Program {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Automate automate = null;

		while (true) { // menu du programme d'automates
			System.out.println("Options:");
			System.out.println("1. Charger un automate d'un fichier text");
			System.out.println("2. Afficher la liste d'états et la liste des transitions de l'automate");
			System.out.println("3. Soumettre une chaîne binaire pour validation");
			System.out.println("4. Quitter");
			System.out.print("Entrez votre choix: ");

			if (scanner.hasNextInt()) {
				int choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {
				case 1:
					System.out.print("Entrez le nom du fichier pour charger la configuration de l'automate (automates.txt): "); // Input de l'automate par l'utilisateur en utilisant un fichier txt.
					String fileName = scanner.nextLine();

					automate = Automate.loadAutomateFromFile(fileName); // variable de l'automate

					if (automate == null) {
						System.err.println("Erreur ! L'automate n'est pas fonctionnel.\n"); // Erreur si le document texte n'est pas présent ou mal écrit
					} else {
						System.out.println("L'automate a été généré avec succès!\n");
					}

					break;
				case 2:
					if (automate != null) {
						System.out.println(automate);
					} else {
						System.err.println("Erreur ! Vous n'avez pas généré d'automate.\n"); // Erreur si l'automate n'a pas été généré par l'option 1
					}
					break;
				case 3:
				    if (automate != null) {
				        String input;	       
				            System.out.print("Entrez une série de 0s et 1s pour validation : "); // Chaîne que vous-voulez valider
				            input = scanner.nextLine();
				            System.out.println("Votre input est : " + Automate.validateAutomate(automate, input) + "\n"); // Retourne true or false, selon l'automate et l'input de la chaîne			       
				    } else {
				        System.err.println("Erreur ! Vous n'avez pas généré d'automate.\n"); // Erreur si l'automate n'a pas été généré par l'option 1
				    }
				    break;
				case 4:
					scanner.close();
					System.out.println("Terminaison du programme... !");
					System.exit(0);
					break;
				default:
					System.err.println("Choix invalide ! Réessayez.\n");
					break;
				}
			} else {
				System.err.println("Choix invalide ! Entrez un numéro correspondant à l'option.\n");
				scanner.next(); 
			}
		}
	}

	
}