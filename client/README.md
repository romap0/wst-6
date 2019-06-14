# Client

## Generate
```sh
rm -rf client/src/main/java/wst/generated
wsimport -p "wst.generated" -s client/src/main/java http://localhost:8080/app/ShopService?wsdl
```

## Run
```sh
java -jar client/target/client-1.0.jar
```
