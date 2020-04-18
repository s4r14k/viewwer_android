package com.priscilla.viewwer.Fragment;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.net.Uri;
        import android.os.Bundle;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;

        import android.preference.PreferenceManager;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.webkit.CookieManager;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;

        import com.priscilla.viewwer.R;
        import com.priscilla.viewwer.activity.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment  extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_post, container, false);

        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        new BaseActivity().toolbarFragment(rootView,R.id.MessageToolbar,R.string.title_posts,((AppCompatActivity)getActivity()),getActivity());

        BaseActivity.showLoadingView(getContext());

        final WebView webView = (WebView) rootView.findViewById(R.id.webViewMessage);
        //final WebView webView = (WebView) rootView.findViewById(R.id.nav_view);

        webView.getSettings().setJavaScriptEnabled(true);

        CookieManager cookieManager = CookieManager.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
       String cookies =  sharedPreferences.getString(BaseActivity.PREFS_COOKIES, "");
        cookieManager.setCookie(BaseActivity.URL_API_SERVER + "/dashboard/messages",cookies);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(BaseActivity.URL_API_SERVER + "/dashboard/messages");
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                "var head = document.getElementsByTagName('header')[0];"
                + "head.parentNode.removeChild(head);" +
               "})()");
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('container')[0].style.top=0; })()");
                // do your stuff here

            }


        });
        BaseActivity.hideLoadingView();

        return rootView;
    }






}
