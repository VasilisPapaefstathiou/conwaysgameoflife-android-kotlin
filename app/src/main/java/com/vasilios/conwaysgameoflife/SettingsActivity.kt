package com.vasilios.conwaysgameoflife

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast


class SettingsActivity : AppCompatActivity() {

    lateinit var builder: AlertDialog.Builder
    lateinit var dialog: AlertDialog
    lateinit var grid: Array<Array<Cell>>
    private lateinit var colours: Array<Int>
    lateinit var colourNames: Array<String>
    var cellColour: Int = 0
    var gridColour: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val listView = findViewById<ListView>(R.id.settingsList)
        listView.adapter = CustomListViewAdapter(this)

        supportActionBar!!.title = ""

        // set back button on action bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // get extras from previous activity
        val extras = intent.extras
        grid = extras.get("grid") as Array<Array<Cell>>
        cellColour = extras.get("cellColour") as Int
        gridColour = extras.get("gridColour") as Int

        // array of colours that the user can choose from
        colours = arrayOf(
            Color.RED,
            Color.CYAN,
            Color.YELLOW,
            Color.DKGRAY,
            Color.GREEN,
            Color.BLUE,
            Color.WHITE,
            Color.BLACK
        )
        colourNames = arrayOf("Red", "Cyan", "Yellow", "Dark Grey", "Green", "Blue", "White", "Black")

        listViewClickHandler(listView)
    }

    private fun listViewClickHandler(listView: ListView) {
        listView.setOnItemClickListener { _, _, position, _ ->

            // build the alert dialog
            builder = this.let { AlertDialog.Builder(it) }

            when (position) {
                0 -> {
                    builder.setTitle("Select Cell Colour: ")
                        .setItems(colourNames) { _, which ->
                            cellColour = colours[which]
                        }
                    dialog = builder.create()
                    dialog.show()
                }

                1 -> {
                    builder.setTitle("Select Grid Colour: ")
                        .setItems(
                            colourNames
                        ) { _, which ->
                            gridColour = colours[which]
                        }
                    dialog = builder.create()
                    dialog.show()
                }

                2 -> {
                    val uri = Uri.parse("market://details?id=$packageName")
                    val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
                    try {
                        startActivity(myAppLinkToMarket)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show()
                    }
                }
                3 -> {

                    val mailto = "mailto:developer@example.org" +
                            "?cc=" + "" +
                            "&subject=" + Uri.encode("Conway's Game of Life App Feedback") +
                            "&body=" + Uri.encode("")

                    val emailIntent = Intent(Intent.ACTION_SENDTO)
                    emailIntent.data = Uri.parse(mailto)

                    try {
                        startActivity(emailIntent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(this, "No default e-mail app found", Toast.LENGTH_SHORT).show()
                    }
                }
                4 -> {
                    builder.setTitle("About Conway's Game of Life")
                        .setMessage(
                            "The universe of the Game of Life is an infinite, two-dimensional orthogonal grid of square cells, each of which is in one of two possible states, alive or dead, (or populated and unpopulated, respectively). Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent. At each step in time, the following transitions occur:\n" +
                                    "\n" +
                                    "Rules:\n" +
                                    "1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.\n" +
                                    "2. Any live cell with two or three live neighbours lives on to the next generation.\n" +
                                    "3. Any live cell with more than three live neighbours dies, as if by overpopulation.\n" +
                                    "4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction."
                        )
                        .apply {
                            setNegativeButton(
                                "DISMISS"
                            ) { _, _ -> }
                        }
                    dialog = builder.create()
                    dialog.show()
                }
                5 -> {
                    builder.setTitle("About the App")
                        .setMessage(
                            "Version: 1.0\n" +
                                    "Developer: Vasilios Papaefstathiou\n" +
                                    "Email: "
                        )
                        .apply {
                            setNegativeButton(
                                "DISMISS"
                            ) { _, _ -> }
                        }
                    dialog = builder.create()
                    dialog.show()
                }
            }

        }
    }

    override fun onBackPressed() {
        changeActivity()
    }

    fun changeActivity() {
        // give extras to next activity
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("grid", grid)
            putExtra("cellColour", cellColour)
            putExtra("gridColour", gridColour)
        }
        startActivity(intent)
        this.finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            changeActivity()
        }
        return super.onOptionsItemSelected(item)
    }
}

class CustomListViewAdapter(context: Context) : BaseAdapter() {

    private val ctx: Context = context

    private val settings = arrayListOf(
        "Change Cell Colour",
        "Change Grid Colour",
        "Rate the App",
        "Contact",
        "About Conway's Game of Life",
        "About the App"
    )

    override fun getCount(): Int {
        return settings.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return ""
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(ctx)
        val rowMain = layoutInflater.inflate(R.layout.settings_row, viewGroup, false)

        val nameTextView = rowMain.findViewById<TextView>(R.id.name_textView)

        nameTextView.text = settings[position]

        return rowMain
    }


}
