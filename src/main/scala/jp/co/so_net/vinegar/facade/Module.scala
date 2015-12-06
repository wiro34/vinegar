package jp.co.so_net.vinegar.facade

import jp.co.so_net.vinegar.facade.lodash.Lodash

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}

class Module[T <: js.Object](val moduleName: String) {
  val module: T = Require[T](moduleName)
}

trait GlobalExport[T <: js.Object] {
  this: Module[T] =>

  // ScalaJS ではネストしたクラス（namespace）へのアクセス方法がないので
  // グローバルに書き出して JSName で指定できるようにする
  Require[Lodash]("lodash").assign(g, js.Dynamic.literal(this.moduleName -> this.module))
}

abstract class GlobalExportedModule[T <: js.Object](override val moduleName: String)
  extends Module[T](moduleName) with GlobalExport[T]
