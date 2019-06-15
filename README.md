# Welcome to Scala Binance API Wrapper

This project is a WIP wrapper around Binance REST & WebSocket API. It depends on Akka HTTP for making requests and Circe for JSON deserialization.

Library is available via both SBT and Maven
```
libraryDependencies += "io.github.patceev" %% "scala-binance-api" % "0.0.1-SNAPSHOT"
```

```
<dependency>
  <groupId>io.github.patceev</groupId>
  <artifactId>scala-binance-api_2.12</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

# Guide

The library tries to be consistent with Binance official API documentation.

An example showing how to get and print historical candles of all Binance's trading pairs:

```scala
val generalApi = BinanceRestAPI.GeneralEndpoints
	
generalApi.exchangeInfo.map(exchangeInfo => 
  exchangeInfo.symbols.foreach(symbol =>
    generalApi.klines(symbol.symbol, CandleInterval.withName("1h")).foreach(candles =>
      println(s"[Symbol $symbol]: $candles")
    )
  )
)
```

REST API is split into two objects:

- GeneralEndpoints, which don't requre authentication and provide access to public market data
- AccountEndpoints, which require auth and provide access to order execution and other methods

AccountEndpoints require having implicit BinanceConfiguration in the scope

```scala
implicit val binanceConf = BinanceConfiguration(publicKey = "pubKeyHere", privateKey = "privKeyHere")
```
