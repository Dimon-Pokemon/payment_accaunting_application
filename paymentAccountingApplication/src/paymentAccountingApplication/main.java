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
        rootContainer.setTitle("Приложение учёта платежей");
        rootContainer.setDefaultCloseOperation(rootContainer.EXIT_ON_CLOSE);

        JButton button = new JButton("Оплатить");

        JButton buttonNewUser = new JButton("Добавить нового потребителя");

        JButton buttonShowBalance = new JButton("Просмотреть задолженности");

        JLabel TextFirstPay = new JLabel("Оплачиваете услугу впервые? Добавьте нового потребителя услуг:");

        JLabel TextEnterPersonalAccount = new JLabel("Введите номер лицевого счета");

        JTextField EnterPersonalAccount = new JTextField();

        JLabel TextEnterTransferAmount = new JLabel("Введите сумму перевода");

        JTextField EnterTransferAmount = new JTextField();

        rootContainer.setLayout(null);

        int x = 10;
        int widthLabel = 400; // длина элемента текста
        int widthButtonEnterCombobox = 200;
        int horizontal_distance = 10; // расстояние между двумя элементами по диагонали
        int x2 = x + widthLabel + horizontal_distance; // координата парного элемента
        int height = 25; //высота элемента
        int distance = 10; // расстояние между элементами по вертикали

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
                //проверка корректности ввода лицевого счета и суммы
                Integer TransferAmount = 0;
                Long PersonalAccount = 0l;
                try {
                    PersonalAccount = Long.parseLong(EnterPersonalAccount.getText());

                    // получение id_user(primary key) по значению лицевого счета
                    String condition1 = "account_number='"+PersonalAccount+"'";
                    Integer id_usr = db.getValueOfTheColumnFromTable(stmt, "id_user", "users", condition1);

                    if(id_usr>-1) {
                        // Узнаем id_service_provider, который является поставщиком для потребителя с лицевым счетом EnterPersonalAccount
                        String condition2 = "id_user=" + id_usr.toString(); // если id_users из таблицы users равен id_usr(id пользователя, который имеет лицевой счёт EnterPersonalAccount)
                        Integer id_service_prov = db.getValueOfTheColumnFromTable(stmt, "id_service_provider", "users", condition2);

                        function.showBalance(stmt, db, id_usr, id_service_prov, TransferAmount);
                    }else{JOptionPane.showMessageDialog(null, "Лицевого счета №"+PersonalAccount+" не существует!", "Output", JOptionPane.PLAIN_MESSAGE);}
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null, "Некорректный ввод!", "Output", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //проверка корректности ввода лицевого счета и суммы
                Integer TransferAmount = 0;
                Long PersonalAccount = 0l;
                try {
                    PersonalAccount = Long.parseLong(EnterPersonalAccount.getText());
                    TransferAmount = Integer.parseInt(EnterTransferAmount.getText());

                    // получение id_user(primary key) по значению лицевого счета
                    String condition1 = "account_number='"+PersonalAccount+"'";
                    Integer id_usr = db.getValueOfTheColumnFromTable(stmt, "id_user", "users", condition1);

                    if(id_usr>-1) {
                        // Узнаем id_service_provider, который является поставщиком для потребителя с лицевым счетом EnterPersonalAccount
                        String condition2 = "id_user=" + id_usr.toString(); // если id_users из таблицы users равен id_usr(id пользователя, который имеет лицевой счёт EnterPersonalAccount)
                        Integer id_service_prov = db.getValueOfTheColumnFromTable(stmt, "id_service_provider", "users", condition2);

                        function.mainPaymentAndMoneyTransfer(stmt, db, id_usr, id_service_prov, TransferAmount, PersonalAccount);
                    }else{JOptionPane.showMessageDialog(null, "Лицевого счета №"+PersonalAccount+" не существует!", "Лицевой счет не существует", JOptionPane.PLAIN_MESSAGE);}
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null, "Некорректный ввод! Поля ввода могут содержать только числовые значения!", "Output", JOptionPane.PLAIN_MESSAGE);
                }


            }
        });

        buttonNewUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                rootContainer.setVisible(false);
                JFrame NewUser = getFrame();
                NewUser.setTitle("Новый потребитель услуг");

                JButton buttonNewServiceProvider = new JButton("Добавить нового постащика");

                JLabel TextChooseServiceProvider = new JLabel("Выберите поставщика услуг");

                String[] items = db.getServiceProvider(stmt);

                JComboBox serviceProvider = new JComboBox(items);

                JLabel TextNotServiceProvider = new JLabel("Не нашли своего поставщика услуг? Добавьте нового:");

                JLabel TextUserFIO = new JLabel("ВВедите ФИО:");
                JTextField EnterUserFIO = new JTextField();

                JLabel TextAccountNumber = new JLabel("ВВедите номер лицевого счета:");
                JTextField EnterAccountNumber = new JTextField();

                JButton buttonAddServiceProvider = new JButton("Добавить");

                NewUser.setLayout(null);

                int xNewUserWindow = 10;
                int widthLabelNewUserWindow = 400; // длина элемента текста
                int widthButtonEnterComboboxNewUserWindow = 200;
                int horizontalDistanceNewUserWindow = 10; // расстояние между двумя элементами по диагонали
                int x2NewUserWindow = xNewUserWindow + widthLabelNewUserWindow + horizontalDistanceNewUserWindow; // координата парного элемента
                int heightNewUserWindow = 25; //высота элемента
                int distanceNewUserWindow = 10; // расстояние между элементами по вертикали

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

                                    //в таблице потребителей "users" содержится id поставщика услуг, который обслуживает пользователя. Этот id получаем из таблицы "service_providers"
                                    Integer idServiceProvider = db.getValueOfTheColumnFromTable(stmt, "id_service_provider", "service_providers", condition);

                                    String SQLCommand = "INSERT INTO users(id_service_provider, fio, account_number) VALUES (" + idServiceProvider.toString() + ", " + "'" + EnterUserFIO.getText() + "', " + EnterAccountNumber.getText() + ");";

                                    db.insertTable(stmt, SQLCommand);
                                    NewUser.dispose();
                                    rootContainer.setVisible(true);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Потребитель с лицевым счетом №" + EnterAccountNumber.getText() + " уже существует!", "Потребитель уже существует", JOptionPane.PLAIN_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "Некорректный ввод! ФИО не может состоять только из цифр или быть пустым!!", "Некорректный ввод", JOptionPane.PLAIN_MESSAGE);
                            }
                        }catch (NumberFormatException e1){
                            JOptionPane.showMessageDialog(null, "Некорректный ввод! Лицевой счет может состоять только из цифр!", "Некорректный ввод", JOptionPane.PLAIN_MESSAGE);
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
                        NewServiceProvider.setTitle("Новый поставщика услуг");

                        JLabel TextServiceProviderName = new JLabel("ВВедите название поставщика услуг:");
                        JTextField EnterServiceProviderName = new JTextField();

                        JLabel TextBankName = new JLabel("ВВедите название банка:");
                        JTextField EnterBankName = new JTextField();

                        JLabel TextRCBICBank = new JLabel("ВВедите БИК банка:");
                        JTextField EnterRCBICBank = new JTextField();

                        JLabel TextCurrentAccaunt = new JLabel("Введите расчетный счет:");
                        JTextField EnterCurrentAccaunt = new JTextField();

                        JButton buttonAddServiceProvider = new JButton("Добавить");

                        NewServiceProvider.setLayout(null);

                        int xServiceProviderWindow = 10;
                        int widthLabelServiceProviderWindow = 400; // длина элемента текста
                        int widthButtonEnterComboboxServiceProviderWindow = 200;
                        int horizontalDistanceServiceProviderWindow = 10; // расстояние между двумя элементами по диагонали
                        int x2ServiceProviderWindow = xServiceProviderWindow + widthLabelServiceProviderWindow + horizontalDistanceServiceProviderWindow; // координата парного элемента
                        int heightServiceProviderWindow = 25; //высота элемента
                        int distanceServiceProviderWindow = 10; // расстояние между элементами по вертикали

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
                                            JOptionPane.showMessageDialog(null, "Некорректный ввод! БИК банка и расчётный счёт могут состоять только из цифр!", "Некорректный ввод", JOptionPane.PLAIN_MESSAGE);
                                        }
                                    }else{
                                        JOptionPane.showMessageDialog(null, "Некорректный ввод! Поля ввода БИК банка и расчётного счёта не могут быть пустыми!", "Некорректный ввод", JOptionPane.PLAIN_MESSAGE);
                                    }

                                }else{
                                    JOptionPane.showMessageDialog(null, "Некорректный ввод! Название поставщика услуг и наименование банка не могут состоять из цифр или быть пустым!", "Некорректный ввод", JOptionPane.PLAIN_MESSAGE);
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
