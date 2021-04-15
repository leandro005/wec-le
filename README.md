# wec-le
Wenance Challenge Repository

# API REST para consultar precio, y calcular promedio y porcentaje del promedio frente al maximo precio

_Es un API REST que aloja los datos obtenidos del API de CEX, los guarda en memoria y nos permite: 
	* Consultar el precio del Bitcoin en un determinado tiempo
	* Calcular el promedio de los precios en un periodo de tiempo y el porcentaje entre promedio y el maximo precio
_


### Tecnologias 📋

	* Spring Boot Web
	* Spring WebFlux
	* Java 1.8

### Arquitectura 🔧

_Se dividió el diseño en dos partes, una refiere al scheduler encargado
de leer el API de CEX y guardar los datos en memoria, y la otra es el API
nuestra que exponemos para ser consumida.
Optamos guardar la serie de precios en un LinkedList para realizar la performance
de busqueda y filtros en los Streams de la coleccion.
Al tener la persistencia en memoria y no invocar a una DB, elegimos prescindir de una
capa de servicios y alojar tanto la consulta de datos como la logica en un layer tipo
repositorio. 
_

### Ejecutamos

_ Para levantar la aplicacion podemos: _
	
	* Correr el metodo main de ChallengeApplication.java
	* mvn spring-boot:run



```
Da un ejemplo
```

_Y repite_

```
hasta finalizar
```

