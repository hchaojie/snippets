package cn.hchaojie.snippets.apps.contacts;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class RemoveAccountAcitvity extends Activity {

    private static final String TAG = "QueryContacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AccountManager accMgr = AccountManager.get(this);
//        Account[] allAccounts = accMgr.getAccountsByType(accountType);
        Account[] allAccounts = accMgr.getAccounts();
        
        for (final Account acc : allAccounts) {
            Log.d(TAG, "Existing contact source account: " + acc.toString());
            
            if (!"com.google".equalsIgnoreCase(acc.type)) continue;
            
            final long start = System.currentTimeMillis();
            Log.d(TAG, "PERFORMANCE: start to delete account at " + start);
            AccountManagerFuture<Boolean> deleted = accMgr.removeAccount(acc, new AccountManagerCallback<Boolean>() {
                
                @Override
                public void run(AccountManagerFuture<Boolean> future) {
                    
                    try {
                        
                        Log.d(TAG, "callback future " + (future.toString()));
                        
                        boolean result = future.getResult().booleanValue();
                        
                        Log.d(TAG, "Deleted account with result" + result);
                        
                        long getResultStart = System.currentTimeMillis();
                        Log.d(TAG, "PERFORMANCE: before getResult: " + getResultStart);
                        future.getResult();
                        long getResultEnd = System.currentTimeMillis();
                        Log.d(TAG, "PERFORMANCE: after getResult: " + getResultEnd + " time elapsed: " + (getResultEnd - getResultStart));
                        Log.d(TAG, "PERFORMANCE: time(getResultEnd - start): " + (getResultEnd - start));
                        
                    } catch (OperationCanceledException e) {
                        Log.e(TAG, "Error deleting account " + acc.name, e);
                    } catch (AuthenticatorException e) {
                        Log.e(TAG, "Error deleting account :" + acc.name, e);
                    } catch (IOException e) {
                        Log.e(TAG, "Error deleting account :" + acc.name, e);
                    }
                }
            }, null);
            
            long invokedTime = System.currentTimeMillis();
            Log.d(TAG, "PERFORMANCE: REMOVE invoked end at: " + invokedTime + " time elapsed: " + (invokedTime - start));
            
            Log.d(TAG, "original future " + (deleted.toString()));
        }
    }
}
