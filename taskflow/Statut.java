package taskflow;

/** Statut d'une tâche. */
public enum Statut {
    A_FAIRE("À faire"),
    TERMINEE("Terminée");

    private final String libelle;

    Statut(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
