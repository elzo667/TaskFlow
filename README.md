# TaskFlow — Suivi du travail

Les cartes représentent des récits utilisateurs (US-xx) et des tâches techniques (T-xx), tous créés comme Issues du dépôt.

## Règles de passage
- À faire → En cours : le membre s'assigne la carte et crée sa branche.
- En cours → À vérifier : la pull request est ouverte et liée à l'Issue.
- À vérifier → Terminé : un autre membre a relu et approuvé la PR, elle est fusionnée dans main, la définition de terminé est respectée.
- Un élément refusé en revue retourne à En cours.

## Définition de terminé
- Les critères d'acceptation sont satisfaits.
- Les scénarios de vérification pertinents sont exécutés et consignés.
- Le code est fusionné dans main par pull request.
- Un autre membre a relu et commenté la PR en précisant ce qui a été relu ou essayé.
- README et documentation sont mis à jour si nécessaire.
- L'Issue est fermée et la carte est dans Terminé.

## Blocages
Un élément bloqué reste dans sa colonne avec le label "bloqué" et un commentaire expliquant la cause.
