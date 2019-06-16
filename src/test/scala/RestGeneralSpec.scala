import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import io.github.patceev.binance.BinanceRestAPI
import io.github.patceev.binance.models.BinanceError.OptionalParamsBadCombo
import io.github.patceev.binance.models.enums.CandleInterval
import org.scalatest.AsyncFlatSpec


class RestGeneralSpec extends AsyncFlatSpec {

	val generalApi = BinanceRestAPI.GeneralEndpoints
	implicit val as = ActorSystem()
	implicit val mat = ActorMaterializer()
	implicit val ec = as.dispatcher

	behavior of "Binance General Rest API Spec"

	it should "return time" in {
		generalApi.time.map(time => assert(time.serverTime > 0))
	}

	it should "return exchangeInfo" in {
		generalApi.exchangeInfo.map(_ => succeed)
	}

	it should "return depth for 100 by default" in {
		generalApi.depth("ETHBTC").map(depth => {
			assert(depth.asks.length === depth.bids.length)
			assert(depth.asks.length === 100)
		})
	}

	it should "return depth for 20" in {
		generalApi.depth("ETHBTC", Some(20)).map(depth =>
			assert(depth.asks.length === 20)
		)
	}

	it should "return 500 trades by default" in {
		generalApi.trades("ETHBTC").map(trades =>
			assert(trades.length === 500)
		)
	}

	it should "return 1000 trades" in {
		generalApi.trades("ETHBTC", Some(1000)).map(trades =>
			assert(trades.length === 1000)
		)
	}

	it should "return 500 aggTrades by default" in {
		generalApi.aggTrades("ETHBTC").map(trades =>
			assert(trades.length === 500)
		)
	}

	it should "return 100 aggTrades" in {
		generalApi.aggTrades("ETHBTC", limit = Some(100)).map(trades =>
			assert(trades.length === 100)
		)
	}

	it should "return aggTrades starting from startTime" in {
		generalApi.aggTrades(
			symbol = "ETHBTC",
			startTime = Some(1550786739099L),
			endTime = Some(1550789839099L),
			limit = Some(5)
		).map(trades =>
			assert(trades.head.timestamp === 1550789834961L)
		)
	}

	it should "return OptionalParamsBadCombo if params are misused" in {
		recoverToSucceededIf[OptionalParamsBadCombo] {
			generalApi.aggTrades(symbol = "ETHBTC", startTime = Some(1550786739099L))
		}
	}

	it should "return 500 klines by default" in {
		generalApi.klines("ETHBTC", CandleInterval.`1h`)
			.map(klines => assert(klines.length === 500))
	}

	it should "return 1000 klines if limit == 1000" in {
		generalApi.klines("ETHBTC", CandleInterval.`1h`, limit = Some(1000))
			.map(klines => assert(klines.length === 1000))
	}

	it should "return klines starting from startTime" in {
		generalApi.klines("ETHBTC", CandleInterval.`1h`, startTime = Some(1550448000000L))
			.map(klines => assert(klines.head.openTime === 1550448000000L))
	}

	it should "return klines ending with endTime" in {
		generalApi.klines("ETHBTC", CandleInterval.`1h`, endTime = Some(1550448000000L))
			.map(klines => assert(klines.head.openTime === 1548651600000L))
	}

	it should "return avgPrice" in {
		generalApi.avgPrice("ETHBTC").map(_ => succeed)
	}

	it should "return ticker price change statistics for one symbol" in {
		generalApi.change24h(Some("ETHBTC")).map(change => assert(change.length == 1))
	}

	it should "return all price change statistics if symbol is omitted" in {
		generalApi.change24h().map(change => assert(change.length > 1))
	}

	it should "return price for one symbol" in {
		generalApi.price(Some("ETHBTC")).map(prices => assert(prices.length == 1))
	}

	it should "return all prices if symbol is omitted" in {
		generalApi.price().map(prices => assert(prices.length > 1))
	}

	it should "return order book ticker for one symbol" in {
		generalApi.bookTicker(Some("ETHBTC")).map(books => assert(books.length == 1))
	}

	it should "return all order book tickers if symbol is omitted" in {
		generalApi.bookTicker().map(books => assert(books.length > 1))
	}

}