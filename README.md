# StateLayout

StateLayout is a custom view. It is a wrapper around FrameLayout that makes it more convenient to toggle between different `ViewState`s.
 
A state is rendered by simply calling `showState()` and passing an instance of `ViewState`. This will hide all currently visible view states and display the new one.
 
`ViewState`s are lazily inflated/constructed when they need to be shown using `ViewState.onCreateView()`. Since this is a potentially expensive operation, `StateLayout` has the ability to re-use views between different `ViewState`s.
