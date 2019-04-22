package io.github.patceev.binance.models.websocket

sealed trait CloseEvent {
  val eventTime: Long
  val streams: Seq[StreamWS]
}

object CloseEvent {

  case class WebsocketGracefulCloseEvent(
    eventTime: Long,
    streams: Seq[StreamWS]
  ) extends CloseEvent

  case class WebsocketErrorCloseEvent(
     eventTime: Long,
     streams: Seq[StreamWS],
     cause: Throwable
   ) extends CloseEvent

}
