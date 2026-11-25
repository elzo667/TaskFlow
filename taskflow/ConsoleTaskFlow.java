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
    private static final String MOT_ANNULER = "annuler";
    private static final String CHOIX_AJOUTER = "1";
    private static final String CHOIX_AFFICHER = "2";
    private static final String CHOIX_TERMINER = "3";
    private static final String CHOIX_PRIORITE = "4";
    private static final String CHOIX_ECHEANCE = "5";
    private static final String CHOIX_SAUVEGARDER = "6";
    private static final String CHOIX_A_FAIRE = "7";
    private static final String CHOIX_RECHERCHER = "8";
    private static final String CHOIX_QUITTER = "0";
    private static final String FORMAT_ENTETE_TABLEAU = "%-6s %-55s %-10s %-13s %s%n";
    private static final String FORMAT_LIGNE_TABLEAU = "%-6d %-55s %-10s %-13s %s%n";

    private final StockageTaches stockage = new StockageTaches();
    private final Scanner scanner = new Scanner(System.in);
    private final GestionTaches gestion = new GestionTaches();
    private boolean entreeTerminee = false; // vrai quand il n'y a plus rien à lire

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
                case CHOIX_AJOUTER: ajouterTache(); break;
                case CHOIX_AFFICHER: afficherTaches(); break;
                case CHOIX_TERMINER: terminerTache(); break;
                case CHOIX_PRIORITE: modifierPriorite(); break;
                case CHOIX_ECHEANCE: gererEcheance(); break;
                case CHOIX_SAUVEGARDER: sauvegarder(); break;
                case CHOIX_A_FAIRE: afficherTachesAFaire(); break;
                case CHOIX_RECHERCHER: rechercherParTitre(); break;
                case CHOIX_QUITTER:
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
        System.out.println("7. Afficher seulement les tâches à faire");
        System.out.println("8. Rechercher une tâche par un mot du titre");
        System.out.println("0. Quitter");
        System.out.println();
    }

    private void ajouterTache() {
        System.out.println("\nAJOUTER UNE TÂCHE\n");
        String titre = lireTitre();
        if (titre == null) {
            annulerAjout();
            return;
        }

        Priorite priorite = lirePriorite();
        if (priorite == null) {
            annulerAjout();
            return;
        }

        LocalDate echeance = lireEcheance();
        if (entreeTerminee) {
            annulerAjout();
            return;
        }

        Tache t = gestion.ajouter(titre, priorite, echeance);
        System.out.println("\nTâche ajoutée avec succès.\n");
        System.out.println("Identifiant : " + t.getId());
        System.out.println("Titre       : " + t.getTitre());
        System.out.println("Priorité    : " + t.getPriorite().getLibelle());
        System.out.println("Échéance    : " + t.getEcheanceTexte());
        System.out.println("Statut      : " + t.getStatut().getLibelle());
    }

    /** Lit le titre ; retourne null si l'utilisateur annule ou si l'entrée est terminée. */
    private String lireTitre() {
        String titre = lire("Titre : ");
        while (titre.trim().isEmpty() && !entreeTerminee) {
            System.out.println("\nLe titre ne peut pas être vide.");
            titre = lire("Entrez un titre (ou « annuler » pour abandonner) : ");
        }
        if (entreeTerminee || titre.trim().equalsIgnoreCase(MOT_ANNULER)) {
            return null;
        }
        return titre;
    }

    /** Lit la priorité ; retourne null si l'entrée est terminée. */
    private Priorite lirePriorite() {
        System.out.println("\nPriorité : Basse (1), Normale (2) ou Élevée (3)");
        System.out.println("Appuyez sur Entrée pour choisir Normale.");
        Priorite priorite = null;
        while (priorite == null) {
            String saisie = lire("Votre priorité : ");
            if (entreeTerminee) {
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
    private LocalDate lireEcheance() {
        System.out.println("\nÉchéance au format AAAA-MM-JJ");
        System.out.println("Appuyez sur Entrée pour ne pas préciser d'échéance.");
        while (true) {
            String saisie = lire("Votre échéance : ").trim();
            if (entreeTerminee || saisie.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(saisie, FORMAT_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Date invalide. Utilisez le format AAAA-MM-JJ avec une date réelle.");
            }
        }
    }

    private void annulerAjout() {
        System.out.println("\nAjout annulé. Aucune tâche n'a été ajoutée.");
    }

    private void afficherTaches() {
        if (gestion.lister().isEmpty()) {
            System.out.println("\nAucune tâche à afficher.");
            return;
        }
        System.out.println("\nLISTE DES TÂCHES\n");
        System.out.printf(FORMAT_ENTETE_TABLEAU, "ID", "Titre", "Priorité", "Échéance", "Statut");
        for (Tache t : gestion.lister()) {
            System.out.printf(FORMAT_LIGNE_TABLEAU, t.getId(), t.getTitre(),
                    t.getPriorite().getLibelle(), t.getEcheanceTexte(), t.getStatut().getLibelle());
        }
        System.out.println("\nNombre de tâches : " + gestion.lister().size());
    }

    private void afficherTachesAFaire() {
        if (gestion.lister().isEmpty()) {
            System.out.println("\nAucune tâche à afficher.");
            return;
        }
        List<Tache> aFaire = gestion.listerAFaire();
        if (aFaire.isEmpty()) {
            System.out.println("\nAucune tâche à faire. Toutes les tâches sont terminées.");
            return;
        }
        System.out.println("\nTÂCHES À FAIRE\n");
        System.out.printf(FORMAT_ENTETE_TABLEAU, "ID", "Titre", "Priorité", "Échéance", "Statut");
        for (Tache t : aFaire) {
            System.out.printf(FORMAT_LIGNE_TABLEAU, t.getId(), t.getTitre(),
                    t.getPriorite().getLibelle(), t.getEcheanceTexte(), t.getStatut().getLibelle());
        }
        System.out.println("\nNombre de tâches à faire : " + aFaire.size());
    }

    private void rechercherParTitre() {
        if (gestion.lister().isEmpty()) {
            System.out.println("\nAucune tâche à afficher.");
            return;
        }
        System.out.println("\nRECHERCHER UNE TÂCHE PAR UN MOT DU TITRE\n");
        String mot = lire("Mot à rechercher : ");
        if (entreeTerminee) {
            return;
        }
        if (mot.trim().isEmpty()) {
            System.out.println("\nLe mot à rechercher ne peut pas être vide.");
            return;
        }
        List<Tache> resultats = gestion.rechercherParTitre(mot);
        if (resultats.isEmpty()) {
            System.out.println("\nAucune tâche ne contient « " + mot.trim() + " » dans son titre.");
            return;
        }
        System.out.println("\nRÉSULTATS DE LA RECHERCHE « " + mot.trim() + " »\n");
        System.out.printf(FORMAT_ENTETE_TABLEAU, "ID", "Titre", "Priorité", "Échéance", "Statut");
        for (Tache t : resultats) {
            System.out.printf(FORMAT_LIGNE_TABLEAU, t.getId(), t.getTitre(),
                    t.getPriorite().getLibelle(), t.getEcheanceTexte(), t.getStatut().getLibelle());
        }
        System.out.println("\nNombre de résultats : " + resultats.size());
    }

    private void terminerTache() {
        System.out.println("\nMARQUER UNE TÂCHE COMME TERMINÉE\n");
        Tache t = demanderTache();
        if (t == null) {
            return;
        }
        if (t.getStatut() == Statut.TERMINEE) {
            System.out.println("\nLa tâche « " + t.getTitre() + " » est déjà terminée.");
        } else {
            gestion.terminer(t.getId());
            System.out.println("\nLa tâche « " + t.getTitre() + " » est maintenant terminée.");
        }
    }

    private void modifierPriorite() {
        System.out.println("\nMODIFIER LA PRIORITÉ D'UNE TÂCHE\n");
        Tache t = demanderTache();
        if (t == null) {
            return;
        }
        System.out.println("\nPriorité actuelle : " + t.getPriorite().getLibelle());
        Priorite nouvelle = Priorite.depuisTexte(
                lire("Nouvelle priorité (Basse, Normale, Élevée ou 1, 2, 3) : "));
        if (nouvelle == null) {
            System.out.println("\nPriorité invalide. La priorité actuelle est conservée.");
            return;
        }
        gestion.modifierPriorite(t.getId(), nouvelle);
        System.out.println("\nLa priorité de la tâche « " + t.getTitre()
                + " » est maintenant : " + nouvelle.getLibelle() + ".");
    }

    private void gererEcheance() {
        System.out.println("\nGÉRER L'ÉCHÉANCE D'UNE TÂCHE\n");
        Tache t = demanderTache();
        if (t == null) {
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

    private Tache demanderTache() {
        Integer id = lireIdentifiant();
        if (id == null) {
            return null;
        }
        Tache tache = gestion.trouver(id);
        if (tache == null) {
            System.out.println("\nAucune tâche ne porte l'identifiant " + id + ".");
        }
        return tache;
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
            entreeTerminee = true;
            return CHOIX_QUITTER; // fin de l'entrée : le menu principal quitte proprement
        }
        return scanner.nextLine();
    }
}