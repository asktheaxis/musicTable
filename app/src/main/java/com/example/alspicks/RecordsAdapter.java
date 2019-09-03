package com.example.alspicks;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class RecordsAdapter extends BaseAdapter {

    private Context recordContext;
    private List<Record> recordList;

    public RecordsAdapter(Context context, List<Record> records) {
        recordList = records;
        recordContext = context;
    }

    public void add(Record record) {
        recordList.add(record);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
            RecordViewHolder holder;
        if (view == null) {
            LayoutInflater recordInflater = (LayoutInflater) recordContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = recordInflater.inflate(R.layout.record, null);

            holder = new RecordViewHolder();
            holder.artistView = (TextView) view.findViewById(R.id.record_artist);
            holder.albumView = (TextView) view.findViewById(R.id.record_album);
            holder.yearView = (TextView) view.findViewById(R.id.record_year);
            holder.styleView = (TextView) view.findViewById(R.id.record_style);
            view.setTag(holder);
        } else {
            holder = (RecordViewHolder) view.getTag();
        }

            Record record = (Record) getItem(i);
            holder.artistView.setText(record.artist);
            holder.albumView.setText(record.album);
            holder.yearView.setText(record.year);
            holder.styleView.setText(record.style);
            return view;
    }

    private static class RecordViewHolder {
        public TextView artistView;
        public TextView albumView;
        public TextView yearView;
        public TextView styleView;

    }
}
