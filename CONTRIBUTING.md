# Contribuer à TaskFlow

Ce document décrit comment l'équipe propose, relit et intègre un changement. Il complète la définition de terminé et les règles de passage de [docs/organisation.md](docs/organisation.md).

## Processus pour chaque changement

1. Choisir une Issue (récit `US-xx`, tâche technique `T-xx` ou anomalie), se l'assigner et déplacer sa carte dans « En cours » du tableau de suivi.
2. Créer une branche depuis `main` à jour :
   - `feature/US-xx-sujet` pour un récit
   - `fix/sujet` pour une anomalie
   - `docs/T-xx-sujet` pour la documentation
   - `chore/T-xx-sujet` pour la configuration du dépôt
3. Faire des commits petits et cohérents, avec un message à l'infinitif qui décrit le changement (ex. « Refuser les dates impossibles comme 2026-02-30 »).
4. Ouvrir une pull request liée à l'Issue (`Closes #numéro`), remplir la description et demander la revue d'un autre membre. La carte passe dans « À vérifier ».
5. Le relecteur lit le diff, compile et essaie le changement, puis commente.
6. L'auteur corrige par de nouveaux commits, répond à chaque commentaire et résout les conversations.
7. La pull request est fusionnée après au moins une approbation d'un autre membre.
8. L'Issue est fermée et la carte passe dans « Terminé » si la définition de terminé est respectée.

Aucun commit direct sur `main` et jamais de `git push --force` sur une branche partagée. Le dépôt étant privé, GitHub n'applique pas de règle de protection de branche : cette règle est une convention de l'équipe.

## Avant d'ouvrir une pull request

Depuis la racine du dépôt :

```
javac -encoding UTF-8 -d out taskflow/*.java
java -cp out taskflow.Main
```

Exécuter ensuite les scénarios concernés de [docs/verification.md](docs/verification.md).

## Revue de code

| Étiquette | Usage |
|---|---|
| problème (bloquant) | défaut à corriger avant la fusion |
| suggestion | amélioration proposée, discutable |
| question | demande d'explication |
| détail | point mineur, non bloquant |
| bravo | ce qui est bien fait |

Une approbation précise ce qui a été relu ou essayé. « OK » seul ne suffit pas.

## Données

Uniquement des données fictives. Ne jamais versionner `taches.txt`, le dossier `out/`, un mot de passe ou un secret.
