package taskflow;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sauvegarde et chargement des tâches dans un fichier texte (UTF-8).
 * Ligne 1 : TASKFLOW-V1
 * Lignes suivantes : id TAB titre TAB priorité TAB échéance (vide si aucune) TAB statut
 */
public class StockageTaches {
    private static final String ENTETE = "TASKFLOW-V1";
    private static final String SEP = "\t";
    private static final int NB_CHAMPS = 5;
    private static final int IDX_ID = 0;
    private static final int IDX_TITRE = 1;
    private static final int IDX_PRIORITE = 2;
    private static final int IDX_ECHEANCE = 3;
    private static final int IDX_STATUT = 4;
    private static final DateTimeFormatter FORMAT_DATE =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    /** Signale un fichier de données mal formé. */
    public static class FormatInvalideException extends Exception {
        public FormatInvalideException(String message) {
            super(message);
        }
    }

    public void sauvegarder(Path fichier, List<Tache> taches) throws IOException {
        List<String> lignes = new ArrayList<>();
        lignes.add(ENTETE);
        for (Tache tache : taches) {
            String echeance = tache.getEcheance() == null ? "" : tache.getEcheance().toString();
            lignes.add(tache.getId() + SEP + tache.getTitre() + SEP + tache.getPriorite().name()
                    + SEP + echeance + SEP + tache.getStatut().name());
        }
        Files.write(fichier, lignes, StandardCharsets.UTF_8);
    }

    public List<Tache> charger(Path fichier) throws IOException, FormatInvalideException {
        List<String> lignes = Files.readAllLines(fichier, StandardCharsets.UTF_8);
        if (lignes.isEmpty() || !lignes.get(0).equals(ENTETE)) {
            throw new FormatInvalideException("en-tête manquant ou incorrect");
        }
        List<Tache> resultat = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        for (int i = 1; i < lignes.size(); i++) {
            String ligne = lignes.get(i);
            if (ligne.trim().isEmpty()) {
                continue;
            }
            resultat.add(lireTache(ligne, i + 1, ids));
        }
        return resultat;
    }

    /** Convertit une ligne du fichier en tâche ; lève une exception si la ligne est invalide. */
    private Tache lireTache(String ligne, int numero, Set<Integer> ids) throws FormatInvalideException {
        String[] champs = ligne.split(SEP, -1);
        if (champs.length != NB_CHAMPS) {
            throw new FormatInvalideException("ligne " + numero + " : 5 champs attendus");
        }
        int id;
        try {
            id = Integer.parseInt(champs[IDX_ID]);
        } catch (NumberFormatException e) {
            throw new FormatInvalideException("ligne " + numero + " : identifiant invalide");
        }
        if (id < 1 || !ids.add(id)) {
            throw new FormatInvalideException("ligne " + numero + " : identifiant invalide ou en double");
        }
        if (champs[IDX_TITRE].trim().isEmpty()) {
            throw new FormatInvalideException("ligne " + numero + " : titre vide");
        }
        Priorite priorite;
        try {
            priorite = Priorite.valueOf(champs[IDX_PRIORITE]);
        } catch (IllegalArgumentException e) {
            throw new FormatInvalideException("ligne " + numero + " : priorité invalide");
        }
        LocalDate echeance = null;
        if (!champs[IDX_ECHEANCE].isEmpty()) {
            try {
                echeance = LocalDate.parse(champs[IDX_ECHEANCE], FORMAT_DATE);
            } catch (DateTimeParseException e) {
                throw new FormatInvalideException("ligne " + numero + " : échéance invalide");
            }
        }
        Statut statut;
        try {
            statut = Statut.valueOf(champs[IDX_STATUT]);
        } catch (IllegalArgumentException e) {
            throw new FormatInvalideException("ligne " + numero + " : statut invalide");
        }
        Tache tache = new Tache(id, champs[IDX_TITRE], priorite, echeance);
        tache.setStatut(statut);
        return tache;
    }
}