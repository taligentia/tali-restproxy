# RestProxy

Dans .secrets
- settings-read.xml
- tali-maven-read.txt
- .env


Fichier .secrets/.env

```
USER=...
PASSWD=...
```

## Build
```
$ docker build -t restproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` --build-arg READPASSWORD=`cat .secrets/tali-maven-read.txt` .
$ docker tag restproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` restproxy:latest
```

## Run / Stop
```
$ docker run --env-file=.secrets/.env -p 9090:9990 --add-host="win2016-sp.taliwin.com:10.168.0.91" --add-host="win2016-dc.taliwin.com:10.168.0.90" --rm -d --name restproxy restproxy:latest  
$ docker stop restproxy
```

## Sharepoint API test url
```
http://win2016-sp.taliwin.com/sites/kamare/_api/web/title
http://win2016-sp.taliwin.com/sites/kamare/_api/web/lists
http://win2016-sp.taliwin.com/sites/kamare/_api/Web/Lists%28guid%2748d3bd48-4aa1-4a9b-9e19-3c448d1c2351%27%29
```

