package lifegame.core

import java.util.{TimerTask, Timer, Observable}

/**
 * @author Adrian Fraisse
 *
 * Singleton object for main execution.
 */
object MainPrg extends Observable {
  val WorldHeight = 80
  val WorldWidth = 100

  val Nanoseconds = 1000000000
  val Fps = 25

  var stepByStep = false
  var started = false
  var paused = false

  var startTime: Long = 0
  var pauseTime: Long = 0
  var lastSec: Long = 0

  val world = World(WorldWidth, WorldHeight)

  def start() = {
    if (started) throw new RuntimeException("World already running")
    started = true

    val period = 1000 / Fps
    val timer = new Timer

    startTime = System.nanoTime()
    lastSec= System.currentTimeMillis()

    timer.scheduleAtFixedRate(new TimerTask {
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

  def pause() = synchronized {
    if (!started) throw new RuntimeException("World not started")
    if (paused) throw new RuntimeException("World already paused")
    pauseTime = System.nanoTime()
    paused = true
  }

  def resume() = synchronized {
    if (!started) throw new RuntimeException("World not started")
    if (!paused) throw new RuntimeException("World not paused")

    val elapsedTime = System.nanoTime() - pauseTime
    startTime = startTime + elapsedTime
    paused = false
  }

  def nextFrame(time: Long) = synchronized {
    world.update(time/Nanoseconds)
  }
}
