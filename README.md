# Uputstvo za podešavanje

## Podešavanje PostgreSQL baze podataka

Kreiranje i pokretanje PostgreSQL 16.10 Docker kontejnera:

```bash
docker run --name postgres-db \
  -e POSTGRES_DB=fon_nastava \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=password123 \
  -p 5434:5432 \
  -d postgres:16.10
```

## Pokretanje aplikacije

### Korišćenje JAR fajla

```bash
export SPRING_DATASOURCE_PASSWORD=password123
java -jar target/your-app-name.jar
```

### Korišćenje Maven Spring Boot

```bash
export SPRING_DATASOURCE_PASSWORD=password123
mvn spring-boot:run
```

## Detalji konekcije na bazu podataka

- **Host:** localhost
- **Port:** 5434
- **Baza podataka:** fon_nastava
- **Korisničko ime:** admin
- **Lozinka:** password123 (postavlja se preko DB_PASSWORD environment promenljive)