
package database;

import database.db;
import java.sql.*;

/**
 *
 * @author Lenovo
 */


public class MySqlConnector implements db {
 
    public Connection openConnection(){
    try{
        String username= "root";
        String password="1234";
        String database= "apex";
        Connection connection;
        connection =DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + database,username,password
        
        ); 
        if (connection==null){
            System.out.println("Connection null");
        }else{
            System.out.println("Connection success");
        }
        return connection;
    }catch(Exception e){
        System.out.println(e);
    }
    return null;
}

  @Override

    public void closeConnection(Connection conn) {

        try{

            if(conn != null && !conn.isClosed() ){

                conn.close();

                System.out.println("Connection close");

            }

            

        }catch(Exception e){

            System.out.println(e);

            

        }

    }



    @Override

    public ResultSet runQuery(Connection conn, String query) {

       try{

           Statement stmp = conn.createStatement();

           ResultSet result = stmp.executeQuery(query);

           return result;

       

       }catch (Exception e){

           System.out.println(e);

           return null;

       }

    }


    @Override
    public int executeUpdate(Connection conn, String query) {
       
     try{

          Statement stmp = conn.createStatement();

          int result = stmp.executeUpdate(query);

          return result;

          

      }catch(Exception e){

          System.out.println(e);

          return -1;

      }

    }

}