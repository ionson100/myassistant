package com.example.user.ling;

import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ling.orm2.Configure;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static android.R.id.list;


public class MainActivity extends AppCompatActivity {


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

        menu.add(1,2,3,"Настройки").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        menu.add(1,2,3,"О программе").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
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
                menu.removeItem(android.R.id.selectAll);
                menu.removeItem(android.R.id.cut);
                menu.removeItem(android.R.id.copy);
                return true;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                menu.add(0, DEFINITION, 0, R.string.kahskjas).setIcon(R.drawable.ic_translator);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {}

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case DEFINITION:
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
                        menu.add(0, DEFINITION, 0, "Definition").setIcon(R.drawable.ic_translator);
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
                            case DEFINITION:
                                int min = 0;
                                int max =  mTextCore.getText().length();
                                if (mTextCore.isFocused()) {
                                    final int selStart = mTextCore.getSelectionStart();
                                    final int selEnd = mTextCore.getSelectionEnd();
                                    min = Math.max(0, Math.min(selStart, selEnd));
                                    max = Math.max(0, Math.max(selStart, selEnd));
                                }
                                // Perform your definition lookup with the selected text
                                final String selectedText = mTextCore.getText().subSequence(min, max).toString();
                                translateCore(selectedText);


                                // Finish and close the ActionMode
                                actionMode.finish();
                                return true;
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

                mIndexComOut=0;
                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setDictionary(Utils.getSelectWordses());
               // selectText.notShowMenu();
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
                for (MDictionary word : mDictionaryList) {
                    if(word.keyWord.toUpperCase().contains(mEditText.getText().toString().toUpperCase())){
                        list.add(word);
                    }
                }
                if(list.size()==0){
                    Utils.SenderYandex(string,list,MainActivity.this);
                }
                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setDictionary(list);
                selectText.show(getSupportFragmentManager(),"skdsjf");
            }
        });

        //mDictionaryList=Configure.getSession().getList(MDictionary.class," 1=1 ORDER BY keyWord ASC  ");
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
                mIndexComOut=0;
                activateText(null,false,false);
                listActivate(mDictionaryList);
            }
        });


        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()>0){
                    List<MDictionary> listTemp=new ArrayList<>();
                    for (MDictionary word : mDictionaryList) {
                        if(word.keyWord.toUpperCase().contains(charSequence.toString().toUpperCase())){
                            listTemp.add(word);
                        }
                    }

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


       // listActivate(mDictionaryList);


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
                mIndexComOut=0;
                File file=new File(Application.sPath2);
                if(!file.exists()) return;
                final File[] files=file.listFiles();



                List<File> files1=new ArrayList<File>(Arrays.asList(files));
                Collections.sort(files1, new Comparator<File>() {
                    @Override
                    public int compare(File file, File t1) {
                        return file.getPath().compareTo(t1.getPath());
                    }
                });
                File[] files2=files1.toArray(new File[files1.size()]);


                DialogSelectText dialog=new DialogSelectText();
                dialog.setData(new DialogSelectText.ISelectText() {
                    @Override
                    public void activate(File file) {
                       String ex= file.getPath().substring(file.getPath().lastIndexOf('.')+1);
                        boolean s=ex.trim().toUpperCase().endsWith("HTML");
                        activateText(Utils.readFile(file.getPath()),true,s);
                    }
                },files2);
                dialog.show(getSupportFragmentManager(),"dsdd");
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
                           activateText(null,false,false);
                           listActivate(mDictionaryList);
                       }
                    }
                });
            }
        });
    }

    private void translateCore(String selectedText) {
        if(selectedText.trim().length()>0){

            MDictionary one=null;
            List<MDictionary> dictionaryArrayList= new ArrayList<>();
            for (MDictionary mDictionary : mDictionaryList) {
                if(mDictionary.keyWord.toUpperCase().contains(selectedText.toUpperCase())){
                    dictionaryArrayList.add(mDictionary);
                    if(mDictionary.keyWord.trim().toUpperCase().equals(selectedText.trim().toUpperCase())){
                        one=mDictionary;
                    }
                }
            }
            if(one!=null&&dictionaryArrayList.size()>1){
                dictionaryArrayList.remove(one);
                dictionaryArrayList.add(0,one);
            }
            if(dictionaryArrayList.size()>0){

            }else{
               Utils.SenderYandex(selectedText, dictionaryArrayList,MainActivity.this);

            }
            DialogSearshWord selectText=new DialogSearshWord();
            selectText.setDictionary(dictionaryArrayList);
            selectText.show(getSupportFragmentManager(),"skdsjf");

        }
    }


    void activateText(String text,boolean isShow,boolean isHTML){
        mIsText =isShow;
        if(isShow){
            if(isHTML){
                mTextCore.setText(Html.fromHtml(text));
                mTextCore.setTextSize(15);
            }else {
                mTextCore.setText(text);
                mTextCore.setTextSize(20);
            }

            mPanelText.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mPanelAbc.setVisibility(View.GONE);
        }else {
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


    public  void listRefrash() {
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


    @Override
    public void onBackPressed() {
        if(++mIndexComOut <2){
            Toast.makeText(this, R.string.eqek, Toast.LENGTH_SHORT).show();
        }else {
            mIndexComOut =0;
            finish();

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
        }

        @Override
        protected List<MDictionary> doInBackground(Void... voids) {
           List<MDictionary> sd=Configure.getSession().getList(MDictionary.class," 1=1 ORDER BY keyWord ASC  ");
            return sd;
        }
    }
}


