package jp.co.so_net.vinegar

import java.io.{File, IOException}
import java.nio.charset.MalformedInputException

import scala.util.{Failure, Success, Try}

class FileEncodingException(message: String) extends IOException(message)

trait FileReader {
  def read(file: File): Either[Throwable, String]
}

class TextFileReader extends FileReader {
  val supportedCharsets = Seq("UTF-8", "SJIS")

  def read(file: File): Either[Throwable, String] = {
    // 本来ならばファイルの文字コードを調べて fromFile に適切なエンコーディングを指定すべきだが、
    // 文字コード判別ライブラリを入れると実行ファイルのサイズが肥大化すること、
    // おそらく UTF-8 と Shift-JIS に対応しておけば問題ないことから、
    // 読み込んでみて例外が発生したら別の文字コードを試すような実装にした。
    def readBy(charsets: Seq[String]): Either[Throwable, String] = charsets match {
      case charset :: rest =>
        Try(io.Source.fromFile(file, charset).mkString) match {
          case Success(s) => Right(s)
          case Failure(e: MalformedInputException) => readBy(rest)
          case Failure(e) => Left(e)
        }
      case Nil =>
        Left(new FileEncodingException("Encoding is not supported. it is supporting UTF-8 or Shift-JIS only."))
    }
    readBy(supportedCharsets)
  }
}
