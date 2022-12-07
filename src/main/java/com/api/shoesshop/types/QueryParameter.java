package com.api.shoesshop.types;

public class QueryParameter {
    private String sort_by, sort_type, p, limit;

    public String getSortBy() {
        return this.sort_by;
    }

    public String getSortType() {
        return this.sort_type;
    }

    public String getP() {
        return this.p;
    }

    public String getLimit() {
        return this.limit;
    }

    public QueryParameter(String sort_by, String sort_type, String p, String limit) {
        this.sort_by = sort_by;
        this.sort_type = sort_type;
        this.p = p;
        this.limit = limit;
    }

    public String getOrderBySQL(String default_sort_by) {
        return " order by " + (sort_by == null ? default_sort_by : sort_by) + " "
                + (sort_type == null ? "desc" : sort_type);
    }

    public String getPaginationSQL() {
        int row_count = limit == null ? -1 : Integer.parseInt(limit);
        int offset = p == null ? 0 : (Integer.parseInt(p) - 1) * row_count;
        if (row_count < 0)
            return "";
        return " offset " + offset + " ROWS" + " FETCH NEXT " + row_count
                + " ROWS ONLY";
    }

}
