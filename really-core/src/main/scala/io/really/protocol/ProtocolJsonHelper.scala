/**
 * Copyright (C) 2014-2015 Really Inc. <http://really.io>
 */
package io.really.protocol

import _root_.io.really.Result._
import _root_.io.really._
import play.api.libs.json._
import play.api.libs.functional.syntax._

/*
 * JSON Formats for Really Protocol
 */

object ProtocolFormats {
  private val Body = "body"
  private val Tag = "tag"
  private val R = "r"
  private val Revision = "rev"
  private val Meta = "meta"
  private val Event = "evt"

  /*
   * JSON Reads for Request
   */
  object RequestReads {
    val tagReads = (__ \ 'tag).read[Long]
    val traceIdReads = (__ \ 'traceId).readNullable[String]
    val cmdReads = (__ \ 'cmd).read[String].map(_.toLowerCase)
    val accessTokenReads = (__ \ 'accessToken).read[String]
    val rReads = (__ \ 'r).read[R]
    val revReads = (__ \ 'rev).read[Long]

    /*
     * JSON Reads for [[io.really.Request.Subscribe]] Request
     */
    object Subscribe {
      val bodyReads = (__ \ 'body).read[SubscriptionBody]

      def read(ctx: RequestContext) = (bodyReads) map (body => Request.Subscribe(ctx, body))
    }

    /*
     * JSON Reads for [[io.really.Request.Unsubscribe]] Request
     */
    object Unsubscribe {
      val bodyReads = (__ \ 'body).read[UnsubscriptionBody]

      def read(ctx: RequestContext) = (bodyReads) map (body => Request.Unsubscribe(ctx, body))
    }

    /*
     * JSON Reads for [[io.really.Request.GetSubscription]] Request
     */
    object GetSubscription {
      def read(ctx: RequestContext) = (rReads) map (r => Request.GetSubscription(ctx, r))
    }

    /*
     * JSON Reads for [[io.really.Request.Get]] Request
     */
    object Get {
      val cmdOptsReads = (__ \ 'cmdOpts).read[GetOpts]

      def read(ctx: RequestContext) = (rReads and cmdOptsReads)((r, cmdOpts) => Request.Get(ctx, r, cmdOpts))
    }

    /*
     * JSON Reads for [[io.really.Request.Update]] Request
     */
    object Update {
      val bodyReads = (__ \ 'body).read[UpdateBody]
      def read(ctx: RequestContext) =
        (rReads and revReads and bodyReads)((r, rev, body) => Request.Update(ctx, r, rev, body))
    }

    /*
     * JSON Reads for [[io.really.Request.Read]] Request
     */
    object Read {
      val cmdOptsReads = (__ \ 'cmdOpts).read[ReadOpts]

      def read(ctx: RequestContext) = (rReads and cmdOptsReads)((r, cmdOpts) => Request.Read(ctx, r, cmdOpts))
    }

    /*
     * JSON Reads for [[io.really.Request.Create]] Request
     */
    object Create {
      val bodyReads = (__ \ 'body).read[JsObject]

      def read(ctx: RequestContext) =
        (rReads and bodyReads)((r, body) => Request.Create(ctx, r, body))
    }

    /*
     * JSON Reads for [[io.really.Request.Delete]] Request
     */
    object Delete {
      def read(ctx: RequestContext) = (rReads) map (r => Request.Delete(ctx, r))
    }

  }

  /*
   * Represent JSON Writes for Responses
   */
  object ResponseWrites {
    /*
     * Represent JSON Writes for Subscribe Response
     */
    object Subscribe {
      def toJson(request: Request.Subscribe, response: SubscribeResult) =
        Json.obj(
          Tag -> request.ctx.tag,
          Body -> Json.obj("subscriptions" -> response.subscriptions)
        )
    }

    /*
     * Represent JSON Writes for Unsubscribe Response
     */
    object Unsubscribe {
      def toJson(request: Request.Unsubscribe, response: UnsubscribeResult) =
        Json.obj(
          Tag -> request.ctx.tag,
          Body -> Json.obj("unsubscriptions" -> response.unsubscriptions)
        )
    }

    /*
     * Represent JSON Writes for [[io.really.Response.GetSubscription]] Response
     */
    object GetSubscription {
      def toJson(request: Request.GetSubscription, response: GetSubscriptionResult) =
        Json.obj(
          Tag -> request.ctx.tag,
          R -> request.r,
          Body -> Json.obj("fields" -> response.fields)
        )
    }

    /*
     * Represent JSON Writes for [[io.really.Response.Get]] Response
     */
    object Get {
      def toJson(request: Request.Get, response: GetResult) =
        Json.obj(
          Tag -> request.ctx.tag,
          Meta -> Json.obj("fields" -> response.fields),
          R -> request.r,
          Body -> response.body
        )
    }

    /*
     * Represent JSON Writes for [[io.really.Response.Update]] Response
     */
    object Update {
      def toJson(request: Request.Update, response: UpdateResult) =
        Json.obj(
          Tag -> request.ctx.tag,
          R -> request.r,
          Revision -> response.rev
        )
    }

    /*
     * Represent JSON Writes for [[io.really.Response.Read]] Response
     */
    object Read {
      def toJson(request: Request.Read, response: ReadResult) =
        Json.obj(
          Tag -> request.ctx.tag,
          Meta -> Json.obj("subscription" -> response.subscription),
          R -> request.r,
          Body -> response.body
        )
    }

    /*
     * Represent JSON Writes for [[io.really.Response.Create]] Response
     */
    object Create {
      def toJson(request: Request.Create, response: CreateResult) =
        Json.obj(
          Tag -> request.ctx.tag,
          R -> request.r,
          Body -> response.body
        )
    }

    /*
     * Represent JSON Writes for [[io.really.Response.Delete]] Response
     */
    object Delete {
      def toJson(request: Request.Delete) =
        Json.obj(
          Tag -> request.ctx.tag,
          R -> request.r
        )
    }

  }

  /*
   * Represent JSON Writes for Push Messages
   */
  object PushMessageWrites {

    /*
     * Represent Created Push Message
     */
    object Created {
      def toJson(subscriptionId: String, r: R, createdObj: JsObject) =
        Json.obj(
          R -> r,
          Event -> "created",
          Meta -> Json.obj("subscription" -> subscriptionId),
          Body -> createdObj
        )
    }

    /*
     * Represent Deleted Push Message
     */
    object Deleted {
      def toJson(deletedBy: R, r: R) =
        Json.obj(
          R -> r,
          Event -> "deleted",
          Meta -> Json.obj("deletedBy" -> deletedBy)
        )
    }

    /*
     * Represent Updated Push Message
     */
    object Updated {
      def toJson(r: R, rev: Int, ops: List[FieldUpdatedOp]) =
        Json.obj(
          R -> r,
          Revision -> rev,
          Event -> "updated",
          Body -> Json.toJson(ops)(FieldUpdatedOp.FieldUpdatedOpListWrites)
        )
    }
  }

}
