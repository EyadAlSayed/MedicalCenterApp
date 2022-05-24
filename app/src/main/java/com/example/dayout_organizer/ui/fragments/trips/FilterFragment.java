package com.example.dayout_organizer.ui.fragments.trips;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.dayout_organizer.R;
import com.example.dayout_organizer.adapter.recyclers.MyTripsAdapter;
import com.example.dayout_organizer.helpers.view.FN;
import com.example.dayout_organizer.models.trip.TripData;
import com.example.dayout_organizer.models.trip.TripModel;
import com.example.dayout_organizer.models.trip.TripType;
import com.example.dayout_organizer.ui.dialogs.ErrorDialog;
import com.example.dayout_organizer.ui.dialogs.LoadingDialog;
import com.example.dayout_organizer.viewModels.TripViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class FilterFragment extends Fragment {

    View view;

    @BindView(R.id.filter_title)
    EditText filterTitle;

    @BindView(R.id.filter_min_price)
    EditText filterMinPrice;

    @BindView(R.id.filter_max_price)
    EditText filterMaxPrice;

    @BindView(R.id.filter_spinner)
    Spinner filterSpinner;

    @BindView(R.id.filter_button)
    Button filterButton;

    LoadingDialog loadingDialog;

    public static boolean isFilterOpen = false;

    MyTripsAdapter adapter;

    int filterType;

    public FilterFragment(MyTripsAdapter adapter, int type) {
        this.adapter = adapter;
        filterType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.filter_fragment, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        loadingDialog = new LoadingDialog(requireContext());
        filterButton.setOnClickListener(onFilterClicked);
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.trip_types));
        filterSpinner.setAdapter(typesAdapter);
    }

    private final View.OnClickListener onFilterClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadingDialog.show();

            showFilteredTrips();
            FN.popStack(requireActivity());
            isFilterOpen = false;
        }
    };

    private void showFilteredTrips() {
        if (filterType == 1) {
            TripViewModel.getINSTANCE().historyTripsMutableLiveData.observe(requireActivity(), new Observer<Pair<TripModel, String>>() {
                @Override
                public void onChanged(Pair<TripModel, String> tripModelStringPair) {
                    loadingDialog.dismiss();
                    if (tripModelStringPair != null) {
                        if (tripModelStringPair.first != null) {
                            adapter.refreshList(filterList(tripModelStringPair.first.data), 1);
                        } else
                            new ErrorDialog(requireContext(), tripModelStringPair.second).show();
                    } else
                        new ErrorDialog(requireContext(), "Error Connection").show();
                }
            });
        } else if (filterType == 2) {
            TripViewModel.getINSTANCE().upcomingTripsMutableLiveData.observe(requireActivity(), new Observer<Pair<TripModel, String>>() {
                @Override
                public void onChanged(Pair<TripModel, String> tripModelStringPair) {
                    loadingDialog.dismiss();
                    if (tripModelStringPair != null) {
                        if (tripModelStringPair.first != null) {
                            adapter.refreshList(filterList(tripModelStringPair.first.data), 2);
                        } else
                            new ErrorDialog(requireContext(), tripModelStringPair.second).show();
                    } else
                        new ErrorDialog(requireContext(), "Error Connection").show();
                }
            });
        } else if (filterType == 3) {
            TripViewModel.getINSTANCE().activeTripsMutableLiveData.observe(requireActivity(), new Observer<Pair<TripModel, String>>() {
                @Override
                public void onChanged(Pair<TripModel, String> tripModelStringPair) {
                    loadingDialog.dismiss();
                    if (tripModelStringPair != null) {
                        if (tripModelStringPair.first != null) {
                            adapter.refreshList(filterList(tripModelStringPair.first.data), 3);
                        } else
                            new ErrorDialog(requireContext(), tripModelStringPair.second).show();
                    } else
                        new ErrorDialog(requireContext(), "Error Connection").show();
                }
            });
        }
    }

    private ArrayList<TripData> filterList(ArrayList<TripData> list) {

        if (!filterTitle.getText().toString().equals(""))
            list = filterListOnTitle(list);
        if (!filterMinPrice.getText().toString().equals(""))
            list = filterListOnMinPrice(list);
        if (!filterMaxPrice.getText().toString().equals(""))
            list = filterListOnMaxPrice(list);
        //TODO: Test after getting types from api - Caesar.
        if (!filterSpinner.getSelectedItem().toString().equals("Any"))
            list = filterListOnType(list);

        return list;
    }

    private ArrayList<TripData> filterListOnTitle(ArrayList<TripData> list) {
        ArrayList<TripData> filteredTrips = new ArrayList<>();

        for (TripData trip : list) {
            if (trip.title.contains(filterTitle.getText().toString())) {
                filteredTrips.add(trip);
            }
        }
        return filteredTrips;
    }

    private ArrayList<TripData> filterListOnMinPrice(ArrayList<TripData> list) {
        ArrayList<TripData> filteredTrips = new ArrayList<>();

        if (Integer.parseInt(filterMinPrice.getText().toString()) > 0) {
            for (TripData trip : list) {
                if (trip.price >= Integer.parseInt(filterMinPrice.getText().toString())) {
                    filteredTrips.add(trip);
                }
            }
        }
        return filteredTrips;
    }

    private ArrayList<TripData> filterListOnMaxPrice(ArrayList<TripData> list) {
        ArrayList<TripData> filteredTrips = new ArrayList<>();

        if (Integer.parseInt(filterMaxPrice.getText().toString()) > 0) {
            for (TripData trip : list) {
                if (trip.price <= Integer.parseInt(filterMaxPrice.getText().toString())) {
                    filteredTrips.add(trip);
                }
            }
        }
        return filteredTrips;
    }

    private ArrayList<TripData> filterListOnType(ArrayList<TripData> list) {
        ArrayList<TripData> filteredTrips = new ArrayList<>();

        for(TripData trip : list){
            for (TripType tripType : trip.types){
                if(tripType.name.equals(filterSpinner.getSelectedItem().toString())) {
                    filteredTrips.add(trip);
                }
            }
        }
        return filteredTrips;
    }
}