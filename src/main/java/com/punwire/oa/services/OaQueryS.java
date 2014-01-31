package com.punwire.oa.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaObject;
import oracle.jdbc.OracleDriver;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.faces.bean.ApplicationScoped;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by kanwal on 1/16/14.
 */
@Stateless
@Singleton
public class OaQueryS {

    //SimpleDriverDataSource ds = new SimpleDriverDataSource(new OracleDriver(), "jdbc:oracle:thin:@//r12dbdev1.misqa.edc.vrsn.com:7001/dev1", "apps", "apps");

    SimpleDriverDataSource ds = new SimpleDriverDataSource(new OracleDriver(), "jdbc:oracle:thin:@//ebsdb.apps.com:1521/vis", "apps", "apps");

    SimpleDateFormat df = new SimpleDateFormat();

    public OaQueryS() {
        df.applyPattern("MM/dd/yyyy");
    }

    public ArrayNode runQuery(ObjectNode qry, final ArrayNode columns, ObjectNode params) {
        return runQuery(qry, columns, params, 0, 15);
    }

    public ArrayNode runQuery(ObjectNode qry, final ArrayNode columns, ObjectNode params, int startPage, int pageSize) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        final ObjectMapper mapper = new ObjectMapper();
        String sql = getString(qry.get("sql"));

        //Build Dynamic part
        StringBuilder sb = new StringBuilder();
        final List<Object> paramList = new ArrayList<Object>();
        Iterator<String> fields = params.fieldNames();
        while (fields.hasNext()) {
            String key = fields.next();
            String value = params.get(key).asText();
            if (value.trim().length() > 0) {
                sb.append(" and " + key + " like ?");
                paramList.add(value);
            }
        }
        System.out.println("replace dynamic where");
        sql = sql.replace("&where&", sb.toString());
        System.out.println(sql);
        System.out.println("replace dynamic where");

        //Get First Column
        ObjectNode fCol = (ObjectNode) columns.get(0);

        sql = sql.trim() + " order by " + fCol.get("name").asText();
        int startRow = startPage * pageSize;
        int endRow = startRow + pageSize;
        String nSql = "select * from ( " + sql + ") where row_num between " + startRow + " and " + endRow;

        System.out.println(nSql);

