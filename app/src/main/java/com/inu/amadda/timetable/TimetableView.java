package com.inu.amadda.timetable;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.inu.amadda.R;
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.util.DateUtils;
import com.inu.amadda.util.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TimetableView extends LinearLayout {
    private static final int DEFAULT_ROW_COUNT = 15;
    private static final int DEFAULT_COLUMN_COUNT = 6;
    private static final int DEFAULT_CELL_HEIGHT_DP = 50;
    private static final int DEFAULT_SIDE_CELL_WIDTH_DP = 30;
    private static final int DEFAULT_HEADER_CELL_HEIGHT_DP = 34;
    private static final int DEFAULT_START_TIME = 9;

    private static final int DEFAULT_SIDE_HEADER_FONT_SIZE_DP = 12;
    private static final int DEFAULT_HEADER_FONT_SIZE_DP = 12;
    private static final int DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP = 12;
    private static final int DEFAULT_STICKER_TITLE_FONT_SIZE_SP = 13;
    private static final int DEFAULT_STICKER_PLACE_FONT_SIZE_SP = 11;


    private int rowCount;
    private int columnCount;
    private int cellHeight;
    private int sideCellWidth;
    private int headerCellHeight;
    private String[] headerTitle;
    private String[] stickerColors;
    private int startTime;
    private int headerHighlightColor;

    private RelativeLayout stickerBox;
    TableLayout tableHeader;
    TableLayout tableBox;

    private Context context;

    HashMap<Integer, Sticker> classStickers = new HashMap<Integer, Sticker>();
    private int stickerCount = -1;

    private static final boolean CLASS = true;
    private static final boolean SCHEDULE = false;

    private OnStickerSelectedListener stickerSelectedListener = null;

    private AppDatabase appDatabase;

    public TimetableView(Context context) {
        super(context, null);
        this.context = context;
    }

    public TimetableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        appDatabase = AppDatabase.getInstance(this.context);
    }

    public TimetableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttrs(attrs);
        init();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimetableView);
        rowCount = a.getInt(R.styleable.TimetableView_row_count, DEFAULT_ROW_COUNT) - 1;
        columnCount = a.getInt(R.styleable.TimetableView_column_count, DEFAULT_COLUMN_COUNT);
        cellHeight = a.getDimensionPixelSize(R.styleable.TimetableView_cell_height, dp2Px(DEFAULT_CELL_HEIGHT_DP));
        sideCellWidth = a.getDimensionPixelSize(R.styleable.TimetableView_side_cell_width, dp2Px(DEFAULT_SIDE_CELL_WIDTH_DP));
        headerCellHeight = a.getDimensionPixelSize(R.styleable.TimetableView_header_cell_height, dp2Px(DEFAULT_HEADER_CELL_HEIGHT_DP));
        int titlesId = a.getResourceId(R.styleable.TimetableView_header_title, R.array.default_header_title);
        headerTitle = a.getResources().getStringArray(titlesId);
        int colorsId = a.getResourceId(R.styleable.TimetableView_sticker_colors, R.array.default_sticker_color);
        stickerColors = a.getResources().getStringArray(colorsId);
        startTime = a.getInt(R.styleable.TimetableView_start_time, DEFAULT_START_TIME);
        headerHighlightColor = a.getColor(R.styleable.TimetableView_header_highlight_color, getResources().getColor(R.color.default_header_highlight_color));
        a.recycle();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_timetable, this, false);
        addView(view);

        stickerBox = view.findViewById(R.id.sticker_box);
        tableHeader = view.findViewById(R.id.table_header);
        tableBox = view.findViewById(R.id.table_box);

        createTable();
    }

    public void setOnStickerSelectEventListener(OnStickerSelectedListener listener) {
        stickerSelectedListener = listener;
    }

    /**
     * date : 2019-02-08
     * get all schedules TimetableView has.
     */
    public ArrayList<Schedule> getAllSchedulesInStickers() {
        ArrayList<Schedule> allSchedules = new ArrayList<Schedule>();
        for (int key : classStickers.keySet()) {
            for (Schedule schedule : classStickers.get(key).getSchedules()) {
                allSchedules.add(schedule);
            }
        }
        return allSchedules;
    }

    /**
     * date : 2019-02-08
     * Used in Edit mode, To check a invalidate schedule.
     */
    public ArrayList<Schedule> getAllSchedulesInStickersExceptIdx(int idx) {
        ArrayList<Schedule> allSchedules = new ArrayList<Schedule>();
        for (int key : classStickers.keySet()) {
            if (idx == key) continue;
            for (Schedule schedule : classStickers.get(key).getSchedules()) {
                allSchedules.add(schedule);
            }
        }
        return allSchedules;
    }

    public void addClass(ArrayList<Schedule> schedules) {
        addClassList(schedules, -1);
    }

    public void addSchedule(ArrayList<Schedule> schedules, int share, Context activityContext) {
        addScheduleList(schedules, share, activityContext, -1);
    }

    private void addClassList(final ArrayList<Schedule> schedules, int specIdx) {
        final int count = specIdx < 0 ? ++stickerCount : specIdx;
        Sticker sticker = new Sticker();
        for (Schedule schedule : schedules) {
            RelativeLayout rl = new RelativeLayout(context);
            RelativeLayout.LayoutParams param = createStickerParam(schedule);
            rl.setLayoutParams(param);

            TextView tv_title = new TextView(context);
            tv_title.setId(View.generateViewId());
            RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
            tv_title.setLayoutParams(titleParams);
            titleParams.setMargins(dp2Px(4), dp2Px(5), dp2Px(4), 0);
            tv_title.setText(schedule.getClassTitle());
            tv_title.setTextColor(Color.parseColor("#FFFFFF"));
            tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_STICKER_TITLE_FONT_SIZE_SP);
            tv_title.setTypeface(null, Typeface.BOLD);

            TextView tv_place = new TextView(context);
            tv_place.setId(View.generateViewId());
            RelativeLayout.LayoutParams placeParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
            tv_place.setLayoutParams(placeParams);
            placeParams.addRule(RelativeLayout.BELOW, tv_title.getId());
            placeParams.setMargins(dp2Px(4), dp2Px(2), dp2Px(4), 0);
            tv_place.setText(schedule.getClassPlace());
            tv_place.setTextColor(Color.parseColor("#FFFFFF"));
            tv_place.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_STICKER_PLACE_FONT_SIZE_SP);

            rl.addView(tv_title);
            rl.addView(tv_place);

            rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stickerSelectedListener != null)
                        stickerSelectedListener.OnStickerSelected(count, schedules);
                }
            });

            sticker.addTextView(rl);
            sticker.addSchedule(schedule);
            classStickers.put(count, sticker);
            stickerBox.addView(rl);
        }
        setClassStickerColor();
    }

    private void addScheduleList(final ArrayList<Schedule> schedules, int share, Context activityContext, int specIdx) {
        final int count = specIdx < 0 ? ++stickerCount : specIdx;
        Sticker sticker = new Sticker();
        for (Schedule schedule : schedules) {
            RelativeLayout rl = new RelativeLayout(context);
            RelativeLayout.LayoutParams param = createStickerParam(schedule);
            rl.setLayoutParams(param);

            TextView tv_title = new TextView(context);
            tv_title.setId(View.generateViewId());
            RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
            tv_title.setLayoutParams(titleParams);
            titleParams.setMargins(dp2Px(4), dp2Px(5), dp2Px(4), 0);
            tv_title.setText(schedule.getClassTitle());
            tv_title.setTextColor(Color.parseColor("#1b2d42"));
            tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_STICKER_TITLE_FONT_SIZE_SP);
            tv_title.setTypeface(null, Typeface.BOLD);

            TextView tv_place = new TextView(context);
            tv_place.setId(View.generateViewId());
            RelativeLayout.LayoutParams placeParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
            tv_place.setLayoutParams(placeParams);
            placeParams.addRule(RelativeLayout.BELOW, tv_title.getId());
            placeParams.setMargins(dp2Px(4), dp2Px(2), dp2Px(4), 0);
            tv_place.setText(schedule.getClassPlace());
            tv_title.setTextColor(Color.parseColor("#1b2d42"));
            tv_place.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_STICKER_PLACE_FONT_SIZE_SP);

            rl.addView(tv_title);
            rl.addView(tv_place);

            rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stickerSelectedListener != null)
                        stickerSelectedListener.OnStickerSelected(count, schedules);
                }
            });

            sticker.addTextView(rl);
            sticker.addSchedule(schedule);
            getScheduleStickerColor(sticker, share, activityContext);
            stickerBox.addView(rl);
        }
    }

    public String createSaveData() {
        return SaveManager.saveSticker(classStickers);
    }

    public void load(String data) {
        removeAll();
        classStickers = SaveManager.loadSticker(data);
        int maxKey = 0;
        for (int key : classStickers.keySet()) {
            ArrayList<Schedule> schedules = classStickers.get(key).getSchedules();
            addClassList(schedules, key);
            if (maxKey < key) maxKey = key;
        }
        stickerCount = maxKey + 1;
        setClassStickerColor();
    }

    public void removeAll() {
        for (int key : classStickers.keySet()) {
            Sticker sticker = classStickers.get(key);
            for (RelativeLayout rl : sticker.getView()) {
                stickerBox.removeView(rl);
            }
        }
        classStickers.clear();
    }

    public void edit(int idx, ArrayList<Schedule> schedules) {
        remove(idx);
        addClassList(schedules, idx);
    }

    public void remove(int idx) {
        Sticker sticker = classStickers.get(idx);
        for (RelativeLayout rl : sticker.getView()) {
            stickerBox.removeView(rl);
        }
        classStickers.remove(idx);
        setClassStickerColor();
    }

    public void setDayHighlight() {
        int idx = DateUtils.getDayOfWeek();
        if(idx < 0)return;

        TableRow row = (TableRow) tableHeader.getChildAt(0);
        View element = row.getChildAt(idx);

        TextView tx = (TextView)element;
        tx.setTextColor(Color.parseColor("#6a6a6a"));
//        tx.setBackgroundColor(headerHighlightColor);
        tx.setBackground(getResources().getDrawable(R.drawable.item_highlight_border));
//        tx.setTypeface(null, Typeface.BOLD);
        tx.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP);

        for (int i = 0; i < rowCount; i++) {
            row = (TableRow) tableBox.getChildAt(i);
            element = row.getChildAt(idx);
            tx = (TextView)element;
//            tx.setBackgroundColor(headerHighlightColor);
            tx.setBackground(getResources().getDrawable(R.drawable.item_highlight_border));
        }
    }

    private void setClassStickerColor() {
        int size = classStickers.size();
        int[] orders = new int[size];
        int i = 0;
        for (int key : classStickers.keySet()) {
            orders[i++] = key;
        }
        Arrays.sort(orders);

        int colorSize = stickerColors.length;

        for (i = 0; i < size; i++) {
            for (RelativeLayout rl : classStickers.get(orders[i]).getView()) {
                rl.setBackgroundColor(Color.parseColor(stickerColors[i % (colorSize)]));
            }
        }

    }

    private void getScheduleStickerColor(Sticker sticker, int share, Context activityContext) {
        if (share > 0){
            new Thread(() -> {
                String color = appDatabase.groupDao().getColorByKey(share);
                if(color == null || activityContext == null)
                    return;
                ((Activity)activityContext).runOnUiThread(() -> setScheduleStickerColor(sticker, color));
            }).start();
        }
        else {
            String color = PreferenceManager.getInstance().getSharedPreference(context, Constant.Preference.COLOR, null);
            setScheduleStickerColor(sticker, color);
        }
    }

    private void setScheduleStickerColor(Sticker sticker, String color) {
        StringBuilder sb = new StringBuilder(color);
        sb.insert(1, "66");
        for (RelativeLayout rl : sticker.getView()) {
            rl.setBackgroundColor(Color.parseColor(sb.toString()));
        }
    }

    private void createTable() {
        createTableHeader();
        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(createTableLayoutParam());

            for (int k = 0; k < columnCount; k++) {
                TextView tv = new TextView(context);
                if (k == 0) {
                    tv.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight));
                } else {
                    tv.setLayoutParams(createTableRowParam(cellHeight));
                }
                tv.setTextColor(getResources().getColor(R.color.colorHeaderText));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SIDE_HEADER_FONT_SIZE_DP);
                tv.setBackground(getResources().getDrawable(R.drawable.item_border));
                if (k == 0){
                    tv.setText(getHeaderTime(i));
                } else {
                    tv.setText("");
                }
                tv.setPadding(0, dp2Px(4),dp2Px(3), 0);
                tv.setGravity(Gravity.END);

                tableRow.addView(tv);
            }
            tableBox.addView(tableRow);
        }

