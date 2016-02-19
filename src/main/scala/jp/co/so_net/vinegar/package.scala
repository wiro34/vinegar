package jp.co.so_net

package object vinegar {
  lazy val newLine: String = try {
    System.getProperty("line.separator")
  } catch {
    case _: Throwable => "\n"
  }
}
