package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.experia.experia.R;
import com.experia.experia.activities.MainActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by doc_dungeon on 8/23/16.
 */
public class YouTubeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_youtube, parent, false);

        YouTubePlayerSupportFragment youTubePlayerSupportFragment =
                (YouTubePlayerSupportFragment) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.youtubesupportfragment);

        /* there is a bug when trying to inflate a youtubesupportfragment
        *  the code below uses an empty framelayout for inflation then replaces it with
        *  a youtubesupportfragment */
        if (youTubePlayerSupportFragment == null) {
            Toast.makeText(getContext(),"ytspf is null",Toast.LENGTH_LONG).show();
            youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.youtubesupportfragment, youTubePlayerSupportFragment).commit();

        }

        youTubePlayerSupportFragment.initialize("AIzaSyDAZFdIt6WG02cloYuSO-mlSevZVtR2sWg", new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean isRecovered) {
                if (!isRecovered) {
                    Toast.makeText(getContext(),"Inside Success",Toast.LENGTH_LONG).show();
                    youTubePlayer.cueVideo("dSajQAGo6x8");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getContext(),"Failure",Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }


}
