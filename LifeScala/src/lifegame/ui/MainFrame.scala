package lifegame.ui

import java.awt.BorderLayout
import java.awt.event._
import javax.swing.event.{ChangeEvent, ChangeListener}
import javax.swing._

import lifegame.core.MainPrg

/**
 * Created by Adrian on 30/04/15.
 *
 * Main lifegame's frame
 */
class MainFrame extends JFrame{
  val worldComponent: WorldComponent = new WorldComponent()

  // Horizontal ScrollBar
  val xScrollBar = new JScrollBar()
  xScrollBar.setOrientation(0)
  xScrollBar.addAdjustmentListener((e: AdjustmentEvent)  => {
    worldComponent.worldRenderer.viewX = e.getValue + worldComponent.worldRenderer.viewWidth / 2
  })
  xScrollBar.addComponentListener((e: ComponentEvent) => {
    val sc = e.getSource.asInstanceOf[JScrollBar]
    sc.setMaximum(worldComponent.worldRenderer.totalWidth)
    sc.setMinimum(0)
    sc.setVisibleAmount(worldComponent.worldRenderer.viewWidth)
  })

  // Vertical ScrollBar
  val yScrollBar = new JScrollBar()
  yScrollBar.setOrientation(1)
  yScrollBar.addAdjustmentListener((e: AdjustmentEvent) => {
    worldComponent.worldRenderer.viewY = e.getValue + worldComponent.worldRenderer.viewHeight / 2
  })
  yScrollBar.addComponentListener((e: ComponentEvent) => {
    val sc = e.getSource.asInstanceOf[JScrollBar]
    sc.setMaximum(worldComponent.worldRenderer.totalHeight)
    sc.setMinimum(0)
    sc.setVisibleAmount(worldComponent.worldRenderer.viewHeight)
  })

  // Slider
  val zoom = new JSlider()
  zoom.setOrientation(1)
  zoom.setMaximum(5000)
  zoom.setMinimum(500)
  zoom.setValue(1000)
  zoom.addChangeListener(new ChangeListener {
    override def stateChanged(e: ChangeEvent): Unit = {
      worldComponent.worldRenderer.setZoom(e.getSource.asInstanceOf[JSlider].getValue / 1000.0)

      //maj des scrollbars
      yScrollBar.setMaximum(worldComponent.worldRenderer.totalHeight)
      yScrollBar.setMinimum(0)
      yScrollBar.setVisibleAmount(worldComponent.worldRenderer.viewHeight)

      xScrollBar.setMaximum(worldComponent.worldRenderer.totalWidth)
      xScrollBar.setMinimum(0)
      xScrollBar.setVisibleAmount(worldComponent.worldRenderer.viewWidth)
    }
  })

  // Controls
  val startButton = new JButton("Start")
  startButton.addActionListener((e: ActionEvent) => { MainPrg.resume() })

  val pauseButton = new JButton("Pause")
  pauseButton.addActionListener((e: ActionEvent) => { MainPrg.pause() })

  val clearButton = new JButton("Clear")
  clearButton.addActionListener((e: ActionEvent) => { MainPrg.clear() })

  def addComponentToPane() = {
    val panel = getContentPane
    panel.setLayout(new BorderLayout())

    val sp = new JPanel()
    sp.setLayout(new BorderLayout())
    sp.add(worldComponent, BorderLayout.CENTER)
    sp.add(xScrollBar, BorderLayout.SOUTH)
    sp.add(yScrollBar, BorderLayout.EAST)

    val controls = new JPanel()
    controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS))
    controls.add(startButton)
    controls.add(pauseButton)
    controls.add(clearButton)

    panel.add(zoom, BorderLayout.EAST)
    panel.add(sp, BorderLayout.CENTER)
    panel.add(controls, BorderLayout.SOUTH)

  }

  implicit def handlerToListener(handler: AdjustmentEvent => Unit) : AdjustmentListener =
    new AdjustmentListener {
      override def adjustmentValueChanged(e: AdjustmentEvent): Unit = handler(e)
    }

  implicit def handlerToListener(handler: ActionEvent => Unit) : ActionListener =
    new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = handler(e)
    }

  implicit def handlerToListener(handler: ComponentEvent => Unit): ComponentListener =
    new ComponentListener {
      override def componentShown(e: ComponentEvent): Unit = {}

      override def componentHidden(e: ComponentEvent): Unit = {}

      override def componentMoved(e: ComponentEvent): Unit = {}

      override def componentResized(e: ComponentEvent): Unit = handler(e)
    }

}

object MainFrame extends App {
  val FrameWidth = 800
  val FrameHeight = 600
  val Title = "LifeGame by A. Fraisse"

  createAndShowGUI()
  MainPrg.start()
  MainPrg.pause()

  private def createAndShowGUI() = {
    val frame = new MainFrame()
    frame.addComponentToPane()
    frame.setVisible(true)
    frame.setSize(MainFrame.FrameWidth, MainFrame.FrameHeight)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    MainPrg.addObserver(frame.worldComponent)
    frame.repaint()
    frame.invalidate()
  }
}
