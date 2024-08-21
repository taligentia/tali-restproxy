# SharepointRestProxy

Image pour Proxy http d'accès à des WS sécurisés par Kerberos (Sharepoint mais pas uniquement).
Le proxy est sécurisé par une autorisation de type bearer token et nécessite de fournir un clientId et un clientSecret.

Dans .secrets

| Fichier | Description |
| ----------- | ----------- |
| .env | Passé en paramètre de docker si container lancé avec "docker run" passé en paramètre de docker si container lancé avec "docker run" |


Fichier .secrets/.env 
```
# Json response Dump dir (pour debugging)
DUMP_DIRECTORY=/dump

# LoginName: "i:0#.w|taliwin\\kamare"
PROXY_KEYTAB_FILE=kamare.keytab
PROXY_PRINCIPAL=HTTP/kamare.taliwin.com@TALIWIN.COM

# LoginName: "SHAREPOINT\\system"
#PROXY_KEYTAB_FILE=sharepoint.keytab
#PROXY_PRINCIPAL=HTTP/win2016-sp@TALIWIN.COM
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

#### Loader l'image docker en local sous podman
```
$ docker save sharepointrestproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` > sharepointrestproxy.img && podman load -i sharepointrestproxy.img && rm sharepointrestproxy.img
$ podman tag sharepointrestproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` docker.pkg.github.com/taligentia/kamare/sharepointrestproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1`
```

## Run / Logs / Stop / Exec

:exclamation: Pour le développement en local, lancer le VPN avant de de démarrer le container !

```
$ mkdir dump && chmod go+rw dump

$ podman run --env-file=.secrets/.env -v "$PWD/dump:/dump" -p 9094:9990 -v /etc/timezone:/etc/timezone:ro -v /etc/localtime:/etc/localtime:ro -v "$(pwd)"/docker/certs:/app/certs:ro -v "$(pwd)"/docker/authorizations.yml:/app/authorizations.yml:ro -v "$(pwd)"/docker/krb5.conf:/etc/krb5.conf:ro -v "$(pwd)"/docker/kamare.keytab:/app/kamare.keytab:ro --add-host="win2016-sp.taliwin.com:10.168.0.91" --add-host="win2016-dc.taliwin.com:10.168.0.90" --rm -d --name sharepointrestproxy sharepointrestproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1`

$ podman logs sharepointrestproxy

$ podman stop sharepointrestproxy

$ podman exec -it sharepointrestproxy /bin/bash
```

## Configuration par variables d'environnement

* Les fichiers de configuration placés dans /app/templates sont copiés au démarrage dans /app après substitution des variables d'environnement
* Il est possible d'écraser des fichiers dans /app/templates au démarrage par montage de volume 

La syntaxe :

| Expression         | Meaning                                                              |
|--------------------|----------------------------------------------------------------------|
| `${var}`           | Value of var (same as `$var`)                                        
| `${var-$DEFAULT}`  | If var not set, evaluate expression as $DEFAULT                      
| `${var:-$DEFAULT}` | If var not set or is empty, evaluate expression as $DEFAULT          
| `${var=$DEFAULT}`  | If var not set, evaluate expression as $DEFAULT                      
| `${var:=$DEFAULT}` | If var not set or is empty, evaluate expression as $DEFAULT          
| `${var+$OTHER}`    | If var set, evaluate expression as $OTHER, otherwise as empty string 
| `${var:+$OTHER}`   | If var set, evaluate expression as $OTHER, otherwise as empty string 
| `$$var`            | Escape expressions. Result will be `$var`.                           


## Sharepoint API test url
```
http://win2016-sp.taliwin.com/sites/kamare/_api/web/title
http://win2016-sp.taliwin.com/sites/kamare/_api/web/lists
http://win2016-sp.taliwin.com/sites/kamare/_api/Web/Lists%28guid%2748d3bd48-4aa1-4a9b-9e19-3c448d1c2351%27%29
```

