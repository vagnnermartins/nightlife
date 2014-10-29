package br.com.nightlife.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.parse.ParseUser;

import br.com.metasix.olhos_do_rio.componentebox.lib.util.NavegacaoUtil;
import br.com.nightlife.R;
import br.com.nightlife.adapter.MenuAdapter;
import br.com.nightlife.fragment.BaladaFragment;
import br.com.nightlife.pojo.MenuPojo;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;


public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLinear;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private ListView mListView;
    private CharSequence mTitle;

    public PullToRefreshAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if (savedInstanceState == null) {
            mListView.setItemChecked(0, true);
            selectItem(BaladaFragment.class.getName());
        }
    }

    private void init() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer);
        mListView = (ListView) findViewById(R.id.list_menu);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mListView.setAdapter(new MenuAdapter(this, R.layout.item_menu, MenuPojo.getItemsMenu()));
        mListView.setOnItemClickListener(configOnMenuItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.abc_action_bar_home_description,  /* "open drawer" description for accessibility */
                R.string.abc_action_bar_home_description  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        attacher = PullToRefreshAttacher.get(this);
    }

    /**
     * Listener para o item clicado na lista
     * @return
     */
    private AdapterView.OnItemClickListener configOnMenuItemClickListener() {
        return (parent, view, position, id) -> {
            mListView.setItemChecked(position, true);
            selectItem(((MenuPojo)parent.getItemAtPosition(position)).getFragmentName());
        };
    }

    /**
     *
     * @param fragmentName Nome da Fragment que será chamada para iniciar na tela
     */
    private void selectItem(String fragmentName) {
        Bundle args = new Bundle();
        Fragment fragment = Fragment.instantiate(this, fragmentName, args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        mDrawerLayout.closeDrawer(mDrawerLinear);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerLinear)){
                    mDrawerLayout.closeDrawer(mDrawerLinear);
                } else {
                    mDrawerLayout.openDrawer(mDrawerLinear);
                }
                break;
            case R.id.menu_main_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void logout() {
        ParseUser.logOut();
        NavegacaoUtil.navegar(this, LoginActivity.class);
        finish();
    }
}
