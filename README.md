# GeoCachter Template

> A Spring Boot & React template project

## Run the Project Backend

```
mvnw clean compile
mvnw --projects backend spring-boot:run
```

Now you can direct your browser to http://localhost:8089

### Building the executable jar file

```
mvnw clean package
```

The jar file can be found in backend/target. You can run it with:

```
java -jar backend/target/backend-0.0.1-SNAPSHOT.jar
```

## Start development mode for Project Frontend

In the frontend directory:

```
npm install
npm run dev
```
Now you can direct your browser to http://localhost:3000

### Start production mode

In the frontend directory:

```
npm install
npm run build
npm run start
```