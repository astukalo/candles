# Candles 
Web socket client app

- Build app
```
./gradlew bootBuildImage
```
- Run docker image
```
docker run -it -p9099:8888 candles:0.0.1-SNAPSHOT
```
- Send request to get candles
```
curl --location --request GET 'localhost:9099/candles?ticker=AA'
```
