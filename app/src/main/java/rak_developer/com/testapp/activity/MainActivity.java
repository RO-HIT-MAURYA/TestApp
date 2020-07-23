package rak_developer.com.testapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rak_developer.com.testapp.R;
import rak_developer.com.testapp.adapter.TestAdapter;
import rak_developer.com.testapp.model.TestModel;
import rak_developer.com.testapp.util.ApiData;
import rak_developer.com.testapp.util.Connectivity;
import rak_developer.com.testapp.util.RecyclerItemTouchHelper;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private Activity activity = this;
    private List<TestModel> testModelList;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private Snackbar snackbar;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();

    }

    private void initializeView() {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.waitMessage));

        rootLayout = findViewById(R.id.rootLayout);

        testModelList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new TestAdapter(activity, testModelList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        callAPI();
    }

    private void callAPI() {
        if (Connectivity.isOnline(activity)) {
            getAPIData();
        } else {
            showSnackbar(getResources().getString(R.string.internetMessage));
        }
    }

    private void getAPIData() {

        progressDialog.show();
        testModelList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiData.callingAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonParentObject = new JSONObject(response);

                    JSONArray jsonArray = jsonParentObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.getString("title");
                        String overview = jsonObject.getString("overview");
                        String release_date = jsonObject.getString("release_date");

                        TestModel model = new TestModel();

                        model.setTitle(title);
                        model.setOverview(overview);
                        model.setRelease_date(release_date);

                        testModelList.add(model);


                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    showSnackbar(getResources().getString(R.string.errorMessage));
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                progressDialog.dismiss();
                showSnackbar(getResources().getString(R.string.errorMessage));
            }
        });

        Volley.newRequestQueue(activity).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS
                , DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }

    private void showSnackbar(String message) {
        snackbar = Snackbar
                .make(rootLayout, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackbar.setActionTextColor(getResources().getColor(R.color.colorWhite));
        snackBarView.setBackgroundColor(getResources().getColor(R.color.colorRed));
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionRefresh) {
            callAPI();
        }
        return super.onOptionsItemSelected(item);
    }
}
