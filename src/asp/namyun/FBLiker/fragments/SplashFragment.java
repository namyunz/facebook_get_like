package asp.namyun.FBLiker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import asp.namyun.FBLiker.R;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash,container,false);

//        LoginButton authButton=(LoginButton)view.findViewById(R.id.authButton);
//        authButton.setFragment(this);
//        authButton.setReadPermissions(Arrays.asList("user_likes"));

        return view;
    }
}