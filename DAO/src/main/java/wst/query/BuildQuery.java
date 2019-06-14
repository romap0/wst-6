package wst.query;

import wst.dao.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BuildQuery {
    private String tableName;
    private List<String> columnNames;
    private List<Condition> conditions;

    public BuildQuery() {
        columnNames = new ArrayList<>();
        conditions = new ArrayList<>();
    }

    public BuildQuery tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    private BuildQuery selectColumn(String columnName) {
        this.columnNames.add(columnName);
        return this;
    }

    public BuildQuery selectColumns(String... columns) {
        for (String column : columns) { ;
            selectColumn(column);
        }
        return this;
    }

    public BuildQuery condition(Condition condition) {
         this.conditions.add(condition);
        return this;
    }

    public Query buildPreparedStatementQuery() {
        StringBuilder query = new StringBuilder("SELECT ");
        String selectColumns = String.join(", ", columnNames);
        query.append(selectColumns).append(" FROM ").append(tableName);

        List<Condition> actualConditions = new ArrayList<>();

        for (Condition cond : conditions) {
            if (cond.build() != null) {
                actualConditions.add(cond);
            }
        }

        if (!actualConditions.isEmpty()) {
            query.append(" WHERE ");
            query.append(actualConditions.stream().map(Condition::build).collect(Collectors.joining(" AND ")));
        }

        Logger.getGlobal().log(Level.ALL, query.toString());
        return new Query(query.toString(), actualConditions);
    }

    @Override
    public String toString() {
        return "BuildQuery{" +
                "tableName='" + tableName + '\'' +
                ", columnNames=" + columnNames +
                ", conditions=" + conditions +
                '}';
    }
}