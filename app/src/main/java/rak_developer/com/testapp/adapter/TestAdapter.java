package rak_developer.com.testapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import rak_developer.com.testapp.R;
import rak_developer.com.testapp.model.TestModel;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    Context context;
    List<TestModel> testModelList;

    public TestAdapter(Context context, List<TestModel> testModelList) {
        this.context = context;
        this.testModelList = testModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_bg,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        TestModel model = testModelList.get(i);

        viewHolder.txtTitle.setText(model.getTitle());
        viewHolder.txtOverview.setText(model.getOverview());
        viewHolder.txtDate.setText("Date : "+model.getRelease_date());

    }

    @Override
    public int getItemCount() {
        return testModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtOverview;
        private TextView txtDate;
        public RelativeLayout backgroundBG;
        public RelativeLayout forgroundBG;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            forgroundBG = itemView.findViewById(R.id.forgroundBG);
            backgroundBG = itemView.findViewById(R.id.backgroundBG);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtOverview = itemView.findViewById(R.id.txtOverview);
            txtDate = itemView.findViewById(R.id.txtDate);

        }
    }
}
