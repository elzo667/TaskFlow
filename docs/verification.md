# TaskFlow — Vérifications manuelles

## Contexte d'exécution

| Élément | Valeur |
|---|---|
| Version vérifiée | `main`, commit `8ff6444` (après fusion de #28, #30 et #31) |
| Date | 2026-10-20 |
| Système | Windows 11 |
| JDK | 24.0.2 |
| Exécuté par | Laeticia Hammi |

## Méthode

1. Compiler depuis la racine du dépôt : `javac -encoding UTF-8 -d out taskflow/*.java`
2. Pour chaque scénario, partir d'un état connu :
   - **sans fichier** : supprimer `taches.txt` s'il existe ;
   - **avec un fichier préparé** : copier le fichier indiqué depuis [`docs/donnees-test/`](donnees-test/) vers `taches.txt`.
3. Lancer `java -cp out taskflow.Main` et saisir les entrées indiquées dans la colonne « Données et saisies ».
   Les saisies sont séparées par des virgules : chaque élément correspond à une ligne tapée puis validée par Entrée. « Entrée » seul signifie appuyer sur Entrée sans rien taper.
4. Comparer l'affichage au résultat attendu.

Toutes les données utilisées sont fictives.

## Tableau de vérification

| # | Scénario | Données et saisies | Résultat attendu | Résultat observé | Conclusion |
|---|---|---|---|---|---|
| 1 | Liste vide | Sans fichier. `2` puis `0` | Démarrage avec une liste vide. Message explicite à l'affichage. | « Aucun fichier de données trouvé. L'application démarre avec une liste vide. » puis « Aucune tâche à afficher. » | Conforme |
| 2 | Ajout valide avec valeurs par défaut | Sans fichier. `1, Mettre à jour le guide utilisateur, Entrée, Entrée` | Identifiant 1, priorité Normale, aucune échéance, statut À faire, message de confirmation avec l'identifiant. | « Tâche ajoutée avec succès. Identifiant : 1 … Priorité : Normale, Échéance : Aucune, Statut : À faire » | Conforme |
| 3 | Titre invalide | Sans fichier. `1, Entrée` (vide), 3 espaces, `annuler`, puis `2` | Titre vide et titre d'espaces refusés avec un message. Possibilité de corriger ou d'annuler. Aucune tâche ajoutée. | « Le titre ne peut pas être vide. » affiché 2 fois, puis « Ajout annulé. Aucune tâche n'a été ajoutée. ». La liste affiche « Aucune tâche à afficher. » | Conforme (après #30) |
| 4 | Liste de plusieurs tâches | Sans fichier. 3 ajouts : « Vérifier la sauvegarde du serveur » (priorité `3`, échéance `2026-10-25`), « Préparer le poste de Marie » (`basse`, Entrée), « Mettre à jour le guide utilisateur » (Entrée, `2026-11-30`), puis `2` | Les 3 tâches apparaissent avec identifiant, titre, priorité, échéance (ou « Aucune ») et statut. | Tableau avec ID 1 à 3 : Élevée / 2026-10-25, Basse / Aucune, Normale / 2026-11-30, toutes « À faire ». « Nombre de tâches : 3 » | Conforme |
| 5 | Tâche terminée | Sans fichier. Ajout de « Tâche A » et « Tâche B ». `3, 1`, `2`, puis à nouveau `3, 1`, `2` | Seule la tâche 1 passe à Terminée. Une 2ᵉ demande est signalée sans rien modifier. | « La tâche « Tâche A » est maintenant terminée. » La liste montre A = Terminée et B = À faire. 2ᵉ fois : « La tâche « Tâche A » est déjà terminée. » La liste est identique. | Conforme |
| 6 | Identifiant inconnu | Sans fichier. Ajout de « Tâche A ». `3, 99`, `4, 99`, `5, 99`, `3, abc`, puis `2` | Message pour chaque identifiant inconnu ou invalide. Données inchangées. | « Aucune tâche ne porte l'identifiant 99. » (menus 3, 4 et 5). « Identifiant invalide. Entrez un nombre entier. » pour `abc`. La tâche A reste inchangée. | Conforme |
| 7 | Priorité | Sans fichier. Ajout de « Tâche A » (Normale). `4, 1, Basse`, `4, 1, 3`, `4, 1, urgent`, puis `2` | Basse et 3 (Élevée) acceptées. « urgent » refusé, priorité actuelle conservée. | « … est maintenant : Basse. », puis « … est maintenant : Élevée. », puis « Priorité invalide. La priorité actuelle est conservée. » Liste : Élevée. | Conforme (après #28) |
| 8 | Échéance | Sans fichier. Ajout de « Tâche A » avec l'échéance `2026-02-30` puis Entrée. Puis menu 5 sur la tâche 1 : définir `2026-12-15`, puis `2026-13-01`, `2026-02-30`, `15/12/2026`, puis `2028-02-29`, puis `2026-01-15` (date passée), puis retirer 2 fois. | Dates valides acceptées (y compris le 29 février d'une année bissextile et une date passée, énoncé section 3). Format incorrect et dates impossibles refusés sans modifier l'échéance. Retrait fonctionnel. Retrait sans échéance signalé. | À l'ajout, `2026-02-30` est refusé (« Date invalide… ») et la saisie est redemandée. `2026-12-15` est accepté. `2026-13-01`, `2026-02-30` et `15/12/2026` sont refusés (« Date invalide. L'échéance actuelle est conservée. »), la liste affiche toujours 2026-12-15. `2028-02-29` est accepté. `2026-01-15` est accepté (« Échéance mise à jour : 2026-01-15 »), sans avertissement. Retrait : « L'échéance a été retirée. » 2ᵉ retrait : « Cette tâche n'a aucune échéance. Rien à retirer. » | Conforme |
| 9 | Sauvegarde et redémarrage | Parcours de l'énoncé (section 5), sans fichier : ajout de « Vérifier la sauvegarde du serveur » (priorité `3`, échéance `2026-10-25`) et de « Mettre à jour le guide utilisateur » (`2`, Entrée). `2`, `4, 2, 1`, `3, 1`, `6`, `0`. Relancer, puis `2` | Sauvegarde confirmée. Au redémarrage, les 2 tâches sont retrouvées avec les mêmes identifiants, priorités, échéances (y compris absente) et statuts. | « Sauvegarde réussie : 2 tâche(s) enregistrée(s) dans taches.txt. » Au redémarrage : « 2 tâche(s) chargée(s) depuis taches.txt. » 1 = Élevée / 2026-10-25 / Terminée. 2 = Basse / Aucune / À faire. | Conforme |
| 10 | Ajout après rechargement | Suite du scénario 9 (fichier avec les ID 1 et 2). `1, Préparer le poste du nouvel employé, Entrée, Entrée`, puis `2` | La nouvelle tâche reçoit un identifiant distinct (3). | « Identifiant : 3 ». La liste contient les ID 1, 2 et 3. | Conforme |
| 11 | Fichier absent ou mal formé | a) Sans fichier, `0`. b) [`entete-incorrect.txt`](donnees-test/entete-incorrect.txt) c) [`date-impossible.txt`](donnees-test/date-impossible.txt) d) [`identifiant-double.txt`](donnees-test/identifiant-double.txt) e) [`champ-manquant.txt`](donnees-test/champ-manquant.txt) | a) Démarrage avec une liste vide. b à e) Message clair, démarrage interrompu, fichier non modifié. | a) « L'application démarre avec une liste vide. » b) « illisible ou mal formé. Détail : en-tête manquant ou incorrect ». c) « ligne 2 : échéance invalide ». d) « ligne 3 : identifiant invalide ou en double ». e) « ligne 2 : 5 champs attendus ». Dans les cas b à e, l'application s'arrête sans afficher le menu, et le contenu du fichier est identique avant et après (comparaison de l'empreinte MD5). | Conforme |
| 12 | Échec d'écriture | Copier [`valide.txt`](donnees-test/valide.txt) vers `taches.txt` et le mettre en lecture seule (Propriétés, Lecture seule, ou `attrib +R taches.txt`). Lancer, ajouter « Nouvelle tâche », puis `6` | L'échec est signalé et aucune réussite n'est annoncée. Le fichier n'est pas modifié. | « Échec de la sauvegarde : impossible d'écrire dans taches.txt. Cause possible : fichier en lecture seule ou dossier protégé. Vos données n'ont pas été enregistrées. » Le fichier ne contient que les tâches d'origine. | Conforme |
| 13 | Tâches à faire (US-18) | A1) Sans fichier. `7`. A2) `1, Tâche A, Entrée, Entrée`, `1, Tâche B, 3, 2026-11-30`, puis `7`. A3) `3, 1`, puis `7`. A4) `3, 2`, puis `7`. A5) `2`. A6) Copier [`valide.txt`](donnees-test/valide.txt) vers `taches.txt`, relancer, puis `7`. A7) `9`, puis `abc` | A1) « Aucune tâche à afficher. » A2) « TÂCHES À FAIRE » avec les tâches 1 et 2, mêmes colonnes que l'option 2, « Nombre de tâches à faire : 2 ». A3) Seule la tâche 2 apparaît, « Nombre de tâches à faire : 1 ». A4) « Aucune tâche à faire. Toutes les tâches sont terminées. » A5) L'option 2 affiche toujours les 2 tâches, au statut Terminée. A6) Seules les tâches au statut À faire du fichier apparaissent. Le fichier n'est pas modifié. A7) « Choix invalide. Veuillez entrer un nombre du menu. » pour `9` et pour `abc`. | Résultats identiques aux attendus. | Conforme |

