package com.example.dayout_organizer.ui.fragments.trips.EditTrip;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.dayout_organizer.R;
import com.example.dayout_organizer.helpers.system.PermissionsHelper;
import com.example.dayout_organizer.helpers.view.ConverterImage;
import com.example.dayout_organizer.helpers.view.FN;
import com.example.dayout_organizer.helpers.view.NoteMessage;
import com.example.dayout_organizer.models.trip.TripData;
import com.example.dayout_organizer.models.trip.photo.TripPhotoModel;
import com.example.dayout_organizer.models.trip.TripModel;
import com.example.dayout_organizer.models.trip.create.CreateTripPhoto;
import com.example.dayout_organizer.ui.activities.MainActivity;
import com.example.dayout_organizer.ui.dialogs.ErrorDialog;
import com.example.dayout_organizer.ui.dialogs.LoadingDialog;
import com.example.dayout_organizer.ui.fragments.home.HomeFragment;
import com.example.dayout_organizer.viewModels.MVP;
import com.example.dayout_organizer.viewModels.TripViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dayout_organizer.config.AppConstants.MAIN_FRC;


public class EditImageTripFragment extends Fragment implements MVP {


    View view;
    @BindView(R.id.select_image_btn)
    Button selectImageButton;
    @BindView(R.id.select_img)
    ImageView selectImg;
    @BindView(R.id.previous_btn)
    Button previousButton;
    @BindView(R.id.next_btn)
    Button nextButton;
    @BindView(R.id.create_btn)
    Button createButton;
    @BindView(R.id.cancel_button)
    ImageButton cancelButton;


    List<Uri> uris;
    List<CreateTripPhoto.Photo> imageBase64;
    int uriIdx, downloadIdx;
    LoadingDialog loadingDialog;
    TripData data;
    CreateTripPhoto createTripPhoto;

    Handler downloadHandler;

    public EditImageTripFragment(TripData data) {
        this.data = data;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_create_image_trip, container, false);
        ButterKnife.bind(this, view);
        initView();
        TripViewModel.getINSTANCE().setMVPInstance(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startDownload();
    }

    @Override
    public void onStart() {
        ((MainActivity) requireActivity()).hideBottomBar();
        super.onStart();
    }

    private void initView() {

        initInfo();
        downloadHandler = new Handler(Looper.getMainLooper());

        loadingDialog = new LoadingDialog(requireContext());

        createTripPhoto = new CreateTripPhoto(data.id, imageBase64);
        selectImageButton.setOnClickListener(onSelectImageClicked);
        previousButton.setOnClickListener(onPreviousClicked);
        nextButton.setOnClickListener(onNextClicked);
        createButton.setOnClickListener(onCreateClicked);
        cancelButton.setOnClickListener(onCancelClicked);
    }

    private void initInfo() {
        uriIdx = data.trip_photos.size();
        uris = new ArrayList<>();
        imageBase64 = new ArrayList<>();
    }

    private void startDownload() {
        loadingDialog.show();
        downloadRunnable.run();
    }

    private final Runnable downloadRunnable = new Runnable() {
        @Override
        public void run() {
            if (downloadIdx < data.trip_photos.size()) {
                TripViewModel.getINSTANCE().getTripPhotoById(data.trip_photos.get(downloadIdx).id);
                downloadIdx++;
                downloadHandler.postDelayed(this, 20000);
            } else {
                loadingDialog.dismiss();
                stopDownload();
            }
        }
    };

    private void stopDownload() {
        downloadHandler.removeCallbacks(downloadRunnable);
    }

    private final View.OnClickListener onSelectImageClicked = v -> pickImage();

    private final View.OnClickListener onCancelClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (uris.size() > 0 && uriIdx >= 0 && uriIdx < uris.size()) {

                if (uris.size() == 1) selectImg.setImageURI(Uri.EMPTY);
                else if (uriIdx == 0) selectImg.setImageURI(uris.get(uriIdx + 1));
                else selectImg.setImageURI(uris.get(uriIdx - 1));

                uris.remove(uriIdx);
                imageBase64.remove(uriIdx);
            }
        }
    };

    private final View.OnClickListener onPreviousClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (uriIdx - 1 >= 0 && uriIdx - 1 < uris.size()) {
                selectImg.setImageURI(uris.get(uriIdx - 1));
                uriIdx--;
            }
        }
    };

    private final View.OnClickListener onNextClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (uriIdx + 1 >= 0 && uriIdx + 1 < uris.size()) {
                selectImg.setImageURI(uris.get(uriIdx + 1));
                uriIdx++;
            }
        }
    };

    private final View.OnClickListener onCreateClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkInfo()) {
                loadingDialog.show();
                TripViewModel.getINSTANCE().createTripPhoto(createTripPhoto);
                TripViewModel.getINSTANCE().createTripMutableLiveData.observe(requireActivity(), tripObserver);
            }
        }
    };

    private final Observer<Pair<TripData, String>> tripObserver = new Observer<Pair<TripData, String>>() {
        @Override
        public void onChanged(Pair<TripData, String> tripStringPair) {
            loadingDialog.dismiss();
            if (tripStringPair != null) {
                if (tripStringPair.first != null) {
                    NoteMessage.showSnackBar(requireActivity(), "Successfully Added");
                    FN.popAllStack(requireActivity());
                    FN.addFixedNameFadeFragment(MAIN_FRC, requireActivity(), new HomeFragment());

                } else {
                    new ErrorDialog(requireContext(), tripStringPair.second).show();
                }
            } else {
                new ErrorDialog(requireContext(), "Error Connection").show();
            }
        }
    };

    private void pickImage() {
        if (PermissionsHelper.getREAD_EXTERNAL_STORAGE(requireActivity()))
            launcher.launch("image/*");
    }

    private final ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            uris.add(result);
            uriIdx = uris.size() - 1;
            selectImg.setImageURI(result);
            imageBase64.add(new CreateTripPhoto.Photo(ConverterImage.convertUriToBase64(requireContext(), result)));
        }
    });

    private boolean checkInfo() {
        if (uris.size() > 0) {
            return true;
        } else {
            NoteMessage.showSnackBar(requireActivity(), "There is no photo selected");
            return false;
        }
    }

    @Override
    public void getImageAsBase64(int id, String base64, String errorMessage) {
        if (errorMessage != null) {
            new ErrorDialog(requireContext(), errorMessage).show();
        } else {
            imageBase64.add(new CreateTripPhoto.Photo(data.id, base64));
        }
    }
}