<!--
  ~ Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
  ~ 
  ~ SPDX-FileContributor: Mathieu SABARTHES
  ~ 
  ~ SPDX-License-Identifier: Apache-2.0
-->
# Contribution

[🇫🇷 Français](CONTRIBUTING.fr.md) | [🇺🇸 English](CONTRIBUTING.md)

## Sommaire

* [Comment contribuer à la documentation](#doc)
* [Comment faire une Pull Request](#pr)
* [Conventions de code](#code)
* [Conventions de test](#test)
* [Conventions de branche](#branch)
* [Validation des modifications](#commit)
* [Gestion des dépendances](#dep)
* [Processus de build](#build)
* [Gestion des releases](#release)
* [Publication](#releasing)
* [Licences](#oss)


## <a name="doc"></a> Comment contribuer à la documentation

Pour contribuer à cette documentation (README, CONTRIBUTING, etc.), nous nous conformons à la [spécification CommonMark](http://spec.commonmark.org/0.27/)

* [https://www.makeareadme.com/#suggestions-for-a-good-readme](https://www.makeareadme.com/#suggestions-for-a-good-readme)
* [https://help.github.com/en/articles/setting-guidelines-for-repository-contributors](https://help.github.com/en/articles/setting-guidelines-for-repository-contributors)


## <a name="pr"></a> Comment faire une Pull Request

1. Copier le dépôt et maintenez une synchronisation active avec notre dépôt
2. Créez vos branches de travail en respectant les [branches conventionnelles](https://conventional-branch.github.io/).
    * **ATTENTION** - Ne modifiez pas la branche main ni aucune de nos branches car cela casserait la synchronisation automatique
3. Quand vous avez terminé, récupérez tout et rebasez votre branche sur notre main ou toute autre de nos branches
    * ex. sur votre branche, faites :
        * `git fetch --all --prune`
        * `git rebase --no-ff origin/main`
4. Testez vos modifications et assurez-vous que tout fonctionne
5. Soumettez votre Pull Request vers la branche dev (la branche main n'est autorisée que pour les propriétaires du projet)
    * N'oubliez pas d'ajouter des relecteurs ! Vérifiez les derniers auteurs du code que vous avez modifié et ajoutez-les.
    * En cas de doute, voici les contributeurs actifs :
        * Jehan BOUSCH


## <a name="code"></a> Conventions de code

### Bonnes pratiques

En règle générale, vous devez suivre le [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

### Formatage

Tous les fichiers source Go doivent se conformer à la [Section Formatting du Style Guide](https://google.github.io/styleguide/javaguide.html#s4-formatting)

### Javadoc

Tous les fichiers source Go doivent utiliser le [Javadoc section of Style Guide](https://google.github.io/styleguide/javaguide.html#s7-javadoc).

## <a name="test"></a> Conventions de test

Pour exécuter tous les tests du projet, utilisez la commande suivante :

  ```
   mvn test
   ```

### Nommage

* Les tests sont situés dans `src/test`
* Quand vous écrivez un test, le nom de fichier doit se terminer par **Test.java**.
* Toutes les fonctions de test doivent commencer par le préfixe `test_`

### Framework de test

* Utilisez le framwork de test `org.junit`.

### Simulations

* Les interfaces Mock doivent avoir le même nom que l'original avec Mock à la fin.

Exemple: `TICCoreStream` --> `TICCoreStreamMock`

## <a name="branch"></a> Conventions de branche

Nous nous conformons aux [branches conventionnelles](https://conventional-branch.github.io/).

## <a name="commit"></a> Validation des modifications

Nous nous conformons aux [commits conventionnels](https://www.conventionalcommits.org/fr/v1.0.0/).

## <a name="dep"></a> Gestion des dépendances

Les dépendances du projet sont listées dans le fichier go.mod. Le fichier go.sum, quant à lui, contient les sommes de contrôle cryptographiques du contenu de versions spécifiques de modules, incluant à la fois les dépendances directes et indirectes.

Pour voir les modules réellement "utilisés" par l'application, utilisez la commande suivante :
   ```
   mvn dependency:list
   ```

Pour une documentation plus détaillée, veuillez vous référer à la [Apache Maven Dependency Plugin](https://maven.apache.org/plugins/maven-dependency-plugin/list-mojo.html).

## <a name="build"></a> Processus de build

Pour compiler l'application TIC2WebSocket dans le répertoire du projet, utilisez la commande suivante :

   ```
   mvn clean package
   ```

## <a name="release"></a> Gestion des releases

La gestion des releases se fait exclusivement sur GitHub

## <a name="releasing"></a> Publication

Les releases de TIC2WebSocket ne sont disponibles que sur GitHub.

### Mise à jour du fichier Changelog

Mettez toujours à jour le changelog avant de créer une release. Cela garantit que les changements sont documentés pour la release en cours de création.
Référez-vous à cette page pour des conseils sur les notes de release générées automatiquement : [Notes de release générées automatiquement](https://docs.github.com/en/repositories/releasing-projects-on-github/automatically-generated-release-notes)

N'hésitez pas à mettre à jour les notes de release générées, en particulier les titres des pull requests :)
Utilisez-les pour mettre à jour [CHANGELOG.md](https://github.com/Enedis-OSS/TIC2WebSocket/blob/main/CHANGELOG.md)


### Publication de version uniquement sur GitHub

#### Informations générales

- Les releases sur GitHub ne mettent à jour que le dernier chiffre de la version (ex., `2.7.1.1` ou `2.9.4.2`).
- La version snapshot suivante reste inchangée.
- Le fichier `CHANGELOG.md` est committé dans le tag.
- Les pull requests ne sont pas requises pour les releases sur GitHub.

#### Étape par étape

```shell
git switch -c release/<RELEASE_VERSION>
git add CHANGELOG.md (voir la section Mise à jour du fichier Changelog)
git add .
git diff --staged
git commit -m "chore: Release <RELEASE_VERSION>"
```

- Ensuite tagger et pousser.
```shell
git tag <TAG_VERSION>
git push origin <TAG_VERSION>
```

- Mettre à jour les notes de release sur [github](https://github.com/Enedis-OSS/TIC2WebSocket/releases)


## <a name="oss"></a> Licences

Nous avons choisi d'appliquer la licence Apache 2.0 (ALv2) : [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Comme pour tout projet, des problèmes de compatibilité de licences peuvent survenir et doivent être pris en compte.

Des instructions concrètes et des outils pour maintenir TIC2WebSocket conforme à ALv2 et limiter les problèmes de licence sont détaillés ci-dessous.

Cependant, nous reconnaissons la complexité du sujet, des erreurs peuvent être commises et nous pourrions ne pas avoir 100% raison.

Néanmoins, nous nous efforçons d'être conformes et équitables, c'est-à-dire de faire de notre mieux en toute bonne foi.

À ce titre, nous accueillons favorablement tout conseil et demande de modification.


À tout contributeur, nous recommandons vivement une lecture approfondie et une recherche personnelle :
* [http://www.apache.org/licenses/](http://www.apache.org/licenses/)
* [http://www.apache.org/legal/](http://www.apache.org/legal/)
* [http://apache.org/legal/resolved.html](http://apache.org/legal/resolved.html)
* [http://www.apache.org/dev/apply-license.html](http://www.apache.org/dev/apply-license.html)
* [http://www.apache.org/legal/src-headers.html](http://www.apache.org/legal/src-headers.html)
* [http://www.apache.org/legal/release-policy.html](http://www.apache.org/legal/release-policy.html)
* [http://www.apache.org/dev/licensing-howto.html](http://www.apache.org/dev/licensing-howto.html)

* [Pourquoi la LGPL n'est pas autorisée](https://issues.apache.org/jira/browse/LEGAL-192)
* https://issues.apache.org/jira/projects/LEGAL/issues/

* Actualités générales : [https://opensource.com/tags/law](https://opensource.com/tags/law)

### Comment gérer la compatibilité des licences

Lors de l'ajout d'une nouvelle dépendance, **on doit vérifier sa licence ainsi que toutes les licences de ses dépendances transitives**.

La compatibilité de la licence ALv2 telle que définie par l'ASF peut être trouvée ici : [http://apache.org/legal/resolved.html](http://apache.org/legal/resolved.html)

3 catégories sont définies :
* [Catégorie A](https://www.apache.org/legal/resolved.html#category-a) : Contient toutes les licences compatibles.
* [Catégorie B](https://www.apache.org/legal/resolved.html#category-b) : Contient les licences compatibles sous certaines conditions.
* [Catégorie X](https://www.apache.org/legal/resolved.html#category-x) : Contient toutes les licences incompatibles qui doivent être évitées à tout prix.

__D'après notre compréhension :__

Si, par quelque moyen que ce soit, votre contribution devait s'appuyer sur une dépendance de Catégorie X, alors vous devez fournir un moyen de la structurer
et de rendre son utilisation optionnelle pour TIC2WebSocket, sous forme de plugin.

Vous pouvez distribuer votre plugin sous les termes de la licence de Catégorie X.

Toute distribution de TIC2WebSocket accompagnée de votre plugin sera probablement faite sous les termes de la licence de Catégorie X.

Mais _"vous pouvez fournir à l'utilisateur des instructions sur comment obtenir et installer le plugin non-inclus"_.

__Références :__
- [Optionnel](https://www.apache.org/legal/resolved.html#optional)
- [Prohibé](https://www.apache.org/legal/resolved.html#prohibited)

### Comment se conformer aux clauses de redistribution et d'attribution

De nombreuses licences imposent des conditions sur la redistribution et l'attribution, y compris ALv2.

__Références :__
* http://mail-archives.apache.org/mod_mbox/www-legal-discuss/201502.mbox/%3CCAAS6%3D7gzsAYZMT5mar_nfy9egXB1t3HendDQRMUpkA6dqvhr7w%40mail.gmail.com%3E
* http://mail-archives.apache.org/mod_mbox/www-legal-discuss/201501.mbox/%3CCAAS6%3D7jJoJMkzMRpSdJ6kAVSZCvSfC5aRD0eMyGzP_rzWyE73Q%40mail.gmail.com%3E

#### Fichier LICENSE
##### Dans la distribution source

Ce fichier contient :
* la licence ALv2 complète.
* la liste des dépendances et pointe vers leur fichier de licence respectif
    * Exemple :
      _Ce produit intègre SuperWidget 1.2.3, qui est disponible sous licence
      "3-clause BSD". Pour plus de détails, voir deps/superwidget/_
* ne pas lister les dépendances sous ALv2

#### Fichier NOTICE

##### Dans la distribution source

_Le fichier NOTICE n'est pas destiné à transmettre des informations aux consommateurs en aval -- il
est un moyen d'*obliger* les consommateurs en aval à *relayer* certains avis requis._

### Questions non résolues - AIDE RECHERCHÉE -
* Les dépendances de test doivent-elles être prises en compte pour la distribution source ?
    * Il semblerait que OUI
* Les dépendances de temps de build doivent-elles être prises en compte ?
    * Il semblerait que NON mais cela pourrait dépendre de ce que fait réellement cette dépendance