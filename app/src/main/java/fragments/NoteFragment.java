package fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.notetestserver.AddNoteActivity;
import com.example.notetestserver.LoginActivity;
import com.example.notetestserver.MainActivity;
import com.example.notetestserver.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.NoteClient;
import cz.msebera.android.httpclient.Header;
import app.*;
import object.LoginObject;
import object.*;
import adapter.*;

public class NoteFragment extends Fragment {
    ShimmerRecyclerView shimmerRecyclerView;
    int start = 0;
    List<NoteObject> list;
    NoteAdapter adapter;
    FloatingActionButton floatingActionButton;

    public NoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        view = init(view);
        return view;
    }

    private View init(View view) {
        shimmerRecyclerView = view.findViewById(R.id.rv);
        shimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        adapter = new NoteAdapter(getActivity(), list);
        shimmerRecyclerView.setAdapter(adapter);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);

        getData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNoteActivity.class));
            }
        });

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(app.main.tag));
        return view;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void getData() {
        RequestParams params = app.getRequestParams();
        params.put(ROUTER.ROUTE, ROUTER.ROUTE_NOTES);
        params.put(ROUTER.ACTION, ROUTER.ACTION_READ);
        params.put(ROUTER.INPUT_START, start);


        // app.l(params.toString());
        // app.l("session  : " + spref.get().getString(ROUTER.SESSION, ""));
        // app.l("user id  : " + spref.get().getInt(ROUTER.USER_ID, -1));


        NoteClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responce = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.has("error")) {
                        app.t(jsonObject.getString("error"), app.ToasetType.error);
                        if (jsonObject.has("action") && jsonObject.getString("action").equals("logout")) {
                            {
                                spref.get().edit().clear();
                                getActivity().finish();
                                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                            }

                        }
                    } else {


                    }

                } catch (JSONException e) {
                    list.clear();

                    NoteObject[] objects = new Gson().fromJson(responce, NoteObject[].class);
                    list.addAll(Arrays.asList(objects));

/*
                    Type listtype = new TypeToken<ArrayList<NoteObject>>() {
                    }.getType();
                    List<NoteObject> newlist =new Gson().fromJson(responce, listtype);
                    list.addAll(newlist);*/


                    adapter.notifyDataSetChanged();
                    // app.l(e.toString());
                }


                // app.l(responce);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                app.l("onFailure");
            }

            @Override
            public void onStart() {
                shimmerRecyclerView.showShimmerAdapter();
                super.onStart();
            }

            @Override
            public void onFinish() {
                shimmerRecyclerView.hideShimmerAdapter();
                super.onFinish();
            }
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra("action", -1);
            if (action == NoteBroadCast.ACTION_ADD) {
                NoteObject noteObject = (NoteObject) intent.getSerializableExtra("object");
                List<NoteObject> objects = new ArrayList<>();
                objects.add(noteObject);
                objects.addAll(list);
                list.clear();
                list.addAll(objects);
                adapter.notifyDataSetChanged();

            } else if (action == NoteBroadCast.ACTION_EDIT) {
                int position = intent.getIntExtra("positon", 0);
                if (list.size() > position) {
                    NoteObject object = (NoteObject) intent.getSerializableExtra("object");
                    list.set(position, object);
                    adapter.notifyDataSetChanged();
                }
            } else if (action == NoteBroadCast.ACTION_DELETE) {
                NoteObject noteObject= (NoteObject) intent.getSerializableExtra("object");
                for (int i=0;i<list.size();i++){
                    NoteObject note=list.get(i);
                    if (note.getId()==noteObject.getId()){
                        list.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }

                }

            } else {

                app.l("i got notefragment");
            }
        }
    };
}