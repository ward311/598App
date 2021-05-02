package com.example.homerenting_prototype_one.model;

import android.media.tv.TvContract;
import android.provider.BaseColumns;
import android.sax.EndElementListener;

public class TableContract {
    private TableContract() {}

    public static class AreaTable implements BaseColumns{
        public static final String TABLE_NAME = "area";
        public static final String COLUMN_NAME_CITY_NAME = "city_name";
        public static final String COLUMN_NAME_CITY_DISTRICT = "city_district";
        public static final String COLUMN_NAME_POSTAL_CODE = "postal_code";

        public static final String SQL_CREATE_AREA = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_CITY_NAME+" VARCHAR(11) NOT NULL, "+
                COLUMN_NAME_CITY_DISTRICT+" VARCHAR(11) NOT NULL, "+
                COLUMN_NAME_POSTAL_CODE+" VARCHAR(10) NOT NULL, "+
                "PRIMARY KEY (`postal_code`) "+");";
        public static final String SQL_DELETE_AREA =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class MemberTable implements BaseColumns{
        public static final String TABLE_NAME = "member";
        public static final String COLUMN_NAME_MEMBER_ID = "member_id";
        public static final String COLUMN_NAME_MEMBER_NAME = "member_name";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_CONTACT_ADDRESS = "contact_address";
        public static final String COLUMN_NAME_CONTACT_WAY = "contact_way";
        public static final String COLUMN_NAME_CONTACT_TIME = "contact_time";

        public static final String SQL_CREATE_MEMBER = ""+
                "CREATE TABLE IF NOT EXISTS "+" ( "+
                COLUMN_NAME_MEMBER_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_MEMBER_NAME+" VARCHAR(50), "+
                COLUMN_NAME_GENDER+" VARCHAR(10), "+
                COLUMN_NAME_PHONE+" VARCHAR(10), "+
                COLUMN_NAME_CONTACT_ADDRESS+" VARCHAR(100), "+
                COLUMN_NAME_CONTACT_WAY+" VARCHAR(100), "+
                COLUMN_NAME_CONTACT_TIME+" VARCHAR(100), "+
                "PRIMARY KEY (`member_id`) "+
                ");";
        public static final String SQL_DELETE_MEMBER =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

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
                        COLUMN_NAME_COMPANY_ID+" INTEGER(10) NOT NULL, "+
                        COLUMN_NAME_COMPANY_NAME+" VARCHAR(100), "+
                        COLUMN_NAME_IMG+" VARCHAR(99), "+
                        COLUMN_NAME_ADDRESS+" VARCHAR(50), "+
                        COLUMN_NAME_PHONE+" VARCHAR(99), "+
                        COLUMN_NAME_STAFF_NUM+" INTEGER(4) NOT NULL, "+
                        COLUMN_NAME_URL+" VARCHAR(100), "+
                        COLUMN_NAME_EMAIL+" VARCHAR(100), "+
                        COLUMN_NAME_LINE_ID+" VARCHAR(100), "+
                        COLUMN_NAME_PHILOSOPHY+" VARCHAR(500), "+
                        COLUMN_NAME_LAST_DISTRIBUTION+" FLOAT DEFAULT 0, "+
                        "PRIMARY KEY(`company_id`)"+
                ");";

        public static final String SQL_DELETE_COMPANY =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }

    public static class OrdersTable implements BaseColumns{
        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_MEMBER_ID = "member_id";
        public static final String COLUMN_NAME_ADDITIONAL = "additional";
        public static final String COLUMN_NAME_MEMO = "memo";
        public static final String COLUMN_NAME_FROM_ADDRESS = "from_address";
        public static final String COLUMN_NAME_TO_ADDRESS = "to_address";
        public static final String COLUMN_NAME_FROM_ELEVATOR = "from_elevator";
        public static final String COLUMN_NAME_TO_ELEVATOR = "to_elevator";
        public static final String COLUMN_NAME_STORAGE_SPACE = "storage_space";
        public static final String COLUMN_NAME_CARTON_NUM = "carton_num";
        public static final String COLUMN_NAME_PROGRAM = "program";
        public static final String COLUMN_NAME_ORDER_STATUS = "order_status";
        public static final String COLUMN_NAME_NEW = "new";
        public static final String COLUMN_NAME_AUTO = "auto";
        public static final String COLUMN_NAME_LAST_UPDATE = "last_update";
        public static final String COLUMN_NAME_SIGNATURE = "signature";

