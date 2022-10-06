package com.albert.reproductorv2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.albert.reproductorv2.MainActivity.archivosMusicas;


public class Canciones_Fragment extends Fragment {

    RecyclerView recyclerView;
    MusicaAdapter musica_adapter;

    public Canciones_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_canciones_, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        if (!(archivosMusicas.size() < 1))
        {
            musica_adapter = new MusicaAdapter(getContext(),archivosMusicas);
            recyclerView.setAdapter(musica_adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        }
        return view;
    }
}