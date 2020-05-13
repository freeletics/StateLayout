[![CircleCI](https://circleci.com/gh/freeletics/StateLayout.svg?style=svg)](https://circleci.com/gh/freeletics/StateLayout)

[![Download](https://maven-badges.herokuapp.com/maven-central/com.freeletics.statelayout/statelayout/badge.svg) ](https://maven-badges.herokuapp.com/maven-central/com.freeletics.statelayout/statelayout)

# StateLayout

StateLayout is a custom view. It is a wrapper around FrameLayout that makes it more convenient to toggle between different `ViewState`s.
 
A state is rendered by simply calling `showState()` and passing an instance of `ViewState`. This will hide all currently visible view states and display the new one.
 
`ViewState`s are lazily inflated/constructed when they need to be shown using `ViewState.onCreateView()`. Since this is a potentially expensive operation, `StateLayout` has the ability to re-use views between different `ViewState`s.

# How to use

### 1. Add StateLayout view to XML layout
```xml
<com.freeletics.statelayout.StateLayout
    android:id="@+id/state_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```


### 2. Define custom states in code. 
For example, simple state can be defined using `ViewState.create()` fuction:
```kotlin
val loadingState = ViewState.create(R.layout.view_state_loading)
```
Or more complex states can be defined by extending `ViewState.Inflatable`:
```kotlin
class BindableContent(val text: String) : ViewState.Inflatable(R.layout.view_state_content) {
    override fun onBindView(view: View) {
        super.onBindView(view)
        val textView = view.findViewById<TextView>(R.id.text)
        textView.text = text
    }
}
...
val contentState = BindableContent("Content ready")
```


### 3. Apply required content
```kotlin
val stateLayout = findViewById<StateLayout>(R.id.state_layout)
stateLayout.showState(loadingState)
...
stateLayout.showState(contentState)
```


### 4. Define transition animation (optional)
It is possible to specify transition between the states. Transition can be defined with `TransitionInflater` and later passed as an optional parameter to the `showState()` method
```kotlin
val transition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_left)
transition.duration = 500
stateLayout.showState(contentState, transition)
```

Check [example](https://github.com/freeletics/StateLayout/tree/master/example) app to see how it works.


# Dependency
Dependencies are hosted on Maven Central:

```groovy
implementation 'com.freeletics.statelayout:statelayout:1.0.0'
```

### Snapshot
Latest snapshot (directly published from master branch):

```groovy
allprojects {
    repositories {
        // Your repositories.
        // ...
        // Add url to snapshot repository
        maven {
            url "https://oss.sonatype.org/content/repositories/snapshots/"
        }
    }
}

```

```groovy
implementation 'com.freeletics.statelayout:statelayout:1.0.1-SNAPSHOT'
```

# License

```
Copyright 2020 Freeletics

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
