/*
package com.example.amol.loanquote;

*/
/**
 * Created by amol13704 on 8/3/2017.
 *//*




        import android.app.NotificationManager;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.res.Configuration;
        import android.graphics.Typeface;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.Settings;
        import android.support.v4.app.Fragment;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarActivity;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.widget.Toolbar;
        import android.text.SpannableString;
        import android.text.Spanned;
        import android.text.TextUtils;
        import android.util.TypedValue;
        import android.view.Gravity;
        import android.view.KeyEvent;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.RelativeLayout;

        import com.mauj.analytics.MaujAnalytics;
        import com.mauj.dm.database.DownloadTableHelper;
        import com.mauj.dm.model.DownloadModel;
        import com.mauj.dm.utils.DMConstants;
        import com.mauj.dm.utils.FWCompat;
        import com.mauj.dm.utils.FilesStorage;
        import com.mauj.dm.utils.Utility;
        import com.mauj.gamesbond.category.CategoryNameFragment;
        import com.mauj.gamesbond.common.OnFragmentInteractionListener;
        import com.mauj.gamesbond.common.PostUserApps;
        import com.mauj.gamesbond.contentDetail.ContentDetailFragment;
        import com.mauj.gamesbond.contentDetail.VideoEntertainmentDetailFragment;
        import com.mauj.gamesbond.home.HomeFragment;
        import com.mauj.gamesbond.home.NavDrawerListAdapter;
        import com.mauj.gamesbond.listing.ChannelFragment;
        import com.mauj.gamesbond.login.LoginCallBack;
        import com.mauj.gamesbond.login.LoginFragment;
        import com.mauj.gamesbond.model.SubscriptionPackModel;
        import com.mauj.gamesbond.myaccount.MyAccountListing;
        import com.mauj.gamesbond.notification.GcmRegTokenParser;
        import com.mauj.gamesbond.notification.GcmService;
        import com.mauj.gamesbond.notification.NotificationConstant;
        import com.mauj.gamesbond.parser.LoginParser;
        import com.mauj.gamesbond.parser.UpdateApiParser;
        import com.mauj.gamesbond.search.PopularSearchFragment;
        import com.mauj.gamesbond.search.SearchListingFragment;
        import com.mauj.gamesbond.utils.AndroidSharedPreference;
        import com.mauj.gamesbond.utils.AppConstants;
        import com.mauj.gamesbond.utils.AppUtility;
        import com.mauj.gamesbond.utils.ChangeFragment;
        import com.mauj.gamesbond.utils.ContentStorageSessionManager;
        import com.mauj.gamesbond.utils.CustomTextView;
        import com.mauj.gamesbond.utils.CustomTypefaceSpan;
        import com.mauj.gamesbond.utils.FontCache;
        import com.mauj.gamesbond.utils.FragmentUtils;
        import com.mauj.gamesbond.utils.PixelConversionUtils;
        import com.mauj.gamesbond.utils.PreferenceConstants;
        import com.mauj.gamesbond.utils.ViewUtil;
        import com.mauj.gamesbond.viewpager.MyAccountFragmentViewPager;
        import com.mauj.permission.MPermission;

        import java.io.File;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Timer;
        import java.util.TimerTask;


public class DemoActivity extends ActionBarActivity implements OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TOTAL_OPEN_COUNT = "total_open_count";
    public static int OPEN_COUNT = 0;
    private static String titleName = "";
    public DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ImageView toolbarIcon;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private RelativeLayout relListLayout;
    private NavDrawerListAdapter navigationDrawerAdapter;
    private ArrayList<String[]> leftSliderData;
    private boolean openMyActivities = Boolean.FALSE;
    private String currentFragmentName = "";
    private String pageName = "";
    private boolean doubleBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        MPermission.getInstance().setActivity(this);

        if (!FWCompat.isMarshmallow_23__OrNewer()) {

        } else {
            MPermission.getInstance().requestPermissions(new MPermission.IOnPermissionResult() {
                @Override
                public void onPermissionResult(MPermission.ResultSet resultSet) {
                    if (resultSet.areAllPermissionsGranted()) {
                        AppConstants.log("PERMISSION ", "GRANTED ----");
                    } else {
                        Utility.showPopUpForPermission(MainActivity.this);
                    }
                }
            }, MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (getIntent().hasExtra(NotificationConstant.KEY_TXT_OPEN)) {
            openInstalledApp(getIntent());
        }

        if (Gamesbond.getInstance().subscriptionPackModel == null) {
            Gamesbond.getInstance().subscriptionPackModel = (SubscriptionPackModel) AndroidSharedPreference.getObjectFromPreference(PreferenceConstants.SUBSCRIPTION_MODEL);

        }

        if (AppConstants.DEVICE_WIDTH == 0 || AppConstants.DEVICE_HEIGHT == 0) {
            AppUtility.setDeviceHeightWidth();
        }

        if (getIntent().hasExtra(DMConstants.ACTION_OPEN_MYACTIVITIES)) {
            if (getIntent().getExtras().getBoolean(DMConstants.ACTION_OPEN_MYACTIVITIES)) {
                openMyActivities = Boolean.TRUE;
            }
        }
        traceGAEventAppVisit();
        initView();
        setDrawerDimensions();
        Gamesbond.getInstance().mainActivity = this;

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        initDrawer();
        //do the GCM registration
        new GcmRegTokenParser(MainActivity.this).getGcmToken(AppConstants.TXT_GCM_PROJECT_ID);
        //update the downloaded content
        new UpdateApiParser(MainActivity.this).updateContent();
        //get downloaded games
        getDownloadedGames();
        if (getIntent().hasExtra(NotificationConstant.KEY_NOTIFICATION_TYPE)) {
            handleLandingPage(getIntent());
        }    //for notification after downloading of app completed. opens the install intent
        else if (getIntent().hasExtra(NotificationConstant.KEY_DOWNLOAD_FILEPATH)) {
            getDownloadFilePathAndOpenInstallIntent(getIntent());
        }
        callPostUserAppsAPI();
        //sendDumyyNotification();
    }

    public Toolbar getToolBar() {
        return toolbar;
    }

    private void callPostUserAppsAPI() {
        OPEN_COUNT = AndroidSharedPreference.getIntFromPref(TOTAL_OPEN_COUNT);
        OPEN_COUNT++;
        AndroidSharedPreference.saveIntToPref(TOTAL_OPEN_COUNT, OPEN_COUNT);
        if (OPEN_COUNT > 7) {
            if (Utility.connectivity(MainActivity.this)) {
                new PostUserApps().execute();
            }
            AppConstants.log("amolakash", " IN Oncreate Mainactivity IN IF OPEN_COUNT = " + OPEN_COUNT);
            OPEN_COUNT = 0;
            AndroidSharedPreference.saveIntToPref(TOTAL_OPEN_COUNT, OPEN_COUNT);
        }
        AppConstants.log("amolakash", " IN Oncreate Mainactivity OPEN_COUNT = " + OPEN_COUNT);
    }

    private void setDrawerDimensions() {
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) relListLayout.getLayoutParams();
        param.width = (int) (AppConstants.DEVICE_WIDTH - AppUtility.actionBarHeight());
    }

    private void getDownloadFilePathAndOpenInstallIntent(Intent intent) {
        String downloadFilePath = intent.getExtras().getString(NotificationConstant.KEY_DOWNLOAD_FILEPATH);
        if (!TextUtils.isEmpty(downloadFilePath)) {
            File file = new File(downloadFilePath);
            if (file.exists()) {
                openIntentToInstallApplication(file);
            } else {
                AppUtility.showToast("File not found");
            }
        }
    }

    private void openIntentToInstallApplication(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void overrideCampaignIdForRequestInThisSession(Intent intent) {
        if (intent.hasExtra(NotificationConstant.KEY_CAMPAIGN_ID)) {
            AppConstants.campaignIdThroughGcm = intent.getExtras().getString(NotificationConstant.KEY_CAMPAIGN_ID);
        }
    }

    private void sendDumyyNotification() {
        Intent intent = new Intent(this, GcmService.class);
        intent.putExtras(GcmService.getDummyNotificationBundle());
        intent.setAction("com.google.android.c2dm.intent.RECEIVE");
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //populate the left drawer
        AppConstants.log("ViewUtil  :", "MainActivity onResume");
        MPermission.getInstance().setActivity(this);
        invalidateOptionsMenu();
        populateLeftMenu();
    }

    */
