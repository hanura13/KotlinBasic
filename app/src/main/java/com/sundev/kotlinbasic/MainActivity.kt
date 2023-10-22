package com.sundev.kotlinbasic

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has been drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // === DO NOT ALTER THE CODE IN THE FOLLOWING IF STATEMENT ===
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === END IF STATEMENT ===

        lemonImage = findViewById(R.id.imageTree)

        lemonImage!!.setOnClickListener {clickLemonImage()}

        lemonImage!!.setOnLongClickListener {
            showSnackBar()
        }
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    private fun clickLemonImage() {

        when(lemonadeState){
            "select" -> lemonadeState = SQUEEZE
        }
        if(lemonSize == -1  && lemonadeState == "squeeze"){
            lemonSize = lemonTree.pick()
        }

        if(lemonSize != 0 && lemonadeState == "squeeze"){
            Toast.makeText(this, "Lemon Size: $lemonSize", Toast.LENGTH_SHORT).show()
            lemonSize -= 1
            squeezeCount +=1

        }else if(lemonSize ==0 && lemonadeState == "squeeze"){
            lemonSize -= 1
            squeezeCount = 0
            lemonadeState = DRINK
        }
        else if(lemonadeState == DRINK && lemonSize == -1) {
            lemonadeState = RESTART
        } else{
            lemonadeState = SELECT
        }

        return setViewElements()
    }

    /**
     * Set up the view elements according to the state.
     */
    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.textView)
        val TextAction = when(lemonadeState) {
            "select" -> getString(R.string.lemon_select)
            "squeeze" -> getString(R.string.lemon_squeeze)
            "drink" -> getString(R.string.lemon_drink)
            else -> getString(R.string.lemon_empty_glass)
        }
        textAction.text = TextAction

        val imageResource = when(lemonadeState) {
            "select" -> R.drawable.lemon_tree
            "squeeze" -> R.drawable.lemon_squeeze
            "drink" -> R.drawable.lemon_drink
            else -> R.drawable.lemon_restart
        }
        lemonImage!!.setImageResource(imageResource)
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackBar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}