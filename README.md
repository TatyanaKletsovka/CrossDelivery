# Cross-Delivery

## Get application from git

1.Open Git Bash

2.Clone application

> git clone git@git.syberry.com:t.kletsovka/aqua-playground.git

## Run application with maven
1.Go to application folder

> cd aqua-playground

1.1.If necessary put in `src/main/resources/application.yaml` your `DB_USER` and `DB_PASS`

2.Build application

> mvn package

3.Run application

> java -jar target/cross-delivery-0.0.1-SNAPSHOT.jar
