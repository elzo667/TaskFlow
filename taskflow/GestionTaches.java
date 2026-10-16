package taskflow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Logique de gestion des tâches (sans entrée/sortie console). */
public class GestionTaches {
    private final List<Tache> taches = new ArrayList<>();
    private int prochainId = 1;

    /** Ajoute une tâche. Retourne null si le titre est vide ou composé d'espaces. */
    public Tache ajouter(String titre, Priorite priorite, LocalDate echeance) {
        if (titre == null || titre.trim().isEmpty()) {
            return null;
        }
        Tache tache = new Tache(prochainId++, titre.trim().replace('\t', ' '), priorite, echeance);
        taches.add(tache);
        return tache;
    }

    public List<Tache> lister() {
        return Collections.unmodifiableList(taches);
    }

    public Tache trouver(int id) {
        for (Tache t : taches) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    /** Marque la tâche terminée. Retourne false si l'identifiant est inconnu. */
    public boolean terminer(int id) {
        Tache t = trouver(id);
        if (t == null) {
            return false;
        }
        t.setStatut(Statut.TERMINEE);
        return true;
    }

    /** Modifie la priorité. Retourne false si l'identifiant est inconnu. */
    public boolean modifierPriorite(int id, Priorite priorite) {
        Tache t = trouver(id);
        if (t == null) {
            return false;
        }
        t.setPriorite(priorite);
        return true;
    }

    /** Définit ou modifie l'échéance. Retourne false si l'identifiant est inconnu. */
    public boolean modifierEcheance(int id, LocalDate echeance) {
        Tache t = trouver(id);
        if (t == null) {
            return false;
        }
        t.setEcheance(echeance);
        return true;
    }

    /** Retire l'échéance. Retourne false si l'identifiant est inconnu. */
    public boolean retirerEcheance(int id) {
        return modifierEcheance(id, null);
    }

    /** Remplace les tâches par celles chargées et calcule le prochain identifiant. */
    public void charger(List<Tache> chargees) {
        taches.clear();
        taches.addAll(chargees);
        int max = 0;
        for (Tache t : taches) {
            if (t.getId() > max) {
                max = t.getId();
            }
        }
        prochainId = max + 1;
    }
}