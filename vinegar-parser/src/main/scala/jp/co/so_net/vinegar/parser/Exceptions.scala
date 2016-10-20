package jp.co.so_net.vinegar.parser

private[parser]
object Exceptions {
  class UnsupportedKeywordException(message: String) extends RuntimeException(message)
}