        List<JsonNode> list = jdbcTemplate.query(nSql, paramList.toArray(), new RowMapper<JsonNode>() {
            @Override
            public JsonNode mapRow(ResultSet resultSet, int i) throws SQLException {
                ObjectNode row = mapper.createObjectNode();
                int cCount = columns.size();
                for (int c = 0; c < cCount; c++) {
                    ObjectNode col = (ObjectNode) columns.get(c);
                    if (col.get("type").asText().equals("String")) {
                        row.put(col.get("name").asText(), resultSet.getString(col.get("name").asText()));
                    } else if (col.get("type").asText().equals("Number")) {
                        row.put(col.get("name").asText(), resultSet.getBigDecimal(col.get("name").asText()));
                    } else if (col.get("type").asText().equals("Integer")) {
                        row.put(col.get("name").asText(), resultSet.getInt(col.get("name").asText()));
                    } else if (col.get("type").asText().equals("Date")) {
                        Date d = resultSet.getDate(col.get("name").asText());
                        row.put(col.get("name").asText(), df.format(d));
                    } else {
                        System.out.println(">>>>>>>>>>>>>  " + col.get("type") + "<<<<<<<<<<<<");
                    }
                }
                return row;
            }
        });
        ArrayNode aNode = mapper.createArrayNode();
        aNode.addAll(list);
        return aNode;
    }

    public ArrayNode runQuery(String sql, final ArrayNode columns, ObjectNode params, int startPage, int pageSize) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        final ObjectMapper mapper = new ObjectMapper();

        //Build Dynamic part
        StringBuilder sb = new StringBuilder();
        final List<Object> paramList = new ArrayList<Object>();
        Iterator<String> fields = params.fieldNames();
        while (fields.hasNext()) {
            String key = fields.next();
            String value = params.get(key).asText();
            if (value.trim().length() > 0) {
                sb.append(" and " + key + " like ?");
                paramList.add(value);
            }
        }
        System.out.println("replace dynamic where");
        sql = sql.replace("&where&", sb.toString());
        System.out.println(sql);
        System.out.println("replace dynamic where");

        //Get First Column
        ObjectNode fCol = (ObjectNode) columns.get(0);

        sql = sql.trim() + " order by " + fCol.get("name").asText();
        int startRow = startPage * pageSize;
        int endRow = startRow + pageSize;
        String nSql = "select * from ( " + sql + ") where row_num between " + startRow + " and " + endRow;

        System.out.println(nSql);

        List<JsonNode> list = jdbcTemplate.query(nSql, paramList.toArray(), new RowMapper<JsonNode>() {
            @Override
            public JsonNode mapRow(ResultSet resultSet, int i) throws SQLException {
                ObjectNode row = mapper.createObjectNode();
                int cCount = columns.size();
                for (int c = 0; c < cCount; c++) {
                    ObjectNode col = (ObjectNode) columns.get(c);
                    if (col.get("type").asText().equals("String")) {
                        row.put(col.get("name").asText(), resultSet.getString(col.get("name").asText()));
                    } else if (col.get("type").asText().equals("Number")) {
                        row.put(col.get("name").asText(), resultSet.getBigDecimal(col.get("name").asText()));
                    } else if (col.get("type").asText().equals("Integer")) {
                        row.put(col.get("name").asText(), resultSet.getInt(col.get("name").asText()));
                    } else if (col.get("type").asText().equals("Date")) {
                        Date d = resultSet.getDate(col.get("name").asText());
                        row.put(col.get("name").asText(), df.format(d));
                    } else {
                        System.out.println(">>>>>>>>>>>>>  " + col.get("type") + "<<<<<<<<<<<<");
                    }
                }
                return row;
            }
        });
        ArrayNode aNode = mapper.createArrayNode();
        aNode.addAll(list);
        return aNode;
    }

    public ObjectNode runSingleQuery(ObjectNode qry, final ArrayNode columns, ObjectNode params) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        final ObjectMapper mapper = new ObjectMapper();
        String sql = getString(qry.get("sql"));

        System.out.println("Single Query");
        System.out.println(columns.toString());
        System.out.println(sql);

        //Build Dynamic part
        StringBuilder sb = new StringBuilder();
        final List<Object> paramList = new ArrayList<Object>();
        Iterator<String> fields = params.fieldNames();
        while (fields.hasNext()) {
            String key = fields.next();
            String value = params.get(key).asText();
            if (value.trim().length() > 0) {
                System.out.println("Adding parameter " + value);
                paramList.add(value);
            }
        }

        ObjectNode row = jdbcTemplate.query(sql, paramList.toArray(), new ResultSetExtractor<ObjectNode>() {
            @Override
            public ObjectNode extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                ObjectNode row = mapper.createObjectNode();
                if (resultSet.next()) {
                    int cCount = columns.size();
                    for (int c = 0; c < cCount; c++) {
                        JsonNode cc = (JsonNode) columns.get(c);
                        if (cc.isArray()) {
                            ArrayNode colArray = (ArrayNode) cc;
                            for (int c3 = 0; c3 < colArray.size(); c3++) {
                                ObjectNode c4 = (ObjectNode) colArray.get(c3);
                                getRowValue(c4, row, resultSet);
                            }
                        } else {
                            ObjectNode col = (ObjectNode) cc;
                            getRowValue(col, row, resultSet);
                        }
                    }
                }
                return row;
            }
        });
        System.out.println("Query REturn SInglr row " + row.toString());
        return row;
    }

    public ObjectNode runSingleQuery(String sql, final ArrayNode columns, ObjectNode params) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        final ObjectMapper mapper = new ObjectMapper();

        System.out.println("Single Query");
        System.out.println(columns.toString());
        System.out.println(sql);

        //Build Dynamic part
        StringBuilder sb = new StringBuilder();
        final List<Object> paramList = new ArrayList<Object>();
        Iterator<String> fields = params.fieldNames();
        while (fields.hasNext()) {
            String key = fields.next();
            String value = params.get(key).asText();
            if (value.trim().length() > 0) {
                System.out.println("Adding parameter " + value);
                paramList.add(value);
            }
        }

        ObjectNode row = jdbcTemplate.query(sql, paramList.toArray(), new ResultSetExtractor<ObjectNode>() {
            @Override
            public ObjectNode extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                ObjectNode row = mapper.createObjectNode();
                if (resultSet.next()) {
                    int cCount = columns.size();
                    for (int c = 0; c < cCount; c++) {
                        JsonNode cc = (JsonNode) columns.get(c);
                        if (cc.isArray()) {
                            ArrayNode colArray = (ArrayNode) cc;
                            for (int c3 = 0; c3 < colArray.size(); c3++) {
                                ObjectNode c4 = (ObjectNode) colArray.get(c3);
                                getRowValue(c4, row, resultSet);
                            }
                        } else {
                            ObjectNode col = (ObjectNode) cc;
                            getRowValue(col, row, resultSet);
                        }
                    }
                }
                return row;
            }
        });
        System.out.println("Query REturn SInglr row " + row.toString());
        return row;
    }

    protected void getRowValue(ObjectNode col, ObjectNode row, ResultSet resultSet) {
        try {
            if (col.get("type").asText().equals("String")) {
                row.put(col.get("name").asText(), resultSet.getString(col.get("name").asText()));
            } else if (col.get("type").asText().equals("Number")) {
                row.put(col.get("name").asText(), resultSet.getBigDecimal(col.get("name").asText()));
            } else if (col.get("type").asText().equals("Integer")) {
                row.put(col.get("name").asText(), resultSet.getInt(col.get("name").asText()));
            } else if (col.get("type").asText().equals("Date")) {
                Date d = resultSet.getDate(col.get("name").asText());
                row.put(col.get("name").asText(), df.format(d));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public String getString(JsonNode node) {
        if (node.isArray()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < node.size(); i++) sb.append(node.get(i).asText() + "\n");
            return sb.toString();
        }
        return node.asText();
    }
}
