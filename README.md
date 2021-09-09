# Dough

## Personal finance software

This is software I wrote to track my personal finances. If you use this for real money stuff, do so at your own risk. I
assume NO responsibility.

**IF THIS SCREWS UP YOUR FINANCES, DON'T BLAME ME!**   

## To Build

To checkout Flunky and compile it, execute the following commands:

    cd <workspace-dir>
    git clone git@github.com:lynchnf/dough.git
    cd dough
    git checkout main
    mvn clean package
    
## Install Database

## TODO

|  x  | Pts | Task
| --- | --- | ---
|     |  1  | Make close date in statement required.
|     |  1  | Make post date in transaction required.
|  x  |  1  | Format code in flunky branch.
|  x  |  1  | Put Flunky generation files for Dough in Dough project.
|  x  |  3  | Create account should create account number.
|  x  |  3  | Create account should create current statement, opening statement, and beginning balance transaction.
|  x  |  3  | Update account allow addition of new account number.
|     |  2  | Create transaction.
|     |  2  | Create transfer.
|     |  2  | Validate cron string in recurring schedule.
|     |  5  | Reconcile account.
|  x  |  2  | Remove deletes.
|     |  3  | Real database.


