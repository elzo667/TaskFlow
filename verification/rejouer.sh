#!/usr/bin/env bash
# Rejoue tous les scénarios de verification/scenarios/ et compare avec verification/references/.
#
# Usage :
#   ./verification/rejouer.sh              compare (aucune ligne FAIL attendue)
#   ./verification/rejouer.sh --generer    (re)génère les références (à faire UNE fois, avec la v1.0.0)
#
# Commande de lancement de TaskFlow (chemins ABSOLUS : le script change de dossier) :
#   TASKFLOW_CMD="java -cp /chemin/out taskflow.Main" ./verification/rejouer.sh
# Par défaut : le JAR target/taskflow-*.jar (après le passage à Maven).
set -u
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
JOPTS="-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstdin.encoding=UTF-8"

if [ -z "${TASKFLOW_CMD:-}" ]; then
  JAR="$(ls "$ROOT"/target/taskflow-*.jar 2>/dev/null | grep -v '^.*/original-' | head -1)"
  [ -z "$JAR" ] && { echo "Aucun JAR trouvé : lancez ./mvnw clean package ou définissez TASKFLOW_CMD."; exit 2; }
  TASKFLOW_CMD="java $JOPTS -jar $JAR"
fi

GEN=0; [ "${1:-}" = "--generer" ] && GEN=1
fail=0; total=0

for sc in "$ROOT"/verification/scenarios/*.txt; do
  nom="$(basename "$sc" .txt)"
  tmp="$(mktemp -d)"
  # État de départ connu : verification/donnees/<scenario>.<nom-du-fichier> -> <nom-du-fichier>
  for d in "$ROOT"/verification/donnees/"$nom".*; do
    [ -f "$d" ] && cp "$d" "$tmp/$(basename "$d" | sed "s/^$nom\.//")"
  done
  (
    cd "$tmp" || exit 1
    eval "$TASKFLOW_CMD" < "$sc" > sortie.txt 2>&1
    # On ajoute le fichier de données final : il prouve le format écrit sur le disque.
    if [ -f taches.txt ]; then { echo; echo "=== taches.txt ==="; cat taches.txt; } >> sortie.txt; fi
    tr -d "\r" < sortie.txt | grep -v "^Picked up JAVA_TOOL_OPTIONS" > normalisee.txt   # \r\n Windows -> \n ; bruit JVM ignoré
  )
  total=$((total+1))
  ref="$ROOT/verification/references/$nom.txt"
  if [ $GEN -eq 1 ]; then
    cp "$tmp/normalisee.txt" "$ref"; echo "référence générée : $nom"
  elif diff "$ref" "$tmp/normalisee.txt" > "$tmp/diff.txt" 2>&1; then
    echo "OK   $nom"
  else
    echo "FAIL $nom"; head -20 "$tmp/diff.txt"; fail=$((fail+1))
  fi
  rm -rf "$tmp"
done

[ $GEN -eq 1 ] && exit 0
echo "---"; echo "$((total-fail))/$total scénarios identiques aux références."
[ $fail -eq 0 ]
