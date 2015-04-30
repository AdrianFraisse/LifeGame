package lifegame.core

/**
 * Created by Adrian on 28/04/15.
 */
class World(val width: Int, val height: Int) {
  val cells: List[List[Cell]] = buildSame(new Cell(false))

  var neighbors: List[List[Int]] = buildSame(0)

  def setCell(x: Int, y: Int, alive: Boolean) =
    cells(x)(y).alive == alive

  def getCell(x: Int, y: Int) : Option[Cell] =
      return Some(cells(x)(y));

  // Construit une liste de listes de T avec la fonction passée en param
  def build[T](f: (Int, Int) => T) : List[List[T]] =
    (for (x <- 0 to width)
       yield (for (y <- 0 to height)
         yield f(x, y)).toList)
      .toList

  def buildSame[T](f: => T) :  List[List[T]] = build((_, _) => f)

  def update(time: Double) = synchronized {
    // Lois pour une cellule :
    //	-Une cellule vivante meure si elle a moins de 2 cellules ou plus de 4 cellules voisines vivantes
    // 	-Une cellule morte reviens � la vie si elle a 3 cellules voisines vivantes

    neighbors = build(countLivingNeighbors)

    var c: Cell = null
    var n: Int = 0
    for (x <- 0 to width) {
      for (y <- 0 to height) {
        c = cells(x)(y)
        n = neighbors(x)(y)
        if (c.alive) {
          if (n <= 2 || n >= 4)
            c.alive = false
        } else {
          if (n == 3)
            c.alive = true
        }
      }
    }

  }

  def tue(): Unit = synchronized {
    for (e <- cells)
      for (l <- e)
        l.alive = false
  }

  def countLivingNeighbors(x: Int, y: Int): Int = {
    var nb: Int = 0
    for (yc <- y-1 to y+1 if yc >= 0 && yc < height )
      for (xc <- x-1 to x+1 if xc >= 0 && xc < width && (xc != x || yc != y))
        if (cells(xc)(yc).alive)
          nb = nb + 1;
        if (cells(x)(y).alive && nb >= 4)
          return nb
    nb
  }


}
