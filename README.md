# Asynchronous Binance API wrapper for Scala

This project is a WIP wrapper around Binance REST & WebSocket API. It depends on Akka HTTP for making requests and Circe for JSON deserialization. The library tries to be consistent with [Binance official API documentation](https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md). Whenever you stumble upon situations when you are not sure which parameters are required and which are optional please read the official documentation.

## Installation

Library is available via both SBT and Maven. It is hosted by Sonatype repository manager.

```scala
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
libraryDependencies += "io.github.patceev" %% "scala-binance-api" % "0.0.1-SNAPSHOT"
```

```
<dependency>
  <groupId>io.github.patceev</groupId>
  <artifactId>scala-binance-api_2.12</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

You also always have an option to install the library locally.

```
git clone https://github.com/patceev/scala-binance-api.git
cd scala-binance-api
sbt publishLocal
```

After that you'll be able to add the library to your dependecies in the same way.

```libraryDependencies += "io.github.patceev" %% "scala-binance-api" % "VERSION"```

Where version is defined in the `build.sbt` of the library.

## Features

### Rest API check-list

- [ ] ping
- [x] time
- [x] exchangeInfo
- [x] depth
- [ ] historicalTrades
- [x] aggTrades
- [x] klines
- [x] avgPrice
- [x] change24h
- [x] price
- [x] bookTicker
- [x] newTestOrder
- [x] newOrder
- [x] orderStatus
- [x] cancelOrders
- [x] openOrders
- [x] allOrders
- [x] myTrades
- [ ] startUserDataStream
- [ ] keepAliveUserDataStream
- [ ] deleteUserDataStream

### Websocket API check-list

- [x] Aggregated Trade Stream
- [x] Trade Stream
- [x] Candle Stream
- [ ] Mini-ticker Stream
- [ ] Ticker Stream
- [ ] All Markets Ticker Stream
- [ ] Parial Book Depth Stream
- [ ] Diff.Depth Stream

## Guide

Since this library is using Akka HTTP, three implicit values should be in scope whenether you are doing a request.
- ActorSystem
- ActorMaterializer
- ExecutionContext

The simplest way to setup all these values is:

```scala
implicit val as = ActorSystem()
implicit val mat = ActorMaterializer()
implicit val ec = as.dispatcher
```

### REST API

REST API is split into two objects:

- GeneralEndpoints, which doesn't requre authentication and provides access to public market data
- AccountEndpoints, which requires auth and provides access to order execution and other methods

An example showing how to get and print historical candles of all Binance's trading pairs:

```scala
val generalApi = BinanceRestAPI.GeneralEndpoints
	
generalApi.exchangeInfo.map(exchangeInfo => 
  exchangeInfo.symbols.foreach(symbol =>
    generalApi.klines(symbol.symbol, CandleInterval.`1h`).foreach(candles =>
      println(s"[Symbol $symbol]: $candles")
    )
  )
)
```

An example showing how to create an order and print out all fills. Please note that having implicit `BinanceConfiguration` is is necessary for AccountEndpoint requests.

```scala
val accountApi = BinanceRestAPI.AccountEndpoints
implicit val binanceConf = BinanceConfiguration(publicKey = "pubKeyHere", privateKey = "privKeyHere")

accountApi.newOrder(
  symbol = "RVNBTC", 
  side = OrderSide.BUY, 
  `type` = OrderType.MARKET,
  quantity = 5000
).foreach(orderResponse => 
  println(s"Fills: ${orderResponse.fills}")
)
```

For more examples check out `test/` directory. It contains tests and examples of usage for most of the available functions. Some still might be missing, it's a work in progress.

## Error handling

All errors described in the Binance API documentation are deserialized into corresponding case classes, which makes it easy to handle all neccesary errors. For reference, navigate to `/models/BinanceError.scala`

```scala
case class IllegalChars(message: String) extends BinanceError
case class TooManyParameters(message: String) extends BinanceError
case class MandatoryParamEmptyOrMalformed(message: String) extends BinanceError
case class UnknownParam(message: String) extends BinanceError
case class UnreadParameters(message: String) extends BinanceError
// much more errors ...
```

```scala
generalApi.exchangeInfo
  .map(println)
  .recover { case TooManyRequests(_) => // do something here }
```

## Tests

Currently covering all API endpoints with tests. It's a work in progress.

- [x] Account Endpoints
- [x] General Endpoints
- [ ] User Streams
- [ ] WebSocket API

For reference, navigate to `/src/test`. Tests are self-documenting and showcase how to use the library.
