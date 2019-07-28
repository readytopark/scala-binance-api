import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import io.github.patceev.binance.models.BinanceError
import io.github.patceev.binance.models.enums._
import io.github.patceev.binance.{BinanceConfiguration, BinanceRestAPI}
import org.scalatest.AsyncFlatSpec

import scala.concurrent.ExecutionContext

class RestAccountSpec extends AsyncFlatSpec {

	val config: Config = ConfigFactory.load("tests.conf")

	implicit val binanceConf: BinanceConfiguration = BinanceConfiguration(
		publicKey = config.getString("binance.publicKey"),
		privateKey = config.getString("binance.privateKey")
	)

	implicit val as: ActorSystem = ActorSystem()
	implicit val mat: ActorMaterializer = ActorMaterializer()
	implicit val ec: ExecutionContext = as.dispatcher

	val accountApi = BinanceRestAPI.AccountEndpoints

	behavior of "Binance Account Rest API Spec"

	if (config.getBoolean("binance.isProvided")) {

		it should "send test order" in {
			accountApi.newTestOrder(
				symbol = "ETHUSDT",
				side = OrderSide.BUY,
				`type` = OrderType.MARKET,
				quantity = 0.05
			).map(_ => succeed)
		}

		it should "create and execute a market order" in {
			accountApi.newOrder(
				symbol = "ETHUSDT",
				side = OrderSide.BUY,
				`type` = OrderType.MARKET,
				quantity = 0.05,
			).map(response =>
				assert(response.executedQty === 0.05)
			).recover { case e: BinanceError => println(e); fail }
		}

		it should "create and execute a limit order" in {
			accountApi.newOrder(
				symbol = "ETHUSDT",
				side = OrderSide.BUY,
				`type` = OrderType.LIMIT,
				timeInForce = Some(TimeInForce.GTC),
				quantity = 0.05,
				price = Some(240),
				newClientOrderId = Some("testOrder")
			).map(response =>
				assert(response.clientOrderId === "testOrder")
			).recover { case e: BinanceError => println(e); fail }
		}

		it should "return current open orders and it should contain testOrder" in {
			accountApi.openOrders(Some("ETHUSDT")).map(orders => {
				assert(orders.head.clientOrderId === "testOrder")
			})
		}

		it should "cancel order with origClientOrderId == testOrder" in {
			accountApi.cancelOrder("ETHUSDT", origClientOrderId = Some("testOrder"))
				.map(response => assert(
					List(OrderStatus.CANCELED, OrderStatus.PENDING_CANCEL) contains response.status
				))
		}

		it should "return testOrder in allOrders" in {
			accountApi.allOrders("ETHUSDT").map(orders =>
				assert(orders.exists(order => order.clientOrderId == "testOrder"))
			)
		}

		it should "return account information" in {
			accountApi.account().map(_ => succeed)
		}

		it should "return test market order trade in myTrades" in {
			accountApi.myTrades("ETHUSDT").map(trades =>
				assert(trades.exists(trade => trade.isBuyer && trade.qty == 0.05))
			)
		}
	} else {

		it should "work only when you provide credentials into your tests.conf file" in {
			succeed
		}
	}
}
