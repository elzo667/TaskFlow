# TaskFlow — Organisation de l'équipe

## Objectif du produit

TaskFlow est un outil interne pour l'équipe de soutien d'une entreprise de services informatiques. Il remplace les notes personnelles par une liste de tâches commune : chaque tâche a une priorité, une échéance facultative et un statut, et les données sont conservées entre deux utilisations.

Cette première version (mini-projet 1) est une application Java en ligne de commande. Elle servira de base aux parties suivantes du cours.

## Équipe

| Membre | GitHub | Rôle |
|---|---|---|
| Elhadji N Diagne | [@elzo667](https://github.com/elzo667) | Coordinateur, développement, propriétaire du dépôt |
| Laeticia Hammi | [@Laeticiahammi](https://github.com/Laeticiahammi) | Développement, vérifications, documentation |
| Jael Mulamba Ilunga | [@JJael](https://github.com/JJael) | Organisation du dépôt et du suivi |

L'équipe compte 3 membres (toutes les équipes étaient complètes).

### Règles de fonctionnement

- **Communication** : Microsoft Teams pour les échanges courants, Outlook pour les messages plus formels (dont ceux à l'enseignant).
- **Coordination** : le coordinateur répartit le travail et fusionne les pull requests. Ce rôle ne remplace pas sa participation au code et aux vérifications.
- **Décisions** : les choix qui touchent le produit, le backlog ou la façon de travailler sont discutés sur Teams, puis notés dans l'Issue concernée ou dans ce document (section « Ajustements »).
- **Blocages** : voir la section « Blocages » plus bas.
- **Vérification et intégration** : tout changement passe par une branche et une pull request liée à une Issue, relue par un autre membre avant la fusion (voir la définition de terminé).

## Backlog

Le backlog est tenu dans les [Issues GitHub](https://github.com/elzo667/TaskFlow/issues?q=is%3Aissue). Chaque Issue contient le récit, les critères d'acceptation, la priorité, l'estimation et l'itération. Les labels distinguent les récits utilisateurs (`user-story`), les tâches techniques (`technique`) et les anomalies (`bug`).

### Récits utilisateurs

| Récit | Issue | Priorité | Estimation | Itération |
|---|---|---|---|---|
| US-01 Ajouter une tâche avec un titre | [#2](https://github.com/elzo667/TaskFlow/issues/2) | 1 | 3 | 1 |
| US-02 Choisir la priorité à l'ajout | [#3](https://github.com/elzo667/TaskFlow/issues/3) | 2 | 2 | 1 |
| US-03 Préciser une échéance à l'ajout | [#4](https://github.com/elzo667/TaskFlow/issues/4) | 3 | 3 | 1 |
| US-16 Naviguer par menu et quitter proprement | [#17](https://github.com/elzo667/TaskFlow/issues/17) | 3 bis | 2 | 1 |
| US-04 Afficher la liste des tâches | [#5](https://github.com/elzo667/TaskFlow/issues/5) | 4 | 2 | 1 |
| US-05 Marquer une tâche comme terminée | [#6](https://github.com/elzo667/TaskFlow/issues/6) | 5 | 2 | 1 |
| US-06 Modifier la priorité d'une tâche | [#7](https://github.com/elzo667/TaskFlow/issues/7) | 6 | 2 | 2 |
| US-07 Attribuer une échéance à une tâche existante | [#8](https://github.com/elzo667/TaskFlow/issues/8) | 7 | 2 | 2 |
| US-08 Modifier l'échéance d'une tâche | [#9](https://github.com/elzo667/TaskFlow/issues/9) | 8 | 1 | 2 |
| US-09 Retirer l'échéance d'une tâche | [#10](https://github.com/elzo667/TaskFlow/issues/10) | 9 | 1 | 2 |
| US-10 Sauvegarder les données | [#11](https://github.com/elzo667/TaskFlow/issues/11) | 10 | 3 | 2 |
| US-11 Recharger automatiquement les données au démarrage | [#12](https://github.com/elzo667/TaskFlow/issues/12) | 11 | 3 | 2 |
| US-12 Identifiants distincts après rechargement | [#13](https://github.com/elzo667/TaskFlow/issues/13) | 12 | 2 | 2 |
| US-13 Démarrer sans fichier de données | [#14](https://github.com/elzo667/TaskFlow/issues/14) | 13 | 1 | 2 |
| US-14 Être averti d'un fichier illisible ou mal formé | [#15](https://github.com/elzo667/TaskFlow/issues/15) | 14 | 3 | 2 |
| US-15 Être averti d'un échec de sauvegarde | [#16](https://github.com/elzo667/TaskFlow/issues/16) | 15 | 2 | 2 |

### Tâches techniques et anomalies

| Élément | Issue | Estimation |
|---|---|---|
| T-01 Initialiser le dépôt, .gitignore et accès (prof + correcteur) | [#18](https://github.com/elzo667/TaskFlow/issues/18) | 1 |
| T-02 Structure du code : console / gestion des tâches / fichier | [#19](https://github.com/elzo667/TaskFlow/issues/19) | 2 |
| T-03 Rédiger README.md | [#20](https://github.com/elzo667/TaskFlow/issues/20) | 2 |
| T-04 Créer le GitHub Project et les règles de passage | [#21](https://github.com/elzo667/TaskFlow/issues/21) | 1 |
| T-05 Rédiger la définition de terminé | [#22](https://github.com/elzo667/TaskFlow/issues/22) | 1 |
| T-06 Tableau de vérification manuelle | [#23](https://github.com/elzo667/TaskFlow/issues/23) | 3 |
| T-07 Rédiger docs/organisation.md | [#24](https://github.com/elzo667/TaskFlow/issues/24) | 2 |
| Anomalie : priorités 1, 2, 3 refusées | [#27](https://github.com/elzo667/TaskFlow/issues/27) | — |
| Anomalie : titre vide non corrigeable, boucle en fin d'entrée | [#29](https://github.com/elzo667/TaskFlow/issues/29) | — |

Aucune fonctionnalité hors énoncé n'a été ajoutée.

### Choix de priorité

Tous les récits sont obligatoires (« Must ») : l'ordre sert donc à décider ce qui est réalisé en premier. Nous avons suivi le parcours d'un utilisateur :

1. **Créer et voir des tâches d'abord** (US-01 à US-05, US-16) : sans ajout ni affichage, aucune autre fonctionnalité ne peut être essayée. Le menu (US-16) a été remonté en priorité « 3 bis » parce qu'il est nécessaire pour utiliser l'application dès la première itération.
2. **Modifier les tâches ensuite** (US-06 à US-09) : ces récits supposent des tâches existantes.
3. **Persistance en dernier** (US-10 à US-15) : la sauvegarde dépend du modèle de données, qui devait être stable avant de fixer le format du fichier. Les cas d'erreur (fichier absent, mal formé, écriture impossible) suivent la sauvegarde et le rechargement de base.

### Estimations

Les estimations sont relatives, en points (1 = petit changement localisé, 2 = changement simple avec quelques cas à valider, 3 = plusieurs cas d'erreur ou une nouvelle partie du code). Elles sont inscrites dans chaque Issue. Hypothèses principales :

- la validation des dates (format strict, dates impossibles) est faite une fois avec `java.time` et réutilisée, d'où des estimations faibles pour US-08 et US-09 ;
- le format du fichier est un texte simple séparé par des tabulations, ce qui limite l'effort de US-10 et US-11 ;
- US-14 est estimé à 3 parce qu'il faut valider chaque champ de chaque ligne au chargement.

Total : 34 points de récits (14 en itération 1, 20 en itération 2).

## Suivi du travail

Le travail est suivi dans le tableau GitHub Projects [TaskFlow - Suivi MP1](https://github.com/users/JJael/projects/1). Il contient les colonnes À faire, En cours, À vérifier et Terminé. Chaque carte porte un champ Catégorie (Récit, Technique ou Anomalie) et un champ Itération.

Les cartes représentent des récits utilisateurs (US-xx) et des tâches techniques (T-xx), tous créés comme Issues du dépôt.

### Règles de passage
- À faire → En cours : le membre s'assigne la carte et crée sa branche.
- En cours → À vérifier : la pull request est ouverte et liée à l'Issue.
- À vérifier → Terminé : un autre membre a relu et approuvé la PR, elle est fusionnée dans main, la définition de terminé est respectée.
- Un élément refusé en revue retourne à En cours.

### Définition de terminé
Un élément (récit, tâche technique ou anomalie) passe dans « Terminé » seulement si toutes ces conditions sont vraies :

1. **Critères d'acceptation** : chaque critère de l'Issue est satisfait et coché dans la PR.
2. **Vérifications** : le code compile avec la commande du README, et les scénarios concernés de verification.md ont été exécutés et consignés. Pour une tâche de documentation ou de configuration : rendu vérifié sur GitHub.
3. **Revue** : un autre membre a approuvé la PR en précisant ce qu'il a relu ou essayé, et les conversations sont résolues.
4. **Intégration** : la PR est fusionnée dans main, et main compile et démarre encore après la fusion.
5. **Documentation** : README, organisation.md ou verification.md sont à jour si une commande, un comportement, le format du fichier ou l'organisation change.
6. **Propreté** : pas de code de débogage, de code commenté inutile, de fichier généré ni de donnée réelle.
7. **Suivi** : l'Issue est fermée et sa carte est dans « Terminé ».

Ces conditions sont reprises sous forme de cases à cocher dans le modèle de pull request (`.github/pull_request_template.md`).

### Blocages
Un élément bloqué reste dans sa colonne avec le label "bloqué" et un commentaire expliquant la cause.

## Itérations

### Itération 1 — Ajouter, afficher et terminer des tâches

- **Objectif** : une première version utilisable où l'on peut ajouter des tâches, les afficher et les marquer comme terminées.
- **Récits** : US-01, US-02, US-03, US-04, US-05, US-16 (14 points), et la tâche technique T-02.
- **Principales tâches** : structure du code en trois responsabilités (`ConsoleTaskFlow` pour l'interaction, `GestionTaches` pour la logique, `Tache`, `Priorite` et `Statut` pour le modèle), menu, validation du titre, de la priorité et de la date.
- **Version obtenue** : [PR #25](https://github.com/elzo667/TaskFlow/pull/25), fusionnée le 2026-10-16. Elle contient aussi la modification de la priorité et la gestion de l'échéance (US-06 à US-09), prévues pour l'itération 2.
- **Vérifications** : essais manuels par l'auteur, non consignés séparément. Les comportements correspondants sont couverts par les scénarios 1 à 8 de [verification.md](verification.md).
- **Observations** : la PR a été fusionnée sans revue par un autre membre. L'option de sauvegarde était présente dans le menu mais affichait « pas encore disponible ». La PR regroupe plus de récits que prévu, ce qui la rend difficile à relire.

### Itération 2 — Modifier les tâches et conserver les données

- **Objectif** : rendre l'application utilisable d'une session à l'autre et permettre de modifier priorité et échéance.
- **Récits** : US-06 à US-15 (20 points), et les tâches techniques T-03, T-06, T-07.
- **Principales tâches** : classe `StockageTaches` (format `TASKFLOW-V1`, validation au chargement), messages d'erreur au démarrage et à la sauvegarde, README, tableau de vérification.
- **Version obtenue** : [PR #26](https://github.com/elzo667/TaskFlow/pull/26) (sauvegarde, rechargement et cas d'erreur, US-10 à US-15, 2026-10-16 ; US-06 à US-09 étaient déjà livrés avec la PR #25), puis les corrections [#28](https://github.com/elzo667/TaskFlow/pull/28) et [#30](https://github.com/elzo667/TaskFlow/pull/30) et la documentation [#31](https://github.com/elzo667/TaskFlow/pull/31) et [#32](https://github.com/elzo667/TaskFlow/pull/32) (2026-10-19 et 2026-10-20).
- **Vérifications** : les 12 scénarios de l'énoncé, consignés dans [verification.md](verification.md), tous conformes sur la version finale.
- **Observations** : en essayant la version de la PR #26, deux anomalies ont été trouvées (#27, #29) et corrigées avant la remise. Les PR ont encore été fusionnées sans revue formelle.

## Ajustements

| Constat | Décision |
|---|---|
| Les deux itérations ont été réalisées par un seul membre et fusionnées sans revue, ce qui ne respecte pas notre définition de terminé. | Répartition des éléments restants entre les trois membres. Une PR n'est fusionnée qu'après l'approbation d'un autre membre qui indique ce qu'il a relu ou essayé. |
| L'essai de la version de l'itération 2 a révélé que l'invite proposait les priorités 1, 2, 3 sans les accepter, et qu'une fin d'entrée provoquait une boucle infinie. | Chaque anomalie est ouverte comme Issue avec les étapes pour la reproduire, puis corrigée dans sa propre branche et sa propre PR. |
| Les PR #25 et #26 regroupent chacune plusieurs récits (jusqu'à neuf), et US-06 à US-09 ont été livrés plus tôt que prévu sans mise à jour des labels d'itération. | Une PR par récit ou par anomalie, comme pour #28 et #30, pour faciliter la revue. Les labels d'itération sont gardés tels que planifiés, et l'écart est noté ici. |
| Le README contenait les règles du tableau de suivi au lieu des informations pour compiler et exécuter. | Le README est réécrit selon la section 9.2 de l'énoncé. Les règles de suivi sont déplacées dans ce document. |
| Pendant les vérifications, une échéance passée est acceptée sans avertissement. | Le comportement demandé par l'énoncé est conservé. Une confirmation à la saisie et une mention « en retard » sont proposées pour une prochaine version (voir [verification.md](verification.md)). |
| Aucun tableau GitHub Projects n'avait été créé. | Tableau créé le 2026-10-27 (T-04, [#21](https://github.com/elzo667/TaskFlow/issues/21)), avec les 25 Issues placées selon leur état réel. |

## Contributions

| Membre | Réalisé | Issues | Pull requests | Revues |
|---|---|---|---|---|
| Elhadji N Diagne | Création du backlog (US-01 à US-16, T-01 à T-07). Structure du code et récits US-01 à US-16. Coordination et fusion des PR. | [#2](https://github.com/elzo667/TaskFlow/issues/2) à [#24](https://github.com/elzo667/TaskFlow/issues/24) | [#25](https://github.com/elzo667/TaskFlow/pull/25), [#26](https://github.com/elzo667/TaskFlow/pull/26) | Aucune revue formelle. A fusionné [#28](https://github.com/elzo667/TaskFlow/pull/28) et [#30](https://github.com/elzo667/TaskFlow/pull/30) |
| Laeticia Hammi | Revue de la PR #26 et signalement de deux anomalies. Correction des priorités 1, 2, 3. Correction de la saisie du titre et de la fin d'entrée. README, tableau de vérification (12 scénarios et données de test), ce document. | [#27](https://github.com/elzo667/TaskFlow/issues/27), [#29](https://github.com/elzo667/TaskFlow/issues/29), [#20](https://github.com/elzo667/TaskFlow/issues/20), [#23](https://github.com/elzo667/TaskFlow/issues/23), [#24](https://github.com/elzo667/TaskFlow/issues/24) | [#28](https://github.com/elzo667/TaskFlow/pull/28), [#30](https://github.com/elzo667/TaskFlow/pull/30), [#31](https://github.com/elzo667/TaskFlow/pull/31), [#32](https://github.com/elzo667/TaskFlow/pull/32) | Commentaire de revue sur [#26](https://github.com/elzo667/TaskFlow/pull/26) |
| Jael Mulamba Ilunga | Règles de contribution et .gitignore (T-01). Tableau GitHub Projects avec ses colonnes et ses champs (T-04). Définition de terminé précisée et modèle de pull request (T-05). | [#18](https://github.com/elzo667/TaskFlow/issues/18), [#21](https://github.com/elzo667/TaskFlow/issues/21), [#22](https://github.com/elzo667/TaskFlow/issues/22) | [#34](https://github.com/elzo667/TaskFlow/pull/34), [#35](https://github.com/elzo667/TaskFlow/pull/35) et la PR de T-04 | |
