package taskflow;

import java.text.Normalizer;

/** Niveaux de priorité d'une tâche. */
public enum Priorite {
    BASSE("Basse"),
    NORMALE("Normale"),
    ELEVEE("Élevée");

    private final String libelle;

    Priorite(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    /**
     * Convertit une saisie (sans tenir compte de la casse ni des accents).
     * Accepte aussi les numéros du menu : 1 = Basse, 2 = Normale, 3 = Élevée.
     * Retourne null si invalide.
     */
    public static Priorite depuisTexte(String texte) {
        if (texte == null) {
            return null;
        }
        String saisie = normaliser(texte);
        for (Priorite p : values()) {
            String numero = String.valueOf(p.ordinal() + 1);
            if (normaliser(p.libelle).equals(saisie) || numero.equals(saisie)) {
                return p;
            }
        }
        return null;
    }

    private static String normaliser(String s) {
        String sansAccents = Normalizer.normalize(s.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sansAccents.toLowerCase();
    }
}
