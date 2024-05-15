# Despliegue de la aplicación en nube

**Objetivo:** Desplegar una aplicación web en Azure 

## Ajuste inicial del proyecto

Inicialmente, para evitar configurar una base de datos, vamos a utilizar [h2](https://www.h2database.com/html/main.html) una base de datos que funciona en memoria y no requiere ninguna instalación.

1. Agregar el soporte para H2 usando el comando `Spring Initializr: Add Starters` y seleccionando `H2`.

2. Configurar la conexión a H2 modificando el archivo `src/main/resources/application.properties`
    
    ```
    # colocar en comentario con la cadena de conexión anterior
    # spring.datasource.url=jdbc:mysql://localhost:3306/bicicletas

    # agregar la cadena de conexión a H2 en memoria
    spring.datasource.url=jdbc:h2:mem:bicicletas
    ```

3. Ejecutar la aplicación y hacer algunas pruebas para validar que funcione

    ```
    # si no está en la carpeta del proyecto
    cd trayectos

    mvn spring-boot:run
    ./pruebas.sh
    ```

## Ajuste inicial de la suscripción en Azure

1. Crear una [cuenta gratuita para estudiantes en Azure](https://azure.microsoft.com/es-es/free/students) 
2. Ingresar al [portal de administración de Azure](https://portal.azure.com/#home)
3. Crear un Grupo de Recursos
4. Crear un plan de App Services

## Crear un pipeline usando Github Actions

1. Crear el archivo con la definición del pipeline en `.github/workflows`

  ```
  # nombre del pipeline
  name: Compila y despliega aplicación en Azure

  # parámetros
  env:
    PROJECT_NAME: 'trayectos'           # colocar el nombre del proyecto java
    AZURE_WEBAPP_NAME: MY_WEBAPP_NAME   # colocar el nombre de la aplicación
    JAVA_VERSION: '21'                  # colocar la versión de java
    DISTRIBUTION: zulu                  # colocar la distribución de Java a utilizar

  # ejecutar cuando se haga push a la rama main o al hacer clic en la pantalla
  on:
    push:
      branches: 
        - main
    workflow_dispatch:

  # tareas
  jobs:

    # trabajo con el nombre compilar
    compilar:
      # se ejecuta en linux Ubuntu
      runs-on: ubuntu-latest

      # pasos
      steps:

        - name: Descarga el código fuente
          uses: actions/checkout@v4

        - name: Configura la versión de Java
          uses: actions/setup-java@v4
          with:
            java-version: ${{ env.JAVA_VERSION }}
            distribution: ${{ env.DISTRIBUTION }}
            cache: 'maven'

        - name: Compila usando Maven
          run: mvn clean install

        - name: Carga el artifacto para la tarea de despliegue
          uses: actions/upload-artifact@v4
          with:
            name: java-app
            path: '${{ github.workspace }}/${{ env.PROJECT_NAME }}/target/*.jar'

    # tarea de despliegue
    desplegar:
      # se ejecuta en linux Ubuntu
      runs-on: ubuntu-latest
      # depende de la tarea compilar
      needs: compilar
    
      # modifica el entorno de producción
      environment:
        name: 'production'
        url: ${{ steps.deploy-to-webapp.outputs.webapp-url }}

      steps:
        - name: Descarga el artifacto de la tarea compilar
          uses: actions/download-artifact@v4
          with:
            name: java-app

        - name: Despliega en Azure App Services
          id: deploy-to-webapp
          uses: azure/webapps-deploy@85270a1854658d167ab239bce43949edb336fa7c
          with:
            app-name: ${{ env.AZURE_WEBAPP_NAME }}
            publish-profile: ${{ secrets.AZURE_WEBAPP_PUBLISH_PROFILE }}
            package: '*.jar'

    ```


## Información adicional

- [Documentación en Github Actions](https://docs.github.com/en/actions/deployment/deploying-to-your-cloud-provider/deploying-to-azure/deploying-java-to-azure-app-service)
  - [Pipeline](https://github.com/actions/starter-workflows/blob/main/deployments/azure-webapps-java-jar.yml) por defecto que provee Github Actions
- [Documentación en Microsoft Learn](https://learn.microsoft.com/es-es/azure/app-service/deploy-github-actions?tabs=applevel%2Cjava)
  

- https://learn.microsoft.com/en-us/azure/app-service/quickstart-java?tabs=springboot&pivots=java-maven-javase