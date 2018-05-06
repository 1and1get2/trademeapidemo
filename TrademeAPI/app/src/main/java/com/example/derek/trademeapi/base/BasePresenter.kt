package com.example.derek.trademeapi.base

import android.os.Bundle

interface BasePresenter<T : MVPView> : Presenter{
//abstract class BasePresenter<out T : BasePresenter, out V : BaseView<T>>(protected val view: V) {
//    init {
//        inject()
//    }

    var view : T?

    /**
     * This method may be called when the presenter view is created
     */
    fun onViewCreated(){}

    /**
     * This method may be called when the presenter view is destroyed
     */
    fun onViewDestroyed(){}

    /**
     * Injects the required dependencies
     */
//    private fun inject() {
//        //TODO: Implement this method
//    }
}


/**
 * A presenter that defines its own lifecycle methods.
 */
interface Presenter {

    /**
     * Starts the presentation. This should be called in the view's (Activity or Fragment)
     * onCreate() or onViewStatedRestored() method respectively.
     *
     * @param savedInstanceState the saved instance state that contains state saved in
     *  [onSaveInstanceState].
     */
    fun onStart(savedInstanceState: Bundle?) = Unit

    /**
     * Resumes the presentation. This should be called in the view's (Activity or Fragment)
     * onResume() method.
     */
    fun onResume() = Unit

    /**
     * Pauses the presentation. This should be called in the view's Activity or Fragment)
     * onPause() method.
     */
    fun onPause() = Unit

    /**
     * Save the state of the presentation (if any). This should be called in the view's
     * (Activity or Fragment) onSaveInstanceState().
     *
     * @param outState the out state to save instance state
     */
    fun onSaveInstanceState(outState: Bundle) = Unit

    /**
     * Ends the presentation. This should be called in the view's (Activity or Fragment)
     * onDestroy() or onDestroyView() method respectively.
     */
    fun onEnd() = Unit
}