//        setDayHighlight();
    }

    private void createTableHeader() {
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(createTableLayoutParam());

        for (int i = 0; i < columnCount; i++) {
            TextView tv = new TextView(context);
            if (i == 0) {
                tv.setLayoutParams(createTableRowParam(sideCellWidth, headerCellHeight));
            } else {
                tv.setLayoutParams(createTableRowParam(headerCellHeight));
            }
            tv.setTextColor(getResources().getColor(R.color.colorHeaderText));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_FONT_SIZE_DP);
            tv.setBackground(getResources().getDrawable(R.drawable.item_border));
            tv.setText(headerTitle[i]);
            tv.setGravity(Gravity.CENTER);

            tableRow.addView(tv);
        }
        tableHeader.addView(tableRow);
    }

    private RelativeLayout.LayoutParams createStickerParam(Schedule schedule) {
        int cell_w = calCellWidth();

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(cell_w, calStickerHeightPx(schedule));
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.setMargins(sideCellWidth + cell_w * schedule.getDay(), calStickerTopPxByTime(schedule.getStartTime()), 0, 0);

        return param;
    }

    private int calCellWidth(){
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int cell_w = (size.x-getPaddingLeft() - getPaddingRight()- sideCellWidth) / (columnCount - 1);
        return cell_w;
    }

    private int calStickerHeightPx(Schedule schedule) {
        int startTopPx = calStickerTopPxByTime(schedule.getStartTime());
        int endTopPx = calStickerTopPxByTime(schedule.getEndTime());
        int d = endTopPx - startTopPx;

        return d;
    }

    private int calStickerTopPxByTime(Time time) {
        int topPx = (time.getHour() - startTime) * cellHeight + (int) ((time.getMinute() / 60.0f) * cellHeight);
        return topPx;
    }

    private TableLayout.LayoutParams createTableLayoutParam() {
        return new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
    }

    private TableRow.LayoutParams createTableRowParam(int h_px) {
        return new TableRow.LayoutParams(calCellWidth(), h_px);
    }

    private TableRow.LayoutParams createTableRowParam(int w_px, int h_px) {
        return new TableRow.LayoutParams(w_px, h_px);
    }

    private String getHeaderTime(int i) {
        int p = (startTime + i) % 24;
        int res = p <= 12 ? p : p - 12;
        return res + "";
    }

    static private int dp2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void onCreateByBuilder(Builder builder) {
        this.rowCount = builder.rowCount;
        this.columnCount = builder.columnCount;
        this.cellHeight = builder.cellHeight;
        this.sideCellWidth = builder.sideCellWidth;
        this.headerCellHeight = builder.headerCellHeight;
        this.headerTitle = builder.headerTitle;
        this.stickerColors = builder.stickerColors;
        this.startTime = builder.startTime;
        this.headerHighlightColor = builder.headerHighlightColor;

        init();
    }


    public interface OnStickerSelectedListener {
        void OnStickerSelected(int idx, ArrayList<Schedule> schedules);
    }

    static class Builder {
        private Context context;
        private int rowCount;
        private int columnCount;
        private int cellHeight;
        private int sideCellWidth;
        private int headerCellHeight;
        private String[] headerTitle;
        private String[] stickerColors;
        private int startTime;
        private int headerHighlightColor;

        public Builder(Context context) {
            this.context = context;
            rowCount = DEFAULT_ROW_COUNT;
            columnCount = DEFAULT_COLUMN_COUNT;
            cellHeight = dp2Px(DEFAULT_CELL_HEIGHT_DP);
            sideCellWidth = dp2Px(DEFAULT_SIDE_CELL_WIDTH_DP);
            headerCellHeight = dp2Px(DEFAULT_HEADER_CELL_HEIGHT_DP);
            headerTitle = context.getResources().getStringArray(R.array.default_header_title);
            stickerColors = context.getResources().getStringArray(R.array.default_sticker_color);
            startTime = DEFAULT_START_TIME;
            headerHighlightColor = context.getResources().getColor(R.color.default_header_highlight_color);
        }

        public Builder setRowCount(int n) {
            this.rowCount = n;
            return this;
        }

        public Builder setColumnCount(int n) {
            this.columnCount = n;
            return this;
        }

        public Builder setCellHeight(int dp) {
            this.cellHeight = dp2Px(dp);
            return this;
        }

        public Builder setSideCellWidth(int dp) {
            this.sideCellWidth = dp2Px(dp);
            return this;
        }

        public Builder setHeaderCellHeight(int dp) {
            this.headerCellHeight = dp2Px(dp);
            return this;
        }

        public Builder setHeaderTitle(String[] titles) {
            this.headerTitle = titles;
            return this;
        }

        public Builder setStickerColors(String[] colors) {
            this.stickerColors = colors;
            return this;
        }

        public Builder setStartTime(int t) {
            this.startTime = t;
            return this;
        }

        public Builder setHeaderHighlightColor(int c) {
            this.headerHighlightColor = c;
            return this;
        }

        public TimetableView build() {
            TimetableView timetableView = new TimetableView(context);
            timetableView.onCreateByBuilder(this);
            return timetableView;
        }
    }
}
