# my-pet-shop-services

`mvn clean install`

# run Keycloak

`bin\kc.bat start-dev --debug 5050 --http-port 8180`

`bin\kc.bat start-dev --debug 5050 --http-port 8180 --spi-theme-static-max-age=-1 --spi-theme-cache-themes=false --spi-theme-cache-templates=false`

admin / 123

Login: http://localhost:8180/realms/petshoprealm/protocol/openid-connect/auth?response_type=code&client_id=pet-shop-services&scope=openid&redirect_uri=http://localhost:8080/