/**
     * Method to get the downloaded games from the database
     *//*

    private void getDownloadedGames() {
        if (Gamesbond.getInstance().alreadyDownloadedApps.isEmpty()) {
            Gamesbond.getInstance().alreadyDownloadedApps.clear();
            Gamesbond.getInstance().alreadyDownloadedApps = DownloadTableHelper.getInstance().getDownloadDataFromDB();

            if (Gamesbond.getInstance().alreadyDownloadedApps.size() > 0) {
                for (int i = 0; i < Gamesbond.getInstance().alreadyDownloadedApps.size(); i++) {
                    if (Gamesbond.getInstance().alreadyDownloadedApps.get(i).getDownloadStatus() == DMConstants.INT_DOWNLOADED) {
                        if (Gamesbond.getInstance().alreadyDownloadedApps.get(i).getPathOnSdCard() == null) {
                            String localFilePath = AppUtility.retrieveFilePathFromUrl((Gamesbond.getInstance().alreadyDownloadedApps.get(i).getId()), "apk");
                            Gamesbond.getInstance().alreadyDownloadedApps.get(i).setPathOnSdCard(localFilePath);
                            DownloadTableHelper.getInstance().updateAppSdcardPath(Gamesbond.getInstance().alreadyDownloadedApps.get(i));
                            DownloadTableHelper.getInstance().updateDownloadPkg(AppUtility.getAppInfo(Gamesbond.getInstance().alreadyDownloadedApps.get(i)));
                        }
                    }
                    Gamesbond.getInstance().alreadyDownloadedMap.put(Gamesbond.getInstance().alreadyDownloadedApps.get(i).getId(), Gamesbond.getInstance().alreadyDownloadedApps.get(i));
                }
            }
        }
    }

    */
