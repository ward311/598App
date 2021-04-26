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
        public static final String COLUMN_NAME_PHILOSOPHY = "philosophy";
        public static final String COLUMN_NAME_LAST_DISTRIBUTION = "last_distribution";

        public static final String SQL_CREATE_COMPANY =
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                        COLUMN_NAME_COMPANY_ID+" int(10) NOT NULL AUTO_INCREMENT, "+
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
        public static final String COLUMN_NAME_ACCURATE_FEE = "accurate_fee";
        public static final String COLUMN_NAME_CONFIRM = "confirm";
        public static final String COLUMN_NAME_VALUATION_STATUS = "valuation_status";

        public static final String SQL_CREATE_CHOOSE = "" +
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ORDER_ID+" int(10) NOT NULL, "+
                COLUMN_NAME_COMPANY_ID+" int(10) NOT NULL, "+
                COLUMN_NAME_VALUATION_DATE+" date, "+
                COLUMN_NAME_VALUATION_TIME+" varchar(99), "+
                COLUMN_NAME_MOVING_DATE+" datetime, "+
                COLUMN_NAME_ESTIMATE_FEE+" int(10), "+
                COLUMN_NAME_ACCURATE_FEE+" int(10), "+
                COLUMN_NAME_CONFIRM+" boolean DEFAULT FALSE, "+
                COLUMN_NAME_VALUATION_STATUS+" enum('self', 'booking', 'match', 'cancel', 'chosen') DEFAULT 'self', "+
                "PRIMARY KEY(order_id, company_id), "+
                "FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE, "+
                "FOREIGN KEY (company_id) REFERENCES company(company_id) ON DELETE CASCADE "+
                ");";

        public static final String SQL_DELETE_CHOOSE =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }

    public static class CommentsTable implements BaseColumns{
        public static final String TABLE_NAME = "comments";
        public static final String COLUMN_NAME_COMMENT_ID = "comment_id";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_MEMBER_ID = "member_id";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_COMMENT_DATE = "comment_date";
        public static final String COLUMN_NAME_SERVICE_QUALITY = "service_quality";
        public static final String COLUMN_NAME_WORK_ATTITUDE = "work_attitude";
        public static final String COLUMN_NAME_COMMENT = "comment";
        public static final String COLUMN_NAME_REPLY = "reply";

        public static final String SQL_CREATE_COMMENTS = "" +
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_COMMENT_ID+" int(10) NOT NULL AUTO_INCREMENT, "+
                COLUMN_NAME_ORDER_ID+" int(10) NOT NULL, "+
                COLUMN_NAME_MEMBER_ID+" int(10), "+
                COLUMN_NAME_COMPANY_ID+" int(10), "+
                COLUMN_NAME_COMMENT_DATE+" timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(), "+
                COLUMN_NAME_SERVICE_QUALITY+" float(10) NOT NULL CHECK (service_quality between 0 and 5), "+
                COLUMN_NAME_WORK_ATTITUDE+" float(10) NOT NULL  CHECK (work_attitude between 0 and 5), "+
                COLUMN_NAME_COMMENT+" varchar(300) CHARACTER SET utf8 DEFAULT NULL, "+
                COLUMN_NAME_REPLY+" varchar(300) CHARACTER SET utf8 DEFAULT NULL, "+
                "PRIMARY KEY (comment_id), "+
                "FOREIGN KEY (order_id), "+
                "REFERENCES orders(order_id) ON DELETE CASCADE, "+
                "FOREIGN KEY (member_id) "+
                "REFERENCES member(member_id) ON DELETE CASCADE, "+
                "FOREIGN KEY (company_id), "+
                "REFERENCES company(company_id) ON DELETE CASCADE "+
                ");";

        public static final String SQL_DELETE_COMMENTS =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }
    public static class StaffTable implements BaseColumns{
        public static final String TABLE_NAME = "staff";
        public static final String COLUMN_NAME_STAFF_ID = "staff_id";
        public static final String COLUMN_NAME_STAFF_NAME = "staff_name";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";

        public static final String SQL_CREATE_STAFF = "" +
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_STAFF_ID+" int(10) NOT NULL AUTO_INCREMENT, "+
                COLUMN_NAME_STAFF_NAME+" varchar(50) CHARACTER SET utf8, "+
                COLUMN_NAME_COMPANY_ID+" int(10) NOT NULL, "+
                COLUMN_NAME_START_TIME+" timestamp NOT NULL DEFAULT current_timestamp(), "+
                COLUMN_NAME_END_TIME+" datetime DEFAULT NULL, "+
                "PRIMARY KEY (staff_id), "+
                "FOREIGN KEY (company_id), "+
                "REFERENCES company(company_id) ON DELETE CASCADE "+
                ");";

        public static final String SQL_DELETE_STAFF =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;

    }

    public static class VehicleTable implements BaseColumns{
        public static final String TABLE_NAME = "vehicle";
        public static final String COLUMN_NAME_VEHICLE_ID = "vehicle_id";
        public static final String COLUMN_NAME_PLAT_NUM = "plat_num";
        public static final String COLUMN_NAME_VEHICLE_WEIGHT = "vehicle_weight";
        public static final String COLUMN_NAME_VEHICLE_TYPE = "vehicle_type";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";

        public static final String SQL_CREATE_VEHICLE = "" +
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_VEHICLE_ID+" int(10) NOT NULL AUTO_INCREMENT, "+
                COLUMN_NAME_PLAT_NUM+" varchar(99) NOT NULL, "+
                COLUMN_NAME_VEHICLE_WEIGHT+" varchar(10), "+
                COLUMN_NAME_VEHICLE_TYPE+" varchar(99) CHARACTER SET utf8, "+
                COLUMN_NAME_COMPANY_ID+" int(10) NOT NULL, "+
                COLUMN_NAME_START_TIME+" timestamp NOT NULL DEFAULT current_timestamp(), "+
                COLUMN_NAME_END_TIME+" datetime DEFAULT NULL, "+
                "PRIMARY KEY (vehicle_id), "+
                "FOREIGN KEY (company_id), "+
                "REFERENCES company(company_id) ON DELETE CASCADE "+
                ");";

        public static final String SQL_DELETE_VEHICLE =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }
}
