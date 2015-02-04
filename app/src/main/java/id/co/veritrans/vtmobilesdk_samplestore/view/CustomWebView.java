package id.co.veritrans.vtmobilesdk_samplestore.view;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by muhammadanis on 2/4/15.
 */
public class CustomWebView extends WebView{

    public CustomWebView(Context context) {
        super(context);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                if (!hasFocus())
                    requestFocus();
                break;
        }

        return super.onTouchEvent(ev);
    }

}
