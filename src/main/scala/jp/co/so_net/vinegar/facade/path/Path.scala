package jp.co.so_net.vinegar.facade.path

import scala.scalajs.js

@js.native
class Path extends js.Object {
  def extname(filename: String): String = js.native

  def basename(filename: String): String = js.native

  def join(paths: String*): String = js.native
}
