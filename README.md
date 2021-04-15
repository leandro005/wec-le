# wec-le
Wenance Challenge Repository

# API REST para consultar precio, y calcular promedio y porcentaje del promedio frente al maximo precio

Es un API REST que aloja los datos obtenidos del API de CEX, los guarda en memoria y nos permite: 
* Consultar el precio del Bitcoin en un determinado tiempo
* Calcular el promedio de los precios en un periodo de tiempo y el porcentaje entre promedio y el maximo precio



## Tecnologias 📋

* Spring Boot Web
* Spring WebFlux
* Java 1.8

## Arquitectura

Se dividió el diseño en dos partes, una refiere al scheduler encargado
de leer el API de CEX y guardar los datos en memoria, y la otra es el API
nuestra que exponemos para ser consumida.
Optamos guardar la serie de precios en un LinkedList para realizar la performance
de busqueda y filtros en los Streams de la coleccion.
Al tener la persistencia en memoria y no invocar a una DB, elegimos prescindir de una
capa de servicios y alojar tanto la consulta de datos como la logica en un layer tipo
repositorio. 

## Ejecutamos

Para levantar la aplicacion podemos: 
	
* Correr el metodo main de ChallengeApplication.java
* mvn spring-boot:run


## Endpoints

* Para obneter el precio en un timestamp:

/wcl/priceByTimestamp/{timestamp}    

Ejemplo:

```
http://localhost:8080/wcl/priceByTimestamp/2021-04-14 20:46:48
```

Respuesta:

```
{"precio":"63102.9","message":"OK con la busqueda"}
```



* Para obneter el precio en un timestamp:

/wcl/averageAndDiffPrice/{timestamp1}/{timestamp2}

Ejemplo:

```
http://localhost:8080/wcl/averageAndDiffPrice/2021-04-14 21:31:58/2021-04-14 21:49:08
```

Respuesta:

```
{"average":"63161.45384615385","percentage":"0.040429445686855164","message":"OK - Se recuperaron el promedio y el porcentaje"}
```

Donde el formato de los timestamp es ->  yyyy-MM-dd mm:ss:SS









