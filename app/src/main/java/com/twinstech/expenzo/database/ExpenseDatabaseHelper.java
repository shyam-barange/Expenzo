package com.twinstech.expenzo.database;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static com.twinstech.expenzo.utils.DateUtil.getCurrentDate;
import static com.twinstech.expenzo.utils.DateUtil.getCurrentWeeksDates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.twinstech.expenzo.model.Expense;
import com.twinstech.expenzo.model.ExpenseType;
import com.twinstech.expenzo.table.ExpenseTable;
import com.twinstech.expenzo.table.ExpenseTypeTable;
import com.twinstech.expenzo.utils.DateUtil;

public class ExpenseDatabaseHelper extends SQLiteOpenHelper {
  public static final String EXPENSE_DB = "expense";

  public ExpenseDatabaseHelper(Context context) {
    super(context, EXPENSE_DB, null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(ExpenseTable.CREATE_TABLE_QUERY);
    sqLiteDatabase.execSQL(ExpenseTypeTable.CREATE_TABLE_QUERY);
    seedExpenseTypes(sqLiteDatabase);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

  }

  public List<String> getExpenseTypes() {
    ArrayList<String> expenseTypes = new ArrayList<>();

    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTypeTable.SELECT_ALL, null);

    if(isCursorPopulated(cursor)){
      do {
      int typeIndex = cursor.getColumnIndex(ExpenseTypeTable.TYPE);
        String type = typeIndex != -1 ? cursor.getString(typeIndex) : null;
        expenseTypes.add(type);
      } while(cursor.moveToNext());
    }

    return expenseTypes;
  }

  public void deleteAll() {
    SQLiteDatabase database = this.getWritableDatabase();
    database.delete(ExpenseTypeTable.TABLE_NAME, "", new String[]{});
    database.delete(ExpenseTable.TABLE_NAME, "", new String[]{});
    database.close();
  }

  public void addExpense(Expense expense) {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(ExpenseTable.AMOUNT, expense.getAmount());
    values.put(ExpenseTable.TYPE, expense.getType());
    values.put(ExpenseTable.DATE, expense.getDate());

    database.insert(ExpenseTable.TABLE_NAME, null, values);
  }

  public List<Expense> getExpenses() {
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTable.SELECT_ALL, null);

    return buildExpenses(cursor);
  }

  public List<Expense> getTodaysExpenses() {
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTable.getExpensesForDate(getCurrentDate()), null);

    return buildExpenses(cursor);
  }

  public List<Expense> getCurrentWeeksExpenses() {
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTable.getConsolidatedExpensesForDates(getCurrentWeeksDates()), null);
    return buildExpenses(cursor);
  }

  public List<Expense> getExpensesGroupByCategory() {
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTable.SELECT_ALL_GROUP_BY_CATEGORY, null);
    return buildExpenses(cursor);
  }

  public List<Expense> getExpensesForCurrentMonthGroupByCategory() {
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTable.getExpenseForCurrentMonth(DateUtil.currentMonthOfYear()), null);
    return buildExpenses(cursor);
  }

  public void addExpenseType(ExpenseType type) {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(ExpenseTable.TYPE, type.getType());

    database.insert(ExpenseTypeTable.TABLE_NAME, null, values);
  }

  public void truncate(String tableName) {
    SQLiteDatabase database = this.getWritableDatabase();
    database.execSQL("delete from " + tableName);
  }

  private List<Expense> buildExpenses(Cursor cursor) {
    List<Expense> expenses = new ArrayList<>();
    if(isCursorPopulated(cursor)){
      do {
       int typeIndex = cursor.getColumnIndex(ExpenseTable.TYPE);
       String type = typeIndex != -1 ? cursor.getString(typeIndex) : null;

       int amountIndex = cursor.getColumnIndex(ExpenseTable.AMOUNT);
       String amount = amountIndex != -1 ? cursor.getString(amountIndex) : null;

       int dateIndex = cursor.getColumnIndex(ExpenseTable.DATE);
       String date = dateIndex != -1 ? cursor.getString(dateIndex) : null;

       int idIndex = cursor.getColumnIndex(ExpenseTable._ID);
       String id = idIndex != -1 ? cursor.getString(idIndex) : null;
        Expense expense = id == null ? new Expense(parseLong(amount), type, date) : new Expense(parseInt(id), parseLong(amount), type, date);
        expenses.add(expense);
      } while(cursor.moveToNext());
    }

    return expenses;
  }

  private boolean isCursorPopulated(Cursor cursor) {
    return cursor != null && cursor.moveToFirst();
  }

  private void seedExpenseTypes(SQLiteDatabase sqLiteDatabase) {
    List<ExpenseType> expenseTypes = ExpenseTypeTable.seedData();
    for (ExpenseType expenseType : expenseTypes) {
      ContentValues contentValues = new ContentValues();
      contentValues.put(ExpenseTypeTable.TYPE, expenseType.getType());

      sqLiteDatabase.insert(ExpenseTypeTable.TABLE_NAME, null, contentValues);
    }
  }
}
