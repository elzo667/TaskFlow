package taskflow;

import java.util.List;

/** Affichages de TaskFlow : menu principal et tableau des tâches. */
public class AffichageConsole {
    private static final String FORMAT_ENTETE_TABLEAU = "%-6s %-55s %-10s %-13s %s%n";
    private static final String FORMAT_LIGNE_TABLEAU = "%-6d %-55s %-10s %-13s %s%n";

    public void afficherMenu() {
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

    public void afficherTableau(List<Tache> taches) {
        System.out.printf(FORMAT_ENTETE_TABLEAU, "ID", "Titre", "Priorité", "Échéance", "Statut");
        for (Tache tache : taches) {
            System.out.printf(FORMAT_LIGNE_TABLEAU, tache.getId(), tache.getTitre(),
                    tache.getPriorite().getLibelle(), tache.getEcheanceTexte(), tache.getStatut().getLibelle());
        }
    }
}