/**
     * Method to initialise views
     *//*

    private void initView() {
        leftDrawerList = (ListView) findViewById(R.id.leftDrawer);
        relListLayout = (RelativeLayout) findViewById(R.id.rel_listlayout);
        AppUtility.setNavigationDrawerSize(relListLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarIcon = (ImageView) findViewById(R.id.toolbar_icon);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //populate the left drawer
        populateLeftMenu();
        //initiate the left drawer
    }

    */
/**
     * Method to populate left menu
     *//*

    public void populateLeftMenu() {
        String[] arrayData = {AppConstants.TXT_HOME, AppConstants.TXT_CATEGORY, AppConstants.TXT_BROWSE_CHANNELS};// getResources().getStringArray(R.array.left_menu);
        int[] iconList = {R.drawable.ic_menu_home, R.drawable.ic_menu_browse_category, R.drawable.ic_menu_browse_channels};

        leftSliderData = new ArrayList<String[]>();

        for (int i = 0; i < arrayData.length; i++) {
            String[] temp = {arrayData[i], Integer.toString(iconList[i])};
            leftSliderData.add(temp);
        }

        //check for whether user is active or not
        SubscriptionPackModel subcriptionModel = Gamesbond.getInstance().subscriptionPackModel;
        if (subcriptionModel != null) {
            if (!Gamesbond.getInstance().subscriptionPackModel.isLoginBtnVisible() &&
                    !AndroidSharedPreference.getBooleanFromPref(PreferenceConstants.IS_QUEUE_ENABLED)) {
                //enable login option if user is not in active state
                String[] temp = {AppConstants.TXT_LOGIN, Integer.toString(R.drawable.ic_menu_login)};
                leftSliderData.add(temp);
            }
        }

        //enable my activities option if user is in active state
        String[] temp = {AppConstants.TXT_MY_ACTIVITIES, Integer.toString(R.drawable.ic_menu_activities)};
        leftSliderData.add(temp);

        if (subcriptionModel != null) {
            setAppUpdateText();
        }

        //populate the left navigation adapter
        navigationDrawerAdapter = new NavDrawerListAdapter(MainActivity.this,
                leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        //set the onitemclicklistner of the left navigation drawer
        leftDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void setAppUpdateText() {
        int installedAppVersionCode = 0;
        int serverVersionCode = Gamesbond.getInstance().subscriptionPackModel.getAppManifestVersionCode();
        int downloadedGamesBondAppVersionCode = 0;

        PackageInfo infoDownloadedApp = null;
        try {
            installedAppVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
        }

        infoDownloadedApp = new ViewUtil().getGamesBondDownloadedApkPackageInfo();
        if (infoDownloadedApp != null) {
            downloadedGamesBondAppVersionCode = infoDownloadedApp.versionCode;
        }

        String menuItemName = AppConstants.TXT_UPDATE_APP;
        if (installedAppVersionCode < downloadedGamesBondAppVersionCode) {
            if (serverVersionCode > downloadedGamesBondAppVersionCode) {
                menuItemName = AppConstants.TXT_UPDATE_APP;
            }
              */
