#This app gets data from https://countries.trevorblades.com/graphql using query: {continents(filter: {code: {nin: ["not_existed"]}}){code,name,countries{code,name}}}, stores data to data.json file, if you want loading to be executed just delete data.json

To launch with docker, you need docker installed

cd ~/takehome
./gradlew clean build
docker build -t takehome:0.0.1 .
docker run -p 8080:8080 -d takehome:0.0.1


#authorized user allowed 20 requests per second, not authorized 5 only
#if you want to be not authorized user: don't use this part : --header 'AuthToken: Authorized'
curl --location --request GET 'http://localhost:8080/neighbors?countries=PE' --header 'Content-Type: application/json' --header 'AuthToken: Authorized' --header 'Cookie: JSESSIONID=2590B045E5ACA9D1B4332A1525046D66'