package com.example.concentration.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.concentration.Info.User;
import com.example.concentration.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private TextView userName, userEmail;
    private ImageView userPic;
    private GoogleSignInClient mGoogleSignInClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button signOutButton = view.findViewById(R.id.sign_out_button);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        userPic = view.findViewById(R.id.profile_image);

        initFireBaseGoogleSignIn();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateUI();
    }


    private void initFireBaseGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }


    private void updateUI() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());

        if (acct != null) {
            String personGivenName = acct.getGivenName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            userName.setText(String.valueOf(personGivenName));
            userEmail.setText(String.valueOf(personEmail));

            if (personPhoto == null) {
                userPic.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background));
            } else {
                Glide.with(ProfileFragment.this.getActivity())
                        .load(personPhoto)
                        .into(userPic);
            }
            userPic.setVisibility(View.VISIBLE);
        } else {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SignInFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }


    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                Toast.makeText(getActivity(), "Signed out Successfully", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignInFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
