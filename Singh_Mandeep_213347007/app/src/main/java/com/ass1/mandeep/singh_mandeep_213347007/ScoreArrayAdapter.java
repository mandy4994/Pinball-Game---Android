package com.ass1.mandeep.singh_mandeep_213347007;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mandeep on 09-Sep-15.
 */
public class ScoreArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    public ScoreArrayAdapter(Context context, List<String>values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
    }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values.get(position));

            switch (position)
            {
                case 0:
                    imageView.setImageResource(R.drawable.gold);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.silver);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.bronze);
                    break;

                default:
                    imageView.setImageResource(R.drawable.player);
                    break;
            }


        return rowView;
    }
}
