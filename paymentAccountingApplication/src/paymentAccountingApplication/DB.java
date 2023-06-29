package paymentAccountingApplication;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
public class DB {
    private Statement statement = null;
    private Connection connection = null;
    public DB() {
        String userName = "postgres";
        String password = "Admin2022";
        String connectionUri = "jdbc:postgresql://localhost:5432/AccountingPayments";

        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(connectionUri, userName, password);
            this.statement = connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    public Connection getConnection(){
        return connection;
    }
    public Statement getStatement(){
        return statement;
    }
    public String[] getServiceProvider(Statement statement) {
        String[] res = null;
        Integer count = 0;
        try {
            ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM service_providers;");
            while(result.next()){
                count = result.getInt("count");

                if(count>0) {
                    res = new String[count];
                }else{
                    res = new String[1];
                    res[0] = "Поставщики услуг отсутствуют";
                }
            }
            result = statement.executeQuery("SELECT name_service_provider FROM service_providers;");
            for(int i=0; i<count; i++){
                result.next();
                res[i] = result.getString("name_service_provider");
            }
            /*statement.close();
            connection.close();*/
        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return res;
    }


    public void setValue(Statement statement,  String table, String newValue, String column, String condition){
        try{
            statement.executeUpdate("UPDATE "+table+" SET "+column+"="+newValue+" WHERE "+condition+";");
        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public Integer getCount(Statement statement, String table, String condition){
        Integer count = 0;
        try {
            ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM " + table + " WHERE " + condition + ";");
            result.next();
            count = result.getInt("count");
        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return count;
    }
    public Integer[] getSomeValueOfTheColumnFromTable(Statement statement, String column, String table, String condition){
        Integer[] values=null;
        Integer count = 0;
        try {
            ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM "+table+" WHERE "+condition+";");
            result.next();
            count = result.getInt("count");

            // следующая проверка нужна на случай, когда count==0.
            if(count>0) {
                values = new Integer[count];
            }else{
                values = new Integer[1];
                values[0] = 0;
                return values;
            }

            result = statement.executeQuery("SELECT "+column+" FROM "+table+" WHERE "+condition+";");
            for(int i = 0; i<count; i++){
                result.next();

                values[i] = result.getInt(column);

            }

        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return values;
    }


    public Integer getValueOfTheColumnFromTable( Statement statement, String column, String table, String condition){
        //если функция вернет -1, то это значит, что данные не найдены
        Integer res = -1;
        try {

            ResultSet resultSQL = statement.executeQuery("SELECT DISTINCT "+column+" FROM "+table+" WHERE "+condition);

            while(resultSQL.next()){
                res = resultSQL.getInt(column);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return res;
    }

    public void insertTable(Statement statement, String SQLCommand){
        try{
            statement.executeUpdate(SQLCommand);
        }catch(Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
