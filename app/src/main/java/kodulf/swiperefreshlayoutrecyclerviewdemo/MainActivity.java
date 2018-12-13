package kodulf.swiperefreshlayoutrecyclerviewdemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnScrollChangeListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<String> mList = new ArrayList<>();
    private MyAdapter myAdapter;
    private static final int ADD = 0;
    private static final int REFRESH = 1;
    private int lastVisiableNum = 0;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case REFRESH:
                    mSwipeRefreshLayout.setRefreshing(false);
                    mList.clear();
                    for (int i = 0; i < 10; i++) {
                        mList.add(""+i);
                    }
                    myAdapter.notifyDataSetChanged();
                    break;

                case ADD:

                    for (int i = 0; i < 10; i++) {
                        mList.add(""+i);
                    }
                    //更新使用这个
                    myAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 10; i++) {
            mList.add(i+"");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.YELLOW);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(this);
        }


        myAdapter = new MyAdapter();
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(myAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(lastVisiableNum+1==mList.size()){
                        mHandler.sendEmptyMessageDelayed(ADD,1000);
                    }
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiableNum=mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);

    }



    @Override
    public void onRefresh() {
        Toast.makeText(this,"onRefresh",Toast.LENGTH_SHORT).show();
        mHandler.sendEmptyMessageDelayed(REFRESH,3000);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

    }


    class MyAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            //inflate 

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);

            return new RecyclerViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
                recyclerViewHolder.textView.setText(mList.get(i));
        }

        @Override
        public int getItemCount() {
            int size = mList.size();
            return size;
        }
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.item_textView);
        }


    }


}
