package com.senyoboss.model;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.*;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Model基础类，待扩展
 *
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public abstract class BaseModel<M extends Model<M>> extends com.jfinal.plugin.activerecord.Model<M> {

    protected final Logger logger = Logger.getLogger(getClass());

    private static final long serialVersionUID = 1L;
    protected static  String oracleSequenceName = null;
    protected static   String primaryKey = null;
    private Table table;
    private String tableName;
    private String modelName;

    private String selectSql;
    private String fromSql;
    private String updateSql;
    private String deleteSql;
    private String dropSql;
    private String countSql;

    protected static String blank = " ";


    /**
     * Get attribute of any mysql type
     */
    public <T> T get(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.get(attr.toLowerCase());
        else
            return super.get(attr);
    }
    /**
     * Get attribute of any mysql type. Returns defaultValue if null.
     */
    public <T> T get(String attr, Object defaultValue) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.get(attr.toLowerCase(), defaultValue);
        else
            return super.get(attr);
    }

    /**
     * Get attribute of mysql type: varchar, char, enum, set, text, tinytext, mediumtext, longtext
     */
    public  String getStr(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getStr(attr.toLowerCase());
        else
            return super.getStr(attr);
    }
    /**
     * Get attribute of mysql type: int, integer, tinyint(n) n > 1, smallint, mediumint
     */
    public Integer getInt(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getNumber(attr.toLowerCase())==null?null:super.getNumber(attr.toLowerCase()).intValue();
        else
            return super.getInt(attr);
    }
    /**
     * Get attribute of mysql type: bigint, unsign int
     */
    public Long getLong(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getNumber(attr.toLowerCase())==null?null:super.getNumber(attr.toLowerCase()).longValue();
        else
            return super.getLong(attr);
    }
    /**
     * Get attribute of mysql type: unsigned bigint
     */
    public java.math.BigInteger getBigInteger(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getBigInteger(attr.toLowerCase());
        else
            return getBigInteger(attr);
    }

    /**
     * Get attribute of mysql type: date, year
     */
    public java.util.Date getDate(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getDate(attr.toLowerCase());
        else
            return super.getDate(attr);
    }

    /**
     * Get attribute of mysql type: time
     */
    public java.sql.Time getTime(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getTime(attr.toLowerCase());
        else
            return super.getTime(attr);
    }
    /**
     * Get attribute of mysql type: timestamp, datetime
     */
    public java.sql.Timestamp getTimestamp(String attr) throws RuntimeException{
        java.sql.Timestamp re = null;
        if(DbKit.getConfig().getDialect().isOracle()){
            Object obj = super.get(attr.toLowerCase());
            if (obj == null) {
                return re;
            }
            if(obj instanceof oracle.sql.TIMESTAMP) {
                try {
                    re =  ((oracle.sql.TIMESTAMP) obj).timestampValue();
                } catch (Exception e) {
                    throw (new RuntimeException(e));
                }
            }
        }else {
            re =  super.getTimestamp(attr);
        }
        return re;
    }

    /**
     * Get attribute of mysql type: real, double
     */
    public Double getDouble(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getNumber(attr.toLowerCase()).doubleValue();
        else
            return super.getDouble(attr);
    }

    /**
     * Get attribute of mysql type: float
     */
    public Float getFloat(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getNumber(attr.toLowerCase()).floatValue();
        else
            return super.getFloat(attr);
    }
    /**
     * Get attribute of mysql type: bit, tinyint(1)
     */
    public Boolean getBoolean(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getBoolean(attr.toLowerCase());
        else
            return super.getBoolean(attr);
    }

    /**
     * Get attribute of mysql type: decimal, numeric
     */
    public java.math.BigDecimal getBigDecimal(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getBigDecimal(attr.toLowerCase());
        else
            return super.getBigDecimal(attr);
    }

    /**
     * Get attribute of mysql type: binary, varbinary, tinyblob, blob, mediumblob, longblob
     */
    public byte[] getBytes(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getBytes(attr.toLowerCase());
        else
            return super.getBytes(attr);
    }

    /**
     * Get attribute of any type that extends from Number
     */
    public Number getNumber(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getNumber(attr.toLowerCase());
        else
            return super.getNumber(attr);
    }
    public boolean save(){
        if(DbKit.getConfig().getDialect().isOracle()) {
            if (oracleSequenceName != null) {
                if (primaryKey != null) {
                    super.set(primaryKey, oracleSequenceName + ".nextval");
                }else{
                    super.set("id", oracleSequenceName + ".nextval");
                }
            }
        }
        return super.save();
    }
    public M set(String attr, Object value) {
        if(DbKit.getConfig().getDialect().isOracle()) {
            if (value instanceof java.util.Date) {
                java.util.Date dt = (java.util.Date)value;
                value = new Timestamp(((Date) value).getTime());
            }
        }
        return super.set(attr, value);
    }

    public Map<String, Object> getAttrs() {
        return super.getAttrs();
    }

    public String getJson() {
        return JsonKit.toJson(this);
    }

    public List<M> findAll() {
        return find("select * from " + getTableName() +" where del_flg = 1");
    }

    public List<M> findBy(String where, Object... paras) {
        return find(getSelectSql() + getFromSql() + getWhere(where), paras);
    }

    public List<M> findTopBy(int topNumber, String where, Object... paras) {
        return paginate(1, topNumber, getSelectSql(), getFromSql() + getWhere(where), paras).getList();
    }

    public M findFirstBy(String where, Object... paras) {
        return findFirst(getSelectSql() + getFromSql() + getWhere(where), paras);
    }

    /**
     * 保存或者更新model
     *
     * @return boolean
     */
    public boolean saveOrUpdate() {
        Table table = TableMapping.me().getTable(this.getClass());
        //获取主键
        Object pKey = this.get(table.getPrimaryKey().toString());
        if (null == pKey) {
            return this.save();
        }
        return this.update();
    }

    public Page<M> paginateAll(int pageNumber, int pageSize) {
        return paginate(pageNumber, pageSize, getSelectSql(), getFromSql());
    }

    public Page<M> paginateBy(int pageNumber, int pageSize, String where, Object... paras) {
        return paginate(pageNumber, pageSize, getSelectSql(), getFromSql() + getWhere(where), paras);
    }

    public boolean updateAll(String set, Object... paras) {
        return Db.update(getUpdateSql() + getSet(set), paras) > 0;
    }

    public boolean updateBy(String set, String where, Object... paras) {
        return Db.update(getUpdateSql() + getSet(set) + getWhere(where), paras) > 0;
    }

    public boolean deleteAll() {
        return Db.update(getDeleteSql(), new Date()) > 0;
    }

    public boolean deleteBy(String where, Object... paras) {
        Object[] realParas = new Object[paras.length + 1];
        realParas[0] = new Date();
        for (int i = 0; i < paras.length; i++) {
            realParas[i + 1] = paras[i];
        }
        return Db.update(getDeleteSql() + getWhere(where), realParas) > 0;
    }

    public boolean dropAll() {
        return Db.update(getDropSql()) > 0;
    }

    public boolean dropBy(String where, Object... paras) {
        return Db.update(getDropSql() + getWhere(where), paras) > 0;
    }

    public Long countAll() {
        return Db.queryFirst(getCountSql());
    }

    public Long countBy(String where, Object... paras) {
        return Db.queryFirst(getCountSql() + getWhere(where), paras);
    }

    protected String getSet(String set) {
        if (set != null && !set.isEmpty() && !set.trim().toUpperCase().startsWith("SET")) {
            set = " SET " + set;
        }
        return set;
    }

    protected String getWhere(String where) {
        if (where != null && !where.isEmpty() && !where.trim().toUpperCase().startsWith("WHERE")) {
            where = " WHERE " + where;
        }
        return where;
    }

    public Table getTable() {
        if (table == null) {
            Class clazz = getClass();
            table = TableMapping.me().getTable(clazz);
        }
        return table;
    }

    public String getPrimaryKey() {
        if (primaryKey == null) {
            primaryKey = getTable().getPrimaryKey().toString();
        }
        return primaryKey;
    }

    public String getTableName() {
        if (tableName == null) {
            tableName = getTable().getName();
        }
        return tableName;
    }

    public String getModelName() {
        if (modelName == null) {
            Class clazz = getClass();
            byte[] items = clazz.getSimpleName().getBytes();
            items[0] = (byte) ((char) items[0] + ('a' - 'A'));
            modelName = new String(items);
        }
        return modelName;
    }

    public String getSelectSql() {
        if (selectSql == null) {
            selectSql = " SELECT '" + getModelName() + "'.* ";
        }
        return selectSql;
    }

    public String getFromSql() {
        if (fromSql == null) {
            fromSql = " FROM " + getTableName() + " '" + getModelName() + "' ";
        }
        return fromSql;
    }

    public String getUpdateSql() {
        if (updateSql == null) {
            updateSql = " UPDATE " + getTableName() + " '" + getModelName() + "' ";
        }
        return updateSql;
    }

    public String getDeleteSql() {
        if (deleteSql == null) {
            deleteSql = " UPDATE " + getTableName() + " '" + getModelName() + "' SET '" + getModelName() + "'.del_flg=? ";
        }
        return deleteSql;
    }

    public String getDropSql() {
        if (dropSql == null) {
            dropSql = " DELETE FROM " + getTableName() + " ";
        }
        return dropSql;
    }

    public String getCountSql() {
        if (countSql == null) {
            countSql = " SELECT COUNT(*) count FROM " + getTableName() + " '" + getModelName() + "' ";
        }
        return countSql;
    }

    public String getNextSql(String where) {
        String nextSql = " WHERE '" + getModelName() + "'." + getPrimaryKey()
                + "=(SELECT MIN('_" + getModelName() + "'." + getPrimaryKey() + ") FROM " + getTableName() + " '_" + getModelName() + "'" + getWhere(where) + ")";

        return nextSql;
    }

    public String getPreviousSql(String where) {
        String previousSql = " WHERE `" + getModelName() + "`." + getPrimaryKey()
                + "=(SELECT MAX(`_" + getModelName() + "`." + getPrimaryKey() + ") FROM " + getTableName() + " `_" + getModelName() + "`" + getWhere(where) + ")";
        return previousSql;
    }
}
