package taskflow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/** Saisies au clavier de TaskFlow : lecture des lignes et validation des valeurs saisies. */
public class SaisieConsole {
    public static final DateTimeFormatter FORMAT_DATE =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private static final String MOT_ANNULER = "annuler";

    private final Scanner scanner = new Scanner(System.in);
    private boolean terminee = false; // vrai quand il n'y a plus rien à lire

    /** Vrai quand l'entrée est épuisée (fin du fichier de saisies). */
    public boolean estTerminee() {
        return terminee;
    }

    /** Affiche l'invite et lit une ligne ; retourne "0" si l'entrée est terminée (le menu quitte). */
    public String lire(String invite) {
        System.out.print(invite);
        if (!scanner.hasNextLine()) {
            terminee = true;
            return "0";
        }
        return scanner.nextLine();
    }

    /** Lit un identifiant ; retourne null (avec un message) si la saisie n'est pas un entier. */
    public Integer lireIdentifiant() {
        String saisie = lire("Identifiant de la tâche : ").trim();
        try {
            return Integer.parseInt(saisie);
        } catch (NumberFormatException e) {
            System.out.println("\nIdentifiant invalide. Entrez un nombre entier.");
            return null;
        }
    }

    /** Lit le titre ; retourne null si l'utilisateur annule ou si l'entrée est terminée. */
    public String lireTitre() {
        String titre = lire("Titre : ");
        while (titre.trim().isEmpty() && !terminee) {
            System.out.println("\nLe titre ne peut pas être vide.");
            titre = lire("Entrez un titre (ou « annuler » pour abandonner) : ");
        }
        if (terminee || titre.trim().equalsIgnoreCase(MOT_ANNULER)) {
            return null;
        }
        return titre;
    }

    /** Lit la priorité ; retourne null si l'entrée est terminée. */
    public Priorite lirePriorite() {
        System.out.println("\nPriorité : Basse (1), Normale (2) ou Élevée (3)");
        System.out.println("Appuyez sur Entrée pour choisir Normale.");
        Priorite priorite = null;
        while (priorite == null) {
            String saisie = lire("Votre priorité : ");
            if (terminee) {
                return null;
            }
            if (saisie.trim().isEmpty()) {
                priorite = Priorite.NORMALE;
            } else {
                priorite = Priorite.depuisTexte(saisie);
                if (priorite == null) {
                    System.out.println("Priorité invalide. Choisissez Basse, Normale ou Élevée (ou 1, 2, 3).");
                }
            }
        }
        return priorite;
    }

    /** Lit l'échéance ; retourne null si aucune échéance n'est saisie ou si l'entrée est terminée. */
    public LocalDate lireEcheance() {
        System.out.println("\nÉchéance au format AAAA-MM-JJ");
        System.out.println("Appuyez sur Entrée pour ne pas préciser d'échéance.");
        while (true) {
            String saisie = lire("Votre échéance : ").trim();
            if (terminee || saisie.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(saisie, FORMAT_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Date invalide. Utilisez le format AAAA-MM-JJ avec une date réelle.");
            }
        }
    }
}
