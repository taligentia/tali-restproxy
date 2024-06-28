# SharepointRestProxy

Image pour Proxy http d'accès à des WS sécurisés par Kerberos (Sharepoint mais pas uniquement).
Le proxy est sécurisé par une autorisation de type bearer token et nécessite de fournir un clientId et un clientSecret.

Dans .secrets

| Fichier | Description |
| ----------- | ----------- |
| settings-read.xml | Utiliser par Dockerfile pour buid |
| tali-maven-read.txt | Pour lire les jar via le repo maven taligentia. Initialise la variable d'environnement READPASSWORD pour le build |
| .env | Passé en paramètre de docker si container lancé avec "docker run" passé en paramètre de docker si container lancé avec "docker run" |


Fichier .secrets/.env 
```
# Authentification
USER=spadmin
PASSWD=RsC!15dK1PLu

# Json response Dump dir (pour debugging)
DUMP_DIRECTORY=/dump
```


## Build jar
```
$ mvn -e -B -Dmaven.test.skip=true package

```

## Build image
```
$ docker build --no-cache -t sharepointrestproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` --build-arg READPASSWORD=`cat .secrets/tali-maven-read.txt` .
```

#### Pousser l'image docker dans la registry github
```
$ cat .secrets/docker-deploy.txt | docker login https://docker.pkg.github.com --password-stdin -u taligentia
$ docker tag sharepointrestproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` docker.pkg.github.com/taligentia/kamare/sharepointrestproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1`
$ docker push docker.pkg.github.com/taligentia/kamare/sharepointrestproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1`
```

## Run / Logs / Stop / Exec

:exclamation: Pour le développement en local, lancer le VPN avant de de démarrer le container !

```
$ mkdir dump && chmod go+rw dump

$ docker run --env-file=.secrets/.env -v "$PWD/dump:/dump" -p 9094:9990 -v /etc/timezone:/etc/timezone:ro -v /etc/localtime:/etc/localtime:ro -v "$(pwd)"/docker/certs:/app/certs:ro -v "$(pwd)"/docker/auth.yml:/app/auth.yml:ro -v "$(pwd)"/docker/authorizations.yml:/app/authorizations.yml:ro -v "$(pwd)"/docker/krb5.conf:/etc/krb5.conf:ro -v "$(pwd)"/docker/login.conf:/app/login.conf:ro -v "$(pwd)"/docker/sharepoint.keytab:/app/sharepoint.keytab:ro --add-host="win2016-sp.taliwin.com:10.168.0.91" --add-host="win2016-dc.taliwin.com:10.168.0.90" --rm -d --name sharepointrestproxy sharepointrestproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1`

$ docker logs sharepointrestproxy

$ docker stop sharepointrestproxy

$ docker exec -i -t sharepointrestproxy /bin/bash

```

## Sharepoint API test url
```
http://win2016-sp.taliwin.com/sites/kamare/_api/web/title
http://win2016-sp.taliwin.com/sites/kamare/_api/web/lists
http://win2016-sp.taliwin.com/sites/kamare/_api/Web/Lists%28guid%2748d3bd48-4aa1-4a9b-9e19-3c448d1c2351%27%29
```

```
mvn -e -B -Dmaven.test.skip=true package

docker build -t restproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` --build-arg READPASSWORD=`cat .secrets/tali-maven-read.txt` .

docker tag restproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` restproxy:latest

docker image ls | grep restproxy

docker run --env-file=.secrets/.env -v "$PWD/dump:/dump" -p 9094:9990 --add-host="win2016-sp.taliwin.com:10.168.0.91" --add-host="win2016-dc.taliwin.com:10.168.0.90" --rm -d --name restproxy restproxy:latest

docker logs restproxy

docker stop restproxy
```