/* means already latest app is downloaded but not installed **//*

            else if (serverVersionCode == downloadedGamesBondAppVersionCode) {
                menuItemName = AppConstants.TXT_INSTALL_APP;
            }
        } else if (serverVersionCode > installedAppVersionCode) {
            menuItemName = AppConstants.TXT_UPDATE_APP;
        } else {
            menuItemName = null;
        }

        if (!TextUtils.isEmpty(menuItemName)) {
            String[] temp = {menuItemName, Integer.toString(R.drawable.ic_menu_updateapp)};
            leftSliderData.add(temp);
        }
    }

    //initiate left navigation drawer
    private void initDrawer() {
        final String category = "Drawer";
        //navigation drawer toggle
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                AppConstants.isDrawerOpen = false;
                if (!AppConstants.TXT_CURRENT_PAGE.equals("PopularSearch Page")) {
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }


                MaujAnalytics.getInstance(MainActivity.this).trackEvent(
                        category
                        , "Drawer closed"
                        , "", AppUtility.addCustomDimesions()
                );
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                AppConstants.isDrawerOpen = true;
                MaujAnalytics.getInstance(Gamesbond.getInstance().getApplicationContext()).trackEvent(
                        category
                        , "Drawer opened"
                        , "", AppUtility.addCustomDimesions()
                );
                if (!AppConstants.TXT_CURRENT_PAGE.equals("PopularSearch Page")) {
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                if (currentFragmentName.equals(ContentDetailFragment.class.getSimpleName())
                        ) {
                    ContentDetailFragment contentDetailFragment = (ContentDetailFragment)
                            getSupportFragmentManager()
                                    .findFragmentById(R.id.container);
                    if(contentDetailFragment.videoPlayerClass != null) {
                        contentDetailFragment.videoPlayerClass.pausePlayingVideo();
                    }
                }
                else if(
                        currentFragmentName.equals(VideoEntertainmentDetailFragment.class.getSimpleName())){
                    VideoEntertainmentDetailFragment videoEntertainmentDetailFragment = (VideoEntertainmentDetailFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.container);
                    if(videoEntertainmentDetailFragment.videoPlayerClass != null) {
                        videoEntertainmentDetailFragment.videoPlayerClass.pausePlayingVideo();
                    }
                }
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        //select the first item of the navigation drawer
        changeFragment(0, false);

        if (openMyActivities) {
            if (TextUtils.isEmpty(currentFragmentName) || !currentFragmentName.equals(MyAccountListing.class.getSimpleName())) {
                openMyActivitiesListing();
            }
        }
    }

    */
