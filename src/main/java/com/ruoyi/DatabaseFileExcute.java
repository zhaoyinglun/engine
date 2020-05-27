package com.ruoyi;

import com.ruoyi.common.utils.DBConfigUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.config.ConfigValues;
import com.ruoyi.framework.datasource.DynamicDataSource;
import org.apache.commons.httpclient.URIException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by zhaoyl on 5/14/20.
 */
public class DatabaseFileExcute {

    private static final Logger log = LoggerFactory.getLogger(DatabaseFileExcute.class);


    public static String path = "src/main/resources/sql/";

    private static boolean isJar()  {
        File sqlModul = new File(DatabaseFileExcute.class.getResource("/sql/").getPath());
        log.debug("---------------------------" + String.valueOf(sqlModul.listFiles() == null));
        if ((sqlModul.listFiles()) == null){
            return true;
        }else {
            return false;
        }
    }

    public static void checkExcute(){
        DataSource dataSource = SpringUtils.getBean("masterDataSource");
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select table_name from information_schema.tables where table_schema='hkjs'");
            List<String> tables = new ArrayList<>();
            while (resultSet.next()){
                tables.add(resultSet.getString("table_name"));
            }
            if (tables.isEmpty()){
                if (isJar()){
                    excuteAllJar();
                }else {
                    excuteAll();
                }
            }else {
//                if (!tables.containsAll(DBConfigUtils.getConfigValue(ConfigValues.RuoyiTablles))){
//                    excuteFile("/sql/0001_ry_20200415.sql");
//                }
//                if (!tables.containsAll(DBConfigUtils.getConfigValue(ConfigValues.QuartzTables))){
//                    excuteFile("/sql/0002_quartz.sql");
//                }
//                if (!tables.containsAll(DBConfigUtils.getConfigValue(ConfigValues.FlightTables))){
//                    excuteFile("/sql/0003_flight.sql");
//                }
//                if (!tables.containsAll(DBConfigUtils.getConfigValue(ConfigValues.OptionTable))){
//                    excuteFile("/sql/0009_create_option.sql");
//                }
            }
        } catch (SQLException e) {
            System.out.println("Connection Failed -zhaoyl");
            e.printStackTrace();
        }

    }

    public static void excuteFile(String file ){
        DataSource dataSource = SpringUtils.getBean("masterDataSource");
        Reader reader;
        try {
            if (isJar()){
                InputStream inputStream = DatabaseFileExcute.class.getResourceAsStream(file);
                reader = new InputStreamReader(inputStream);
            }else {
                reader = new FileReader(path+file.substring(file.lastIndexOf("/") + 1));
            }
            BufferedReader bufferedReader = new BufferedReader(reader);
            String sql = "";
            String str =null;
            while ((str = bufferedReader.readLine())!= null){
                str = str.trim();
                if (!str.endsWith(";")){
                    sql = sql + str + "\n";
                    continue;
                }else if (str.endsWith(";")){
                    try {
                        sql = sql + str;
                        Connection conn = dataSource.getConnection();
                        Statement statement = conn.createStatement();
                        log.info(sql);
                        statement.execute(sql);
                        statement.close();
                        conn.close();
                        sql="";
                    } catch (SQLException e) {
                        System.out.println(file+"文件执行失败，失败原因如下：");
                        System.out.println("SQLException:  "+e.getMessage());
                        System.out.println("------------------------------------------------------------------------------------");
                        log.error("SQLException:  "+e.getMessage());
                        sql="";
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(file+"文件执行失败，失败原因如下：");
            System.out.println("IOException:  "+e.getMessage());
            System.out.println("------------------------------------------------------------------------------------");
        }

    }


    public static void excuteAllJar() {
        List<String> filelist = new ArrayList<>();
        try {
            URL jarUrl = DatabaseFileExcute.class.getResource("/sql");
            JarURLConnection jarURLConnection = (JarURLConnection) jarUrl.openConnection();
            JarFile jarFile = jarURLConnection.getJarFile();
            Enumeration<JarEntry> jarEntrys = jarFile.entries();
            while (jarEntrys.hasMoreElements()){
                JarEntry entry = jarEntrys.nextElement();
                String name = entry.getName();
                if (name.startsWith("sql") && !entry.isDirectory()){
                    log.info(name);
                    filelist.add("/"+name);
                }
            }
        }catch (IOException e){

        }
        DataSource dataSource = SpringUtils.getBean("masterDataSource");
        for (int i = 0 ; i < filelist.size() ; i++){
            if (filelist.get(i).endsWith("sql")){
                try {
                    InputStream inputStream = DatabaseFileExcute.class.getResourceAsStream(filelist.get(i));
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    log.info("excute " + filelist.get(i) + "  file");
                    String sql = "";
                    String str =null;
                    while ((str = bufferedReader.readLine())!= null){
                        str = str.trim();
                        if (!str.endsWith(";")){
                            sql = sql + str + "\n";
                            continue;
                        }else if (str.endsWith(";")){
                            try {
                                sql = sql + str;
                                Connection conn = dataSource.getConnection();
                                Statement statement = conn.createStatement();
                                log.info(sql);
                                statement.execute(sql);
                                statement.close();
                                conn.close();
                                sql="";
                            } catch (SQLException e) {
                                System.out.println(filelist.get(i)+"文件执行失败，失败原因如下：");
                                System.out.println("SQLException:  "+e.getMessage());
                                System.out.println("------------------------------------------------------------------------------------");
                                log.error("SQLException:  "+e.getMessage());
                                sql="";
                            }
                        }
                    }
                }catch (FileNotFoundException e){
                    System.out.println(filelist.get(i)+"文件执行失败，失败原因如下：");
                    System.out.println("FileNotFoundException:  "+e.getMessage());
                    System.out.println("------------------------------------------------------------------------------------");
                } catch (IOException e) {
                    System.out.println(filelist.get(i)+"文件执行失败，失败原因如下：");
                    System.out.println("IOException:  "+e.getMessage());
                    System.out.println("------------------------------------------------------------------------------------");
                }
            }
        }
    }

    public static void excuteAll() {
        log.debug("--------------------------------------------");
        log.debug("-----------------------------------excute idea");
        String[] filelist = new File(path).list();
        for (int i = 0 ; i < filelist.length ; i++){
            DataSource dataSource = SpringUtils.getBean("masterDataSource");
            try {
                Reader reader = new FileReader(path+filelist[i]);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String sql = "";
                String str =null;
                while ((str = bufferedReader.readLine())!= null){
                    str = str.trim();
                    if (!str.endsWith(";")){
                        sql = sql + str + "\n";
                        continue;
                    }else if (str.endsWith(";")){
                        try {
                            sql = sql + str;
                            Connection conn = dataSource.getConnection();
                            Statement statement = conn.createStatement();
                            log.info(sql);
                            statement.execute(sql);
                            statement.close();
                            conn.close();
                            sql="";
                        } catch (SQLException e) {
                            System.out.println(filelist[i]+"文件执行失败，失败原因如下：");
                            System.out.println("SQLException:  "+e.getMessage());
                            System.out.println("------------------------------------------------------------------------------------");
                            log.error("SQLException:  "+e.getMessage());
                            sql="";
                        }
                    }
                }
            }catch (FileNotFoundException e){
                System.out.println(filelist[i]+"文件执行失败，失败原因如下：");
                System.out.println("FileNotFoundException:  "+e.getMessage());
                System.out.println("------------------------------------------------------------------------------------");
            } catch (IOException e) {
                System.out.println(filelist[i]+"文件执行失败，失败原因如下：");
                System.out.println("IOException:  "+e.getMessage());
                System.out.println("------------------------------------------------------------------------------------");
            }
        }
    }
}
