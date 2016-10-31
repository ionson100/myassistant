package com.example.user.ling;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;
import com.example.user.ling.orm2.Configure;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static final int YANDEX = 29247;

    interface IKeyboardVisibilityListener {
        void onVisibilityChanged(boolean visible);
    }


    private int mIndexComOut =0;
    private static final int DEFINITION = 333434 ;
    private LinearLayout mSsearshText;
    private  boolean mIsText;
    public static List<MDictionary> mDictionaryList=new ArrayList<>();
    private LinearLayout mPanelText;
    private TextView mTextCore;
    private View mParentView;
    private MyArrayAdapterWord mAdapter =null;
    private RelativeLayout mRelativeLayout;
    private LinearLayout mPanelButton;
    private ListView mListView;
    private EditText mEditText;
    private LinearLayout mPanelAbc;


    private android.view.ActionMode mActionMode;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(R.string.settings_name).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                DialogSettings dialogSettings=new DialogSettings();
                dialogSettings.show(getSupportFragmentManager(),"set");
                return false;
            }
        });
        menu.add(R.string.stiry).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
               List<MDictionary> list=Configure.getSession().getList(MDictionary.class," index_story > 0 order by index_story ");
                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setDictionary(list);
                selectText.isHistory();
                selectText.show(getSupportFragmentManager(),"skdsjf");
                return false;
            }
        });

        menu.add(R.string.clear_selectword).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Utils.messageBox(getString(R.string.warning), getString(R.string.error1), MainActivity.this, new IAction() {
                    @Override
                    public void action(Object o) {
                        mIndexComOut=0;
                        for (MDictionary mDictionary : mDictionaryList) {
                            if(mDictionary.isSelect()){
                                mDictionary.setSelect(false);
                                Configure.getSession().update(mDictionary);
                            }
                        }

                        Toast.makeText(MainActivity.this, R.string.all_removed, Toast.LENGTH_SHORT).show();
                        if(!mIsText){
                            activateText(null,false,false,null);
                            listActivate(mDictionaryList);
                        }
                    }
                });
                return false;
            }
        });


        menu.add(R.string.help_name).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Utils.showHelpNote(MainActivity.this,"help.html");
                return false;
            }
        });

        menu.add(R.string.addWord).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                addNewWord();
                return false;
            }
        });

        menu.add(R.string.about).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Utils.showHelpNote(MainActivity.this,"about.html");
                return false;
            }
        });

        menu.add(R.string.close_app).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                MainActivity.this.finish();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mEditText = (EditText) findViewById(R.id.find_word);
        mListView = (ListView) findViewById(R.id.list_view);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_text);
        mPanelButton = (LinearLayout) findViewById(R.id.panel_buton);
        mPanelText = (LinearLayout) findViewById(R.id.text_panel);
        mTextCore = (TextView) findViewById(R.id.text_core);
        mPanelAbc = (LinearLayout) findViewById(R.id.panel_abc);

        mTextCore.setCustomSelectionActionModeCallback(new android.view.ActionMode.Callback() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                menu.removeItem(android.R.id.selectAll);
//                menu.removeItem(android.R.id.cut);
//                menu.removeItem(android.R.id.copy);
                return true;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                menu.add(0, YANDEX, 0, R.string.yandex).setIcon(R.drawable.yandex);
                menu.add(0, DEFINITION, 0, R.string.kahskjas).setIcon(R.drawable.accessories_dictionary);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {}

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case DEFINITION:{
                        int min = 0;
                        int max = mTextCore.getText().length();
                        if (mTextCore.isFocused()) {
                            final int selStart = mTextCore.getSelectionStart();
                            final int selEnd = mTextCore.getSelectionEnd();

                            min = Math.max(0, Math.min(selStart, selEnd));
                            max = Math.max(0, Math.max(selStart, selEnd));
                        }
                        final String selectedText = mTextCore.getText().subSequence(min, max).toString();
                        translateCore(selectedText);
                        mode.finish();
                        return true;
                    }

                    case YANDEX:{
                        int min = 0;
                        int max = mTextCore.getText().length();
                        if (mTextCore.isFocused()) {
                            final int selStart = mTextCore.getSelectionStart();
                            final int selEnd = mTextCore.getSelectionEnd();

                            min = Math.max(0, Math.min(selStart, selEnd));
                            max = Math.max(0, Math.max(selStart, selEnd));
                        }
                        final String selectedText = mTextCore.getText().subSequence(min, max).toString();
                        translateYandex(selectedText);
                        mode.finish();
                        return true;
                    }

                    default:
                        break;
                }
                return false;
            }
        });

        mTextCore.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MainActivity.this.startActionMode(new android.view.ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
                        menu.add(0, YANDEX, 0, R.string.yandex).setIcon(R.drawable.yandex);
                        menu.add(0, DEFINITION, 0, R.string.kahskjas).setIcon(R.drawable.accessories_dictionary);
                        mActionMode=actionMode;
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
                        return true;
                    }


                    @Override
                    public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case DEFINITION:{
                                int min = 0;
                                int max =  mTextCore.getText().length();
                                if (mTextCore.isFocused()) {
                                    final int selStart = mTextCore.getSelectionStart();
                                    final int selEnd = mTextCore.getSelectionEnd();
                                    min = Math.max(0, Math.min(selStart, selEnd));
                                    max = Math.max(0, Math.max(selStart, selEnd));
                                }
                                final String selectedText = mTextCore.getText().subSequence(min, max).toString();
                                translateCore(selectedText);
                                actionMode.finish();
                                return true;
                            }
                            case YANDEX:{
                                int min = 0;
                                int max = mTextCore.getText().length();
                                if (mTextCore.isFocused()) {
                                    final int selStart = mTextCore.getSelectionStart();
                                    final int selEnd = mTextCore.getSelectionEnd();

                                    min = Math.max(0, Math.min(selStart, selEnd));
                                    max = Math.max(0, Math.max(selStart, selEnd));
                                }
                                final String selectedText = mTextCore.getText().subSequence(min, max).toString();
                                translateYandex(selectedText);
                                actionMode.finish();
                                return true;
                            }

                            default:
                                break;
                        }
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(android.view.ActionMode actionMode) {
                       mActionMode=null;
                    }
                });
                return false;
            }
        });

        mTextCore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mActionMode!=null){
                    mActionMode.finish();
                    mActionMode=null;
                }
            }
        });

        mSsearshText = (LinearLayout)  findViewById(R.id.searsh_text);

        mParentView =findViewById(R.id.activity_main);

        findViewById(R.id.image_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMyDictionary();


                Settings.Add(new WrapperAction(Wrapperator.SHOW_MY_DICTIONARY,new IAction() {
                    @Override
                    public void action(Object o) {
                        getMyDictionary();
                    }
                }));

            }

            private void getMyDictionary() {
                mIndexComOut=0;
                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setDictionary(Utils.getSelectWordses());
                selectText.show(getSupportFragmentManager(),"skdsjf");
            }
        });

        findViewById(R.id.image_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndexComOut=0;
                mRelativeLayout.setVisibility(View.VISIBLE);
                mPanelButton.setVisibility(View.GONE);
                Handler mHandler = new Handler();
                mHandler.post(
                        new Runnable() {
                            public void run() {
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.toggleSoftInputFromWindow(mEditText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                                mEditText.requestFocus();
                                mEditText.setText("");
                                if(mIsText){
                                    mSsearshText.setVisibility(View.VISIBLE);
                                }else{
                                    mSsearshText.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });

        findViewById(R.id.button_text_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String string = mEditText.getText().toString();
                if(string.trim().length()==0) return;
                List<MDictionary> list= new ArrayList<>();

                if(list.size()==0){
                    Utils.SenderYandex(string,list,MainActivity.this);
                }

                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setDictionary(list);
                selectText.show(getSupportFragmentManager(),"skdsjf");
            }
        });



        findViewById(R.id.button_text_search_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mEditText.getText().toString();
                if(string.trim().length()==0) return;
                List<MDictionary> list= getWordFromDictionary(string,true);
                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setDictionary(list);
                selectText.show(getSupportFragmentManager(),"skdsjf");
            }


        });

        new PreviewTask().execute();

        findViewById(R.id.bt_random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndexComOut=0;
                DialogWordRandom dialogWord=new DialogWordRandom();
                int index=new Random().nextInt(mDictionaryList.size())-1;
                dialogWord.setDictionary(mDictionaryList.get(index));
                dialogWord.show(getSupportFragmentManager(),"sdsd");
            }
        });

        findViewById(R.id.bt_show_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCommonDictionary();
                Settings.Add(new WrapperAction(Wrapperator.SHOW_COMMON_DICTIONARY, new IAction() {
                    @Override
                    public void action(Object o) {
                        getCommonDictionary();
                    }
                }));
            }
        });


        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()>0){
                    List<MDictionary> listTemp=getWordFromDictionary(charSequence.toString(),false);
                    if(!mIsText){
                        listActivate(listTemp);
                    }
                }else{
                    if(!mIsText){
                        listActivate(mDictionaryList);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        mListView.setOnCreateContextMenuListener(this);

        setListenerToRootView(new IKeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean visible) {
                if(!visible){
                    mPanelButton.setVisibility(View.VISIBLE);
                    mRelativeLayout.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.image_text_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getMyList()) return;

                Settings.core().Add(new WrapperAction(Wrapperator.SHOW_MY_LIST,new IAction() {
                    @Override
                    public void action(Object o) {
                        if (getMyList()) return;
                    }
                }));
            }

            private boolean getMyList() {
                mIndexComOut=0;
                File file=new File(Application.sPath2);
                File[] files2= Utils.getArrayFiles(file);
                if(files2==null) return true;
                final DialogFileNames dialog=new DialogFileNames();
                dialog.setData(new DialogFileNames.ISelectText() {
                    @Override
                    public void activate(final File file) {
                        if(!file.isDirectory()){

                            String ex= file.getPath().substring(file.getPath().lastIndexOf('.')+1);
                            boolean s=ex.trim().toUpperCase().endsWith("HTML");
                            activateText(Utils.readFile(file.getPath()),true,s,file.getName());
                            if(dialog!=null){
                                dialog.dismiss();
                            }
                            Settings.Add(new WrapperAction(ex.hashCode(),new IAction() {
                                @Override
                                public void action(Object o) {
                                    String ex= file.getPath().substring(file.getPath().lastIndexOf('.')+1);
                                    boolean s=ex.trim().toUpperCase().endsWith("HTML");
                                    activateText(Utils.readFile(file.getPath()),true,s,file.getName());
                                    if(dialog!=null){
                                        dialog.dismiss();
                                    }
                                }
                            }));
                        }
                    }
                },files2);
                dialog.show(getSupportFragmentManager(),"dsdd");
                return false;
            }
        });

        findViewById(R.id.image_delete_select_word).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.messageBox(getString(R.string.warning), getString(R.string.error1), MainActivity.this, new IAction() {
                    @Override
                    public void action(Object o) {
                        mIndexComOut=0;
                        for (MDictionary mDictionary : mDictionaryList) {
                           if(mDictionary.isSelect()){
                               mDictionary.setSelect(false);
                               Configure.getSession().update(mDictionary);
                           }
                        }

                        Toast.makeText(MainActivity.this, R.string.all_removed, Toast.LENGTH_SHORT).show();
                       if(!mIsText){
                           activateText(null,false,false,null);
                           listActivate(mDictionaryList);
                       }
                    }
                });
            }
        });

        new SpeechSearch(MainActivity.this).activate();


    }

    private void getCommonDictionary() {
        mIndexComOut=0;
        activateText(null,false,false,null);
        listActivate(mDictionaryList);
    }


    private void translateYandex(String selectedText) {
        List<MDictionary> dictionaryArrayList=getWordFromYandex(selectedText);
        DialogSearshWord selectText=new DialogSearshWord();
        selectText.setDictionary(dictionaryArrayList);
        selectText.show(getSupportFragmentManager(),"skdsjf");
    }


    List<MDictionary> getWordFromDictionary(String selectedText, boolean isSenderYandex) {
        List<MDictionary> dictionaryArrayList = new ArrayList<>();
        if (selectedText.trim().length() > 0) {

            if (!Settings.core().synchTraslate) {
                String res = selectedText.trim().toUpperCase();
                String res2 = null;


                for (MDictionary mDictionary : mDictionaryList) {
                    if (mDictionary.keyWord.trim().toUpperCase().contains(res)) {
                        dictionaryArrayList.add(mDictionary);
                    }
                }

                if (dictionaryArrayList.size() == 0 && selectedText.length() > 3) {
                    res = selectedText.trim().toUpperCase().substring(0, selectedText.trim().toUpperCase().length() - 1);
                    for (MDictionary mDictionary : mDictionaryList) {
                        if (mDictionary.keyWord.toUpperCase().contains(res)) {
                            dictionaryArrayList.add(mDictionary);
                        }
                    }
                }

                if (dictionaryArrayList.size() > 0) {

                } else {
                    if(isSenderYandex){
                        Utils.SenderYandex(selectedText, dictionaryArrayList, MainActivity.this);
                    }


                }

                MDictionary one = null;
                if (dictionaryArrayList.size() > 1) {
                    for (MDictionary dictionary : dictionaryArrayList) {
                        if (res2 == null) {
                            if (dictionary.keyWord.trim().toUpperCase().equals(res)) {
                                one = dictionary;
                                break;
                            }
                        } else {
                            if (dictionary.keyWord.trim().toUpperCase().equals(res2)) {
                                one = dictionary;
                                break;
                            }
                        }
                    }
                    if (one != null) {
                        dictionaryArrayList.remove(one);
                        dictionaryArrayList.add(0, one);
                    }

                }
            } else {
                for (MDictionary mDictionary : mDictionaryList) {
                    if (mDictionary.valueWord.trim().toUpperCase().contains(selectedText.trim().toUpperCase())) {
                        dictionaryArrayList.add(mDictionary);
                    }
                }
            }


        }
        return dictionaryArrayList;
    }


    List<MDictionary> getWordFromYandex(String selectedText) {

        List<MDictionary> dictionaryArrayList = new ArrayList<>();

        Utils.SenderYandex(selectedText, dictionaryArrayList, MainActivity.this);

        return dictionaryArrayList;
    }



    private void translateCore(String selectedText) {

            List<MDictionary> dictionaryArrayList=getWordFromDictionary(selectedText, true);
            DialogSearshWord selectText=new DialogSearshWord();
            selectText.setDictionary(dictionaryArrayList);
            selectText.show(getSupportFragmentManager(),"skdsjf");
    }


    void activateText(String text,boolean isShow,boolean isHTML,String pathFile){
        mIsText =isShow;
        if(isShow){
            if(pathFile!=null){
                this.setTitle(pathFile);
            }

            if(isHTML){
                mTextCore.setText(Html.fromHtml(text));
                mTextCore.setTextSize(15);
            }else {
                if(Settings.core().paintWords){
                    mTextCore.setTag(null);
                    mTextCore.setText(text, TextView.BufferType.SPANNABLE);
                    new WordColor(mTextCore).paint();
                }else {
                    mTextCore.setTag(null);
                    mTextCore.setText(text);
                }
                mTextCore.setTextSize(20);
            }

            mPanelText.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mPanelAbc.setVisibility(View.GONE);
        }else {
            this.setTitle(R.string.app_name);
            mPanelText.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mPanelAbc.setVisibility(View.VISIBLE);
        }
    }


    void listActivate(List<MDictionary> words){
        mAdapter = new MyArrayAdapterWord(this, R.layout.simple_list_item_1, new ArrayList<>(words));
        mListView.setAdapter(mAdapter);
    }


    public void setListenerToRootView(final IKeyboardVisibilityListener listener) {

        mParentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {

                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, mParentView.getResources().getDisplayMetrics());
                mParentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = mParentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    return;
                }

                alreadyOpen = isShown;
                listener.onVisibilityChanged(isShown);
            }
        });
    }


    int[] scrolState=new int[2];

    public  void listRefrash() {

        if(mIsText&&Settings.core().paintWords){
            new WordColor(mTextCore).paint();
            return;
        }

        if(mAdapter !=null){
            mAdapter.notifyDataSetInvalidated();
        }
    }


    private void createListABC() {
        ListView listViewE = (ListView) findViewById(R.id.listView_alphavit);

        List<MDictionary> mD=new ArrayList<>(Utils.sListABC.length);
        for (String s : Utils.sListABC) {
            MDictionary d=new MDictionary();
            d.valueWord=s;
            mD.add(d);
        }

        listViewE.setAdapter(new MyArrayAdapterWord(this, R.layout.simple_list_item_2, new ArrayList<>(mD)));
        listViewE.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = Utils.sListABC[position];
                List<MDictionary> dictionaryList=new ArrayList<>();
                for (MDictionary dictionary : mDictionaryList) {
                    char d=dictionary.keyWord.trim().toUpperCase().charAt(0);
                    char d2=string.charAt(0);
                    if(d==d2){
                        dictionaryList.add(dictionary);
                    }
                }
                listActivate(dictionaryList);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int position = aMenuInfo.position;

        if(mAdapter !=null){
            final MDictionary mDictionary = mAdapter.getItem(position);
            if(!(mDictionary != null ? mDictionary.isSelect() : false)){
                menu.add(R.string.add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        mDictionary.setSelect(true);

                        Configure.getSession().update(mDictionary);
                        for(int i = 0; i< mListView.getChildCount(); i++){
                            MDictionary dictionary = ((MDictionary) mListView.getChildAt(i).getTag());
                            if(dictionary.id==mDictionary.id){
                                mListView.getChildAt(i).findViewById(R.id.red_star).setVisibility(View.VISIBLE);
                            }
                        }
                        Toast.makeText(MainActivity.this, R.string.add, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }


            if(mDictionary.isSelect()){
                menu.add(R.string.remove).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        mDictionary.setSelect(false);

                        Configure.getSession().update(mDictionary);
                        for(int i = 0; i< mListView.getChildCount(); i++){
                            MDictionary dd = ((MDictionary) mListView.getChildAt(i).getTag());
                            if(dd.id==mDictionary.id){
                                mListView.getChildAt(i).findViewById(R.id.red_star).setVisibility(View.INVISIBLE);
                            }
                        }
                        Toast.makeText(MainActivity.this, R.string.removed, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }

            menu.add(R.string.edit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    DialogEditWord editWord=new DialogEditWord();
                    editWord.setmWord(mDictionary);
                    editWord.setIAction(new IAction() {
                        @Override
                        public void action(Object o) {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    editWord.show(getSupportFragmentManager(),"ada");
                    Toast.makeText(MainActivity.this, R.string.edited, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            menu.add(R.string.addWord).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    addNewWord();
                    return false;
                }
            });

            final Parcelable[] state = new Parcelable[1];
            menu.add(R.string.delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Utils.messageBox(getString(R.string.asddd), getString(R.string.dasffsf), MainActivity.this, new IAction() {
                        @Override
                        public void action(Object o) {
                           state[0] = mListView.onSaveInstanceState();
                            mDictionaryList.remove(mDictionary);
                            Configure.getSession().delete(mDictionary);
                            mAdapter.remove(mDictionary);
                            mAdapter.notifyDataSetInvalidated();
                            Toast.makeText(MainActivity.this, R.string.removed_permanent , Toast.LENGTH_SHORT).show();
                            mListView.onRestoreInstanceState(state[0]);
                        }
                    });
                        return false;
                }
            });

        }

    }


    private void addNewWord() {
        DialogAddWord editWord=new DialogAddWord();
        editWord.setIAction(new IAction() {
            @Override
            public void action(Object o) {
                Configure.getSession().insert(o);
                mDictionaryList.add((MDictionary) o);
                mAdapter.add((MDictionary) o);
                mAdapter.notifyDataSetInvalidated();
                Toast.makeText(MainActivity.this, R.string.addnew, Toast.LENGTH_SHORT).show();
            }
        });
        editWord.show(getSupportFragmentManager(),"ada");
    }

    @Override
    public void onBackPressed() {
        if(Settings.core().iActions.size()==0){
            if(++mIndexComOut <2){
                Toast.makeText(this, R.string.eqek, Toast.LENGTH_SHORT).show();
            }else {
                mIndexComOut =0;
                finish();

            }
        }else {
            if(Settings.iActions.size()==1){
                Settings.iActions.clear();
                getCommonDictionary();
            }else {
                Settings.core().iActions.get(Settings.core().iActions.size()-2).iAction.action(null);
                Settings.core().iActions.remove(Settings.core().iActions.size()-1);
            }

        }

    }




        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SpeechSearch.SPEECH&& resultCode == RESULT_OK) {
            final ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() == 0){
                Toast.makeText(MainActivity.this, "Гугл не распознал ", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, matches.get(0).toString(), Toast.LENGTH_SHORT).show();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showResultSpeech(matches.get(0).toString());
                    }
                });
            }
        }
    }

    void showResultSpeech(String word){
        try{
            List<MDictionary> ww=Searcher.getDictionarys(mDictionaryList,word,this);
            DialogSearshWord  dialog =new DialogSearshWord();
            dialog.setDictionary(ww);
            dialog.show(getSupportFragmentManager(),"rer");
        }catch (Exception dd){
            Toast.makeText(this, dd.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    class PreviewTask extends AsyncTask<Void, Void, List<MDictionary>> {



        @Override
        protected void onPostExecute(List<MDictionary> mDictionaries) {
                mDictionaryList=mDictionaries;
            listActivate(mDictionaries);
            createListABC();

            Utils.sIndexSurogat =0;
            for (MDictionary mDictionary : mDictionaryList) {
                if(mDictionary.index>Utils.sIndexSurogat){
                    Utils.sIndexSurogat =mDictionary.index;
                }
            }
            MainActivity.this.findViewById(R.id.panel_buton_top).setVisibility(View.VISIBLE);
            if(Settings.iActions.size()>0){
                Settings.iActions.get(Settings.iActions.size()).iAction.action(null);
            }
        }

        @Override
        protected List<MDictionary> doInBackground(Void... voids) {
           List<MDictionary> sd=Configure.getSession().getList(MDictionary.class," 1=1 ORDER BY keyWord ASC  ");
            return sd;
        }
    }


}
class SpeechSearch{

    public static final int SPEECH=23;


    private ImageButton imageButton;
    private final MainActivity activity;

    public SpeechSearch( MainActivity activity){
        this.imageButton = (ImageButton) activity.findViewById(R.id.image_microphone);
        this.activity = activity;
    }
    public void activate(){

        if(!isSpeechRecognitionActivityPresented(activity)){
            activity.findViewById(R.id.parent_microphon).setVisibility(View.GONE);
            return;
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // создаем Intent с действием RecognizerIntent.ACTION_RECOGNIZE_SPEECH
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                // добавляем дополнительные параметры:

                if(Settings.core().directTraslateSpeec){

                }else{
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                }

                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Голосовой поиск");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

                // стартуем Activity и ждем от нее результата
                activity.startActivityForResult(intent, SPEECH);
            }
        });
    }
    private static boolean isSpeechRecognitionActivityPresented(Activity ownerActivity) {
        try {
            PackageManager pm = ownerActivity.getPackageManager();
            List activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
            if (activities.size() != 0) {
                return true;
            }
        } catch (Exception e) {}

        return false;
    }
}


