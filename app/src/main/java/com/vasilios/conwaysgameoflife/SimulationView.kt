package com.vasilios.conwaysgameoflife

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import java.io.Serializable
import kotlin.random.Random


class SimulationView : SurfaceView {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, atopRights: AttributeSet) : super(ctx, atopRights)

    var drawingRunnable = DrawingRunnable()

    init {
        // create a new thread for the grid
        val drawingThread = Thread(drawingRunnable)
        surfaceHandler(drawingThread)
    }

    fun surfaceHandler(drawingThread: Thread) {
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                // start the drawing thread when the surface becomes valid
                drawingThread.start()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }
        })
    }

    // on cell touch reverse its state
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val cellx = (event.x.toInt() / drawingRunnable.cellSize).toInt()
            val celly = (event.y.toInt() / drawingRunnable.cellSize).toInt()
            val grid = drawingRunnable.grid
            grid[cellx][celly].isAlive = !grid[cellx][celly].isAlive
            drawingRunnable.canvas = holder.lockCanvas()
            for (x in 0 until drawingRunnable.numberOfColumns) {
                for (y in 0 until drawingRunnable.numberOfRows) {
                    val cell = drawingRunnable.grid[x][y]
                    drawingRunnable.draw(cell)
                }
            }
            holder.unlockCanvasAndPost(drawingRunnable.canvas)
        }
        return super.onTouchEvent(event)
    }

    inner class DrawingRunnable : Runnable, Serializable {

        var threadIsStopped = false
        var outsideThreadIsStopped = false

        // all grids
        var nextGrid = arrayOf<Array<Cell>>()
        var returnedGrid = arrayOf<Array<Cell>>()
        var grid = arrayOf<Array<Cell>>()

        lateinit var canvas: Canvas

        // default cell/grid colours
        var cellColour: Int = Color.CYAN
        var gridColour: Int = Color.DKGRAY

        var numberOfColumns = 0
        var numberOfRows = 0
        var simulationSpeed: Long = 50

        // default cell size
        var cellSize = 25f

        override fun run() {

            // try to find a cell size that fits perfectly on the screen
            for (i in 20..25)
                if (height % i == 0 && width % i == 0)
                    cellSize = i.toFloat()

            // lock canvas in order to draw on it
            canvas = holder.lockCanvas()

            // find out how many columns and rows the screen can fit based on cell size
            numberOfColumns = Math.round(width / cellSize)
            numberOfRows = Math.round(height / cellSize)

            // if there isn't a previously drawn grid then draw a random one
            // if there is draw that one
            grid = arrayOf()
            if (returnedGrid.isEmpty()) {
                // create the first grid with random cell states
                val random = Random
                for (x in 0 until numberOfColumns) {
                    var cells = arrayOf<Cell>()
                    for (y in 0 until numberOfRows) {
                        val cell = Cell(x.toFloat(), y.toFloat(), random.nextBoolean())
                        cells += cell
                        draw(cell)
                    }
                    grid += cells
                }
            } else {
                grid = returnedGrid
                for (x in 0 until numberOfColumns) {
                    for (y in 0 until numberOfRows) {
                        val cell = grid[x][y]
                        draw(cell)
                    }
                }
                returnedGrid = arrayOf()
            }

            // unlock canvas and display the drawn grid
            holder.unlockCanvasAndPost(canvas)

            while (!outsideThreadIsStopped) {
                while (!threadIsStopped) {

                    // add delay to the simulation to improve performance
                    Thread.sleep(simulationSpeed)

                    canvas = holder.lockCanvas()

                    // create a grid for the next generation based on the currently drawn one
                    nextGrid = arrayOf()
                    for (x in 0 until numberOfColumns) {
                        var emptyCells = arrayOf<Cell>()
                        for (y in 0 until numberOfRows) {
                            emptyCells += (Cell(x.toFloat(), y.toFloat(), false))
                        }
                        nextGrid += emptyCells
                    }

                    // loop through the current generation and count neighbours to generate the next generation
                    for (x in 0 until numberOfColumns) {
                        for (y in 0 until numberOfRows) {
                            val cell = grid[x][y]
                            val nextGridCell = nextGrid[x][y]

                            // count number of neighbours around cell
                            val neighbours = numberOfNeighbours(cell, grid)

                            // game of life rules
                            if ((neighbours == 2 || neighbours == 3) && cell.isAlive)
                                nextGridCell.isAlive = true
                            if (neighbours < 2 || neighbours > 3)
                                nextGridCell.isAlive = false
                            if (neighbours == 3 && !cell.isAlive)
                                nextGridCell.isAlive = true

                            draw(nextGridCell)
                        }
                    }

                    // reference the current generation grid to the next one
                    grid = nextGrid

                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }

        fun stopresume() {
            threadIsStopped = !threadIsStopped
        }

        fun stop() {
            threadIsStopped = true
        }

        fun speedup() {
            val maxSpeed: Long = 20
            if (simulationSpeed == maxSpeed) {
                Toast.makeText(context, "Maximum simulation speed reached!", Toast.LENGTH_SHORT).show()
            } else {
                simulationSpeed -= 10
            }
        }

        fun slowdown() {
            val minSpeed: Long = 80
            if (simulationSpeed == minSpeed) {
                Toast.makeText(context, "Minimum simulation speed reached!", Toast.LENGTH_SHORT).show()
            } else {
                simulationSpeed += 10
            }
        }

        fun reset() {
            // pause simulation
            if (!threadIsStopped)
                threadIsStopped = true

            canvas = holder.lockCanvas()

            // draw an empty grid
            var tempGrid = arrayOf<Array<Cell>>()
            for (x in 0 until numberOfColumns) {
                var emptyCells = arrayOf<Cell>()
                for (y in 0 until numberOfRows) {
                    val cell = Cell(x.toFloat(), y.toFloat(), false)
                    emptyCells += cell
                    draw(cell)
                }
                tempGrid += emptyCells
            }

            grid = tempGrid

            holder.unlockCanvasAndPost(canvas)
        }

        fun random() {
            canvas = holder.lockCanvas()

            // draw a random grid
            var tempGrid = arrayOf<Array<Cell>>()
            val random = Random
            for (x in 0 until numberOfColumns) {
                var cells = arrayOf<Cell>()
                for (y in 0 until numberOfRows) {
                    val cell = Cell(x.toFloat(), y.toFloat(), random.nextBoolean())
                    cells += cell
                    draw(cell)
                }
                tempGrid += cells
            }

            grid = tempGrid
            holder.unlockCanvasAndPost(canvas)
        }

        fun draw(cell: Cell) {
            val paint = Paint()
            paint.color = if (cell.isAlive) cellColour else gridColour
            canvas.drawRect(
                cell.x * cellSize + 1,
                cell.y * cellSize + 1,
                cell.x * cellSize + cellSize - 1,
                cell.y * cellSize + cellSize - 1,
                paint
            )
        }

        private fun numberOfNeighbours(cell: Cell, grid: Array<Array<Cell>>): Int {
            var count = 0
            val cellX = cell.x.toInt()
            val cellY = cell.y.toInt()

            // loop around the cell
            for (x in -1..1) {
                // if on the left edge and x == -1
                if (cellX == 0 && x == -1)
                    continue
                // if on the right edge and x == +1
                if (cellX == grid.size - 1 && x == 1)
                    continue

                for (y in -1..1) {
                    // if on the top edge and y == -1
                    if (cellY == 0 && y == -1)
                        continue
                    // if on the bottom edge and y == +1
                    if (cellY == grid[0].size - 1 && y == 1)
                        continue
                    // skip counting own cell
                    if (x == 0 && y == 0)
                        continue

                    if (grid[cellX + x][cellY + y].isAlive)
                        count++

                }
            }
            return count
        }

    }
}




