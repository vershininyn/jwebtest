package com.fincassa.jtest.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vyn on 14.11.2016.
 */
public class DBManager {

    public static Map<String,String> getFilesByClientId(String pClientid) {
        HashMap<String,String> fileslist= new HashMap<String,String>(100);

        Connection connection= null;

        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");

            PreparedStatement statement= connection.prepareStatement("SELECT * FROM get_fileslist(?);");

            statement.setString(1, pClientid);
            statement.execute();

            ResultSet rs= statement.getResultSet();
            while(rs.next()) {
                fileslist.put(rs.getString(1),rs.getString(2));
            }

            rs.close();
            statement.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection != null) {connection.close();}
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return fileslist;
    }

    public static List<String> getFileinfoById(int pFileid) {
        ArrayList<String> fileinfo= new ArrayList<String>(2);

        Connection connection= null;

        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");

            CallableStatement getFilepathProc= connection.prepareCall("{ ? = call get_filepath(?) }");
            getFilepathProc.registerOutParameter(1, Types.VARCHAR);
            getFilepathProc.setInt(2,pFileid);

            getFilepathProc.execute();
            fileinfo.add(getFilepathProc.getString(1));
            getFilepathProc.close();

            CallableStatement getFilenameProc= connection.prepareCall("{ ? = call get_filename(?) }");
            getFilenameProc.registerOutParameter(1, Types.VARCHAR);
            getFilenameProc.setInt(2,pFileid);

            getFilenameProc.execute();
            fileinfo.add(getFilenameProc.getString(1));
            getFilenameProc.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection != null) {connection.close();}
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return fileinfo;
    }

    public static int registerFile(String pClientid, String pFilepath, String pFilename) {
        int fileid= -1;

        Connection connection= null;

        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");

            CallableStatement registerProc= connection.prepareCall("{ ? = call register_file(?,?,?) }");
            registerProc.registerOutParameter(1, Types.INTEGER);
            registerProc.setString(2, pClientid);
            registerProc.setString(3, pFilepath);
            registerProc.setString(4, pFilename);

            registerProc.execute();
            fileid= registerProc.getInt(1);
            registerProc.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection != null) {connection.close();}
            } catch (SQLException ex) {
              ex.printStackTrace();
            }
        }

        return fileid;
    }

}
