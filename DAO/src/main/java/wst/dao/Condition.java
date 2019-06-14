package wst.dao;

public class Condition {
    private final String columnName;
    private final Object value;
    private final Class<?> type;

    public Condition(String columnName, Object value, Class<?> type) {
        this.columnName = columnName;
        this.value = value;
        this.type = type;
    }

    public String build() {
        if (getValue() != null) {
            return getColumnName() + " = ?";
        }
        
        return null;
    }

    public Object getValue() {
        return value;
    }
    public String getColumnName() {
        return columnName;
    }
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "columnName='" + columnName + '\'' +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}
