package paymentAccountingApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.util.regex.Pattern;

public class main {
    public static void main(String[] args){
        JFrame rootContainer = getFrame();
        rootContainer.setTitle("���������� ����� ��������");
        rootContainer.setDefaultCloseOperation(rootContainer.EXIT_ON_CLOSE);

        JButton button = new JButton("��������");

        JButton buttonNewUser = new JButton("�������� ������ �����������");

        JButton buttonShowBalance = new JButton("����������� �������������");

        JLabel TextFirstPay = new JLabel("����������� ������ �������? �������� ������ ����������� �����:");

        JLabel TextEnterPersonalAccount = new JLabel("������� ����� �������� �����");

        JTextField EnterPersonalAccount = new JTextField();

        JLabel TextEnterTransferAmount = new JLabel("������� ����� ��������");

        JTextField EnterTransferAmount = new JTextField();

        rootContainer.setLayout(null);

        int x = 10;
        int widthLabel = 400; // ����� �������� ������
        int widthButtonEnterCombobox = 200;
        int horizontal_distance = 10; // ���������� ����� ����� ���������� �� ���������
        int x2 = x + widthLabel + horizontal_distance; // ���������� ������� ��������
        int height = 25; //������ ��������
        int distance = 10; // ���������� ����� ���������� �� ���������

        TextFirstPay.setBounds(x, 10, widthLabel, height);
        rootContainer.add(TextFirstPay);
        buttonNewUser.setBounds(x2, TextFirstPay.getY(), widthButtonEnterCombobox, height);
        rootContainer.add(buttonNewUser);

        TextEnterPersonalAccount.setBounds(x, TextFirstPay.getY()+distance+height, widthLabel, height);
        rootContainer.add(TextEnterPersonalAccount);
        EnterPersonalAccount.setBounds(x2, TextFirstPay.getY()+distance+height, widthButtonEnterCombobox, height);
        rootContainer.add(EnterPersonalAccount);

        TextEnterTransferAmount.setBounds(x, TextEnterPersonalAccount.getY()+distance+height, widthLabel, height);
        rootContainer.add(TextEnterTransferAmount);
        EnterTransferAmount.setBounds(x2, TextEnterPersonalAccount.getY()+distance+height, widthButtonEnterCombobox, height);
        rootContainer.add(EnterTransferAmount);

        button.setBounds(x, TextEnterTransferAmount.getY()+distance+height, widthButtonEnterCombobox, height);
        rootContainer.add(button);
        buttonShowBalance.setBounds(x2, TextEnterTransferAmount.getY()+distance+height, widthButtonEnterCombobox, height);
        rootContainer.add(buttonShowBalance);

        rootContainer.setVisible(true);

        DB db = new DB();
        Connection conn = db.getConnection();
        Statement stmt = db.getStatement();
        /*String[] items = db.getServiceProvider(conn, stmt);*/

        buttonShowBalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //�������� ������������ ����� �������� ����� � �����
                Integer TransferAmount = 0;
                Long PersonalAccount = 0l;
                try {
                    PersonalAccount = Long.parseLong(EnterPersonalAccount.getText());

                    // ��������� id_user(primary key) �� �������� �������� �����
                    String condition1 = "account_number='"+PersonalAccount+"'";
                    Integer id_usr = db.getValueOfTheColumnFromTable(stmt, "id_user", "users", condition1);

                    if(id_usr>-1) {
                        // ������ id_service_provider, ������� �������� ����������� ��� ����������� � ������� ������ EnterPersonalAccount
                        String condition2 = "id_user=" + id_usr.toString(); // ���� id_users �� ������� users ����� id_usr(id ������������, ������� ����� ������� ���� EnterPersonalAccount)
                        Integer id_service_prov = db.getValueOfTheColumnFromTable(stmt, "id_service_provider", "users", condition2);

                        function.showBalance(stmt, db, id_usr, id_service_prov, TransferAmount);
                    }else{JOptionPane.showMessageDialog(null, "�������� ����� �"+PersonalAccount+" �� ����������!", "Output", JOptionPane.PLAIN_MESSAGE);}
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null, "������������ ����!", "Output", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //�������� ������������ ����� �������� ����� � �����
                Integer TransferAmount = 0;
                Long PersonalAccount = 0l;
                try {
                    PersonalAccount = Long.parseLong(EnterPersonalAccount.getText());
                    TransferAmount = Integer.parseInt(EnterTransferAmount.getText());

                    // ��������� id_user(primary key) �� �������� �������� �����
                    String condition1 = "account_number='"+PersonalAccount+"'";
                    Integer id_usr = db.getValueOfTheColumnFromTable(stmt, "id_user", "users", condition1);

                    if(id_usr>-1) {
                        // ������ id_service_provider, ������� �������� ����������� ��� ����������� � ������� ������ EnterPersonalAccount
                        String condition2 = "id_user=" + id_usr.toString(); // ���� id_users �� ������� users ����� id_usr(id ������������, ������� ����� ������� ���� EnterPersonalAccount)
                        Integer id_service_prov = db.getValueOfTheColumnFromTable(stmt, "id_service_provider", "users", condition2);

                        function.mainPaymentAndMoneyTransfer(stmt, db, id_usr, id_service_prov, TransferAmount, PersonalAccount);
                    }else{JOptionPane.showMessageDialog(null, "�������� ����� �"+PersonalAccount+" �� ����������!", "������� ���� �� ����������", JOptionPane.PLAIN_MESSAGE);}
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null, "������������ ����! ���� ����� ����� ��������� ������ �������� ��������!", "Output", JOptionPane.PLAIN_MESSAGE);
                }


            }
        });

        buttonNewUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                rootContainer.setVisible(false);
                JFrame NewUser = getFrame();
                NewUser.setTitle("����� ����������� �����");

                JButton buttonNewServiceProvider = new JButton("�������� ������ ���������");

                JLabel TextChooseServiceProvider = new JLabel("�������� ���������� �����");

                String[] items = db.getServiceProvider(stmt);

                JComboBox serviceProvider = new JComboBox(items);

                JLabel TextNotServiceProvider = new JLabel("�� ����� ������ ���������� �����? �������� ������:");

                JLabel TextUserFIO = new JLabel("������� ���:");
                JTextField EnterUserFIO = new JTextField();

                JLabel TextAccountNumber = new JLabel("������� ����� �������� �����:");
                JTextField EnterAccountNumber = new JTextField();

                JButton buttonAddServiceProvider = new JButton("��������");

                NewUser.setLayout(null);

                int xNewUserWindow = 10;
                int widthLabelNewUserWindow = 400; // ����� �������� ������
                int widthButtonEnterComboboxNewUserWindow = 200;
                int horizontalDistanceNewUserWindow = 10; // ���������� ����� ����� ���������� �� ���������
                int x2NewUserWindow = xNewUserWindow + widthLabelNewUserWindow + horizontalDistanceNewUserWindow; // ���������� ������� ��������
                int heightNewUserWindow = 25; //������ ��������
                int distanceNewUserWindow = 10; // ���������� ����� ���������� �� ���������

                TextChooseServiceProvider.setBounds(xNewUserWindow, 10, widthLabelNewUserWindow, heightNewUserWindow);
                NewUser.add(TextChooseServiceProvider);
                serviceProvider.setBounds(x2NewUserWindow, TextChooseServiceProvider.getY(), widthButtonEnterComboboxNewUserWindow, heightNewUserWindow);
                NewUser.add(serviceProvider);

                TextNotServiceProvider.setBounds(xNewUserWindow, TextChooseServiceProvider.getY()+distanceNewUserWindow+heightNewUserWindow, widthLabelNewUserWindow, heightNewUserWindow);
                NewUser.add(TextNotServiceProvider);
                buttonNewServiceProvider.setBounds(x2NewUserWindow, TextChooseServiceProvider.getY()+distanceNewUserWindow+heightNewUserWindow, widthButtonEnterComboboxNewUserWindow, heightNewUserWindow);
                NewUser.add(buttonNewServiceProvider);

                TextUserFIO.setBounds(xNewUserWindow, TextNotServiceProvider.getY()+distanceNewUserWindow+heightNewUserWindow, widthLabelNewUserWindow, heightNewUserWindow);
                NewUser.add(TextUserFIO);
                EnterUserFIO.setBounds(x2NewUserWindow, TextNotServiceProvider.getY()+distanceNewUserWindow+heightNewUserWindow, widthButtonEnterComboboxNewUserWindow, heightNewUserWindow);
                NewUser.add(EnterUserFIO);

                TextAccountNumber.setBounds(xNewUserWindow, TextUserFIO.getY()+distanceNewUserWindow+heightNewUserWindow, widthLabelNewUserWindow, heightNewUserWindow);
                NewUser.add(TextAccountNumber);
                EnterAccountNumber.setBounds(x2NewUserWindow, TextUserFIO.getY()+distanceNewUserWindow+heightNewUserWindow, widthButtonEnterComboboxNewUserWindow, heightNewUserWindow);
                NewUser.add(EnterAccountNumber);

                buttonAddServiceProvider.setBounds((NewUser.getWidth())/2, TextAccountNumber.getY()+distanceNewUserWindow+heightNewUserWindow, widthButtonEnterComboboxNewUserWindow, heightNewUserWindow);
                NewUser.add(buttonAddServiceProvider);

                NewUser.setVisible(true);

                buttonAddServiceProvider.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Long AccountNumber = Long.parseLong(EnterAccountNumber.getText());
                            if(!Pattern.matches("[0-9]+[\\\\.]?[0-9]*", EnterUserFIO.getText()) && !(EnterUserFIO.getText().equals(""))) {
                                if (db.getCount(stmt, "users", "account_number='" + AccountNumber + "';") == 0) {
                                    String condition = "name_service_provider='" + serviceProvider.getSelectedItem() + "'";

                                    //� ������� ������������ "users" ���������� id ���������� �����, ������� ����������� ������������. ���� id �������� �� ������� "service_providers"
                                    Integer idServiceProvider = db.getValueOfTheColumnFromTable(stmt, "id_service_provider", "service_providers", condition);

                                    String SQLCommand = "INSERT INTO users(id_service_provider, fio, account_number) VALUES (" + idServiceProvider.toString() + ", " + "'" + EnterUserFIO.getText() + "', " + EnterAccountNumber.getText() + ");";

                                    db.insertTable(stmt, SQLCommand);
                                    NewUser.dispose();
                                    rootContainer.setVisible(true);
                                } else {
                                    JOptionPane.showMessageDialog(null, "����������� � ������� ������ �" + EnterAccountNumber.getText() + " ��� ����������!", "����������� ��� ����������", JOptionPane.PLAIN_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "������������ ����! ��� �� ����� �������� ������ �� ���� ��� ���� ������!!", "������������ ����", JOptionPane.PLAIN_MESSAGE);
                            }
                        }catch (NumberFormatException e1){
                            JOptionPane.showMessageDialog(null, "������������ ����! ������� ���� ����� �������� ������ �� ����!", "������������ ����", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                });

                NewUser.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        rootContainer.setVisible(true);
                        e.getWindow().dispose();
                    }
                });

                buttonNewServiceProvider.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        NewUser.setVisible(false);
                        JFrame NewServiceProvider = getFrame();
                        NewServiceProvider.setTitle("����� ���������� �����");

                        JLabel TextServiceProviderName = new JLabel("������� �������� ���������� �����:");
                        JTextField EnterServiceProviderName = new JTextField();

                        JLabel TextBankName = new JLabel("������� �������� �����:");
                        JTextField EnterBankName = new JTextField();

                        JLabel TextRCBICBank = new JLabel("������� ��� �����:");
                        JTextField EnterRCBICBank = new JTextField();

                        JLabel TextCurrentAccaunt = new JLabel("������� ��������� ����:");
                        JTextField EnterCurrentAccaunt = new JTextField();

                        JButton buttonAddServiceProvider = new JButton("��������");

                        NewServiceProvider.setLayout(null);

                        int xServiceProviderWindow = 10;
                        int widthLabelServiceProviderWindow = 400; // ����� �������� ������
                        int widthButtonEnterComboboxServiceProviderWindow = 200;
                        int horizontalDistanceServiceProviderWindow = 10; // ���������� ����� ����� ���������� �� ���������
                        int x2ServiceProviderWindow = xServiceProviderWindow + widthLabelServiceProviderWindow + horizontalDistanceServiceProviderWindow; // ���������� ������� ��������
                        int heightServiceProviderWindow = 25; //������ ��������
                        int distanceServiceProviderWindow = 10; // ���������� ����� ���������� �� ���������

                        TextServiceProviderName.setBounds(xServiceProviderWindow, 10, widthLabelServiceProviderWindow, heightServiceProviderWindow);
                        NewServiceProvider.add(TextServiceProviderName);
                        EnterServiceProviderName.setBounds(x2ServiceProviderWindow, TextServiceProviderName.getY(), widthButtonEnterComboboxServiceProviderWindow, heightServiceProviderWindow);
                        NewServiceProvider.add(EnterServiceProviderName);

                        TextBankName.setBounds(xServiceProviderWindow, TextServiceProviderName.getY()+distanceServiceProviderWindow+heightServiceProviderWindow, widthLabelServiceProviderWindow, heightServiceProviderWindow);
                        NewServiceProvider.add(TextBankName);
                        EnterBankName.setBounds(x2ServiceProviderWindow, TextServiceProviderName.getY()+distanceServiceProviderWindow+heightServiceProviderWindow, widthButtonEnterComboboxServiceProviderWindow, heightServiceProviderWindow);
                        NewServiceProvider.add(EnterBankName);

                        TextRCBICBank.setBounds(xServiceProviderWindow, TextBankName.getY()+distanceServiceProviderWindow+heightServiceProviderWindow, widthLabelServiceProviderWindow, heightServiceProviderWindow);
                        NewServiceProvider.add(TextRCBICBank);
                        EnterRCBICBank.setBounds(x2ServiceProviderWindow, TextBankName.getY()+distanceServiceProviderWindow+heightServiceProviderWindow, widthButtonEnterComboboxServiceProviderWindow, heightServiceProviderWindow);
                        NewServiceProvider.add(EnterRCBICBank);

                        TextCurrentAccaunt.setBounds(xServiceProviderWindow,TextRCBICBank.getY()+distanceServiceProviderWindow+heightServiceProviderWindow, widthLabelServiceProviderWindow, heightServiceProviderWindow);
                        NewServiceProvider.add(TextCurrentAccaunt);
                        EnterCurrentAccaunt.setBounds(x2ServiceProviderWindow,TextRCBICBank.getY()+distanceServiceProviderWindow+heightServiceProviderWindow, widthButtonEnterComboboxServiceProviderWindow, heightServiceProviderWindow);
                        NewServiceProvider.add(EnterCurrentAccaunt);

                        buttonAddServiceProvider.setBounds((NewServiceProvider.getWidth())/2, TextCurrentAccaunt.getY()+distanceServiceProviderWindow+heightServiceProviderWindow, widthButtonEnterComboboxServiceProviderWindow, heightServiceProviderWindow);
                        NewServiceProvider.add(buttonAddServiceProvider);

                        NewServiceProvider.setVisible(true);

                        buttonAddServiceProvider.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if((!Pattern.matches("[0-9]+[\\\\.]?[0-9]*", EnterServiceProviderName.getText()))&&(!Pattern.matches("[0-9]+[\\\\.]?[0-9]*", EnterBankName.getText()))) {
                                    if(!(EnterServiceProviderName.getText().equals(""))&&!(EnterBankName.getText().equals(""))) {
                                        try {
                                            String SQLCommand = "INSERT INTO service_providers(name_service_provider, name_bank, rcbic, current_accaunt) " +
                                                    "VALUES ('" + EnterServiceProviderName.getText() + "', " + "'" + EnterBankName.getText() + "', " + "'" + EnterRCBICBank.getText() + "', " + "'" + EnterCurrentAccaunt.getText() + "');";
                                            db.insertTable(stmt, SQLCommand);
                                            NewServiceProvider.dispose();
                                            NewUser.setVisible(true);
                                        } catch (NumberFormatException e1) {
                                            JOptionPane.showMessageDialog(null, "������������ ����! ��� ����� � ��������� ���� ����� �������� ������ �� ����!", "������������ ����", JOptionPane.PLAIN_MESSAGE);
                                        }
                                    }else{
                                        JOptionPane.showMessageDialog(null, "������������ ����! ���� ����� ��� ����� � ���������� ����� �� ����� ���� �������!", "������������ ����", JOptionPane.PLAIN_MESSAGE);
                                    }

                                }else{
                                    JOptionPane.showMessageDialog(null, "������������ ����! �������� ���������� ����� � ������������ ����� �� ����� �������� �� ���� ��� ���� ������!", "������������ ����", JOptionPane.PLAIN_MESSAGE);
                                }

                            }
                        });

                        NewServiceProvider.addWindowListener(new WindowAdapter()
                        {
                            @Override
                            public void windowClosing(WindowEvent e)
                            {
                                NewUser.setVisible(true);
                                e.getWindow().dispose();
                            }
                        });

                    }
                });

            }
        });

    }

    public static JFrame getFrame(){
        JFrame Container = new JFrame();
        Container.setResizable(false);
        Container.setBounds(125, 125, 700, 250);
        return Container;
    }
}
