package asp.namyun.FBLiker.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import asp.namyun.FBLiker.BaseElement;
import asp.namyun.FBLiker.R;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.facebook.model.GraphObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LikeListFragment extends Fragment {

    private ListView listView;
    private ActionListAdapter adapter;
    private List<BaseElement> elementList;

    private ArrayList<String> pageIds = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selection,container,false);
        elementList = new ArrayList<BaseElement>();
        listView = (ListView)view.findViewById(R.id.likeListView);
        adapter = new ActionListAdapter(getActivity(),R.id.likeListView,elementList);
        listView.setAdapter(adapter);
        doBatchRequest();
        return view;
    }

    private void doBatchRequest() {

        RequestBatch requestBatch = new RequestBatch();
        requestBatch.add(new Request(Session.getActiveSession(),
                "me/likes", null, null, new Request.Callback() {
            public void onCompleted(Response response) {
                GraphObject graphObject = response.getGraphObject();
                if (graphObject != null) {
                    Map<String,Object> responseMap = graphObject.asMap();

                    JSONArray likesData = (JSONArray)responseMap.get("data");
                    for(int i=0; i<likesData.length(); i++) {
                        try {
                            JSONObject data=(JSONObject)likesData.get(i);

                            RequestBatch pageRequestBatch = new RequestBatch();
                            pageRequestBatch.add(new Request(Session.getActiveSession(),
                                    String.valueOf(data.get("id")), null, null, new Request.Callback() {
                                public void onCompleted(Response response) {
                                    GraphObject graphObject = response.getGraphObject();
                                    if (graphObject != null) {
                                        if (graphObject.getProperty("id")!=null) {
                                            elementList.add(new LikeListElement((String)graphObject.getProperty("name"),
                                                    String.valueOf(graphObject.getProperty("likes")),
                                                    (String)graphObject.getProperty("about")));
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }));

                            pageRequestBatch.executeAsync();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }));

        requestBatch.executeAsync();
    }

    private class LikeListElement extends BaseElement {
        public LikeListElement(String name, String likes, String about) {
            super(name, likes, about);
        }

        @Override
        public View.OnClickListener getOnClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Do nothing..
                }
            };
        }
    }

    private class ActionListAdapter extends ArrayAdapter<BaseElement> {
        private List<BaseElement> elementList;

        public ActionListAdapter(Context context, int resourceId, List<BaseElement> elementList) {
            super(context,resourceId,elementList);
            this.elementList = elementList;

            for(int i=0; i<elementList.size(); i++) {
                elementList.get(i).setAdapter(this);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view==null) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listitem, null);
            }

            BaseElement element = elementList.get(position);
            if(element!=null) {
                view.setOnClickListener(element.getOnClickListener());

                TextView name = (TextView)view.findViewById(R.id.name);
                TextView likes = (TextView)view.findViewById(R.id.likes);
                TextView about = (TextView)view.findViewById(R.id.about);

                if(name!=null) {
                    name.setText(element.getName());
                }

                if(likes!=null) {
                    likes.setText(element.getLikes());
                }

                if(about!=null) {
                    about.setText(element.getAbout());
                }
            }

            return view;
        }
    }
}