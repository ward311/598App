package com.example.homerenting_prototype_one.model;

import android.provider.BaseColumns;

public class TableContract {
    private TableContract() {}

    public static class CompanyTable implements BaseColumns {
        public static final String TABLE_NAME = "company";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_COMPANY_NAME = "company_name";
        public static final String COLUMN_NAME_IMG = "img";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_STAFF_NUM = "staff_num";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_LINE_ID = "line_id";
        public static final String COLUMN_NAME_PHILOSOPHY = "pyilosophy";
        public static final String COLUMN_NAME_LAST_DISTRIBUTION = "last_distribution";

        public static final String SQL_CREATE_COMPANY =
                "CREATE TABLE "+TABLE_NAME+" ( "+
                        COLUMN_NAME_COMPANY_ID+" int(10) NOT NULL, "+
                        COLUMN_NAME_COMPANY_NAME+" varchar(100), "+
                        COLUMN_NAME_IMG+" varchar(99), "+
                        COLUMN_NAME_ADDRESS+" varchar(50), "+
                        COLUMN_NAME_PHONE+" varchar(99), "+
                        COLUMN_NAME_STAFF_NUM+" int(4) NOT NULL, "+
                        COLUMN_NAME_URL+" varchar(100), "+
                        COLUMN_NAME_EMAIL+" varchar(100), "+
                        COLUMN_NAME_LINE_ID+" varchar(100), "+
                        COLUMN_NAME_PHILOSOPHY+" varchar(500), "+
                        COLUMN_NAME_LAST_DISTRIBUTION+" float DEFAULT 0, "+
                        "PRIMARY KEY(`company_id`)"+
                ");";

        public static final String SQL_DELETE_COMPANY =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }

    public static class ChooseTable implements BaseColumns {
        public static final String TABLE_NAME = "choose";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_VALUATION_DATE = "valuation_date";
        public static final String COLUMN_NAME_VALUATION_TIME = "valuation_time";
        public static final String COLUMN_NAME_MOVING_DATE = "moving_date";
        public static final String COLUMN_NAME_ESTIMATE_FEE = "estimate_fee";
        public static final String COLUMN_NAME_ACCURATTE_FEE = "accurate_fee";
        public static final String COLUMN_NAME_CONFIRM = "confirm";
        public static final String COLUMN_NAME_VALUATION_STATUS = "valuaiton_status";

        public static final String SQL_CREATE_CHOOSE = "" +
                "CREATE TABLE "+TABLE_NAME+" ( "+
                COLUMN_NAME_ORDER_ID+" int(10) NOT NULL, "+
                COLUMN_NAME_COMPANY_ID+" int(10) NOT NULL, "+
                COLUMN_NAME_VALUATION_DATE+" date, "+
                COLUMN_NAME_VALUATION_TIME+" varchar(99), "+
                COLUMN_NAME_MOVING_DATE+" datetime, "+
                COLUMN_NAME_ESTIMATE_FEE+" int(10), "+
                COLUMN_NAME_ACCURATTE_FEE+" int(10), "+
                COLUMN_NAME_CONFIRM+" boolean DEFAULT FALSE, "+
                COLUMN_NAME_VALUATION_STATUS+" enum('self', 'booking', 'match', 'cancel', 'chosen') DEFAULT 'self', "+
                "PRIMARY KEY(order_id, company_id), "+
                "FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE, "+
                "FOREIGN KEY (company_id) REFERENCES company(company_id) ON DELETE CASCADE "+
                ");";

        public static final String SQL_DELETE_CHOOSE =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }
}
