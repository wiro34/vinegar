package jp.co.so_net.vinegar.facade

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}

object Require {
  def apply[T <: js.Object](name: String): T = g.require(name).asInstanceOf[T]
}
