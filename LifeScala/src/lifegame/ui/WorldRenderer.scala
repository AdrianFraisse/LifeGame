package lifegame.ui

import java.awt.{Graphics2D, Color}

import lifegame.core.World

/**
 * Created by Adrian on 30/04/15.
 */
class WorldRenderer(val world: World, val viewWidth: Int = 0, val viewHeight: Int = 0) {

  var cellWidth = WorldRenderer.CellWidth
  var cellHeight = WorldRenderer.CellHeight
  var totalWidth = 0
  var totalHeight = 0
  var zoom = 1.0
  var viewX = 0
  var viewY = 0

  def setZoom(zoom: Double) = {
    this.zoom = zoom
    cellWidth = (WorldRenderer.CellWidth * zoom).toInt
    cellHeight = (WorldRenderer.CellHeight * zoom).toInt
    totalWidth = cellWidth * world.width
    totalHeight = cellHeight * world.height
  }

  def setCell(alive: Boolean, x: Int, y: Int) = {
    val xd = ((viewX - viewWidth / 2.0) / cellWidth).toInt
    val yd = ((viewY - viewHeight / 2.0) / cellHeight).toInt

    val cdx = xd * cellWidth - (viewX - viewWidth / 2)
    val cdy = yd * cellHeight - (viewY - viewHeight / 2)

    val cx = (x-cdx/cellWidth)
    val cy = (y-cdy/cellHeight)

    world.setCell(cx + xd, cy + yd, alive)
  }

  def render(where: Graphics2D) = {
    where.setClip(0, 0, viewWidth, viewHeight)
    where.setColor(WorldRenderer.BackgroundColor)
    where.fillRect(0, 0, viewWidth, viewHeight)

    var xd = ((viewX- viewWidth / 2.0) / cellWidth).toInt
    var yd = ((viewY - viewHeight / 2.0) / cellHeight).toInt
    if (xd < 0) xd = 0
    if (yd < 0) yd = 0

    var xf = xd + (viewWidth/cellWidth)
    var yf = yd + (viewHeight/cellHeight)
    if (xf >= world.width) xf = world.width - 1
    if (yf >= world.height) yf = world.height - 1

    var cx = xd * cellWidth - (viewX - viewWidth / 2)
    var cy = 0
    val cdy = yd * cellHeight - (viewY - viewHeight/2)

    for (x <- xd to xf) {
      cy = cdy
      for (y <- yd to yf) {
        where.setColor(WorldRenderer.BackgroundColor)
        where.drawRect(cx, cy, cellWidth, cellHeight)

        if (world.getCell(x, y).get.alive) {
          where.setColor(WorldRenderer.CellColor)
          where.fillRect(cx, cy, cellWidth, cellHeight)
        }
        cy = cy + cellHeight
      }
      cx = cx + cellWidth
    }
  }

}

object WorldRenderer {
  val CellWidth = 16
  val CellHeight = 16
  val CellColor: Color = new Color(0, 0, 255)
  val BorderColor: Color = new Color(200, 200, 200)
  val BackgroundColor: Color = new Color(255, 255, 255)

  def apply(world: World, viewWidth: Int, viewHeight: Int) = new WorldRenderer(world, viewWidth, viewHeight)

}
