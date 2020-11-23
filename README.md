# ApiUsuario

Prueba técnica 

Pre-requisitos:

    -   Gradle: https://gradle.org/
    -   jdk8

1. Compilar el proyecto:

    - gradle build

2. Ejecutar el proyecto:

    - gradle run
 

Pasos para Probar:

   1. Ingrese a la dirección: http://localhost:8081/api-usuario/swagger-ui.html

   2. Vaya al método POST crear-usuario

      http://localhost:8081/api-usuario/swagger-ui.html#!/usuario-controller/crearUsingPOST  
   
   3. Cree el usuario con el siguiente json:

{
  "email": "francisco@prueba.com",
  "id": 0,
  "name": "Francisco Abreu",
  "password": "Francisco04",
  "phones": [
    {
      "citycode": "2",
      "contrycode": "57",
      "number": "76544334"
    },
    {
      "citycode": "2",
      "contrycode": "57",
      "number": "776544"
    }
  ]
}

    4. copie el tokenJWT que devuelve el resutado del paso 3

    5. Vaya al método lista-usuarios y utilice el token JWT del paso 4

       http://localhost:8081/api-usuario/swagger-ui.html#!/usuario-controller/listarUsingGET

    6. Verifique que el usuario que creó en el paso 3 exista.



Base de Datos H2, usar las credenciales:

    - http://localhost:8081/api-usuario/h2-console
    - user: sa
    - pass: 

 