package com.freeletics.statelayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.transition.Transition
import android.view.View
import android.widget.TextView
import androidx.transition.TransitionInflater

class MainActivity : AppCompatActivity() {

    private lateinit var stateLayout: StateLayout

    private val loadingState = ViewState.create(R.layout.view_state_loading)
    private val contentState = BindableContent("Content ready")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stateLayout = findViewById(R.id.state_layout)
        stateLayout.showState(loadingState)

        Handler().postDelayed({
            val transition = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_left)
            transition.duration = 200
            stateLayout.showState(contentState, transition)
        }, 2000)
    }

    private inner class BindableContent(
        val text: String
    ) : ViewState.Inflatable(R.layout.view_state_content) {
        override fun onBindView(view: View) {
            super.onBindView(view)
            val textView = view.findViewById<TextView>(R.id.text)
            textView.text = text
        }
    }
}
