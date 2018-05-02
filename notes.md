# TODO list
* UI improvements (listing screen centre alignments etc)
* pre-fetching images 
* category partial load (performance tweaks)
* persistency on configuration changes
* listing recycler view reaching end of the list



# crashes
1. only happened once, not able to reproduce
```
2018-05-02 17:41:21.498 22561-22561/com.example.derek.trademeapi E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.example.derek.trademeapi, PID: 22561
    java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder{32d7261 position=171 id=-1, oldPos=170, pLpos:170 scrap [attachedScrap] tmpDetached no parent} android.support.v7.widget.RecyclerView{5e0d493 VFED..... .F....I. 0,0-1080,1863 #7f080086 app:id/recycler_view}, adapter:com.example.derek.trademeapi.ui.listings.ListingActivity$Adapter@9c6c16b, layout:android.support.v7.widget.GridLayoutManager@c5124c8, context:com.example.derek.trademeapi.ui.listings.ListingActivity@bcf1ddb
        at android.support.v7.widget.RecyclerView$Recycler.validateViewHolderForOffsetPosition(RecyclerView.java:5610)
        at android.support.v7.widget.RecyclerView$Recycler.tryGetViewHolderForPositionByDeadline(RecyclerView.java:5792)
        at android.support.v7.widget.RecyclerView$Recycler.getViewForPosition(RecyclerView.java:5752)
        at android.support.v7.widget.RecyclerView$Recycler.getViewForPosition(RecyclerView.java:5748)
        at android.support.v7.widget.LinearLayoutManager$LayoutState.next(LinearLayoutManager.java:2232)
        at android.support.v7.widget.GridLayoutManager.layoutChunk(GridLayoutManager.java:556)
        at android.support.v7.widget.LinearLayoutManager.fill(LinearLayoutManager.java:1519)
        at android.support.v7.widget.LinearLayoutManager.onLayoutChildren(LinearLayoutManager.java:614)
        at android.support.v7.widget.GridLayoutManager.onLayoutChildren(GridLayoutManager.java:170)
        at android.support.v7.widget.RecyclerView.dispatchLayoutStep1(RecyclerView.java:3763)
        at android.support.v7.widget.RecyclerView.dispatchLayout(RecyclerView.java:3527)
        at android.support.v7.widget.RecyclerView.onLayout(RecyclerView.java:4082)
        at android.view.View.layout(View.java:19659)
        at android.view.ViewGroup.layout(ViewGroup.java:6075)
        at android.support.design.widget.HeaderScrollingViewBehavior.layoutChild(HeaderScrollingViewBehavior.java:132)
        at android.support.design.widget.ViewOffsetBehavior.onLayoutChild(ViewOffsetBehavior.java:42)
        at android.support.design.widget.AppBarLayout$ScrollingViewBehavior.onLayoutChild(AppBarLayout.java:1361)
        at android.support.design.widget.CoordinatorLayout.onLayout(CoordinatorLayout.java:894)
        at android.view.View.layout(View.java:19659)
        at android.view.ViewGroup.layout(ViewGroup.java:6075)
        at android.widget.FrameLayout.layoutChildren(FrameLayout.java:323)
        at android.widget.FrameLayout.onLayout(FrameLayout.java:261)
        at android.view.View.layout(View.java:19659)
        at android.view.ViewGroup.layout(ViewGroup.java:6075)
        at android.widget.LinearLayout.setChildFrame(LinearLayout.java:1791)
        at android.widget.LinearLayout.layoutVertical(LinearLayout.java:1635)
        at android.widget.LinearLayout.onLayout(LinearLayout.java:1544)
        at android.view.View.layout(View.java:19659)
        at android.view.ViewGroup.layout(ViewGroup.java:6075)
        at android.widget.FrameLayout.layoutChildren(FrameLayout.java:323)
        at android.widget.FrameLayout.onLayout(FrameLayout.java:261)
        at android.view.View.layout(View.java:19659)
        at android.view.ViewGroup.layout(ViewGroup.java:6075)
        at android.widget.LinearLayout.setChildFrame(LinearLayout.java:1791)
        at android.widget.LinearLayout.layoutVertical(LinearLayout.java:1635)
        at android.widget.LinearLayout.onLayout(LinearLayout.java:1544)
        at android.view.View.layout(View.java:19659)
        at android.view.ViewGroup.layout(ViewGroup.java:6075)
        at android.widget.FrameLayout.layoutChildren(FrameLayout.java:323)
        at android.widget.FrameLayout.onLayout(FrameLayout.java:261)
        at com.android.internal.policy.DecorView.onLayout(DecorView.java:761)
        at android.view.View.layout(View.java:19659)
        at android.view.ViewGroup.layout(ViewGroup.java:6075)
        at android.view.ViewRootImpl.performLayout(ViewRootImpl.java:2496)
        at android.view.ViewRootImpl.performTraversals(ViewRootImpl.java:2212)
        at android.view.ViewRootImpl.doTraversal(ViewRootImpl.java:1392)
        at android.view.ViewRootImpl$TraversalRunnable.run(ViewRootImpl.java:6752)
2018-05-02 17:41:21.499 22561-22561/com.example.derek.trademeapi E/AndroidRuntime:     at android.view.Choreographer$CallbackRecord.run(Choreographer.java:911)
        at android.view.Choreographer.doCallbacks(Choreographer.java:723)
        at android.view.Choreographer.doFrame(Choreographer.java:658)
        at android.view.Choreographer$FrameDisplayEventReceiver.run(Choreographer.java:897)
        at android.os.Handler.handleCallback(Handler.java:790)
        at android.os.Handler.dispatchMessage(Handler.java:99)
        at android.os.Looper.loop(Looper.java:164)
        at android.app.ActivityThread.main(ActivityThread.java:6499)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:442)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:807)
        at de.robv.android.xposed.XposedBridge.main(XposedBridge.java:108)
```

