/*
 * Copyright (c) 2019 Christopher Bartlett
 * [This program is licensed under the "MIT License"]
 * Please see the file LICENSE in the source
 * distribution of this software for license terms.
 */

lazy val root = (project in file ("""."""))

  .aggregate(baseCard, deck)

  .settings ()

lazy val baseCard = (project in file ("""Card"""))

  .settings(
    name := "BaseCard",
    libraryDependencies += "com.eed3si9n" %% "gigahorse-okhttp" % "0.3.1",
  )

lazy val deck = (project in file ("""Game"""))
  .settings (
    name := "Deck"
  )