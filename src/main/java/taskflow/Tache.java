package taskflow;

import java.time.LocalDate;

/** Une tâche gérée par TaskFlow. */
public class Tache {
    private final int id;
    private final String titre;
    private Priorite priorite;
    private LocalDate echeance; // null = aucune échéance
    private Statut statut;

    public Tache(int id, String titre, Priorite priorite, LocalDate echeance) {
        this.id = id;
        this.titre = titre;
        this.priorite = priorite;
        this.echeance = echeance;
        this.statut = Statut.A_FAIRE;
    }

    public int getId() { return id; }
    public String getTitre() { return titre; }
    public Priorite getPriorite() { return priorite; }
    public LocalDate getEcheance() { return echeance; }
    public Statut getStatut() { return statut; }

    public void setPriorite(Priorite priorite) { this.priorite = priorite; }
    public void setEcheance(LocalDate echeance) { this.echeance = echeance; }
    public void setStatut(Statut statut) { this.statut = statut; }

    public String getEcheanceTexte() {
        return echeance == null ? "Aucune" : echeance.toString();
    }
}
