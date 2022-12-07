package com.api.shoesshop.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.utils.Helper;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StatisticsController {

    class RevenueMonthYear {
        public int month, year, revenue;
    }

    class RevenueDay {
        public int day, revenue;
    }

    class RevenueHour {
        public int hour, revenue;
    }

    class Count {
        public int product, order, account;
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/api/statistics/revenue")
    public ResponseEntity<String> getRevenues(HttpServletRequest req, @RequestParam Map<String, String> query) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                String sql = "";
                String year = query.get("year");
                String month = query.get("month");
                String day = query.get("day");
                if (day != null && month != null && year != null) {
                    sql = "select TO_CHAR( created_at, 'HH24' ) as hour,"
                            + " sum(get_total_price_function(order_id)) as revenue"
                            + " from orders where order_status=N'Vận chuyển thành công' and to_char(created_at, 'YYYY')="
                            + year
                            + " and to_char(created_at, 'MM')=" + month
                            + " and to_char(created_at, 'DD')=" + day
                            + " group by TO_CHAR( created_at, 'HH24' )"
                            + " order by TO_CHAR( created_at, 'HH24' )";
                    List<RevenueHour> list = jdbcTemplate.query(sql,
                            new RowMapper<RevenueHour>() {
                                public RevenueHour mapRow(ResultSet rs, int rowNum) throws SQLException {
                                    RevenueHour r = new RevenueHour();
                                    r.hour = rs.getInt("hour");
                                    r.revenue = rs.getInt("revenue");
                                    return r;
                                }
                            });
                    return Helper.responseSuccess(list);
                } else if (month != null && year != null) {
                    sql = "select to_char(created_at, 'DD') as day,"
                            + " sum(get_total_price_function(order_id)) as revenue"
                            + " from orders where order_status=N'Vận chuyển thành công' and to_char(created_at, 'YYYY')="
                            + year
                            + " and to_char(created_at, 'MM')=" + month
                            + " group by to_char(created_at, 'DD')"
                            + " order by to_char(created_at, 'DD')";
                    List<RevenueDay> list = jdbcTemplate.query(sql,
                            new RowMapper<RevenueDay>() {
                                public RevenueDay mapRow(ResultSet rs, int rowNum) throws SQLException {
                                    RevenueDay r = new RevenueDay();
                                    r.day = rs.getInt("day");
                                    r.revenue = rs.getInt("revenue");
                                    return r;
                                }
                            });
                    return Helper.responseSuccess(list);
                } else if (month == null && year != null) {
                    sql = "select to_char(created_at, 'YYYY') as year,"
                            + " to_char(created_at, 'MM') as month,"
                            + " sum(get_total_price_function(order_id)) as revenue"
                            + " from orders where order_status=N'Vận chuyển thành công' and to_char(created_at, 'YYYY')="
                            + year
                            + " group by to_char(created_at, 'YYYY'),to_char(created_at, 'MM')"
                            + " order by to_char(created_at, 'YYYY'),to_char(created_at, 'MM')";

                    List<RevenueMonthYear> list = jdbcTemplate.query(sql,
                            new RowMapper<RevenueMonthYear>() {
                                public RevenueMonthYear mapRow(ResultSet rs, int rowNum) throws SQLException {
                                    RevenueMonthYear r = new RevenueMonthYear();
                                    r.month = rs.getInt("month");
                                    r.year = rs.getInt("year");
                                    r.revenue = rs.getInt("revenue");
                                    return r;
                                }
                            });
                    return Helper.responseSuccess(list);
                } else {
                    LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    sql = "select TO_CHAR( created_at, 'HH24' ) as hour,"
                            + " sum(get_total_price_function(order_id)) as revenue"
                            + " from orders where order_status=N'Vận chuyển thành công' and to_char(created_at, 'YYYY')="
                            + localDate.getYear()
                            + " and to_char(created_at, 'MM')=" + localDate.getMonthValue()
                            + " and to_char(created_at, 'DD')=" + localDate.getDayOfMonth()
                            + " group by TO_CHAR( created_at, 'HH24' )"
                            + " order by TO_CHAR( created_at, 'HH24' )";
                    List<RevenueHour> list = jdbcTemplate.query(sql,
                            new RowMapper<RevenueHour>() {
                                public RevenueHour mapRow(ResultSet rs, int rowNum) throws SQLException {
                                    RevenueHour r = new RevenueHour();
                                    r.hour = rs.getInt("hour");
                                    r.revenue = rs.getInt("revenue");
                                    return r;
                                }
                            });
                    return Helper.responseSuccess(list);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return Helper.responseError();
        }
        return Helper.responseUnauthorized();
    }

    @GetMapping("/api/statistics/count")
    public ResponseEntity<String> getCount(HttpServletRequest req, @RequestParam Map<String, String> query) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String sql = "select "
                        + " (select count(product_id) from products where to_char(created_at, 'YYYY')="
                        + localDate.getYear() + " and to_char(created_at, 'MM')=" + localDate.getMonthValue()
                        + ") as cproduct,"
                        + " (select count(account_id) from accounts where to_char(created_at, 'YYYY')="
                        + localDate.getYear() + " and to_char(created_at, 'MM')=" + localDate.getMonthValue()
                        + ") as caccount,"
                        + " (select count(order_id) from orders where to_char(created_at, 'YYYY')="
                        + localDate.getYear() + " and to_char(created_at, 'MM')=" + localDate.getMonthValue()
                        + ") as corder"
                        + " from dual";
                List<Count> list1 = jdbcTemplate.query(sql,
                        new RowMapper<Count>() {
                            public Count mapRow(ResultSet rs, int rowNum) throws SQLException {
                                Count r = new Count();
                                r.product = rs.getInt("cproduct");
                                r.account = rs.getInt("caccount");
                                r.order = rs.getInt("corder");
                                return r;
                            }
                        });
                sql = "select "
                        + " (select count(product_id) from products where to_char(created_at, 'YYYY')="
                        + (localDate.getMonthValue() == 1 ? localDate.getYear() - 1 : localDate.getYear())
                        + " and to_char(created_at, 'MM')="
                        + (localDate.getMonthValue() == 1 ? 12 : (localDate.getMonthValue() - 1))
                        + ") as cproduct,"
                        + " (select count(account_id) from accounts where to_char(created_at, 'YYYY')="
                        + (localDate.getMonthValue() == 1 ? localDate.getYear() - 1 : localDate.getYear())
                        + " and to_char(created_at, 'MM')="
                        + (localDate.getMonthValue() == 1 ? 12 : (localDate.getMonthValue() - 1))
                        + ") as caccount,"
                        + " (select count(order_id) from orders where to_char(created_at, 'YYYY')="
                        + (localDate.getMonthValue() == 1 ? localDate.getYear() - 1 : localDate.getYear())
                        + " and to_char(created_at, 'MM')="
                        + (localDate.getMonthValue() == 1 ? 12 : (localDate.getMonthValue() - 1))
                        + ") as corder"
                        + " from dual";
                List<Count> list2 = jdbcTemplate.query(sql,
                        new RowMapper<Count>() {
                            public Count mapRow(ResultSet rs, int rowNum) throws SQLException {
                                Count r = new Count();
                                r.product = rs.getInt("cproduct");
                                r.account = rs.getInt("caccount");
                                r.order = rs.getInt("corder");
                                return r;
                            }
                        });
                list2.add(list1.get(0));
                return Helper.responseSuccess(list2);
            } catch (Exception e) {
                System.out.println(e);
            }
            return Helper.responseError();
        }
        return Helper.responseUnauthorized();
    }
}