        public static final String SQL_CREATE_ORDERS = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+ " ( "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_MEMBER_ID+" INTEGER(10), "+
                COLUMN_NAME_ADDITIONAL+" VARCHAR(300), "+
                COLUMN_NAME_MEMO+" VARCHAR(300), "+
                COLUMN_NAME_FROM_ADDRESS+" VARCHAR(100), "+
                COLUMN_NAME_TO_ADDRESS+" VARCHAR(100), "+
                COLUMN_NAME_FROM_ELEVATOR+" BOOLEAN, "+
                COLUMN_NAME_TO_ELEVATOR+" BOOLEAN, "+
                COLUMN_NAME_STORAGE_SPACE+" VARCHAR(10), "+
                COLUMN_NAME_CARTON_NUM+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_PROGRAM+" VARCHAR(4), "+
                COLUMN_NAME_ORDER_STATUS+" enum('evaluating', 'scheduled', 'assigned','done', 'cancel', 'paid), "+
                COLUMN_NAME_NEW+" BOOLEAN DEFAULT TRUE, "+
                COLUMN_NAME_AUTO+" BOOLEAN DEFAULT TRUE, "+
                COLUMN_NAME_LAST_UPDATE+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                COLUMN_NAME_SIGNATURE+" TEXT NOT NULL, "+
                "PRIMARY KEY (`order_id`), "+
                "FOREIGN KEY (`member_id`) "+
                "REFERENCES member (`member_id`) ON DELETE SET NULL "+
                ");"+"\n"+
                "CREATE TRIGGER last_update_time_trigger "+
                "AFTER UPDATE ON "+TABLE_NAME+" FOR EACH ROW WHEN "+
                "NEW.last_update <= OLD.last_update"+
                "BEGIN \n"+
                "UPDATE "+TABLE_NAME+" SET "+COLUMN_NAME_LAST_UPDATE+
                " = CURRENT_TIMESTAMP WHERE "+COLUMN_NAME_ORDER_ID+" = "+
                "OLD."+COLUMN_NAME_ORDER_ID+";"+
                "\n END";
        public static final String SQL_DELETE_ORDERS =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }



    public static class SpecialTable implements BaseColumns{
        public static final String TABLE_NAME = "special";
        public static final String COLUMN_NAME_SPECIAL_ID = "special_id";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_NUM = "num";
        public static final String SQL_CREATE_SPECIAL = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_SPECIAL_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10), "+
                COLUMN_NAME_NAME+" VARCHAR(100), "+
                COLUMN_NAME_NUM+" INTEGER(10), "+
                "PRIMARY KEY (`special_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_SPECIAL =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
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

    public static class StaffTable implements BaseColumns{
        public static final String TABLE_NAME = "staff";
        public static final String COLUMN_NAME_STAFF_ID = "staff_id";
        public static final String COLUMN_NAME_STAFF_NAME = "staff_name";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_START_TIME= "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String SQL_CREATE_STAFF = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_STAFF_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_STAFF_NAME+" VARCHAR(50), "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10), "+
                COLUMN_NAME_START_TIME+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                COLUMN_NAME_END_TIME+" DATETIME DEFAULT NULL, "+
                "PRIMARY KEY (`staff_id`), "+
                "FOREIGN KEY (`company_id`) REFERENCES company(`company_id`) ON DELETE CASCADE "+
                ");\n"+
                "CREATE TRIGGER start_time_trigger "+
                "AFTER UPDATE ON "+TABLE_NAME+" FOR EACH ROW WHEN "+
                "NEW.start_time <= OLD.start_time"+
                "BEGIN \n"+
                "UPDATE "+TABLE_NAME+" SET "+COLUMN_NAME_START_TIME+
                " = CURRENT_TIMESTAMP WHERE "+COLUMN_NAME_STAFF_ID+" = "+
                "OLD."+COLUMN_NAME_STAFF_ID+";"+
                "\n END";
        public static final String SQL_DELETE_STAFF =
                "DROP TABLE IF NOT EXISTS "+TABLE_NAME;
    }

