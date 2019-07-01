package com.vasilios.conwaysgameoflife

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    lateinit var simulationView: SimulationView
    lateinit var drawingRunnable: SimulationView.DrawingRunnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // get the view and the thread that draws on the grid
        simulationView = findViewById(R.id.myview)
        drawingRunnable = simulationView.drawingRunnable
        drawingRunnable.stop()

        // check if this activity has been started from another activity
        val extras = intent.extras
        if (extras != null) {
            // if yes then get the extras and set them on the view
            drawingRunnable.returnedGrid = extras.get("grid") as Array<Array<Cell>>
            drawingRunnable.cellColour = extras.get("cellColour") as Int
            drawingRunnable.gridColour = extras.get("gridColour") as Int
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_items, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.patterns -> {
                drawingRunnable.stop()
                // pass extras to next activity
                val i = Intent(applicationContext, PatternsActivity::class.java).apply {
                    putExtra("grid", drawingRunnable.grid)
                    putExtra("cellColour", drawingRunnable.cellColour)
                    putExtra("gridColour", drawingRunnable.gridColour)

                }
                startActivity(i)
                this.finish()
            }
            R.id.random -> drawingRunnable.random()
            R.id.slowdown -> drawingRunnable.slowdown()
            R.id.playpause -> drawingRunnable.stopresume()
            R.id.speedup -> drawingRunnable.speedup()
            R.id.reset -> drawingRunnable.reset()
            R.id.settings -> {
                // pause simulation when changing activities
                drawingRunnable.stop()
                // pass extras to next activity
                val i = Intent(applicationContext, SettingsActivity::class.java).apply {
                    putExtra("grid", drawingRunnable.grid)
                    putExtra("cellColour", drawingRunnable.cellColour)
                    putExtra("gridColour", drawingRunnable.gridColour)
                }
                startActivity(i)
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}