/* Called whenever we call invalidateOptionsMenu() *//*

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = drawerLayout.isDrawerOpen(leftDrawerList);
//        menu.findItem(R.id.menu_search).setVisible(!drawerOpen);
//        if (currentFragmentName.equals(PopularSearchFragment.class.getSimpleName()) ||
//                currentFragmentName.equals(SearchListingFragment.class.getSimpleName())) {
//            menu.findItem(R.id.menu_notification).setVisible(Boolean.FALSE);
//        } else {
//            menu.findItem(R.id.menu_notification).setVisible(!drawerOpen);
//        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        boolean drawerOpen = drawerLayout.isDrawerOpen(relListLayout);
        AppConstants.isDrawerOpen = drawerOpen;
        menu.findItem(R.id.menu_search).setVisible(!drawerOpen && !AppConstants.showOpenSearch);
        if (currentFragmentName != null) {
            if (currentFragmentName.equals(PopularSearchFragment.class.getSimpleName()) ||
                    currentFragmentName.equals(SearchListingFragment.class.getSimpleName())) {
                menu.findItem(R.id.menu_notification).setVisible(Boolean.FALSE);
            } else {
                MenuItem item = menu.findItem(R.id.menu_notification).setVisible(!drawerOpen);
                createCustomMenuItem(item);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void createCustomMenuItem(MenuItem item) {
        try {
            RelativeLayout actionView = (RelativeLayout) item.getActionView();
            ImageView downloadIcon = (ImageView) actionView.findViewById(R.id.img_download_icon);
            downloadIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDownloadIconClickListener();
                }
            });
            AppUtility.increaseToucharea(downloadIcon, 20, 20, 20, 20);
            actionView.setGravity(Gravity.RIGHT);
            actionView.setPadding(0, 0, PixelConversionUtils.getSpacingBetweenCards(), 0);
            setBadgeView(actionView);
        } catch (Exception e) {

        }

    }

    private void handleDownloadIconClickListener() {
        Fragment fragment = null;
        fragment = new MyAccountListing();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.MY_ACCOUNT_LIST_TITLE, "Recently Downloaded");
        bundle.putString(AppConstants.MY_ACCOUNT_LIST_TYPE, "dwn");
        bundle.putInt("contentTypeId", AppConstants.INT_CONTENT_TYPE_GAME);

        fragment.setArguments(bundle);


        if (fragment != null && AppUtility.checkInternetConnection()) {
            //change the fragment in fragment manager
            FragmentUtils.getInstance().addFragment(fragment, getSupportFragmentManager(), Boolean.TRUE);
            MaujAnalytics.getInstance(Gamesbond.getInstance().getApplicationContext()).trackEvent(
                    AppConstants.btnMyActivities,
                    pageName,
                    "", AppUtility.addCustomDimesions());
        }
    }

    private void setBadgeView(View view) {

        try {
            int badgeCount = 0;

            List<DownloadModel> listDownloadModel = DownloadTableHelper.getInstance().getDownloadDataFromDB();
            for (DownloadModel model : listDownloadModel) {
                if (model.getDownloadStatus() == DMConstants.INT_DEFAULT) {
                } else if (model.getDownloadStatus() == DMConstants.INT_INSTALLED) {
                } else if (model.getDownloadStatus() == DMConstants.INT_CANCELED) {
                } else if (model.getDownloadStatus() == DMConstants.INT_INSTALLED) {
                } else {
                    badgeCount += 1;
                }

                AppConstants.log(TAG, "COUNT : " + badgeCount + " status : " + model.getDownloadStatus());
            }

            RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.ll_notification_badge);

            if (badgeCount > 0) {

                layout.setGravity(Gravity.CENTER);

                int height = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 22, getResources().getDisplayMetrics());

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        height, height);
                params.setMargins(PixelConversionUtils.getSpacingWithinCard() * (3 / 2) + 10, 0, 0, 0);
                //params.setMargins(0, 0, 0, PixelConversionUtils.getSpacingBetweenCards() * 2);
                //params.addRule(RelativeLayout.ALIGN_TOP, R.id.img_download_icon);
                layout.setLayoutParams(params);

                layout.setBackgroundResource(R.drawable.myapp_info_border);

                CustomTextView txtView = (CustomTextView) view.findViewById(R.id.tv_notification_badge);
                //txtView.setTextSize(getResources().getDimension(R.dimen.text_size_xxsmall));
                if (badgeCount > 9) {
                    txtView.setText("9+");
                } else {
                    txtView.setText("" + badgeCount);
                }
            } else {
                layout.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(DMConstants.ACTION_OPEN_MYACTIVITIES)) {
            if (intent.getExtras().getBoolean(DMConstants.ACTION_OPEN_MYACTIVITIES)) {
                if (currentFragmentName.isEmpty() || !currentFragmentName.equals(MyAccountListing.class.getSimpleName())) {
                    openMyActivitiesListing();
                }
            }
        } else if (intent.hasExtra(NotificationConstant.KEY_NOTIFICATION_TYPE)) {
            setGCMMsgOpenedTracing(intent);
            if (Gamesbond.getInstance().subscriptionPackModel == null) {
                Gamesbond.getInstance().subscriptionPackModel = (SubscriptionPackModel) AndroidSharedPreference.getObjectFromPreference(PreferenceConstants.SUBSCRIPTION_MODEL);
                if (Gamesbond.getInstance().subscriptionPackModel == null) {
                    new LoginParser(MainActivity.this, MainActivity.class.getSimpleName(), null, null, new LoginCallBack() {
                        @Override
                        public void onLoginSuccess(Object inputObject) {
                            handleLandingPage(intent);
                        }

                        @Override
                        public void onLoginFailure(Object inputObject) {

                        }
                    }).performUserLogin(null, Boolean.FALSE);
                }
            } else {
                handleLandingPage(intent);
            }
        } else if (intent.hasExtra(NotificationConstant.KEY_DOWNLOAD_FILEPATH)) {
            int notificationId = intent.getIntExtra(NotificationConstant.KEY_GCM_MSG_ID, 0);
            getDownloadFilePathAndOpenInstallIntent(intent);
            NotificationManager manager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);

            MaujAnalytics.getInstance(Gamesbond.getInstance().getApplicationContext()).trackEvent(
                    "Install",
                    "From Notification",
                    "ContentID:" + notificationId, AppUtility.addCustomDimesions());
        } else if (intent.hasExtra(NotificationConstant.KEY_TXT_OPEN)) {
            openInstalledApp(intent);
        }
    }

    private void openInstalledApp(Intent intent) {


        int notificationId = intent.getIntExtra(NotificationConstant.KEY_GCM_MSG_ID, 0);
        String packageName = intent.getStringExtra(NotificationConstant.KEY_PACKAGE_NAME);
        intentPlayApp(packageName);
        NotificationManager manager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);

        MaujAnalytics.getInstance(Gamesbond.getInstance().getApplicationContext()).trackEvent(
                "Play",
                "From Notification",
                "ContentID:" + notificationId, AppUtility.addCustomDimesions());
    }

    private void intentPlayApp(String packageNameOfInstalledApp) {
        Intent launchIntent = Gamesbond.getInstance().getAppContext()
                .getPackageManager()
                .getLaunchIntentForPackage(packageNameOfInstalledApp);
        if (launchIntent != null) {
            startActivity(launchIntent);
        }
    }

    private void setGCMMsgOpenedTracing(Intent intent) {
        String notifType = intent.getExtras().getString(NotificationConstant.KEY_NOTIFICATION_TYPE);
        String gcmMsgId = intent.getExtras().getString(NotificationConstant.KEY_GCM_MSG_ID);
        MaujAnalytics.getInstance(Gamesbond.getInstance().getApplicationContext()).trackEvent(
                "GCM", "msg body click",
                gcmMsgId + "_" + notifType, AppUtility.addCustomDimesions());
        AppUtility.gcmMessageTrackerHit(gcmMsgId, "open");
    }

    private void handleLandingPage(Intent intent) {
        HandleLandingPageForNotifiacation handleLandingPageForNotifiacation = new HandleLandingPageForNotifiacation(intent, this, currentFragmentName);
        handleLandingPageForNotifiacation.handleLandingPage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_search) {
            MaujAnalytics.getInstance(Gamesbond.getInstance().getApplicationContext()).trackEvent(
                    AppConstants.btnSearch,
                    pageName,
                    "", AppUtility.addCustomDimesions());

            //open popular searches
            PopularSearchFragment fragment = new PopularSearchFragment();
            FragmentUtils.getInstance().addFragment(fragment, getSupportFragmentManager(), Boolean.TRUE);
            return true;
        } else if (id == R.id.menu_notification) {
            MyAccountFragmentViewPager fragment = new MyAccountFragmentViewPager();
            fragment.setArguments(new Bundle());
            if (fragment != null && AppUtility.checkInternetConnection()) {
                //change the fragment in fragment manager

                fragment.setArguments(new Bundle());
                FragmentUtils.getInstance().addFragment(fragment, getSupportFragmentManager(), Boolean.TRUE);
                MaujAnalytics.getInstance(Gamesbond.getInstance().getApplicationContext()).trackEvent(
                        AppConstants.btnMyActivities,
                        pageName,
                        "", AppUtility.addCustomDimesions());
            }
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    */
/**
     * Swaps fragments in the main content view
     *//*

    private void openMyActivitiesListing() {

        if (!AppUtility.checkInternetConnection()) {
            return;
        }

        //change the fragment in fragment manager
        Fragment fragment = null;
        fragment = new MyAccountListing();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.MY_ACCOUNT_LIST_TITLE, "Recently Downloaded");
        bundle.putString(AppConstants.MY_ACCOUNT_LIST_TYPE, "dwn");
        bundle.putInt("contentTypeId", AppConstants.INT_CONTENT_TYPE_GAME);

        fragment.setArguments(bundle);


        if (fragment != null) {
            //change the fragment in fragment manager
            FragmentUtils.getInstance().addFragment(fragment, getSupportFragmentManager(), Boolean.TRUE);
        }
        // Insert the fragment by replacing any existing fragment
        //close the drawer if opened
        if (drawerLayout.isDrawerOpen(relListLayout)) {
            drawerLayout.closeDrawer(relListLayout);
        }
    }

    @Override
    public void onBackPressed() {
        if (currentFragmentName.equals(ContentDetailFragment.class.getSimpleName())) {

            Fragment fragment = getSupportFragmentManager()
                    .findFragmentById(R.id.container);
            if (fragment instanceof ContentDetailFragment) {
                ContentDetailFragment contentDetailFragment = (ContentDetailFragment) fragment;

                // similarly handle for videoaentertainemnt by putting in basaeclass
                if (!contentDetailFragment.shouldExitFragment()) {
                */
