package lifegame.ui

import java.awt.{Graphics2D, Graphics}
import java.awt.event._
import java.util.{Observable, Observer}
import javax.swing.JComponent

import lifegame.core.MainPrg

/**
 * Created by Adrian on 30/04/15.
 */
class WorldComponent extends JComponent with Observer with MouseListener with MouseMotionListener with ComponentListener {

  val worldRenderer = WorldRenderer(MainPrg().world, this.getVisibleRect.width, this.getVisibleRect.height)
  worldRenderer.viewX = worldRenderer.viewWidth / 2
  worldRenderer.viewY = worldRenderer.viewHeight / 2

  setSize(worldRenderer.viewWidth, worldRenderer.viewHeight)

  this.addMouseListener(this)
  this.addMouseMotionListener(this)
  this.addComponentListener(this)
  repaint()

  override def paintComponent(g: Graphics) = worldRenderer.render(g.asInstanceOf[Graphics2D])

  override def update(o: Observable, arg: scala.Any): Unit = repaint()

  override def mouseClicked(e: MouseEvent): Unit = e.getButton match {
    case MouseEvent.BUTTON1 => worldRenderer.setCell(true, e.getX, e.getY)
    case MouseEvent.BUTTON3 => worldRenderer.setCell(false, e.getX, e.getY)
  }

  override def mouseDragged(e: MouseEvent): Unit = e.getButton match {
    case MouseEvent.BUTTON1 => worldRenderer.setCell(true, e.getX, e.getY)
    case MouseEvent.BUTTON3 => worldRenderer.setCell(false, e.getX, e.getY)
  }

  override def componentResized(e: ComponentEvent): Unit = {
    worldRenderer.viewWidth = this.getWidth
    worldRenderer.viewHeight = this.getHeight
  }

  override def mouseEntered(e: MouseEvent): Unit = ???
  override def mousePressed(e: MouseEvent): Unit = ???
  override def mouseReleased(e: MouseEvent): Unit = ???
  override def mouseMoved(e: MouseEvent): Unit = ???
  override def mouseExited(e: MouseEvent): Unit = ???
  override def componentShown(e: ComponentEvent): Unit = ???
  override def componentHidden(e: ComponentEvent): Unit = ???
  override def componentMoved(e: ComponentEvent): Unit = ???
}
