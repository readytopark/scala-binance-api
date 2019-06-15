# Welcome to Scala Binance API Wrapper

This project is a WIP wrapper around Binance REST & WebSocket API. It depends on Akka HTTP for making requests and Circe for JSON deserialization.

Library is available via both SBT and Maven
```
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

# Guide

The library tries to be consistent with [Binance official API documentation](https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md).

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

An example showing how to create an order and print out all fills:

```scala
val accountApi = BinanceRestAPI.AccountEndpoints

accountApi.newOrder(
  symbol = "RVNBTC", 
  side = OrderSide.BUY, 
  `type` = OrderType.MARKET,
  quantity = 5000
).foreach(orderResponse => 
  println(s"Fills: ${orderResponse.fills}")
)
```

REST API is split into two objects:

- GeneralEndpoints, which doesn't requre authentication and provides access to public market data
- AccountEndpoints, which requires auth and provides access to order execution and other methods

AccountEndpoints requires having implicit BinanceConfiguration in the scope

```scala
implicit val binanceConf = BinanceConfiguration(publicKey = "pubKeyHere", privateKey = "privKeyHere")
```

# Roadmap

The project is still work in progress and not all API endpoints are finished. Most of the REST API is done, WebSocket isn't fully supported yet.

### Rest API check-list

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
