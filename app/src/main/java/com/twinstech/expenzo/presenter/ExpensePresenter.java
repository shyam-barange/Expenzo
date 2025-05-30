package com.twinstech.expenzo.presenter;

import static com.twinstech.expenzo.utils.DateUtil.getCurrentDate;

import com.twinstech.expenzo.database.ExpenseDatabaseHelper;
import com.twinstech.expenzo.model.Expense;
import com.twinstech.expenzo.view.ExpenseView;

public class ExpensePresenter {

  private ExpenseDatabaseHelper database;
  private ExpenseView view;

  public ExpensePresenter(ExpenseDatabaseHelper expenseDatabaseHelper, ExpenseView view) {
    this.database = expenseDatabaseHelper;
    this.view = view;
  }

  public boolean addExpense() {
    String amount = view.getAmount();

    if(amount.isEmpty()) {
      view.displayError();
      return false;
    }

    Expense expense = new Expense(Long.valueOf(amount), view.getType(), getCurrentDate());
    database.addExpense(expense);
    return true;
  }

  public void setExpenseTypes() {
    view.renderExpenseTypes(database.getExpenseTypes());
  }
}
