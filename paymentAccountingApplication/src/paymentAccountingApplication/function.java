package paymentAccountingApplication;

import javax.swing.*;
import java.sql.Statement;
import java.util.Arrays;

public class function {

    public static void showBalance(Statement stmt,  DB db, Integer id_usr, Integer id_service_prov, Integer TransferAmount) {
        Integer[] allTrueAmounts = db.getSomeValueOfTheColumnFromTable(stmt, "amount", "payment", "id_user=" + id_usr.toString() + " AND check_payment=true;"); // массив ОПЛАЧЕННЫХ платежей пользователя
        Integer allTrueAmountsSum = 0;// массив сумм ОПЛАЧЕННЫХ платежей
        for (int i = 0; i < allTrueAmounts.length; i++) {
            allTrueAmountsSum += allTrueAmounts[i];
        }

        Integer[] allMoneyTransfer = db.getSomeValueOfTheColumnFromTable(stmt, "amount_transfer", "money_transfer", "id_service_provider=" + id_service_prov.toString() + ";"); // массив всех денежных сумм, переведённых пользователем данному поставщику услуг
        Integer allMoneyTransferSum = 0; // сумма, которую перевел пользователь за все время данному поставщику
        for (int i = 0; i < allMoneyTransfer.length; i++) {
            allMoneyTransferSum += allMoneyTransfer[i];
        }

        // когда из суммы переведенных денег вычитаем сумму переведенных платежей, получаем остаток, т.е. лишние, свободные деньги, лежащие на счету потребителя
        Integer balance = allMoneyTransferSum - allTrueAmountsSum;
        Integer lastBalanceAndTransferAmount = TransferAmount; // lastBalanceAndTransferAmount - сумма, которую ввел пользователь, + остаток с прошлых переводов, если таковой имеется
        // добавляем к сумме, которую ввёл пользователь, остаток денежных средств с прошлого перевода
        if (balance > 0) {
            lastBalanceAndTransferAmount += balance;
        }

        Integer[] amounts = db.getSomeValueOfTheColumnFromTable(stmt, "amount", "payment", "id_user=" + id_usr.toString() + " AND check_payment=false;"); // массив НЕОПЛАЧЕННЫХ платежей пользователя
        Integer sumAmounts = 0;
        for (int i = 0; i < amounts.length; i++) {
            sumAmounts += amounts[i];
        }

        if ((amounts.length == 1) && (amounts[0]==0)) {
            JOptionPane.showMessageDialog(null, "У вас нет неоплаченных платежей. Ваш баланс на данный момент: " +lastBalanceAndTransferAmount+ " руб.", "Уведомление", JOptionPane.PLAIN_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, "Количество неоплаченных платежей на сегодня:"+amounts.length+". Общая сумма непереведённых платежей: "+sumAmounts+ " руб. Ваш баланс на данный момент: " +lastBalanceAndTransferAmount+ " руб.", "Уведомление", JOptionPane.PLAIN_MESSAGE);
        }
    }
    public static void mainPaymentAndMoneyTransfer(Statement stmt,  DB db, Integer id_usr, Integer id_service_prov, Integer TransferAmount, Long PersonalAccount){
        // получение id_user(primary key) по значению лицевого счета


        //два массива amount и id_payment. amount[i] соответствует id_payment[i]
        Integer[] allTrueAmounts = db.getSomeValueOfTheColumnFromTable(stmt, "amount", "payment", "id_user="+id_usr.toString()+" AND check_payment=true;"); // массив ОПЛАЧЕННЫХ платежей пользователя
        Integer allTrueAmountsSum = 0;// массив сумм ОПЛАЧЕННЫХ платежей
        for(int i=0; i<allTrueAmounts.length; i++){
            allTrueAmountsSum+=allTrueAmounts[i];
        }

        Integer[] allMoneyTransfer = db.getSomeValueOfTheColumnFromTable(stmt, "amount_transfer", "money_transfer", "id_service_provider="+id_service_prov.toString()+";"); // массив всех денежных сумм, переведённых пользователем данному поставщику услуг
        Integer allMoneyTransferSum = 0; // сумма, которую перевел пользователь за все время данному поставщику
        for(int i=0; i<allMoneyTransfer.length; i++){
            allMoneyTransferSum += allMoneyTransfer[i];
        }

        // когда из суммы переведенных денег вычитаем сумму переведенных платежей, получаем остаток, т.е. лишние, свободные деньги, лежащие на счету потребителя
        Integer balance = allMoneyTransferSum-allTrueAmountsSum;
        Integer lastBalanceAndTransferAmount = TransferAmount; // lastBalanceAndTransferAmount - сумма, которую ввел пользователь, + остаток с прошлых переводов, если таковой имеется
        // добавляем к сумме, которую ввёл пользователь, остаток денежных средств с прошлого перевода
        if(balance>0){lastBalanceAndTransferAmount+=balance;}


        Integer[] amounts = db.getSomeValueOfTheColumnFromTable(stmt, "amount", "payment", "id_user="+id_usr.toString()+" AND check_payment=false;"); // массив НЕОПЛАЧЕННЫХ платежей пользователя
        Integer sumAmounts = 0;
        for(int i=0; i<amounts.length; i++){
            sumAmounts += amounts[i];
        }
        Integer[] id_payment = db.getSomeValueOfTheColumnFromTable(stmt, "id_payment", "payment", "id_user="+id_usr.toString()+" AND check_payment=false;"); // массив primary key(ID платежа) всех платежей пользователя

        if((amounts[0]==0) && (allTrueAmounts[0]==0)){
            JOptionPane.showMessageDialog(null, TransferAmount+" руб. успешно зачислены на счет "+PersonalAccount+". Пока что поставщик услуг не выставил вам ни одного платежа.", "Уведомление", JOptionPane.PLAIN_MESSAGE);
        }
        else if(lastBalanceAndTransferAmount==sumAmounts){
            String condition = "";
            for(int i =0; i<id_payment.length-1; i++){
                condition = condition + "id_payment="+id_payment[i].toString()+" OR ";
            }
            condition = condition + "id_payment="+id_payment[id_payment.length-1].toString()+";";
            db.setValue(stmt, "payment", "true", "check_payment", condition);
            JOptionPane.showMessageDialog(null, TransferAmount+" руб. успешно зачислены на счет "+PersonalAccount+". Ваш баланс на данный момент: 0 руб.", "Уведомление", JOptionPane.PLAIN_MESSAGE);
            //JOptionPane.showMessageDialog(null, EnterTransferAmount.getText()+" руб. успешно зачислены на счет "+EnterPersonalAccount.getText()+". Ваш баланс на данный момент: 0 руб.", "Output", JOptionPane.PLAIN_MESSAGE);
        }
        else if(lastBalanceAndTransferAmount>sumAmounts){
            String condition = "";
            if(id_payment.length>1) {
                for (int i = 0; i < id_payment.length - 1; i++) {
                    condition = condition + "id_payment=" + id_payment[i].toString() + " OR ";
                }
                condition = condition + "id_payment=" + id_payment[id_payment.length - 1].toString() + ";";
                db.setValue(stmt, "payment", "true", "check_payment", condition);
                Integer newBalance = lastBalanceAndTransferAmount - sumAmounts;
                JOptionPane.showMessageDialog(null, TransferAmount + " руб. успешно зачислены на счет " + PersonalAccount + ". Ваш баланс на данный момент: " + newBalance.toString() + "руб.", "Уведомление", JOptionPane.PLAIN_MESSAGE);
            }
            else{
                condition = "id_payment="+id_payment[0].toString()+";";
                db.setValue(stmt, "payment", "true", "check_payment", condition);
                Integer newBalance = lastBalanceAndTransferAmount - sumAmounts;
                JOptionPane.showMessageDialog(null, TransferAmount + " руб. успешно зачислены на счет " + PersonalAccount + ". Ваш баланс на данный момент: " + newBalance.toString() + "руб.", "Уведомление", JOptionPane.PLAIN_MESSAGE);
            }
        }else {
            //отсортированная копия массива amounts
            Integer[] sort_amounts = amounts.clone();
            Arrays.sort(sort_amounts);
            Integer count = 0;  // количество платежей, которые мы можем оплатить

            // чтобы не менять переменную TransferAmount, содержащую введённую потребителем сумму, создадим копию этой переменной
            Integer duplicateLastBalanceAndTransferAmount = lastBalanceAndTransferAmount;
            for(int i=0; i<sort_amounts.length; i++){
                duplicateLastBalanceAndTransferAmount -= sort_amounts[i];
                if(duplicateLastBalanceAndTransferAmount>0){count+=1;}
                else if(duplicateLastBalanceAndTransferAmount==0){count+=1;break;}
                else{
                    break;
                }
            }
            String condition = "";
            Integer sumNewTrueAmounts = 0;
            if(count>1) {
                for (int i = 0; i < count-1; i++) {
                    sumNewTrueAmounts = sumNewTrueAmounts+ amounts[Arrays.asList(amounts).indexOf(sort_amounts[i])];
                    condition = condition + "id_payment=" + id_payment[Arrays.asList(amounts).indexOf(sort_amounts[i])].toString() + " OR ";
                }
                sumNewTrueAmounts = sumNewTrueAmounts + amounts[Arrays.asList(amounts).indexOf(sort_amounts[count-1])];
                condition = condition + "id_payment=" + id_payment[Arrays.asList(amounts).indexOf(sort_amounts[count-1])].toString() + ";";
                db.setValue(stmt, "payment", "true", "check_payment", condition);
                Integer ost = sumAmounts-sumNewTrueAmounts;
                JOptionPane.showMessageDialog(null, TransferAmount+" руб. успешно зачислены на счет "+PersonalAccount+". Количество неоплаченных платежей на сегодня: "+count.toString()+". Сумма, необходимая для погошения задолженности: "+ost.toString()+" руб.", "Неоплаченные платежи", JOptionPane.PLAIN_MESSAGE);
            }
            else{
                //Integer NewTrueAmounts = amounts[Arrays.asList(amounts).indexOf(sort_amounts[0])];
                Integer ost = sumAmounts -lastBalanceAndTransferAmount;
                condition = "id_payment=" + id_payment[Arrays.asList(amounts).indexOf(sort_amounts[0])].toString()+";";
                db.setValue(stmt, "payment", "true", "check_payment", condition);
                JOptionPane.showMessageDialog(null, TransferAmount+" руб. успешно зачислены на счет "+PersonalAccount+". На данный момент у вас один неоплаченный платеж на сумму "+ost.toString(), "Неоплаченный платеж", JOptionPane.PLAIN_MESSAGE);
            }
        }
        // добавление нового перевода денежных средств в таблицу переводов денежных средств
        String SQLCommand = "INSERT INTO money_transfer(id_service_provider, amount_transfer) VALUES("+id_service_prov.toString()+", "+TransferAmount.toString()+");";
        db.insertTable(stmt, SQLCommand);
    }
}
