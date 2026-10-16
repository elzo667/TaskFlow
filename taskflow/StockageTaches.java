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
        for (Tache t : taches) {
            String echeance = t.getEcheance() == null ? "" : t.getEcheance().toString();
            lignes.add(t.getId() + SEP + t.getTitre() + SEP + t.getPriorite().name()
                    + SEP + echeance + SEP + t.getStatut().name());
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
            int numero = i + 1;
            String[] c = ligne.split(SEP, -1);
            if (c.length != 5) {
                throw new FormatInvalideException("ligne " + numero + " : 5 champs attendus");
            }
            int id;
            try {
                id = Integer.parseInt(c[0]);
            } catch (NumberFormatException e) {
                throw new FormatInvalideException("ligne " + numero + " : identifiant invalide");
            }
            if (id < 1 || !ids.add(id)) {
                throw new FormatInvalideException("ligne " + numero + " : identifiant invalide ou en double");
            }
            if (c[1].trim().isEmpty()) {
                throw new FormatInvalideException("ligne " + numero + " : titre vide");
            }
            Priorite priorite;
            try {
                priorite = Priorite.valueOf(c[2]);
            } catch (IllegalArgumentException e) {
                throw new FormatInvalideException("ligne " + numero + " : priorité invalide");
            }
            LocalDate echeance = null;
            if (!c[3].isEmpty()) {
                try {
                    echeance = LocalDate.parse(c[3], FORMAT_DATE);
                } catch (DateTimeParseException e) {
                    throw new FormatInvalideException("ligne " + numero + " : échéance invalide");
                }
            }
            Statut statut;
            try {
                statut = Statut.valueOf(c[4]);
            } catch (IllegalArgumentException e) {
                throw new FormatInvalideException("ligne " + numero + " : statut invalide");
            }
            Tache t = new Tache(id, c[1], priorite, echeance);
            t.setStatut(statut);
            resultat.add(t);
        }
        return resultat;
    }
}