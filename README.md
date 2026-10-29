# TaskFlow

Application Java en ligne de commande pour gérer une liste de tâches : ajout, affichage, priorité, échéance, statut, sauvegarde et rechargement.

Projet réalisé dans le cadre du cours INF1763 — Mini-projet 1.

## Équipe

| Membre | GitHub |
|---|---|
| Elhadji N Diagne | [@elzo667](https://github.com/elzo667) |
| Laeticia Hammi | [@Laeticiahammi](https://github.com/Laeticiahammi) |
| Jael Mulamba Ilunga | [@JJael](https://github.com/JJael) | 

L'organisation de l'équipe, le backlog, la définition de terminé et les itérations sont décrits dans [docs/organisation.md](docs/organisation.md).
Les vérifications manuelles sont consignées dans [docs/verification.md](docs/verification.md).
Le suivi du travail se fait dans le tableau GitHub Projects [TaskFlow - Suivi MP1](https://github.com/users/JJael/projects/1) (accès réservé à l'équipe et à l'enseignant).

## Fonctionnalités

1. **Ajouter une tâche** : titre obligatoire, priorité (Normale par défaut), échéance facultative au format `AAAA-MM-JJ`. L'identifiant et le statut « À faire » sont attribués automatiquement.
2. **Afficher les tâches** : identifiant, titre, priorité, échéance (ou « Aucune ») et statut. Message explicite si la liste est vide.
3. **Marquer une tâche comme terminée** : signale un identifiant inconnu ou une tâche déjà terminée.
4. **Modifier la priorité** : Basse, Normale ou Élevée (saisie par le mot ou par le numéro 1, 2, 3). Une valeur invalide laisse la priorité inchangée.
5. **Gérer l'échéance** : définir, modifier ou retirer. Une date au mauvais format ou impossible (ex. `2026-02-30`) est refusée.
6. **Sauvegarder** les données dans un fichier local. Elles sont rechargées automatiquement au démarrage suivant.
7. **Afficher seulement les tâches à faire** : mêmes colonnes que l'option 2, avec le nombre de tâches à faire. Message explicite si la liste est vide ou si toutes les tâches sont terminées.
8. **Rechercher une tâche par un mot du titre** : sans tenir compte des majuscules, mêmes colonnes que l'option 2, avec le nombre de résultats. Un mot vide est refusé. Message explicite si la liste est vide ou si aucune tâche ne correspond.
0. **Quitter** l'application.

## Prérequis

- **JDK 11 ou plus récent** (`javac` et `java` accessibles dans le terminal). Le projet a été testé avec le **JDK 24**.
- Aucune bibliothèque externe.

Vérifier l'installation :

```bash
java -version
javac -version
```

## Compiler et exécuter

Depuis la racine du dépôt (le dossier qui contient `README.md` et `taskflow/`) :

```bash
javac -encoding UTF-8 -d out taskflow/*.java
java -cp out taskflow.Main
```

Les fichiers compilés sont placés dans `out/` (ignoré par Git).

Sous Windows, si les accents s'affichent mal dans la console, exécuter `chcp 65001` avant de lancer l'application.

## Exemple d'utilisation

```text
TASKFLOW — Gestion des tâches

Aucun fichier de données trouvé.
L'application démarre avec une liste vide.

1. Ajouter une tâche
2. Afficher les tâches
3. Marquer une tâche comme terminée
4. Modifier la priorité d'une tâche
5. Gérer l'échéance d'une tâche
6. Sauvegarder les données
7. Afficher seulement les tâches à faire
8. Rechercher une tâche par un mot du titre
0. Quitter

Votre choix : 1

AJOUTER UNE TÂCHE

Titre : Vérifier la sauvegarde du serveur

Priorité : Basse (1), Normale (2) ou Élevée (3)
Appuyez sur Entrée pour choisir Normale.
Votre priorité : 3

Échéance au format AAAA-MM-JJ
Appuyez sur Entrée pour ne pas préciser d'échéance.
Votre échéance : 2026-11-03

Tâche ajoutée avec succès.

Identifiant : 1
Titre       : Vérifier la sauvegarde du serveur
Priorité    : Élevée
Échéance    : 2026-11-03
Statut      : À faire

Votre choix : 2

LISTE DES TÂCHES

ID     Titre                                                   Priorité   Échéance      Statut
1      Vérifier la sauvegarde du serveur                       Élevée     2026-11-03    À faire

Nombre de tâches : 1

Votre choix : 6

Sauvegarde réussie : 1 tâche(s) enregistrée(s) dans taches.txt.

Votre choix : 0

Fermeture de TaskFlow. À bientôt.
```

## Fichier de données

### Emplacement

Le fichier s'appelle **`taches.txt`**. Il est créé **dans le dossier courant**, c'est-à-dire le dossier depuis lequel la commande `java` est lancée (normalement la racine du dépôt). Il est ignoré par Git (`.gitignore`).

### Format

Fichier texte encodé en UTF-8 :

- **Ligne 1** : l'en-tête `TASKFLOW-V1`.
- **Lignes suivantes** : une tâche par ligne, 5 champs séparés par une **tabulation** :

| Champ | Valeurs possibles |
|---|---|
| Identifiant | entier positif, unique |
| Titre | texte non vide |
| Priorité | `BASSE`, `NORMALE` ou `ELEVEE` |
| Échéance | date `AAAA-MM-JJ`, ou **vide** si aucune échéance |
| Statut | `A_FAIRE` ou `TERMINEE` |

Exemple (`→` représente une tabulation) :

```text
TASKFLOW-V1
1→Vérifier la sauvegarde du serveur→ELEVEE→2026-11-03→TERMINEE
2→Mettre à jour le guide utilisateur→BASSE→→A_FAIRE
```

## Sauvegarde et rechargement

- **Sauvegarde** : uniquement sur demande, avec l'option **6** du menu. Le fichier est entièrement réécrit avec toutes les tâches. Si l'écriture échoue (fichier en lecture seule, dossier protégé), un message d'échec s'affiche et aucune réussite n'est annoncée.
- **Rechargement** : automatique au démarrage.
  - Fichier absent : l'application démarre avec une liste vide.
  - Fichier présent et valide : les tâches sont rechargées avec leurs identifiants, priorités, échéances et statuts. La prochaine tâche reçoit l'identifiant le plus grand + 1.
  - Fichier illisible ou mal formé (en-tête incorrect, champ manquant, identifiant en double, date ou valeur invalide) : un message indique le problème et la ligne concernée, puis l'application s'arrête **sans modifier le fichier**.
- **Quitter sans sauvegarder** (option 0) : les modifications depuis la dernière sauvegarde sont perdues.

## Limites et anomalies connues

- Pas de sauvegarde automatique : il faut utiliser l'option 6 avant de quitter.
- Le fichier `taches.txt` dépend du dossier depuis lequel l'application est lancée : la lancer depuis un autre dossier fait démarrer avec une autre liste.
- La sauvegarde réécrit directement `taches.txt` : si l'écriture est interrompue en cours de route, le fichier peut être incomplet.
- Une tabulation dans un titre est remplacée par une espace (la tabulation sert de séparateur dans le fichier).
- Il n'est pas possible de supprimer une tâche ni de modifier son titre (hors du périmètre du mini-projet 1).
- Les anomalies relevées pendant les vérifications sont suivies dans les [Issues](https://github.com/elzo667/TaskFlow/issues) et dans [docs/verification.md](docs/verification.md).
