package fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.experia.experia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    Uri profilePhotoUrl;
    String displayName;
    String email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth !=null)
        {
            user = auth.getCurrentUser();
            profilePhotoUrl = auth.getCurrentUser().getPhotoUrl();
            Toast.makeText(getContext(), "Profile Url: " + profilePhotoUrl.toString(), Toast.LENGTH_LONG).show();
            displayName = user.getDisplayName();
            email = user.getEmail();
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }
}