/* Current Mode is Landspace **//*

                    contentDetailFragment.onFragmentBackPress();
                    return;
                }
            }
        }
        else if (currentFragmentName.equals(VideoEntertainmentDetailFragment.class.getSimpleName())) {
            VideoEntertainmentDetailFragment videoEntertainmentDetailFragment = (VideoEntertainmentDetailFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.container);

            // similarly handle for videoaentertainemnt by putting in basaeclass
            if (!videoEntertainmentDetailFragment.shouldExitFragment()) {
                */
/* Current Mode is Landspace **//*

                videoEntertainmentDetailFragment.onFragmentBackPress();
                return;
            }
        }

        //check for whether search view opened
        //check for whether drawer is open
        if (!drawerLayout.isDrawerOpen(relListLayout)) {
            //go the previous fragment in fragment manager backstack
            gotoPreviousFragment();

        } else {
            //close the drawer which is open
            drawerLayout.closeDrawer(relListLayout);
        }
    }

    */
/**
     * Method to go to the previous fragment in fragment manager
     *//*

    public void gotoPreviousFragment() {

        // fragment manager back stack count less then or equal to one the close the activity
        boolean isHomeFragment = !TextUtils.isEmpty(currentFragmentName) && currentFragmentName.equals(HomeFragment.class.getSimpleName());
        if (isHomeFragment || (FragmentUtils.getInstance().getBackStackEntryCount(getSupportFragmentManager()) <= 1)) {
            if (doubleBackPressed) {
                finish();
            }
            doubleBackPressed = true;
//            Toast.makeText(Gamesbond.getInstance().getAppContext(), "Press again to exit. ", Toast.LENGTH_LONG);
            AppUtility.showToast("Press again to exit. ");
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    doubleBackPressed = false;
                }
            }, 2500);
            return;
        } else {
            try {
                //popback the immediate fragment from back stack
                AppConstants.TXT_PREVIOUS_PAGE = AppConstants.TXT_CURRENT_PAGE;
                Gamesbond.getInstance().pageFlowModel.setLastPgaeName(AppConstants.TXT_PREVIOUS_PAGE);
                if (DMConstants.headerMap.containsKey(AppConstants.GB_LAST_PAGE_NAME)) {
                    DMConstants.headerMap.put(AppConstants.GB_LAST_PAGE_NAME, AppConstants.TXT_CURRENT_PAGE);
                }
                getSupportFragmentManager().popBackStackImmediate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //get the previous fragment
            Fragment fragment = getSupportFragmentManager().getFragments().get(
                    getSupportFragmentManager().getBackStackEntryCount() - 1);

            if (fragment != null) {
                //if previous fragment is PopularSearchFragment then make backpress twice
                if (PopularSearchFragment.class.getSimpleName().equals(fragment.getClass().getSimpleName())) {
                    gotoPreviousFragment();
                } else {
                    //resume the previous fragment
                    fragment.onResume();
                }
            }
        }
    }

    */
