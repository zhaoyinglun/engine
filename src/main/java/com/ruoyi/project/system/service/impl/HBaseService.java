package com.ruoyi.project.system.service.impl;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 4/26/20.
 */
public class HBaseService {
    public static boolean createTable(Connection conn, String tableName, String[] cfs) {
        return HBaseService.createTable(conn, tableName, cfs, null, 1);
    }

    public static boolean createTable(Connection conn, String tableName, String[] cfs, byte[][] splitKes, int maxVers) {
        try {
            HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
            if (admin.tableExists(tableName)) {
                return false;
            }
            HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
            for (String cf : cfs) {
                HColumnDescriptor colDesc = new HColumnDescriptor(cf);
                colDesc.setMaxVersions(maxVers);
                tableDesc.addFamily(colDesc);
            }
            if (splitKes == null)
                admin.createTable(tableDesc);
            else
                admin.createTable(tableDesc, splitKes);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean deleteTable(Connection conn, String tableName) {
        try {
            HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean existTable(Connection conn, String tableName) {
        try {
            HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
            return admin.tableExists(tableName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteColumeFamily(Connection conn, String tableName, String cfName) {
        try {
            HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
            admin.deleteColumn(tableName, cfName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean deleteQualifier(Connection conn, String tableName, String rowName, String cfName, String qualifierName) {
        try {
            Table table = conn.getTable(TableName.valueOf(tableName));
            Delete delete = new Delete(rowName.getBytes());
            delete.addColumns(cfName.getBytes(), qualifierName.getBytes());
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean delete(Connection conn, String tableName, String rowName) {
        return delete(conn, tableName, rowName.getBytes());
    }

    public static boolean delete(Connection conn, String tableName, byte[] row) {
        try {
            Table table = conn.getTable(TableName.valueOf(tableName));
            Delete delete = new Delete(row);
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ResultScanner scan(Connection conn, String tableName, String startRowKey, String stopRowKey) {
        return scan(conn, tableName, Bytes.toBytes(startRowKey), Bytes.toBytes(stopRowKey), 3000, null);
    }

    public static ResultScanner scan(Connection conn, String tableName, byte[] startRowKey) {
        return scan(conn, tableName, startRowKey, null, 3000, null);
    }

    public static ResultScanner scan(Connection conn, String tableName, byte[] startRowKey, byte[] stopRowKey) {
        return scan(conn, tableName, startRowKey, stopRowKey, 3000, null);
    }

    public static ResultScanner scan(Connection conn, String tableName, byte[] startRowKey, byte[] stopRowKey, int caching, FilterList filterList) {
        ResultScanner rs = null;
        try {
            Table table = conn.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setStartRow(startRowKey);
            if (stopRowKey != null) {
                scan.setStopRow(stopRowKey);
            }
            scan.setCaching(caching);
            if (filterList != null) {
                scan.setFilter(filterList);
            }
            rs = table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static boolean existRow(Connection conn, String tableName, String row) {
        try {
            Table table = conn.getTable(TableName.valueOf(tableName));
            Get g = new Get(Bytes.toBytes(row));
            return table.exists(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Result getRow(Connection conn, String tableName, String row) {
        return getRow(conn, tableName, row, null);
    }

    public static Result getRow(Connection conn, String tableName, byte[] brow, FilterList filterList) {
        Result result = null;
        try {
            Table table = conn.getTable(TableName.valueOf(tableName));
            Get g = new Get(brow);
            if (filterList != null) {
                g.setFilter(filterList);
            }
            result = table.get(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Result getRow(Connection conn, String tableName, String row, FilterList filterList) {
        return getRow(conn, tableName, Bytes.toBytes(row), filterList);
    }

    public static boolean putRow(Connection conn, String tableName, String row, String cf, String qualifier, String data) {
        try {
            Put put = new Put(Bytes.toBytes(row));
            put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(qualifier), Bytes.toBytes(data));
            putRows(conn, tableName, Arrays.asList(put));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean putRow(Connection conn, String tableName, Put put) {
        try {
            putRows(conn, tableName, Arrays.asList(put));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean putRows(Connection conn, String tableName, List<Put> puts) {
//        long currentTie = System.currentTimeMillis();
        final BufferedMutator.ExceptionListener listener = new BufferedMutator.ExceptionListener() {
            public void onException(RetriesExhaustedWithDetailsException e, BufferedMutator bufferedMutator) throws RetriesExhaustedWithDetailsException {
                e.printStackTrace();
                // TODO
                throw new RuntimeException("BufferedMutator.ExceptionListener.onException");
            }
        };
        BufferedMutatorParams params = new BufferedMutatorParams(TableName.valueOf(tableName)).listener(listener);
        params.writeBufferSize(5 * 1024 * 1024);
        try {
            BufferedMutator mutator = conn.getBufferedMutator(params);
            mutator.mutate(puts);
            mutator.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
