Devsu - Banking Api

1. Clonar el repositorio

git clone https://github.com/JersonVilla/banking-api.git

2. Levantar Backend (Spring Boot + MySQL):

cd banking-api -> 
docker-compose up --build

El backend quedará disponible en:

http://localhost:8080

3. Levantar Frontend (Angular + Nginx)

cd banking-front

docker build -t banking-front .

docker run -p 4200:80 banking-front

4. Este proceso:

Levanta un contenedor con Nginx

El frontend estará disponible en:

http://localhost:4200