/**
     * Method to change the fragment from fragment manager
     *//*

    public void changeFragment(int position, boolean isDrawerItemClicked) {
        String action = "";
        String category = "Drawer";
        String gamesBondAppFilePath = FilesStorage.DOWNLOADS
                + Gamesbond.getInstance().subscriptionPackModel.getContentId() + ".apk";

        // set the selected drawer position
        Fragment fragment = null;
        if (leftSliderData.get(position)[0].equals(AppConstants.TXT_LOGIN)) {
            //open login fragment
            if (!AppUtility.checkInternetConnection()) {
                return;
            }
            fragment = new LoginFragment();
            action = AppConstants.TXT_LOGIN;
        } else if (leftSliderData.get(position)[0].equals(AppConstants.TXT_MY_ACTIVITIES)) {
            if (!AppUtility.checkInternetConnection()) {
                return;
            }
            //open myaccount
            fragment = new MyAccountFragmentViewPager();
            fragment.setArguments(new Bundle());
            action = AppConstants.TXT_MY_ACTIVITIES;
        } else if (leftSliderData.get(position)[0].equals(AppConstants.TXT_CATEGORY)) {
            //open category list
//            if (!ContentStorageSessionManager.getInstance().isSessionValid(CategoryNameFragment.KEY_CATEGORYNAME_CONTENT_STORAGE_SESSION_TIME))
            {
                if (AppUtility.checkInternetConnection()) {
                    fragment = new CategoryNameFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstants.CONTENT_TYPE_ID, String.valueOf(AppConstants.INT_CONTENT_TYPE_GAME));
                    fragment.setArguments(bundle);
                    action = AppConstants.TXT_CATEGORY;
                }
            }
//            else {
//                fragment = new CategoryNameFragment();
//                action = AppConstants.TXT_CATEGORY;
//            }

        } else if (leftSliderData.get(position)[0].equals(AppConstants.TXT_BROWSE_CHANNELS)) {
            //open all channels
            if (!ContentStorageSessionManager.getInstance().isSessionValid(ChannelFragment.KEY_CHANNEL_CONTENT_STORAGE_SESSION_TIME)) {
                if (AppUtility.checkInternetConnection()) {
                    fragment = ChangeFragment.openListing(true, false);
                    action = AppConstants.TXT_BROWSE_CHANNELS;
                }
            } else {
                fragment = ChangeFragment.openListing(true, false);
                action = AppConstants.TXT_BROWSE_CHANNELS;
            }


        } else if (leftSliderData.get(position)[0].equals(AppConstants.TXT_RECOMMENDED_GAMES)) {
            //open recommended games
            fragment = ChangeFragment.openListing(false, true);
            action = AppConstants.TXT_RECOMMENDED_GAMES;
        } else if (leftSliderData.get(position)[0].equals(AppConstants.TXT_HOME)) {
            //open home page
            if (!ContentStorageSessionManager.getInstance().isSessionValid(HomeFragment.KEY_HOMEDATA_CONTENT_STORAGE_SESSION_TIME)) {
                if (AppUtility.checkInternetConnection()) {
                    fragment = new HomeFragment();
                    action = AppConstants.TXT_HOME;
                }
            } else {
                fragment = new HomeFragment();
                action = AppConstants.TXT_HOME;
            }
        } else if (leftSliderData.get(position)[0].equals(AppConstants.TXT_UPDATE_APP)) {
            if (!AppUtility.isGBAppUpdateRunning()) {
                if (AppUtility.checkInternetConnection()) {
                    File file = new File(gamesBondAppFilePath);
                    if (file.exists()) {
                        file.delete();
                    }
                    new AppUtility().goForAppUpdate(MainActivity.this);
                }
                action = AppConstants.TXT_UPDATE_APP;
            } else {
                AppUtility.showToast(getResources().getString(R.string.gb_app_update_running));
            }

        } else if (leftSliderData.get(position)[0].equals(AppConstants.TXT_INSTALL_APP)) {
            File file = new File(gamesBondAppFilePath);
            if (file.exists()) {
                new ViewUtil().openDialogToInstallApp(file);
            } else {
                if (AppUtility.checkInternetConnection()) {
                    new AppUtility().goForAppUpdate(MainActivity.this);
                }
            }
            action = AppConstants.TXT_INSTALL_APP;
        }

        if (fragment != null) {
            //change the fragment in fragment manager
            FragmentUtils.getInstance().addFragment(fragment, getSupportFragmentManager(), Boolean.TRUE);
        }

        // Highlight the selected item, update the title, and close the drawer
        leftDrawerList.setItemChecked(position, true);
        if (isDrawerItemClicked) {
            MaujAnalytics.getInstance(Gamesbond.getInstance().getApplicationContext()).
                    trackEvent(category, action, "", AppUtility.addCustomDimesions());
        }
        if (drawerLayout.isDrawerOpen(relListLayout)) {
            drawerLayout.closeDrawer(relListLayout);
        }
    }

    */
