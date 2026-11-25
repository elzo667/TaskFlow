package taskflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Interface console de TaskFlow (menu, saisies et affichages). */
public class ConsoleTaskFlow {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleTaskFlow.class);
    private static final Path FICHIER = Paths.get("taches.txt");
    private static final String CHOIX_AJOUTER = "1";
    private static final String CHOIX_AFFICHER = "2";
    private static final String CHOIX_TERMINER = "3";
    private static final String CHOIX_PRIORITE = "4";
    private static final String CHOIX_ECHEANCE = "5";
    private static final String CHOIX_SAUVEGARDER = "6";
    private static final String CHOIX_A_FAIRE = "7";
    private static final String CHOIX_RECHERCHER = "8";
    private static final String CHOIX_QUITTER = "0";

    private final StockageTaches stockage = new StockageTaches();
    private final SaisieConsole entree = new SaisieConsole();
    private final AffichageConsole affichage = new AffichageConsole();
    private final GestionTaches gestion = new GestionTaches();

    public void demarrer() {
        System.out.println("TASKFLOW — Gestion des tâches");
        LOG.info("Démarrage de TaskFlow");
        System.out.println();
        if (!chargerAuDemarrage()) {
            return;
        }

        boolean continuer = true;
        while (continuer) {
            affichage.afficherMenu();
            String choix = entree.lire("Votre choix : ");
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
                    LOG.info("Arrêt de TaskFlow");
                    continuer = false;
                    break;
                default:
                    System.out.println("\nChoix invalide. Veuillez entrer un nombre du menu.");
                    LOG.warn("Saisie invalide refusée : choix du menu");
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
            LOG.error("Fichier de données illisible", e);
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
            LOG.error("Échec de la sauvegarde", e);
            System.out.println("\nÉchec de la sauvegarde : impossible d'écrire dans " + FICHIER + ".");
            System.out.println("Cause possible : fichier en lecture seule ou dossier protégé.");
            System.out.println("Vos données n'ont pas été enregistrées.");
        }
    }


    private void ajouterTache() {
        System.out.println("\nAJOUTER UNE TÂCHE\n");
        String titre = entree.lireTitre();
        if (titre == null) {
            annulerAjout();
            return;
        }

        Priorite priorite = entree.lirePriorite();
        if (priorite == null) {
            annulerAjout();
            return;
        }

        LocalDate echeance = entree.lireEcheance();
        if (entree.estTerminee()) {
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

    private void annulerAjout() {
        System.out.println("\nAjout annulé. Aucune tâche n'a été ajoutée.");
    }

    private void afficherTaches() {
        if (gestion.lister().isEmpty()) {
            System.out.println("\nAucune tâche à afficher.");
            return;
        }
        System.out.println("\nLISTE DES TÂCHES\n");
        affichage.afficherTableau(gestion.lister());
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
        affichage.afficherTableau(aFaire);
        System.out.println("\nNombre de tâches à faire : " + aFaire.size());
    }

    private void rechercherParTitre() {
        if (gestion.lister().isEmpty()) {
            System.out.println("\nAucune tâche à afficher.");
            return;
        }
        System.out.println("\nRECHERCHER UNE TÂCHE PAR UN MOT DU TITRE\n");
        String mot = entree.lire("Mot à rechercher : ");
        if (entree.estTerminee()) {
            return;
        }
        if (mot.trim().isEmpty()) {
            System.out.println("\nLe mot à rechercher ne peut pas être vide.");
            LOG.warn("Saisie invalide refusée : mot de recherche vide");
            return;
        }
        List<Tache> resultats = gestion.rechercherParTitre(mot);
        if (resultats.isEmpty()) {
            System.out.println("\nAucune tâche ne contient « " + mot.trim() + " » dans son titre.");
            return;
        }
        System.out.println("\nRÉSULTATS DE LA RECHERCHE « " + mot.trim() + " »\n");
        affichage.afficherTableau(resultats);
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
            LOG.warn("Tâche {} déjà terminée", t.getId());
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
                entree.lire("Nouvelle priorité (Basse, Normale, Élevée ou 1, 2, 3) : "));
        if (nouvelle == null) {
            System.out.println("\nPriorité invalide. La priorité actuelle est conservée.");
            LOG.warn("Saisie invalide refusée : priorité");
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
        String choix = entree.lire("Votre choix : ").trim();
        switch (choix) {
            case "1": definirEcheance(t); break;
            case "2": retirerEcheance(t); break;
            case "0": System.out.println("\nOpération annulée."); break;
            default: System.out.println("\nChoix invalide. Aucune modification.");
                LOG.warn("Saisie invalide refusée : choix de l'échéance");
        }
    }

    private void definirEcheance(Tache t) {
        String saisie = entree.lire("Nouvelle échéance (AAAA-MM-JJ) : ").trim();
        LocalDate date;
        try {
            date = LocalDate.parse(saisie, SaisieConsole.FORMAT_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("\nDate invalide. L'échéance actuelle est conservée.");
            LOG.warn("Saisie invalide refusée : échéance");
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
        Integer id = entree.lireIdentifiant();
        if (id == null) {
            return null;
        }
        Tache tache = gestion.trouver(id);
        if (tache == null) {
            System.out.println("\nAucune tâche ne porte l'identifiant " + id + ".");
            LOG.warn("Identifiant inconnu : {}", id);
        }
        return tache;
    }
}