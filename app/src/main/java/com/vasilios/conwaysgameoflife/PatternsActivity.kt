package com.vasilios.conwaysgameoflife

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.widget.Toast
import java.lang.Exception
import java.security.AccessController.getContext


class PatternsActivity : AppCompatActivity() {

    lateinit var grid: Array<Array<Cell>>
    var cellColour: Int = 0
    var gridColour: Int = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patterns)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""

        val extras = intent.extras
        grid = extras.get("grid") as Array<Array<Cell>>
        cellColour = extras.get("cellColour") as Int
        gridColour = extras.get("gridColour") as Int

        val patternNames = arrayOf(
            "Block",
            "Boat",
            "Pulsar",
            "Kok's Galaxy",
            "Glider",
            "Gosper Glider Gun"
        )

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(patternNames)

        // set recyclerview layout and adapter
        recyclerView = findViewById<RecyclerView>(R.id.patterns_recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // add recycler view item separation line
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        recyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val rows = grid.size
                val columns = grid[0].size
                val middleCellX = rows / 2
                val middleCellY = columns / 2

                // empty grid
                for (row in grid) {
                    for (cell in row) {
                        cell.isAlive = false
                    }
                }

                try {
                    // draw pattern
                    when (patternNames[position]) {
                        "Block" -> {
                            for (x in 0..1)
                                for (y in 0..1)
                                    grid[middleCellX + x][middleCellY + y].isAlive = true
                        }
                        "Boat" -> {
                            grid[middleCellX][middleCellY].isAlive = true
                            grid[middleCellX - 1][middleCellY].isAlive = true
                            grid[middleCellX - 1][middleCellY + 1].isAlive = true
                            grid[middleCellX][middleCellY + 2].isAlive = true
                            grid[middleCellX + 1][middleCellY + 1].isAlive = true
                        }
                        "Pulsar" -> {
                            grid[middleCellX][middleCellY].isAlive = true
                            grid[middleCellX + 1][middleCellY].isAlive = true
                            grid[middleCellX + 2][middleCellY].isAlive = true
                            grid[middleCellX][middleCellY + 1].isAlive = true
                            grid[middleCellX + 1][middleCellY + 1].isAlive = true
                            grid[middleCellX - 1][middleCellY + 1].isAlive = true
                        }
                        "Kok's Galaxy" -> {
                            for (x in 0..5)
                                for (y in 0..1)
                                    grid[middleCellX + x][middleCellY + y].isAlive = true
                            for (x in -3..2)
                                for (y in 7..8)
                                    grid[middleCellX + x][middleCellY + y].isAlive = true
                            for (x in -3..-2)
                                for (y in 0..5)
                                    grid[middleCellX + x][middleCellY + y].isAlive = true
                            for (x in 4..5)
                                for (y in 3..8)
                                    grid[middleCellX + x][middleCellY + y].isAlive = true
                        }
                        "Glider" -> {
                            grid[middleCellX][middleCellY].isAlive = true
                            grid[middleCellX + 1][middleCellY].isAlive = true
                            grid[middleCellX - 1][middleCellY].isAlive = true
                            grid[middleCellX + 1][middleCellY - 1].isAlive = true
                            grid[middleCellX][middleCellY - 2].isAlive = true
                        }
                        "Gosper Glider Gun" -> {
                            grid[middleCellX - 16][middleCellY].isAlive = true
                            grid[middleCellX - 17][middleCellY].isAlive = true
                            grid[middleCellX - 16][middleCellY - 1].isAlive = true
                            grid[middleCellX - 17][middleCellY - 1].isAlive = true

                            grid[middleCellX][middleCellY].isAlive = true
                            grid[middleCellX - 1][middleCellY].isAlive = true
                            grid[middleCellX - 1][middleCellY - 1].isAlive = true
                            grid[middleCellX - 1][middleCellY + 1].isAlive = true
                            grid[middleCellX - 2][middleCellY - 2].isAlive = true
                            grid[middleCellX - 2][middleCellY + 2].isAlive = true
                            grid[middleCellX - 3][middleCellY].isAlive = true

                            grid[middleCellX - 7][middleCellY].isAlive = true
                            grid[middleCellX - 7][middleCellY - 1].isAlive = true
                            grid[middleCellX - 7][middleCellY + 1].isAlive = true
                            grid[middleCellX - 6][middleCellY - 2].isAlive = true
                            grid[middleCellX - 6][middleCellY + 2].isAlive = true
                            grid[middleCellX - 5][middleCellY - 3].isAlive = true
                            grid[middleCellX - 4][middleCellY - 3].isAlive = true
                            grid[middleCellX - 5][middleCellY + 3].isAlive = true
                            grid[middleCellX - 4][middleCellY + 3].isAlive = true

                            grid[middleCellX + 3][middleCellY - 3].isAlive = true
                            grid[middleCellX + 3][middleCellY - 2].isAlive = true
                            grid[middleCellX + 3][middleCellY - 1].isAlive = true
                            grid[middleCellX + 4][middleCellY - 3].isAlive = true
                            grid[middleCellX + 4][middleCellY - 2].isAlive = true
                            grid[middleCellX + 4][middleCellY - 1].isAlive = true

                            grid[middleCellX + 5][middleCellY - 4].isAlive = true
                            grid[middleCellX + 5][middleCellY].isAlive = true

                            grid[middleCellX + 7][middleCellY - 4].isAlive = true
                            grid[middleCellX + 7][middleCellY - 5].isAlive = true
                            grid[middleCellX + 7][middleCellY].isAlive = true
                            grid[middleCellX + 7][middleCellY + 1].isAlive = true

                            grid[middleCellX + 17][middleCellY - 3].isAlive = true
                            grid[middleCellX + 18][middleCellY - 3].isAlive = true
                            grid[middleCellX + 17][middleCellY - 2].isAlive = true
                            grid[middleCellX + 18][middleCellY - 2].isAlive = true
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@PatternsActivity, "Screen size too small to draw pattern!", Toast.LENGTH_SHORT).show()
                }

                changeActivity()
            }
        })
    }

    fun changeActivity() {
        // pass extras to next activity
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("grid", grid)
            putExtra("cellColour", cellColour)
            putExtra("gridColour", gridColour)
        }
        startActivity(intent)
        this.finish()
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            changeActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        changeActivity()
    }
}

// custom recyclerview adapter
class MyAdapter(private val patternNames: Array<String>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyAdapter.MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.pattern_row, parent, false) as TextView
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = patternNames[position]
    }

    override fun getItemCount() = patternNames.size
}