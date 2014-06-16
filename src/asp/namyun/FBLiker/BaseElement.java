package asp.namyun.FBLiker;

import android.view.View;
import android.widget.BaseAdapter;

public abstract class BaseElement {
    private String id;
    private String name;
    private String likes;
    private String about;

    private BaseAdapter adapter;

    public BaseElement(String id, String name, String likes, String about) {
        super();
        this.id = id;
        this.name = name;
        this.likes = likes;
        this.about = about;
    }

    public String getID() {
        return this.id;
    }

    public void setName(String value) {
//        if(adapter!=null) {
//            adapter.notifyDataSetChanged();
//        }
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public void setLikes(String value) {
//        if(adapter!=null) {
//            adapter.notifyDataSetChanged();
//        }
        this.likes = value;
    }

    public String getLikes() {
        return this.likes;
    }

    public void setAbout(String value) {
//        if(adapter!=null) {
//            adapter.notifyDataSetChanged();
//        }
        this.about = value;
    }

    public String getAbout() {
        return this.about;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public abstract View.OnClickListener getOnClickListener();
}
