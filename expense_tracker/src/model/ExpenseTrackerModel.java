package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@code ExpenseTrackerModel} class represents the Model in the MVC
 * (Model-View-Controller) architecture pattern.
 * <p>
 * This class implements the Observer design pattern as the Observable. It
 * maintains a list of {@code ExpenseTrackerModelListener}
 * objects that are notified whenever the state of the model changes, such as
 * when a transaction is added or removed.
 * <p>
 * The {@code ExpenseTrackerModel} class maintains a list of transactions and a
 * list of matched filter indices. It provides
 * methods for adding transactions, applying filters, and managing listeners.
 * <p>
 * When a transaction is added, removed, or a filter is applied, the model
 * updates the list of matched filter indices and then notifies
 * all registered listeners of the change. Each listener's {@code stateChanged}
 * method is called with a {@code ChangeEvent}
 * that contains this model as the source.
 */

public class ExpenseTrackerModel {

  // encapsulation - data integrity
  private List<Transaction> transactions;
  private List<Integer> matchedFilterIndices;
  private List<ExpenseTrackerModelListener> listeners = new ArrayList<ExpenseTrackerModelListener>();

  // This is applying the Observer design pattern.
  // Specifically, this is the Observable class.

  public ExpenseTrackerModel() {
    transactions = new ArrayList<Transaction>();
    matchedFilterIndices = new ArrayList<Integer>();
  }

  /**
   * Adds the given transaction to the list of transactions.
   * Impliments the observer pattern
   * After adding the transaction, the model notifies all subscribed observers
   * (view)
   * 
   * @param transaction the transaction object to add
   */
  public void addTransaction(Transaction t) {
    // Perform input validation to guarantee that all transactions added are
    // non-null.
    if (t == null) {
      throw new IllegalArgumentException("The new transaction must be non-null.");
    }
    this.transactions.add(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();

    // Notify all registered observers about the change
    stateChanged();
  }

  /**
   * Removes the given transaction from the list of transactions.
   * Impliments the observer pattern by notifying all subscribed observers after
   * removing the transaction by calling the update method on each observer with
   * the new model (new state)
   * 
   * @param transaction the transaction object to remove
   */
  public void removeTransaction(Transaction t) {
    this.transactions.remove(t);
    System.out.println("removeTransaction called" + t.getAmount());
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();

    // Notify all registered observers about the change
    stateChanged();
  }

  public List<Transaction> getTransactions() {
    // encapsulation - data integrity
    return Collections.unmodifiableList(new ArrayList<>(transactions));
  }

  /**
   * Sets the matchedFilterIndices to the given list of indices.
   * Impliments the observer pattern by notifying all subscribed observers after
   * setting the matchedFilterIndices by calling the update method on each
   * observer with the new model (new state)
   * 
   * @param list of indices that match the filter
   */
  public void setMatchedFilterIndices(List<Integer> newMatchedFilterIndices) {
    // Perform input validation
    if (newMatchedFilterIndices == null) {
      throw new IllegalArgumentException("The matched filter indices list must be non-null.");
    }
    for (Integer matchedFilterIndex : newMatchedFilterIndices) {
      if ((matchedFilterIndex < 0) || (matchedFilterIndex > this.transactions.size() - 1)) {
        throw new IllegalArgumentException(
            "Each matched filter index must be between 0 (inclusive) and the number of transactions (exclusive).");
      }
    }
    // For encapsulation, copy in the input list
    this.matchedFilterIndices.clear();
    this.matchedFilterIndices.addAll(newMatchedFilterIndices);

    // Notify all registered observers about the change
    stateChanged();
  }

  public List<Integer> getMatchedFilterIndices() {
    // For encapsulation, copy out the output list
    List<Integer> copyOfMatchedFilterIndices = new ArrayList<Integer>();
    copyOfMatchedFilterIndices.addAll(this.matchedFilterIndices);
    return copyOfMatchedFilterIndices;
  }

  /**
   * Registers the given ExpenseTrackerModelListener for
   * state change events.
   *
   * @param listener The ExpenseTrackerModelListener to be registered
   * @return If the listener is non-null and not already registered,
   *         returns true. If not, returns false.
   */
  public boolean register(ExpenseTrackerModelListener listener) {
    // For the Observable class, this is one of the methods.
    //
    // TODO
    if (listener != null && !listeners.contains(listener)) {
      listeners.add(listener);
      System.out.println("listener added");
      System.out.println("listener size" + listeners.size());
      return true;
    }
    return false;

  }

  /**
   * Function to return the number of listeners
   * 
   * @return number of listeners
   */
  public int numberOfListeners() {
    // For testing, this is one of the methods.
    //
    // TODO
    return listeners.size();
  }

  /**
   * Function to check if the given listener is already Subscribed
   * 
   * @param listener
   * @return true if the listener is already Subscribed, false otherwise
   */
  public boolean containsListener(ExpenseTrackerModelListener listener) {
    // For testing, this is one of the methods.
    //
    // TODO
    return listeners.contains(listener);
  }

  /**
   * Function that updates all the subscribed observers (view) with the new
   * model (new state). For each listener, the update method is called with the
   * new model (new state).
   * 
   */
  protected void stateChanged() {
    // For the Observable class, this is one of the methods.
    //
    // TODO
    for (ExpenseTrackerModelListener listener : listeners) {
      listener.update(this);
    }
  }
}
