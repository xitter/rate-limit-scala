# Spray API Server Sample(with API rate limiting)

## How to run the service
Clone the repository:
```
> git clone https://github.com/xitter/spray-api-demo.git
```

Run the service:
```
> sbt run
```

Environment Variables - HOST, PORT (default 0.0.0.0:5000)

## Configure rate limits

Config file - src/main/resources/throttling.conf
A Global rate limit is defined. However, specific rate limits can also be configured for multiple API Keys. 

## Database

Application uses CSV as its data repository. CSV is located at src/main/resources/hoteldb.csv

## Sample Usage

### Search Hotel by City 

Query Params - city, sort{ASC/DESC}(optional)
Request:
```
curl -X GET -H "X-APIKey: Test-a" "http://localhost:5000/hotels/search?city=Ashburn"
```
