package lifegame.core

import java.util.{TimerTask, Timer, Observable}

/**
 * Created by Adrian on 30/04/15.
 */
class MainPrg(val world: World, var stepByStep: Boolean = false, var started: Boolean = false, var paused: Boolean = false) extends Observable {

  val NanoSeconds = 1000000000
  val Fps = 25
  
  var startTime: Long = 0
  var pauseTime: Long = 0
  var lastSec: Long = 0

  def start() = {
    if (started) throw new RuntimeException("World already running")
    started = true

    val period = 1000 / Fps
    val timer = new Timer

    startTime = System.nanoTime()
    lastSec= System.currentTimeMillis()

    timer.scheduleAtFixedRate(new TimerTask {
      // TODO : Refactor
      override def run(): Unit = {
        if (!paused) {
          nextFrame(System.nanoTime() - startTime)
          if (stepByStep)
            pause()
        }
        if (System.currentTimeMillis() - lastSec >= 1000)
          lastSec = System.currentTimeMillis()

        setChanged()
        notifyObservers()
      }
    }, 0, period)

  }

  def pause() = {
    if (!started) throw new RuntimeException("World not started")
    if (paused) throw new RuntimeException("World already paused")
    pauseTime = System.nanoTime()
    paused = true
  }
  
  def resume() = {
    if (!started) throw new RuntimeException("World not started")
    if (!paused) throw new RuntimeException("World not paused")

    val elapsedTime = System.nanoTime() - pauseTime
    startTime = startTime + elapsedTime
    paused = false
  }

  def nextFrame(time: Long) = synchronized {
    world.update(time/NanoSeconds)
  }

}

object MainPrg {
  val WorldHeight = 400
  val WorldWidth = 400

  def apply() = new MainPrg(new World(WorldWidth, WorldHeight))
}
