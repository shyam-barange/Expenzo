package com.twinstech.expenzo.presenter;

import com.twinstech.expenzo.database.ExpenseDatabaseHelper;
import com.twinstech.expenzo.utils.ExpenseCollection;
import com.twinstech.expenzo.view.CurrentWeekExpenseView;

public class CurrentWeekExpensePresenter {

  private CurrentWeekExpenseView view;
  private ExpenseDatabaseHelper database;
  private ExpenseCollection expenseCollection;

  public CurrentWeekExpensePresenter(ExpenseDatabaseHelper database, CurrentWeekExpenseView view) {
    this.database = database;
    this.view = view;
    expenseCollection = new ExpenseCollection(this.database.getCurrentWeeksExpenses());
  }

  public void renderTotalExpenses() {
    view.displayTotalExpenses(expenseCollection.getTotalExpense());
  }

  public void renderCurrentWeeksExpenses() {
    view.displayCurrentWeeksExpenses(expenseCollection.groupByDate());
  }
}