/**
     * Method to enable/disable the navigation drawer back arrow and hamburger menu
     *//*

    public void showDrawerToggle(boolean showHamburger, boolean showbackArrow) {
        drawerToggle.setDrawerIndicatorEnabled(showHamburger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showbackArrow);
        drawerToggle.syncState();
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    */
/**
     * Method to enable/disable the actionbar title and actionbar icon
     *
     * @param title string for actionbar title
     *//*

    public void UpdateActionBarTitleFragment(String title, boolean isIconVisible, boolean isTitleVisible) {
//        if (!TextUtils.isEmpty(title))
        {
            titleName = title;
        }

        Typeface myTypeface = FontCache.get("fonts/Roboto-Regular.ttf", getApplicationContext());
        SpannableString spannableString = new SpannableString(titleName);
        spannableString.setSpan(new CustomTypefaceSpan("", myTypeface), 0, titleName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        if (!isIconVisible) {
            toolbarIcon.setImageDrawable(null);
            getSupportActionBar().setTitle(spannableString);
        } else {
            toolbarIcon.setImageDrawable(getResources().getDrawable(R.drawable.actionbar_icon));
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    public void updateFragmentName(String fragmentName, String pageName) {

        currentFragmentName = fragmentName;
        this.pageName = pageName;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppConstants.campaignIdThroughGcm = "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermission.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private void traceGAEventAppVisit() {

        String category = "Total Open";
        String action = "";
        String label = "ANDROID ID:" + Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (AndroidSharedPreference.getIntFromPref(PreferenceConstants.APP_OPEN_STATE) == 0) {
            AndroidSharedPreference.saveIntToPref(PreferenceConstants.APP_OPEN_STATE, PreferenceConstants.FIRST_OPEN);
            action = "First Open";
        } else if (AndroidSharedPreference.getIntFromPref(PreferenceConstants.APP_OPEN_STATE) == PreferenceConstants.FIRST_OPEN) {
            AndroidSharedPreference.saveIntToPref(PreferenceConstants.APP_OPEN_STATE, PreferenceConstants.RE_OPEN);
            action = "Reopen";
        } else if (AndroidSharedPreference.getIntFromPref(PreferenceConstants.APP_OPEN_STATE) == PreferenceConstants.RE_OPEN) {
            action = "Reopen";
        }

        long currentTime = System.currentTimeMillis();

        MaujAnalytics.getInstance(Gamesbond.getInstance().getApplicationContext()).trackEvent(category, action, label + " : Current Time : " + currentTime, AppUtility.addCustomDimesions());
    }

    //Navigation drawer click listener
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //selectDrawerItem(position);
            changeFragment(position, true);
        }
    }
}
*/