Après le scénario 12, retirer la lecture seule (`attrib -R taches.txt`) avant de supprimer le fichier.

Le scénario 13 a été exécuté par Jael Mulamba Ilunga le 2026-10-28 sur la branche `feature/US-18-taches-a-faire` (commit `0359333`), sous Windows 11 avec le JDK 21.

## Questions soulevées pendant les vérifications

**Échéance antérieure à la date du jour.** L'application accepte une échéance passée, par exemple `2026-01-15` saisie le 2026-10-20, sans afficher de message. C'est conforme à l'énoncé (section 3 : les dates passées sont acceptées, puisqu'une tâche peut être en retard), mais le comportement reste discutable :

- une date passée saisie par erreur (ex. `2025` au lieu de `2026`) n'est pas détectée ;
- à l'affichage, rien ne distingue une tâche en retard d'une tâche à venir.

Nous gardons le comportement demandé par l'énoncé. Pour une prochaine version, nous proposons de demander une confirmation lorsqu'une échéance passée est saisie, et d'indiquer « en retard » dans la liste pour les tâches non terminées dont l'échéance est dépassée. À discuter avec l'équipe avant de l'ajouter au backlog.

## Anomalies

### Trouvées pendant les vérifications et corrigées

| Issue | Anomalie | Correction |
|---|---|---|
| [#27](https://github.com/elzo667/TaskFlow/issues/27) | Les priorités 1, 2 et 3 étaient refusées alors que l'invite les propose. | [#28](https://github.com/elzo667/TaskFlow/pull/28) |
| [#29](https://github.com/elzo667/TaskFlow/issues/29) | Un titre vide annulait l'ajout sans permettre de corriger la saisie. Une fin d'entrée pendant un ajout provoquait une boucle infinie. | [#30](https://github.com/elzo667/TaskFlow/pull/30) |

### Connues, non corrigées

- **Pas de sauvegarde automatique** : après un échec de sauvegarde (scénario 12) ou si l'utilisateur quitte sans utiliser l'option 6, les modifications depuis la dernière sauvegarde sont perdues.
- **Sauvegarde non atomique** : `taches.txt` est réécrit directement. Une interruption pendant l'écriture pourrait laisser un fichier incomplet. Ce cas n'a pas été reproduit.
- **Emplacement du fichier** : `taches.txt` est lu et écrit dans le dossier depuis lequel `java` est lancé.
- **Fin de lignes** : sous Windows, le fichier est écrit avec des fins de ligne CRLF. Le chargement les accepte. Le comportement avec un fichier créé sous un autre système n'a pas été vérifié.
- **Code de sortie** : quand le démarrage est interrompu par un fichier mal formé, le programme se termine avec le code 0 (succès). Ce n'est pas exigé par l'énoncé, mais un code non nul serait plus précis pour un script.
