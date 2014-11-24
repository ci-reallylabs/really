/**
 * Copyright (C) 2014-2015 Really Inc. <http://really.io>
 */
package io.really.rql

import io.really.model.{ FieldKey, Field }
import play.api.libs.json.{ JsObject, JsValue }

import scala.util.parsing.input.{ Position, Positional }

object RQL {

  case class Term(term: String) extends Positional

  case class TermValue(value: JsValue) extends Positional

  trait Operator extends Positional

  object Operator {

    case object Gt extends Operator {
      override def toString() = ">"
    }

    case object Gte extends Operator {
      override def toString() = ">="
    }

    case object Lt extends Operator {
      override def toString() = "<"
    }

    case object Lte extends Operator {
      override def toString() = ">="
    }

    case object Eq extends Operator {
      override def toString() = "="
    }

    case object Between extends Operator {
      override def toString() = "<...>"
    }

    case object IN extends Operator {
      override def toString() = "IN"
    }

  }

  abstract class Query {
    def isValid(obj: JsObject, fields: Map[FieldKey, Field[_]]): Boolean
  }

  case object EmptyQuery extends Query {
    def isValid(obj: JsObject, fields: Map[FieldKey, Field[_]]): Boolean = true
  }

  case class SimpleQuery(key: Term, op: Operator, value: TermValue) extends Query with Positional {
    override def toString() =
      key.term + " " + op.toString + " " + value.value.toString

    def isValid(obj: JsObject, fields: Map[FieldKey, Field[_]]): Boolean = {
      //todo implement that
      true
    }
  }

  case class AndCombinator(q1: Query, q2: Query) extends Query with Positional {
    override def toString() =
      q1.toString + " AND " + q2.toString

    def isValid(obj: JsObject, fields: Map[FieldKey, Field[_]]): Boolean =
      q1.isValid(obj, fields) && q2.isValid(obj, fields)
  }

  trait RQLException
  class ParseError(message: String, position: Position)
    extends Exception("at: " + position.toString + ", parser error:" + message) with RQLException
  class MissingValue(fieldKey: String)
    extends Exception(s"cannot find value for key {$fieldKey} in passed values") with RQLException

}