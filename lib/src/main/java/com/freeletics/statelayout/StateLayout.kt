package com.freeletics.statelayout

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager

/**
 * A wrapper around [FrameLayout] that makes it more convenient to toggle between different [ViewState]s.
 *
 * A state is rendered by simply calling [showState] and passing an instance of [ViewState]. This will hide all
 * currently visible view states and display the new one.
 *
 * [ViewState]s are lazily inflated/constructed when they need to be shown using [ViewState.onCreateView]. Since this
 * is a potentially expensive operation, [StateLayout] has the ability to re-use views between different [ViewState]s.
 * See the [ViewState] documentation for more details.
 *
 * @see ViewState
 */
class StateLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    /**
     * The default transition to use when animating view state changes.
     */
    private val transition: Transition

    private val stateViews = SparseArray<View>()
    private var activeViewState: ViewState? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StateLayout)

        val transitionId =
            a.getResourceId(R.styleable.StateLayout_transition, android.R.transition.no_transition)
        transition = TransitionInflater.from(context).inflateTransition(transitionId)

        a.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        check(childCount <= 0) { "StateLayout should not contain any children" }
    }

    /**
     * Show a new [ViewState].
     *
     * The previously visible [ViewState] will be removed from StateLayout, but still cached.
     * If [transition] is given, it will be used to animate between the old and the new [ViewState].
     *
     * @param viewState New [ViewState] to be shown
     * @param transition Transition behaviour
     */
    @JvmOverloads
    fun showState(viewState: ViewState, transition: Transition = this.transition) {
        if (viewState == activeViewState) return

        // Check if a view with same id exists. If not, create a new one and add it to the map.
        val view = stateViews.get(viewState.id) ?: viewState.onCreateView(this).also {
            stateViews.put(viewState.id, it)
        }

        val parent = view.parent
        if (parent is ViewGroup) {
            parent.removeView(view)
        }

        val scene = Scene(this, view)
        scene.setEnterAction {
            viewState.onBindView(view)
            // not sure why this is needed
            view.visibility = View.VISIBLE
        }
        TransitionManager.endTransitions(this)
        TransitionManager.go(scene, transition)

        activeViewState = viewState
    }
}
