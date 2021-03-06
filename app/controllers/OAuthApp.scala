package controllers

import lila.app._
import lila.oauth.{ OAuthApp => App }
import views._

final class OAuthApp(env: Env) extends LilaController(env) {

  private val appApi = env.oAuth.appApi
  private val forms  = env.oAuth.forms

  def index = Auth { implicit ctx => me =>
    appApi.list(me) map { apps =>
      Ok(html.oAuth.app.index(apps))
    }
  }

  def create = Auth { implicit ctx => _ =>
    Ok(html.oAuth.app.form.create(forms.app.create)).fuccess
  }

  def createApply = AuthBody { implicit ctx => me =>
    implicit val req = ctx.body
    forms.app.create.bindFromRequest.fold(
      err => BadRequest(html.oAuth.app.form.create(err)).fuccess,
      setup => {
        val app = setup make me
        appApi.create(app) inject Redirect(routes.OAuthApp.edit(app.clientId.value))
      }
    )
  }

  def edit(id: String) = Auth { implicit ctx => me =>
    OptionFuResult(appApi.findBy(App.Id(id), me)) { app =>
      Ok(html.oAuth.app.form.edit(app, forms.app.edit(app))).fuccess
    }
  }

  def update(id: String) = AuthBody { implicit ctx => me =>
    OptionFuResult(appApi.findBy(App.Id(id), me)) { app =>
      implicit val req = ctx.body
      forms.app
        .edit(app)
        .bindFromRequest
        .fold(
          err => BadRequest(html.oAuth.app.form.edit(app, err)).fuccess,
          data =>
            appApi.update(app) { data.update(_) } inject Redirect(routes.OAuthApp.edit(app.clientId.value))
        )
    }
  }

  def delete(id: String) = Auth { _ => me =>
    appApi.deleteBy(App.Id(id), me) inject
      Redirect(routes.OAuthApp.index)
  }
}
