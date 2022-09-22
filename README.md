# Candles 
Web socket client app to create candles from market data
```
{"data":[{"p":70.08297527221663,"s":"BABA","t":1663783963681,"v":10}],"type":"trade"}
{"data":[{"p":65.68205744334107,"s":"ARKF","t":1663783963681,"v":10}],"type":"trade"}
```
Returns minute candles
```
[
{"time":"2022-09-21T20:31:48.504Z","open":91.85277986831485,"high":91.85277986831485,
"low":54.6257311747465,"close":84.41510591194498,"symbol":"AAPL"},
{"time":"2022-09-21T20:32:00.534Z","open":76.52960526481115,"high":92.63095755249394,
"low":51.476701540469534,"close":51.476701540469534,"symbol":"AAPL"}
]
```


- Build app
```
./gradlew bootBuildImage
```
- Run docker image
```
docker run -it -p9099:8080 candles:0.0.1-SNAPSHOT
```
- Send request to get candles
```
curl --location --request GET 'localhost:9099/candles?ticker=AA'
```
