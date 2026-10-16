package taskflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

/** Interface console de TaskFlow (menu, saisies et affichages). */
public class ConsoleTaskFlow {
    private static final DateTimeFormatter FORMAT_DATE =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private static final Path FICHIER = Paths.get("taches.txt");

    private final StockageTaches stockage = new StockageTaches();
    private final Scanner scanner = new Scanner(System.in);
    private final GestionTaches gestion = new GestionTaches();

    public void demarrer() {
        System.out.println("TASKFLOW — Gestion des tâches");
        System.out.println();
        if (!chargerAuDemarrage()) {
            return;
        }

        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            String choix = lire("Votre choix : ");
            switch (choix) {
                case "1": ajouterTache(); break;
                case "2": afficherTaches(); break;
                case "3": terminerTache(); break;
                case "4": modifierPriorite(); break;
                case "5": gererEcheance(); break;
                case "6": sauvegarder(); break;
                case "0":
                    System.out.println("\nFermeture de TaskFlow. À bientôt.");
                    continuer = false;
                    break;
                default:
                    System.out.println("\nChoix invalide. Veuillez entrer un nombre du menu.");
            }
        }
    }

    private boolean chargerAuDemarrage() {
        if (!Files.exists(FICHIER)) {
            System.out.println("Aucun fichier de données trouvé.");
            System.out.println("L'application démarre avec une liste vide.");
            return true;
        }
        try {
            List<Tache> chargees = stockage.charger(FICHIER);
            gestion.charger(chargees);
            System.out.println(chargees.size() + " tâche(s) chargée(s) depuis " + FICHIER + ".");
            return true;
        } catch (StockageTaches.FormatInvalideException | IOException e) {
            System.out.println("Le fichier " + FICHIER + " est illisible ou mal formé.");
            System.out.println("Détail : " + e.getMessage());
            System.out.println("Le fichier n'a pas été modifié. Corrigez-le ou supprimez-le, puis relancez TaskFlow.");
            return false;
        }
    }

    private void sauvegarder() {
        try {
            stockage.sauvegarder(FICHIER, gestion.lister());
            System.out.println("\nSauvegarde réussie : " + gestion.lister().size()
                    + " tâche(s) enregistrée(s) dans " + FICHIER + ".");
        } catch (IOException e) {
            System.out.println("\nÉchec de la sauvegarde : impossible d'écrire dans " + FICHIER + ".");
            System.out.println("Cause possible : fichier en lecture seule ou dossier protégé.");
            System.out.println("Vos données n'ont pas été enregistrées.");
        }
    }

    private void afficherMenu() {
        System.out.println();
        System.out.println("1. Ajouter une tâche");
        System.out.println("2. Afficher les tâches");
        System.out.println("3. Marquer une tâche comme terminée");
        System.out.println("4. Modifier la priorité d'une tâche");
        System.out.println("5. Gérer l'échéance d'une tâche");
        System.out.println("6. Sauvegarder les données");
        System.out.println("0. Quitter");
        System.out.println();
    }

    private void ajouterTache() {
        System.out.println("\nAJOUTER UNE TÂCHE\n");
        String titre = lire("Titre : ");
        if (titre.trim().isEmpty()) {
            System.out.println("\nLe titre ne peut pas être vide. Aucune tâche n'a été ajoutée.");
            return;
        }

        System.out.println("\nPriorité : Basse (1), Normale (2) ou Élevée (3)");
        System.out.println("Appuyez sur Entrée pour choisir Normale.");
        Priorite priorite = null;
        while (priorite == null) {
            String saisie = lire("Votre priorité : ");
            if (saisie.trim().isEmpty()) {
                priorite = Priorite.NORMALE;
            } else {
                priorite = Priorite.depuisTexte(saisie);
                if (priorite == null) {
                    System.out.println("Priorité invalide. Choisissez Basse, Normale ou Élevée (ou 1, 2, 3).");
                }
            }
        }

        System.out.println("\nÉchéance au format AAAA-MM-JJ");
        System.out.println("Appuyez sur Entrée pour ne pas préciser d'échéance.");
        LocalDate echeance = null;
        boolean valide = false;
        while (!valide) {
            String saisie = lire("Votre échéance : ").trim();
            if (saisie.isEmpty()) {
                valide = true;
            } else {
                try {
                    echeance = LocalDate.parse(saisie, FORMAT_DATE);
                    valide = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Date invalide. Utilisez le format AAAA-MM-JJ avec une date réelle.");
                }
            }
        }

        Tache t = gestion.ajouter(titre, priorite, echeance);
        System.out.println("\nTâche ajoutée avec succès.\n");
        System.out.println("Identifiant : " + t.getId());
        System.out.println("Titre       : " + t.getTitre());
        System.out.println("Priorité    : " + t.getPriorite().getLibelle());
        System.out.println("Échéance    : " + t.getEcheanceTexte());
        System.out.println("Statut      : " + t.getStatut().getLibelle());
    }

    private void afficherTaches() {
        if (gestion.lister().isEmpty()) {
            System.out.println("\nAucune tâche à afficher.");
            return;
        }
        System.out.println("\nLISTE DES TÂCHES\n");
        System.out.printf("%-6s %-55s %-10s %-13s %s%n", "ID", "Titre", "Priorité", "Échéance", "Statut");
        for (Tache t : gestion.lister()) {
            System.out.printf("%-6d %-55s %-10s %-13s %s%n", t.getId(), t.getTitre(),
                    t.getPriorite().getLibelle(), t.getEcheanceTexte(), t.getStatut().getLibelle());
        }
        System.out.println("\nNombre de tâches : " + gestion.lister().size());
    }

    private void terminerTache() {
        System.out.println("\nMARQUER UNE TÂCHE COMME TERMINÉE\n");
        Integer id = lireIdentifiant();
        if (id == null) {
            return;
        }
        Tache t = gestion.trouver(id);
        if (t == null) {
            System.out.println("\nAucune tâche ne porte l'identifiant " + id + ".");
        } else if (t.getStatut() == Statut.TERMINEE) {
            System.out.println("\nLa tâche « " + t.getTitre() + " » est déjà terminée.");
        } else {
            gestion.terminer(id);
            System.out.println("\nLa tâche « " + t.getTitre() + " » est maintenant terminée.");
        }
    }

    private void modifierPriorite() {
        System.out.println("\nMODIFIER LA PRIORITÉ D'UNE TÂCHE\n");
        Integer id = lireIdentifiant();
        if (id == null) {
            return;
        }
        Tache t = gestion.trouver(id);
        if (t == null) {
            System.out.println("\nAucune tâche ne porte l'identifiant " + id + ".");
            return;
        }
        System.out.println("\nPriorité actuelle : " + t.getPriorite().getLibelle());
        Priorite nouvelle = Priorite.depuisTexte(
                lire("Nouvelle priorité (Basse, Normale, Élevée ou 1, 2, 3) : "));
        if (nouvelle == null) {
            System.out.println("\nPriorité invalide. La priorité actuelle est conservée.");
            return;
        }
        gestion.modifierPriorite(id, nouvelle);
        System.out.println("\nLa priorité de la tâche « " + t.getTitre()
                + " » est maintenant : " + nouvelle.getLibelle() + ".");
    }

    private void gererEcheance() {
        System.out.println("\nGÉRER L'ÉCHÉANCE D'UNE TÂCHE\n");
        Integer id = lireIdentifiant();
        if (id == null) {
            return;
        }
        Tache t = gestion.trouver(id);
        if (t == null) {
            System.out.println("\nAucune tâche ne porte l'identifiant " + id + ".");
            return;
        }
        System.out.println("\nTâche : " + t.getTitre());
        System.out.println("Échéance actuelle : " + t.getEcheanceTexte());
        System.out.println();
        System.out.println("1. Définir ou modifier l'échéance");
        System.out.println("2. Retirer l'échéance");
        System.out.println("0. Annuler");
        System.out.println();
        String choix = lire("Votre choix : ").trim();
        switch (choix) {
            case "1": definirEcheance(t); break;
            case "2": retirerEcheance(t); break;
            case "0": System.out.println("\nOpération annulée."); break;
            default: System.out.println("\nChoix invalide. Aucune modification.");
        }
    }

    private void definirEcheance(Tache t) {
        String saisie = lire("Nouvelle échéance (AAAA-MM-JJ) : ").trim();
        LocalDate date;
        try {
            date = LocalDate.parse(saisie, FORMAT_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("\nDate invalide. L'échéance actuelle est conservée.");
            return;
        }
        String ancienne = t.getEcheanceTexte();
        gestion.modifierEcheance(t.getId(), date);
        System.out.println("\nÉchéance mise à jour : " + date + " (ancienne : " + ancienne + ").");
    }

    private void retirerEcheance(Tache t) {
        if (t.getEcheance() == null) {
            System.out.println("\nCette tâche n'a aucune échéance. Rien à retirer.");
            return;
        }
        gestion.retirerEcheance(t.getId());
        System.out.println("\nL'échéance a été retirée. Échéance actuelle : Aucune.");
    }

    private Integer lireIdentifiant() {
        String saisie = lire("Identifiant de la tâche : ").trim();
        try {
            return Integer.parseInt(saisie);
        } catch (NumberFormatException e) {
            System.out.println("\nIdentifiant invalide. Entrez un nombre entier.");
            return null;
        }
    }

    private String lire(String invite) {
        System.out.print(invite);
        if (!scanner.hasNextLine()) {
            return "0"; // fin de l'entrée : quitter proprement
        }
        return scanner.nextLine();
    }
}