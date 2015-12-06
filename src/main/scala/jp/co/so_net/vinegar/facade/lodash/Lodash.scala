package jp.co.so_net.vinegar.facade.lodash

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}

@js.native
class Lodash extends js.Object {
  def isObject(obj: Any): Boolean = js.native

  def assign(obj: js.Any, src: js.Any): js.Object = js.native
}
