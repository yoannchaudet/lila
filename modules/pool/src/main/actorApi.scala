package lila.pool
package actorApi

import lila.game.Game
import lila.socket.SocketMember
import lila.user.User

private[pool] case class Member(
  channel: JsChannel,
  userId: Option[String],
  troll: Boolean) extends SocketMember

private[pool] object Member {
  def apply(channel: JsChannel, user: Option[User]): Member = Member(
    channel = channel,
    userId = user map (_.id),
    troll = user.??(_.troll))
}

private[pool] case class Join(
  uid: String,
  user: Option[User],
  version: Int)

private[pool] case class Talk(tourId: String, u: String, t: String, troll: Boolean)

private[pool] case class Connected(enumerator: JsEnumerator, member: Member)

private[pool] case object GetPool
private[pool] case object Reload
private[pool] case class Enter(user: User)
private[pool] case class Leave(user: User)
private[pool] case class Wave(user: User)
private[pool] case object Pairing
private[pool] case object CheckLeaders
case class RemindPool(pool: Pool)
