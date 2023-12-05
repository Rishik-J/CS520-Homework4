package controller;

import view.ExpenseTrackerView;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.TransactionFilter;

/**
 * The {@code ExpenseTrackerController} class represents the Controller in the
 * MVC (Model-View-Controller) architecture pattern.
 * <p>
 * This class is responsible for handling user input and updating the
 * {@code ExpenseTrackerModel}. It also applies filters to the
 * transactions in the model using the Strategy design pattern.
 * <p>
 * In a traditional MVC architecture, the Controller is responsible for updating
 * the View. However, in this implementation, the
 * Model updates the View directly. This is achieved by making the View an
 * observer of the Model. When the state of the Model changes,
 * it notifies all registered observers, which includes the View. This allows
 * the View to update itself based on the new state of the Model.
 * <p>
 * This change simplifies the Controller's role and improves the separation of
 * concerns in the application. The Controller is solely
 * responsible for handling user input and updating the Model, while the Model
 * is responsible for maintaining its state and notifying
 * observers of changes.
 */

public class ExpenseTrackerController {

  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  /**
   * The Controller is applying the Strategy design pattern.
   * This is the has-a relationship with the Strategy class
   * being used in the applyFilter method.
   */
  private TransactionFilter filter;

  public ExpenseTrackerController(ExpenseTrackerModel model, ExpenseTrackerView view) {
    this.model = model;
    this.view = view;
    // For the MVC architecture pattern, the Observer design pattern is being
    // used to update the View after manipulating the Model.
    this.model.register(this.view);
  }

  public void setFilter(TransactionFilter filter) {
    // Sets the Strategy class being used in the applyFilter method.
    this.filter = filter;
  }

  /**
   * This method is called when the user clicks the "Add Transaction" button.
   * Calls the model to add the transaction
   * Calls model to impliment observer pattern
   * 
   * @param amount
   * @param category
   * @return true if the transaction was added, false otherwise
   */
  public boolean addTransaction(double amount, String category) {
    if (!InputValidation.isValidAmount(amount)) {
      return false;
    }
    if (!InputValidation.isValidCategory(category)) {
      return false;
    }

    Transaction t = new Transaction(amount, category);
    model.addTransaction(t);
    return true;
  }

  /**
   * This method is called when the user clicks the "Apply Category Filter"
   * button.
   * Calls the model to set the matchedFilterIndices
   * Calls model to impliment observer pattern
   */
  public void applyFilter() {
    // null check for filter
    if (filter != null) {
      // Use the Strategy class to perform the desired filtering
      List<Transaction> transactions = model.getTransactions();
      List<Transaction> filteredTransactions = filter.filter(transactions);
      List<Integer> rowIndexes = new ArrayList<>();
      for (Transaction t : filteredTransactions) {
        int rowIndex = transactions.indexOf(t);
        if (rowIndex != -1) {
          rowIndexes.add(rowIndex);
        }
      }
      model.setMatchedFilterIndices(rowIndexes);
    } else {
      JOptionPane.showMessageDialog(view, "No filter applied");
      view.toFront();
    }

  }

  /**
   * This method is called when the user clicks the undo button.
   * Calls the model to remove the transaction at the given index
   * Calls model to impliment observer pattern
   * 
   * @param rowIndex
   * @return true if the transaction was removed, false otherwise
   */
  public boolean undoTransaction(int rowIndex) {
    if (rowIndex >= 0 && rowIndex < model.getTransactions().size()) {
      Transaction removedTransaction = model.getTransactions().get(rowIndex);
      model.removeTransaction(removedTransaction);
      return true;
    }

    // The undo was disallowed.
    return false;
  }
}
