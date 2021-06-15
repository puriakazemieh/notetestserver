package fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.notetestserver.LoginActivity;
import com.example.notetestserver.R;

import app.*;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    TextView email, fname, lname;
    Button logout;
    CircleImageView imageView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return init(view);
    }

    private View init(View view) {
        email = view.findViewById(R.id.email);
        fname = view.findViewById(R.id.fname);
        lname = view.findViewById(R.id.lname);
        imageView = view.findViewById(R.id.imageView);
        logout = view.findViewById(R.id.ok);
        email.setText(spref.get().getString(ROUTER.INPUT_EMAIL, ""));
        fname.setText(spref.get().getString(ROUTER.INPUT_FNAME, ""));
        lname.setText(spref.get().getString(ROUTER.INPUT_LNAME, ""));


        Glide.with(Application.getContext())
                .load(app.main.url + spref.get().getString("image", ""))
                .into(imageView);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("logout");
                alert.setMessage("log out ???");
                alert.setPositiveButton("ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spref.get().edit().clear().apply();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                });
                alert.setNegativeButton("nein", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();

            }
        });
        return view;
    }
}