/*
 * Copyright (C) 2020 Freeletics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.freeletics.statelayout

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.freeletics.statelayout.StateLayout

/**
 * Describes a view state that can be rendered by [StateLayout].
 *
 * [onCreateView] is responsible for inflating/constructing the [View] that should be shown when the
 * [ViewState] is displayed. [onBindView] should be used to bind any dynamic data to the [View]. The reason for this
 * separation is that [View]s can be reused between multiple [ViewState]s:
 * If two [ViewState]s share the same [id], [onCreateView] will only be called when the first [ViewState] is shown. For
 * the second one, the same [View] is reused and only attached to the new [ViewState] by calling [onBindView]. This
 * mechanism works similar to the one used in [RecyclerView.Adapter]s.
 *
 * A common use case for view re-usage is e.g. to define different error states (generic error, network error, etc)
 * that all share the same layout. When switching between these, the layout will only be inflated once and each
 * [ViewState] will just bind different data to it (e.g. set a different error message).
 *
 * An easy way to create a new [ViewState] is to use the convenient factory [ViewState.create]. It will simply inflate
 * its [View] from a given layout resource id.
 *
 * See [StateLayout] for some example usages.
 *
 * @see StateLayout
 */
interface ViewState {

    /**
     * Id of this [ViewState].
     *
     * This is used to determine if two [ViewState]s can share the same [View].
     */
    val id: Int

    /**
     * Construct/inflate the [View] hierarchy that should be shown when this [ViewState] is rendered.
     *
     * The [View] constructed here can be reused by multiple [ViewState]s. For that reason, any dynamic data
     * (e.g. error messages) should not be set here, but rather in [onBindView].
     */
    fun onCreateView(parent: StateLayout): View

    /**
     * Called by [StateLayout] when this [ViewState] is attached to a [View].
     *
     * This should be used to bind any dynamic data to the [View].
     */
    fun onBindView(view: View) {}

    companion object {
        /**
         * Convenient method to inflate a [ViewState] from a layout resource.
         *
         * The layout resource id will be used as [ViewState.id]. This means that all [ViewState] using the same layout
         * will share the same [View].
         */
        fun create(@LayoutRes layoutResId: Int, viewBinder: View.() -> Unit = {}): ViewState {
            return object : ViewState.Inflatable(layoutResId) {
                override fun onBindView(view: View) = view.viewBinder()
            }
        }
    }

    /**
     * Simple [ViewState] that inflates its [View] from a layout resource id.
     *
     * This is used by [ViewState.create].
     */
    open class Inflatable(
        @LayoutRes private val layoutResId: Int
    ) : ViewState {
        override val id: Int = layoutResId

        override fun onCreateView(parent: StateLayout): View =
            LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
    }
}