    public static class StaffAssignmentTable implements BaseColumns{
        public static final String TABLE_NAME = "staff_assignment";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_STAFF_ID = "staff_id";
        public static final String COLUMN_NAME_PAY = "pay";
        public static final String SQL_CREATE_STAFF_ASSIGNMENT = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10), "+
                COLUMN_NAME_STAFF_ID+" INTEGER(10), "+
                COLUMN_NAME_PAY+" INTEGER(10) DEFAULT -1, "+
                "PRIMARY KEY (`order_id`, `staff_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`staff_id`) "+
                "REFERENCES staff(`staff_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_STAFF_ASSIGNMENT =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
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
                COLUMN_NAME_VEHICLE_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_PLAT_NUM+" VARCHAR(99) NOT NULL, "+
                COLUMN_NAME_VEHICLE_WEIGHT+" VARCHAR(10), "+
                COLUMN_NAME_VEHICLE_TYPE+" VARCHAR(99) CHARACTER SET utf8, "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_START_TIME+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                COLUMN_NAME_END_TIME+" DATETIME DEFAULT NULL, "+
                "PRIMARY KEY (`vehicle_id`), "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(company_id) ON DELETE CASCADE "+
                ");";

        public static final String SQL_DELETE_VEHICLE =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }

    public static class VehicleAssignmentTable implements BaseColumns{
        public static final String TABLE_NAME = "vehicle_assignment";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_VEHICLE_ID = "vehicle_id";
        public static final String SQL_CREATE_VEHICLE_ASSIGNMENT = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10), "+
                COLUMN_NAME_VEHICLE_ID+" INTEGER(10), "+
                "PRIMARY KEY (`order_id`, `vehicle_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`vehicle_id`) "+
                "REFERENCES vehicle(`vehicle_id`) "+");";
        public static final String SQL_DELETE_VEHICLE_ASSIGNMENT =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class VehicleDemandTable implements BaseColumns{
        public static final String TABLE_NAME = "vehicle_demand";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_NUM = "num";
        public static final String COLUMN_NAME_VEHICLE_WEIGHT = "vehicle_weight";
        public static final String COLUMN_NAME_VEHICLE_TYPE = "vehicle_type";
        public static final String SQL_CREATE_VEHICLE_DEMAND = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+ " ( "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10), "+
                COLUMN_NAME_NUM+"INTEGER(10), "+
                COLUMN_NAME_VEHICLE_WEIGHT+" VARCHAR(10), "+
                COLUMN_NAME_VEHICLE_TYPE+" VARCHAR(99), "+
                "PRIMARY KEY (`order_id`, `vehicle_weight`, vehicle_type`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_VEHICLE_DEMAND =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }
    public static class FurnitureTable implements BaseColumns{
        public static final String TABLE_NAME = "furniture";
        public static final String COLUMN_NAME_FURNITURE_ID = "furniture_id";
        public static final String COLUMN_NAME_SPACE_TYPE = "space_type";
        public static final String COLUMN_NAME_FURNITURE_NAME = "furniture_name";
        public static final String COLUMN_NAME_IMG = "img";
        public static final String SQL_CREATE_FURNITURE = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_FURNITURE_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_SPACE_TYPE+ "VARCHAR(99), "+
                COLUMN_NAME_FURNITURE_NAME+" VARCHAR(99), "+
                "PRIMARY KEY (`furniture_id`) "+");";
        public static final String SQL_DELETE_FURNITURE =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class RoomTable implements BaseColumns{
        public static final String TABLE_NAME = "room";
        public static final String COLUMN_NAME_ROOM_ID = "room_id";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_FLOOR = "floor";
        public static final String COLUMN_ROOM_NAME = "room_name";
        public static final String COLUMN_ROOM_TYPE= "room_type";
        public static final String SQL_CREATE_ROOM = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ROOM_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_ORDER_ID+" INTEGER(10), "+
                COLUMN_FLOOR+" INTEGER(10), "+
                COLUMN_ROOM_NAME+" INTEGER(10), "+
                COLUMN_ROOM_TYPE+" ENUM(`房間`, 廳`, `戶外`), "+
                "PRIMARY KEY (`room_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_ROOM =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class FurnitureListAppTable implements BaseColumns{
        public static final String TABLE_NAME = "furniture_list_app";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_FURNITURE_ID = "furniture_id";
        public static final String COLUMN_NAME_NUM = "num";
        public static final String SQL_CREATE_FURNITURE_LIST = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10), "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(20), "+
                COLUMN_NAME_FURNITURE_ID+" INTEGER(10), "+
                COLUMN_NAME_NUM+" INTEGER(10), "+
                "PRIMARY KEY (`order_id`, `company_id`, `furniture_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders (`order_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(`company`), "+
                "FOREIGN KEY (`furniture_id`) "+
                "REFERENCES furniture (`furniture_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_FURNITURE_LIST =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class FurniturePositionTable implements BaseColumns {
        public static final String TABLE_NAME = "furniture_id";
        public static final String COLUMN_NAME_ROOM_ID = "room_id";
        public static final String COLUMN_NAME_FURNITURE_ID = "furniture_id";
        public static final String COLUMN_NAME_NUM = "num";
        public static final String SQL_CREATE_FURNITURE_POSITION = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COLUMN_NAME_ROOM_ID + " INTEGER(10), " +
                COLUMN_NAME_FURNITURE_ID + " INTEGER(10), " +
                COLUMN_NAME_NUM + " NUM, " +
                "PRIMARY KEY (`room_id`, `furniture_id`), " +
                "FOREIGN KEY (`room_id`) " +
                "REFERENCES room(`room_id`), " +
                "FOREIGN KEY (`furniture_id`) " +
                "REFERENCES furniture(`furniture_id`) ON DELETE CASCADE " + ");";
        public static final String SQL_DELETE_FURNITURE_POSITION =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class DiscountTable implements BaseColumns{
        public static final String TABLE_NAME = "discount";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_VALUATE = "valuate";
        public static final String COLUMN_NAME_DEPOSIT = "deposit";
        public static final String COLUMN_NAME_CANCEL = "cancel";
        public static final String COLUMN_NAME_UPDATE_TIME = "update_time";
        public static final String SQL_CREATE_DISCOUNT = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_VALUATE+" BOOLEAN DEFAULT FALSE, "+
                COLUMN_NAME_DEPOSIT+" BOOLEAN DEFAULT FALSE, "+
                COLUMN_NAME_UPDATE_TIME+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                "PRIMARY KEY (`company_id`, `update_time`), "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(`company_id`) ON DELETE CASCADE"+");\n"+
                "CREATE TRIGGER update_time_trigger "+
                        "AFTER UPDATE ON "+TABLE_NAME+" FOR EACH ROW WHEN "+
                "NEW.update_time <= OLD.update_time"+
                "BEGIN \n"+
                "UPDATE "+TABLE_NAME+" SET "+COLUMN_NAME_COMPANY_ID+
                " = CURRENT_TIMESTAMP WHERE "+COLUMN_NAME_COMPANY_ID+" = "+
                "OLD."+COLUMN_NAME_COMPANY_ID+";"+
                "\n END";
        public static final String SQL_DELETE_DISCOUNT =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class PeriodDiscountTable implements BaseColumns{
        public static String TABLE_NAME = "period_discount";
        public static final String COLUMN_NAME_DISCOUNT_ID = "discount+id";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_DISCOUNT_NAME = "discount_name";
        public static final String COLUMN_NAME_DISCOUNT = "discount";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_END_DATE = "end_date";
        public static final String COLUMN_NAME_ENABLE = "enable";
        public static final String COLUMN_NAME_IS_DELETE = "is_delete";
        public static final String COLUMN_NAME_ENABLE_TIME = "enable_time";
        public static final String COLUMN_NAME_DISABLE_TIME = "disable_time";
        public static final String SQL_CREATE_PERIOD_DISCOUNT = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_DISCOUNT_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_DISCOUNT_NAME+" VARCHAR(20), "+
                COLUMN_NAME_DISCOUNT+" FLOAT(10) NOT NULL CHECK (`discount` >=0 AND `discount` <= 5), "+
                COLUMN_NAME_START_DATE+" DATE NOT NULL, "+
                COLUMN_NAME_END_DATE+" DATE DEFAULT NULL, "+
                COLUMN_NAME_ENABLE+" BOOLEAN DEFAULT FALSE, "+
                COLUMN_NAME_IS_DELETE+" BOOLEAN DEFAULT FALSE, "+
                COLUMN_NAME_ENABLE_TIME+" DATETIME DEFAULT NULL, "+
                COLUMN_NAME_DISABLE_TIME+" DATETIME DEFAULT NULL, "+
                "PRIMARY KEY (`discount_id`), "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(`company_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_PERIOD_DISCOUNT =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class ServiceClassTable implements BaseColumns{
        public static final String TABLE_NAME = "service_class";
        public static final String COLUMN_NAME_SERVICE_ID = "service_id";
        public static final String COLUMN_NAME_SERVICE_NAME = "service_name";
        public static final String SQL_CREATE_SERVICE_CLASS = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_SERVICE_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_SERVICE_NAME+" VARCHAR(20), "+
                "PRIMARY KEY (`service_id`), "+
                "UNIQUE (`service_name`) "+");";
        public static final String SQL_DELETE_SERVICE_CLASS =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class ServiceItemTable implements BaseColumns{
        public static final String TABLE_NAME = "service_item";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_ITEM_NAME = "item_name";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "cend_time";
        public static final String COLUMN_NAME_SERVICE_ID = "service_id";
        public static final String COLUMN_NAME_IS_DELETE = "is_delete";
        public static final String SQL_CREATE_SERVICE_ITEM = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_ITEM_NAME+" VARCHAR(20), "+
                COLUMN_NAME_START_TIME+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                COLUMN_NAME_END_TIME+" DATETIME DEFAULT NULL, "+
                COLUMN_NAME_SERVICE_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_IS_DELETE+" BOOLEAN DEFAULT FALSE, "+
                "PRIMARY KEY (`company_id`, `item_name`, `start_time`), "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(`company_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`service_id`) "+
                "REFERENCES service_class(`service_id`) ON DELETE CASCADE "+");";
        public static final String DELETE_SERVICE_ITEM =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class AnnouncementTable implements BaseColumns{
        public static final String TABLE_NAME = "announcement";
        public static final String COLUMN_NAME_ANNOUNCEMENT_ID = "announcement_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_OUTLINE = "outline";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String SQL_CREATE_ANNOUNCEMENT = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ANNOUNCEMENT_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_TITLE+" VARCHAR(20), "+
                COLUMN_NAME_OUTLINE+" VARCHAR(30), "+
                COLUMN_NAME_CONTENT+"VARCHAR(300), "+
                COLUMN_NAME_DATE+" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
                "PRIMARY KEY (`announcement_id`) "+");";
        public static final String SQL_DELETE_ANNOUNCEMENT =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
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
        public static final String COLUMN_NAME_PRICE_GRADE = "price_grade";
        public static final String COLUMN_NAME_COMMENT = "comment";
        public static final String COLUMN_NAME_REPLY = "reply";

        public static final String SQL_CREATE_COMMENTS = "" +
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_COMMENT_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_MEMBER_ID+" INTEGER(10), "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10), "+
                COLUMN_NAME_COMMENT_DATE+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                COLUMN_NAME_SERVICE_QUALITY+" FLOAT(10) NOT NULL CHECK (`service_quality` >= 0 AND `service_quality` <= 5), "+
                COLUMN_NAME_WORK_ATTITUDE+" FLOAT(10) NOT NULL CHECK (`work_attitude` >= 0 AND `work_attitude` <=5), "+
                COLUMN_NAME_PRICE_GRADE+" FLOAT(10) NOT NULL CHECK (`price_grade` >= 0 AND `price_grade` <=5), "+
                COLUMN_NAME_COMMENT+" VARCHAR(300) DEFAULT NULL, "+
                COLUMN_NAME_REPLY+" VARCHAR(300) DEFAULT NULL, "+
                "PRIMARY KEY (`comment_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`member_id`) "+
                "REFERENCES member(`member_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(`company_id`) ON DELETE CASCADE "+
                ");"+"\n"+
                "CREATE TRIGGER update_time_trigger "+
                "AFTER UPDATE ON "+TABLE_NAME+" FOR EACH ROW WHEN "+
                "NEW.comment_date <= OLD.comment_date"+
                "BEGIN \n"+
                    "UPDATE "+TABLE_NAME+" SET "+COLUMN_NAME_COMMENT_DATE+
                " = CURRENT_TIMESTAMP WHERE "+COLUMN_NAME_COMMENT_ID+" = "+
                "OLD."+COLUMN_NAME_COMMENT_ID+";"+
                "\n END";

        public static final String SQL_DELETE_COMMENTS =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }


    public static class StaffLeaveTable implements BaseColumns{
        public static final String TABLE_NAME = "staff_leave";
        public static final String COLUMN_NAME_STAFF_ID = "staff_id";
        public static final String COLUMN_NAME_LEAVE_DATE = "leave_date";
        public static final String SQL_CREATE_STAFF_LEAVE = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_STAFF_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_LEAVE_DATE+" DATE NOT NULL, "+
                "PRIMARY KEY (`staff_id`, `leave_date`), "+
                "FOREIGN KEY (`staff_id`) "+
                "REFERENCES staff(`staff_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_SQL_STAFF_LEAVE =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }


}
