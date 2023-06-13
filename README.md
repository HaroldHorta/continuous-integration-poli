# Integración continua, implementación docker compose - microservice Java

Creación de múltiples microservicios con Spring Boot e implementación y ejecución con docker compose.

## Tabla de contenido

* [Arquitectura](#Arquitectura)
* [Tecnologias](#Tecnologias)
* [Pre-requisitos](#Prerequisitos)
* [Jenkins](#Jenkins)
* [Push imagen docker hub con Jenkins](#PushImagen)

## Arquitectura

El proyecto esta construido en microservicios. Tenemos tres microservicios independientes (Usuario,
Sitio y Organización), que se conectan a la base de datos H2 y se comunican entre sí mediante RestTemplate. Usamos
Docker Compose para implementarlos y ejecutarlos en contenedores docker.

## Tecnologias

1. Java 8
2. Maven Dependency Management
3. Spring Boot in microservices development:

    + Spring Web
    + Spring Data JPA
    + Spring Devtools
    + Spring Actuator

4. H2 database
5. Docker

### Prerequisitos

Se necesitan las siguientes herramientas:

* Java JDK 1.8+
* Maven 3.0+
* Git client
* Docker Compose: To [install docker-compose](https://docs.docker.com/compose/install/)

### Desarrollo

Pasos a seguir:

**Paso 1.** Construcción de *microservicios* usando Spring Boot y comunicación entre ellos por RestTemplate:

+ [User microservice](user-service)
+ [Site microservice](site-service)
+ [Organization microservice](organization-service)

**Paso 2.** Crear el *Dockerfile* para cada servicio. El archivo Docker es una lista de comandos que queremos que el
motor de la ventana acoplable
ejecutar.

Vaya al directorio de cada proyecto Spring Boot y cree un Dockerfile:

+ User microservice [Dockerfile](user-service/Dockerfile)
+ Site microservice [Dockerfile](site-service/Dockerfile)
+ Organization microservice [Dockerfile](organization-service/Dockerfile)

Ejemplos Dockerfile

```bash
FROM openjdk:8-jdk-alpine
RUN addgroup -S devopsc && adduser -S javams -G devopsc
USER javams:devopsc
ENV JAVA_OPTS=""
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
 # use a volume is mor efficient and speed that filesystem
VOLUME /tmp
EXPOSE 7280
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
```

**Paso 3.** Crear [docker-compose.yml](docker-compose.yml) :

```bash
version: "1.0"
services:
  organization:
#    image: organization-service
    build: ./organization-service
    ports:
      - "8083:8083"
    networks:
      - organization-user
      - organization-site
    depends_on:
      - user
      - site

  site:
#    image: site-service
    build: ./site-service
    ports:
      - "8082:8082"
    networks:
      - site-user
      - organization-site
    depends_on:
      - user
 
  user:
#    image: user-service
    build: ./user-service
    ports:
      - "8081:8081"
    networks:
      - site-user
      - organization-user

networks:
  site-user:
  organization-user:
  organization-site:
```

**Paso 4.** Ejecutar y probar aplicaciones:

Ejecute docker-compose up y se iniciara y ejecutara todos sus servicios.

### Configuración

Para ejecutar este proyecto, instálelo localmente de la siguiente manera:

1. **Clonar**

   ```bash
   git clone https://github.com/HaroldHorta/continuous-integration-poli.git
   ```

2. **Cree un archivo JAR para cada servicio**

   Ejecutar comando maven - *clean install*, y se crea un archivo jar en el directorio de destino para cada servicio de
   la siguiente manera:

   ```bash
   cd directorio_microservicio
   maven clean install
   ```	
   + *directorio_microservicio*: es cada directorio del repositorio (user-service, site-service y organization-service).	

3. **Inicie el usuario, el sitio y la organización usando docker-compose**

El proyecto incluye un archivo [*docker-compose.yml*](docker-compose.yml) para que pueda ejecutar *docker-compose up*
para comenzar
Servicios completos, no necesita instalación.

   ```bash
   cd solution_directory
   docker-compose up -d
   ```

Puedes ver -

- Construcción de una imagen desde Dockerfile para cada servicio si no existe.
- Construcción de contenedores (usuario, sitio, organización) usando las imágenes.
- Inicio de los servicios (usuario, sitio, organización).

1. **Consulta las imágenes creadas**

   Use el siguiente comando para verificar las imágenes creadas:

   ```bash
   docker images
   ```

   ![created images](images/docker-images.png)

2. **consulte los containers creados**

   ```bash
   docker ps -a
   ```
   ![User, Site, Organization, and MySql are UP and RUNNING](images/docker-ps-a.png)

3. **revise logs**

   ```bash
   docker container logs CONTAINER_ID
   ```
   
### Jenkins
se utiliza la imagen de jenkins recomendada en docker hub, installando maven y gradle como gestor de depencias

 ```bash
   FROM jenkins/jenkins
USER root
#Define variables
ENV MAVEN_VERSION 3.9.2

#Update Base OS and install additional tools
RUN apt-get update && apt-get install -y wget
RUN  wget --no-verbose https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz -P /tmp/
RUN tar xzf /tmp/apache-maven-$MAVEN_VERSION-bin.tar.gz -C /opt/ 
RUN ln -s  /opt/apache-maven-$MAVEN_VERSION /opt/maven 
RUN ln -s /opt/maven/bin/mvn /usr/local/bin 
RUN rm /tmp/apache-maven-$MAVEN_VERSION-bin.tar.gz 

# gradle version
ENV GRADLE_VERSION 2.11

# Install gradle, cd to /var/jenkins_home
WORKDIR  /usr/bin

RUN  curl -sLO https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-all.zip && \
     unzip gradle-${GRADLE_VERSION}-all.zip && \
     ln -s gradle-${GRADLE_VERSION} gradle && \
     rm gradle-${GRADLE_VERSION}-all.zip

ENV  GRADLE_HOME /usr/bin/gradle
ENV  PATH $PATH:$GRADLE_HOME/bin
#Set up permissions
RUN chown jenkins:jenkins /opt/maven;
ENV MAVEN_HOME=/opt/mvn
USER jenkins
   ```

Se procede la configracion de contraseñas y usuarios

![jenkins-users.png](jenkins%2Fjenkins-users.png)

![config-instance.png](jenkins%2Fconfig-instance.png)

damos en crear jobs

![create-jobs.png](jenkins%2Fcreate-jobs.png)

Asignamos un nombre y seleccionamos "Crear proyecto de estilo libre" 
![type-pipeline.png](jenkins%2Ftype-pipeline.png)

Se debe configurar el origen del código fuente

![origen-repo.png](jenkins%2Forigen-repo.png)

Build Steps
Ejecutar tareas 'maven' de nivel superior

![ejecutar-tareas.png](jenkins%2Fejecutar-tareas.png)

se ejecuta la tarea
![construir-ahora.png](jenkins%2Fconstruir-ahora.png)

para revisar el proceso correctamente y se pode revisar logs en la opción console logs
![logs.png](jenkins%2Flogs.png)

### PushImagen
Para subir una imagen al repositorio de docker hub
instalar plugin 

![plugin.png](jenkins%2Fplugin.png)

![plugin2.png](jenkins%2Fplugin2.png)

![plugin0.png](jenkins%2Fplugin0.png)

![dockerbuild.png](jenkins%2Fdockerbuild.png)

En las configuraciones del jobs de jenkins

![Docker Build a.png](jenkins%2FDocker%20Build%20a.png)

![dockerPublishAdvance.png](jenkins%2FdockerPublishAdvance.png)

Validamos que en docker hub la imagen no existe
![dockerHUb.png](jenkins%2FdockerHUb.png)

Constrimos la imagen a partir del repositorio de github

![construirImagen.png](jenkins%2FconstruirImagen.png)

![succesConfig.png](jenkins%2FsuccesConfig.png)

Se valida la imagen creada
![validateDockerHub.png](jenkins%2FvalidateDockerHub.png)

### Running

Para acceder a los servicios utilice los siguientes endpoint

**Ejecute los microservicios de User/Site/Organization**

El microservicio de usuario comenzará en el puerto `8081`, por lo que podrá visitar el microservicio de usuario en
`http://localhost:8081`.

El microservicio del sitio comenzará en el puerto `8082`, por lo que podrá visitar el microservicio del sitio en
`http://localhost:8082`.

El microservicio de la organización comenzará en el puerto `8083`, por lo que podrá visitar el microservicio de la
organización en
`http://localhost:8083`.

+ Ver información sobre microservicios (Site, User and Organization)
    * http://localhost:8081/actuator/info
    * http://localhost:8082/actuator/info
    * http://localhost:8083/actuator/info

+ Comprobar el estado de cada microservicio
    * http://localhost:8081/actuator/health
    * http://localhost:8082/actuator/health
    * http://localhost:8083/actuator/health

+ Acceder a las API de los servicios:
    * http://localhost:8081/api/users
    * http://localhost:8081/api/sites
    * http://localhost:8081/api/organizations




## Ejemplos


### User APIs

* Podemos revisar las siguientes urls

http://localhost:8081/actuator/info

* Revisamos Health para el microservicio usuario
  http://localhost:8081/actuator/health

* Lista todos los usuarios

http://localhost:8081/api/users

* Buscar usuario por Id

http://localhost:8081/api/users/1

* Filtra usuarios por SiteId

http://localhost:8081/api/users/site/3

* Filtra usuarios por OrganizationId
http://localhost:8081/api/users/organization/1

### Sitio APIs

*  Info microservicio sitio

http://localhost:8082/actuator/info

* Revisar Health para microservicio sitio

http://localhost:8082/actuator/health

* Lista todos los sitios

http://localhost:8082/api/sites

* busca sitios por Id

http://localhost:8082/api/sites/1

* Buscar sitios por OrganizationId

http://localhost:8082/api/sites/organization/1

* Busca sitios con usuario by organizacion

http://localhost:8082/api/sites/organization/1/with-users

### Organizacion APIs

* Info microservicio Organizacion

http://localhost:8083/actuator/info'

* Health para microservicio organizacion

http://localhost:8083/actuator/health'

* Lista todas las organizaciones
* 
http://localhost:8083/api/organizations'

