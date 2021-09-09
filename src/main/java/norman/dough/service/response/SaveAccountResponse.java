package norman.dough.service.response;

import norman.dough.domain.Account;
import norman.dough.domain.AccountNumber;

public class SaveAccountResponse {
    private Account savedAccount;
    private AccountNumber savedAccountNumber;

    public Account getSavedAccount() {
        return savedAccount;
    }

    public void setSavedAccount(Account savedAccount) {
        this.savedAccount = savedAccount;
    }

    public AccountNumber getSavedAccountNumber() {
        return savedAccountNumber;
    }

    public void setSavedAccountNumber(AccountNumber savedAccountNumber) {
        this.savedAccountNumber = savedAccountNumber;
    }
}
