package paymentAccountingApplication;

import javax.swing.*;
import java.sql.Statement;
import java.util.Arrays;

public class function {

    public static void showBalance(Statement stmt,  DB db, Integer id_usr, Integer id_service_prov, Integer TransferAmount) {
        Integer[] allTrueAmounts = db.getSomeValueOfTheColumnFromTable(stmt, "amount", "payment", "id_user=" + id_usr.toString() + " AND check_payment=true;"); // ������ ���������� �������� ������������
        Integer allTrueAmountsSum = 0;// ������ ���� ���������� ��������
        for (int i = 0; i < allTrueAmounts.length; i++) {
            allTrueAmountsSum += allTrueAmounts[i];
        }

        Integer[] allMoneyTransfer = db.getSomeValueOfTheColumnFromTable(stmt, "amount_transfer", "money_transfer", "id_service_provider=" + id_service_prov.toString() + ";"); // ������ ���� �������� ����, ����������� ������������� ������� ���������� �����
        Integer allMoneyTransferSum = 0; // �����, ������� ������� ������������ �� ��� ����� ������� ����������
        for (int i = 0; i < allMoneyTransfer.length; i++) {
            allMoneyTransferSum += allMoneyTransfer[i];
        }

        // ����� �� ����� ������������ ����� �������� ����� ������������ ��������, �������� �������, �.�. ������, ��������� ������, ������� �� ����� �����������
        Integer balance = allMoneyTransferSum - allTrueAmountsSum;
        Integer lastBalanceAndTransferAmount = TransferAmount; // lastBalanceAndTransferAmount - �����, ������� ���� ������������, + ������� � ������� ���������, ���� ������� �������
        // ��������� � �����, ������� ��� ������������, ������� �������� ������� � �������� ��������
        if (balance > 0) {
            lastBalanceAndTransferAmount += balance;
        }

        Integer[] amounts = db.getSomeValueOfTheColumnFromTable(stmt, "amount", "payment", "id_user=" + id_usr.toString() + " AND check_payment=false;"); // ������ ������������ �������� ������������
        Integer sumAmounts = 0;
        for (int i = 0; i < amounts.length; i++) {
            sumAmounts += amounts[i];
        }

        if ((amounts.length == 1) && (amounts[0]==0)) {
            JOptionPane.showMessageDialog(null, "� ��� ��� ������������ ��������. ��� ������ �� ������ ������: " +lastBalanceAndTransferAmount+ " ���.", "�����������", JOptionPane.PLAIN_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, "���������� ������������ �������� �� �������:"+amounts.length+". ����� ����� ������������� ��������: "+sumAmounts+ " ���. ��� ������ �� ������ ������: " +lastBalanceAndTransferAmount+ " ���.", "�����������", JOptionPane.PLAIN_MESSAGE);
        }
    }
    public static void mainPaymentAndMoneyTransfer(Statement stmt,  DB db, Integer id_usr, Integer id_service_prov, Integer TransferAmount, Long PersonalAccount){
        // ��������� id_user(primary key) �� �������� �������� �����


        //��� ������� amount � id_payment. amount[i] ������������� id_payment[i]
        Integer[] allTrueAmounts = db.getSomeValueOfTheColumnFromTable(stmt, "amount", "payment", "id_user="+id_usr.toString()+" AND check_payment=true;"); // ������ ���������� �������� ������������
        Integer allTrueAmountsSum = 0;// ������ ���� ���������� ��������
        for(int i=0; i<allTrueAmounts.length; i++){
            allTrueAmountsSum+=allTrueAmounts[i];
        }

        Integer[] allMoneyTransfer = db.getSomeValueOfTheColumnFromTable(stmt, "amount_transfer", "money_transfer", "id_service_provider="+id_service_prov.toString()+";"); // ������ ���� �������� ����, ����������� ������������� ������� ���������� �����
        Integer allMoneyTransferSum = 0; // �����, ������� ������� ������������ �� ��� ����� ������� ����������
        for(int i=0; i<allMoneyTransfer.length; i++){
            allMoneyTransferSum += allMoneyTransfer[i];
        }

        // ����� �� ����� ������������ ����� �������� ����� ������������ ��������, �������� �������, �.�. ������, ��������� ������, ������� �� ����� �����������
        Integer balance = allMoneyTransferSum-allTrueAmountsSum;
        Integer lastBalanceAndTransferAmount = TransferAmount; // lastBalanceAndTransferAmount - �����, ������� ���� ������������, + ������� � ������� ���������, ���� ������� �������
        // ��������� � �����, ������� ��� ������������, ������� �������� ������� � �������� ��������
        if(balance>0){lastBalanceAndTransferAmount+=balance;}


        Integer[] amounts = db.getSomeValueOfTheColumnFromTable(stmt, "amount", "payment", "id_user="+id_usr.toString()+" AND check_payment=false;"); // ������ ������������ �������� ������������
        Integer sumAmounts = 0;
        for(int i=0; i<amounts.length; i++){
            sumAmounts += amounts[i];
        }
        Integer[] id_payment = db.getSomeValueOfTheColumnFromTable(stmt, "id_payment", "payment", "id_user="+id_usr.toString()+" AND check_payment=false;"); // ������ primary key(ID �������) ���� �������� ������������

        if((amounts[0]==0) && (allTrueAmounts[0]==0)){
            JOptionPane.showMessageDialog(null, TransferAmount+" ���. ������� ��������� �� ���� "+PersonalAccount+". ���� ��� ��������� ����� �� �������� ��� �� ������ �������.", "�����������", JOptionPane.PLAIN_MESSAGE);
        }
        else if(lastBalanceAndTransferAmount==sumAmounts){
            String condition = "";
            for(int i =0; i<id_payment.length-1; i++){
                condition = condition + "id_payment="+id_payment[i].toString()+" OR ";
            }
            condition = condition + "id_payment="+id_payment[id_payment.length-1].toString()+";";
            db.setValue(stmt, "payment", "true", "check_payment", condition);
            JOptionPane.showMessageDialog(null, TransferAmount+" ���. ������� ��������� �� ���� "+PersonalAccount+". ��� ������ �� ������ ������: 0 ���.", "�����������", JOptionPane.PLAIN_MESSAGE);
            //JOptionPane.showMessageDialog(null, EnterTransferAmount.getText()+" ���. ������� ��������� �� ���� "+EnterPersonalAccount.getText()+". ��� ������ �� ������ ������: 0 ���.", "Output", JOptionPane.PLAIN_MESSAGE);
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
                JOptionPane.showMessageDialog(null, TransferAmount + " ���. ������� ��������� �� ���� " + PersonalAccount + ". ��� ������ �� ������ ������: " + newBalance.toString() + "���.", "�����������", JOptionPane.PLAIN_MESSAGE);
            }
            else{
                condition = "id_payment="+id_payment[0].toString()+";";
                db.setValue(stmt, "payment", "true", "check_payment", condition);
                Integer newBalance = lastBalanceAndTransferAmount - sumAmounts;
                JOptionPane.showMessageDialog(null, TransferAmount + " ���. ������� ��������� �� ���� " + PersonalAccount + ". ��� ������ �� ������ ������: " + newBalance.toString() + "���.", "�����������", JOptionPane.PLAIN_MESSAGE);
            }
        }else {
            //��������������� ����� ������� amounts
            Integer[] sort_amounts = amounts.clone();
            Arrays.sort(sort_amounts);
            Integer count = 0;  // ���������� ��������, ������� �� ����� ��������

            // ����� �� ������ ���������� TransferAmount, ���������� �������� ������������ �����, �������� ����� ���� ����������
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
                JOptionPane.showMessageDialog(null, TransferAmount+" ���. ������� ��������� �� ���� "+PersonalAccount+". ���������� ������������ �������� �� �������: "+count.toString()+". �����, ����������� ��� ��������� �������������: "+ost.toString()+" ���.", "������������ �������", JOptionPane.PLAIN_MESSAGE);
            }
            else{
                //Integer NewTrueAmounts = amounts[Arrays.asList(amounts).indexOf(sort_amounts[0])];
                Integer ost = sumAmounts -lastBalanceAndTransferAmount;
                condition = "id_payment=" + id_payment[Arrays.asList(amounts).indexOf(sort_amounts[0])].toString()+";";
                db.setValue(stmt, "payment", "true", "check_payment", condition);
                JOptionPane.showMessageDialog(null, TransferAmount+" ���. ������� ��������� �� ���� "+PersonalAccount+". �� ������ ������ � ��� ���� ������������ ������ �� ����� "+ost.toString(), "������������ ������", JOptionPane.PLAIN_MESSAGE);
            }
        }
        // ���������� ������ �������� �������� ������� � ������� ��������� �������� �������
        String SQLCommand = "INSERT INTO money_transfer(id_service_provider, amount_transfer) VALUES("+id_service_prov.toString()+", "+TransferAmount.toString()+");";
        db.insertTable(stmt, SQLCommand);
    }
}
