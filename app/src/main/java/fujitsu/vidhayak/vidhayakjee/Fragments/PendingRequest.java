package fujitsu.vidhayak.vidhayakjee.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fujitsu.vidhayak.vidhayakjee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingRequest extends Fragment {


    public PendingRequest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_request, container, false);
    }

}
