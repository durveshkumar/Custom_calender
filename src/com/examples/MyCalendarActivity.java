package com.examples;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(3)
public class MyCalendarActivity extends Activity implements OnClickListener{
	private static final String tag = "MyCalendarActivity";
	String[] preArray;
	int[] value;
	Vector<String> selected = new Vector<String>();
	HashMap<String,Integer> m = new HashMap<String,Integer>();
    int s = 0,count=0;
    
	private TextView currentMonth;
	//private Button selectedDayMonthYearButton;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	@SuppressLint("NewApi")
	private int month, year;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_calendar_view);
		preArray = new String[6];
		value = new int[6];
		//s = 0;18/12/2015 , 19/12/2015 , 20/12/2015 , 21/12/2015 , 22/12/2015, 23/12/2015
		preArray[0] = "18/12/2015"; value[0] = 2;
		preArray[1] = "19/12/2015"; value[1] = 5;
		preArray[2] = "20/12/2015"; value[2] = 9;
		preArray[3] = "21/12/2015"; value[3] = 11;
		preArray[4] = "22/12/2015"; value[4] = 0;
		preArray[5] = "23/12/2015"; value[5] = 8;
		
	    for(int i = 0 ; i < 6 ; i++){
	    	m.put(preArray[i],value[i]);
	    }
		

		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);
		
		//selectedDayMonthYearButton = (Button) this
	//			.findViewById(R.id.selectedDayMonthYear);
		//selectedDayMonthYearButton.setText("Selected: ");

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	/**
	 * 
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		//currentMonth.setTextColor(Color.parseColor("#000000"));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
					+ "Year: " + year);
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
					+ daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is "
					+ getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(prevMonth)
						+ "-"
						+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);
				if (i == getCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
				int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);
            gridcell.setTextSize(10);
			// ACCOUNT FOR SPACING

			Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}
           //gridcell.setBackgroundColor(color);
			// Set the Day GridCell
			gridcell.setText(theday);
			
			//gridcell.setTextColor(Color.parseColor("#000000"));
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
					+ theyear);

			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources()
						.getColor(R.color.lightgray));
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(getResources().getColor(
						R.color.black));
			}
			if (day_color[1].equals("BLUE")) {
				gridcell.setTextColor(getResources().getColor(R.color.blue));
			}
			
			for(int i = 0 ; i < preArray.length ; i++){
				int mon=0;
				  String mont = themonth;
				  if(mont.equals("January"))mon = 1;
					else if(mont.equals("February"))mon = 2;
					else if(mont.equals("March"))mon = 3;
					else if(mont.equals("April"))mon = 4;
					else if(mont.equals("May"))mon = 5;
					else if(mont.equals("June"))mon = 6;
					else if(mont.equals("July"))mon = 7;
					else if(mont.equals("August"))mon = 8;
					else if(mont.equals("September"))mon = 9;
					else if(mont.equals("October"))mon = 10;
					else if(mont.equals("November"))mon = 11;
					else if(mont.equals("December"))mon = 12;
				  String s = theday+"/"+mon+"/"+theyear;
				  if(m.containsKey(s)){
						 if(m.get(s) == 0){	
							gridcell.setTextColor(Color.rgb(255, 0, 0));
					     }
						}
			}
			
			for(int i = 0;i < selected.size(); i++){
				
				String te = theday+"/"+themonth+"/"+theyear;
				if(te.equals(selected.get(i)))gridcell.setTextColor(Color.rgb(0, 204, 0));
				
			   }
			if(count == 0){
			 int min = 100;
			  for(int i = 0 ; i < selected.size(); i++){
				  int mon=0;
				  String new1[] = (selected.get(i)).split("/");
				  if(new1[1].equals("January"))mon = 1;
					else if(new1[1].equals("February"))mon = 2;
					else if(new1[1].equals("March"))mon = 3;
					else if(new1[1].equals("April"))mon = 4;
					else if(new1[1].equals("May"))mon = 5;
					else if(new1[1].equals("June"))mon = 6;
					else if(new1[1].equals("July"))mon = 7;
					else if(new1[1].equals("August"))mon = 8;
					else if(new1[1].equals("September"))mon = 9;
					else if(new1[1].equals("October"))mon = 10;
					else if(new1[1].equals("November"))mon = 11;
					else if(new1[1].equals("December"))mon = 12;
				  String s = new1[0]+"/"+mon+"/"+new1[2];
			    // Toast.makeText(MyCalendarActivity.this, selected.get(i), Toast.LENGTH_SHORT).show();
				  	for(int z = 0 ; z  < preArray.length ; z++){
					if(s.equals(preArray[z]))min = (min > m.get(preArray[z])?m.get(preArray[z]):min);
					//Toast.makeText(MyCalendarActivity.this, min + " ", Toast.LENGTH_SHORT).show();
				}  
				if(i == selected.size()-1)Toast.makeText(MyCalendarActivity.this, min + " ", Toast.LENGTH_SHORT).show();
			}
		}
			count = count + 1;
			return row;
		}

		@Override
		public void onClick(View view) {
			String date_month_year = (String) view.getTag();
			//selectedDayMonthYearButton.setText("Selected: " + date_month_year);
			int mon = 0,was=0,danger=0;
			String[] new1 = date_month_year.split("-");
			if(new1[1].equals("January"))mon = 1;
			else if(new1[1].equals("February"))mon = 2;
			else if(new1[1].equals("March"))mon = 3;
			else if(new1[1].equals("April"))mon = 4;
			else if(new1[1].equals("May"))mon = 5;
			else if(new1[1].equals("June"))mon = 6;
			else if(new1[1].equals("July"))mon = 7;
			else if(new1[1].equals("August"))mon = 8;
			else if(new1[1].equals("September"))mon = 9;
			else if(new1[1].equals("October"))mon = 10;
			else if(new1[1].equals("November"))mon = 11;
			else if(new1[1].equals("December"))mon = 12;
			String day_mon_year = new1[0]+"/" + new1[1] + "/" + new1[2];
			for(int i = 0 ; i < selected.size(); i++){
				if((selected.get(i)).equals(day_mon_year)){selected.remove(i);was++;}
				
			}
			
			String day_mon2 = new1[0] + "/" + mon + "/" + year;
			for(int i = 0 ; i < preArray.length; i++){
				if((day_mon2).equals(preArray[i])){
					//Toast.makeText(MyCalendarActivity.this, day_mon2 , Toast.LENGTH_SHORT).show();
					if(m.get(day_mon2) == 0){
						//Toast.makeText(MyCalendarActivity.this, "you did", Toast.LENGTH_SHORT).show();
						danger++;
						break;
					}
				}
			}
			if(was == 0 && danger == 0)selected.addElement(day_mon_year);
			
			int month1 = mon,year1 = Integer.parseInt(new1[2]);
			count = 0;
			setGridCellAdapterToDate(month1, year1);
			Log.e("Selected date", date_month_year);
			try {
				Date parsedDate = dateFormatter.parse(date_month_year);
				Log.d(tag, "Parsed Date: " + parsedDate.toString());

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
	}
}
