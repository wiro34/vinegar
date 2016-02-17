//package jp.co.so_net.vinegar.transformer
//
//import gherkin.ast.{Step => GherkinStep, _}
//import jp.co.so_net.vinegar.model._
//
///**
// * Transform List[gherkin.ast.*] into step tree
// */
//object StepMapper {
//
//  import StringTrimmer.trimComment
//
//  def mapSteps(steps: Seq[GherkinStep], comments: Seq[Comment], commentScanStop: Int): Seq[Step] = {
//    def inner(steps: Seq[GherkinStep], result: Seq[Step]): Seq[Step] = {
//      steps match {
//        case step :: tail =>
//          val text = step.getText
//          val argument = getArgument(Option(step.getArgument))
//          val comment = getComment(step, tail.headOption, comments, commentScanStop)
//          StepFactory.create(step, result.lastOption) match {
//            case g: Given =>
//              inner(tail, result :+ g.copy(text = text, argument = argument, comment = comment))
//            case w: Then =>
//              inner(tail, result :+ w.copy(text = text, argument = argument, comment = comment))
//            case t: When =>
//              inner(tail, result :+ t.copy(text = text, argument = argument, comment = comment))
//          }
//        case Nil =>
//          result
//      }
//    }
//    inner(steps, Nil)
//  }
//
//  private def getArgument(nodeOption: Option[Node]): Option[Argument] = nodeOption match {
//    case Some(ds: DocString) =>
//      Some(DataText(ds.getContent))
//    case _ =>
//      None
//  }
//
//  private def getComment(step: GherkinStep, nextStep: Option[GherkinStep], comments: Seq[Comment], commentScanStop: Int): Option[String] = {
//    val range = Range(
//      step.getLocation.getLine,
//      nextStep.map(_.getLocation.getLine).getOrElse(commentScanStop)
//    )
//    val stepComments = comments
//      .filter(comment => range.contains(comment.getLocation.getLine))
//      .sortBy(comment => comment.getLocation.getLine)
//      .map(comment => comment.getText)
//      .mkString("\n")
//    trimComment(stepComments) match {
//      case s if s.nonEmpty => Some(s)
//      case _ => None
//    }
//  }
//}
//package jp.co.so_net.vinegar.parser
//
//import gherkin.ast.{Comment, Step}
//import jp.co.so_net.vinegar.model._
//
//import scala.annotation.tailrec
//
//
//class StepExtractor(steps: Seq[Step], comments: Seq[Comment]) {
//
//  import StepMatchPattern._
//
//  def parse: Seq[GivenGroup] = {
//    inner(steps, Root(groups = Seq.empty[GivenGroup])).groups
//  }
//
//  @tailrec
//  private def inner(steps: Seq[Step], root: Root): Root = {
//    extractHeadStep(steps, root) match {
//      case step@Some(g: Given) =>
//        inner(steps.tail, root)
//      //        inner(steps.tail, step, root.copy(children = root.children :+ g))
//      //      case step@Some(w: When) =>
//      //        val g = root.children.lastOption.getOrElse(Given())
//      //        inner(steps.tail, step, root.copy(children = initOfChildren(root) :+ g.copy(children = g.children :+ w)))
//      //      case step@Some(t: Then) =>
//      //        val g = root.children.lastOption.getOrElse(Given())
//      //        val w = g.children.lastOption.getOrElse(When())
//      //        inner(steps.tail, step, root.copy(children = initOfChildren(root) :+ g.copy(children = initOfChildren(g) :+ w.copy(children = w.children :+ t))))
//      case _ =>
//        root
//    }
//  }
//
//  def extractHeadStep(steps: Seq[Step], root: Root): Option[StepCase] = {
//    steps.headOption.map { step =>
//      val text = step.getText
//      val comment = ""
//      (step, root.lastGroup) match {
//        case GivenPattern(givenStep, lastGroup) => Given(text = text)
//        //        case WhenPattern(_) => When(text = text)
//        //        case ThenPattern(_) => Then(text = text)
//
//      }
//    }
//  }
//
//  private def initOfChildren[T](hc: HasChildren[T]): Seq[T] = hc.children match {
//    case init :+ last => init
//    case _ => Nil
//  }
//}
//
//
//
////  private def inner(steps: Seq[gherkin.ast.Step], last: Option[Case], root: Root): Root = {
////    headCase(steps, last) match {
////      case step@Some(g: Given) =>
////        inner(steps.tail, step, root.copy(children = root.children :+ g))
////      //      case step@Some(w: When) =>
////      //        val g = root.children.lastOption.getOrElse(Given())
////      //        inner(steps.tail, step, root.copy(children = initOfChildren(root) :+ g.copy(children = g.children :+ w)))
////      //      case step@Some(t: Then) =>
////      //        val g = root.children.lastOption.getOrElse(Given())
////      //        val w = g.children.lastOption.getOrElse(When())
////      //        inner(steps.tail, step, root.copy(children = initOfChildren(root) :+ g.copy(children = initOfChildren(g) :+ w.copy(children = w.children :+ t))))
////      case None =>
////        root
////    }
////  }
//
////  private def take(id: Int, stepOption: Option[Step], last: Option[Case]): Option[Case] = {
////    stepOption.map { step =>
////      val text = getStepText(step)
////      val note = getStepComment(step)
////      (step, last) match {
////        case GivenPattern(_) => Given(text = text, comment = note)
////        case WhenPattern(_) => When(text = text, comment = note)
////        case ThenPattern(_) => Then(id = makeCaseId(id), text = text, comment = note)
////      }
////    }
////  }
//
//
////  private def getStepComment(step: Step): Option[String] = {
////    val range = Range(step.getLocation.getLine, scanStopLine(step))
////    val comments = feature.getComments
////      .filter(comment => range.contains(comment.getLocation.getLine))
////      .sortBy(comment => comment.getLocation.getLine)
////      .map(comment => comment.getText)
////      .mkString("\n")
////    trimComment(comments) match {
////      case s if s.nonEmpty => Some(s)
////      case _ => None
////    }
////  }
