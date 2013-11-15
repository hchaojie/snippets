package cn.hchaojie.snippets.view.pageindicator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import cn.hchaojie.snippets.R;
import cn.hchaojie.snippets.view.pageindicator.TabContainer.OnTabSelectedListener;

public class TestTabContainer extends FragmentActivity {
	static final int NUM_ITEMS = 5;

	private MyAdapter mAdapter;
	private ViewPager mPager;
	private FrameLayout mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_pager_sample);

		mActionBar = (FrameLayout) findViewById(R.id.action_bar);
		
		mAdapter = new MyAdapter(getSupportFragmentManager(), this);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		
		TabContainer tabs = (TabContainer) findViewById(R.id.tabs);
		tabs.setViewPager(mPager);
		tabs.setOnTabSelectedListener(new OnTabSelectedListener() {
			@Override
			public void onTabSelected(int position) {
				mActionBar.removeAllViews();
				
				Fragment f = mAdapter.getItem(position);
				if (f instanceof BaseTabFragment) {
					BaseTabFragment fragment = (BaseTabFragment) f;
					mActionBar.addView(fragment.getActionBarView());
				}
			}
		});
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		private Context mContext;
		
		public MyAdapter(FragmentManager fm, Context context) {
			super(fm);
			mContext = context;
		}

		@Override
		public int getCount() {
			return NUM_ITEMS + 1;
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return HomeTabFragment.newInstance(mContext);
			}
			
			return ArrayListFragment.newInstance(position);
		}
	}
	
	private static abstract class BaseTabFragment extends Fragment {
		public abstract View getActionBarView();
	}
	
	public static class HomeTabFragment extends BaseTabFragment {
		private Context mContext;
		
		@Override
		public View getActionBarView() {
			Context context = mContext;
			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			Button btn = new Button(context);
			btn.setText("Left");
			
			View v = new View(context);
			LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
			lp.weight = 1;
			v.setLayoutParams(lp);
			
			Button btn2 = new Button(context);
			btn2.setText("Right");
			
			layout.addView(btn);
			layout.addView(v);
			layout.addView(btn2);
			return layout;
		}
		
		public static HomeTabFragment newInstance(Context context) {
			HomeTabFragment f = new HomeTabFragment();
			f.mContext = context;

			return f; 
		}
	}

	public static class ArrayListFragment extends ListFragment {
		int mNum;

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static ArrayListFragment newInstance(int num) {
			ArrayListFragment f = new ArrayListFragment();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);

			return f;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_pager_list, container,
					false);
			View tv = v.findViewById(R.id.text);
			((TextView) tv).setText("Fragment #" + mNum);
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, CHEESES));
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentList", "Item clicked: " + id);
		}
	}
	
    public static final String[] CHEESES = {
        "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
        "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",
        "Aisy Cendre", "Allgauer Emmentaler", "Alverca", "Ambert", "American Cheese",
        "Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh", "Anthoriro", "Appenzell",
        "Aragon", "Ardi Gasna", "Ardrahan", "Armenian String", "Aromes au Gene de Marc",
        "Asadero", "Asiago", "Aubisque Pyrenees", "Autun", "Avaxtskyr", "Baby Swiss"
    };
}